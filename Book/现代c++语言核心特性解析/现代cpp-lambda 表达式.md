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