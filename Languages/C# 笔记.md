# 基本结构

```csharp
using System;                          // 引用命名空间, 类似 Java 的 import
namespace XXX                         // 当前类的命名空间, 类似 Java 的 package
{
    class ClassName                     // 类
    {
        static void Main() { ... }      // Main 方法, C# 的入口方法
        ...                             // 其他字段, 方法 属性等
    }
}
```

# 类型

## 变量与常量

### 变量

```csharp
// 声明: 类型 变量名;   
int a, b;    // a, b 均为 int
int c = 5;   // 声明并赋值

// 自动推断: 必须给定初始化值
var d = 18;
a.GetType(); // System.Int32, 即 int 类型
```

### 常量

```csharp
// 常量使用 const 声明, 可省略 static
// 常量在编译时可取, 因此不能以变量初始化常量
const int a = 100;
```

## 值类型

> 储存于堆栈 stack 中，直接存储值

| C# 类型       | .Net 类型        |
|:-----------:|:--------------:|
| sbyte (有符号) | System.SByte   |
| short       | System.Int16   |
| int         | System.Int32   |
| long        | System.Int64   |
| byte (无符号)  | System.Byte    |
| ushort      | System.UInt16  |
| uint        | System.UInt32  |
| ulong       | System.UInt64  |
| float       | System.Single  |
| double      | System.Double  |
| decimal     | System.Decimal |
| bool        | System.Boolean |
| char        | System.Char    |

```csharp
// 1 整形可以直接赋值十六进制，但需要以 0x 开头
int a = 0xFF;
// 2 对 int uint long ulong 赋值，没有特殊标记，均赋值为 int 类型
int a = 10;
uint b = 10U;
long c = 10L;
ulong d = 10UL;
// 3 硬编码的浮点数，默认为 double，可用 F 指定 float
var a = 1.23;    // double
var b = 1.23F;    // float
// 4 decimal 提供较大精度计算，用于财务计算；使用 M 标记
//   但 decimal 不是基本类型，使用会造成性能损失
decimal d = 1.23M;
// 5 bool 与 int，char 与 byte 均不能隐式自动转化
// 6 char 初始化: '', (char) 强转, 十六进制, Unicode, 转义字符
char a = 'A', b = (char)65, c = '\x0041', d = '\u0041', e = '\n';
```

### 可空类型

1. 正常情况下，只有引用类型可为 null，而值类型不能为 null

2. 可空类型：`XXX?` (int?, double?, char? 等，包括任意结构体类型)

3. 值类型可隐式转换为可空类型，但可空类型不能隐式转换为值类型

4. 可以使用能用于基本类型的所有操作符

5. 操作符
   
   ```csharp
   int? a = 0;
   int? b = null;
   
   a.HasValue;            // true
   b.HasValue;            // false
   int c = a ?? -1;    // 0
   int d = b ?? -1;    // -1
   ```

### 结构体

> 值类型，存于栈中或内联（另一个堆的一部分）；但使用方法和类相同

```csharp
public struct MyStruct {
    public var Field1 = "field1";
    public var Field2 = "field2";
}
var a1 = new MyStrict();    // 可以像初始化类一样被初始化
MyStruct a2;                // 可以像其他值类型的方式初始化
```

1. 堆内存， 创建删除快，但传参会将自身复制，影响性能（可用 ref 传递）
2. 不支持继承（实际上继承自 `System.ValueType`），可提供 `Close()` 或 `Dispose()` 方法，用作小数据结构
3. 没有提供默认构造时，默认构造会初始化每个量的默认值

## 引用类型

> 储存于托管堆 managed heap 中
> 
> 存储对值的引用

| C# 类型  | .Net 类型       |
|:------:|:-------------:|
| object | System.Object |
| string | System.String |

```csharp
// 1 值类型的父类也是 object
// 2 String 的三种形式
var a = 0;
string s1 = "a: {a}\\int";    // a: {a}\int    不会自动引用，会处理转义字符
string s2 = @"a: {a}\\int";    // a: {a}\\int    不会自动引用，不会处理转义字符
string s3 = $"a: {a}\\int";    // a: 0\int        会自动引用，会处理转义字符，C# 6 开始支持
```

### 类

```csharp
class ClassName {
    ...
}
```

#### 字段

>  类的数据成员, 是类型的一个变量, 该类型是类的一个成员

#### 属性

> 可以从客户端访问的函数组

```csharp
// 手动实现 getter / setter
private string _field[ = "default value"];    // 字段
public string Field                            // 属性
{
    get { return _field; }
    set { _field = value; }
}
// 自动实现
public string Field { get; set; }[ = "default value";]
```

##### 访问修饰符

- 允许 get 和 set 使用不同的修饰符
- C# 6：可只实现 get 作为只读属性

#### 只读成员

> 初始化后无法修改，只读成员则为正常的属性，而常量 编译器会将常量值代替程序中的引用位置

实现：只实现 get 方法，或使用 readonly 修饰

#### 方法

> 与特定类相关的函数

```csharp
[modifiers] return_type MethodName([parameters])
{
    // Method body
}
```

1. 表达式体方法：若方法实现只有一个语句，可简化为 lambda 操作符
   
   ```csharp
   [modifiers] return_type MethodName([parameters]) => Value;
   ```

2. 方法调用
   
   ```csharp
   public int Method(int arg1, bool arg2, string arg3 = "aaa", object arg4 = new object(), params int[] data)
   // 直接调用
   int ret = Method(0, true, "aaa", new object());
   // 命名参数：可带有变量名，此时变量顺序可变
   int ret = Method(arg1: 0, arg3: "aaa", arg2: true, arg4: new object());
   // 可选参数：声明中带有默认值得参数，调用时可省略
   int ret = Method(0, true);
   // 个数可变参数：声明中带有 params 的数组参数，可直接传入数量不等的参数；只能有一个，且为最后一个参数
   int ret = Method(0, true, "aaa", new object(), 0, 1 , 2, 3);
   ```

##### 函数传参的两种特殊传递方式

1. ref：传递当前量的引用
   
    1. 值类型：传入的是当前量的引用；因此，该值类型在函数内改变，函数外同样会变
       
       ```csharp
       struct A {
           public int X { get; set; }
       }
       void method(ref A a) {
           a.X = 2;
           a = new A { X = 3 };
       }
       A a1 = new A { X = 1 };
       method(ref a1);
       Console.WriteLine(a1.X);    // 3: a 是对 a1 的引用，a1 就是 A的对象
       ```
   
    2. 引用类型：传入的是引用的引用；因此，该引用内容在函数内改变，函数外不变
       
       ```csharp
       class A {
           public int X { get; set; }
       }
       void method(ref A a) {
           a.X = 2;
           a = new A { X = 3 };
       }
       A a1 = new A { X = 1 };
       method(ref a1);
       Console.WriteLine(a1.X);    // 2: a 是对 a1 的引用，a1 是对 A的对象 的引用
       ```

2. out：使用前必须赋值，相当于多了一个输出
   
   ```csharp
   void outMethod(out int a, out object b) {
       // Console.WriteLine($"{a}, {b}"); --> out 必须在使用前对其复制
       a = 1;
       b = new object();
       Console.WriteLine($"{a}, {b}");
   }
   
   int a = 0;
   object b = new { name = "b" };
   Console.WriteLine($"{a}, {b}");
   outMethod(out a, out b);
   Console.WriteLine($"{a}, {b}");
   // Console:
   //    0, { name = b }
   //  1, System.Object
   //  1, System.Object
   ```

##### 扩展方法

> 在类不能被继承（如密封类）时给对象添加功能的方法
> 
> 扩展方法也可应用于对接口的扩展

```csharp
public static class StringExtension {
    // 第一个参数必须是 this 修饰，指代调用此方法的对象；
    // 即使被定义为 static 方法，仍要通过实例对象调用
    // 有多个相同定义时，同包优先
    public static int GetWordCount(this string s) => s.Splite().Length;
}
// 使用:
string s = "a string extension method has been called";
s.GetWordCount();    // 编译器会转化为: StringExtension.GetWordCount(s);
```

##### Main 方法

> C# 程序的入口函数，可在任意类中
> 
> 使用 static 修饰，返回 int 或 void；无参 或 参数为 string[]
> 
> 与访问级别无关，private 也能运行 

#### 构造函数

实例化对象时自动调用的特殊函数, 与类同名, 无返回值. 用于初始化字段的值

```csharp
class  ClassName 
{
    public ClassName() { ... }
}
```

1. 没有提供任何构造时，编译器自动生成一个无参构造；但若自定义一个带参构造，编译器不会提供无参构造

2. 构造函数可声明为 private / protected

3. 静态构造：不能预计按什么顺序执行，但可保证在引用之前调用一次，且仅调用一次。
   
   ```csharp
   class MyClass {
       // 隐式调用，不能有任何参数，无视任何修饰符
       static MyClass() { ... }
   }
   ```

4. 在一个构造中调用其他构造：使用 this()；调用基类构造，使用 base()
   
   ```csharp
   class MyClass: MyClassParent {
       MyClass(int p1, int p2, int p3): base() { ... }
       MyClass(int p1, int p2): this(p1, p2, 3) { ... }
       MyClass(int p1): this(p1, 2, 3) { ... }
       MyClass(): this(1, 2, 3) { ... }
   }
   ```

#### 索引器

允许对象用访问数组的方式访问

```csharp
class IndexClass {
    // 索引器方法
    public string this[int index]
    {
        get {
            // ...
        }
        
        set {
            // ...
        }
    }
    // 索引器方法可以重载
    public int this[string name, int index]
    {
        get {
            // ...
        }
        
        set {
            // ...
        }
    }
}
```

#### 运算符重载

使用 `operator` 重载

```csharp
Class AClass {}

public AClass operator +(AClass a, AClass b) => a;
```

#### 事件

类的成员，发生某些行为时，通知对象调用，详见 12 委托与事件

#### 析构函数

CLR 检测到不再需要某个对象时调用
 
不能预测何时析构

```csharp
class MyClass {
    NyClass() { ... }     // 构造函数
    ~MyClass() { ... }    // 析构函数
}
```

### 匿名类

> 由编译器生成. 直接继承 Object, 无名称的类型

```csharp
// 所有属性都相同时，两个匿名类对象的类型是相同的
// 匿名类不能进行任何反射
var objectName = new {
    Field1 = Value1,
    Field2 = Value2,
    Field3 = Value3,
    ...
};
```

### 枚举

用户定义的一组整型类

```csharp
public enum MyEnum : int/short/long 等整型，默认 int
{
    A;        // 0
    B;        // 1
    C = 3;    // 3
}

// 转化
(int) MyEnum.A    // 0
(MyEnum) 1        // B

// 为一个变量指定多个值
enum Days 
{
    Monday = 0x1;
    Tuesday = 0x2;
    Wednesday = 0x4;
    Thursday = 0x8;
    Friday = 0x10;
    Saturday = 0x20;
    Sunday = 0x40;
    // 也可以结合单个位设置不同值
    Weekend = Saturday | Sunday;
    Workday = 0x1f;
    AllWeek = Workday | Weekend;
}
Days day = Days.Monday | Days.Tuesday
Console.WriteLine(day)    // Monday, Tuesday
```

### 部分类

部分类：允许将类，结构，方法，接口等放在多个文件中，每个文件仅包含其一部分内容

部分类在 class 前使用 partial 修饰

有使用 public/private/protected/internal/abstract/sealed/new/一般约束 的，必须应用在同一类的所有部分

部分类会自动合并属性，XML注释，接口，泛型类型的参数属性，成员

部分类可以包含部分方法，部分方法的实现可以在任意文件，但部分方法必须是 void 类型的

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

### Object

`Object` 为所有类的基类

#### ToString

获取对象字符串的一种快捷方式

要实现更多选择，需实现 `IFormattable` 接口

#### GetHashCode

若要将该类作为字典的一个键，则需要重写该方法

#### Equals

可重写 按值比对对象 重写时不能抛异常； 两个参, 可处理 `null`

#### ReferenceEquals

静态方法 比较两个对象是否引用同一实例 无法重写

#### GetType

返回 `System.Type` 派生的类，包含该对象所属类的信息，包括基本类型，方法，属性等，也是 .Net 反射技术的入口

#### MemberwiseClone

浅表复制（只复制值类型和引用类型的引用），用于复制对象，返回对副本的一个引用，不能重写

## 继承

类：每个类只能派生自一个基类；但可以实现多个接口

结构体：结构体只能实现接口，不能继承其他基类或结构体

使用 `:` 继承，同时继承自类和接口，类必须在最前面

```csharp
class MyClass : MyBaseClass, IMyInterface1, IMyInterface2 {
    ...
}
```

### 虚方法，虚属性

基类方法或属性声明为 `virtual`，则可在子类中重写该方法，子类方法重写基类方法或接口方法，使用 `override` 声明

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

### 隐藏方法

当基类与子类有同样的方法，且基类没有用 virtual 声明，子类也没有用 override 声明时，可用 new 声明隐藏基类方法

> 多用于解决继承于其他第三方类的子类的版本冲突，不应故意用于隐藏基类成员

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

### 抽象类和抽象方法/属性

> 将类和方法声明为 abstract，则类为抽象类
> 
> 抽象类不能被实例化，抽象方法不能直接实现，只能重写
> 
> 含有抽象方法的类必须声明为抽象类

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

### 密封类和密封方法/属性

> 用 sealed 修饰类或方法，不允许派生出子类或重写此方法，常用于库，防止子类造成功能的不稳定性
> 
> 用 sealed 修饰类还可以缩短或消除用于虚拟方法的虚拟表，以提高性能

```csharp
public sealed class MySealedClass { ... }
public class MyClass1: MyClass { ... }                // 编译器报错，无法继承于密封类

public class MyBaseClass {
    public sealed void MySealedMethod() { ... }
}
public class MyClass2: MyBaseClass {
    public override void MySealedMethod() { ... }    // 编译器报错，无法重写密封方法或属性
}
```

### 修饰符

> 注：若存在嵌套类型，内部类型可访问外部类型的所有成员，即 InnerClass 中的代码可访问所有 OuterClass 成员，包括 private 成员

| 修饰符       | 应用              | 说明                                              |
|:---------:|:---------------:|:-----------------------------------------------:|
| public    | 所有类型及成员         | 逻辑访问修饰符，任何代码均可访问                                |
| protected | 类型和内嵌类型的所有成员    | 逻辑访问修饰符，仅派生类可访问                                 |
| internal  | 所有类型及成员         | 物理访问修饰符，所在程序集内可访问                               |
| private   | 类型和内嵌类型的所有成员    | 逻辑访问修饰符，仅限所属类型内部可访问                             |
| new       | 函数成员            | 相同签名时隐藏继承的成员                                    |
| static    | 所有类型成员          | 作为类成员而非对象成员；修饰类时表示该类无法实例化                       |
| virtual   | 函数成员            | 可由派生类重写                                         |
| abstract  | 函数成员            | 定义了成员签名，但没实现具体代码                                |
| override  | 函数成员            | 重写继承的抽象类或虚拟类                                    |
| sealed    | 类 方法 属性         | 类: 不能派生子类；<br />方法, 属性: 成员重写已继承虚拟成员, 其派生类无法继续重写 |
| extern    | 静态[DLLImport]方法 | 成员在外部用另一种语言实现                                   |
| unsafe    | 任何成员            | 允许运行非安全代码，允许使用指针                                |

## 接口

> 一个类派生自某个接口，声明这个类就会实现某些函数

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

1. 声明接口语法与抽象类完全相同，但不允许任有何成员的实现方式；一般只包含方法，属性，索引器，事件声明
2. 不需要 abstract 关键字，不能有构造和字段，不允许包含运算符重载
3. 不允许声明成员修饰符，接口成员总是隐式 public（可由实现类声明），不能声明为 virtual
4. 接口可派生自接口

## is，as，cast

is：判断对象是否为某一类或接口的实例

as：类似于 cast 运算符，用于强转；当对象并非转换的类的实现时，不抛异常，而是返回 null

```csharp
public interface I1 {}
public interface I2 {}
class MyClass: I1 {}

MyClass obj = new MyClass();
I1 i1 = null;
I2 i2 = null;

obj is I1;        // true
obj is I2;        // false

i1 = (I1)obj;    // i1 != null
i2 = obj as I2;    // i2 = null
i2 = (I2)obj;    // 抛出 InvalidCastException 异常
```

# 命名空间

1. C# 类按照命名空间的逻辑组织, 命名空间与文件的组织方式无关

2. 命名空间可嵌套
   
   ```csharp
   // S1.S2.S3.ClassName
   namespace S1 {
       namespace S2 {
           namespace S3 {
               class ClassName {
                   ...
               }
           }
       }
   }
   
   namespace S1.S2.S3 {
       class ClassName {
           ...
       }
   }
   ```

3. 不允许声明嵌套在另一个命名空间中的多部分命名空间

