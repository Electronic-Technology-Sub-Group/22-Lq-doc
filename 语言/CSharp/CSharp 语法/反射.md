反射：在运行过程中检查和处理元素的功能
- 枚举类型成员
- 实例化新对象
- 执行对象成员
- 查找类型信息
- 查找程序集信息
- 检查应用于某种类型的自定义特性
- 创作和编译新程序集

# 对象

反射所有功能主要围绕 `System.Type` 对象进行。该对象存储有关类型的引用，可通过以下方法获取
- `typeof()`
- `object.GetType()`
- `Type.GetType("FullName")`

`System.Type` 常用属性有：
- Name：类名
- FullName：完全限定名（包括命名空间）
- Namespace：命名空间
- BaseType：直接基类
- UnderlyingSystemType：.Net 运行库中映射的类型
- IsAbstract
- IsArray，IsClass，IsEnum，IsInterface，IsPointer，IsPrimitive（一种预定义的基元数据类型）
- IsPublic，IsSealed，IsValueType

`System.Type` 常用方法有：
- GetConstructor() & GetConstructors()    ConstructorInfo
- GetEvent() & GetEvents()                            EventInfo
- GetField() & GetFields()                               FieldInfo
- GetMember() & GetMembers()                 MemberInfo
- GetMethod() & GetMethods()                   MethodInfo
- GetProperty() & GetProperties()               PropertyInfo

# 程序集

`Assembly` 类可获取所在程序集引用，通过以下方法获取：
- `Assembly.GetAssembly(Type)`
- `Assembly.Load("程序集名")`
- `Assembly.LoadFrom("程序集完整路径名")`

`Assembly` 常用方法：
- GetTypes()                获取所有程序集中定义的类型
- GetCustomAttributes()       获取所有程序集中定义的特性
   - 若传入一个 Type 对象，则返回该类型的所有特性

# 特性

类似于注解，使用 `[]` 添加。

特性通过 `ICustomAttributeProvider` 对象的 `GetCustomAttributes()` / `GetCustomAttribute()` 方法获取。实现了 `ICustomAttributeProvider` 接口的方法包括 `Type`，`Assembly` 及其他反射获取的 `XxxInfo` 对象。
## 自定义

自定义特性继承于 Attribute 的类，并用 AttributeTarget 设定特性适用范围 
- All
- Class，Constructor，Delegate，Enum，Event，Field，GenericParameter，Interface，Method，Parameter，Property，ReturnValue，Struct
- Assembly，Module：不对应于任何程序元素，而是应该在特性前使用 assembly 或 module 前缀
```c#
 [assembly:SomeAssemblyAttribute(Parameters)]
 [module:SomeModuleAttribute(Parameters)]
```

以下适用于自定义特性的特性可选：
- AllowMultiple：是否可多次应用到同一元素上
- Inherited：特性在应用到类或接口时，是否由派生类和接口继承。当设定为 true 时
	- 应用到类或接口的特性也可以应用到所派生的类或接口上
	- 应用到属性或方法上，自动应用到该属性或方法的重写版本上
## 参数

- 必选参数：由类的构造函数参数定义
- 可选参数：公共属性或字段定义
