> [!note] auto
> `auto` 关键字并不是 c++11 新加入的关键字。从 c 语言时期开始就存在 `auto` 关键字，用于声明变量具有自动的生命周期（即在一个 `{}` 之间有效，与之相对的是 `static`）。但由于默认情况下声明的变量就是自动生命周期，该关键字总是被省略。

自 C++ 11 开始，代码中我们可以通过 `auto` 让编译器自动推断一个变量的类型，称为类型推断。其形式为 `auto x = expr`。
# 声明语句类型

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
# 类成员变量

`auto` 关键字可以用于声明被 `const`（c++11）或 `inline`（c++17）修饰的类/结构体静态成员变量

```c++
struct AutoStructure {
    static const auto i = 10;
    static inline auto j = 20;
}
```
# 函数及 lambda 表达式

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
```

- 函数返回值后置（c++11），使用 `->` 指定返回值类型

```c++
// 不涉及到类型推断，相当于 int add(int a, int b);
auto add(int a, int b) -> int {
    return a + b;
}
```

`auto` 关键字引发的返回值后置经常与 `decltype` 配合使用，详见[[现代cpp-decltype 说明符#函数返回类型后置|函数返回类型后置]]。

当返回值类型比较复杂时（如函数指针），返回值后置还可以使代码更加简洁。

```c++
auto get_op() -> int(*)(int, int) {
    // 返回一个 int(int, int) 的函数指针
}
```

- 函数返回值推导（c++14）

```c++
// 根据 a+b 推断返回值为 int 类型
auto add(int a, int b) {
    return a + b;
}
```
# 非类型模板

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
# 推导规则

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