---
语言: cpp
语法类型: 高级语法
---
> [!note] 使用场景
> 定义 `requires` 表达式，实现复杂的条件检验

`requires` 还可以用于定义 `requires` 表达式 - 一个纯右值表达式，对其进行模板实参替换，替换之后 `requires` 表达式出现无效类型或违反约束条件，则表达式结果为 `false`，测试不通过。

```cpp
// 检查 T().clear() 是一个合法表达式
// 即 T 有一个无参构造且具有 clear() 函数
template<class T>
concept check = requires {
    T().clear();
};

template<check T>
struct G {};

int main() {
    // 通过
    G<vector<char>> g1;
    // 通过
    G<string> g2;
    // error: template constraint failure for 'template<class T>  requires  check<T> struct G'
    G<array<char, 10>> g3;
    return 0;
}
```

`requires` 还支持形参表达式，但形参不具备生命周期和存储方式，只用于编译期检查，因此不支持不定参数或对变量的具体值进行测试：

```cpp
// 这个版本不检查构造函数
template<class T>
concept check = requires(T t) {
    t.clear();
};
```

# 要求序列

对模板实参的若干要求形成的序列

````tabs
tab: 简单要求

不以 `requires` 开始的要求，只断言表达式有效性，不进行求值，若替换失败则计算结果为 `false`
<br/>

```cpp
template<class T>
concept C = requires(T a, T b) {
    a + b;
};
```

tab: 类型要求
以 `typename` 开始的要求，用于检查嵌套类型、类模板、别名模板特化的有效性。若模板替换失败则结果为 `false`
<br/>

```cpp
template<class T, class T::type = 0> struct S;
template<class T> using Ref = T&;
template<class T> concept C  = requires {
    typename T::inner; // 检查嵌套类型
    typename S<T>;     // 检查类模板特化
    typename Ref<T>;   // 检查别名模板特化
};
```

tab: 复合要求
由 `{ 表达式 }`，`noexcept`，`-> 返回类型约束` 组成的多个约束
  * 替换模板实参，检查表达式有效性
  * 若存在 `noexcept`，检查表达式没有抛出异常的可能
  * 若存在 `->`，则确保 `decltype<表达式结果>` 满足返回类型约束
<br/>

```cpp
template<class T>
concept check = requires(T t1, T t2) {
    { t1.clear() } noexcept;
    { t1 + t2 } -> same_as<int>;
};
```

tab: 嵌套要求
以 `requires` 开始的要求，通常用于根据局部形参指定其他额外要求
<br/>

```cpp
template<class T>
concept check = requires(T t1, T t2) {
    requires same_as<decltype(t1 + t2), int>;
    // 等同于
    {t1 + t2} -> same_as<int>;
};
```
````
