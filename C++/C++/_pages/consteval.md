修饰一个函数为*立即函数*，也可以修饰 `if`，当其立即计算时表示为一个编译时常量；而其他任何潜在求值结果都是一个常量表达式，称为 立即调用。

```c++
consteval int add(int a, int b) {
    return a + b;
}

consteval int add3(int a, int b, int c) {
    return add(a, b) + c;
}

constexpr int r = add(3, 5); // r = 8
constexpr int add4(int a, int b, int c, int d) {
    return add3(a, b, c) + d; // 错误：外围类型不是 consteval 且 add3(...) 不是常量
}
```

`consteval` 函数传入的参数和潜在求值结果（函数指针等）都应当是 `consteval` 的。

`consteval` 隐含了 `inline`。

`consteval` 不能用于修饰析构函数，分配函数等
