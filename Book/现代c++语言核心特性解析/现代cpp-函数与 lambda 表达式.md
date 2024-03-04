 # lambda 表达式

c++11 开始提供对 lambda 表达式的支持。

```
[ captures ] ( params ) specifiers exception -> ret { body }
```

- `captures`：捕获列表，捕获后可在 lambda 表达式内访问，包括按值捕获和引用捕获两种
- `params`：可选参数列表，格式与函数形参列表相同，若无参数则可省略包括括号在内的整个参数列表部分
- `specifiers`：可选限定符，C++11 可用 `mutable`
	- `mutable`：允许 lambda 表达式内修改按值捕获的参数值，或调用非 const 成员函数
- `exceptions`：异常列表，若无异常可省略或使用 `noexcept`
- `ret`：返回值类型，若返回 `void` 或可以直接通过函数体推断出来则可以省略包括 `->` 在内的整个返回值类型部分
- `body`：lambda 表达式的函数体，与普通函数的函数体相同

捕获列表和函数体是必须存在的部分，因此最简单的 lambda 表达式可以表示为 `[]{}`。

lambda 表达式实际上是一个语法糖，编译器自动生成一个重写了 `operator()` 运算符的类实例。 
## 捕获列表

在 `[]` 中直接输入变量表示捕获列表，默认使用按值捕获，即将变量值复制一份传入。

按值捕获实际上类似于 `static const` 修饰的局部变量，而不是函数参数。且复制传入的初值是在声明时对应变量的值，与调用时值无关。带有 `mutable` 修饰时则类似于不带 `const` 修饰符。

```c++
int a = 99;
// a 捕获的值为 99
auto f = [a] mutable { cout << (a++) << '\n'; };

a = 0;
f(); // 99，a=100

a = 1;
f(); // 100，a=101

a = 2;
f(); // 101，a=102
```

使用 `&` 表示该变量为引用捕获，捕获的是外部对象的引用。无论是否带有 `mutable` 该引用均可以被修改。

```c++
int a = 99;
auto f = [&a] { cout << (a++) << '\n'; };

a = 0;
f(); // 0
cout << a << '\n'; // 1
a = 10;
f(); // 10
cout << a << '\n'; // 11
a = 20;
f(); // 20
cout << a << '\n'; // 21
```

按值捕获和引用捕获可以混用，还有一些特殊的捕获方式：
- `[this]`：捕获 `this` 指针
- `[=]`：以按值捕获的形式，捕获 lambda 表达式所在作用域的所有变量值，包括 this
- `[&]`：以引用捕获的形式，捕获 lambda 表达式所在作用域的所有变量值，包括 this

`[=]`，`[&]` 后可以与带变量名的按值捕获、引用捕获连用，表示除后面这些变量外其他所有变量值以给定方式捕获。
## 无状态 lambda 表达式

没有任何捕获的 lambda 表达式称为无状态 lambda 表达式。该类表达式可以隐式转换为一个函数指针。

```c++
int (*op)(int, int);
op = [](int a, int b) { return a + b; };
```

在 c++20 后，无状态 lambda 表达式可以被构造 -- 即其对应类型有了一个无参构造、复制构造等。因此可以将其类型传入一些标准库的泛型（尤其是比较大小的泛型）中：

```c++
auto greater = [](auto x, auto y) { return x > y; };
std::map<std::string, int, decltype(greater)> a_map;
```
## 广义捕获

c++14 定义了两种 lambda 表达式的捕获方式 - 简单捕获和初始化捕获。

简单捕获即前面的按值捕获和引用捕获。初始化捕获由 c++14 引入，在捕获中使用 `=` 初始化某个变量，解决了简单捕获无法捕获表达式结果和自定义捕获变量名的问题。

```c++
int x = 5;
auto foo = [r = x + 1] { return r; };
```

以上实例中，`r` 作用域存在于 lambda 表达式中；`x` 作用域存在于上下文中，在 lambda 表达式中无法访问。广义捕获常用于：
- 允许在捕获时使用移动构造

