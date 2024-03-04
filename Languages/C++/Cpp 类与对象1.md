       类是一种实现 `OOP` 继承、封装、多态特性的结构，使用 `class` 关键字定义

# 声明

```c++
class Box {
public:
    double width;
    double height;
    double length { 5.0 };
};
```

声明的类可通过 `{}` 初始化对象

```c++
#include<iostream>

using namespace std;

class Box
{
public:
    double width;
    double height;
    // 默认值为 5.0
    double length { 5.0 };
    // 可使用 Box::int_type 访问
    typedef int int_type;

    void print() {
        cout << "Box: width=" << width << ", height=" << height << ", length=" << length << endl;
    }
};

int main() {
    Box b1 {};
    Box b2 {1, 2};
    Box b3 {1, 2, 3};

    // Box: width=0, height=0, length=5
    b1.print();
    // Box: width=1, height=2, length=5
    b2.print();
    // Box: width=1, height=2, length=3
    b3.print();
}
```

类中的函数可以在类中定义，也可以在类外定义，在类内定义的函数默认是 `inline` 的，该类函执行速度更快，但也会使类更臃肿。一般比较简单的函数写在类中。当然，也可以给函数手动添加 `inline` 关键字声明该函数是 `inline` 的。

一般来说，类的声明在 `.h` 文件中，称为类声明、头文件；而函数实现等在于 `.cpp` 文件中，称为源文件。

```c++
class TwoInt
{
public:
    int a;
    int b;

    inline int max() const;

    int sum() const
    {
        return a + b;
    }

    int all_sum(int c, int d) const;
};
```

```c++
#include "twoint.h"

int TwoInt::max() const {
    return this->a + this->b;
}

int TwoInt::all_sum(int c, int d) const {
    return sum() + c + d;
}
```

# 访问控制

## 私有成员

仅能被该类内部方法和友元函数访问的成员，类外无法直接访问，`class` 默认成员就是私有的，也可使用 `private` 声明

```c++
class A {
    int v1;
public:
    int v2;
private: 
    int v3;
}
```

v1 和 v3 都是私有成员，v2 是公有成员

## 保护成员

`protected` 类型的成员仅该类和类的子类可访问

## 友元

使用 `friend` 声明的一类成员，他们对该类的成员的访问权限与类内成员相同

```c++
class Box {
private:
    int width;
    int height;
    int length;
friend:
    int size(const Box &box);
}

// 该函数虽然不是 Box 类的成员，但作为友元成员，可以直接访问到其私有成员
int size(const Box &box) {
    return box.width * box.height * box.length;
}
```

友元成员包括**全局函数**，**类成员（包括构造）**，**友元类**等

# 继承与派生

## 继承

使用 `:` 表示继承关系

```c++
class A : [private/protected/public] [BaseClass] {
// ...
}
```

其中，`[BaseClass]` 即基类（或叫父类），`[private/protected/public]` 表示子类（派生类）可以访问基类的内容（权限）
-   `private`：子类无法继承基类的 `private` 成员，基类 `protected`，`public` 成员以 `private` 权限传递给子类，表示 `implemented-in-terms-of` 关系，只有实现部分被继承，接口部分被略去。
	-   编译器不会将一个子类转换成一个基类对象，即丧失了多态性
	-   `implemented-in-terms-of` 关系尽量使用复合实现，尽量不使用 `private` 继承
-   `protected`：基类 `public`，`protected` 成员以 `protected` 权限传递给子类
-   `public`：基类 `public`，`protected` 成员直接传递给子类，不发生权限变更，表示 `is-a` 关系

除开 `private` 继承方式外，派生类继承基类的**所有数据成员**和**部分函数成员**。
-   继承了所有数据成员不代表能直接访问到，`private` 权限子类仍无法直接访问到
-   不被继承的函数成员：构造函数，析构函数，`=` 运算符重载；但对于构造函数，派生类的构造必须调用基类构造，可通过 `:` 调用

```c++
class A {
public:
    A(int i) {};
    A() {};
}

class B: A {
public:
    B(int i, double j): A(i) {};
    B(double j) {};
}
```

## 虚函数

虚函数以 `virtual` 声明，表示选择函数在运行时动态链接而非在编译期由编译器决定。这样要经过一次查表，速度慢一点，但允许派生类实现自己的代码覆盖基类相同方法。注意有无`const`修饰的函数是不同函数。

```c++
#include<iostream>

using namespace std;

class A {
public:
    virtual void fun1() { cout << "A" << endl; }
    void fun2() { cout << "A" << endl; }
};

class B: public A {
public:
    virtual void fun1() override { cout << "B" << endl; }
    void fun2() { cout << "B" << endl; }
};

int main() {
    A a;
    a.fun1(); // A
    a.fun2(); // A

    B b;
    b.fun1(); // B
    b.fun2(); // B

    A *pb = &b;
    pb->fun1(); // B
    pb->fun2(); // A
}
```

当使用 `A*` 表示 `b` 对象时，`fun1` 由于是虚函数，会在运行时查找调用的函数，为 `B` 类重写的方法；而 `fun2` 非虚，故在编译期确定了 `A` 类的 `fun2` 函数。

子类重写基类虚函数时，可使用 `override` 关键字声明。这个关键字不是必须的，但可以验证基类是否真有该函数避免错误。

`final` 关键字修饰的虚函数表示该虚函数不允许被类的子类继续重写该函数。

## 纯虚函数

当一个虚函数没有任何实现时，使用 `=0` 占位，称该函数为纯虚函数。

```c++
class A {
public:
    virtual void a() = 0;
}
```

带有纯虚函数的类，或基类中带有纯虚函数而子类没有全部实现的类称为抽象类。抽象类无法直接实例化。

## final

`final` 关键字修饰类时，表示该类不允许被继承，无法派生出子类。此时该类必须不是抽象类。

`final` 关键字修饰虚函数时，表示该函数不允许子类重写。

# 特殊成员

## 静态成员

类静态成员属于类而非对象，可通过类名直接访问，使用作用域解析运算符 `::` 访问，使用 `static` 声明。

类静态成员可包括变量（及常量）、函数，函数不可使用 `this` 指针（因为没有绑定成员对象）。

```c++
class Box {
public:
    static int count {0};
    static void set_count(int c) {
        Box::count = c;
    }
}
```

## 常量成员

常量成员使用 `const` 修饰
-   修饰类对象，可禁止一切类成员的变更行为

```c++
class Box {
public: int value;
}

int main() {
    const Box b {1};
    // 错误，const 类无法修改其成员
    b.value = 10;
}
```

-   在尾部修饰类方法，表示该方法不会修改类成员
	-   `this` 指针类型也会变成 `const ClassName*`
-   `const` 修饰的类对象只能使用 `const` 修饰的方法
-   带有 `const` 的方法和不带 `const` 的方法不同

# 命名空间冲突

当作用域中存在与类名同名的变量时，使用 `class` 用于消去歧义

```C++
class Bar {};

int main() {
    Bar Bar;
    class Bar bar2; 
}
```
