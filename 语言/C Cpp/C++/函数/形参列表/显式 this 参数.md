---
语言: cpp
语法类型: 高级语法
---
> [!note] 使用场景
> 将[[../../类/类|类]]成员函数 `this` 指针作为普通参数使用

#cpp23 

类成员函数可通过 `this` 指针访问类，该指针常被认为是一个隐式传入的参数，函数后置的修饰符就是该参数的修饰符

```cpp
struct X {
    // fun1(const X *this)
    void fun1() const {}
    // fun2(volatile X&& *this)
    void fun2(int i) volatile &&{}
};
```

函数第一个参数可以使用 `this` 修饰，称为 `this` 参数
- 类似 `python` 的 `self`，不需要显式传入。
* 静态函数：使用 `this` 绑定的参数变量代替 `this` 指针
* 不允许 `static`，`virtual`，`cv` 限定符

```cpp
struct X {
    void fun1(this const X &self) {}
    void fun2(this volatile X &&self, int i) {}
};
```

适用于形参的一些技巧便可以直接应用到 `this` 上：
* 模板
* 可以不必要是一个指针或引用，也可以是对象（适用于 `string_view` 等）
* 递归 lambda：`lambda` 是带 `operator()` 的匿名类，可以让第一个参数为 `this auto self`

```cpp
auto fact = [](this auto self, int n) -> int {
    return n ? n * self(n - 1) : 1; 
}
```
