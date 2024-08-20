---
语言: cpp
语法类型: 中级语法
---
#cpp11

> [!note] 重写：override - 子类实现基类虚函数

> [!note] 重载：overload - 同一作用域内，同名但形参列表不同的函数

> [!note] 隐藏：overwrite - 子类中若存在与基类名称相同的函数，基类中的函数将被隐藏

由于 C++ 对重写的判断非常严格，经常容易将重载写成隐藏，如下面三个函数的声明是不同的

```cpp
void fun(int &a) const;
void fun(int &a);
void fun(int a) const;
```

`override` 关键字用于声明该函数为重写函数。若在基类中找不到对应函数，C++ 将提示错误

```cpp
class A {
public:
    virtual int a() const;
    virtual int b() const; 
};

class B: A {
public:
    // Non-virtual member function marked 'override' hides virtual member function
    int a() override {};
    int b() const override {};
};
```

*`override`* *关键字只起到编译时检查作用，不加也能实现重写*

> 重写与隐藏的区别主要在于通过指针调用类成员函数：
> 当将一个子类对象赋值给基类指针时，通过该指针调用重写的成员函数，实际调用子类的函数；调用隐藏的成员函数，实际调用基类的函数。

