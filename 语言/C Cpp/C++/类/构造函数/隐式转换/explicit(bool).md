---
语言: cpp
语法类型: 高级语法
---
> [!note] 使用场景
> [[../../../模板/SFINAE/SFINAE|SFINAE]]或[[../../../模板/SFINAE/约束/约束|约束]]

#cpp20 

explicit 允许接受一个 `bool` 常量表达式，指定 `explicit` 修饰的功能是否生效。

预期实现下面的效果：

```cpp
pair<string, string> safe(const char* first, const char* second) {
    return {first, second};
}

pair<vector<int>, vector<int>> unsafe(int first, int second) {
    return {first, second};
}
```

直接使用 `pair` ，`unsafe` 编译失败 - `const char*` 可以转化为 `string`，但 `int` 不能转换为 `vector<int>`

首先考虑 `vector(T...)` 构造函数是否被 `explicit` 修饰，但实际上于此无关，因为 `pair` 构造中为 `first`，`second` 赋值是通过间接构造完成，在构造时是显式调用的构造函数，不受 `explicit` 影响。

```cpp
template<class F, class S>
class MyPair {
    F _f;
    S _s;
public:
    template<class _F, class _S>
    // 显式调用构造函数
    MyPair(_F &f, _S &s): _f(f), _s(s) {
    }
};

MyPair<vector<int>, vector<int>> unsafe(int first, int second) {
    return {first, second};
}
```

我们可以给 `MyPair` 的构造函数添加 `explicit` 修饰以禁用该构造函数：

```cpp
template<class F, class S>
class MyPair {
    F _f;
    S _s;
public:
    template<class _F, class _S>
    // 显式调用构造函数
    explicit MyPair(_F &f, _S &s): _f(f), _s(s) {
    }
};
```

但这样 `safe` 函数也不能正常完成编译了。考虑使用 `SFINAE` 条件编译

```cpp
template<class F, class S>
class MyPair {
    F _f;
    S _s;
public:
    template<class _F = F, class _S = S>
    // 约束：
    //    1. F 可通过 _F 构造，S 可通过 _S 构造
    //    2. _F 可转换为 F，_S 可转换为 S
    requires is_constructible_v<F, _F> && is_constructible_v<S, _S> 
          && is_convertible_v<_F, F>   && is_convertible_v<_S, S>
    MyPair(_F &f, _S &s): _f(f), _s(s) {
    }
  
    template<class _F = F, class _S = S>
    requires is_constructible_v<F, _F> && is_constructible_v<S, _S>
    MyPair(_F&&, _S&&);
};
```

此时，可通过 `explicit` 进行开关控制：

```cpp
template<class F, class S>
class MyPair {
    F _f;
    S _s;
public:
    template<class _F = F, class _S = S>
    requires  is_constructible_v<F, _F> &&  is_constructible_v<S, _S>
    explicit(!is_convertible_v<_F, F>   || !is_convertible_v<_S, S>)
    MyPair(_F &f, _S &s): _f(f), _s(s) {
    }
};
```