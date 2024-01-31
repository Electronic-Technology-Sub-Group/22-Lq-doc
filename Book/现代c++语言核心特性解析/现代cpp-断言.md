# 运行时断言

C、C++ 本身支持运行时断言，在不存在 `#define NDEBUG` 定义时，程序运行时会检查表达式是否为真，否则产生错误。
# 静态断言的间接实现

在 C++11 之前，如果我们需要在编译时对某些内容进行检查，如模板实例化时，运行时断言无法做到。此时我们可以通过宏定义或模板等方法间接实现：

- 宏定义：利用数组下标不可为负

```c++
#define STATIC_ASSERT_CONCAT_IMP(x, y) x ## y
#define STATIC_ASSERT_CONCAT(x, y) STATIC_ASSERT_CONCAT_IMP(x, y)
#define STATIC_ASSERT(expr) \
do {                        \
    char STATIC_ASSERT_CONCAT(static_assert_var, __COUNTER__) \
    [(expr) != 0 ? 1 : -1]; \
} while(0)                  \

int main() {
    // ok
    STATIC_ASSERT(1);
    // size '-1' of array 'static_assert_var1' is negative
    STATIC_ASSERT(0);
    return 0;
}
```

- 使用模板特化：通过实例化一个不存在的模板引发错误，但无法出现在类和结构体的定义中

```c++
template<bool>
struct static_assert_st;
template<>
struct static_assert_st<true> {};

#define STATIC_ASSERT(expr) static_assert_st<(expr) != 0>()

int main() {
    // ok
    STATIC_ASSERT(1);
    // declaration of 'struct static_assert_st<false>'
    STATIC_ASSERT(0);
    return 0;
}
```

- 另一种模板特化的实现：声明一个变量，可以出现在定义中，但会改变其内存布局

```c++
template<bool>
struct static_assert_st;
template<>
struct static_assert_st<true> {
};

#define STATIC_ASSERT_CONCAT_IMP(x, y) x ## y
#define STATIC_ASSERT_CONCAT(x, y) STATIC_ASSERT_CONCAT_IMP(x, y)

#define STATIC_ASSERT(expr) \
static_assert_st<(expr) != 0> \
STATIC_ASSERT_CONCAT(static_assert_var, __COUNTER__)

int main() {
    // ok
    STATIC_ASSERT(1);
    // Implicit instantiation of undefined template 'static_assert_st<false>'
    STATIC_ASSERT(0);
    return 0;
}
```
# 静态断言

自己实现的静态断言总有一些问题，C++11 引入静态断言的关键字和新特性

```c++
static_assert(expr, msg);
```

- expr 必须是一个常量表达式
- msg 为一个字符串。当 expr 为 false 时，引发编译时错误并输出该信息

C++20 引入单参数的静态断言，即 `msg` 参数可省略。此时，输出的信息即 `expr`，类似下面的定义：

```c++
#define sinple_static_assert(expr) static_assert(expr, #expr)
```