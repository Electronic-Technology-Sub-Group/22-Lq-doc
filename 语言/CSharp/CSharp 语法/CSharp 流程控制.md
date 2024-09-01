流程控制与 Java、C++ 等基本相同
# if-else

```csharp
if (condition1) { ... }
else if (condition2) { ... }
else { ... }
```
# switch

可有多个 `case` 块，当 `condition == X` 时调用
- 没有代码块时，允许不存在 `break;`
- 没有 break 大多数会报编译错误；要跳到别的块只能使用 `goto case X;`
都没匹配时匹配 `default`

```csharp
switch (condition)
{
    case X: 
        // do something 
        break; 
    default: 
        // do something
        break;
}
```
# for

先执行 initicalizer，然后 condition，每次循环体完成后执行 iterator，然后继续进行 condition

```csharp
for (initicalizer; condition; iterator)
{
    // do something 
}
```
# while

两种循环方式：do-while 与 while 循环，最大区别就是 do-while 无论如何都先执行一次循环体

```csharp
while (condition)
{
    ...;
}
```

```C#
do
{
    ...;
} while (condition);
```
# foreach

遍历 `IEnumerable<T>` 对象

```csharp
foreach (var item in items)
{
    ...;
}
```
# goto

限制：不能跳出类，不能跳出/入循环代码块，不能跳出 finally 块

```csharp
goto Label;
...;
Label: 
...;
```
# break

跳出 switch 和循环代码块
# continue

用于循环代码块。中断本次循环并进行下一个循坏
