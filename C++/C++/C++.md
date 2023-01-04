# Hello World

先从一段示例程序开始

```c++
// Example
/*
  A Simple Example of a C++ Program
*/

#include<iostream>
using namespace std;

int main() {
    int apples, oranges;
    int fruit;
    apples = 5; oranges = 6;
    fruit = apples + oranges;
    cout << endl;
    cout << "Oranges are not the only fruit." << endl
         << " - and we have " << fruit << " fruit in all";
    cout << endl;
    return 0;
}
```

这段程序展示了一个最简单的顺序结构 `c++` 程序，类似于 `c`，从 `main` 函数开始执行。

![[hello world.png]]

- `C++` 每条语句结束后必须使用 `;` 结尾，但回车和空格随意。
- [[注释]]：以 `//` 开头或用 `/**/` 包围的部分
- [[预处理指令]]：以 `#` 开头的部分，这里通过 `#include` 指令引入了 `iostream` 头文件
	- 使用 `#include` 引入的文件，其内容将合并到当前程序中
	- 基本输入输出：`C++` 主要通过 `iostream` 头文件的 `cin` 和 `cout` 实现控制台的输入输出
- [[基本类型]]：每个变量都有一个唯一确定的类型，表明了其占用内存的大小和允许的行为
	- [[其他类型]]：一个类型不止可以包含一个变量，其他类型有更多玩法
- [[函数]]：以[[标识符]]命名的代码块，可通过名称调用，main 函数为所有程序的入口
- [[变量]]：以[[标识符]]命名的内存区域，可存储特定类型数据，如 `apples, oranges, fruit` 
- [[命名空间]]：为解决命名冲突，`C++` 引入了命名空间，为成员增加一个所有域。`C++` 头文件的内容都归属于 `std` 命名空间
- [[运算符]]：运算符用于直接对数据进行变换、比较等操作
- [[程序控制]]：通过控制语句，可执行条件、分支、循环结构，完成顺序结构难以完成的工作
- [[模板]]：模板是针对 C/C++ 代码的元编程，可以在编译时对程序进行操作
- [[异常处理]]：当程序运行出现问题时，主动或被动产生异常，异常处理可以处理异常或将异常传递出去
- [[模块]]：一种替代 `#include` 的东西，C++ 20 新增
- 其他一些不重要的东西
	- 代用记号：[代用运算符表示 - cppreference.com](https://zh.cppreference.com/w/cpp/language/operator_alternative)
	- [[汇编]]：C/C++ 允许在代码中直接使用
	- [[属性]]：使用 `[[]]` 标记的内容

# 编译型语言？

> 值得注意，和流行的误解不同，ISO C 和 ISO C++ 都从未明确要求源程序被 _编译（compile）_，而仅要求 _翻译（translate）_，因此C和C++并不是所谓的 _编译型_ 语言。 摘自维基百科

# C 的超集？

一般来说，我们认为 `C++` 是 `C` 的超集，但也有例外：
- `C` 允许从 `void*` 类型隐式转换到其他指针类型，而 `C++` 不允许
- `C++` 定义了很多新关键字，如 `new`，`delete` 等，无法作为合法的标识符

