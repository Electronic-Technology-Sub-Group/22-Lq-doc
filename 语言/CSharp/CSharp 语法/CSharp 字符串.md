字符串类型为 `System.String`，`string` 类型是其别名。

字符串字面量使用 `""` 包围，并可以使用 `@` 或 `$` 前缀控制引用和转义的行为
- 无前缀：不会自动引用，会处理转义字符
- `@`：不会自动引用，不会处理转义字符
- `$`：字符串模板，会自动引用，会处理转义字符

```csharp
var a = 0;
string s1 = "a: {a}\\int";     // a: {a}\int
string s2 = @"a: {a}\\int";    // a: {a}\\int
string s3 = $"a: {a}\\int";    // a: 0\int
```
# 运算符

- 索引：字符串允许使用下标获取第 n 个字符

```csharp
char a = "hello"[0]; // a = 'h'
```

- 连接：字符串使用 `+` 连接，同时支持 `+=` 运算符，相当于 `Concat`

```csharp
string s1 = "hello";
string s2 = "world";

string str = s1 + "" + s2;
s1 += s2;
```
# 方法与属性

`string` 类型包括大量常用方法和属性
- `PadLeft()`，`PadRight()`：字符串左、右侧填充指定重复字符串
- `IndexOfAny()`，`LastIndexOfAny()`：查找指定字符串中第一次、最后一次出现某组字符的位置
- `Length`：字符串实际长度
	- `Capacity`：字符串在分配的内存中的最大长度
	- `MaxCapacity`：字符串允许的最大长度，默认为 `int.MaxValue`
# StringBuilder

`System.Text.StringBuilder` 类用于字符串编辑，避免不必要的复制操作
- 大大提高替换单个字符串的效率，但插入/删除字符串扔效率低下
- 默认内存分配大小为 150
- AppendFormat 可用于追加格式化字符串
# 字符串差值

- 声明时，使用 `$` 格式化；或使用 `String.Format` 方法格式化字符串
	- `{}` 作为占位符
	- 若要输入 `{}` 则使用 `{{}}` 转义

```csharp
string s1 = "World";
string s2 = $"Hello, {s1}";    // Hello World
// 相当于 ==>
string s2 = String.Format("Hello, {0}", s1);    // 字符串占位符从 0 开始
```

- FormattableString：可以获取字符串和属性
	- 需要 .Net 4.6 或 NuGet 安装 `StringInterprolationBridge` 依赖

```csharp
int x = 3, y = 4;
FormattableString f = $"The result of {x} + {y} is {x + y}";
WriteLine($"format = {f.Format}"); // The result of {0} + {1} is {2}
for (int i = 0; i < f.ArgumentCount; i++)
{
    WriteLine("Argument {0} = {1}", i, f.GetArgument(i)); // 0 = 3, 1 = 4, 2 = 7
}
```

- IFormattable：自定义特定格式

```csharp
CustomFormattable format = new CustomFormattable() {
    FirstName = "aaa",
    LastName = "bbb"
}
string s = $"{format:F}"    // aaa

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

`DateTime` 实现了该接口，可以该方法格式化时间和日期

```csharp
var day = new DateTime(2025, 2, 14);
// s.ToString(InvariantCulture)
FormattableString.Invariant($"{day:d}");

DateTime day = new DateTime(2025, 2, 14);
// 详见 MSDN DateTime.ToString
WriteLine($"{day:D}");    // Friday, February 14, 2025
WriteLine($"{day:d}");    // 2/14/2025
```
# 正则

正则表达式对象的类为 `System.Text.RegularExpressions`

```csharp
string str = "...(原始字符串)...";
string regex = "...(正则字符串)..."
MatchCollection collection = 
    Regex.Matches(str, regex[, RegexOptions options1[ | option2 | ...]]);
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
