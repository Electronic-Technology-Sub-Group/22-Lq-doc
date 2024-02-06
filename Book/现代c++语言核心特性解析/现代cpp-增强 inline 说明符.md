C++17 之前，定义类的静态成员容易产生重复定义 -- 必须保证全局没有同名声明。

考虑下面例子：

```c++
// x.h
#ifndef CPP_X_H
#define CPP_X_H

#include <string>

class X {
public:
    static std::string text;
};

std::string X::text{"hello"};

#endif //CPP_X_H
```

```c++
// x.cpp
#include "x.h"
```

```c++
// main.cpp
#include <iostream>
#include <string>

#include "x.h"

using namespace std;

int main() {
    cout << X::text;
    return 0;
}
```

在上面的三个例子中，`x.cpp`，`main.cpp` 都引入了 `x.h`，造成了 `X::text` 的重复定义。

C++17 允许通过 `inline` 声明内联到类的静态变量。此时，允许在声明时初始化值。

```c++
// x.cpp

#ifndef CPP_X_H
#define CPP_X_H

#include <string>

class X {
public:
    static inline std::string text {"hello"};
};

#endif //CPP_X_H
```