4. 别名(alias)：
   
   ```csharp
   using AAA.BBB.CCC = ABC; // 可直接用 ABC 代替 AAA.BBB.CCC
   ```

5. 可以使用 using static XXX 引用 XXX, 可直接使用 XXX 的静态成员
   
   ```csharp
   using static System.Console;
   (方法中)
   // 相当于 System.Console.WriteLine("Hello World!")
   WriteLine("Hello World!");    
   ```

# 注释

## 注释

```csharp
// 单行注释
/*
多行注释
*/
```

## XML 文档

```csharp
/* 注释 XML: 根据注释自动生成 XML 格式文档
 *  以 /// 标记, 都是单行
 *  <c>             将行中的文本标记为代码
 *  <code>          将多行标记为代码
 *  <example>       标记为代码示例
 *  <exception>     说明为异常类, 编译器会验证语法
 *  <include>       包含其它说明文件注释, 编译器会验证语法
 *  <list>          插入列表
 *  <para>          建立文本结构
 *  <param>         标记方法参数, 编译器会验证语法
 *  <paramref>      表名一个单词是方法的参数, 编译器会验证语法
 *  <permission>    说明对成员的访问, 编译器会验证语法
 *  <remarks>       给成员添加描述
 *  <returns>       说明返回值 
 *  <see>           提供对另一个参数的交叉引用, 编译器会验证语法
 *  <seealso>       "参见" 部分, 编译器会验证语法
 *  <summary>       类型或成员的简短小结
 *  <typeparam>     泛型类中, 以说明一个类型参数
 *  <typepararef>   类型参数名称
 *  <value>         描述属性
 */
///<summary>
/// Hello world!!!
/// <param name="x"> 传参: x </param>
///</summary>
```

# 预处理指令

## #define #undef

> 注册/取消编译时名称符号

## #if #elif #else #endif

> 条件编译，判断条件为符号，符号存在相当于 true
> 
> 支持 !/==/!=/||/&&

```csharp
#define ENTER
#define W10
#if ENTER
    ...
    #if W10
        ...
    #endif
#elif W10 && (ENTER == false)
    ...
#endif
```

## #warning #error

> 编译时遇到 warning 会输出后面的提示信息，遇到 error  会报错并停止编译

```csharp
#if DEBUG && RELEASE
    #error "DEBUG 与 RELEASE 不能共存"
#endif
#warning "Hello !!"
```

## #region #endregion

> 将某段代码视为有指定名称的一个块
> 
> 无实际用处，可被编译器识别，让代码在屏幕上更好的布局

```csharp
#region Member Field Declaration
int x;
double d;
#endregion
```

## #line

> 改变编译器警告和错误信息的行号和文件

```csharp
#line 165 "Core.cs"    // 信息指定到 Core.cs 的 165 行
#line default        // 还原原始信息
```

## #pragma

> 抑制或还原编译器警告

```csharp
#pragma warning disable 169        // 禁用 字段未使用 警告
public class MyClass { ... }
#pragma warning restore 169        // 恢复 字段未使用 警告
```

# 流程控制

## if-else

```csharp
if (condition1) { ... }            // 必有
else if (condition2) { ... }    // 可选 可含有多个
else { ... }                    // 可选
```

## switch

```csharp
switch (condition)
{
    case X:        // 可有多个, 当 condition == X 时调用
        ...;    // 没有代码块时，允许不存在 break;
        break;    // 没有 break 大多数会报编译错误；要跳到别的块只能使用 goto case X;
    default:    // 都没匹配时调用
        ...;
        break;
}
```


## for

```csharp
for (initicalizer;    // 先执行 initicalizer
     condition;        // 然后 condition
     iterator)        // 每次循环体完成后执行 iterator，然后继续进行 condition
{
    ...;            // condition 返回 true 则运行循环体
}                    // initicalizer -> condition -> ... -> iterator -> condition -> ... ->-------
```

## while

```csharp
while (condition)
{
    ...;
}

// do-while 与 while 循环最大区别就是无论如何都先执行一次循环体
do
{
    ...;
} while (condition);
```


## foreach

```csharp
foreach (type/var item in items)    // items 需要为 IEnumerable<T> 类型对象
{
    ...;
}
```

## goto

```csharp
goto Label;    // 限制：不能跳出类，不能跳出/入循环代码块，不能跳出 finally 块
...;
Label: 
...;
```

## break

跳出 switch 和循环代码块

## continue

用于循环代码块。中断本次循环并进行下一个循坏

## return

退出类的方法

# 资源释放与内存回收

## 内存模型

### 虚拟地址空间

> 虚拟地址空间: Windows 的虚拟寻址系统把程序可用的内存地址映射到硬件内存中的实际地址上，实际结果是 32 位处理器上每一个进程都可以使用 4G 的内存，64 位会更大，这些内存实际上包含了程序的所有部分，包括可执行代码，加载的 DLL，所有变量，这些内存称为虚拟地址空间，或虚拟内存，简称内存

### 栈

存储不是对象类型的值数据类型和方法传递的参数副本

释放变量时，顺序总是与给他们分配内存的顺序相反

块作用域：某个作用于包含在另一个作用域中，该作用于称为块作用域或结构作用域，常见有内部代码块

栈实际上是由上到下填充的，程序开始时栈指针指向栈底，从高内存地址向低内存地址填充

### 引用数据类型

#### 栈局限性

> 尽管栈有非常高的性能，但某些变量生存期必须嵌套，希望使用一个方法分配内存存储数据，并在该方法退出后仍保证数据可用，如所有以 new 运算符请求分配的存储空间

#### 托管堆

> 处理器可用内存的一个内存区域, 简称堆, 在垃圾回收器控制下工作（区别于C++的堆，无需手动释放内存）

## 垃圾回收

> 垃圾回收期运行时，会在堆中删除不再引用的所有对象（在引用根表找到所有引用对象，并在引用的对象树中查找）， 删除堆中对象后，堆会立即把对象分散起来，与已释放的内存混合在一起
> 
> 一般情况下，垃圾回收会在需要时候运行，但也可以手动调用：
> 
> ```csharp
> System.GC.Collect();
> ```

### 托管堆

> 托管堆会在 GC 后将剩余对象移至堆顶部，因此读取对象时不需要遍历寻找，只需要读取内存地址就好，放置新堆也不需要遍历链表

### 大对象堆

> 当对象大小超过 85000 字节时，会放在大对象堆上而不是主堆上，压缩大对象代价比较大，因此大对象堆的对象不会进行压缩

### 分代回收

> 创建对象时，放在第 0 代堆上，驻留了最新对象；新对象分配内存空间时，超出第 0 代内存或调用 Collect() 方法，会进行垃圾回收
> 第一次垃圾回收，仍保留的对象会被压缩，移至堆得下一部分或世代部分 -- 第 1 代部分
> 第二次垃圾回收，第 1 代保留的对象移至第 2 代，第 0 代保留对象移至第 1 代
> 第二次垃圾回收后，第 2 代和大对象堆上的回收放在后台线程上进行，以减少总停顿时间，服务器和工作站默认开启此功能

### 垃圾回收的平衡

> 专用于服务器垃圾回收
> 
> 每个逻辑服务器都有一个垃圾回收堆, 当一个触发, 平衡其他几个垃圾回收器的小对象堆和大对象堆，减少不必要的回收
> 
> 垃圾回收器回收方式由 GCLatencyMode 设置

| GCLatencyMode       | 说明                                                                        |
|:-------------------:|:-------------------------------------------------------------------------:|
| Batch               | 禁用并发, 垃圾回收设置为最大吞吐量, 会重写配置设置                                               |
| Interactive         | 工作站默认. 使用并发, 平衡吞吐量响应                                                      |
| LowLatency          | 保守. 只有系统存在内存压力时才进行完整回收, 用于短期执行特定操作                                        |
| SustainedLowLatency | 只有系统存在内存压力时才进行完整的内存块回收                                                    |
| NoGCRegion          | (.Net 4.6) 只读, 可在代码块中调用 GC.TryStanNoGCRegion 和 EndNoGCRegion 设置, 期间不运行 GC |

```csharp
GC.Collect();
GC.TryStartNoGCRegion(1024);
    // No GC
GC.EndNoGCRegion();
```

## 强引用与弱引用

### 强引用

> 在根表中引用，GC 不会回收这些对象
> 
> 对象互相引用且不被根表引用，GC 可销毁这些对象
> 
> 超出作用域或指定为 null 则释放内存

### 弱引用

> 使用 WeakReference 创建和使用对象
> 
> 当持有对象时，若 GC 工作，会回收对象并释放内存，因此应使用 IsAlive 判断是否持有对象
> 
> 弱引用对小对象无意义，因为弱引用有自己的开销，可能比小对象要大
> 
> 使用事件很容易错过引用的清理，因此也可用弱引用

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

## 非托管资源

> GC 可以回收托管资源，但无法回收非托管资源，如 文件句柄，网络连接和数据库连接等

### 处理非托管资源

1. 声明析构函数或终结器，作为类的一个成员

2. 类中实现 System.IDispose 接口

### 析构函数

> 析构函数会在 GC 前调用
> 
> 析构函数最终在生成 IL 代码时生成终结器 finalizer
> 
> 由于 GC 发生时间的不确定性，不太适合在这里释放资源；频繁进行 GC 也会显著影响性能

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

### IDisposable

> IDispose 定义方法，显式的释放非托管资源，可精准控制释放资源的事件
> 
> 实现 IDispose 不意味着还要实现一个终结器，因为终结器会带来额外开销（会释放一个新对象），因此只有需要时候才会

```csharp
IDispose theInstance = null;
try {
    theInstance = new InputStream();
    // do something
} finally {
    theInstance?.Dispose();
}
```

### using 语句

> 使用 using 在调用完成后或出现异常自动关闭，编译成 IL 后等价于前面的 IDispose

```csharp
using (InputStream inputStream = new InputStream()) {
    // do something
}
```

### 同时实现 IDispose 和 析构

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
        Dispose(false);
    }
}
```

## 不安全的代码

### 使用指针访问内存

#### 原因

1. 向后兼容性: 调用其他本地 C/C++ API 需要传递指针
2. 使用指针提供最优性能

#### 限制

语法比较复杂，使用比较困难，需要仔细考虑逻辑操作

容易出现细微，难以察觉的错误，导致栈溢出，访问到未知内存区域，甚至重写 .Net 运行库所需信息，导致崩溃

必须授权使用, 默认仅运行于本地计算机, 远程计算机则需要授予额外许可

#### 使用

##### unsafe 标记

由于指针存在风险，所以 C# 仅允许在标记 unsafe 的代码块中使用

unsafe 允许修饰在任何类，结构，方法，代码块，参数，属性上，无论有没有其他任何修饰，但不能修饰局部变量

需要在 project.json 中设置 compilation.allowUnsafe=true

##### 指针语法

1. `&`: 取地址，将值转换为地址
2. `*`: 获取地址内容，将指针转换为值

```csharp
public unsafe void MyMethod() {
    int* pWidth, pHeight;
    double* pResult;
    byte*[] pFlags;

    int x = 10;
    int* pX, pY;
    pX = &x;
    pY = pX;
    *pY = 20;
    // result: *pX = x = 10, *pY = 20
}
```

##### 指针强转为整型

DWORD：即 32 位 uint，32 位 win 下效率最高的存储单元，因此一些非 4 字节的数据格式也会补全 4 字节，如 byte

指针类型可被安全转换为 uint，long，ulong，其他类型包括 int 都有可能溢出

在 C# 中的函数调用，没有传入指针参数的方法，应先强转为 ulong

```csharp
// 指针类型可与整型类型显示转化
ulong y = (ulong)pX;
int* pD = (int*)y;
// result: *pD = x
```

##### 指针之间的强转

> 不允许隐式转换，必须显式强转；允许不同类型指针之间强转（实现类似 C Union 的结构）

```csharp
// 以下代码不会报错，但运行起来会有问题。
// double 值得内存区域大于 byte 值，会读到未知的内存区域，得到无意义的值
// 但可以转化为 sbyte 类型值，用以检查内存单个字节
byte aByte = 8;
byte* pByte = &aByte;
double* pDouble = (double*) pByte;
```

##### void 指针

不希望指定指针类型, 可以声明为 void* 类型指针

主要用途是使用带有 void* 参数的 API

试图对 void* 指针使用 * 运算符会报错

```csharp
int* pointerToInt;
void* pointerToVoid;
pointerToVoid = (void*)pointerToInt;
```

##### 指针算术运算

可使用 +，-，+=，-=，++，--

只能与 long，ulong 类型进行运算

不允许对 void* 指针进行算术运算

两个指针之间的运算结果是 long 类型

```csharp
uint u = 3;
byte b = 8;
double d = 10.0;
uint* pUint = &u;        // size of a uint = 4
byte* pByte = &b;        // size of a byte = 1
double* pDouble = &d;    // size of a double = 8

// 假设指针指向的内存地址：
// pUnit    1243332
// pByte    1243328
// pDouble    1243320

++pUint;                        // adds (1*4)=4 bytes to pUnit            -> 1243336
pByte -= 3;                        // subtracts (3*1)=3 bytes to pUnit        -> 1243325
double* pDouble2 = pDouble + 4;    // pDouble2 = pDouble + (4*8=32 bytes)    -> 1243352
double* pD1 = (double*)1243324;
double* pD2 = (double*)1243300;
long d = pD1 - pD2;                // result: 3(24/sizeof(double)=3)
```

##### sizeof

> 作用：接收某一类型，返回该类型所需的字节数

```csharp
int s = sizeof(double) // s = 8
```

##### stackalloc

> 作用：申请内存

```csharp
decimal* pDecimals = stackalloc decimal[10];
```

##### 结构体指针

> 限制：结构体中不能包含任何引用类型

```csharp
// struct
struct MyStruct {
    public long X;
    public float F;
}
var myStruct = new MyStruct();
// unsafe
MyStruct* pStruct = &myStruct;

(*pStruct).X = 4;
(*pStruct).F = 3.4f;

pStruct->X = 3;
pStruct->F = 3.4f;

long* pL = &(myStruct.X);
float* pF = &(myStruct.F);

long* pL = &(pStruct->X);
float* pF = &(pStruct->F);
```

##### 类成员指针

> 仍不能创建指向类的指针，只能指向对象内值类型成员
> 
> 需要使用 fixed 关键字，以防止 GC 将对象回收

```csharp
// class
class MyClass {
    public long X;
    public float F;
}
var myObject = new MyClass();
var myObject2 = new MyClass();

// unsafe
long* pL = &(myObject.X);    // Error
float* pF = &(myObject.F);    // Error
// GC 会把对象移到内存新单元，因此指针地址会发生错误。
// 应用 fixed 关键字 告诉 GC 不要移动该对象
fixed (long* pObject = &(myObject.X)) {
    // do something
}
// 用到多个指针，可在代码块前放置多条 fixed 语句
fixed (long* pX = &(myObject.X))
fixed (float* pF = &(myObject.F))
{
    // do something
}
// 在不同阶段固定几个指针，可嵌套 fixed 块
fixed (long* pX = &(myObject.X)) {
    // do something with pX
    fixed (float* pF = &(myObject.F)) {
        // do something else with pF
    }
}
// 若变量类型相同，则可在一个 fixed 块中初始化
fixed (long* pX = &(myObject.X), pX2 = &(myObject2.X)) {
    // etc
}
```

## 平台调用

### 获取平台方法

> 使用 dumpbin 方法
> 
> 例：`dumpbin /exports c:\windows\system32\kernel32.dll | more`

### 查找 Windows API 定义

> 例：
> 
> BOOL CreateHardLink(
> 
>     LPCTSTR lpFileName,
>     
>     LPCTSTR lpExistingFileName,
>     
>     LPSECURITY_ATTRIBUTES lpSecurityAttributes
> 
> )
> 
> 使用 Hungarian 表示法
> 
> | 缀     | 类    型                          |
> | ----- | ------------------------------- |
> | sz    | 指向一个以零字符结尾的字符串中的第一个字符           |
> | str   | 字符串                             |
> | i     | int                             |
> | n     | 数或int                           |
> | ui    | Unsigned int                    |
> | c     | char                            |
> | w     | WORD（unsigned short）            |
> | dw    | DWORD（unsigned long）            |
> | fn    | 函数指针（function pointer）          |
> | d     | Double                          |
> | by    | byte                            |
> | l     | long                            |
> | p     | pointer                         |
> | lp    | long pointer                    |
> | lpstr | 指向字符串的long pointer              |
> | h     | 句柄（handle）                      |
> | m_    | 类成员（class member）               |
> | g_    | 全局型（global type）                |
> | hwnd  | 窗口的句柄（Window handle）            |
> | hdc   | Windows设备上下文（device context）的句柄 |

```csharp
        // .Net 1.0：使用 IntPtr 作为句柄，但容易出现线程安全问题；之后使用 SafeFileHandle
        [SecurityCritical]
        internal static class NativeMethods
        {
            [DllImport("kernel32.dll",
                SetLastError = true,
                EntryPoint = "CreateHardLinkW",
                CharSet = CharSet.Unicode)]
            [return: MarshalAs(UnmanagedType.Bool)]
            private static extern bool CreateHardLink(
                [In, MarshalAs(UnmanagedType.LPWStr)] string newFileName,
                [In, MarshalAs(UnmanagedType.LPWStr)] string existingFileName,
                IntPtr securityAttributes);

            internal static void CreateHardLink(string oldFileName, string newFileName)
            {
                if (!CreateHardLink(newFileName, oldFileName, IntPtr.Zero))
                {
                    var ex = new Win32Exception(Marshal.GetLastWin32Error());
                    throw new IOException(ex.Message, ex);
                }
            }
        }

        public static class FileUtility
        {
            [FileIOPermission(SecurityAction.LinkDemand, Unrestricted = true)]
            public static void CreateHardLink(string oldNameFile, string newFileName)
            {
                NativeMethods.CreateHardLink(oldNameFile, newFileName);
            }
        }
