#cpp20 `consteval` 说明符用于声明立即函数，表示对应函数必须在编译期执行。

> [!note] `consteval` 与 `constexpr` 的区别
>1. `consteval` 只能修饰函数，`constexpr` 还可以修饰变量
>2. `constexpr` 函数既可以作为编译时函数又可以作为普通函数；`consteval` 函数必须在编译期执行，否则产生异常

```c++
constexpr int sqr_expr(int x) {
    return x * x;
}

consteval int sqr_eval(int x) {
    return x * x;
}

// 都没问题
int a1 = sqr_eval(100);
int a2 = sqr_expr(100);

int b = 100;
// error: the value of 'b' is not usable in a constant expression
int b1 = sqr_eval(b);
// 没问题
int b2 = sqr_expr(b);
```
