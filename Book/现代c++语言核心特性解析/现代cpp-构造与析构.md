# 非静态成员默认初始化

c++11 允许非静态成员在声明时直接初始化，可以使用 `=` 或 `{}`。

**注意：初始化列表对数据成员的初始化总是优先于声明时默认初始化**

```c++
class X {
public:
    X() {}
    X(int a) : a_(a) {}
    X(double b) : b_(b) {}
private:
    int a_ = 0;
    double b_ {1.0};
}
```

在 c++20 后，非静态成员允许按位域初始化：

> [!note] 位域
> 在某些情况下，我们并不需要一个完整的字节，而是需要一个或几个二进制位。c 提供了一种数据结构 -- 位域（又叫位段），允许将一个字节中的二进制位划分为几个不同的区域，每个区域有一个名称，如：
>  ```
>  struct Date {
>      unsigned short nWeekDay  : 3;    // 0..7   (3 bits)
>      unsigned short nMonthDay : 6;    // 0..31  (6 bits)
>      unsigned short nMonth    : 5;    // 0..12  (5 bits)
>      unsigned short nYear     : 8;    // 0..100 (8 bits)
>  } ;
>  ```
>  ![[Pasted image 20240110081353.png]]

```c++
struct S {
    // 低 8 位初始值为 11
    int y: 8 = 11;
    // 之后 4 位初始值为 7
    int z: 4 { 7 };
}
```
# 初始化列表

c++11 开始引入新初始化方式 - `{}` 初始化。

```c++
struct ST {
   ST(int a) { ... }
}

// 直接构造
T a {1}
// 拷贝构造
T b = {1}
```

该方法统一了普通对象/结构体、数组、STL 容器等的初始化方式

```c++
int x[] { 1, 2, 3 };
std::vector<int> y { 1, 2, 3 };
std::set<int> z { 1, 2, 3 };
std::map<int, char> w { {1, 'a'}, {2, 'b'}, {3, 'c'} };
```

列表构造函数：接收一个 `std::initializer_list<T>` 参数的构造函数，则该对象允许使用 `{}` 直接初始化（隐式转换）

```c++
// vector 的构造函数
vector(initializer_list<value_type> __l,
	   const allocator_type& __a = allocator_type()): _Base(__a) { ... }
```

使用 `{}` 初始化既可以匹配一般构造函数，也可以匹配列表构造函数。当可以同时匹配普通构造函数和列表构造函数时，c++ 优先使用列表构造初始化。
# 指定初始化

C++20 开始，允许指定初始化成员类型以提高可读性

```c++
struct Point {
    int x, y;
};

// 实例化变量 p
Point p {
    .x = 10,
    .y = 20
};
```

