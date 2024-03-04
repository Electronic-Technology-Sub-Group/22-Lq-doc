# 可变参数模板

C++11 开始，允许类或函数模板的形参数量是可变的，在形参变量前加 `...` 即可

```c++
// 函数模板
template<class ...Args>
void foo(Args ...args) {
    // do something
}

// 类模板
template<class ...Args>
class bar {
public:
    bar(Args ...args) {
        // do something
        foo(args...);
    }
};
```

其中，
- `class ...Args`：类型模板形参包，接受零个或多个类型的模板形参
- `Args ...args`：函数形参包，出现在函数形参列表中，接受零个或多个函数实参
- `args...`：形参包展开，将形参包展开为零个或多个模式列表（解包）

可变参的一些特点与限制：

1. 不能使用类型自动推断，但可以手动指定类型：

```c++
template<class ...Args>
class bar {
public:
    bar(Args ...args) {
        // do something
        foo(args...);
    }
};

int main() {
   bar<> b1;
   bar<int> b2;
   bar<int, double> b3;
}
```

2. 类模板中，可变参数模板必须是最后一个模板参数，但函数模板中可以不是，只要保证后续参数类型能匹配即可

```c++
template<class ...Args, class T, class U = double>
void foo(T, U, Args ...args);
```

3. 可变参数适用于非类型模板形参

```c++
template<int ...Args>
void foo();

foo<1, 2, 10, 20>();
```

4. 在进行模板匹配时，可变参的优先级低于有固定数量参数的模板
## 形参包展开

形参包展开的形式是一个包含形参包的表达式后加 `...`，可用于大部分常见场景：
- 表达式列表，初始化列表，成员初始化列表
- 函数参数列表，lambda 表达式捕获列表，模板参数列表
- 基类描述，C++17前动态异常列表，属性列表
- `sizeof...` 运算符，对其运算符（？）

```c++
template<class T, class U>
T baz(T t, U u) {
    cout << t << ": " << u << endl;
    return t;
}

template<class ...Args>
void foo(Args ...args) {
}

template<class ...Args>
struct bar {
    bar(Args ...args) {
        foo(baz(&args, args)...);
    }
};

int main() {
    // 0x16d01ffb90: hello
    // 0x16d01ffb88: 8
    // 0x16d01ffb80: 5
    // 0x16d01ffb78: 1
    bar b(1, 5.0, 8LL, "hello");
    return 0;
}
```

上面的代码片段中展示了如何展开形参包的过程，其运行结果有点类似于 `foreach`。`foo` 是一个中间函数，实际什么都不做，只是承载中间的计算结果。`bar` 类在其构造函数中对形参包进行展开，其中 `bar(&args, args)...` 就是包展开，`bar(&args, args)` 是模式，即展开的方法。上面这段代码实际上相当于：

```c++
struct bar {
    bar(int v1, double v2, long long v3, const char *v4) {
        auto p4 = baz(&v4, v4);
        auto p3 = baz(&v3, v3);
        auto p2 = baz(&v2, v2);
        auto p1 = baz(&v1, v1);
        foo(p1, p2, p3, p4);
    }
};
```

现在，我们再嵌套一层：

```c++
template<class ...T>
int baz(T ...t) {
    // ...
    return 0;
}

template<class ...Args>
void foo(Args ...args) {
}

template<class ...Args>
struct bar {
    bar(Args ...args) {
        foo(baz(&args) + args...);
    }
};
```

上面那段代码涉及到两层包展开：

1. `baz(&args)` 在 `baz(T ...t)` 中进行展开，结果为 `baz(&v1, &v2, ...)`
2. `foo(baz(args) + args...)` 在 `foo(Args ...args)` 中进行展开

最终结果为：

```c++
foo(
    baz(&v1, &v2, &v3, ...) + v1, 
    baz(&v1, &v2, &v3, ...) + v2, 
    baz(&v1, &v2, &v3, ...) + v3, 
    ...
)
```

形参包展开的模式非常灵活，比如：

```c++
int add(int a, int b) { return a + b; }

int sub(int a, int b) { return a - b; }

template<class ...Args>
pair<int, int *> build_array(Args (*...op)(int, int)) {
    int count = 0;
    int tmp[] = {(count++, op(7, 11)) ...};
    int *arr = new int[count];
    memcpy(arr, tmp, count * sizeof(int));
    return {count, arr};
}

int main() {
    auto [count, array] = build_array(add, sub);
    // Array 0x1bd597d1a80 has 2 values.
    cout << "Array " << array << " has " << count << " values." << endl;
    // 18 -4 
    for (int i = 0; i < count; ++i) cout << array[i] << ' ';
    delete[] array;
    return 0;
}
```

上面的代码将包展开用于构造数组，在 `build_array` 中展开的结果为（仅节选包展开部分）：

```c++
// int tmp[] = {(count++, op(7, 11)) ...};
int tmp[] = {
    (count++, add(7, 11)),
    (count++, sub(7, 11))
};
```

