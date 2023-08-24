C# 结构体属于值类型，以栈中或内联（另一个堆的一部分）的形式存储

结构体使用 `struct` 声明，不支持继承（实际上继承自 `System.ValueType`）
- `System.ValueType` 类型未添加任何其他方法，但提供了一些构造
- 可重写 `Close()` 或 `Dispose()` 方法等，可重写 Object 类方法
- 可创建构造函数

```csharp
public struct MyStruct {
    public var Field1 = "field1";
    public var Field2 = "field2";
}
```

结构体使用 `new` 创建，但也可以像其他值类型变量一样声明

```c#
var a1 = new MyStrict();
MyStruct a2;
```

结构体对象内存在栈中分配，创建和删除非常快，但作为参数传递时会复制一份，当结构体数据过多时用作函数传参会造成不必要的复制，可使用 `ref` 或 `out` 类型

结构体每个字段在内存中的布局可以改变
#未完成 