```

# 泛型

## 命名约定

- 以 T 做前缀
- 没有特殊要求，泛型允许用任何类型替代，且只有一个泛型类型，可用 T 作为泛型类型名称
- 泛型类型有特殊要求，或有至少两个泛型类型，就应给泛型类型使用描述性名称

## 空值

> 由于泛型仍可以为值类型，所以不能传入 null
> 
> 使用 default(T) 作为泛型的空类型

## 协变与抗变

### 协变

> 参数类型是协变的，泛型类型用 out 修饰，即协变的

### 抗变

> 返回值是抗变的，泛型类型用  in 修饰，即抗变的

## 泛型约束

> 若泛型类需要调用泛型类型的方法，必须使用泛型约束，格式：
> 
>     <T> where T: 约束1, 约束2, 约束3....
> 
> 在 类 或者 方法的声明末尾

| 约束              | 说明                        |
|:---------------:|:-------------------------:|
| where T: struct | 结构约束，类型 T 必须是值类型          |
| where T: class  | 类约束，类型 T 必须为引用类型          |
| where T: IFoo   | 指定类型 T 必须实现 IFoo 接口       |
| where T: Foo    | 指定类型 T 必须派生自基类 Foo        |
| where T: new()  | 构造函数约束，指定类型 T 必须有一个默认构造函数 |
| where T1: T2    | 类型 T1 派生自泛型类型 T2          |

```csharp
public interface IDocument {
    string Title { get; set;
    string Content { get; set; }
 }
}

public class DocumentManager<TDocument> where TDocument: IDocument {

    private readonly Queue<TDocument> documentQueue = new Queue<TDocument> 

    public void DisplayAllDocuments() {
        foreach (TDocument doc in documentQueue) {
            Console.WriteLine(((IDocument)doc).Title)
        }
    }
}
```

## 静态成员

> 泛型类的静态成员只在相同泛型的类中共享

## 泛型结构

> Nullable<T>

# 数组与元组

## 简单数组

> 数组位于托管堆

### 声明

#### 一维数组

```csharp
T[] arrName;
arrName = new T[count];

T[] arrName = new T[count];

T[] arrName = new T[count]{ t1, t2, t3, ... };

T[] arrName = new T[] { t1, t2, t3, ... };

T[] arrName = { t1, t2, t3, ... };
```

#### 多维数组

> 一旦声明，数组维度无法改变

```csharp
T[,] arrNane = new T[count1, count2];

T[,] arrName = {
    { t00, t01, t02, ... },
    { t10, t11, t12, ... },
    { t20, t21, t22, ... }, ...
};
```

#### 锯齿数组

```csharp
T[][] arrName = new T[count1][];
arrName[0] = new T[count20];
arrName[1] = new T[count21];
...
```

### Array 类

> Array 为抽象类，除了使用 C# 语句初始化外，还可用 Array.CreateInstance 创建数组

### 遍历

> IEnumerable：可使用 for 或者 foreach 循环

### 复制（浅表复制）

> ICloneable：Clone()
> 
> Array.Copy()

### 排序

> Quicksort算法：Array.Sort()
> 
> 数组内容必须实现 IComparable 接口

### 数组作为参数

```csharp
void Function(int[] args) { ... }
```

- 数组支持协变：T[] 可以匹配任何 T 子类的数组；但只能应用于引用类型，不能使用值类型

### ArraySegment

> 代表数组的一个片段
> 
> 并没有复制原数组的数据，片段内容改变后，原数组也会改变

## 元组

### Tuple

> 元组结合了不同类型的对象
> 
> C# 默认提供了 Tuple<T1>, Tuple<T1, T2>, ... , Tuple<T1, T2, ...., T7> 共 7 种有限长度的元组和 <T1, ..., T7, TTuple<...>> 7个标准类型和1个元组类型的元组

# IEnumerable

> 枚举接口，定义了一个枚举器，以实现 foreach 等循环

## IEnumerator

> 枚举器：用于对对象的迭代

## yield

> 用于简单的实现一个集合代码

```csharp
public IEnumerator GetEnumerator() {
    // yield return 返回集合的一个元素
    // yield break  退出循环
    // 会依次读取 yield return，直到 yield back
    // 可包含多条 yield 语句, 但不可包含 return 语句
    yield return "hello";
    yield return "world";
}
```

# 运算符

## 运算符

> 运算符
>      *  算术运算符 逻辑运算符 字符串连接运算符(+) 递增递减运算符 移位运算符(>> <<) 比较运算符(== != <> <=> =)
>      *  赋值运算符 成员访问运算符(.) 索引运算符([]) 类型转换运算符(()) 
>      *  三元运算符
>      *      [condition] ? [true_value] : [false_value]
>      *  委托链接删除运算符(+ -) 
>      *  对象创建运算符(new) 
>      *  类型信息运算符(sizeof is typeof as)
>      *      sizeof: sizeof(a) => int, 获取 a 的长度(字节) 若a为复杂类型, 则将 a 放入 unsafe 代码块中
>      *      is: a is B => bool, 判断 a 是否为 B 或 B的子类(java 的 instanceof)
>      *      as: a as B => B?, 判断 a 是否为 B 或 B的子类, 如果是, 类型转换; 不是, a 赋值 null
>      *      typeof: typeof(A) => 特定类型的 System.Type 对象, 主要用于反射
>      *  溢出异常控制运算符(checked unchecked)
>      *      byte b = 255;
>      *      --
>      *      b++;
>      *      WriteLine(b);
>      *          ==> OverflowException
>      *      --
>      *      checked
>      *      {
>      *          b++;
>      *      }
>      *      WriteLine(b);
>      *          ==> Console 输出错误信息
>      *      --
>      *      unchecked
>      *      {
>      *          b++;
>      *      }
>      *      WriteLine(b);
>      *          ==> 溢出检查禁止, 不会抛异常, 但会丢失数据, 因此返回的是 0
>      *      
>      *      编译器使用 /checked 参数, 可检查程序中所有未标记代码的溢出
>      *  间接寻址运算符([])
>      *  名称空间别名限定符(::)
>      *  空合并运算符(??)
>      *      int? a = null;
>      *      int b;
>      *      b = a ?? 10;    // b=10
>      *      a = 3;
>      *      b = a ?? 10;    // b=3
>      *  空值传播运算符(?. ?[]) 
>      *  标识符的名称运算符(nameof())
>      *      nameof: nameof(符号/属性/方法) => string: 返回符号/属性/方法的名称 

## 运算符优先级

> 优先级和关联性
>      *  除赋值和条件运算符外, 其他都是左关联
>      *  优先级
>      *      基本运算符           () . [] x++ x-- new typeof sizeof checked unchecked
>      *      一元运算符           + - ! ~ ++x --x 强制转换
>      *      乘除运算符           * / %
>      *      加减运算符           + -
>      *      移位运算符           << >>
>      *      关系运算符           < ><= >= is as
>      *      比较运算符           == !=
>      *      按位 AND 运算符      &
>      *      按位 XOR 运算符      ^
>      *      按位 OR 运算符       |
>      *      条件 AND 运算符      &&
>      *      条件 OR 运算符       ||
>      *      空合并运算符         ??
>      *      条件运算符           ?:
>      *      赋值运算符和lambda   = += -= *= /= %= &= |= ^= <<= >>= >>>= =>

## 运算符重载

> 运算符重载
>      *  public static Type operator [运算符 +/-/...] (...) {...}
>           *  注: 重写 == 运算符必须重写的 equals 和 getHashCode 方法
>           *  可重载运算符
>                *      + * - / %
>                *      + - ++ --
>                     *      & | ^ >> <<
>                     *      ! ~ true false(true false 必须成对重载)
>                     *      == != <= >= < > (必须成对重载)
>                     *      += -= *= /= >>= <<= %= &= |= ^= (隐式重写)
>                     *      []
>                     *          public [返回类型] this[int(任意类型都有效) index]
>                     *          {
>                     *              get{}
>                     *              set{}
>                     *          }
>                     *      ()(类型强转)
>                     *          public static implicit operator B(A value) {...} // 必须显式转换
>                     *          public static explicit operator B(A value) {...} // 可隐式转换

# 委托与事件

## 委托

### 声明

> 实质上是一个新类 继承于 System.MulticastDelegate，从表现上类似于定义一个函数的签名
> 
> 类型安全：可确保被调用的方法的签名正确的
> 
> 给委托的实例可以引用任何类型对象的实例或静态方法，只要签名相同

```csharp
delegate TReturn Name([params]);
Name dName = 某个函数;
dName() <=> dName.Invoke();
```

### Action 与 Func 委托

> Action: <T, T2, T3, ...> => void (T, T2, T3, ...)
> 
> Func: <T, T2, T3, ..., TResult> => TResult (T, T2, T3, ...)

### 多播委托

> 注：多播委托是一个委托的链表，按顺序执行，但具体顺序未定义
> 
> 因此，应避免使用依赖顺序的委托
> 
> 同时，一个委托抛出异常，其后的其他委托均无法调用

```csharp
// 多播委托: 同一委托执行多个操作
// 使用 + - += -=
// 匿名方法: 用作委托参数的一段代码
void func(Func<double, double, double> aDelegate)
{
    operation += aDelegate;
    operation += delegate (double a, double b) { return a; };
}
```

### 事件

```csharp
// 事件
void Events()
{
    var dealer = new CarDealer();

    var daniel = new Consumer("Daniel");
    dealer.NewCarInfo += daniel.NewCarHere;
    dealer.NewCar("Mercedes");

    var sebastian = new Consumer("sebastian");
    dealer.NewCarInfo += sebastian.NewCarHere;
    dealer.NewCar("Ferrari");

    dealer.NewCarInfo -= sebastian.NewCarHere;
    dealer.NewCar("Red Bull Racing");
}
// 具体事件 Event
public class CarInfoEventArgs : EventArgs
{
    public string Car { get; }

    public CarInfoEventArgs(string car)
    {
        Car = car;
    }
}
// 事件管理
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
// 提供事件响应的方法
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

## Lambda 表达式

```csharp
        // lambda 表达式
        // (params 只有一个参数可省略括号) => {}
        void Lambda()
        {
            operation += (param1, param2) => {
                return param1 * param2;
            };
            operation += (p1, p2) => p1 + p2;
        }
```

# 字符串与正则

## System.String

### 索引语法

```csharp
char a = "hello"[0]; // a = 'h'
```

### 连接字符串

```csharp
string s1 = "hello";
string s2 = "world";

string str = s1 + "" + s2;
s1 += s2;
```

### 方法

| 方法             | 作用                        |
|:--------------:|:-------------------------:|
| Compare        | 判断字符串是否相同                 |
| CompareOrdinal | Compare，忽略区域值背景           |
| Concat         | 合并多个字符串                   |
| CopyTo         | 将指定数量的字符串复制到数组的实例中        |
| Format         | 格式化字符串                    |
| IndexOf        | 查找指定字符串中第一次出现某个字符（串）的位置   |
| IndexOfAny     | 查找指定字符串中第一次出现某个字符或某组字符的位置 |
| Inert          | 指定字符串插入到另一个字符串的实例的指定索引中   |
| Join           | 合并字符串串，并生成新的字符串           |
| LastIndexOf    | 最后一次出现某个字符（串）的位置          |
| LastIndexOfAny | 最后一次出现的某个或某组字符的位置         |
| PadLeft        | 字符串左侧填充指定重复字符串            |
| PadRight       | 字符串右侧填充指定重复字符串            |
| Replace        | 替换字符串中的指定字符或字符串           |
| Split          | 分割字符串                     |
| Substring      | 截取指定位置字符串                 |
| ToLower        | 转为小写                      |
| ToUpper        | 转为大写                      |
| Trim           | 删除首尾空白                    |
| ...            |                           |

## System.Text.StringBuilder

> 创建时可指定分配内存，且在字符编辑（replace，append，insert，remove等）时避免复制操作
> 
> 可指定分配内存大小，默认 150
> 
> 大大提高替换单个字符串的效率，但插入/删除字符串扔效率低下

### Length

> 指定包含字符串实际长度

### Capacity

> 指定包含字符串在分配的内存中的最大长度

### MaxCapacity

> 允许的最大长度。默认 int.MaxValue
> 
> 只能在构造前指定

### 方法

| 方法           | 说明                                       |
|:------------:|:----------------------------------------:|
| Append       | 追加字符串                                    |
| AppendFormat | 追加格式化字符串，最终会在 ToString 时调用               |
| Insert       | 插入字符串                                    |
| Remove       | 删除字符串                                    |
| Replace      | 替换字符串                                    |
| ToString     | 生成 System.String，除此外无法用其他转换方法，无论显式还是隐式转换 |

## 字符串格式

### 字符串插值

> 若要输入 {} 则使用 {{}} 转义

```csharp
string s1 = "World";
string s2 = $"Hello, {s1}";    // Hello World
// 相当于 ==>
string s2 = String.Format("Hello, {0}", s1);    // 字符串占位符从 0 开始
```

### FormattableString

> 可以获取个书画字符串和属性
> 
> 需要 .Net 4.6，或使用 NuGet 的 StringInterprolationBridge

```csharp
int x = 3, y = 4;
FormattableString f = $"The result of {x} + {y} is {x + y}";
WriteLine($"format = {f.Format}");                            // The result of {0} + {1} is {2}
for (int i = 0; i < f.ArgumentCount; i++)
{
    WriteLine("Argument {0} = {1}", i, f.GetArgument(i));    // 0 = 3, 1 = 4, 2 = 7
}
```

#### 使用区域值

```csharp
var day = new DateTime(2025, 2, 14);
// s.ToString(InvariantCulture)
FormattableString.Invariant($"{day:d}");
```

#### 使用特定格式

```csharp
DateTime day = new DateTime(2025, 2, 14);
// 详见 MSDN DateTime.ToString
WriteLine($"{day:D}");    // Friday, February 14, 2025
WriteLine($"{day:d}");    // 2/14/2025
```

#### 自定义特定格式：IFormattable

```csharp
CustomFormattable format = new CustomFormattable() {
    FirstName = "aaa",
    LastName = "bbb"
}
$"{format:F}"    // aaa
class CustomFormattable : IFormattable
{
    public string FirstName { get; set; }
    public string LastName { get; set; }

    public override string ToString() => $"{FirstName} {LastName}";

    public virtual string ToString(string format) => ToString(format, null);

    public string ToString(string format, IFormatProvider formatProvider)
    {
        switch (format)
        {
            case null:
            case "A":
                return ToString();
            case "F":
                return FirstName;
            case "L":
                return LastName;
            default:
                throw new FormatException($"Invalid format string {format}");
        }
    }
}
```

## 正则

> System.Text.RegularExpressions

```csharp
string str = "...(原始字符串)...";
string regex = "...(正则字符串)..."
MatchCollection collection = Regex.Matches(str, regex[, RegexOptions options1[ | option2 | ...]]);
// 匹配结果
foreach (Match match in MatchCollection)
{
    int index = match.Index;
    string result = match.ToString();
}
// 捕获结果
foreach (Match match in collection)
{
    foreach (Group group in match.Groups)
    {
        if (group.Success)
        {
            int value = group.Value;
        }
    }
}
```

| RegexOptions            | 说明                                |
|:-----------------------:|:---------------------------------:|
| CultureImvariant        | 指定忽略字符串区域值                        |
| ExplicitCapture         | 修改收集匹配的方式。确保把显示指定的匹配作为有效的搜索结果     |
| IgnoreCase              | 忽略大小写                             |
| IgnorePatternWhitespace | 删除未转义的空白，通过 # 指定注释                |
| Multiline               | 修改 ^ 和 $ ，应用于每一行开头结尾而不是整个字符串的开头结尾 |
| RightToLeft             | 从右到左读                             |
| Singleline              | 指定 . 含义，使其可匹配换行符                  |

