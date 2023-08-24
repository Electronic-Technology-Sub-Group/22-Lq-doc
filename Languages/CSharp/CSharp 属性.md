字段与属性都是类中的数据成员，区别是字段可以被直接访问，而属性则需要通过 get 和 set 方法控制。
# 字段

直接声明即可，一旦实例化对象，就可使用 `Object.FieldName` 访问

```c#
private string _field = "default value";
```
# 属性

可手动实现 setter 和 getter，也可以自动生成
- get 访问器不带有任何参数, 且必须返回声明的类型对象
- set 访问器不须指定显式参数, 但编译器假定它带有一个相应类型的参数 value

```csharp
// 手动实现 需要自己声明关联的字段
private string _field = "default value";
public string Field
{
    get { return _field; }
    set { _field = value; }
}

// 自动实现 内置一个字段
public string Field { get; set; } = "default value";
```

只实现 get 或使用 readonly 修饰的属性称为只读属性。只读属性不是常量，只是初始化后无法修改
- 只能由构造函数赋值或直接声明
- 自动实现: 使用只有 get 的属性

```c#
public string Field;
```

只读属性可以是表达式属性。表达式属性是带有 get 的属性，但只需要写 lambda 操作符

```c#
public string FirstName { get; set; };
public string LastName { get; set; };

public string FullName => $"{FirstName} {LastName}";
```