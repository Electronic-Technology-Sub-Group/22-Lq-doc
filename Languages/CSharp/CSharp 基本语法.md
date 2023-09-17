- 导入：`using`，包括静态导入 `static using`，与 Java 的 `import` 几乎相同
- 命名空间：`namespace` 声明，类似 Java 的 `package` 但不与目录结构绑定
- 类：与 Java 相同，以 `class` 声明，且所有成员必须在类中
# 注释

```csharp
// 单行注释
/*
多行注释
*/
```
# 变量

变量使用 `变量类型` 或 `var` 声明，常量使用 `const` 声明
- 使用 `var` 自动推断变量名时，声明时必须赋初值
- 声明常量时，声明时必须赋初值
- 常量隐含 static，故不允许使用 static 修饰

```csharp
// 声明: 类型 变量名;   
int a, b;    // a, b 均为 int
int c = 5;   // 声明并赋值

// 自动推断: 必须给定初始化值
var d = 18;
d.GetType(); // System.Int32, 即 int 类型

// 常量使用 const 声明, 可省略 static
// 常量在编译时可取, 因此不能以变量初始化常量
const int e = 100;
```

一个不被 static 修饰的变量，其作用域在离其最近的一组大括号之内。
# 入口函数

C# 中基本的函数声明如下：

```csharp
[修饰符] 返回值 函数名(形参列表)
{
    // 函数体
}
```

以 Main 为函数名的静态函数为入口函数，一个程序中只能有一个入口函数

```csharp
using System;
namespace XXX
{
    class ClassName
    {
        static void Main(string[] args) { ... }
    }
}
```

- 返回值可以是无参或 int
- 参数为 `string[]` 或无参
- 入口函数不一定是 public 的，但需要 static
# 文档

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
# 约定
## 命名约定

- 类名、接口名等使用 Pascal 大小写形式
- 公有的成员属性、方法使用 Pascal 大小写形式
- 私有成员应当为以 `_` 开头的 camel 形式
- 函数参数名使用 camel 形式
- 命名空间建议使用 `<CompanyName>`，`<TechnologyName>`
## 字段与属性

- 字段应总是私有的
- getter
	- 不应有耗时操作
	- 不应有任何其他负面效应
	- 按相同顺序读取多个属性，取得的值应相同
- setter
	- 不要使用只写属性
	- 各个 set 方法互相独立，使用不同顺序设置不同值效果应相同