# 集合

## 接口

> 泛型集合     System..Collections.Generic
> 
> 特定类型集合 System..Collections.Specialized
> 
> 线程安全集合 System..Collections.Immutable

| 接口                        | 说明                                                                      |
|:-------------------------:|:-----------------------------------------------------------------------:|
| IEnumerable<T>            | 定义了 GetEnumerator(), 用于 foreach                                         |
| ICollection<T>            | 定义了获取集合元素个数，复制到数组，从其他集合添加删除元素等方法<br />Count, CopyTo, Add, Remove, Clear |
| IList<T>                  | 定义了索引器，用于插入和删除某些项 <br />Insert, Remove                                  |
| ISet<T>                   | 定义了集的合并，交集，检查重叠等 派生自 ICollection                                        |
| IDictionary<TKey, TValue> | 定义了包含键值对的泛型集合类实现，访问接口所有键和值，以及其索引器                                       |
| ILookup<TKey, TValue>     | 定义了包含键值对的集合，以及其访问，允许一个键包含多个值                                            |
| IComparer<T>              | 定义了一个比较器，实现 Compare() 方法给集合排序                                           |
| IEqualityComparer<T>      | 实现一个比较器，用于字典中的键，对可以对象进行相等性比较                                            |
| IReadOnlyCollection<T>    | 只读集合                                                                    |
| IReadOnlyList<T>          | 只读列表                                                                    |

## 列表

- [x] IList
- [x] IEnumerable
- [x] ICollection
- [ ] 只读列表
   - [x] IReadOnlyCollection
   - [x] IReadOnlyList

> List<T>
> 
> 基础操作：Add，AddRange，Insert，InsertRange，Remove，RemoveAll，RemoveRange，IndexOf，FindIndex，Find，FindAll，Exists,，Sort，Reverse

```csharp
var intList = new List<int>(10) { 0, 1 };   // 参数为初始大小，容量不足的话调整为当前的两倍(a, 2a, 4a, 8a, ...); {} 可用作初始化
intList.Capacity = 10;                      // 可获取和设置列表容量
intList.TrimExcess();                       // TrimExcess 方法可以删除集合内的不需要的容量，如果已占有量超过 90% 则不做任何事情
var intListReadonly = intList.AsReadOnly(); // 转换为只读列表
```

## 队列

- [x] ICollection
- [x] IEnumerable

> Queue<T>：LILO
> 
> 基础操作：Count
> 
>     Enqueue    入列
>     
>     Dequeue    出列
>     
>     Peek        出列但不删除
>     
>     TrimExcess    重新设置队列容量，从头部去除空元素

```csharp
var intQueue = new Queue<int>();
intQueue.Enqueue(1);            // 入列
var qg = intQueue.Dequeue();    // 出列
var qr = intQueue.Peek();       // 出列但不删除
```

## 栈

- [x] ICollection
- [x] IEnumerable

> Stack<T>：LIFO
> 
> 基础操作：
> 
>     Push        入栈
>     
>     Pop            出栈
>     
>     Peek        出栈但不删除
>     
>     Contains    是否存在

```csharp
var intStack = new Stack<int>();
intStack.Push(1);               // 入栈
var q = intStack.Pop();         // 出栈
var e = intStack.Contains(1);   // 是否存在
```

## 链表

> LinkedList<T>：双向链表

```csharp
LinkedList<int> intLinkedList = new LinkedList<int>();
intLinkedList.AddFirst(1);
int il = intLinkedList.First;
```

## 字典

> 用于字典 key 类的对象必须重写 GetHashCode  方法 和 IEquatable<T>.Equals or Object.Equals 方法

```csharp
var intDictionary = new Dictionary<string, int>()
{
    ["1"] = 1,
    ["2"] = 2
};
var id1 = intDictionary["1"];
var defInt = -1;
var has1 = intDictionary.TryGetValue("3", out defInt);

// 有序字典: 二叉搜索树
// 使用内存比 SortedList 多但插入删除操作比其快
// key 类型必须实现 IComparable<TKey> 接口
var intSortedDictionary = new SortedDictionary<string, int>();

// Lookup: Key -> [Value], 键指向的值是一组值
// 不能用一般方法创建, 只能使用 toLookup(selector) 方法
// selector 是一个选择器, value => key
var lookup1 = racers.ToLookup(r => r.Country);
foreach(Racer r in lookup1["ca"])
{
    Console.WriteLine(r);
}
```

## 集

> 集: HashSet, SortedSet
> 
> 可创建交集 并集 补集 子集 判断包含等

## 特殊集合

### 位处理

> BitArray: 可重新设置长度
> 
>     Count
>     
>     Length               可重置大小
>     Item
>     
>     Get & Set & SetAll
>     
>     Not
>     And & Or & Xor       合并两个对象
> 
> BitVector32: 不可重新设置长度, 固定长度 32 位, 值类型, 效率高
>        Data            转换为整数
>        Item            索引器, 可通过 Mask 或 Section 获取和设置值
>        CreateMask      Static, 为访问对象某一位创建掩码
>        CreateSection   Static, 创建 32 位中的几个片段

```csharp
void Bit()
{
    var bits1 = new BitArray(10);
    bits1.Length = 11;
    bits1.Not();
    var bits2 = new BitVector32(10);
    var bit1Mask = BitVector32.CreateMask();            // 第一位
    var bit2Mask = BitVector32.CreateMask(bit1Mask);    // 第二位
    var bit3Mask = BitVector32.CreateMask(bit2Mask);    // 第三位
    var bit4Mask = BitVector32.CreateMask(bit3Mask);    // 第四位
    bits2[bit1Mask] = true;
    bits2[0xacdef] = true;      // 自定义掩码 结果为 ‭101010111100110111101111‬ (即 0xabcdef)
    var bitSection1 = BitVector32.CreateSection(0xfff);
    var bitSection2 = BitVector32.CreateSection(0xff, bitSection1);
    var bitSection3 = BitVector32.CreateSection(0xf, bitSection2);
    var bitSection4 = BitVector32.CreateSection(0x7, bitSection3);
    var bitSection5 = BitVector32.CreateSection(0x7, bitSection4);
    var bitSection6 = BitVector32.CreateSection(0x3, bitSection5);
    var b2s1 = bits2[bitSection1];
    var b2s2 = bits2[bitSection2];
    var b2s3 = bits2[bitSection3];
    var b2s4 = bits2[bitSection4];
    var b2s5 = bits2[bitSection5];
    var b2s6 = bits2[bitSection6];
}
```

### 可观察集合

> 可观察集合 ObservableCollection<T>
> 
> 派生自 Collection<T> 类, 内部使用 List<T> 类
> 
> 触发 CollectionChanged 事件

```csharp
var observableCollection = new ObservableCollection<int>();
observableCollection.CollectionChanged += (sender, e) =>
{
    Console.WriteLine("Action: " + e.Action);
    Console.WriteLine("Start Index: from " + e.NewStartingIndex + " to " + e.OldStartingIndex);
    Console.WriteLine("Data Change: " + e.OldItems + " => " + e.NewItems);
}
```

### 不变集合

> 使用 Add, Remove, Replace, Sort 等方法时, 不改变原集合, 会返回一个新集合
> 
> 不会复制集合, 而是使用共享状态, 仅在需要时创建集合
> 
>     ToImmutableList    创建一个新的不变集合
>     
>     ToBuilder           创建一个可变集合, 修改完成后使用 ToImmutable 方法创建不可变集合

```csharp
var iArray1 = ImmutableArray.Create<string>();
var iArray2 = iArray1.Add("hello");
```

### 并发集合

> 并发集合 ConcurrentCollection
> 
>     线程安全的集合类
>     
>     ConcurrentXXX类: 线程安全集合类, 可能失败的方法会变成 TryXXX 方法, 返回 bool 类型, 表示是否失败
>     
>     BlockCollection<T>类: 阻塞性集合类, 会阻塞线程; 迭代时应通过 GetConsumingEnumerable 获取阻塞迭代器

```csharp
class PipelineSample
{
    public static async Task StartPipelineAsync()
    {
        var fileNames = new BlockingCollection<string>();
        var lines = new BlockingCollection<string>();
        var words = new ConcurrentDictionary<string, int>();
        var items = new BlockingCollection<Info>();
        var coloredItems = new BlockingCollection<Info>();
        Task t1 = PipelineStages.ReadFilenameAsync(@"../../..", fileNames);
        ColoredConsole.WriteLine("started stage 1");
        Task t2 = PipelineStages.LoadContentAsync(fileNames, lines);
        ColoredConsole.WriteLine("started stage 2");
        Task t3 = PipelineStages.ProcessContentAsync(lines, words);
        await Task.WhenAll(t1, t2, t3);
        ColoredConsole.WriteLine("stage 1, 2, 3 completed");

        Task t4 = PipelineStages.TransferContentAsync(words, items);
        Task t5 = PipelineStages.AddColorAsync(items, coloredItems);
        Task t6 = PipelineStages.ShowContentAsync(coloredItems);
        ColoredConsole.WriteLine("stage 4, 5, 6 started");
        await Task.WhenAll(t4, t5, t6);
        ColoredConsole.WriteLine("all stages finished");

    }
}
```

```csharp
class Info
{
    public string Word { get; set; }
    public int Count { get; set; }
    public string Color { get; set; }
    public override string ToString() => $"{Count} times: {Word}";
}

class PipelineStages
{
    public static Task ReadFilenameAsync(string path, BlockingCollection<string> output)
    {
        return Task.Factory.StartNew(() =>
        {
            foreach (string filename in Directory.EnumerateFiles(path, "*.cs", SearchOption.AllDirectories))
            {
                output.Add(filename);
                ColoredConsole.WriteLine($"stage 1: added {filename}");
            }
            output.CompleteAdding();
        }, TaskCreationOptions.LongRunning);
    }

    public static async Task LoadContentAsync(BlockingCollection<string> input, BlockingCollection<string> output)
    {
        foreach (var filename in input.GetConsumingEnumerable())
        {
            using (FileStream stream = File.OpenRead(filename))
            {
                var reader = new StreamReader(stream);
                string line = null;
                while ((line = await reader.ReadLineAsync()) != null)
                {
                    output.Add(line);
                    ColoredConsole.WriteLine($"stage 2: added {line}");
                }
            }
        }
        output.CompleteAdding();
    }

    public static Task ProcessContentAsync(BlockingCollection<string> input, ConcurrentDictionary<string, int> output)
    {
        return Task.Factory.StartNew(() =>
        {
            foreach (var line in input.GetConsumingEnumerable())
            {
                string[] words = line.Split(' ', ';', '\t', '{', '}', '(', ')', ':', ',', '"');
                foreach (var word in words.Where(w => !string.IsNullOrEmpty(w)))
                {
                    output.AddOrUpdate(key: word, addValue: 1, updateValueFactory: (s, i) => ++i);
                    ColoredConsole.WriteLine($"stage3: added {word}");
                }
            }
        }, TaskCreationOptions.LongRunning);
    } 

    public static Task TransferContentAsync(ConcurrentDictionary<string, int> input, BlockingCollection<Info> output)
    {
        return Task.Factory.StartNew(() =>
        {
            foreach (var word in input.Keys)
            {
                if (input.TryGetValue(word, out int value))
                {
                    var info = new Info
                    {
                        Word = word,
                        Count = value
                    };
                    output.Add(info);
                    ColoredConsole.WriteLine($"stage 4: added {info}");
                }
                output.CompleteAdding();
            }
        }, TaskCreationOptions.LongRunning);
    }

    public static Task AddColorAsync(BlockingCollection<Info> input, BlockingCollection<Info> output)
    {
        return Task.Factory.StartNew(() =>
        {
            foreach (var item in input.GetConsumingEnumerable())
            {
                if (item.Count > 40)
                {
                    item.Color = "Red";
                }
                else if (item.Count > 20)
                {
                    item.Color = "Yello";
                }
                else
                {
                    item.Color = "Green";
                }
                output.Add(item);
                ColoredConsole.WriteLine($"stage 5: added color {item.Color} to {item}");
            }
            output.CompleteAdding();

        }, TaskCreationOptions.LongRunning);
    }

    public static Task ShowContentAsync(BlockingCollection<Info> input)
    {
        return Task.Factory.StartNew(() =>
        {
            foreach (var item in input.GetConsumingEnumerable())
            {
                ColoredConsole.WriteLine($"stage 6: {item}", item.Color);
            }
        }, TaskCreationOptions.LongRunning);
    }
}

class ColoredConsole
{
    private static object syncOutput = new object();

    public static void WriteLine(string message)
    {
        lock (syncOutput)
        {
            Console.WriteLine(message);
        }
    }

    public static void WriteLine(string message, string color)
    {
        lock (syncOutput)
        {
            Console.ForegroundColor = (ConsoleColor)Enum.Parse(typeof(ConsoleColor), color);
            Console.WriteLine(message);
            Console.ResetColor();
        }
    }
}
```

# LinQ

> LINQ: Language Integrated Query, 语言集成查询
> 
> 使用相同的语法访问不同的数据源

## LinQ 查询

```csharp
var list = new List<string>();
var query = from r in list where r.StartsWith("a") orderby r descending select r;
```

## 扩展方法

> 为 IEnumerable<T> 接口提供的扩展方法

| 方法                                                                                                                                                                                                      | 说明            |
|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:-------------:|
| Where & OFType<TResult>                                                                                                                                                                                 | 筛选内容/类型       |
| Selet & SelectManySelet & SelectMany                                                                                                                                                                    | 转换类型 (类似 map) |
| OrderBy & OrderByDescending                                                                                                                                                                             | 排序            |
| ThenBy & ThenByDescending                                                                                                                                                                               | 二次排序          |
| Reverse                                                                                                                                                                                                 | 反向            |
| Join & GroupJoin                                                                                                                                                                                        | 连接两个集合        |
| GroupBy & ToLookup                                                                                                                                                                                      | 组合 分组         |
| All & Any & Contains                                                                                                                                                                                    | 检查是否满足特定条件    |
| Take & Skip & TakeWhile & SkipWhile                                                                                                                                                                     | 返回集合的一个子集     |
| Distint                                                                                                                                                                                                 | 删除重复元素        |
| Union                                                                                                                                                                                                   | 并集            |
| Intersect                                                                                                                                                                                               | 交集            |
| Except                                                                                                                                                                                                  | 只出现在一个集合中的元素  |
| Zip                                                                                                                                                                                                     | 合并集合          |
| First & FirstOrNull & Last & ElementAt & Single<br />Count & Sum & Max & Min & Average & Aggregate<br />ToArray & AsEnumerable & ToList & ToDirectory & Cast<TResult><br />Empty & Range & Repeat<br /> |               |

```csharp
var list = new List<string>();
list
    .Where(v => v.StartsWith("a"))
    .OrderByDescending(v => v)
    .Select(v => v);
```

# 错误与异常

### 分类

| 类                      | 说明                                                               |
|:----------------------:|:----------------------------------------------------------------:|
| SystemException        | .Net 运行库或几乎所有的应用抛出异常                                             |
| ApplicationException   | 本作为自定义应用异常类的基类，但 CLR 抛出的应用也派生自此类。<br />因此推进自定义异常类直接继承自 Exception |
| StackOverflowException | 分配给栈的内存区域已满。致命错误，有时甚至无法进入 finally                                |
| EndOfStreamException   | 数据源的数据流读到文件末尾                                                    |
| OverflowException      | checked 环境下将 -40 的 int 转换为 uint                                  |

### 捕获

```csharp
class CException
{
    void catchE()
    {
        try
        {
            // code
            throw new OverflowException();
        }
        catch(Exception e) when (e.InnerException == null) // 筛选
        {
            // exception
            // 额外信息
            var data = e.Data;
            // 链接到一个帮助文件
            var link = e.HelpLink;
            // 若此 Exception 为 catch 段抛出, 则此处包含 catch 获取的异常
            var inner = e.InnerException;
            // 描述文本
            var msg = e.Message;
            // 异常程序或对象名
            var source = e.Source;
            // 堆栈信息
            var stack = e.StackTrace;
        }
        finally
        {
            // clean up
        }
    }
}
```

# 异步

> 详见 任务与并行编程
> 
> 异步编程: async await 关键字
>     async 只能修饰返回 void 和 Task<> 类型的方法, 且不能修饰 main 方法
>     await 只能用于返回 Task<> 的方法
> 
> 组合器
>     Task.WhenAll(): 每个异步方法都完成后返回 Task 对象
>     Task.WhenAny(): 每个异步方法有一个完成就返回 Task

# 反射 元数据和动态编程

## 反射

> 在运行过程中检查和处理元素的功能

- 枚举类型成员
- 实例化新对象
- 执行对象成员
- 查找类型信息
- 查找程序集信息
- 检查应用于某种类型的自定义特性
- 创作和编译新程序集

