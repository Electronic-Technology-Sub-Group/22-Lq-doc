
```csharp
[修饰符] 返回值 函数名(形参列表)
{
    // 函数体
}
```
# 参数

## 特殊参数

- 命名参数：可带有变量名，此时变量顺序可变

```c#
public void MoveAndResize(int x, int y, int w, int h) { ... }

MoveAndResize(x: 10, y: 20, w: 30, h: 40);
MoveAndResize(y: 20, x: 10, h: 40, w: 30);
```

- 可选参数：声明中带有默认值的参数，调用时可省略
	- 可选参数必须是可变参数前最后的参数，多个参数如果要修改应使用命名参数
- 可变参数：声明中带有 `params` 的数组参数，只能在末尾，且最多有一个

```c#
int Method(int arg1, bool arg2, string arg3 = "aaa", params int[] data)
```
## 引用传递

使用 `ref` 修饰形参，表示按引用传入参数
- 值类型：传入的是当前量的引用；因此，该值类型在函数内改变，函数外同样会变
- 引用类型：传入的是引用的引用；因此，该引用内容在函数内改变，函数外不变

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
Console.WriteLine(a1.X);    // 3: a 是对 a1 的引用，a1 就是 A 的对象
```
       
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
Console.WriteLine(a1.X);    // 2: a 是对 a1 的引用，a1 是对 A 的对象 的引用
```
## 输出参数

使用 `out` 修饰形参，表示该变量可以作为输出。但该变量传入时必须有值，即不可以是 null

```csharp
void outMethod(out int a, out object b) {
    // Console.WriteLine($"{a}, {b}"); --> out 必须在使用前对其复制
    a = 1;
    b = new object();
//  0, { name = b }
    Console.WriteLine($"{a}, {b}");
}
   
int a = 0;
object b = new { name = "b" };
//  1, System.Object
Console.WriteLine($"{a}, {b}");
outMethod(out a, out b);
//  1, System.Object
Console.WriteLine($"{a}, {b}");
```
# 运算符重载

使用 `operator` 重载，详见 [[CSharp 运算符]]

```csharp
Class AClass {}

public AClass operator +(AClass a, AClass b) => a;
```
# 扩展方法

在类不能被继承（如密封类）时给对象添加功能的方法，也可应用于对接口的扩展

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
# lambda 操作符

若方法实现只有一个语句，可简化为 lambda 操作符
   
```csharp
[修饰符] 返回值 函数名(形参列表) => Value;
```
# lambda 表达式

`(params 只有一个参数可省略括号) => {}`

```csharp
void Lambda()
{
    var mul = (param1, param2) => {
        return param1 * param2;
    };
}
```
