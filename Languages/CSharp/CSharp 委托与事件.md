# 多播委托

委托使用 `delegate` 声明，实质上是一个继承于 `System.MulticastDelegate` 的类，从表现上类似于定义一个函数的签名。给委托的实例可以引用任何类型对象的实例或静态方法，只要签名相同

```csharp
delegate TReturn Name([params]);
Name dName = 某个函数;
dName() // 等同于 dName.Invoke();
```

C# 默认提供了 `Action` 委托与 `Func` 委托：
- `Action<T, T2, T3, ...>` 对应 `void (T, T2, T3, ...)`
- `Func<T, T2, T3, ..., TResult>` 对应 `TResult (T, T2, T3, ...)`

同一委托可以执行多个操作，使用 `+/+=`，`-/-=` 向一个委托添加或删除其他操作。
- 多播委托是一个委托的链表，具体顺序未定义，应避免依赖委托的执行顺序
- 一个委托抛出异常，其后的其他委托均无法调用

```csharp
void func(Func<double, double, double> aDelegate)
{
    operation += aDelegate;
    operation += delegate (double a, double b) { return a; };
}
```
# 事件

事件使用 `event` 声明，一个完整的事件及处理程序通常包含以下几部分：

- 具体事件对象，包含事件内存储的具体数据，继承自 `EventArgs` 类

```c#
public class CarInfoEventArgs : EventArgs
{
    public string Car { get; }

    public CarInfoEventArgs(string car)
    {
        Car = car;
    }
}
```

- 事件处理方法，提供事件响应的方法

```c#
internal class Consumer
{
    private string v;

    public Consumer(string v)
    {
        this.v = v;
    }

    internal void NewCarHere(object sender, CarInfoEventArgs e)
    {
        Console.WriteLine($"{v}: car {e.Car} is now");
    }
}
```

- 事件管理器，实际使用事件的地方

```csharp
public class CarDealer
{
    private EventHandler<CarInfoEventArgs> newCarInfo;
    public event EventHandler<CarInfoEventArgs> NewCarInfo
    {
        add { newCarInfo += value; }
        remove { newCarInfo -= value; }
    }

    public void NewCar(string car)
    {
        Console.WriteLine($"CarDealer, new car {car}");
        newCarInfo?.Invoke(this, new CarInfoEventArgs(car));
    }
}
```

之后就可以使用了

```c#
// 创建事件管理器
var dealer = new CarDealer();

var daniel = new Consumer("Daniel");
// 注册事件处理器
dealer.NewCarInfo += daniel.NewCarHere;
// 发送事件
dealer.NewCar("Mercedes");

var sebastian = new Consumer("sebastian");
// 注册事件处理器
dealer.NewCarInfo += sebastian.NewCarHere;
// 发送事件
dealer.NewCar("Ferrari");

// 注销事件处理器
dealer.NewCarInfo -= sebastian.NewCarHere;
// 发送事件
dealer.NewCar("Red Bull Racing");
```