### System.Type

> 存储有关类型的引用

#### 创建

- typeof()
- object.GetType()
- Type.GetType("FullName")

#### 属性

- Name                    类名
- FullName                               完全限定名（包括命名空间）
- Namespace                           命名空间
- BaseType                               直接基类
- UnderlyingSystemType       .Net 运行库中映射的类型
- IsAbstract
- IsArray，IsClass，IsEnum，IsInterface，IsPointer，IsPrimitive（一种预定义的基元数据类型）
- IsPublic，IsSealed，IsValueType

#### 方法

- GetConstructor() & GetConstructors()    ConstructorInfo
- GetEvent() & GetEvents()                            EventInfo
- GetField() & GetFields()                               FieldInfo
- GetMember() & GetMembers()                 MemberInfo
- GetMethod() & GetMethods()                   MethodInfo
- GetProperty() & GetProperties()               PropertyInfo

### 程序集

#### 获取所在程序集引用

- Assembly assembly = Assembly.GetAssembly(Type)
- Assembly assembly = Assembly.Load("程序集名")
- Assembly assembly = Assembly.LoadFrom("程序集完整路径名")

#### 方法

- GetTypes()                获取所有程序集中定义的类型
- GetCustomAttributes()       获取所有程序集中定义的特性
   - 若传入一个 Type 对象，则返回该类型的所有特性

## 自定义特性（类似于注解）

### 创建

> 继承于 Attribute 的类，并使用 AttributeTarget 特性

#### AttributeTarget

- All

- Class，Constructor，Delegate，Enum，Event，Field，GenericParameter，Interface，Method，Parameter，Property，ReturnValue，Struct

- Assembly，Module
  
   - 两个不对应于任何程序元素，而是应该在特性前使用 assembly 或 module 前缀：
     
         [assembly:SomeAssemblyAttribute(Parameters)]
         
           [module:SomeModuleAttribute(Parameters)]

- 可选：AllowMultiple，Inherited
  
   - 由 <ParameterName>=<ParameterValue> 指定
     
      - AllowMultiple：是否可多次应用到同一元素上
        
         - 当设定为 false 时，如下类似调用会产生一个错误（多次调用 FieldName 特性）
           
           ```csharp
               [FieldName("SocialSecurityNumber")]
               [FieldName("NationalInsuranceNumber")]
               public string SocialSercurityNumber { ... }
           ```
     
      - Inherited：特性在应用到类或接口时，是否由派生类和接口继承
        
         - 当设定为 true 时
            - 应用到类或接口的特性也可以应用到所派生的类或接口上
            - 应用到属性或方法上，自动应用到该属性或方法的重写版本上

#### 参数

- 必选参数：由类的构造函数参数定义
- 可选参数：公共属性或字段定义

### 获取

> Assembly/XXXInfo 对象的 GetCustomAttributes()/GetCustomAttribute() 方法

## dynamic 类型

> dynamic 类型：允许编写时期忽略编译期间类型检查代码，编译器假定 dynamic 类型的对象定义的任何操作都是有效的

#### DLR

> Dynamic Language Runtime，动态语言运行时
> 
>  允许添加动态语言，并使 C# 具备这些动态语言相同的某些动态功能

##### DLR ScriptRuntime

> 为应用程序添加脚本功能，使应用程序可以利用脚本完成工作
> 
> 支持语言：IronPython，IronRuby，需要通过 NuGet 添加
> 
> 下面例子以 Python 为例

- 启动 ScriptRuntime 对象：包含环境的全局状态
  
   - ScriptRuntime.CreateFromConfiguration()
  
   - App.config：configuration 必须是根元素的第一个子元素
     
     ```xml
     <configuration>
         <configSections>
             <section name="microsoft.scripting" 
                      type="Microsoft.Scripting.Hosting.Configuration.Section, Microsoft.Scripting" />
         </configSections>
     
         <microsoft.scripting>
             <languages>
                 <language names="IronPython;Python;py"
                           extensions=".py"
                           displayName="IronPython 2.7.5"
                           type="IronPython.Runtime.PythonContext, IronPython" />
             </languages>
         </microsoft.scripting>
     </configuration>
     ```

- 设置合适的 ScriptEngine：完成执行脚本代码的工作；提供 ScriptSource 和 ScriptScope
  
   - ScriptRuntime.GetEngine
  
   - 1# 使用 ScriptSource 提交
     
      - 创建 ScriptSource：允许访问脚本，表示脚本的源代码
         - ScriptEngine.CreateScriptSourceFromFile
      - 创建 ScriptScope：名称空间。用于给脚本传入传出参数
         - ScriptEngine.CreateScope
         - ScriptScope.SetVariable
         - ScriptScope.GetVariable
      - 执行
         - ScriptScope.execute(scope)
  
   - 2# 生成 dynamic 对象，手动调用
     
     ```csharp
      dynamic pyObject = ScriptEngine.UseFile
      pyObject.Xxx()
     ```

## DynamicObject 和 ExpandoObject

> 创建自己的动态对象

### DynamicObject

> 从 DynamicObject 类派生，并复写某些方法

```csharp
    public class WroxDynamicObject: DynamicObject
    {
        private Dictionary<string, object> _dynamicData = new Dictionary<string, object>();

        public override bool TryGetMember(GetMemberBinder binder, out object result)
        {
            bool success = false;
            result = null;
            if (_dynamicData.ContainsKey(binder.Name))
            {
                result = _dynamicData[binder.Name];
                success = true;
            }
            else
            {
                result = "Property Not Found!";
                success = false;
            }
            return success;
        }

        public override bool TrySetMember(SetMemberBinder binder, object value)
        {
            _dynamicData[binder.Name] = value;
            return true;
        }

        public override bool TryInvokeMember(InvokeMemberBinder binder, object[] args, out object result)
        {
            dynamic method = _dynamicData[binder.Name];
            result = method(args);
            return result != null;
        }
    }
```

### ExpandoObject

> 直接创建 ExpandoObject 对象

# 任务和并行编程

## Parallel

> Parallel：对线程的抽象，提供对数据和任务的并行性
> 
> 若需要更多控制并行性，如设置优先级，就需要使用 Thread 类
> 
> action 使用 async 修饰，否则还是在主线程执行（实际上返回 Task 对象）
> 
> 主线程为其他后台任务线程的守护线程

### Parallel.For

> 并行迭代
> 
> 迭代顺序不确定

```csharp
// Parallel.For(int fromInclusive, int toExclusive, Action<int> action)
Parallel.For(0, 10, async i => {
    await Task.Delay(300);
});
```

#### Action 泛型：int

> int 为前面 fromInclusive 到 toExclusive 之间的数字

#### Action 泛型：int, int

> 第二个 int 表示迭代到该序列时中断，其他高于该数字的在启动后直接中断

#### Action 泛型：int, ParallelLoopState

> 第二个参数 ParallelLoopState，可以通过该对象的 Break() 或 Stop() 方法结束循环

#### 参数：ParallelOptions

- CancellationToken：传递取消通知
- MaxDegreeOfParallelism：最大并发数
- TaskScheduler：内部调度机制

#### 泛型重载

> 有个带有 <TLocal> 泛型的版本，可定义：
> 
>     用于初始化调用的 Func<TLocal> 委托，在 Action 委托之前调用
>     
>     用于结束调用的 Action<TLocal> 委托
> 
> 而且，在传递 Action<int, int> 委托时，达到第二个 int，Func 委托仍会执行

### Parallel.ForEach

> 并行迭代迭代器
> 
> 迭代顺序不确定
> 
> ParallelOptions，ParallelLoopState，TLocal 详见 For

```csharp
// Parallel.ForEach<T>(IEnumerable<T> items, Action<T> action)
string[] data = {
    "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten"
};
Parallel.ForEach<string>(data, async item => {
    await Task.Delay(300);
});
```

### Parallel.Invoke

> 并行迭代
> 
> 迭代顺序不确定
> 
> ParallelOptions 详见 For

```csharp
Action a1 = () => Console.WriteLine("Task 1");
Action a2 = () => Console.WriteLine("Task 2");
Action a3 = () => Console.WriteLine("Task 3");
Action a4 = () => Console.WriteLine("Task 4");
Action a5 = () => Console.WriteLine("Task 5");
Parallel.Invoke(a1, a2, a3, a4, a5);
```

## Task

> 表示将完成的某个工作单元

### 创建

- 使用 TaskFactory, 将要执行的任务传递给 TaskFactory.StartNew()
- 使用 Task.Factory.StartNew() 方法, 访问 Task 内置 Factory
- 使用 Task 构造 (此时指定 Created 状态), 调用 Start() 方法
- 使用 Task.Run() 方法, 立即执行任务

### 启动

- Task.Start() / TaskFactory.Start()

#### 使用线程池

- TaskFactory.StartNew
- Task.Factory.StartNew
- new Task().Start()
- Task.Run

#### 单独线程任务

> 不会使用线程池的任务, 而是创建一个新线程

- new Task(..., TaskCreationOptions.LongRunning).Start()

#### 同步任务

> 先在主线程调用, 而后在创建的 Task 上调用

- new Task().RunSynchronously()

### 返回结果

> 使用带泛型的 Task 类，泛型为返回值类型
> 
> 执行后返回 Future 类

```csharp
void TaskWithResult()
{
    Task<Tuple<int, int>> t1 = new Task<Tuple<int, int>>(TaskMethodResult, Tuple.Create(8, 3), TaskCreationOptions.LongRunning);
    t1.Start();
    Console.WriteLine(t1.Result);
    t1.Wait();
    Console.WriteLine($"result from task: {t1.Result.Item1} {t1.Result.Item2}");
}
```

### 连续任务

> Task.ContinueWith()
> 
> 可通过 TaskContinuationOptions 指定在上一个任务成功/失败/取消时启动

```csharp
void TaskContinue()
{
    Action task1;
    Action task2;

    Task t1 = new Task(task1);
    Task t2 = t1.ContinueWith(task2);
    Task t3 = t2.ContinueWith(task2);
    Task t4 = t3.ContinueWith(task2);
    t1.Start();
}
```

### 任务层次结构

> 可由父任务创建子任务
> 
> 若要取消子任务与父任务的绑定状态, 使用 TaskCreationOption.DetachedFromParent

### 其他方法

- Task.FromResult: 创建一个带结果的任务
- Task.WhenAll / Task.WhenAny: 创建不阻塞的任务调用, 返回 Task 对象
- Task.WaitAll / Task.WaitAny: 创建阻塞的任务调用, 即时调用
- Task.Delay: 延时
- Task.Yield: 释放 CPU 资源, 让其他任务运行; 没有其他任务时, 立即执行此任务

### 任务取消

```csharp
        void TaskCancel()
        {
            var cts = new CancellationTokenSource();
            cts.Token.Register(() => Console.WriteLine("*** token canceled"));
            cts.CancelAfter(500);

            Task t1 = Task.Run(() =>
            {
                try
                {
                    // Do Something
                }
                catch (AggregateException e)
                {
                    Console.WriteLine($"exception: {e.GetType().Name}, {e.Message}");
                    foreach (var innerExceoption in e.InnerExceptions)
                    {
                        Console.WriteLine($"inner exception: {e.InnerException.GetType()}, ${e.InnerException.Message}");
                    }
                }
                catch (Exception e)
                {
                    Console.WriteLine($"exception: {e.GetType().Name}, {e.Message}");
                }
            }, cts.Token);
        }
```

## 数据流

> 数据并行性：Parallel类，Task类，Parallel LinQ
> 
> Task Parallel Library Data Flow（TPL Data Flow）

### 动作块

> TPL Data Flow 的核心，作为提供数据的源或接收数据的目标

```csharp
// 例：将字符串的信息写入控制台
// 定义一个 ActionBlock
var processInput = new ActionBlock<string>(s => Console.WriteLine($"user input: {s}"));
bool exit = false;
while(!exit)
{
    // 读取用户输入
    String input = Console.ReadLine();
    if (string.Compare(input, "exit", ignoreCase: true) == 0)
    {
        exit = true;
    }
    else
    {
        // 将输入的字符写入 ActionBlock
        processInput.Post(input);
    }
}
```

### 源和目标数据块

- 数据块都实现了 IDataBlock 接口

- 目标块：实现 ITargetBlock

- 数据源块：实现 ISourceBlock

```csharp
// 例：一个同时实现了 ITargetBlock 和 ISourceBlock 接口的数据块：BufferBlock
BufferBlock<string> s_buffer = new BufferBlock<string>();
Task t1 = Task.Run(() => Producer(s_buffer));
Task t2 = Task.Run(() => Consumer(s_buffer));
Task.WaitAll(t1, t2);

private void Producer(BufferBlock<string> buffer)
{
    bool exit = false;
    while(!exit)
    {
        string input = Console.ReadLine();
        if (string.Compare(input, "exit", ignoreCase: true) == 0)
        {
            exit = true;
        }
        else
        {
            buffer.Post(input);
        }
    }
}

private async void Consumer(BufferBlock<string> buffer)
{
    while(true)
    {
        string data = await buffer.ReceiveAsync();
        Console.WriteLine($"user input: {data}");
    }
}
```

### 连接块

> 连接多个块，形成管道：TransformBlock

```csharp
ITargetBlock<string> SetupPipeline()
{
    var fileNameForPath = new TransformBlock<string, IEnumerable<string>>(p => GetFileNames(p));
    var lines = new TransformBlock<IEnumerable<string>, IEnumerable<string>>(f => LoadLines(f));
    var words = new TransformBlock<IEnumerable<string>, IEnumerable<string>>(l => GetWords(l));
    var display = new ActionBlock<IEnumerable<string>>(words2 =>
    {
        foreach(var word in words2)
        {
            Console.WriteLine(word);
        }
    });
    fileNameForPath.LinkTo(lines);
    lines.LinkTo(words);
    words.LinkTo(display);
    return fileNameForPath;
}

block.SetupPipeline().Post(".");
```

# 任务同步

- 尽力使同步要求最低，同步很复杂，且会阻塞线程，尝试避免共享状态
- 类的静态成员应该是线程安全的。通常，.Net Framework 的类满足这个要求
- 实例状态不需要是线程安全的。为得到最佳性能，最好在类的外部使用同步功能，且不对类的每个成员使用同步功能

## Lock

```csharp
lock(object)
{
    // synchronized region for obj
}
// 3 Monitor 类：lock 的本质
//     与 lock 相比，可添加一个等待被锁定的超时值，不会无限制的等待被锁定
//    可使用 TryEnter 方法


// 4 SpinLock 结构
//    使用与 Monitor 基本相同，只是多了
//         IsHide
//        IsHeldByCurrentThread
//     方法
// *** 注意 SpinLock 定义为结构 *** 因此通过 ref 传递 ***

// 5 
```

## Interlocked 类

> 线程安全的对整型的递增，递减，交换，取值的方法

## Monitor 类

> lock 的本质
> 
> 与 lock 相比，可添加一个等待被锁定的超时值，不会无限制的等待被锁定
> 
> 可使用 TryEnter 方法

```csharp
object key = new object();
Monitor.Enter(key);
try
{
    // synchronized region for obj
}
finally
{
    Monitor.Exit(key);
}
```

## SpinLock 结构

> 使用与 Monitor 基本相同，只是多了
> 
>     IsHide
>     
>     IsHeldByCurrentThread
> 
> 方法
> 
> 注意 SpinLock 定义为结构，因此通过 ref 传递

## WaitHandle 基类

> 抽象类，用于等待不同的信号
> 
> 等待的句柄也可以由简单的异步委托使用

```csharp
// 异步委托的 BeginInvoke 方法返回一个 IAsyncResult 接口对象，使用该对象可以用 AsyncWaitHandle 属性访问 WaitHandle 基类
// 调用 WaitOne 或者超时发生时，线程会等待接收一个与等待句柄相关的信号
// 调用 EndInvoke 方法，线程会最终阻塞，直到得到结果为止

public delegate int TakesAWhileDelegate(int x, int ms);

// Main
TakesAWhileDelegate d1 = TakesAWhile;
IAsyncResult ar = d1.BeginInvoke(1, 3000, null, null);
while (true)
{
    if (ar.AsyncWaitHandle.WaitOne(50))
    {
        WriteLine("Can get the result now");
        break;
    }
}
int result = d1.EndInvoke(ar);
WriteLine($"Result: {result}");

// int TakesAWhile(int x, int ms)
Task.Delay(ms).Wait();
return 42;

// 控制台输出：
// Can get the result now
// Result: 42
```

## Mutex 类

> 互斥
> 
> 类似于 Monitor，只有一个线程能拥有锁定，只有一个县城能获得互斥锁定，访问到受互斥保护的同步代码区域

