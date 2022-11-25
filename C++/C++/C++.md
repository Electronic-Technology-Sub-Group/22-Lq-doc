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
- [[预处理指令]]：以 `#` 开头的部分，这里通过#[[include]]指令引入了[[iostream]]头文件
- [[函数]]：命名的代码块，可通过名称调用，[[main 函数]]为所有程序的入口
- 基本输入输出：`C++` 主要通过 [[iostream]] 头文件的 `cin` 和 `cout` 实现控制台的输入输出
- [[变量]]：以[[标识符]]命名的内存区域，可存储特定类型数据，如 `apples, oranges, fruit` 
- [[命名空间]]：为解决命名冲突，`C++` 引入了命名空间，为成员增加一个所有域。`C++` 头文件的内容都归属于 `std` 命名空间

# 编译型语言？

> 值得注意，和流行的误解不同，ISO C 和 ISO C++ 都从未明确要求源程序被 _编译（compile）_，而仅要求 _翻译（translate）_，因此C和C++并不是所谓的 _编译型_ 语言。 摘自维基百科

# C 的超集？

一般来说，我们认为 `C++` 是 `C` 的超集，但也有例外：
- `C` 允许从 `void*` 类型[[隐式转换]]到其他[[指针]]类型，而 `C++` 不允许
- `C++` 定义了很多新关键字，如[[new]]，[[delete]]等，无法作为合法的[[标识符]]

