---
语言: cpp
语法类型: 基础语法
---
> [!note] 头文件
> 包含一个源文件具体内容的声明，通常包含函数声明、类型别名声明、结构体与类声明等。

编译器以 cpp 文件为基本编译单位，编译时无法访问到其他源文件。头文件负责告诉编译器这里应当存在某些信息（各种声明），这些信息通常位于其他源码中。

`#include` 预处理指令**将指定头文件全部内容插入到该源文件中，取代该指令所在位置**，因此代码可以访问到插入的头文件中的函数，类型，结构等元素。

* `#include <系统头文件名称>`：先从 C++ 编译器的头文件目录查找，再找源码目录。
* `#include "自定义头文件信息"`：头文件只会从源码目录查找，支持使用相对目录。

```cpp
#include <iostream> // C++ 头文件
#include <string.h> // C 头文件
#include "a.h" // 当前目录下的 a.h
#include "a/b.h" // 当前目录/a 子目录下的 b.h
#include "../a.h" // 当前目录上一级目录下的 a.h
```

`````col
````col-md
头文件 a.h:

```cpp
namespace Xxx {

    int get(int a) {
        return a;
    }

    int add(int a, int b) {
        return a + b;
    }
}
```

源文件：

```cpp
#include "a.h"

int main() {

}
```
````
````col-md
预编译后的 `main.i`

```cpp
# 1 "main.cpp"
# 1 "<built-in>"
# 1 "<command-line>"
# 1 "main.cpp"
# 1 "a.h" 1
namespace Xxx {

    int get(int a) {
        return a;
    }

    int add(int a, int b) {
        return a + b;
    }
}
# 2 "main.cpp" 2

int main() {

}
```
````
`````

原来 `#include "a.h"` 位置完全被 `a.h` 文件内容替换了

C++ 提供[[../STL/STL|STL]]库