```csharp
bool createdNew;
// 可定制参数：锁最初是否应由主调线程持有，互斥名，获得互斥是否已存在的信息
// 第三个参数为是否为新建，若返回 false 则表示互斥已经定义
// 互斥可在另一个进程中定义，操作系统可以识别有名称的互斥，可由进程间共享；未命名则不能在进程间共享
Mutex mutex = new Mutex(false, "CSharpMutex", out createdNew);
// WaitOne 获得互斥锁定，称为互斥锁的拥有者
if (mutex.WaitOne())
{
    try
    {
        // synchronized region
    }
    finally
    {
        // 释放互斥锁
        mutex.ReleaseMutex();
    }
}
else 
{
    // some problem happened while waiting
}
```

> 由于系统能识别有名称的互斥，因此可以禁止应用启动两次

```csharp
bool mutexCreated;
Mutex mutex = new Mutex(false, "SingletonWinAppMutex", out mutexCreated);
if (mutexCreated)
{
    MessageBox.Show("You can only start one instance of the application");
    Application.Current.Shutdown();
}
```

## Semaphore 类

> 信号量类似于互斥，但信号量可以由多个线程使用，是一种计数的互斥锁定
> 
> 可以设置可同时访问资源的线程数
> 
>     Semaphore        可以命名，使用系统范围内的资源，允许在不同进程之间同步
>     
>     SemaphoreSlim    较短等待时间进行了优化的轻型版本

```csharp
        static void Main()
        {
            int taskCount = 6;
            int semaphoreCount = 3;

            SemaphoreSlim semaphore = new SemaphoreSlim(semaphoreCount, semaphoreCount);
            Task[] tasks = new Task[taskCount];
            for (int i = 0; i < taskCount; i++)
            {
                tasks[i] = Task.Run(() => TaskMain(semaphore));
            }

            Task.WaitAll(tasks);
            WriteLine("All tasks finished");

            ReadLine();
        }

        public static void TaskMain(SemaphoreSlim semaphore)
        {
            bool isCompleted = false;
            while (!isCompleted)
            {
                // 等待锁定信号量
                if (semaphore.Wait(600))
                {
                    try
                    {
                        WriteLine($"Task {Task.CurrentId} locks the semaphore");
                        Task.Delay(2000).Wait();
                    }
                    finally
                    {
                        WriteLine($"Task {Task.CurrentId} releases the semaphore");
                        semaphore.Release();
                        isCompleted = true;
                    }
                }
                else
                {
                    WriteLine($"Timeout for task {Task.CurrentId}; wait again");
                }
            }
        }
```

## Events 类

> 事件也是一个系统范围内的资源同步方法
> 
>     System.Threading.ManualResetEvent
>     
>     System.Threading.ManualResetEventSlim
>     
>         Set()        发送信号
>     
>         Reset()        返回不发信号的状态
>     
>         WaitOne()    等待信号锁
>     
>     System.Threading.AutoResetEvent
>     
>         如果一个线程等待自动重置的事件发信号，当第一个线程等待状态结束时，事件自动变为不发信号状态
>     
>     System.Threading.CountdownEvent

```csharp
/*
Task 2 starts calculation
Task 3 starts calculation
Task 4 starts calculation
Task 1 starts calculation
Task 4 is ready
finished task for 3, result: 10
Task 1 is ready
finished task for 0, result: 4
Task 3 is ready
finished task for 2, result: 8
Task 2 is ready
finished task for 1, result: 6
*/
namespace CSharpDemo.Chapter22.Calculator
{

    class Program
    {
        static void Main()
        {
            const int taskCount = 4;

            var mEvents = new ManualResetEventSlim[taskCount];
            var waitHandles = new WaitHandle[taskCount];
            var calcs = new Calculator[taskCount];

            for (int i = 0; i < taskCount; i++)
            {
                int i1 = i;
                mEvents[i1] = new ManualResetEventSlim(false);
                waitHandles[i1] = mEvents[i1].WaitHandle;
                calcs[i1] = new Calculator(mEvents[i1]);
                Task.Run(() => calcs[i1].Calculation(i1 + 1, i1 + 3));
            }

            for (int i = 0; i < taskCount; i++)
            {
                int index = WaitHandle.WaitAny(waitHandles);
                if (index == WaitHandle.WaitTimeout)
                {
                    WriteLine("Timeout!");
                }
                else
                {
                    mEvents[index].Reset();
                    WriteLine($"finished task for {index}, result: {calcs[index].Result}");
                }
            }

            ReadLine();
        }
    }

    public class Calculator
    {
        private ManualResetEventSlim _mEvent;
        public int Result { get; set; }

        public Calculator(ManualResetEventSlim ev)
        {
            _mEvent = ev;
        }

        public void Calculation(int x, int y)
        {
            WriteLine($"Task {Task.CurrentId} starts calculation");
            Task.Delay(new Random().Next(3000)).Wait();
            Result = x + y;

            // signal the event-completed!
            WriteLine($"Task {Task.CurrentId} is ready");
            _mEvent.Set();
        }
    }
}
```

> 可用 CountdownEvent 简化

```csharp
/*
Task 3 starts calculation
Task 1 starts calculation
Task 2 starts calculation
Task 4 starts calculation
Task 2 is ready
Task 1 is ready
Task 3 is ready
Task 4 is ready
all finished
task for 0, result 4
task for 1, result 6
task for 2, result 8
task for 3, result 10
*/
namespace CSharpDemo.Chapter22.Calculator
{

    class Program
    {
        static void Main()
        {
            const int taskCount = 4;

            var cEvent = new CountdownEvent(taskCount);
            var calcs = new Calculator[taskCount];

            for (int i = 0; i < taskCount; i++)
            {
                calcs[i] = new Calculator(cEvent);
                int i1 = i;
                Task.Run(() => calcs[i1].Calculation(i1 + 1, i1 + 3));
            }
            cEvent.Wait();
            WriteLine("all finished");
            for (int i = 0; i < taskCount; i++)
            {
                WriteLine($"task for {i}, result {calcs[i].Result}");
            }

            ReadLine();
        }
    }

    public class Calculator
    {
        private CountdownEvent _mEvent;
        public int Result { get; set; }

        public Calculator(CountdownEvent ev)
        {
            _mEvent = ev;
        }

        public void Calculation(int x, int y)
        {
            WriteLine($"Task {Task.CurrentId} starts calculation");
            Task.Delay(new Random().Next(3000)).Wait();
            Result = x + y;

            // signal the event-completed!
            WriteLine($"Task {Task.CurrentId} is ready");
            _mEvent.Signal();
        }
    }
}
```

## Barrier 类

> 多个任务分支且以后又需要合并工作的情况
> 
> 激活一个任务，可动态添加其他任务，然后等待所有任务完成

```csharp
var barrier = new Barrier(numberTasks);
// 通知增加一个任务
barrier.AddParticipant();
Task.Run(() => {
    // 每个任务完成后，通知该任务已经达到屏障
    barrier.SignalAndWait();
    // 移除一个任务
    barrier.RemoveParticipant();
});
```

## ReaderWriterLockSlim 类

> 使锁定机制允许锁定多个读取器（而非一个写入器）访问一个资源
> 
> 提供一个锁定功能，如果没有写入器锁定资源，则允许多个读取器访问资源

```csharp
/*
Writer 1 acquired thie lock
Writer 2 waiting for the write lock
current reader count: 0
Writer 2 waiting for the write lock
current reader count: 0
Writer 2 waiting for the write lock
current reader count: 0
Writer 2 waiting for the write lock
current reader count: 0
Writer 2 waiting for the write lock
current reader count: 0
Writer 1 finished
Writer 2 acquired thie lock
Writer 2 finished
reader 4, loop: 0, item: 2
reader 3, loop: 0, item: 2
reader 2, loop: 0, item: 2
reader 1, loop: 0, item: 2
reader 1, loop: 1, item: 3
reader 4, loop: 1, item: 3
reader 3, loop: 1, item: 3
reader 2, loop: 1, item: 3
reader 3, loop: 2, item: 4
reader 4, loop: 2, item: 4
reader 1, loop: 2, item: 4
reader 2, loop: 2, item: 4
reader 1, loop: 3, item: 5
reader 3, loop: 3, item: 5
reader 4, loop: 3, item: 5
reader 2, loop: 3, item: 5
reader 4, loop: 4, item: 6
reader 1, loop: 4, item: 6
reader 3, loop: 4, item: 6
reader 2, loop: 4, item: 6
reader 1, loop: 5, item: 7
reader 4, loop: 5, item: 7
reader 2, loop: 5, item: 7
reader 3, loop: 5, item: 7
*/   
class Program
{
    private static List<int> _items = new List<int>() { 0, 1, 2, 3, 4, 5 };
    private static ReaderWriterLockSlim _rwl = new ReaderWriterLockSlim(LockRecursionPolicy.SupportsRecursion);

    static void Main()
    {
        var taskFactory = new TaskFactory(TaskCreationOptions.LongRunning, TaskContinuationOptions.None);
        var tasks = new Task[]
        {
            taskFactory.StartNew(WriterMethod, 1),
            taskFactory.StartNew(ReaderMethod, 1),
            taskFactory.StartNew(ReaderMethod, 2),
            taskFactory.StartNew(WriterMethod, 2),
            taskFactory.StartNew(ReaderMethod, 3),
            taskFactory.StartNew(ReaderMethod, 4)
        };
        Task.WaitAll(tasks);

        ReadLine();
    }

    public static void ReaderMethod(object reader)
    {
        try
        {
            _rwl.EnterReadLock();
            for (int i = 0; i < _items.Count; i++)
            {
                WriteLine($"reader {reader}, loop: {i}, item: {_items[i]}");
                Task.Delay(40).Wait();
            }
        }
        finally
        {
            _rwl.ExitReadLock();
        }
    }

    public static void WriterMethod(object writer)
    {
        try
        {
            while(!_rwl.TryEnterWriteLock(50))
            {
                WriteLine($"Writer {writer} waiting for the write lock");
                WriteLine($"current reader count: {_rwl.CurrentReadCount}");
            }
            WriteLine($"Writer {writer} acquired thie lock");
            for (int i = 0; i < _items.Count; i++)
            {
                _items[i]++;
                Task.Delay(50).Wait();
            }
            WriteLine($"Writer {writer} finished");
        }
        finally
        {
            _rwl.ExitWriteLock();
        }
    }
}
```

## Timer 类

> 计时器

### System.Threading.Timer

```csharp
// Timer(TimerCallback委托, 传入参数, 第一次执行的时间, 多次执行的间隔 只执行一次则传入-1)
var t1 = new Timer(TimeAction, null, TimeSpan.FromSeconds(2), TimeSpan.FromSeconds(3));
// 更改调用时间间隔
t1.Change();
```

### System.Windows.Threading.DispatcherTimer

> 基于 XAML 的计时器
> 
> 事件函数在 UI 线程调用

```csharp
private DispatcherTimer _timer = new DispatcherTimer();
_timer.Tick += (sender, e) => { ... };
_timer.Interval = TimeSpan.FromSeconds(10);
_timer.Start();
_timer.Stop();
```

# 文件和流

> 通用 Windows 平台（UWP）应用中，只能访问特定的目录，或者让用户选择文件。
> 
> 使用流，可以压缩数据，利用内存映射的文件和管道在不同任务中共享数据

## 管理文件系统

### FileSystemInfo

> 表示任何文件系统对象的基类

### FileInfo 和 File

> 表示文件系统的文件
> 
> File 类只包含静态方法，不能实例化。只要路径就可以执行一个操作，可省去创建 .Net 对象的开销
> 
> FileInfo 类包含与 File 类似的公共方法，但是有状态的，且成员都不是静态的，需要实例化。要对同一个文件进行多个操作使用这些比较有效，因为包含了读取适合文件系统对象的身份验证和其他信息，不需要再次检查文件的详细内容
> 
> FileInfo 提供文件属性的访问，但只有 创建时间和最后一次访问时间是可修改的

- Create：创建文件
- WriteAllText：创建文件并写入内容
- CopyTo：复制文件
- MoveTo：移动文件
- Delete：删除
- Exists：是否存在该文件；该属性要求文件系统对象必须具备适当的类型
- 文件读写
   - File.ReadAllText：一次性读入所有文件内容
   - File.WriteAllText：一次性写入所有内容到文件
   - File.ReadLines：无需等待读完所有数据，即可遍历已读文件内容

### DirectoryInfo 和 Directory

> 表示文件系统的文件夹
> 
> 文件夹：包含文件并用于组织文件系统的对象。Windows 平台的术语，其他平台成为 目录
> 
> Directory 类只包含静态方法，不能实例化。只要路径就可以执行一个操作，可省去创建 .Net 对象的开销
> 
> DirectoryInfo 类包含与 Directory 类似的公共方法，但是有状态的，且成员都不是静态的，需要实例化。要对同一个文件夹进行多个操作使用这些比较有效，因为包含了读取适合文件系统对象的身份验证和其他信息，不需要再次检查文件夹的详细内容

- CreateDirectory：创建文件夹

- CopyTo：复制文件

- MoveTo：移动文件

- Delete：删除文件夹。只能删除空文件夹。

- Exists：是否存在该文件；该属性要求文件系统对象必须具备适当的类型

- GetFiles：返回所有文件，可设置搜索

- EnumerateFiles：立即返回所有文件，可设置搜索，可在完全返回前索引已返回文件

- GetDirectories：返回所有文件夹，可设置搜索

- EnumerateDirectories：立即返回所有文件夹，可设置搜索，可在完全返回前索引已返回文件

### Path

> 包含可用于处理路径名的静态方法

- Combine：组合路径，放置遗漏单个分隔符或使用太多字符
- GetXxx：获取路径的各个部分
- VolumeSeparatorChar：分割硬盘的分隔符，win下：
- DirectorySeparatorChar：分割文件夹的分隔符，win下：\
- AltDirectorySeparatorChar：分割文件的分隔符，win下：/
- PathSeparator：分割不同路径的分隔符，win下：;
- GetTempPath：用户临时文件夹
- GetTempFileName：创建临时文件名，不包含文件夹
- GetRandomFileName：创建随机文件名

### Environment

> 有关当前环境和平台的信息以及操作它们的方法

- GetFolderPath：获取系统特殊文件夹的目录路径
- GetEnvironmentVariable：检索环境变量
   - "HOMEDRIVE"：系统所在磁盘驱动器
   - "HOMEPATH"：当前用户目录，不包含磁盘驱动器
- 其他功能详见 MSDN

### DriveInfo

> 提供指定驱动器的信息。
> 
> 可扫描系统，提供可用驱动器列表和任何驱动器的大量细节

```csharp
/*
Drive name: C:\
Format: NTFS
Type: Fixed
Root directory: C:\
Volumn label:
Free space: 42469195776
Available space: 42469195776
Total size: 127281393664

Drive name: D:\
Format: NTFS
Type: Fixed
Root directory: D:\
Volumn label:
Free space: 253473484800
Available space: 253473484800
Total size: 1000203833344
*/
DriveInfo[] drives = DriveInfo.GetDrives();
foreach (DriveInfo drive in drives)
{
    if(drive.IsReady)
    {
        WriteLine($"Drive name: {drive.Name}");
        WriteLine($"Format: {drive.DriveFormat}");
        WriteLine($"Type: {drive.DriveType}");
        WriteLine($"Root directory: {drive.RootDirectory}");
        WriteLine($"Volumn label: {drive.VolumeLabel}");
        WriteLine($"Free space: {drive.TotalFreeSpace}");
        WriteLine($"Available space: {drive.AvailableFreeSpace}");
        WriteLine($"Total size: {drive.TotalSize}");
        WriteLine();
    }
}
```

## 流

> 流：用于传输数据的对象
> 
> - 读取流：数据从外部源传入到程序中
> - 写入流：数据从程序中传出到外部源
> - 随机存取：某些流允许随机存取，允许将读写游标在流的不同位置改变
> 
> 外部源
> 
> - 网咯：使用网络协议读写网络上的数据，目的是选择数据或从另一个计算机发送数据
> - 管道：读写到命名管道上
> - 内存：把数据度写到一个内存区域上
> 
> 用完后要及时释放，或使用 using

### FileStream

> 用于二进制文件中读写二进制数据

```csharp
// fileName: 要访问的文件
// FileMode：打开文件的模式
// FileAccess：访问文件的方式
// FileShare：共享访问策略
new FileStream(fileName, FileMode.Open, FileAccess.Read, FileShare.Read);
File.OpenRead(fileName); // FileAccess.Read, FileShare.Read
```

- CanRead，CanWrite，CanSeek，CanTimeout

- Read()，Write()

- CopyTo()

