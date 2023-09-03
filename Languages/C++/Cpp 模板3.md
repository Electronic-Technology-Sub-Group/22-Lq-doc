# 外部模板

C++11 允许编译器在编译期遇到完整定义的模板时，暂不在特定位置实例化模板，使用`extern`声明：

```c++
extern template class std::vector<MyClass>;
```

这表示，告诉编译器，**不要**在该文件中将该模板类实例化。
# 模板特化

C++ 允许对特定的模板值设定特定的实现，称为模板特化。当函数模板特化时，又称重载函数模板。

模板特化时，将需要特化的模板从尖括号中移除，并在特定位置将对应值替代即可。

```c++
#include <cstring>

template<typename T>
T add(T a, T b) {
    return a + b;
}

// 将 T 为 const char* 的类型进行特化
template<>
const char *add(const char *a, const char *b) {
    size_t len = strlen(a) + strlen(b);
    char *new_str = new char[len];
    memcpy(new_str, a, strlen(a) * sizeof(char));
    strcat(new_str, b);
    return new_str;
}

int main() {
    // 8
    cout << add(3, 5) << endl;
    // Hello World
    cout << add("Hello ", "World") << endl;
    return 0;
}
```

模板特化可以只特化部分模板参数。对于方法的部分特化又可以看作函数重载的一个特例，不被认为是模板特化

```c++
template<typename T, int time>
T addAndMul(T a, T b) {
    return (a + b) * time;
}

template<int time>
const char *addAndMul(const char *a, const char *b) {
    char *tmp_str = new char[strlen(a) + strlen(b)];
    memcpy(tmp_str, a, strlen(a) * sizeof(char));
    strcat(tmp_str, b);

    char *new_str = new char[strlen(tmp_str) * time];
    new_str[0] = '\0';
    for (int i = 0; i < time; ++i) {
        strcat(new_str, tmp_str);
    }

    return new_str;
}
```

对于类的部分特化又称为局部特化
# 断言

用于在编译期对数据进行测试，若不通过则产生编译期异常，使用 `static_assert` 关键字触发

```c++
static_assert([constant_expression], [string_literal]);
```
- `[constant_expression]`：一个返回 `bool` 类型的常量表达式
- `[string_literal]`：若为 `false` 则显示的异常提示

该断言用于编译时，不会对运行时产生影响

## 模板类型检查

断言可用于对模板的类型进行检查，相关头文件位于`type_traits`中

```c++
template<class T>
T average(const vector<T>& data) {
    static_assert(is_arithnetic<T>::value, "Type T must is arithmetic");
    // do something
}
```

- `is_arithnetic<T>`：类型 T 实现了 `+` 等算术运算符
- 类型判断：`is_integral<T>`，`is_signed<T>`，`is_unsigned<T>`，`is_floating_point<T>`，`is_enum<T>` 等