其中，`(count++, op(7, 11)) ...` 为包展开，`(count++, op(7, 11))` 为包展开模式。这种模式还可以用于类继承：

```c++
template<class ...Args>
class derived: public Args... {
public:
    derived(const Args& ...args): Args(args)... {}
};
```

上面的例子中，构造函数中涉及到了包展开，`Args(args)...` 是包展开，`Args(args)` 是模式。

包展开还可以用于 `lambda` 表达式中，一个比较常见的使用场景是延迟计算：

```c++
template<class F, class ...Args>
auto delay_invoke(F f, Args ...args) {
    return [f, args...]() -> decltype(auto) {
        return std::invoke(f, args...);
    };
}
```

以上例子中，两次展开，第一次在捕获列表中，将参数捕获；第二次在函数调用中，将捕获的参数作为参数传入。

通过形参包的展开，还可以实现类似于 `reduce` 函数的递归形式：

```c++
template<class T>
T sum(T arg) {
    return arg;
}

template<class T, class ...Args>
auto sum(T begin, Args ...args) {
    return begin + sum(args...);
}
```

可以看到，在 `sum(T, Args)` 中进行了展开，每次都会将 `...args` 中的第一个值作为 `begin` 加入函数并消掉，最后只剩下一个值由 `sum(T)` 接收。最后的结果类似于以下的递归调用：

```c++
auto sum(v1, v2, v3, v4, v5, v6, ...) {
    // v1 + (v2 + (v3 + (v4 + (v5 + (v6 + (...))))))
    return v1 + sum(v2, v3, v4, v5, v6, ...);
}
```

