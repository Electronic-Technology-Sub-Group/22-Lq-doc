# 外部模板

C++11 允许编译器在编译期遇到完整定义的模板时，暂不在特定位置实例化模板，使用`extern`声明：

```c++
extern template class std::vector<MyClass>;
```

这表示，告诉编译器，**不要**在该文件中将该模板类实例化。

# 模板特化
#未完成 

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