用户定义的一组整型类

```c#
public enum 枚举名 : 枚举类型
{
    成员1;
    成员2;
    成员3 = 3;
    ...
}
```

枚举的声明与 C++ 的很像，可以自定义枚举值类型（可省略，默认 int），也可以改变枚举值，甚至可以使用前面的值生成后面的值。

```csharp
enum Days 
{
    Monday = 0x1;
    Tuesday = 0x2;
    Wednesday = 0x4;
    Thursday = 0x8;
    Friday = 0x10;
    Saturday = 0x20;
    Sunday = 0x40;
    
    // 结合单个位设置不同值
    Weekend = Saturday | Sunday;
    Workday = 0x1f;
    AllWeek = Workday | Weekend;
}
Days day = Days.Monday | Days.Tuesday
Console.WriteLine(day)    // Monday, Tuesday
```

枚举对应整型类型和枚举类型之间可以强制转换

```c#
enum MyEnum
{
    A;
    B;
}

(int) MyEnum.A    // 0
(MyEnum) 1        // B
```

枚举类型变量也可以与字符串互相转化：
- 返回字符串：`ToString()`
- 从字符串获取：`(EnumType) Enum.Parse(typeof(EnumType), "name", 是否忽略大小写)`
