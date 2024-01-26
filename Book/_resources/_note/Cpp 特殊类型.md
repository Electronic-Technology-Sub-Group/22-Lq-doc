# 聚合类

Aggregate Class，其特征为：
- 没有自定义构造函数
- 没有基类和虚函数
- 没有类中初始化器（即各成员没有使用 `{}` 的初始化值，直接用 `=` 初始化值可以）
- 所有成员都是 `public` 的

特殊性：
- 可以使用初始化列表和[[现代cpp-普通类构造与初始化#指定初始化|指定初始化]]
- 可以直接访问成员
# POD 类型

Plain Old Data，C++ 内建类型或传统 C 结构体，可以通过 `std::is_pod<T>::value` 判断，特点是完全与 C 兼容
# 平凡类

平凡类可以通过 `std::is_trival<T>::value` 判断。trival 平凡与 non-trival 非平凡是针对一个类的四种构造函数来说的：
- 默认构造 ctor
- 复制构造 copy
- 赋值构造 assignment
- 默认析构 dtor

类型是平凡的特征为：
- 类中对应函数只有默认实现
- 类中所有非静态成员都是[[#POD 类型]]，且基本类型与其他POD类型不可同时存在

```c++
class A {
    int a;
}

class B {
    int b;
}

class C {
    A a;
    B b; // 可以
    int c; // 打破平凡类
}
```

- 没有虚函数、虚基类，且整个继承链中只有一个类可以有非静态基本类型

```c++
class A {
public:
    int a;
}

class B: public A {
    int b; // 打破平凡类
}
```

- 所有非静态成员都有相同访问级别（public，private）

```c++
class A {
public:
    int a;
private: // 打破平凡类
    int b;
}
```

若一个类针对某个函数是平凡的，则编译器可以对构造、复制、赋值、析构等操作时进行优化，直接对内存进行操作而不需要调用其函数以提高效率。
- 允许直接使用 `memcpy` 复制类