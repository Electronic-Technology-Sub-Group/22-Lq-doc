---
语言: cpp
语法类型: 高级语法
---
> [!note] 使用场景
> 使用 `auto` 推断时，采用[[../decltype|decltype]]的类型推导规则

#cpp14

`decltype(auto)` 结合使用表示用 `decltype` 的推导规则推导 `auto`。用法与 `auto` 几乎完全相同只是推导规则不同。

> [!warning] 必须单独使用，不能结合指针、引用、`const`、`volatile` 使用

#cpp17 `decltype(auto)` 还可以用于非类型模板的类型。

```cpp
int i = 3;
int &j = i;
int &&foo();

// a1 a2 均为 int
auto a1 = i;
decltype(auto) a2 = i;
// b1 为 int，b2 为 int&
auto b1 = j;
decltype(auto) b2 = j;
// c1 为 int，c2 为 int&&
auto c1 = foo();
decltype(auto) c2 = foo();
// d1 d2 均为 int
auto d1 {3};
decltype(auto) d2 {3};
// e1 为 initializer_list<int>
// e2 无法编译，因为 {3, 5} 不是表达式
auto e1 = {3, 5};
decltype(auto) e2 = {3, 5};
// *f1 为 int*
// f2 无法编译，因为 decltype(auto) 只能独立使用
auto *f1 = &i;
decltype(auto) *f2 = &i;
```

可以代替函数返回类型后置

`````col
````col-md
```cpp
template<class A, class B>
decltype(auto) add2(A a, B b) {
    return a + b;
}
```
````
````col-md
```cpp
template<class A, class B>
auto add1(A a, B b) -> decltype(a + b) {
    return a + b;
}
```
````
`````
