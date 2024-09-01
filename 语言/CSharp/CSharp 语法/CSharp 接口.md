定义某种行为的外观，使用 `interface` 声明

```csharp
public interface IInterface1 {
    void MethodInInterface1(int i);
}
public interface IInterface2: IInterface1 {
    int ValueInInterface2 {get;set;}
}
public class MyClass: IInterface2 {
    private int _value { get; set; }

    public void MethodInInterface1(int i) {
        Console.WriteLine(i);
    }

    int ValueInInterface2 => _value;
}
```

- 声明接口语法与抽象类完全相同，但不允许任有何成员的实现方式
	- 一般只包含方法，属性，索引器，事件声明
- 不能有构造和字段，不允许包含运算符重载
- 不允许声明成员修饰符，接口成员总是隐式 public（可由实现类声明），不能声明为 virtual
- 接口可派生自其他接口