- 随机访问流
  
  > 随机读取, 可以快速访问文件特定位置
  
  ```csharp
          void RandomAccessSample()
          {
              try
              {
                  using (FileStream stream = File.OpenRead("./samplefile.data"))
                  {
                      byte[] buffer = new byte[RECORDSIZE];
                      do
                      {
                          try
                          {
                              WriteLine("record number (or 'bye' to End): ");
                              string line = ReadLine();
                              if (line.ToUpper().CompareTo("BYE") == 0) break;
  
                              int record;
                              if(int.TryParse(line, out record))
                              {
                                  stream.Seek((record - 1) * RECORDSIZE, SeekOrigin.Begin);
                                  stream.Read(buffer, 0, RECORDSIZE);
                                  string s = Encoding.UTF8.GetString(buffer);
                                  WriteLine($"record: {s}");
                              }
                          }
                          catch (Exception ex)
                          {
  
                              WriteLine(ex.Message);
                          }
                      } while (true);
                      WriteLine("finished");
                  }
              }
              catch (FileNotFoundException)
              {
  
                  WriteLine("Create the sample file using the option -sample first");
              }
          }
  ```

- 分析编码：BOM
  
  > BOM: Byte Order Mark, 字节顺序标记, 提供了文件如何编码的信息
  
  ```csharp
          Encoding GetEncoding(FileStream stream)
          {
              if (!stream.CanSeek)
                  throw new ArgumentException("Require a stream that can seek");
  
              Encoding encoding = Encoding.ASCII;
  
              byte[] bom = new byte[5];
              int nRead = stream.Read(bom, 0, 5);
              if (bom[0] == 0xff && bom[1] == 0xfe && bom[2] == 0 && bom[3] == 0)
              {
                  WriteLine("UTF-32");
                  stream.Seek(2, SeekOrigin.Begin);
                  return Encoding.Unicode;
              }
              else if (bom[0] == 0xfe && bom[1] == 0xff)
              {
                  WriteLine("UTF-16, big endian");
                  stream.Seek(2, SeekOrigin.Begin);
                  return Encoding.BigEndianUnicode;
              }
              else if (bom[0] == 0xef && bom[1] == 0xbb && bom[2] == 0xbf)
              {
                  WriteLine("UTF-8");
                  stream.Seek(3, SeekOrigin.Begin);
                  return Encoding.UTF8;
              }
              stream.Seek(0, SeekOrigin.Begin);
              return encoding;
          }
  ```

## 读取器和写入器

### StreamReader / StreamWriter

> 专门用于读写文本格式的流
> 
> 可以使用 ReadLine，WriteLine
> 
> 不必关心编码问题，Writer 默认 UTF-8
> 
> 使用 EndOfStream 判断是否已经到结尾
> 
> 当其释放后，内保存的 Stream 也会同时释放

### BinaryReader / BinaryWriter

> 专门用于读写二进制格式的流

## 压缩文件

### DeflateStream

> 压缩/解压缩
> 
> 压缩 使用抑制算法, RFC 1951

```csharp
        // 压缩: DeflateStream -> CompressionMode.Compress
        void CompressFile(string fileName, string compressedFileName)
        {
            using (FileStream inputStream = File.OpenRead(fileName))
            {
                FileStream outputStream = File.OpenWrite(compressedFileName);
                using (var compressStream = new DeflateStream(outputStream, CompressionMode.Compress))
                {
                    inputStream.CopyTo(compressStream);
                }
            }
        }

        // 解压缩: DeflateStream -> CompressionMode.Decompress
        void DecompressFile(string fileName)
        {
            FileStream inputStream = File.OpenRead(fileName);
            using (MemoryStream outputStream = new MemoryStream())
            {
                using (var compressStream = new DeflateStream(inputStream, CompressionMode.Decompress))
                {
                    compressStream.CopyTo(outputStream);
                    outputStream.Seek(0, SeekOrigin.Begin);
                    using (var reader = new StreamReader(outputStream, 
                        Encoding.UTF8, detectEncodingFromByteOrderMarks: true, bufferSize: 4096, leaveOpen: true))
                    {
                        string result = reader.ReadToEnd();
                        WriteLine(result);
                    }
                }
            }
        }
```

### GZipStream

> 后台使用 DeflateStream，增加了循环冗余检验
> 
> Win下可直接打开 DeflateStream 压缩的文件，但不能直接打开 GZipStream 压缩的文件
> 
> ZipArchive：创建和读取 Zip 文件

```csharp
        void CreateZipFile(string directory, string zipFile)
        {
            FileStream zipStream = File.OpenWrite(zipFile);
            using(var archive = new ZipArchive(zipStream, ZipArchiveMode.Create))
            {
                IEnumerable<string> files = Directory.EnumerateFiles(directory, "*", SearchOption.TopDirectoryOnly);
                foreach (var file in files)
                {
                    ZipArchiveEntry entry = archive.CreateEntry(Path.GetFileName(file));
                    using (FileStream inputStream = File.OpenRead(file))
                    {
                        using (Stream outputStream = entry.Open())
                        {
                            inputStream.CopyTo(outputStream);
                        }
                    }
                }
            }
        }
```

## 观察文件修改

> FileSystemWatcher：监视文件的更改，事件在创建、重命名、删除和更改文件时触发

```csharp
var watcher = new FileSystemWatcher(path, filter)
{
    IncludeSubdirectories = true;
}
watcher.Created += (object sender, FileSystemEventArgs e) => {};
watcher.Changed += (object sender, FileSystemEventArgs e) => {};
watcher.Deleted += (object sender, FileSystemEventArgs e) => {};
watcher.Renamed += (object sender, RenamedEventArgs e) => {};
watcher.Error += (object sender, ErrorEventArgs e) => {};
watcher.EnableRaisingEvents = true;
```

## 内存映射

> 允许访问文件，或在不同的进程中共享内存
> 
> 允许使用物理文件或共享的内存（把页面文件作为后备储存器，共享内存可大于可用物理内存）
> 
> 创建内存映射后, 即可创建一个视图, 用于映射完整内存映射文件的一部分, 以此访问, 读写
> 
> 内存映射文件通信时, 必须同步读取器和写入器, 读取器才知道数据何时可用

```csharp
private ManualResetEventSlim _mapCreated = new ManualResetEventSlim(false);
private ManualResetEventSlim _dataWrittenEvent = new ManualResetEventSlim(false);
private const string MAPNAME = "SampleMap";

// Write
using (MemoryMappedFile mappedFile = MemoryMappedFile.CreateOrOpen(MAPNAME, 10000, MemoryMappedFileAccess.ReadWrite))
{
    _mapCreated.Set();
    using (MemoryMappedViewAccessor accessor = mappedFile.CreateViewAccessor(0, 10000, MemoryMappedFileAccess.Write))
    {
        for (int i = 0, pos = 0; i < 100; i++, pos += 4)
        {
            accessor.Write(pos, i);
            await Task.Delay(10);
        }
        _dataWrittenEvent.Set();
    }
}

// Write Using Stream
using (MemoryMappedFile mappedFile = MemoryMappedFile.CreateOrOpen(MAPNAME, 10000, MemoryMappedFileAccess.ReadWrite))
{
    _mapCreated.Set();
    MemoryMappedViewStream stream = mappedFile.CreateViewStream(0, 10000, MemoryMappedFileAccess.Write);
    using (var writer = new StreamWriter(stream))
    {
        writer.AutoFlush = true;
        for (int i = 0; i < 100; i++)
        {
            string s = $"some data {i}";
            await writer.WriteLineAsync(s);
        }
        _dataWrittenEvent.Set();
    }
}

// Read
using (MemoryMappedFile mappedFile = MemoryMappedFile.CreateOrOpen(MAPNAME, 10000, MemoryMappedFileAccess.ReadWrite))
{
    _mapCreated.Set();
    using (MemoryMappedViewAccessor accessor = mappedFile.CreateViewAccessor(0, 10000, MemoryMappedFileAccess.Write))
    {
        for (int i = 0, pos = 0; i < 100; i++, pos += 4)
        {
            accessor.Write(pos, i);
            await Task.Delay(10);
        }
        _dataWrittenEvent.Set();
    }
}

// Read Using Stream
_mapCreated.Wait();
using (MemoryMappedFile mappedFile = MemoryMappedFile.OpenExisting(MAPNAME, MemoryMappedFileRights.Read)) 
{
    MemoryMappedViewStream stream = mappedFile.CreateViewStream(0, 10000, MemoryMappedFileAccess.Read);
    using (var reader = new StreamReader(stream))
    {
        _dataWrittenEvent.Wait();
        for (int i = 0; i < 100; i++)
        {
            long pos = stream.Position;
            string s = await reader.ReadLineAsync();
        }
    }
}
```

## 管道

> 在不同系统之间快速通信；C# 中，管道实现为流
> 
> 命名管道：名称可以用于链接到每一端
> 
> 匿名管道：只能用于一个父子进程之间的通信或不同任务之间的通信

### 命名管道

```csharp
// 创建命名管道服务器，用于读取
// PipeTransmissionMode: 
//     Byte: 连续的流
//     Message: 可检索每条消息
// PipeOptions: 
//     WriteThrough: 立即写入管道而不缓存
// PipeSecurity: 管道安全性
// HandleInheritability: 句柄可继承性
using (var pipeReader = new NamedPipeServerStream(pipeName, PipeDirection.In))
{
    pipeReader.WaitForConnection();
    const int BUFFERSIZE = 256;
    bool completed = false;
    while (!completed)
    {
        byte[] buffer = new byte[BUFFERSIZE];
        int nRead = pipeReader.Read(buffer, 0, BUFFERSIZE);
        string line = Encoding.UTF8.GetString(buffer, 0, nRead);
        WriteLine(line);
        if (line == "bye") completed = true;
    }
}

// 创建命名管道客户端，用于写入
var pipeWriter = new NamedPipeClientStream(".", pipeName, PipeDirection.Out);
using (var writer = new StreamWriter(pipeWriter))
{
    pipeWriter.Connect();
    bool completed = false;
    while (!completed)
    {
        string input = ReadLine();
        if (input == "bye") completed = true;
        writer.WriteLine(input);
        writer.Flush();
    }
}
```

### 匿名管道

```csharp
        void Reader()
        {
            try
            {
                var pipeReader = new AnonymousPipeServerStream(PipeDirection.In, System.IO.HandleInheritability.None);
                using (var reader = new StreamReader(pipeReader))
                {
                    _pipeHandle = pipeReader.GetClientHandleAsString();
                    WriteLine($"pipe handler: {_pipeHandle}");
                    _pipeHandleSet.Set();

                    bool end = false;
                    while (!end)
                    {
                        string line = reader.ReadLine();
                        WriteLine(line);
                        if (line == "end") end = true;
                    }
                    WriteLine("finished reading");
                }
            }
            catch (Exception ex)
            {
                WriteLine(ex.Message);
            }
        }

        void Writer()
        {
            WriteLine("anonymous pipe writer");
            _pipeHandleSet.Wait();

            var pipeWriter = new AnonymousPipeClientStream(PipeDirection.Out, _pipeHandle);
            using (var writer = new StreamWriter(pipeWriter))
            {
                writer.AutoFlush = true;
                WriteLine("starting writer");
                for (int i = 0; i < 5; i++)
                {
                    writer.WriteLine($"Message {i}");
                    Task.Delay(500).Wait();
                }
                writer.WriteLine("end");
            }
        }
```

## 使用 Windows 运行库

> 名称空间：Windows.Storage.Streams
> 
> 《C#高级编程 第10版》P716

# 安全性

## 验证用户信息

> 使用标识符和 principle 获取用户信息
> 
> .Net 中 Principal 类有 WindowsPrincipal，GenericPrincipal，RolePrincipal，派生自 ClaimsPrincipal
> 
> 也可以创建实现了 IPrincipal 接口或派生于 ClaimsPrincipal 的类

### Windows 标识

> 所有标识类，包括 WindowsIdentity，都实现了 IIdentity 接口，定义了 AuthenticationType，IsAuthenticated，Name 属性
> 
> 一般来说，发行人是 AD AUTHIRITY （Active Directory） 的信息更值得信赖

```csharp
WindowsIdentity identitiy = WindowsIdentity.GetCurrent();
if (identitiy == null)
{
    WriteLine("Not a Windows Identity");
    return null;
}

WriteLine($"IdentitiyType: {identitiy}");
WriteLine($"Name: {identitiy.Name}");
WriteLine($"Authenticated: {identitiy.IsAuthenticated}");
WriteLine($"Authentication Type: {identitiy.AuthenticationType}");
WriteLine($"Anonymous? {identitiy.IsAnonymous}");
WriteLine($"Access Token: {identitiy.AccessToken.DangerousGetHandle()}");

WriteLine($"Show Principal information");
WindowsPrincipal principal = new WindowsPrincipal(identity);
```

### WindowsPrincipal

> principal 包含一个标识，提供额外信息，如用户所属角色。
> 
> principal 实现 IPrincipal 接口，提供 IsInRole 方法和 Identity 属性
> 
> Windows 中，用户所属的所有 Windows 映射到角色，重载 IsInRole 方法，以接收安全标识符，角色字符串或 WindowsBuildInRole 枚举值

```csharp
WriteLine($"Show Principal information");
WindowsPrincipal principal = new WindowsPrincipal(identity);
if (principal == null)
{
    WriteLine("Not a Windows Identity");
    return null;
}

WriteLine($"Users? {principal.IsInRole(WindowsBuiltInRole.User)}");
WriteLine($"Administrators? {principal.IsInRole(WindowsBuiltInRole.Administrator)}");
return principal;
```

### 声称

> 声称比角色更强大灵活。
> 
> 声称是一个关于标识（来自权威机构）的语句
> 
> 权威机构如 Active Directory 或 Microsoft Live 账户身份验证服务，建立关于用户的声称
> 
> 添加声称：identity.AddClaim(new Claim("Age", "25"));
> 
> 检索声称：identity.HasClaim，identity.FindAll
> 
>     ClaimTypes 类定义了一组已知类型

## 加密数据

> 机密数据得到保护，使未授权用户不能读取
> 
> 一般以非对称方式交换密钥，然后使用对称方式进行解密
> 
> 使用 System.Security.Cryptography 的类进行加密
> 
> 没有 Cng、Managed 或 CyptoServiceProvider 后缀的类是抽象基类
> 
> Cng（Cryptography Next Generation） 后缀的类为本机 Crypto API 的更新版本，可以使用基于提供程序的模型，编写独立于算法的程序，为利用新 Cryptography CNG API 类
> 
> Managed 后缀表示这个算法用托管代码实现，其他类可能封装了本地 Windows API 调用
> 
> CyptoServiceProvider 后缀用于实现了抽象基类的类

| 类别  | 类                                                                                                                                                                                                                                      | 说明                                                                                                                                                                                                                                                                                                                                              |
|:---:|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|
| 散列  | MD5<br />MD5Cng <br />SHA<br />SHA1Managed<br />SHA1Cng<br />SHA256<br />SHA256Managed<br />SHA256Cng<br />SHA384<br />SHA384Managed<br />SHA384Cng<br />SHA512<br />SHA512Managed<br />SHA512Cng<br />RIPEMD160<br />RIPEMD160Managed | 目的：从任意长的二进制字符串中创建一个长度固定的散列值，算法和数字签名一起保证数据的完整性<br />MD5 比 SHA1 快，SHA1 在抵御暴力攻击方面比较强大<br />MD5 使用 128 位散列长度，SHA1 使用 160 位散列长度，其他 SHA 算法在其名称后面标记散列长度<br />RIPEMD160 算法使用 160 位，替代了 128 位 MD4 和 MD5.RIPEMD                                                                                                                                           |
| 对称  | DES <br />DESCryptoServiceProvider<br />TripleDES<br /> TripleDESCryptoServiceProvider<br />Aes<br />AesCryptoServiceProvider<br />AesManaged <br />RC2<br />RC2CryptoServiceProvider<br />Rijandel<br />RijandelManaged               | 现认为 DES（Data Encryption Standard）是不安全的，因为只有 56 位密钥长度，可在不超过 24h 内破解<br />TripleDES 是 DES 继承者，密钥长度 128 位，但有效安全性只有 112 位<br />AES（Advanced Encryption Standard 高级加密标准）密钥长度 128，192 或 256 位<br />Rijandel 类似 AES，只是在密钥长度选项较多，是美国政府采用的加密标准                                                                                                           |
| 非对称 | DSA<br />DSACryptoServiceProvider<br />ECDsa<br />ECDsaCng<br />ECDiffieHellman<br />ECDiffieHellmanCng<br />RSA<br />RSACryptoServiceProvider<br />RSACng                                                                             | RSA（Rivest，Shamir，Adleman）是第一个用于签名和加密的算法，广泛用于电子商务协议<br />RSACng 是 .Net 4.6 和 .Net 5 Core 的新类，基于 Cng 实现方式<br />DSA（Digital Signature Algorithm，数字签名算法）是用于数字签名的一个美国联邦政府标准<br />ECDSA（Elliptic Curve DSA，椭圆曲线数字签名算法）和 EC-Diffie-Hellman 使用基于椭圆曲线组的算法，比较安全且使用较短的密钥长度<br />DSA密钥长度 1024 位，ECDSA 长度 160 位<br />EC-Diffie-Hellman 算法用于以安全的方式在公共信道中交换私钥 |