```c++
string s;
auto foo = [str = std::move(s)] { ... };
```

- 传入 `this` 对象的副本，防止 lambda 表达式调用时外部对象被析构造成的未定义行为

```c++
class Work {
public:
    void test() {
        auto foo = [tmp = *this] { ... }
    }
}
```

在 c++17 中，可以使用 `*this` 捕获 `this` 对象的副本，下面的实例与上面作用相同：

```c++
class Work {
public:
    void test() {
        auto foo = [*this] { ... }
    }
}
```

为了更容易区分 `[=, *this]` 与 `[=]`，c++20 允许使用 `[=, this]` 的写法，该写法的效果与 `[=]` 相同。
- `[=, *this]`：捕获 this 对象副本和其他所有值副本
- `[=, this]` / `[=]`：捕获 this 指针和其他所有副本
## 泛型 lambda 表达式

c++14 允许 lambda 表达式带有模板能力，称为泛型 lambda 表达式，只需要将类型定义为 auto 即可：

```c++
// a 的类型为模板类型
auto f = [] (auto a) { return a; };

// a: int
int i = f(3);
// a: const char*
const char* s = f("hello world");
```

c++20 进一步添加模板对 lambda 的支持：

```c++
auto f1 = []<typename T>(std::vector<T> vector) { ... }
auto f2 = []<typename T>(T const& x) { ... }
```
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
# 返回值优化

> [!note]
> RVO：Return Value Optimization，返回值操作数为临时对象时的优化
> NRVO：Named Return Value Optimization：返回值操作数为具名对象时的优化

C++11 复制消除：在函数返回一个对象时，且返回的对象在编译期可确定，不再需要调用复制构造，可以直接转移到目标位置。该优化策略即 `RVO` 和 `NRVO` 优化。

```c++
struct X {
public:
    X() { cout << "X ctor\n"; }
    X(const X &x) { cout << "X copy ctor\n"; }
    ~X() { cout << "X dtor\n"; }
};

X make_x_rvo() {
    return {};
}

X make_x_nrvo() {
    X x;
    return x;
}

int main() {
    cout << "-------------------------------------\n";
    // X ctor
    X x1 = make_x_rvo();
    cout << "-------------------------------------\n";
    // X ctor
    X x2 = make_x_nrvo();
    cout << "-------------------------------------\n";
    return 0;
    // X dtor
    // X dtor
}
```

但如果编译器无法在编译时确定被构造对象是哪一个，则优化会失效：

```c++
struct X {
public:
    X() { cout << "X ctor\n"; }
    X(const X &x) { cout << "X copy ctor\n"; }
    ~X() { cout << "X dtor\n"; }
};

X make_x() {
    X x1, x2;
    // 编译器无法确定返回的是 x1 还是 x2
    if (time(nullptr) % 50) {
        return x1;
    } else {
        return x2;
    }
}

int main() {
    cout << "-------------------------------------\n";
    // X ctor
    // X ctor
    // X copy ctor  --> 从此处开始，返回值优化失效
    // X dtor
    // X dtor
    X x = make_x();
    cout << "-------------------------------------\n";
    return 0;
    // X dtor
}
```

> [!note]
> 上面的例子中，事实上复制消除还是消除了一次复制构造的调用的，即将 `make_x()` 的返回值复制到 `main` 中的 `x` 对象上

- 复制构造必须是可访问的，否则造成语法错误
- GCC 禁用复制消除：`-fno-elide-constructors`

C++14 要求对于常量表达式和常量初始化而言，编译器应保证 `RVO`，禁止 `NRVO`

C++17 规定，在传递临时对象或从函数返回对象时，编译器应忽略对象的复制构造和移动构造。
- 复制和移动构造在这两种情况下永远不会调用和检查，因此可以不存在或不可访问
- 复制和移动构造中若还有其他副作用，也不会调用
- 最终效果是将对象直接构造到目标的存储变量上，避免临时对象的产生
该规定允许所有类型都可以使用工厂函数。