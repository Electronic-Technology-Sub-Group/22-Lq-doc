类使用 `class` 声明，通过引用传递。

```csharp
class ClassName {
    ...
}
```

类中可以包含各种属性和函数，表示类的行为，详见 [[CSharp 属性]]，[[CSharp 函数]]。静态成员属于类. 实例成员属于对象。
# 构造函数

实例化对象时自动调用的特殊函数，与类同名，无返回值。没有提供任何构造时，编译器自动生成一个无参构造。

- 构造函数可声明为 `private` / `protected`

```csharp
class  ClassName 
{
    private ClassName() { ... }
}
```

- 静态构造：不能预计什么时候执行，但可保证在引用该类时有且仅有一次调用。
   
```csharp
class MyClass {
    // 隐式调用，不能有任何参数，无视任何修饰符
    static MyClass() { ... }
}
```

- 在一个构造中调用其他构造：使用 `this()`；调用基类构造，使用 `base()`
   
```csharp
class MyClass: MyClassParent {
    MyClass(int p1, int p2, int p3): base() { ... }
    MyClass(int p1, int p2): this(p1, p2, 3) { ... }
    MyClass(int p1): this(p1, 2, 3) { ... }
    MyClass(): this(1, 2, 3) { ... }
}
```
# 析构函数

CLR 检测到不再需要某个对象时调用，不能预测何时析构

```csharp
class MyClass {
    NyClass() { ... }     // 构造函数
    ~MyClass() { ... }    // 析构函数
}
```

析构函数在 .Net 中统称终结器，在 IL 中映射为 `Finalize()` 方法。注意实现终结器对性能会有显著影响。
# 匿名类

由编译器生成的类，直接继承 Object，无名称，直接使用 `new { ... }` 创建 
- 所有属性相同时, 两个匿名类生成的对象类型相同
- 匿名类不能进行任何反射

```csharp
var objectName = new {
    Field1 = Value1,
    Field2 = Value2,
    Field3 = Value3,
    ...
};
```
# 部分类

部分类：允许将类，结构，方法，接口等放在多个文件中，每个文件仅包含其一部分内容，使用 `partial` 修饰

- 部分类会自动合并属性，XML注释，接口，泛型类型的参数属性，成员等部分
- 所有部分必须拥有相同的修饰符，包括 `public` 等访问修饰符，`internal`、`abstract`、`sealed`、`new` 等一般约束修饰符
- 部分类可以包含部分方法。部分方法即方法的声明和实现不在一起，但其返回值必须是 `void` 的

```csharp
// in SampleClass1.cs
partial class SampleClass {
    public void Method1() {
        APartialMethod();
    }

    public partial void APartialMethod();
}

// in SampleClass2.cs
partial class SampleClass: IOtherSampleClass {
    public void APartialMethod() {
        // implementation
    }
}
```
# 继承

类与结构体都可以继承，使用 `:` 继承。同时继承自类和接口时，类必须在最前面，且不支持多继承
- 类：每个类只能派生自一个基类；但可以实现多个接口
- 结构体：结构体只能实现接口，不能继承其他基类或结构体
- 访问基类成员使用 `base`

```csharp
class MyClass : MyBaseClass, IMyInterface1, IMyInterface2 {
    ...
}
```

基类方法或属性使用 `virtual` 声明，在子类中重写该方法时使用 `override` 修饰

```csharp
class MyBaseClass {
    public virtual void MyMethod() {
        Console.WriteLine("Method in MyBaseClass");
    }
}

class MyClass: MyBaseClass {
    public override void MyMethod() {
        base.MyMothod()    // 调用父类的方法
        Console.WriteLine("Method in MyClass");
    }
}
```

当基类与子类有同样的方法，且基类没有用 `virtual` 声明，子类也没有用 `override` 声明时，可用 new 声明隐藏基类方法

**隐藏方法多用于解决继承于其他第三方类的子类的版本冲突，不应故意用于隐藏基类成员**

```csharp
class MyBaseClass {
    public void MyMethod() {
        Console.WriteLine("Method in MyBaseClass");
    }
}

class MyClass: MyBaseClass {
    new public void MyMethod() {
        base.MyMothod()    // 调用父类的方法
        Console.WriteLine("Method in MyClass");
    }
}
```
# 抽象类

将类和方法声明为 abstract，则类为抽象类，抽象类不能被实例化，抽象方法不能直接实现，只能实现抽象类后重写。

含有抽象方法的类必须声明为抽象类。

```csharp
abstract class MyBaseClass {
    public abstract void MyMethod1();
    public abstract void MyMethod2();
}

abstract class MyClass: MyBaseClass {
    public override void MyMethod1() {
        Console.WriteLine("Method in MyClass");
    }

    public override void MyMethod2() {
        // 可抛出该异常，作为临时实现
        throw new NotImplementedException();
    }
}
```
# 修饰符

注意：若存在嵌套类型，内部类型可访问外部类型的所有成员，即 InnerClass 中的代码可访问所有 OuterClass 成员，包括 private 成员

|  修饰符   |           应用           |                                       说明                                       |
|:---------:|:------------------------:|:--------------------------------------------------------------------------------:|
|  public   |      所有类型及成员      |                         逻辑访问修饰符，任何代码均可访问                         |
| protected | 类型和内嵌类型的所有成员 |                          逻辑访问修饰符，仅派生类可访问                          |
| internal  |      所有类型及成员      |                        物理访问修饰符，所在程序集内可访问                        |
|  private  | 类型和内嵌类型的所有成员 |                      逻辑访问修饰符，仅限所属类型内部可访问                      |
|    new    |         函数成员         |                             相同签名时隐藏继承的成员                             |
|  static   |       所有类型成员       |                作为类成员而非对象成员；修饰类时表示该类无法实例化                |
|  virtual  |         函数成员         |                                  可由派生类重写                                  |
| abstract  |         函数成员         |                         定义了成员签名，但没实现具体代码                         |
| override  |         函数成员         |                             重写继承的抽象类或虚拟类                             |
|  sealed   |       类 方法 属性       | 类: 不能派生子类；<br />方法, 属性: 成员重写已继承虚拟成员, 其派生类无法继续重写 |
|  extern   |   静态[DLLImport]方法    |                            成员在外部用另一种语言实现                            |
|  unsafe   |         任何成员         |                         允许运行非安全代码，允许使用指针                         |