### 对称加密

> 创建一个密钥，使用同一个密钥进行加密解密
> 
> 比较快，但要求密钥必须以安全的方式互换

```csharp
    class Program
    {
        private CngKey _aliceKeySignature;
        private byte[] _alicyPubKeyBlob;

        static void Main()
        {
            var p = new Program();
            p.Run();
        }

        // 千万不要用 Encoding 类将加密的数据转化为 string，Encoding 类验证和转换 Unicode 不允许使用的无效值 
        void Run()
        {
            InitAliceKeys();
            byte[] aliceData = Encoding.UTF8.GetBytes("Alice");
            byte[] aliceSignature = CreateSignature(aliceData, _aliceKeySignature);
            WriteLine($"Alice created signature: {Convert.ToBase64String(aliceSignature)}");
            if (VerifySignature(aliceData, aliceSignature, _alicyPubKeyBlob))
            {
                WriteLine("Alice signature verified successfully");
            }
        }

        // 创建新的密钥对
        // 除了使用 CngKey 外，还可以打开存储在密钥存储器的已有密钥，使用 CngKey.Open 访问
        void InitAliceKeys()
        {
            _aliceKeySignature = CngKey.Create(CngAlgorithm.ECDsaP521);
            // 导出公钥
            _alicyPubKeyBlob = _aliceKeySignature.Export(CngKeyBlobFormat.GenericPublicBlob);
        }

        // 创建签名
        byte[] CreateSignature(byte[] data, CngKey key)
        {
            byte[] signature;
            using (var signingAlg = new ECDsaCng(key))
            {
#if NET46
                signature = signingAlg.SignData(data);
                signingAlg.Clear();
#else
                signature = signingAlg.SignData(data, HashAlgorithmName.SHA512);
#endif
            }
            return signature;
        }

        // 检查签名
        bool VerifySignature(byte[] data, byte[] signature, byte[] pubKey)
        {
            bool retValue = false;
            using (CngKey key = CngKey.Import(pubKey, CngKeyBlobFormat.GenericPublicBlob))
            {
                using (var signingAlg = new ECDsaCng(key))
                {
#if NET46
                    retValue = signingAlg.VerifyData(data, signature);
                    signingAlg.Clear();
#else
                    retValue = signingAlg.VerifyData(data, signature, HashAlgorithmName.SHA512);
#endif
                }
            }
            return retValue;
        }
    }
```

### 不对称加密

> 加密解密使用两个不同密钥
> 
>     使用一个公钥进行加密，必须使用对应的私钥进行解密
>     
>     使用一个私钥进行加密，必须使用对应的公钥进行解密    
>     
>     不能从一个密钥中计算出另一个密钥

```csharp
    class Program
    {
        private CngKey _aliceKey;
        private CngKey _bobKey;
        private byte[] _alicePubKeyBlob;
        private byte[] _bobPubKeyBlob;

        static void Main()
        {
            var p = new Program();
            p.RunAsync().Wait();
            ReadLine();
        }

        async Task RunAsync()
        {
            try
            {
                CreateKeys();
                byte[] encrytypedData = await AliceSendsDataAsync("This is a secret message for Bob");
                await BobReceivesDataAsync(encrytypedData);
            }
            catch (Exception ex)
            {
                WriteLine(ex.Message);
            }
        }

        void CreateKeys()
        {
            _aliceKey = CngKey.Create(CngAlgorithm.ECDiffieHellmanP521);
            _bobKey = CngKey.Create(CngAlgorithm.ECDiffieHellmanP521);
            _alicePubKeyBlob = _aliceKey.Export(CngKeyBlobFormat.EccPublicBlob);
            _bobPubKeyBlob = _bobKey.Export(CngKeyBlobFormat.EccPublicBlob);
        }

        async Task<byte[]> AliceSendsDataAsync(string message)
        {
            WriteLine($"Alice sends message: {message}");
            byte[] rawData = Encoding.UTF8.GetBytes(message);
            byte[] encryptedData = null;

            using (var aliceAlgorithm = new ECDiffieHellmanCng(_aliceKey))
            using (CngKey bobPubKey = CngKey.Import(_bobPubKeyBlob, CngKeyBlobFormat.EccPublicBlob))
            {
                byte[] symmKey = aliceAlgorithm.DeriveKeyMaterial(bobPubKey);
                WriteLine($"Alice creates this symmetric key with Bobs public key information: {Convert.ToBase64String(symmKey)}");

                using (var aes = new AesCryptoServiceProvider())
                {
                    aes.Key = symmKey;
                    aes.GenerateIV();
                    using (ICryptoTransform encryptor = aes.CreateEncryptor())
                    using (var ms = new MemoryStream())
                    {
                        using (var cs = new CryptoStream(ms, encryptor, CryptoStreamMode.Write))
                        {
                            await ms.WriteAsync(aes.IV, 0, aes.IV.Length);
                            await cs.WriteAsync(rawData, 0, rawData.Length);
                        }
                        encryptedData = ms.ToArray();
                    }
                    aes.Clear();
                }
            }

            WriteLine($"Alice: message is encrypted: {Convert.ToBase64String(encryptedData)}");
            WriteLine();
            return encryptedData;
        }

        async Task BobReceivesDataAsync(byte[] encryptedData)
        {
            WriteLine("Bob receives encrypted data");
            byte[] rawData = null;

            var aes = new AesCryptoServiceProvider();
            int nBytes = aes.BlockSize >> 3;
            byte[] iv = new byte[nBytes];
            for (int i = 0; i < iv.Length; i++)
            {
                iv[i] = encryptedData[i];
            }

            using (var bobAlgorithm = new ECDiffieHellmanCng(_bobKey))
            using (CngKey alicePubKey = CngKey.Import(_alicePubKeyBlob, CngKeyBlobFormat.EccPublicBlob))
            {
                byte[] symmKey = bobAlgorithm.DeriveKeyMaterial(alicePubKey);
                WriteLine($"Bob creates this symmertic key with Alices public key information: {Convert.ToBase64String(symmKey)}");

                aes.Key = symmKey;
                aes.IV = iv;

                using (ICryptoTransform decryptor = aes.CreateDecryptor())
                using (MemoryStream ms = new MemoryStream())
                {
                    using (var cs = new CryptoStream(ms, decryptor, CryptoStreamMode.Write))
                    {
                        await cs.WriteAsync(encryptedData, nBytes, encryptedData.Length - nBytes);
                    }
                    rawData = ms.ToArray();
                    WriteLine($"Bob decrypts message to: ${Encoding.UTF8.GetString(rawData)}");
                }
                aes.Clear();
            }
        }
    }
```

> 使用 RSA

```csharp
    class Program
    {
        private CngKey _aliceKey;
        private byte[] _alicePubKeyBlob;

        static void Main()
        {
            var p = new Program();
            p.Run();
        }

        void Run()
        {
            byte[] document;
            byte[] hash;
            byte[] signature;
            AliceTask(out document, out hash, out signature);
            BobTask(document, hash, signature);
        }

        void AliceTask(out byte[] document, out byte[] hash, out byte[] signature)
        {
            CreateKeys();
            document = Encoding.UTF8.GetBytes("Best greeting from Alice");
            hash = HashDocument(document);
            signature = AddSignatureToHash(hash, _aliceKey);
        }

        void CreateKeys()
        {
            _aliceKey = CngKey.Create(CngAlgorithm.Rsa);
            _alicePubKeyBlob = _aliceKey.Export(CngKeyBlobFormat.GenericPublicBlob);
        }

        byte[] HashDocument(byte[] document)
        {
            using (var hashAlg = SHA384.Create())
            {
                return hashAlg.ComputeHash(document);
            }
        }

        byte[] AddSignatureToHash(byte[] hash, CngKey key)
        {
            using (var signingAlg = new RSACng(key))
            {
                byte[] signed = signingAlg.SignHash(hash, HashAlgorithmName.SHA384, RSASignaturePadding.Pss);
                return signed;
            }
        }

        void BobTask(byte[] document, byte[] hash, byte[] signature)
        {
            CngKey aliceKey = CngKey.Import(_alicePubKeyBlob, CngKeyBlobFormat.GenericPublicBlob);
            if (!IsSignatureVaild(hash, signature, aliceKey))
            {
                WriteLine("signature not vaild");
                return;
            }
            if (!IsDocumentUnchanged(hash, document))
            {
                WriteLine("document was changed");
                return;
            }
            WriteLine("signature vaild, document unchanged");
            WriteLine($"document from Alice: {Encoding.UTF8.GetString(document)}");
        }

        private bool IsDocumentUnchanged(byte[] hash, byte[] data)
        {
            byte[] newHash = HashDocument(data);
            return newHash.SequenceEqual(hash);
        }

        private bool IsSignatureVaild(byte[] hash, byte[] signature, CngKey key)
        {
            using (var signingAlg = new RSACng(key))
            {
                return signingAlg.VerifyHash(hash, signature, HashAlgorithmName.SHA384, RSASignaturePadding.Pss);
            }
        }
    }
```

## 数据保护 P737

> System.Security.DataProtection，包装本机 Windows Data Protection API
> 
> 日后的检索存储可信信息，但储存媒体不能信任自己，所以要加密储存在主机上

## 资源的访问控制

## 证书发布

# 网络

> System.Net            与较高层操作有关，上传下载，Web 请求等
> 
> System.Net.Socket    与较低层操作有关，直接使用套接字或 TCP/IP 协议等

## HttpClient

> 线程安全的 HttpClient 对象, 一个对象可处理多个请求, 拥有自己的线程池
> 
> HttpClient 对象间的请求会被隔离, 使用 Dispose 释放资源

```csharp
            using (var client = new HttpClient())
            {
                // 包括 PostAsync, PutAsync, DeleteAsync，SendAsync
                var response = await client.GetAsync(NorthwindUrl); 
                if (response.IsSuccessStatusCode)
                {
                    WriteLine($"Response Status Code: {response.StatusCode} {response.ReasonPhrase}");
                    string responseBodyAsText = await response.Content.ReadAsStringAsync();
                    WriteLine($"Received payload of {responseBodyAsText.Length} characters");
                    WriteLine();
                    WriteLine($"{responseBodyAsText.Substring(0, 20)} ......");
                }
            }
```

> 抛出异常

```csharp
                using (var client = new HttpClient())
                {
                    // 传递标题和请求头
                    var headers = client.DefaultRequestHeaders;
                    headers.Add("Accept", "application/json;odata=verbose");
                    foreach (var header in headers)
                    {
                        string value = string.Join(" ", header.Value);
                        WriteLine($"Header: {header.Key} Value: {value}");
                    }
                    WriteLine();

                    var response = await client.GetAsync(NorthwindUrl);
                    // EnsureSuccessStatusCode 在 IsSuccessStatusCode = false 时抛出异常
                    response.EnsureSuccessStatusCode();
                    WriteLine($"Response Status Code: {response.StatusCode} {response.ReasonPhrase}");
                    string responseBodyAsText = await response.Content.ReadAsStringAsync();
                    WriteLine($"Received payload of {responseBodyAsText.Length} characters");
                    WriteLine();
                    WriteLine($"{responseBodyAsText.Substring(0, 20)} ......");
                }
            }
            catch (Exception ex)
            {
                WriteLine(ex.Message);
            }
```

> 自定义请求，可自定义 ClientCertificates，Pipelining，CachePolity，ImpersonationLevel 等

```csharp
var client = new HttpClient(new SampleMessageHandler("error"));
HttpResponseMessage response = await client.GetAsync(NorthwindUrl);

public class SampleMessageHandler: HttpClientHandler
{
    private string _message;

    public SampleMessageHandler(string message)
    {
        _message = message;
    }

    protected override Task<HttpResponseMessage> SendAsync(HttpRequestMessage request, CancellationToken cancellationToken)
    {
        WriteLine($"In SampleMessageHandler {_message}");
        if (_message == "error")
        {
            var response = new HttpResponseMessage(System.Net.HttpStatusCode.BadRequest);
            return Task.FromResult(response);
        }
        return base.SendAsync(request, cancellationToken);
    }
}
```

## DNS 与 IP

### Uri

> Uri: 分析, 组合, 比较 URI
> 
> UriBuilder: 将各部分组合成一个 URI

```csharp
        void UriSample(string url)
        {
            // Uri
            var page = new Uri(url);
            WriteLine($"scheme: {page.Scheme}");
#if NET46
            WriteLine($"host: {page.Host}, type: {page.HostNameType}");
#else
            WriteLine($"host: {page.Host}, type: {page.HostNameType}, idn host: {page.IdnHost}");
#endif
            WriteLine($"port: {page.Port}");
            WriteLine($"path: {page.AbsolutePath}");
            WriteLine($"query: {page.Query}");
            foreach (var segment in page.Segments)
            {
                WriteLine($"segment: {segment}");
            }

            // UriBuilder
            var builder = new UriBuilder();
            builder.Host = "www.cninnovation.com";
            builder.Port = 80;
            builder.Path = "training/MVC";
            Uri uri = builder.Uri;
            WriteLine(uri);
        }
```

### IPAddress

> IP 地址, 可与字节数组互相转化, 可转化 IPv4 与 IPv6

```csharp
        void IPAddressSample(string ipAddressString)
        {
            IPAddress address;
            if (!IPAddress.TryParse(ipAddressString, out address))
            {
                WriteLine($"cannot parse {ipAddressString}");
                return;
            }
            byte[] bytes = address.GetAddressBytes();
            for (int i = 0; i < bytes.Length; i++)
            {
                WriteLine($"byte {i}: {bytes[i]:X}");
            }
            WriteLine($"family: {address.AddressFamily}, map to ipv6: {address.MapToIPv6()}, map to ipv4: {address.MapToIPv4()}");
            // 特殊地址
            // Loopback: 此 ip 代表主机名 localhost, 可绕过网络硬件
            WriteLine($"IPv4 loopback address: {IPAddress.Loopback}");
            WriteLine($"IPv6 loopback address: {IPAddress.IPv6Loopback}");
            WriteLine($"IPv4 broadcast address: {IPAddress.Broadcast}");
            WriteLine($"IPv4 anycase address: {IPAddress.Any}");
            WriteLine($"IPv6 anycase address: {IPAddress.IPv6Any}");
        }
```

### IPHostEntry

> 封装了与某台特定主机相关的信息

- HostName：返回主机名
- AddressList：返回 IPAddress[]

## WebListener 服务器 P718

```csharp
    class HttpServer
    {
        static void hMain(string[] args)
        {
            if (args.Length < 1)
            {
                ShowUsage();
                return;
            }
            StartServerAsync(args).Wait();
            ReadLine();
        }

        private static void ShowUsage()
        {
            WriteLine("Usage: HttpServer Prefix [Prefix2] [Prefix3] [Prefix4]");
        }

        public static async Task StartServerAsync(params string[] prefixs)
        {
            try
            {
                WriteLine("server starting at");
                var listener = new WebListener();
                foreach (var prefix in prefixs)
                {
                    listener.Settings.UrlPrefixes.Add(prefix);
                    WriteLine($"\t{prefix}");
                }
                listener.Start();

                do
                {
                    using (RequestContext context = await listener.AcceptAsync())
                    {
                        context.Response.Headers.Add("content-type", new string[] { "text/html" });
                        context.Response.StatusCode = (int)HttpStatusCode.OK;
                        byte[] buffer = GetHtmlContent(context.Request);
                        await context.Response.Body.WriteAsync(buffer, 0, buffer.Length);
                    }
                } while (true);
            }
            catch (Exception ex)
            {
                WriteLine(ex.Message);
            }
        }

        private static string htmlFormat = "<!DOCTYPE html><html><head><title>{0}</title></head>" +
            "<body>{1}</body></html>";

        private static byte[] GetHtmlContent(Request request)
        {
            string title = "Sample WebListener";

            var sb = new StringBuilder("<h1>Hello from the server</h1>");
            sb.Append("<h2>Header Info</h2>");
            sb.Append(string.Join(" ", GetHeaderInfo(request.Headers)));
            sb.Append("<h2>Request Object Information</h2>");
            sb.Append(string.Join(" ", GetRequestInfo(request)));
            string html = string.Format(htmlFormat, title, sb.ToString());
            return Encoding.UTF8.GetBytes(html);
        }

        private static IEnumerable<string> GetHeaderInfo(HeaderCollection headers) =>
            headers.Keys.Select(key => 
                $"<div>{key}: {string.Join(",", headers.GetValues(key))}</div>");

        private static IEnumerable<string> GetRequestInfo(Request request) =>
            request.GetType().GetProperties().Select(p => 
                $"<div>{p.Name}: {p.GetValue(request)}</div>");
    }
```

## TCP

```csharp

```

## UDP

# Composition

# XML 和 JSON

# 本地化