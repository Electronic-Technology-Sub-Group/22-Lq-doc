# `auto` 类型推断

> [!note] auto
> `auto` 关键字并不是 c++11 新加入的关键字。从 c 语言时期开始就存在 `auto` 关键字，用于声明变量具有自动的生命周期（即在一个 `{}` 之间有效，与之相对的是 `static`）。但由于默认情况下声明的变量就是自动生命周期，该关键字总是被省略。

自 C++ 11 开始，代码中我们可以通过 `auto` 让编译器自动推断一个变量的类型，称为类型推断。其形式为 `auto x = expr`。
## 声明语句类型

使用 `auto` 通过声明语句声明变量，要求声明时存在赋值。此时，该变量的具体类型由右侧表达式决定

```c++
// 推断 a 类型为 int
auto a = 10;
// 推断 b 类型为 int
auto b = a * 3;
```

- 允许同时声明多个变量，遵循自左向右推导规则，取第一个变量的推导类型

```c++
int n = 5;
// 根据 &n 推断 auto 为 int
auto *pn = &n, m = 10;
// 编译错误：根据 &n 推断 auto 为 int，不符合 m2 类型
auto *pn2 = &n, m2 = 10.0;
```

- 可用于对函数指针的简化写法

```c++
int add(int a, int b) {
    return a + b;
}
// 推断为函数指针 int(*)(int, int)
auto add_func = add;
```

- 可用在 new 关键字之后，但不常用

```c++
// 经过两次推导，第一次推导右侧 auto 类型为 int，第二次推导 i 类型为 int*
auto i = new auto(5);
// 经过两次推导，第一次推导右侧 auto 类型为 int，第二次推导 *j 类型为 int
auto *j = new auto(7);
```

- 可用于元组解构

```c++
int a[2] = {1, 2};
// x, y: int
auto [x, y] = a;
// m, n: &int
auto& [m, n] = a;
```
## 类成员变量

`auto` 关键字可以用于声明被 `const`（c++11）或 `inline`（c++17）修饰的类/结构体静态成员变量

```c++
struct AutoStructure {
    static const auto i = 10;
    static inline auto j = 20;
}
```
## 函数及 lambda 表达式

- `lambda` 形参列表及返回值（c++14），实现类似模板的效果

```c++
auto add = [](auto a, auto b) { return a + b; };
// a 类型推导为 int，b 类型推导为 double，r1 推导为 double
auto r1 = add(3, 5.0);
// a 类型推导为 double，b 类型推导为 int，r2 推导为 double
auto r2 = add(3.0, 5);
// a 类型推导为 char，b 类型推导为 char，r3 推导为 char
auto r3 = add('3', '5');

// 返回值推导为 int
auto l = [](int &i) -> auto& { return i; };
```

- 函数形参列表（c++20）及返回值推断（c++14）

```c++
// auto 具体类型为实参类型
void echo(auto value) {
  // do something
}

// 根据 a+b 推断返回值为 int 类型
auto add(int a, int b) {
    return a + b;
}
```

- 函数返回值后置（c++11），使用 `->` 指定返回值类型

```c++
// 不涉及到类型推断，相当于 int add(int a, int b);
auto add(int a, int b) -> int {
    return a + b;
}
```

