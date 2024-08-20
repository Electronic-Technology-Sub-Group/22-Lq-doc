---
语言: cpp
语法类型: 基础语法
---
模板类型引用通常需要使用 `typename` 标志说明该值为一个类型

```cpp
template<class T> void foo(typename T::type);
```

#cpp17 支持声明[[支持声明模板形参|带模板的类型]]

有两种情况可以例外：
* 指定基类：`class A<T>: T::B`
* 成员初始化：`class A: T::B { A(): T::B() {} };`

#cpp20 支持在[[省略|更多情况]]下省略 `typename` 关键字。
