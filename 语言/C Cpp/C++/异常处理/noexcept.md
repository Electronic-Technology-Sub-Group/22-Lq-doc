---
语言: cpp
语法类型: 高级语法
---
> [!note] 使用场景
> 声明某个函数一定不会产生异常

#cpp11 

传统的声明方法只能声明函数会抛出异常，但无法声明函数不会抛出异常。

`noexcept` 声明说明函数不可能发生任何异常。这类函数中抛出异常时，不会展开堆栈，而是直接交给 `std::terminate` 结束程序。
* 一定不会出现异常的函数，通常非常短，复杂度极低
* 目标是一定不会出现异常的函数，该类函数一旦出错，抛出异常不如直接终止程序

```cpp
struct X {
    int f() noexcept { ... }
}
```

`noexcept` 也可以通过括号接收一个布尔常量表达式，当结果为 `true` 时表示函数不可能发生异常，否则可能发生异常，常用于模板。

例：`move` 函数，当 T 为基础类型时不可能抛出异常；否则，可能抛出异常。

```cpp
template<typename T>
T move(const T &o) noexcept(std::is_fundamental<T>::value) { ... }
```

由于 `noexcept` 是在编译时而不是运行时触发，我们可以在括号里进行一个函数调用，内部 `noexcept` 在被调函数可能抛出异常时返回 true

```cpp
template<typename T>
T move(const T &o) noexcept(noexcept(T(o))) { ... }
```

上面函数中包含了一个 `noexcept(noexcept(T(o)))`，内层 `noexcept` 判断 `T(const T&)` 函数是否可能抛出异常，当 `T(const &T)` 带有 `noexcept` 时 `move` 函数也是 `noexcept` 的，否则就是可能抛出异常的。

**带有** **`noexcept`** **与不带有该标记的函数签名是[[noexcept 函数|两个不同的函数签名]]**，可以就此设计不同的实现。

```cpp
// 带有 noexcept 的 swap 实现，用于带有移动构造的对象
template<typename T>
void swap_impl(T& a, T& b, std::integral_constant<bool, true>) noexcept {
    T tmp(std::move(a));
    a = std::move(b);
    b = std::move(tmp);
}

// 不带 noexcept 的 swap 实现，用于不带移动构造的对象
template<typename T>
void swap_impl(T& a, T& b, std::integral_constant<bool, false>) {
    T tmp(a);
    a = b;
    b = tmp;
}

// 实现函数
template<typename T>
void swap(T& a, T& b)
noexcept(noexcept(swap_impl(a, b, std::integral_const<bool,
                            noexcept(T(std::move(a)))
                         && noexcept(a.operator=(std::move(b)))>))) {
    swap_impl(a, b, std::integral_const<bool,
                            noexcept(T(std::move(a)))
                         && noexcept(a.operator=(std::move(b)))>());
}
```

上面是一个判断移动构造优化的 `swap` 实现函数。通过检查对象带有移动构造和移动赋值运算符实现，确定使用哪个函数。
## 默认 noexcept 函数

以下函数默认是 noexcept 的：
* 当函数所有基类和成员的以下函数是 `noexcept` 的时，默认是 `noexcept` 的：
    * 默认构造函数，默认复制构造函数，默认移动构造函数
    * 默认赋值函数，默认移动赋值函数
* 默认析构函数和（当前类及基类）未被 `noexcept(false)` 显式声明的自定义析构函数
* 默认 `delete` 运算符