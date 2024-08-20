---
语言: cpp
语法类型: 高级语法
---
> [!note] 使用场景
> 判断是否在编译期，选择 consteval 函数还是普通函数

#cpp23

判断当前是否是在编译期。

需求：存在 `constexpr` 函数 `a` 和 `consteval` 函数 `b`，在 a 中我们需要做的是根据当前环境，若是在编译期则执行 b，否则做其他事。在不使用 `if consteval` 的情况下：

> [!error] 错误代码片段 1

```cpp
// a: some_computation
consteval int compiler_computation(int i) {
    // 编译期计算
    return i;
}

// b: compiler_computation
constexpr int some_computation(int i) {
    if (std::is_constant_evaluated()) {
        // 编译期计算
        return compiler_computation(i);
    } else {
        // 非编译期
        return i;
    }
}
```

`std::is_constant_evaluated` 判断是否是编译期，但 `if` 本身就在运行时执行，编译器会认为当前分支仍可能在运行时调用，因此 `consteval` 会产生错误。

> [!error] 错误代码片段 2

```cpp
constexpr int some_computation(int i) {
    if constexpr (std::is_constant_evaluated()) {
        // 编译期计算
        return compiler_computation(i);
    } else {
        // 非编译期
        return i;
    }
}
```

`if constexpr` 的条件永远会在编译期计算，`is_constant_evaluated` 在编译时计算结果为 `true`，函数会退化成下面的样子，显然也是错误的：

```cpp
constexpr int some_computation(int i) {
    return compiler_computation(i);
}
```

> [!success] 正确代码片段

`if consteval` 用于区分编译期和运行时，注意 `{}` 不能省略。

```cpp
constexpr int some_computation(int i) {
    if consteval {
        // 编译期计算
        return compiler_computation(i);
    } else {
        // 非编译期
        return i;
    }
}
```