但有以下限制（斜体表示 c++ 不可用但 c 可用）：
- 类型必须是一个[[Cpp 特殊类型#聚合类]]
- 不能设置静态成员值

```c++
struct Point {
    int x, y;
    static int w;
};

Point p {
    .x = 10,
    .y = 20,
    // error: 'Point' has no non-static data member named 'w'
    .w = 5;
};
```

- 每个成员只能初始化一次

```c++
Point p {
    .x = 10,
    // error: '.x' designator used multiple times in the same initializer list
    .x = 20
};
```

- *必须按声明顺序设置（比如例子中不能先初始化 y 后初始化 x）*

```c++
Point p {
    .y = 10,
    // error: designator order for field 'Point::x' does not match declaration order in 'Point'
    .x = 20
};
```

- 若类型为一个 `union`，则只能初始化一个值

```c++
union XY { 
    int x;
    float y;
};

XY {
    .x = 3,
    // error: too many initializers for 'main()::XY'
    .y = 1.0f
};
```

- *不能使用 `.` 嵌套初始化，但可以使用 `{}`*

```c++
struct Point {
    int x, y;
};

struct Rect {
    Point p0, p1;
};

Rect {
    // error: expected primary-expression before '{' token
    .p0.x = 1,
    .p0.y = 10,
    .p1.x = 10,
    .p1.y = 20
};

// 正确
Rect{
    .p0 {.x = 1, .y = 10},
    .p1 {.x = 10, .y = 20}
};
```

- *不能混用指定初始化和其他初始化*

```c++
Point p{
    .x = 3,
    // error: either all initializer clauses should be designated or none of them should be
    5
};
```
# 默认函数

到 C++ 为止，一个类中有以下函数会在特定情况下自动创建：
- 构造函数：在没有任何构造函数时创建
	- 默认构造、析构函数
	- 复制构造
	- 移动构造（c++11）
- 运算符
	- 复制赋值运算符
	- 移动赋值运算符（c++11）

虽然手动实现某些构造能在一定程度上替代默认构造函数，但有以下问题：
- 破坏[[Cpp 特殊类型#平凡类]]
- 无法在链接阶段前使用 - 如友元类

C++11 允许使用 `=default` 表示使用编译器默认生成的对应函数

```c++
class A {
public:
    int a;

    A(int _a): a(_a) {}
    A() = default;
};

class B {
public:
    B();
};
B::B() = default;
```
# 删除函数

C++ 11 允许使用 `=delete` 明确指定删除某些函数，包括
- 编译器默认产生的函数
- 普通成员函数
- 运算符
	- 删除 `new` 运算符后，可以禁止在堆中创建该对象，此时该类仅能创建自动变量、静态变量和全局变量
	- 删除 `delete` 运算符后，可以禁止通过自动变量、静态变量和全局变量创建对象，但可以通过 `new` 创建（但不能通过 `delete` 释放）
# 委托构造

c++ 11 允许使用委托构造，将一个构造函数委托给其他构造函数执行。前者称为委托构造，后者称为代理构造。

```c++
class A {

    A(int a): A(a, 3) { ... }
    // 
    A(int a, int b) { ... }
}
```

委托构造不能有任何初始化列表。

任何函数都可以是委托构造和代理构造，也可以同时是委托构造和另一个构造的代理构造。因此**一定要避免循环构造**。

委托构造函数的执行顺序如下：
- 代理构造的成员初始化列表
- 代理构造的函数体
- 委托构造的函数体
## 委托模板构造

构造函数允许使用模板，因此可以将其他构造函数委托到一个模板函数进一步减少重复代码

```c++
class A {
private:
    template<class T>
    A(T a, T b) {
        // do something
    }
public:
    A(vector<int>& list): A(list.begin(), list.end()) {}
    A(deque<int>& list): A(list.begin(), list.end()) {}
};
```
## 异常捕获

在委托时捕获异常，当代理构造产生异常，程序跳转到 `catch` 块执行

```c++
class A {
public:
    
    A() try: A(1.0) {
        // 正常构造 A
    } catch (int code) {
        // 异常 A
    }

    A(int a) try: A(a, 0.0) {
        // 正常构造 B
    } catch (int code) {
        // 异常 B
    }

    A(double b) try: A(1, b) {
        // 正常构造 C
    } catch (int code) {
        // 异常 C
        throw -2;
    }

    A(int a, double b) {
        // 委托构造
        throw -1;
    }
};
```

调用 `A(int)` 时，执行顺序为：委托构造 - 异常 B
调用 `A()` 时，执行顺序为：委托构造 - 异常 C - 异常 A
# 继承构造

C++11 开始，`using` 允许将基类的构造函数引入子类，此时子类会存在对应构造并只会将数据传递给基类对应构造。

```c++
class A {
public:
    A() {}
    A(int a, double b) {}
    A(char a, float b) {}
};

class B: A {
    // 导入所有 A 的构造函数
    using A::A;
};
```

继承构造需要注意一下问题：
- 导入方式是隐式导入，即只有在代码中实际用到某个构造，编译时才会导入该构造
- 不会导入默认构造和复制构造
- 不影响自动生成派生类默认构造 -- 即使用了 `using` 但没有其他构造，也会产生默认构造
- 若派生类中有与基类接受形参相同的构造，不会生成继承构造

```c++
class A {
public:
    A(int a) {
        cout << "Constructor from A" << endl;
    }
    
    A() = default;
};

class B : public A {
public:
    using A::A;
    B(int b): A() {
        cout << "Constructor from B" << endl;
    }
};

int main() {
    // Constructor from B
    B b {10};
    return 0;
}
```

- 多继承中，若导入的构造包含相同的形参列表，编译失败

```c++
class A1 {
public:
    A1(int a) {}
};

class A2 {
public:
    A2(int a) {}
};

class B : public A1, public A2 {
    using A1::A1;
    using A2::A2;
};

int main() {
    // error: call of overloaded 'B(<brace-enclosed initializer list>)' is ambiguous
    // note: candidate: 'A2::A2(int)'
    // note: candidate: 'A1::A1(int)'
    B b = B{10};
    return 0;
}
```

- 基类构造不能为 private
# 伪析构函数结束对象生命周期

C++20 之前，伪析构函数不会结束对象生命周期：

```c++
template<class T>
void destroy(T* p) {
    p->~T();
}
```

上面代码中，
- 若 `T` 为非平凡类型时，`~T()` 对结束对象的生命周期；
- 若 `T` 为平凡类型，如 `int` 等，C++20 之前 `~T()` 会被认为是无效语句，C++20 之后则会结束对象生命周期。
