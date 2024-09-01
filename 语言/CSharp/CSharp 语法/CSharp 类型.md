# 值类型

值类型储存于堆栈 stack 中，直接存储值，包含基本类型和结构体两种。

栈的内存分配和使用速度非常快，但容量较小
## 基本类型

由于 C# 会编译成 IL，每一种值类型都会有一个 .Net 类型进行映射：

|    C# 类型     |    .Net 类型     | 字面量                        |
|:--------------:|:----------------:| ----------------------------- |
| sbyte (有符号) |   System.SByte   |                               |
|     short      |   System.Int16   |                               |
|      int       |   System.Int32   | 10, 0xFF                      |
|      long      |   System.Int64   | 10L                           |
| byte (无符号)  |   System.Byte    |                               |
|     ushort     |  System.UInt16   |                               |
|      uint      |  System.UInt32   | 10U                           |
|     ulong      |  System.UInt64   | 10UL                          |
|     float      |  System.Single   | 10.0F                         |
|     double     |  System.Double   | 10.0                          |
|    decimal     |  System.Decimal  | 10M                           |
|      bool      |  System.Boolean  |                               |
|      char      |   System.Char    | `'A'`, `'\x0041'`, `'\u0041'` |
## 结构体

结构体使用 `struct` 声明，详见 [[CSharp 结构体]]

```csharp
public struct MyStruct {
    public var Field1 = "field1";
    public var Field2 = "field2";
}
var a1 = new MyStrict();    // 可以像初始化类一样被初始化
MyStruct a2;                // 可以像其他值类型的方式初始化
```
## 可空类型

正常情况下，只有引用类型可为 `null`，而值类型不能为 `null`。而可空类型允许值类型使用 `null`

每一个值类型都有对应的可空类型，只需要在对应类型名后加 `?` 即可，如 `char` 对应的可空类型为 `char?`，结构体也有对应的可空类型。

可空类型可使用值类型的所有操作符，值类型可隐式转换为可空类型，但可空类型不能隐式转换为对应的值类型

可空类有两个特殊操作：
- `HasValue` 属性：`bool` 类型，当该类型为 `null` 时该值为 `false`
- `??` 运算符：二元运算符，第二个值为一个对应非空值，当该变量为 `null` 时返回第二个值
   
   ```csharp
   int? a = 0;
   int? b = null;
   
   a.HasValue;            // true
   b.HasValue;            // false
   int c = a ?? -1;    // 0
   int d = b ?? -1;    // -1
   ```
# 引用类型

引用类型的值储存于托管堆（managed heap）中，存储对值的引用

| C# 类型  | .Net 类型       |
|:------:|:-------------:|
| object | System.Object |
| string | System.String |

`Object` 为所有类的基类，定义了一组方法
- ToString：获取对象字符串的一种快捷方式，要实现更多选择，需实现 `IFormattable` 接口
- GetHashCode：若要将该类作为字典的一个键，则需要重写该方法
- Equals：按值比对对象，不能抛异常。接收两个参数，可处理 null

还有一些无法重写的方法
- ReferenceEquals：比较两个对象是否引用同一实例
- GetType：返回 `System.Type` 派生的类，包含该对象所属类的信息，包括基本类型，方法，属性等，也是 .Net 反射技术的入口
- MemberwiseClone：浅表复制（只复制值类型和引用类型的引用），用于复制对象，返回对副本的一个引用

类使用 `class` 声明，详见 [[CSharp 类与继承]]
# 判断与强转

- `is`：判断对象是否为某一类或接口的实例
- `(T) obj`：类型强转，失败则抛出异常
- `as`：类似于 cast 运算符，用于安全强转：当对象并非转换的类的实现时返回 `null`

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
# sizeof

接收某一类型，返回该类型所需的字节数

```csharp
int s = sizeof(double) // s = 8
```
# stackalloc

申请一块内存。该内存不会被 GC 销毁，因此不必使用 `fixed` 固定

```csharp
decimal* pDecimals = stackalloc decimal[10];
```

`stackalloc` 申请的内存可以赋值给指针，也可以赋值给 `System.Span<T>` 或 `System.ReadOnlySpan<T>` 变量。赋值给指针时需要 `unsafe` 上下文，详见[[CSharp 指针]]
