# include

```c++
// a.h
namespace Xxx {

    int get(int a) {
        return a;
    }

    int add(int a, int b) {
        return a + b;
    }
}
```

```c++
#include "a.h"

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

可以看出，原来 `#include "a.h"` 位置完全被 `a.h` 文件内容替换了

## 路径

`C++` 中，若引入 `C/C++` 标准库的头文件，如 `iostream`, `iomanip` 等，使用 `<>` 引入

若引用当前项目或其他库中的头文件，则使用 `""` 引入，支持使用相对目录

```c++
#include <iostream> // C++ 头文件
#include <string.h> // C 头文件
#include "a.h" // 当前目录下的 a.h
#include "a/b.h" // 当前目录/a 子目录下的 b.h
#include "../a.h" // 当前目录上一级目录下的 a.h
```

# define

可以通过该预处理指令定义仿函数，即宏，使用`()`表示一个变量

```c++
#define ADD(x, y) ((x) + (y))

int main() {
    int result = ADD(5, 12);
}
```

其预编译结果为

```c++
# 1 "main.cpp"
# 1 "<built-in>"
# 1 "<command-line>"
# 1 "main.cpp"


int main() {
    int result = ((5) + (12));
}
```

## 类型别名

可通过该头文件定义类型别名：`#define 新名称 旧名称`

```c++
#define UINT unsigned int

int main() {
    UINT a = 30u;
}
```

其编译结果为

```c++
# 1 "main.cpp"
# 1 "<built-in>"
# 1 "<command-line>"
# 1 "main.cpp"


int main() {
    unsigned int a = 30u;
}
```
