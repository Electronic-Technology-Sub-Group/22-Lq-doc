---
语言: cpp
语法类型: 高级语法
---
> [!note] 使用场景
> 条件选择，多用于配合[[../../模板/模板|模板]]实现[[../../预处理指令/if|条件编译]]

#cpp17

* `if constexpr ( [初始化语句] 条件 ) true分支语句`
* `if constexpr ( [初始化语句] 条件 ) true分支语句 else false分支语句`
* `if [!]constexpr true分支语句`
* `if [!]constexpr true分支语句 else false分支语句`

类似 `#if`，可用于编写紧凑的模板代码，根据编译时条件进行实例化。
* `if constexpr` 条件必须是编译期常量
* 编译器仅编译结果为 true 的代码

```cpp
void check() {
    if constexpr(sizeof(int) > sizeof(char)) {
        cout << "sizeof(int) > sizeof(char)";
    } else {
        cout << "sizeof(int) <= sizeof(char)";
    }
}
```

经过编译后，由于标准规定 `int` 的内存空间总大于 `char`，`else` 分支将被移除

```cpp
void check() {
    cout << "sizeof(int) > sizeof(char)";
}
```

该功能用于模板时将非常好用。假设一种相等函数模板的定义如下：

```cpp
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

```cpp
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

```cpp
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

```cpp
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