`auto` 关键字引发的返回值后置经常与 `decltype` 配合使用，详见[[#函数返回类型后置|函数返回类型后置]]。

当返回值类型比较复杂时（如函数指针），返回值后置还可以使代码更加简洁。

```c++
auto get_op() -> int(*)(int, int) {
    // 返回一个 int(int, int) 的函数指针
}
```
## 非类型模板

可用于非类型模板的形参（c++17）

```c++
// 根据传入的实际参数推断 N 类型
template<auto N>
void f() {
    cout << N << endl;
}

f<10>();  
f<10.0>();  
f<'a'>();
```
## 推导规则

在使用 `auto` 推导时遵循以下规则：

- `cv` 限定符：若推导类型为值类型（非指针、引用），忽略 `const`，`volatile` 修饰符

```c++
const int i = 5;
// auto 推断为 int 而非 const int
auto j = i;
```

- 引用：若推导类型为引用类型，则自动忽略引用。若需要推断为引用则需要使用 `&` 声明

```c++
int i = 5;
int &j = i;
// auto 推断为 int 而非 int&
auto m = j;
// 若需要引用，需要这么写
auto &n = j;
```

但若声明为万能引用，则左值被推导为引用

```c++
int i = 3;
// 由于引用折叠，m 的类型为 int&
auto&& m = i;
// n 的类型为 int&&
auto&& n = 3;
```

- 若被推导类型为函数或数组，推导类型为对应类型的指针

```c++
int add(int a, int b) {
    return a + b;
}

int arr[10];

// a 类型为 int(*)(int, int)
auto a = add;
// b 类型为 int*
auto b = arr;
```

- 初始化列表：若直接使用，必须使用单元素；若通过 `=` 连接，推导为 `std::initializer_list<T>` 类型且元素类型必须相同

```c++
// auto 推断为 std::initializer_list<int>
auto x1 = {1, 2};
// auto 推断为 std::initializer_list<int>
auto x2 = {1};
// 编译错误，元素类型必须相同
auto x3 = {'a', 2.0};
// auto 推断为 int，值为 1
auto x4 {1};
// 编译错误，必须使用单元素
auto x5 {1, 2};
```
# decltype 说明符

C++11 引入 `decltype` 关键字用于在编译期确定某个表达式的具体类型。`decltype` 允许作为普通成员变量类型（`auto` 只能用于静态成员变量）

> [!note]
> 在 `decltype` 之前，可用于编译期获取类型信息的方法：
> - auto 根据目标表达式值推断类型
> - decltype 根据目标表达式每一项类型推断类型
> - typeof 运算符由 gcc 扩展，非 c++ 标准，类似 decltype
> 
> 可用于运行时获取类型信息方法：
> - typeid 运算符将类型信息保存在 `std::type_info` 中，但其 `name()` 输出内容与编译器相关
## 推导规则

- 若被推导表达式是一个未加括号的表达式或类成员 e，推断结果为 e 的类型 T；若 e 为一组重载函数，则无法推断

```c++
int i = 10;
// decltype(i) 推断为 int
decltype(i) i_ret;
```

![[Pasted image 20240108092223.png]]

```c++
struct STRUCT {
    int x;
    double y;
} *j;
// decltype(j) 推导为 STRUCT*
decltype(j) j_ret;
// decltype(*j) 推导为 STRUCT&
decltype(*j) j__ret;
// decltype(j->y) 推导为 double
decltype(j->y) jx_ret;
```

![[Pasted image 20240108092232.png]]

- 若推导表达式为一个函数或仿函数调用，推断类型为函数返回值。注意此时函数并不会被调用

```c++
const int&& foo();
// decltype(foo()) 推断为 int&&
decltype(foo()) foo_result;

int fun(char);
double fun(int);
char fun(double);
// decltype(fun(3.0)) 推断为 char
decltype(fun(3.0)) fun_d_result;
```

![[Pasted image 20240108093953.png]]

- 若被推导值为类型为 T 的左值，则结果为 `T&`；若被推导值为将亡值，则结果为 `T&&`

```c++
// i=3 结果左值，decltype(i=3) 推断结果为 int&
decltype(i=3) lv_result;
// (int&&)i 结果为右值，decltype((int&&)i) 推断结果为 int&&
decltype((int&&)i) rv_result;
```

![[Pasted image 20240108094440.png]]

- 其他类型直接返回被推导值的类型（比较奇怪的是函数）

```c++
void foo();
// foo_type 类型为 void(void)
// typeid(foo_type).name() 为 FvvE
// 这个不知道有什么用
decltype(foo) foo_type;
// p_foo_type 类型为 void(*)(void)
// typeid(p_foo_type).name() 为 PFvvE
// 这个是函数指针
decltype(foo) *p_foo_type;
```

- 推断保留 const，volatile 修饰符
## 函数返回类型后置

当使用函数返回值后置时，若返回值类型不确定（如使用模板等），可以使用 `decltype` 对其进行推断

```c++
// 实际返回值类型为 a + b 的类型
// a b 类型为模板类型 A B，在编译时确定，编写时不确定
// 实际返回值类型将在模板实例化时由编译器推断确定
template<typename A, typename B>
auto add1(A a, B b) -> decltype(a + b) {
    return a + b;
}
```

这么写的原因是在决定返回值类型（`auto` 的位置）不能访问到输入的参数的。当然，严格来说这种情况也可以不用后置返回类型来写，不过很麻烦，比如：

```c++
// 这种写法隐式访问了 A 和 B 的无参构造
// 因此要求两个类型必须有无参构造
template<typename A, typename B>
decltype(A() + B()) add(A a, B b) {
    return a + b;
}
```

或者这么写，由于不实际完成计算，可以利用 nullptr 获取两个类型的值用于推断：

```c++
template<typename A, typename B>
decltype(*static_cast<A*>(nullptr) + *static_cast<B*>(nullptr))
add(A a, B b) {
    return a + b;
}
```

或者利用 `std::declval` 模板方法。该方法位于 `type_traits` 头文件中，可以将给定类型转换为其引用类型（但没有实现，因此用于模板和类型推断），与强转的原理类似

```c++
#include <type_traits>
using std::declval;

template<typename A, typename B>
decltype(declval<A>() + declval<B>())
add(A a, B b) {
    return a + b;
}
```
## decltype(auto)

C++14 引入 `decltype(auto)` 结合使用表示用 `decltype` 的推导规则推导 `auto`。该组合必须单独使用，不能结合指针、引用、const、volatile 使用，其他用法与 `auto` 几乎完全相同只是推导规则不同。

自 C++17 后，`decltype(auto)` 还可以用于非类型模板的类型，详见[[现代cpp-类型推断与类型引用#非类型模板|auto非类型模板]]。

```c++
int i = 3;
int &j = i;
int &&foo();

// a1 a2 均为 int
auto a1 = i;
decltype(auto) a2 = i;
// b1 为 int，b2 为 int&
auto b1 = j;
decltype(auto) b2 = j;
// c1 为 int，c2 为 int&&
auto c1 = foo();
decltype(auto) c2 = foo();
// d1 d2 均为 int
auto d1 {3};
decltype(auto) d2 {3};
// e1 为 initializer_list<int>
// e2 无法编译，因为 {3, 5} 不是表达式
auto e1 = {3, 5};
decltype(auto) e2 = {3, 5};
// *f1 为 int*
// f2 无法编译，因为 decltype(auto) 只能独立使用
auto *f1 = &i;
decltype(auto) *f2 = &i;
```

该组合用法可以代替函数返回类型后置：

```c++
template<typename A, typename B>
decltype(auto) add2(A a, B b) {
    return a + b;
}
// 等效于
template<typename A, typename B>
auto add1(A a, B b) -> decltype(a + b) {
    return a + b;
}
```
# 类型别名

C++ 11 后，可以使用 `using` 声明类型别名，类似 `#define` 声明别名。相比 `#define`，`using` 允许声明函数指针、数组和带模板的类型的别名

```c++
// 普通类型: using 别名 = 类型名
using LL = long long;
// 函数指针: 类型名那里将变量名省略
using op = int(*)(int, int);
// 指针数组
using ps = int*[10];
// 模板类型
template<typename T>  
using vec = std::vector<T>;

int main() {
    // 实例：函数指针
    int add(int a, int b);
    op p_add = add;
    // 实例: 模板类型
    vec<int> v;
    v.push_back(10); 
}
```
# 减少 `typename` 使用

类型引用通常需要使用 `typename` 标志说明该值为一个类型，如：

```c++
template<class T> void foo(typename T::type);
```

C++20 之前，有两种情况可以例外：
- 指定基类：`class A<T>: T::B`
- 成员初始化：`class A: T::B { A(): T::B() {} };`

C++20 之后，以下情况也可以省略，主要是上下文仅可能为类型的情况：

- 类型转换

```c++
// 包括 static_cast, const_cast, reinterpret_cast, dynamic_cast
static_cast<T::B>(p);
```

- 类型别名

```c++
using R = T::B;
```

- 后置返回值

```c++
auto g() -> T::B;
```

- 模板形参默认值

```c++
template<class R = T::B> struct X;
```

- 全局或命名空间中简单声明或函数定义

```c++
template<class T> T::R f();
```

- 结构体成员类型

```c++
template<class T>
struct D {
    T::B b;
}
```

- 成员函数或 `lambda` 表达式形参声明

```c++
struct D {
    T::B f(T::B) { return T::B(); };
}
```