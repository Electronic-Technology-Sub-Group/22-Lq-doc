---
语言: cpp
语法类型: 中级语法
---
```cpp
#if CONDITION
    // Code to include if CONDITION is true
#else
    // Code to include if CONDITION is false
#endif
```

如

```cpp
#define DEBUG 1

#if DEBUG
#include <stdio.h>
void debugLog(const char* message) {
    printf("DEBUG: %s\n", message);
}
#endif
```
# __has_include

#cpp17

条件编译时可以使用 `__has_include()` 特性判断当前文件是否可以包含某个头文件，括号内形式与 `#include` 相同

> [!danger] 该头文件检查的是头文件是否可以被包含，不检查是否已经被包含

```cpp
#if __has_include(<optional>)
#  include <optional>
#  define have_optional
#elif __has_include(<experimental/optional>)
#  include <experimental/optional>
#  define have_optional
#  define experimental_optional
#endif
```