这种展开方式在 C++17 中会提供[[#折叠表达式]]作为更简洁的表达形式。
## 嵌套的形参包

可以在模板中嵌套一个模板的形参包，但被嵌套的包无法解开：

```c++
template<template<class...> class ...Args>
class bar: public Args<int, double>... {
public:
    bar(const Args<int, double>& ...args) : Args<int, double>(args)... {}
};

template<class ...Args>
class baz1 {};

template<class ...Args>
class baz2 {};

int main() {
    baz1<int, double> a1;
    baz2<int, double> a2;
    bar b(a1, a2);
    return 0;
}
```

上面的例子中，`baz1` 和 `baz2` 是两个带有形参包的类型，而 `bar` 模板上也需要一个形参包。看似 `bar` 包含两个形参包，但内层的 `template<class...>` 是无法（在 `bar` 中）展开的，因此事实上只包含了一个形参包。事实上 `bar` 继承自 `baz1<int, double>`，`baz2<int, double>` 两个模板实例而不是 `baz1<...Args>`，`baz2<...Args>`，因为两个类的模板缺少模板实参，无法实例化。

事实上一个类中是可以存在多个形参包的，但往往需要这几个形参包的长度是相同的：

```c++
template<class ...>          struct Tuple {};
template<class T1, class T2> struct Pair {};

template<class ...FirstArgs>
struct Zip {
    template<class ...SecondArgs>
    struct with {
        typedef Tuple<Pair<FirstArgs, SecondArgs>...> type;
    };
};

int main() {
    // 展开后的类型：Tuple<Pair<int, char>, Pair<double, string>, Pair<float, int>>
    Zip<int, double, float>::with<char, string, int>::type t;
    return 0;
}
```
## sizeof...

C++11 引入 `sizeof...(args)` 运算符，针对形参包，可以获取形参包中形参的个数，返回类型为 `std::size_t`。
## 折叠表达式

前面[[#形参包展开]]最后一节模拟了递归展开/`reduce` 函数的执行方式，但过于繁琐且不直观。

C++17 新增形参包展开用于替代递归的形参包展开，于是我们可以写成：

```c++
template<class ...Args>
auto sum(Args ...args) {
    return (args + ...);
}
```

C++17 添加以下四种折叠表达式，其中 `op` 为任意二元运算符，`k` 为任意值，`args` 为形参包名，其中的值为 `v1, v2, v3, ..., vN`：
1. `(args op ...)` 展开得 `(v0 op (v1 op (v2 op (v3 op (...)))))`
2. `(... op args)` 展开得 `(((v0 op v1) op v2) op v3) op ...`
3. `(args op ... op k)` 展开得 `(v0 op (v1 op (v2 op (v3 op (... op (vN op k))))))`
4. `(k op ... op args)` 展开得 `(((v0 op v1) op v2) op v3) op ...`

其中，第 1 和第 2 条在空包（即 `args` 中没有值）的情况下会有问题，因为 `op` 是二元运算符，第 3，4 条由于额外的一个 `k` 得以避免。于是有了以下规则：
- 仅 `op` 为 `&&`，`||`，`,` 运算符时支持空包
- `&&` 的空包结果为 `true`，`||` 的空包结果为 `false`
- `,` 的空包结果为 `void()`
## using 声明中的包展开

C++17 开始，允许 `using` 声明列表内的包展开，便于可变参数类模板派生于形参包的情况

```c++
template<class T>
class base {
public:
    base() {}
    base(T t) {}
};

template<class ...Args>
class derived : public base<Args>... {
public:
    using base<Args>::base...;
};
```
## `lambda` 初始化捕获包展开

```c++
template<class F, class ...Args>
auto delay_invoke(F f, Args ...args) {
    return [f, args...]() -> decltype(auto) {
        return std::invoke(f, args...);
    };
}
```

在之前的 `delay_invoke` 实例中，如果参数过大会产生过多的额外消耗（因为这里是复制构造）。如果是使用引用传递，又依赖于外部的函数环境，因此使用初始化捕获+移动语义是一个比较合理的解决方案：

```c++
template<class F, class ...Args>
auto delay_invoke(F f, Args ...args) {
    using namespace std;
    return [f = move(f), tup = make_tuple(move(args)...)]() -> decltype(auto) {
        return apply(f, tup);
    };
}
```

这样使用 tuple 进行打包，但一旦复杂起来（例如固定被调函数 `f`，此时需要在 `apply` 第一个参数传入一个 `lambda` 表达式中使用引用）难以理解

```c++
template<class ...Args>
auto foo(Args ...args) {
    return (args + ... + 0);
}

template<class ...Args>
auto delay_invoke(Args ...args) {
    return [tup = std::make_tuple(std::move(args)...)]() -> decltype(auto) {
        return std::apply([](auto& ...args) -> decltype(auto) { return foo(args...); } , tup);
    };
}
```

C++20 开始允许形参包展开的初始化捕获。但与普通包展开的形式有所不同的是，`...` 在模式之前

```c++
template<class ...Args>
auto foo(Args ...args) {
    return (args + ... + 0);
}

template<class ...Args>
auto delay_invoke(Args ...args) {
    return [...args = std::move(args)]() -> decltype(auto) {
        return foo(args...);
    };
}
```
# `typename` 支持声明模板形参

C++17 之前，模板中仅能用 `class` 声明带模板的形参

```c++
template<typename T> struct A{};
template<template<typename> class T> struct B{};
```

C++17 之后，下面的情况也可以实现了：

```c++
template<template<typename> typename T> struct B{};
```
# 放宽的模板限制

- C++11 之后，允许局部和匿名类型作为模板实参

```c++
template <class T> class X {};
template <class T> void f(T t) {};

// 匿名类型
struct {} unnamed_obj;

int main() {
    // 局部类型
    struct A {};
    typedef struct {} B;
    // 局部匿名类型
    enum { e };

    X<A>  x1;
    X<A*> x2;
    X<B>  x3;
    f(e1);
    f(unnamed_obj);
    B b;
    f(b);
}
```

- C++11 后，允许函数模板的默认模板参数，而且不需要遵守必须从左往右定义的规则

```c++
template<class T = double>
void foo() { T t; };
```

- C++17 后，对于任何类型的非类型模板形参使用的实参，都可以使该模板形参类型的任何经转换表达式

> [!note]
> C++17 之前，作为模板实参的值有比较严格的要求，最主要的是链接和静态：
> - 整型：经转换（可以隐式转换成整数）的常量表达式
> - 对象指针：静态，或有内部或外部链接的完整对象
> - 函数指针：有链接的函数指针
> - 左值引用：有内部或外部链接
> - 成员指针：静态成员

- C++17 后，允许模板形参至少和实参列表一样特化 -- 匹配时可以忽略默认值了

```c++
template<template<class> class T, class U>
void foo() {
    T<U> n;
}

template<class, class = int> 
struct bar {};

int main() {
    // bar 匹配 T
    // T 要求有一个类型模板, bar 有两个类型模板但其中一个默认 int
    foo<bar, double>();
    return 0;
}
```

配合 `auto` 作为非类型模板形参占位符，有以下写法（注意与上一个例子不等效）：

```c++
template<template<auto> class T, auto N>
void foo() {
    T<N>();
}

template<auto>
struct bar {};

int main() {
    foo<bar, 5>();
    return 0;
}
```

- C++20 后，允许非类型模板参数中的字面量类类型，字面量类类型可用于非类型模板参数，限制有：
	- 所有基类和非静态数据成员都是 `public` 且不可变的
	- 所有基类和非静态数据成员都是标量类型、左值引用或其数组

```c++
// 一个类类型
template<class T, size_t N>
struct basic_fixed_string {
    T data[N + 1];

    constexpr basic_fixed_string(const T(&foo)[N + 1]) {
        copy_n(foo, N + 1, data);
    }
};

// 允许一个字符串隐式转换成 basic_fixed_string
template<class T, size_t N>
basic_fixed_string(const T(&foo)[N]) -> basic_fixed_string<T, N - 1>;

template<basic_fixed_string str>
struct X {
    X() {
        cout << str.data;
    }
};

int main() {
    // 实参
    X<"hello world"> x;
    return 0;
}
```
# ADT 可以查找函数模板

> [!note]
> ADT：C++参数依赖查找。当编译器对无限定域的函数调用进行名字查找时，除了当前名字空间域以外，也会把函数参数类型所处的名字空间加入查找的范围。

C++20 之后，编译器 ADT 规则支持在参数命名空间中查找带显示指定模板实参的函数模板。

```c++
int h = 0;
void g() {};
namespace N {
    struct A{};
    template<class T> void f(T) {};
    template<class T> void g(T) {};
    template<class T> void h(T) {};
}

int main() {
    // 成功
    f<N::A>(N::A());
    // 成功
    g<N::A>(N::A());
    // 失败：存在变量 h，可被解析成 h <N::A > (N::A())，存在歧义
    h<N::A>(N::A());
    return 0;
}
```
# 类模板实参推导

- C++17 之后，允许通过初始化构造推导类模板实参，但不允许部分推导
	- 注意：C++20 规定，当拷贝初始化可以使用时，拷贝初始化优先

```c++
template<class F, class S>
struct MyPair {
    MyPair(F &&f, S &&s): _f(std::move(f)), _s(std::move(s)) {
    }

    F _f;
    S _s;
};

int main() {
    // p: MyPair<int, double>
    MyPair p {3, 5.0};
    // 错误 error: wrong number of template arguments (1, should be 2)
    MyPair<int> pp {3, 5.0};
    return 0;
}
```

对于非类型（包括 `auto`）也可以进行推导

```c++
template<class T, auto N>
struct CountOf {
    CountOf(T(&)[N]) {}
};

int main() {
    // c: CountOf<char, 7>
    CountOf c{"string"};
    return 0;
}
```

该规则也可以用于简化 `lambda` 表达式作为数据成员存储时的写法。C++17 前：

```c++
template<class T>
struct LambdaWrap {
    T func;
    LambdaWrap(T t): func(t) {}

    template<class ...Args>
    void operator()(Args&& ...args) {
        func(std::forward<Args>(args)...);
    }
};

int main() {
    auto f = [](int a, float b) {
        cout << a << ' ' << b;
    };
    // 需要使用 decltype 推导 f 的类型
    LambdaWrap<decltype(f)> wrap(f);
    wrap(1, 2.3f);
    return 0;
}
```

C++17 后，可以省略 `decltype`：

```c++
int main() {
    auto f = [](int a, float b) {
        cout << a << ' ' << b;
    };
    LambdaWrap wrap(f);
    wrap(1, 2.3f);
    return 0;
}
```

- C++20 之后，支持别名模板的类模板实参推导

```c++
template<class T, class U>
struct C {
    C(T, U) {}
};

template<class V>
using A = C<V*, V*>;

int main() {
    int i;
    double d;

    // 编译通过 T, U 类型均为 int*
    A a1(&i, &i);
    // 编译错误: int 无法匹配指针类型
    // error: class template argument deduction failed
    A a2(i, i);
    // 编译错误: int*, double* 无法匹配 V*, V*
    // error: class template argument deduction failed
    A a3(&i, &d);
    return 0;
}
```

- C++20 后，支持聚合类型的类模板实参推导

```c++
template<class T>
struct S {
    T x;
    T y;
};

int main() {
    S {1, 2};
    // 编译错误: x:int y:uint 无法匹配 x:T y:T
    // error: class template argument deduction failed
    S {1, 2u};
    return 0;
}
```
# 自定义推导指引

先看下之前的 `MyPair` 类型（改名为 `Pair`）：

```c++
template<class F, class S>
struct Pair {
    F _f;
    S _s;

    Pair(const F &f, const S &s): _f(std::move(f)), _s(std::move(s)) {}
};

int main() {
    Pair p1(10, 20);
    // error: array used as initializer
    Pair p2(10, "hello");
    return 0;
}
```

上面代码中，`p2` 对象无法编译成功，因为 `p2` 中类型为 `char[6]` 无法使用构造函数。STL 中 `pair` 会将其退化成指针，于是有了 `make_pair`，在 C++11 使用 `std::decay` 完成退化，C++11 之前使用函数传参进行退化。

C++17 开始，支持类模板的自定义推导指引，只需要使用类似 函数返回值后置 的语法声明即可，函数名使用类名（类似构造函数）。

```c++
template<class F, class S>
struct Pair {
    F _f;
    S _s;

    Pair(const F &f, const S &s): _f(std::move(f)), _s(std::move(s)) {}
};

template<class F, class S> Pair(F, S) -> Pair<F, S>;
```

`template<class F, class S> Pair(F, S) -> Pair<F, S>` 就是一个典型的用户自定义推导指引。
- `template<class F, class S>` 是类模板名
- `Pair(F, S)` 是形参声明
- `Pair<F, S>` 是指引的目标类型
这条用户自定义推导表示，编译器直接推导按值传递的实参，即每个值不是以引用，而是以按值传递的形式推导类型，此时数组退化成了一个指针。

实际推导方式非常灵活：

```c++
// 使用 std::common_type 将多个不同类型统一成相同类型存入 vector
// 例如：int, unsigned int, double 统一成 double
namespace std {
    template<class ...T>
    vector(T&& ...t) -> vector<std::common_type_t<T...>>;
}

// 将 const char* 推导为 std::string
template<class F> Pair(F, const char*) -> Pair<F, std::string>;

// 要求使用显式对象构造（p = {1, "2"} 的形式无法编译）
template Pair(int, const char*) -> Pair<long long, std::string>;
```

C++20 开始，支持聚合类型类模板的推导指引

```c++
template<class T>
struct Wrap {
    T data;
};

template<class T> Wrap(T) -> Wrap<T>;

int main() {
    // ok
    Wrap w = {7};
    return 0;
}
```
# SFINAE

> [!note]
> SFINAE：`Substitution Failure Is Not An Error`，替换失败不是错误。在函数模板重载时，当 *模板形参替换为指定的实参* 或 *由模板实参推导出模板形参* 的过程中出现了失败，则放弃这个重载而不是抛出一个编译错误。
> 
> 替换失败：在直接上下文中使用模板实参替换形参后，类型或表达式不合法。
> 编译错误：替换后在非直接上下文中产生副作用导致错误。

`SFINAE` 规则在 C++11 明确指定。但 C++03 及之前的版本都没有明确禁止，所以支持 C++11 的编译器有时也会在指定老版本 C++ 编译代码时使用该规则。
## 替换失败

由于编译错误比替换失败更容易列举，这里只列举编译失败的例子，除开编译失败外其他模板错误均为替换失败：

1. 处理表达式外部某些实体时发生错误

```c++
class bar {
public:
    bar() {};
    // error: expected ',' or '...' before '&&' token
    // error: invalid constructor; you probably meant 'bar (const bar&)'
    bar(bar&&) {};
};

template<class T>
T foo(T& t) {
    T tt(t);
    return t;
}

void foo(...) {
}

int main() {
    bar b;
    foo(b);
    return 0;
}
```

这是编译错误。`foo` 在根据函数声明进行推断时，可以匹配 `T foo(T&)`，`T` 推断为 `bar`。在执行到 `foo` 函数中时，发现 `T tt(t)` 一步无法生成复制构造，造成编译错误

2. 由于实现受限导致错误：代码可能正确，但由于编译器实现上的限制造成了错误

3. 访问违规

```c++
class bar {
    bar() {};
};

template<class T>
T foo(T*) {
    // error: 'bar::bar()' is private within this context
    return T();
}

void foo(...) {
}

int main() {
    foo(static_cast<bar*>(nullptr));
    return 0;
}
```

编译错误。`foo` 在进行推断时，可匹配 `T foo(T*)`，`T` 推断为 `bar`。在执行到 `foo` 函数中时，发现 `T()` 一步无法访问私有构造，造成编译错误

4. 由于同一个函数的不同声明的词法顺序不同，导致替换顺序不同或无法替换

```c++
template<class T> struct A { using X = typename T::X; };
template<class T> T::X foo(typename A<T>::X);
template<class T> void foo(...) {}
template<class T> auto bar(typename A<T>::X) -> T::X;
template<class T> void bar(...);

int main() {
    foo<int>(0);
    // error: 'int' is not a class, struct, or union type
    bar<int>(0);
    return 0;
}
```

- 第一个 `foo<int>(0)` 编译通过。匹配 `T::X foo(A<T>::X)` 时找不到 `int::X`，产生替换失败，可以正常匹配到 `foo(...)`。
- 第二个 `bar<int>(0)` 编译错误。在匹配 `bar(A<T>::X)` 时，返回值后置，编译器由于 `A<int>::T` 实例化了一个模板，此时不再是直接上下文环境，不会触发替换失败，直接发生编译错误。
## SFINAE 实例

```c++
struct X {};
struct Y {
    Y(X) {}
};

X foo(Y, Y) { return X(); }

template<class T>
auto foo(T t1, T t2) -> decltype(t1 + t2) {
    return t1 + t2;
}

int main() {
    X t1, t2;
    auto t3 = foo(t1, t2);
    return 0;
}
```

上面的例子中，`t1`，`t2` 类型为 X，`X + X` 并没有对应的类型，触发 SFINAE，模板不会进行实例化，转而调用 `X foo(Y, Y)`；而 `Y` 可以由 `X` 隐式转换而来，可以正常编译，`t3` 类型为 `X`

```c++
template<int I>
void foo(char(*)[I % 2 == 0] = nullptr) {
    cout << "I % 2 == 0\n";
}

template<int I>
void foo(char(*)[I % 2 != 0] = nullptr) {
    cout << "I % 2 != 0\n";
}

int main() {
    char a[1];
    // I % 2 != 0
    foo<5>(&a);
    // I % 2 == 0
    foo<4>(&a);
    return 0;
}
```

上面的例子中，根据 `I` 值的奇偶性可以引出 `foo` 后面的形参的两种情况：
- `char(*)[0]=nullptr`：传入的数组长度为 1，替换失败
- `char(*)[1]=nullptr`：编译通过
通过这种方法，可以实现编译时的函数选择

```c++
class SomeObj {
public:
    void Dump2File() const {
        cout << "dump the object to file\n";
    }
};

template<class T>
auto DumpObj(const T &t) -> decltype(((void) t.Dump2File(), void())) {
    t.Dump2File();
}

void DumpObj(...) {
    cout << "object must have function Dump2File()\n";
}

int main() {
    SomeObj v1;
    string v2;
    // dump the object to file
    DumpObj(v1);
    // object must have function Dump2File()
    DumpObj(v2);
    return 0;
}
```

上面的例子有一定的实用性了。`DumpObj` 方法要求给定对象需要有 `Dump2File` 方法，通过 `decltype` 检查了一次 `Dump2File` 方法，若没有该方法触发替换失败，调用 `void DumpObj(...)` 重载。
## 约束

STL 标准库中提供了便于使用 SFINAE 的模板函数 `enable_if` 称为约束，位于 `type_traits` 头文件中。该头文件中还存在一系列用于判断的条件。

```c++
template<class T, class U = enable_if_t<is_integral_v<T>>>
struct X {};

int main() {
    // 编译成功
    X<int> x1;
    // 编译失败
    X<string> x2;
    return 0;
}
```
# 概念与约束

概念是 C++20 对[[#SFINAE#约束|约束模板]]的扩展，可以将约束的类型从模板中剥离出来，分为概念和约束表达式两部分。
## 概念

完整的概念使用 `concept` 声明，并替换 `class` 或 `typename`，被替换的名称称为概念，右侧的表达式称为约束表达式，如下面例子中的 `IntegralType`

```c++
template<class T>
concept IntegralType = is_integral_v<T>;

template<IntegralType T>
struct X {};
```

概念也可以对 `auto` 进行约束

```c++
template<class T>
concept IntegralType = is_integral_v<T>;

void foo(IntegralType auto v) { ... }
```

**注意：概念必须紧跟被修饰类型，中间不能加 `const`**

```c++
template<class C>
concept IntType = is_integral_v<C>;

IntType auto i = 1;
const IntType auto j = 2;
IntType auto const k = 3;
// error: expected 'auto' or 'decltype(auto)' after 'IntType'
IntType const auto l = 3;
```
## require 约束

对于简单的约束，也可以直接使用 `requires` 对类型进行约束，可以出现在模板形参列表或函数声明的尾部：

```c++
template<class T>
// 位于模板形参列表尾部
requires std::is_integral_v<T>
struct X {};


template<class T>
T add(T a, T b)
// 位于函数声明尾部
requires std::is_integral_v<T>
{
    return a + b;
}
```

使用概念和约束表达式可以使异常信息更加清晰：

```c++
template<class T>
requires std::is_integral_v<T>
struct X {};

int main() {
    // error: template constraint failure for 'template<class T>  requires  is_integral_v<T> struct X'
    X<string> x1;
    return 0;
}
```

约束表达式应当是一个类型为 `bool` 的纯右值常量表达式，结果为 `true` 表示约束检查通过，替换结果合法。
- `&&` 运算称为约束的合取
- `||` 运算称为约束的析取

当约束表达式存在于 `requires` 子句中时，约束表达式有了进一步的要求：
- 若表达式不是一个初等表达式，应当使用括号包围
- 只能是几个初等表达式（或带括号的任意表达式）与 `&&`，`||` 的组合

当存在多种约束时，检查的优先级如下：
1. 模板形参列表中的形参约束
2. 模板形参列表之后的 `requires` 子句
3. 简写函数模板声明中修饰 `auto` 的概念
4. 函数模板声明尾部的 `requires` 子句
## 原子约束

>[!note]
>原子约束：表达式和表达式中模板形参到模板实参映射的组合，简称为形参映射

当且仅当代码上具有相同表现，且形参映射相同时，两个原子约束是相同的。

```c++
template<int N> constexpr bool Atomic = true;
template<int N> concept C = Atomic<N>;
template<int N> concept Add1 = C<N + 1>;
template<int N> concept AddOne = C<N + 1>;
template<int M> void f() requires Add1<2 * M> {};
template<int M> void f() requires AddOne<2 * M> && true {};
```

上面例子中，`Add1` 与 `AddOne` 约束是相同的，形参映射都是 `N ~ 2 * N + 1`。此时，编译器会选择 `AddOne<2 * M> && true` 作为目标函数，因为其更加复杂，且包含了 `Add1<2 * M>`

```c++
template<int M> void f() requires Add1<2 * M> {};
template<int M> void f() requires Add1<M * 2> && true {};
// error: call of overloaded 'f<10>()' is ambiguous
```

如果改成这样，则无法通过编译，因为两个 `Add1` 的形参映射分别为 `2 * M + 1`和 `M * 2 + 1`，在形式上是不同的。

*当函数存在原子约束时，如果约束表达式的结果是相同的，则约束表达式应当是相同的。*

```c++
template<class T> concept sad = false;
template<class T> int f(T) requires (!sad<T>) { return 1; };
template<class T> int f(T) requires (!sad<T>) && true { return 1; };
```

以上代码编译失败，因为两个 `!sad<T>` 是来自不同的约束表达式。应该为

```c++
template<class T> concept sad = false;
template<class T> concept not_sad = !sad<T>;
template<class T> int f(T) requires (not_sad<T>) { return 1; };
template<class T> int f(T) requires (not_sad<T>) && true { return 1; };
```
## requires 表达式

`requires` 除了引入 `requires` 子句，还可以用于定义 `requires` 表达式。该表达式为一个纯右值表达式，对其进行模板实参替换，若替换之后 `requires` 表达式出现无效类型或违反约束条件，则表达式结果为 `false`，测试不通过。

```c++
// 检查 T().clear() 是一个合法表达式
// 即 T 有一个无参构造且具有 clear() 函数
template<class T>
concept check = requires {
    T().clear();
};

template<check T>
struct G {};

int main() {
    // 通过
    G<vector<char>> g1;
    // 通过
    G<string> g2;
    // error: template constraint failure for 'template<class T>  requires  check<T> struct G'
    G<array<char, 10>> g3;
    return 0;
}
```

`requires` 还支持形参表达式，但注意形参不具备生命周期和存储方式，只用于编译期检查，因此不支持需要运行时计算实参数量的不定参数，也不能对变量的具体值进行测试：

```c++
// 这个版本不检查构造函数
template<class T>
concept check = requires(T t) {
    t.clear();
};
```

> [!note]
> 要求序列：对模板实参的若干要求形成的序列，分为简单要求、类型要求、复合要求和嵌套要求四种。

- 简单要求：不以 `requires` 开始的要求，只断言表达式有效性，不进行求值，若替换失败则计算结果为 `false`

```c++
template<class T>
concept C = requires(T a, T b) {
    a + b;
};
```

- 类型要求：以 `typename` 开始的要求，用于检查嵌套类型、类模板、别名模板特化的有效性。若模板替换失败则结果为 `false`

```c++
template<class T, class T::type = 0> struct S;
template<class T> using Ref = T&;
template<class T> concept C  = requires {
    typename T::inner; // 检查嵌套类型
    typename S<T>;     // 检查类模板特化
    typename Ref<T>;   // 检查别名模板特化
};
```

- 复合要求：由 `{ 表达式 }`，`noexcept`，`-> 返回类型约束` 组成的多个约束
	- 替换模板实参，检查表达式有效性
	- 若存在 `noexcept`，检查表达式没有抛出异常的可能
	- 若存在 `->`，则确保 `decltype<表达式结果>` 满足返回类型约束

```c++
template<class T>
concept check = requires(T t1, T t2) {
    { t1.clear() } noexcept;
    { t1 + t2 } -> same_as<int>;
};
```

- 嵌套要求：以 `requires` 开始的要求，通常用于根据局部形参指定其他额外要求：

```c++
template<class T>
concept check = requires(T t1, T t2) {
    requires same_as<decltype(t1 + t2), int>;
    // 等同于
    {t1 + t2} -> same_as<int>;
};
```
## 约束可变参模板

约束可以修饰可变参数，展开后表示对形参包中每一个参数进行约束，然后进行合取

```c++
template<class T> concept Checked = true;
template<Checked ...T> struct X;

// 约束: Checked<int> && Checked<double> && Checked<float>
X<int, double, float> x;
```

```c++
template<class T, class U> concept Checked = true;
template<Checked<int> ...T> struct X;

// 约束: Checked<int, int> && Checked<double, int> && Checked<float, int>
X<int, double, float> x;
```
## 约束类模板特化

约束可以影响类模板特化，编译器会选择更满足约束条件的特化版本

```c++
template<class T> concept C1 = is_signed_v<T>;
template<class T> concept C2 = C1<T> && is_floating_point_v<T>;

template<class T> struct X {
    X() { cout << "template<T> X\n"; }
};

template<class T> struct X<T*> {
    X() { cout << "template<T*> X\n"; }
};

template<C1 T> struct X<T> {
    X() { cout << "template<C1 T> X\n"; }
};

template<C2 T> struct X<T> {
    X() { cout << "template<C2 T> X\n"; }
};

// template<T*> X
X<int*> x1;
// template<C1 T> X
// C2 包含 C1，但 C2 不符合，使用 C1
X<int> x2;
// template<C2 T> X
// C2 包含 C1
X<float> x3;
// template<T> X
X<string> x4;
```
## 约束 auto

约束可以约束 `auto` 和 `decltype(auto)`

```c++
template<class C>
concept IntType = is_integral_v<C>;

IntType auto i1 = 5;
// error: deduced initializer does not satisfy placeholder constraints
IntType auto i2 = 5.1;

IntType decltype(auto) i3 = 7;
// error: deduced initializer does not satisfy placeholder constraints
IntType decltype(auto) i4 = 7.1;

IntType auto foo1() { return 0; }
// error: deduced return type does not satisfy placeholder constraints
IntType auto foo2() { return 0.1; }

auto bar1 = []() -> IntType auto { return 1; };
// error: deduced return type does not satisfy placeholder constraints
auto bar2 = []() -> IntType auto { return 1.1; };
```
# 外部模板

C++11 支持使用 `extern` 声明外部模板，该声明允许模板不在当前文件中实例化，可用于函数模板和类模板。

如果不使用外部模板，C++ 编译器会在所有使用模板的文件中实例化对应模板。虽然最终链接器会对重复的模板进行去重操作，不影响编译的正确性，但在文件过多时，影响编译效率。

```c++
// 模板定义的文件中，特化指定类型的模板函数
template<class T>
void foo() {
    cout << "foo<T>\n";
}

template<>
void foo<int>() {
    cout << "foo<int>\n";
}

template<>
void foo<double>() {
    cout << "foo<double>\n";
}
```

```c++
// 模板实际使用位置
// extern template 特化的模板声明;
extern template void foo<int>();
extern template void foo<double>();

int main() {
    // foo<T>
    foo<string>();
    // foo<int>
    foo<int>();
    // foo<double>
    foo<double>();
    return 0;
}
```
# `friend` 声明模板形参

在 C++11 之后，友元类可以忽略 `class`（C++11 之前可能允许，看编译器）

```c++
class C;
class X {
    friend C;
}
```

此时，类型 `C` 可以是一个普遍意义上的类型，而不一定是一个类甚至不一定是一个具体类 -- 可以是基本类型，可以是类型模板参数，可以是类型别名等

```c++
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

于是，我们可以通过模板声明友元类了。一个使用场景如下：

```c++
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
# 变量模板

C++14 引入变量模板，允许根据不同类型生成不同变量

```c++
template<class T>
constexpr T PI = static_cast<T>(3.1415926);

template<class T, int N>
constexpr T NPI = static_cast<T>(3.1415926) * N;

int main() {
    cout << PI<int> << endl;
    cout << PI<float> << endl;
    cout << PI<double> << endl;
    cout << NPI<double, 5> << endl;
    return 0;
}
```

该语法可以简化 `type_traits` 等标准库的写法，但直到 C++17，该类型在标准库中才有所体现，即那些 `_v` 结尾的模板，直接代表结果变量。

```c++
bool b1 = std::is_same<int, std::size_t>::value;
bool b2 = std::is_same_v<int, std::size_t>;
```
# `explicit(bool)`

C++20 开始允许接受一个 `bool` 常量表达式，指定 `explicit` 修饰的功能是否生效。

首先，观察下面的代码：

```c++
pair<string, string> safe(const char* first, const char* second) {
    return {first, second};
}

pair<vector<int>, vector<int>> unsafe(int first, int second) {
    return {first, second};
}
```

上面代码中，`unsafe` 编译失败。这是符合直觉的 - `const char*` 可以转化为 `string`，但 `int` 不能转换为 `vector<int>`

现在看看这是如何实现的。首先考虑是因为 `vector(T...)` 构造函数被 `explicit` 修饰，但实际上并不是，因为 pair 构造中为 `first`，`second` 赋值是通过间接构造完成，在构造时是显式调用的构造函数，不受 `explicit` 影响。

```c++
template<class F, class S>
class MyPair {
    F _f;
    S _s;
public:
    template<class _F, class _S>
    // 显式调用构造函数
    MyPair(_F &f, _S &s): _f(f), _s(s) {
    }
};

MyPair<vector<int>, vector<int>> unsafe(int first, int second) {
    return {first, second};
}
```

若要解决这个问题，我们可以给 `MyFirst` 的构造函数添加 `explicit` 修饰以禁用该构造函数：

```c++
template<class F, class S>
class MyPair {
    F _f;
    S _s;
public:
    template<class _F, class _S>
    // 显式调用构造函数
    explicit MyPair(_F &f, _S &s): _f(f), _s(s) {
    }
};
```

但这样 `safe` 函数也不能正常完成编译了。这时候考虑使用 `SFINAE` 进行条件编译

```c++
template<class F, class S>
class MyPair {
    F _f;
    S _s;
public:
    template<class _F = F, class _S = S>
    // 约束：
    //    1. F 可通过 _F 构造，S 可通过 _S 构造
    //    2. _F 可转换为 F，_S 可转换为 S
    requires is_constructible_v<F, _F> && is_constructible_v<S, _S> 
          && is_convertible_v<_F, F>   && is_convertible_v<_S, S>
    MyPair(_F &f, _S &s): _f(f), _s(s) {
    }
    
    template<class _F = F, class _S = S>
    requires is_constructible_v<F, _F> && is_constructible_v<S, _S>
    explicit MyPair(_F&&, _S&&);
};
```

此时，可通过 `explicit` 进行开关控制：

```c++
template<class F, class S>
class MyPair {
    F _f;
    S _s;
public:
    template<class _F = F, class _S = S>
    requires  is_constructible_v<F, _F> &&  is_constructible_v<S, _S>
    explicit(!is_convertible_v<_F, F>   || !is_convertible_v<_S, S>)
    MyPair(_F &f, _S &s): _f(f), _s(s) {
    }
};
```