---
语言: cpp
语法类型: 高级语法
---
> [!note] 使用场景
> 解决静态成员变量赋值时重复引用问题

#cpp17 

C++17 之前，静态成员必须保证全局没有同名声明，产生重复定义。

```cpp title:x.h
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

```cpp title:x.cpp
#include "x.h"
```

```cpp title:main.cpp
#include <iostream>
#include <string>

#include "x.h"

using namespace std;

int main() {
    cout << X::text;
    return 0;
}
```

`x.cpp`，`main.cpp` 都引入了 `x.h`，造成了 `X::text` 的重复定义。

通过 `inline` 声明内联到类的静态变量，允许在声明时初始化值。

```cpp title:x.cpp
#ifndef CPP_X_H
#define CPP_X_H

#include <string>

class X {
public:
    static inline std::string text {"hello"};
};

#endif //CPP_X_H
```

`constexpr` 修饰的静态成员变量默认也是内联的。

```cpp
class X {
public:
    static constexpr int num{5};
};
```
