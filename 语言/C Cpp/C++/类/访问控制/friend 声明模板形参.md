---
语言: cpp
语法类型: 高级语法
---
> [!note] 使用场景
> 通过[[../../模板/模板|模板]]声明友元

#cpp11

友元类可以忽略 `class`

> [!info] 一些编译器 C++ 11 之前的标准也可以使用

```cpp
class C;
class X {
    friend C;
}
```

类型 `C` 可以是一个普遍意义上的类型，如基本类型、类型模板参数、类型别名等

```cpp
class C;
typedef C Ct;

template<class T>
class X {
    friend void;
    friend int;
    friend C;
    friend Ct;
    friend T;
}
```

一个用法是通过模板声明友元，在不同情况下让不同的类作为友元类：

```cpp
class InnerVisitor { /* do something */ };

template<class T>
class SomeDatabase {
    friend T;
    // do something
}

// 内部诊断使用，允许使用 InnerVisitor 操作和查看数据库类私有成员
typedef SomeDatabase<InnerVisitor> DiagDatabase;
// 外部使用，没有友元类
typedef SomeDatabase<void>         SimpleDatabase;
```