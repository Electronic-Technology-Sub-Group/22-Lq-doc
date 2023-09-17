# 预编译

预处理指令在预编译后的效果可通过以下编译命令查看：

```bash
gcc -E [源代码文件] -o [结果输出文件]
gcc -E main.cpp -o main.i
```

# 预处理指令

预处理指令以 `#` 开头，不属于标准 `C++` 语句，末尾没有分号。

预处理指令会在编译期由编译器处理，根据预处理指令对源码做出相应的修改。

# include 与头文件

头文件：包含一个源文件具体内容的声明，通常包含函数声明、类型别名声明、结构体与类声明等。

由于编译器以一个源码文件为编译的基本单位，在编译期无法访问到其他源码文件，因此若源码中使用了其他源码文件的内容，就需要告诉编译器，这些内容在别的地方是存在的，这些信息通常位于其他源码的头文件中。

`#include` 预处理指令**将指定头文件全部内容插入到该源文件中，取代该指令所在位置**，因此代码可以访问到插入的头文件中的函数，类型，结构等元素。

语法：
- `#include <系统头文件称>`：使用 `<>` 包围的头文件优先从 C++ 头文件目录（主要由编译时的环境变量定义）查找，没有的话从源码目录查找。
- `#include "自定义头文件信息"`：使用 `""` 包围的头文件只会从源码目录查找，因此用于自定义头文件。

# 命名空间

由于 `#include` 会直接将所有头文件内容复制到源文件中，当多个头文件中存在相同名称的元素时，就会引发异常，尽管可以通过预编译阶段，但在编译阶段会产生异常

```c++
// a.h
int doSomething(int a, int b) {
    return a + b;
}
```

```c++
// b.h
int doSomething(int a, int b) {
    return a - b;
}
```

```c++
#include "a.h"
#include "b.h"

int main() {
}
```

预编译后生成 `main.i`，内容为

```c++
# 1 "main.cpp"
# 1 "<built-in>"
# 1 "<command-line>"
# 1 "main.cpp"
# 1 "a.h" 1
int doSomething(int a, int b) {
    return a + b;
}
# 2 "main.cpp" 2
# 1 "b.h" 1
int doSomething(int a, int b) {
    return a - b;
}
# 3 "main.cpp" 2

int main() {
}
```

可以看到，生成的 `main.i` 有重复的 `int doSomething(int, int)` 函数，预编译过了，但编译不会通过

C++ 通过命名空间将多个同名函数分割出来，使用 `namespace` 关键字声明命名空间。使用 `namespace` 创建命名空间，使用域解析运算符 `::` 访问

```c++
// a.h
namespace aaa {
   int get() {
       return 3;
   }
}
```

```c++
// b.h
namespace bbb {
    int get() {
        return 5;
    }
}
```

```c++
// main.cpp
#include "a.h"
#include "b.h"

int main() {
    int v1 = aaa::get(); // v1 = 3
    int v2 = bbb::get(); // v2 = 5
}
```

## namespace

`namespace` 关键字可以声明一个命名空间，用于解决全局成员的命名冲突问题。

```c++
namespace aaa {
    int a {10};
}
```

命名空间是逻辑上的一种分割，因此同一个命名空间可以分布在不同文件中，同一文件中可以包含多个命名空间，也可以多次声明同一个命名空间。即使成员分布在不同文件中，名称相同的命名空间仍是同一个命名空间。

```c++
// a.h
namespace aaa {
    int a {10};
}
```

```c++
// b.h
namespace bbb {
    int a {100};
}
```

```c++
// c.h
namespace aaa {
    // 冲突：与 a.h 的 a 重名
    int a {20};
}
```

```c++
// d.h
namespace aaa {
    int b {20};
}

namespace bbb {
    int b {30};
}

// 没问题
namespace aaa {
    int c {50};
}
```

命名空间也可以嵌套

```c++
// e.h
namespace aaa {
    namespace bbb {
        namespace ccc {
            // aaa::bbb::ccc::abc = 10
            int abc {10};
        }
    }

    namespace ddd {
        // aaa::ddd::def = 20
        int def {20};
    }
}
```

## using

可以使用 `using` 将其他命名空间的成员引入到当前作用域中，当然前提是我们要确保他们不会造成冲突。
- `using 命名空间::成员名;`：将指定命名空间的成员导入到当前作用域
- `using namespace 命名空间;`：将指定命名空间的所有成员导入到当前作用域

```c++
#include<iostream>
#include "a.h"

// 引入 aaa 作用域的所有成员
using namespace aaa;
// 引入 std 作用域的 cin 成员
using std::cout;

void main() {
    // std::cout, aaa::get() 都已被导入到当前作用域（的父作用域）中，因此可以直接访问
    // std::endl 还没有被导入，因此需要域解析
    cout << get() << std::endl;
    
    using std::endl;
    // 这里导入了 std::endl，因此可以直接使用了
    // 但注意的是 std::endl 导入在 main 函数的作用域中，using 之前和 main 之外仍未导入
    cout << endl;
}
```

## std

C++ 所有头文件成员都在 `std` 命名空间中

C++ 兼容 C，而 C 语言的所有头文件成员在全局作用域中，包括 `xxx.h` 版本和 `cxxx` 版本，因此尽量使用 C++ 提供的的头文件

# iostream

负责来自 `C++` 的基本控制台输入输出功能，其地位相当于 `C` 中的.`stdio.h` 头文件。
## cin

`cin`  相当于 `C` 中的 `scanf` 函数，用于从控制台输入数据。

`cin` 是一个 `istream` 对象，重写了 `>>` 运算符，可直接将数据赋值给一个变量的引用

```c++
#include<iostream>

using namespace std;

int main() {
    int num1, num2;
    cin >> num1 >> num2;
}
```
## cout

`cout` 相当于 `C` 中的 `printf` 函数，用于将数据输出到控制台

`cout` 是一个 `ostream` 对象，重写了 `<<` 运算符，可将一个数据直接输出到控制台中

```c++
#include<iostream>

using namespace std;

int main() {
    cout << "Hello " << "World!" << endl;
    const char* lang = "C++";
    cout << "I'm writing a note for " << lang << endl;
}
```

如果需要格式化输出，则可以使用 `iomanip` 头文件提供的控制函数和对象。

> [!important]
> 传统的 `scanf` 和 `printf` 仍然可以使用，且在数据量很大且没有其他设置的情况下，比 `cin`和 `cout` 快近一倍
## cerr

异常输出
## endl

`std::endl` 可直接输出到 `cout`，表示换行，相当于 `cout << '\n'`
