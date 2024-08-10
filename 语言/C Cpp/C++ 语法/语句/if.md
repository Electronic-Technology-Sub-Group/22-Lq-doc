`if-else` 语句属于选择语句
* `if [constexpr] ( [初始化语句] 条件 ) true分支语句`
* `if [constexpr] ( [初始化语句] 条件 ) true分支语句 else false分支语句`
* `if [!]constexpr true分支语句`
* `if [!]constexpr true分支语句 else false分支语句`

其中
* 初始化语句： #cpp17 允许在 `if` 条件语句之前增加一条初始化语句，可以是表达式语句、简单声明、结构化绑定或别名声明
* 条件：返回 bool 或可隐式转换成 `bool` 类型的表达式语句
# if constexpr
#cpp17

类似 `#if`，可用于编写紧凑的模板代码，根据编译时条件进行实例化。
* `if constexpr` 条件必须是编译期常量
* 编译器仅编译结果为 true 的代码

```c++
void check() {
    if constexpr(sizeof(int) > sizeof(char)) {
        cout << "sizeof(int) > sizeof(char)";
    } else {
        cout << "sizeof(int) <= sizeof(char)";
    }
}
```

经过编译后，由于标准规定 `int` 的内存空间总大于 `char`，`else` 分支将被移除

```c++
void check() {
    cout << "sizeof(int) > sizeof(char)";
}
```

该功能用于模板时将非常好用。假设一种相等函数模板的定义如下：

```c++
template<class T>
bool is_same_value(T a, T b) {
    return a == b;
}

template<>
bool is_same_value<double>(double a, double b) {
    if (std::abs(a - b) < 0.0001) {
        return true;
    } else {
        return false;
    }
}
```

可以通过 `if constexpr` 可以将模板特化简化

```c++
template<class T>
bool is_same_value(T a, T b) {
    if constexpr (std::is_same_v<T, double>) {
        if (std::abs(a - b) < 0.0001) {
            return true;
        } else {
            return false;
        }
    } else {
        return a == b;
    }
}
```

>[!warning] `if constexpr` 的 `else` 很多情况下不能省略，如果省略编译结果可能与预期不符，常见可能产生多个 `return` 点，其返回类型可能不同。

> [!danger] `if constexpr` 不支持短路规则，可能产生错误：

```c++
template<class T>
int any2i(T value) {
    // error: 'npos' is not a member of 'int'
    if constexpr (std::is_same_v<T, std::string> && T::npos == -1) {
        return atoi(value);
    } else {
        return (int) value;
    }
}
```

`if constexpr` 首先判断 `T` 类型是否为 `string`。当 `T != string` 时，`T::npos` 不一定存在，但由于没有短路，将产生编译时错误。此处应改为嵌套 `if`：

```c++
template<class T>
int any2i(T value) {
    if constexpr (std::is_same_v<T, std::string>) {
        if (T::npos == -1) {
            return atoi(value);
        }
    } else {
        return (int) value;
    }
}
```
# if consteval
#cpp23

判断当前是否是在编译期。

假设有一个 `constexpr` 函数 a 和 `consteval` 函数 b，在 a 中我们需要做的是根据当前环境，若是在编译期则执行 b，否则做其他事。在 C++23 之前，我们其实无法实现：

```c++
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

上面代码片段中 `std::is_constant_evaluated` 判断是否是编译期，但其检查结果无法传递给编译器，编译器会认为当前分支仍然有可能在运行时调用，因此 `consteval` 会产生错误。

```c++
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

```c++
constexpr int some_computation(int i) {
    return compiler_computation(i);
}
```

`if consteval` 用于区分编译期和运行时，注意 `{}` 不能省略。

```c++
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
