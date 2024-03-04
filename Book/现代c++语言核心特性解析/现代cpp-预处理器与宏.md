# 预处理器
## `__has_include`

C++17 新增 `__has_include()` 判断特性，括号内形式与 `#include` 相同，可以判断当前文件是否可以包含某个头文件，**不关心该头文件是否已经被包含**

```c++
#if __has_include(<optional>)
#  include <optional>
#  define have_optional
#elif __has_include(<experimental/optional>)
#  include <experimental/optional>
#  define have_optional
#  define experimental_optional
#endif
```
# 宏
## 特性测试宏

C++20 提供了一组属性和功能特性测试宏。
- 属性测试宏：`__has_attribute(属性)`。
	- 当属性为支持的标准属性时，返回支持的年份+月份（6位数字）
	- 当属性为支持的厂商属性时，返回一个非零值
- 语言功能特性测试宏：很多。具体查表
- 标准库功能特性测试宏：很多，包含在 `<version>` 和对应功能所在头文件中。
## `__VA_ARGS__`

C++11 开始引入 `__VA_ARGS__` 宏，功能与 C 对应宏相同，用于展开宏可变参数。

```c++
#define LOG(msg, ...) (printf("[%s] %d " #msg, __FILE__, __LINE__, ##__VA_ARGS__))

int main() {
    // [C:/Dev/untitleds/cpp/main.cpp] 8 hello aaa 123
    LOG(hello %s %d, "aaa", 123);
    return 0;
}
```

以上例子中，引入 `...` 作为可变参数，相当于 `printf("[%s] %d " "msg", 文件名, 行号, ...)`，一般情况下可以正常输出。

考虑特殊情况，当可变参数 `...` 没有参数时，其结果为 `printf("...", ..., 行号, )`，末尾一个逗号多余，从语法上来说有问题（但 GCC 经测试可以正常使用）。

C++20 开始引入 `__VA_OPT__` 宏，`__VA_OPT__(分隔符) __VA_ARGS__` 表示仅在可变参数存在时，在前面补充一个分隔符，否则不添加。

```c++
#define LOG(msg, ...) (printf("[%s] %d " #msg, __FILE__, __LINE__ \
                                              __VA_OPT__(,) ##__VA_ARGS__))
```