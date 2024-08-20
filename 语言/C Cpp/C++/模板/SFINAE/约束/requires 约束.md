---
语言: cpp
语法类型: 高级语法
---
> [!note] 使用场景
> 简化的[[概念]]写法

简单的约束可以直接使用 `requires` 进行约束，出现在模板形参列表或函数声明的尾部。

```cpp
template<class T>
// 位于模板形参列表尾部
requires std::is_integral_v<T>
struct X {};


template<class T>
T add(T a, T b)
// 位于函数声明尾部
requires std::is_integral_v<T>
{
    return a + b;
}
```

使用概念和约束表达式可以使异常信息更加清晰：

```cpp
template<class T>
requires std::is_integral_v<T>
struct X {};

int main() {
    // error: template constraint failure for 'template<class T>  requires  is_integral_v<T> struct X'
    X<string> x1;
    return 0;
}
```

约束表达式应当是一个类型为 `bool` 的纯右值常量表达式，结果为 `true` 表示约束检查通过，替换结果合法。
* `&&` 运算称为约束的合取
* `||` 运算称为约束的析取

当约束表达式存在于 `requires` 子句中时，约束表达式有了进一步的要求：
* 若表达式不是一个初等表达式，应当使用括号包围
* 只能是几个初等表达式（或带括号的任意表达式）与 `&&`，`||` 的组合

当存在多种约束时，检查的优先级如下：
1. 模板形参列表中的形参约束
2. 模板形参列表之后的 `requires` 子句
3. 简写函数模板声明中修饰 `auto` 的概念
4. 函数模板声明尾部的 `requires` 子句