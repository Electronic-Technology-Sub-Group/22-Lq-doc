# 内存模型

- 虚拟地址空间：Windows 的虚拟寻址系统把程序可用的内存地址映射到硬件内存中的实际地址上，实际上包含了程序的所有部分，包括可执行代码，加载的 DLL，所有变量，这些内存称为虚拟地址空间，或虚拟内存，简称内存

- 栈：存储不是对象类型的值数据类型和方法传递的参数副本，由上到下填充，程序开始时栈指针指向栈底，从高内存地址向低内存地址填充。释放变量时，顺序与给他们分配内存的顺序相反。

- 托管堆：处理器可用内存的一个内存区域，简称堆，在垃圾回收器控制下工作。

尽管栈有非常高的性能，堆内存比栈性能差一些，但某些变量生存期需要嵌套，希望使用一个方法分配内存存储数据，并在该方法退出后仍保证数据可用，如所有以 new 运算符请求分配的存储空间

```c#
// 声明一个 Customer 引用: 在栈中为 arabel 引用分配空间(非实际对象), 占用 4 字节空间, 并将 arabel 的值设置为分配给 Customer 对象的内存空间(即使还没有调用构造函数创建)
// 为找到存储对象的位置, .Net 运行库会在堆中搜索并选取第一个未使用且包含 n 字节(n=sizeof(Customer))的连续块为 arabel 引用在栈中的空间赋值
Customer arabel;
// 实例化该引用
arabel = new Customer();
Customer otherCustomers = new EnhancedCustomer();
```
# 垃圾回收

垃圾回收期运行时，会在引用根表找到所有引用对象，并在引用的对象树中查找堆中不再引用的所有对象并删除

删除堆中对象后，堆会立即把对象分散起来，与已释放的内存混合在一起。一般情况下，垃圾回收会在需要时候运行，但也可以手动调用：

```csharp
System.GC.Collect();
```

- 托管堆：托管堆会在 GC 后将剩余对象移至堆顶部，因此读取对象时不需要遍历寻找，只需要读取内存地址就好，放置新堆也不需要遍历链表
- 大对象堆：当对象大小超过 85000 字节时，会放在大对象堆上而不是主堆上，压缩大对象代价比较大，因此大对象堆的对象不会进行压缩
## 分代回收

刚创建对象放在第 0 代堆上，驻留了最新对象；新对象分配内存空间时，超出第 0 代内存或调用 `Collect()` 方法，会进行垃圾回收
- 第一次垃圾回收，仍保留的对象会被压缩，移至堆得下一部分或世代部分 -- 第 1 代部分
- 第二次垃圾回收，第 1 代保留的对象移至第 2 代，第 0 代保留对象移至第 1 代
- 第二次垃圾回收后，第 2 代和大对象堆上的回收放在后台线程上进行，以减少总停顿时间，服务器和工作站默认开启此功能
## 回收方式

专用于服务器垃圾回收，设置垃圾回收方式，由 GCLatencyMode 设置。

每个逻辑服务器都有一个垃圾回收堆，当一个触发，平衡其他几个垃圾回收器的小对象堆和大对象堆，减少不必要的回收。

|    GCLatencyMode    |                               说明                               |
|:-------------------:|:----------------------------------------------------------------:|
|        Batch        |        禁用并发，垃圾回收设置为最大吞吐量，会重写配置设置        |
|     Interactive     |                工作站默认。使用并发平衡吞吐量响应                |
|     LowLatency      | 保守。只有系统存在内存压力时才进行完整回收，用于短期执行特定操作 |
| SustainedLowLatency |           只有系统存在内存压力时才进行完整的内存块回收           |
|     NoGCRegion      |      (.Net 4.6) 只读，设定一块区域，使用该区域期间不运行 GC      | 

NoGCRegion 使用方法如下：

```csharp
GC.Collect();
GC.TryStartNoGCRegion(1024);
// No GC
GC.EndNoGCRegion();
```
# 引用

## 强引用

在根表中引用，GC 不会回收这些对象。GC 可以正常回收循环引用的情况，超出作用域或指定为 null 则释放内存

除对象相互引用而未在根表中引用，垃圾回收不会回收存在强引用的对象
## 弱引用

使用 WeakReference 创建和使用对象，若 GC 工作，会回收对象并释放内存，因此应使用 IsAlive 判断是否持有对象。

```ad-note
弱引用一般不用，对小对象也没有意义，弱引用的开销可能比持有小对象更大。
```

```csharp
var myWeakRefrence = new WeakRefrence(new DataObject());
if (myWeakRefrence.IsAlive) {
    DataObject data = myWeakRefrence.Target as DataObject;
    if (data != null) {
        // 此时 GC 不会回收，因为有强引用 data
        // do something
    }
}
```

事件很容易错过引用的清理，因此也可用弱引用。
# 非托管资源

GC 无法回收非托管资源，如文件句柄，网络连接和数据库连接等。处理非托管资源通常有如下方法
- 声明析构函数或终结器用于释放资源
- 类中实现 `System.IDispose` 接口
## 析构函数

析构函数会在 GC 前调用。
- 由于 GC 发生时间的不确定性，释放资源时间不确定
- 实现终结器、频繁进行 GC 都会显著影响性能

```csharp
// in C#
class MyClass {
    ~MyClass() {
        // Finalizer impletentation
    }
}

// in IL
protected override void Finalizer() {
    try {
        // Finalizer impletentation
    } finally {
        base.Finalizer();
    }
}
```
## IDisposable

显式的释放非托管资源接口，重写 `void Dispose()` 方法中释放资源。

```csharp
IDispose theInstance = null;
try {
    theInstance = new InputStream();
    // do something
} finally {
    theInstance?.Dispose();
}
```

也可以使用 `using` 在调用完成后或出现异常自动关闭。

```csharp
using (InputStream inputStream = new InputStream()) {
    // do something
}
```
## 同时实现

```csharp
public class ResourceHolder: IDisposable {
    private bool _isDisposed = false;

    public void Dispose() {
        // 手动调用 清理一切托管和非托管资源
        Dispose(true);
        // 通知 GC，该对象不需要调用析构函数了
        GC.SuppressFinallize(this);
    }

    protected virtual void Dispose(bool disposing) {
        if (!_isDisposed) {
            if (disposing) {
                
                // cleanup managed objects by using Dispose() methods
                
            }
            
            // cleanup unmanaged objects
            
            _isDisposed = true;
        }
    }

    ~ResourceHolder() {
        // 直接调用析构函数，由 GC 调用，此时由于不能确定非托管资源状态，清理所有非托管资源
        // 而托管资源一般可由 .Net 自行释放
        Dispose(false);
    }
}
```
