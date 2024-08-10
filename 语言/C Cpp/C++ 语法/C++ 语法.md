C++ 是一个静态弱类型语言。

> [!note] 静态类型语言
> 数据类型在编译期间检查的语言，要声明所有变量的数据类型

> [!note] 弱类型语言
> 某一个变量被定义类型，该变量可以根据环境变化自动进行转换，不需要经过现行强制转换

> [!warning] 通常认为，C++ 是一个编译型语言，但 ISO C 和 ISO C++ 都从未明确要求源程序被编译（compile），仅要求翻译（translate），因此 C 和 C++ 并不严格是编译型语言。事实上也有 C++ 解释器如 cling

一般来说，我们认为 `C++` 是 `C` 的超集，但也有例外：
* `C` 允许从 `void*` 类型隐式转换到其他指针类型，而 `C++` 不允许
* `C++` 定义了很多新关键字，如 `new`，`delete` 等，无法作为合法的标识符

```cpp
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

C++ 程序从 `main` 函数开始执行，即实例代码中的 `int main() {}` 中大括号内的内容。

# 未定义行为

C++ 标准中，并未定义所有行为，那些符合语法但没有定义具体行为的写法被称为未定义行为。

# 参考

```cardlink
url: https://book.douban.com/subject/35602582/
title: "现代C++语言核心特性解析"
description: "这是一本 C++ 进阶图书，全书分为 42 章，深入探讨了从 C++11 到 C++20 引入的核心特性。本书不仅通过大量的实例代码讲解特性的概念和语法，还从编译器的角度分析特性的实现原理，让读者能够..."
host: book.douban.com
image: https://img3.doubanio.com/view/subject/l/public/s33999027.jpg
```

```cardlink
url: https://zh.cppreference.com/w/cpp
title: "C++ 参考手册 - cppreference.com"
host: zh.cppreference.com
```

```cardlink
url: https://learn.microsoft.com/zh-cn/cpp/standard-library/cpp-standard-library-reference?view=msvc-170
title: "C++ 标准库参考"
description: "详细了解：C++ 标准库参考 (STL)"
host: learn.microsoft.com
image: https://learn.microsoft.com/en-us/media/open-graph-image.png
```

