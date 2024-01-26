# override

- 重写：override - 子类实现基类虚函数
- 重载：overload - 同一作用域内，同名但形参列表不同的函数
- 隐藏：overwrite - 子类中若存在与基类名称相同的函数，基类中的函数将被隐藏
	- 若形参列表、返回值、相关修饰符等与基类相同，且基类为虚函数，则为重写
	- 若想保留基类相关函数，使用 `using` 声明

由于 C++ 对重写的判断非常严格，经常容易将重载写成隐藏，如下面三个函数的声明是不同的

```c++
void fun(int &a) const;
void fun(int &a);
void fun(int a) const;
```

C++11 新增 `override` 关键字，用于声明该函数为重写函数。若在基类中找不到对应函数，C++ 将提示错误

```C++
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

*`override` 关键字只起到编译时检查作用，不加也能实现重写*
# final

C++ 11 新增 `final` 关键字，声明在函数尾部，以禁止派生类重写基类虚函数

```c++
class A {
public:
    virtual int a() final;
    virtual int b();
};

class B: A {
public:
    // Declaration of 'a' overrides a 'final' function
    int a() override {};
    int b() override {};
};
```