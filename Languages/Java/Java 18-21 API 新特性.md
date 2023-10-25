# Java 18

- 虚拟线程：[[#Java 21 LTS#Virtual Threads]]
- VectorAPI：[[#Java 21 LTS#Vector API]]
- 新增一个新的 SPI 用于地址解析，以便 `InetAddress` 可以使用平台外的三方解析器
# Java 19

- 外部函数和内存 API：[[#Java 21 LTS#Foreign Function & Memory API]]
- 结构化并发：[[#Java 21 LTS#Structured Concurrency]]
# Java 20

- 虚拟线程：[[#Java 21 LTS#Virtual Threads]]
- 作用域值：[[#Java 21 LTS#Scoped Values]]
- 结构化并发：[[#Java 21 LTS#Structured Concurrency]]
- FFM：[[#Java 21 LTS#Foreign Function & Memory API]]
- VectorAPI：[[#Java 21 LTS#Vector API]]
# Java 21 LTS
## Virtual Threads

Loom 子项目，JEP444

> [!note]- 平台线程
> 运行于底层操作系统线程，并在代码整个生命周期捕获操作系统线程

> [!note]- 虚拟线程
> 运行于底层操作系统线程，但不会在整个生命周期捕获操作系统线程，因此多个虚拟线程可以共享一个平台线程

Virtual Thread，由 JVM 而非操作系统实现的轻量级线程，多虚拟线程共享一个线程
- 避免上下文切换的额外开销
- 简化并发程序的复杂性
- 可共享平台线程，因此可以产生远比普通线程更多的线程

虚拟线程可以通过 `Thread.ofVirtual().name("name").unstarted(runnable)` 创建，或通过 `Thread.startVirtualThread(runnable)` 快速创建并运行

虚拟线程创建后，对象类型仍为 `Thread` 对象，其余 API 使用方法与普通线程类似
- `isVirtual()`：线程是否为虚拟线程
- `Executors.newVirtualThreadPerTaskExecutor()`：创建一个线程池，线程池创建的线程都是虚拟线程

**注意：**
- 虚拟线程旨在增大系统并发量，对于高度依赖于 CPU 的工作并不会提升性能
- 虚拟线程支持 `ThreadLocal`，且虚拟线程之间，虚拟线程与平台线程之间的对象是互相隔离的
- 虚拟线程不应池化，因为虚拟线程非常轻量
- `synchronized` 会使虚拟线程被固定在平台线程上，应使用 `ReentrantLock` 替换
## Scoped Values
#实验性功能 

Loom 子项目，JEP446，属于虚拟线程的 `ThreadLocal`，但是不可变的。 也可以用于解决全局或部分上下文

```java
import jdk.incubator.concurrent.ScopedValue;

public class Main {
    public static void main(String[] args) {

        ScopedValue<String> scopedValue = ScopedValue.newInstance();
        ScopedValue
                // 作用域范围值和要绑定的值
                .where(scopedValue, "value")
                // 绑定值后执行
                .run(() -> {
                    String v = scopedValue.get();
                    System.out.println(v); // value
                });
    }
}
```
## Structured Concurrency

Loom 子项目，JEP453，当使用多个并发时，有时候很难处理多个任务的异常并取消其他任务

```java
Future<String> user = executor.submit(() -> {
    // do something
    return "This is user";
});
Future<Integer> order = executor.submit(() -> {
    // do something
    return 0;
});
// join
user.get();
order.get();
```

当 `user` 获取失败时，无法取消 `order` 任务

```java
import jdk.incubator.concurrent.StructuredTaskScope;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            Future<Object> task1 = scope.fork(() -> null);
            Future<Object> task2 = scope.fork(() -> null);
            Future<Object> task3 = scope.fork(() -> null);
            scope.join();
            scope.throwIfFailed();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
```

结构化并发中，`StructuredTaskScope` 允许将一批子任务作为一个单元调度
## Foreign Function & Memory API
#实验性功能 

Pamana 子项目，JEP442，与 Java 运行时之外的代码和数据进行互操作，高效调用外部函数，安全访问外部内存，调用本机库并处理本机数据，而不会像 JNI 那样危险和脆弱。
- 分配外部内存 ：`MemorySegment` 和 `SegmentAllocator`
- 操作和访问结构化的外部内存： `MemoryLayout`，`VarHandle`
- 控制外部内存的分配和释放：`Arena` 与 `SegmentScope`
- 调用外部函数：`Linker`、`FunctionDescriptor` 和 `SymbolLookup`

```java
// 初始化环境
Linker linker = Linker.nativeLinker();
// or SymbolLookup.loaderLookup();
SymbolLookup stdlib = linker.defaultLookup();
// 查找函数
// void radixsort(char* s[], int length)
MemorySegment segment = stdlib.find("radixsort").orElseThrow();
FunctionDescriptor desc = FunctionDescriptor.ofVoid(  
        MemoryLayout.paddingLayout(ValueLayout.ADDRESS.byteSize()),  
        MemoryLayout.paddingLayout(ValueLayout.JAVA_INT.byteSize())  
);
MethodHandle radixSort = linker.downcallHandle(segment, desc);
// 分配内存
String[] strs = {"mouse", "cat", "dog", "car"};
try (Arena offHeap = Arena.openConfined()) {
    MemorySegment pointers = offHeap.allocateArray(ValueLayout.ADDRESS, strs.length);
    for (int i = 0; i < strs.length; i++) {
        MemorySegment cstr = offHeap.allocateUtf8String(strs[i]);
        pointers.setAtIndex(ValueLayout.ADDRESS, i, cstr);
    }
    // 调用函数
    radixSort.invoke(pointers, strs.length, MemorySegment.NULL, '\0');
    // 将结果复制回 JVM 堆
    for (int i = 0; i < strs.length; i++) {
        MemorySegment cstr = pointers.getAtIndex(ValueLayout.ADDRESS, i);
        strs[i] = cstr.getUtf8String(0);
    }
} catch (Throwable e) {
    throw new RuntimeException(e);
}
```
- `Linker`：提供 Java 代码与外部函数之间互相访问的功能
	- `downcall` 允许 Java 链接到外部函数
	- `upcall` 允许外部函数调用 Java 方法
- `SymbolLookup`：检索一个或多个库中的符号地址
- `Arena`：竞技场范围，控制内存段的生命周期，实现了 `AutoClosable` 且关闭后不可再访问
- `MemorySegment`：提供对连续内存区域的访问
	- `heap segments`：分配于 Java 堆中
	- `native segments`：分配于 Java 堆外
- `ValueLayout`：对基本数据类型进行建模，且对 Java 原始类型和地址定义了有用的布局常量
## Vector API
#实验性功能 

Pamana 子项目，JEP448，包含针对向量计算的一系列操作，通过对编译后的 CPU 指令优化以达到超过标量计算的性能

```java
void scalarComputation(float[] a, float[] b, float[] c) {
   for (int i = 0; i < a.length; i++) {
        c[i] = (a[i] * a[i] + b[i] * b[i]) * -1.0f;
   }
}

static final VectorSpecies<Float> SPECIES = FloatVector.SPECIES_PREFERRED;

void vectorComputation(float[] a, float[] b, float[] c) {
    int i = 0;
    int upperBound = SPECIES.loopBound(a.length);
    for (; i < upperBound; i += SPECIES.length()) {
        // FloatVector va, vb, vc;
        var va = FloatVector.fromArray(SPECIES, a, i);
        var vb = FloatVector.fromArray(SPECIES, b, i);
        var vc = va.mul(va)
                   .add(vb.mul(vb))
                   .neg();
        vc.intoArray(c, i);
    }
    for (; i < a.length; i++) {
        c[i] = (a[i] * a[i] + b[i] * b[i]) * -1.0f;
    }
}
```

以上代码等效于 

```java
for (int i = 0; i < a.length; i++) {  
    c[i] = (a[i] * a[i] + b[i] * b[i]) * -1.0f;  
}
```
## 其他

- 顺序集合接口：`SequencedCollection` 系列接口，表示集合中的元素会按顺序出现，包括 Set，List，Map 等
