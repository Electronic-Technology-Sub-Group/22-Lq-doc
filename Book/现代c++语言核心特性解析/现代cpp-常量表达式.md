# constexpr

C++11 之前，无法将一个函数的结果作为常量使用 -- 即使函数使用的所有数据都是常量。
- `const`：可能是运行时常量
- `#define`：普通的查找替换，若原本不是常量则宏定义也不是常量

```c++
int get_index0() { return 0; }
int get_index1() { return 1; }

const int index0 = get_index0();
#define index1 get_index1()

int main() {
    int argc;
    switch (argc) {
        // Case value is not a constant expression
        case index0: ;
        // non-constexpr function 'get_index1' cannot be used in a constant expression
        case index1: ;
    }
    return 0;
}
```

C++ 11 新增 `constexpr` 关键字，用于表示对应变量、方法为一个编译时常量，可以在编译时运算出来。该关键字既可以修饰函数也可以修饰变量。

```c++
constexpr int get_index0() { return 0; }
constexpr int get_index1() { return 1; }

int main() {
    int argc;
    switch (argc) {
        case get_index0(): ;
        case get_index1(): ;
    }
    return 0;
}
```

对于变量，类似于 `const`，但要求赋值的表达式每一部分必须都是编译时常量表达式

对于函数，要求如下：
- 有一个返回值，即不能是 `void` 的
- 函数体只有一条 `return` 语句
- 不允许先声明、使用后实现
- 可以有参数，相同参数返回值必须相同
- 除了参数，其他各部分必须是编译时常量表达式
- 若函数属于一个类，则 `constexpr` 修饰的函数无论是否被 `const` 修饰，都是 `const` 的

下面是各种反例：

```c++
#include <limits>

using namespace std;

// invalid return type 'void' of 'constexpr' function 'constexpr void foo()'
// 在 GCC 上，这是个 warning 而非 error
constexpr void foo() {
}

// expression '++ x' is not a constant expression
// 在 GCC 上，这是个 warning 而非 error
constexpr int next(int x) {
    return ++x;
}

int g() {
    return 42;
}

// call to non-'constexpr' function 'int g()'
// err: g() 不是一个常量表达式
constexpr int f() {
    return g();
}

// 'constexpr int max_unsigned_char()' used before its definition
constexpr int max_unsigned_char();
enum {
    // enumerator value for 'max_uchar' is not an integer constant
    max_uchar = max_unsigned_char();
};
constexpr int max_unsigned_char() {
    return UCHAR_MAX;
}

// body of 'constexpr' function 'constexpr int abs2(int)' not a return-statement
// 函数中只能有一个 return 表达式
constexpr int abs2(int x) {
    if (x > 0) {
        return x;
    } else {
        return -x;
    }
}

// body of 'constexpr' function 'constexpr int sum(int)' not a return-statement
constexpr int sum(int x) {
    int result = 0;
    while (x > 0) {
        result += x--;
    }
    return result;
}
```

C++14 放松了对 `constexpr` 的限制：
- 函数体允许声明变量，但有限制
	- 必须初始化
	- 不能是 `static`，`thread_local` 的
- 允许出现 `if`，`switch`，但不能有 `goto`
- 允许出现 `for`，`while`，`do-while` 循环
- 允许修改生命周期与常量表达式相同的对象
- 函数返回值可以为 `void`
- `constexpr` 声明的成员函数不再自动具有 `const` 属性

C++20 后，对虚函数的限制进一步放松
- 允许使用平凡类的默认初始化
- 允许修改联合体类型的有效成员。事实上 GCC 和 MSVC 均可以在 17 版本下编译通过
- 允许出现 `try-catch` 结构，但不能使用 `throw` 抛出异常。事实上 `catch` 块永远不会被执行
## 构造函数

`constexpr` 允许修饰类、结构体构造函数，使某个类或结构体可以在编译时创建。此类构造函数要求为：
- 可以有参数，初始化列表必须是编译时常量表达式（参数视为常量）
- 函数体必须为空
- 所在类虚函数是平凡的（没有自定义析构、虚析构，所有成员对析构都是平凡的）

使用 `constexpr` 修饰的常量只能使用 `constexpr` 构造函数。带有 `constexpr` 构造函数的类称为*字面量类型（literal class type）*。

`constexpr` 构造函数在实参非编译时常量表达式时，退化成普通构造函数。
## lambda 表达式

C++17 后，对于所有满足 `constexpr` 要求的 `lambda` 表达式，都将默认声明为 `constexpr` 的。经验证，以下三个例子中，GCC 全部可以编译，MSVC（VS 2022）编译错误：C2131 表达式的计算结果不是常数

```c++
// 例1 正常编译通过
auto get_size = [](int i) { return i * 2; };
// get_size 函数体满足编译时常量表达式的要求，自动转换为 constexpr 的
char buffer[get_size(10)] = {0};

// 例2
int k = 5;
char buffer2[get_size(k)] = {0};

// 例3
auto get_size2 = [](int i) {
    static int times = 2;
    return i * times;
};
char buffer3[get_size2(10)] = {0};
```
## 内联属性

C++17 后，`constexpr` 修饰的静态成员变量默认也是内联的。

```c++
class X {
public:
    static constexpr int num{5};
};
```
## if constexpr

C++17 的 `if constexpr` 类似 `#if`，可用于编写紧凑的模板代码，根据编译时条件进行实例化。
- `if constexpr` 条件必须是编译期常量
- 编译器仅编译结果为 true 的代码

```c++
void check() {
    if constexpr(sizeof(int) > sizeof(char)) {
        cout << "sizeof(int) > sizeof(char)";
    } else {
        cout << "sizeof(int) <= sizeof(char)";
    }
}
```

经过编译后，若 `int` 的内存空间大于 `char`，`else` 分支将被移除；否则，将仅保留 `else` 分支（事实上一定是 `true`，这是 C++ 标准之一）

```c++
void check() {
    cout << "sizeof(int) > sizeof(char)";
}
```

该功能用于模板时将非常好用。假设一种相等函数模板的定义如下：

```c++
template<class T>
bool is_same_value(T a, T b) {
    return a == b;
}

template<>
bool is_same_value<double>(double a, double b) {
    if (std::abs(a - b) < 0.0001) {
        return true;
    } else {
        return false;
    }
}
```

可以通过 `if constexpr` 可以将模板特化简化

```c++
template<class T>
bool is_same_value(T a, T b) {
    if constexpr (std::is_same_v<T, double>) {
        if (std::abs(a - b) < 0.0001) {
            return true;
        } else {
            return false;
        }
    } else {
        return a == b;
    }
}
```

> [!warning]
> `if constexpr` 的 `else` 很多情况下不能省略，如果省略编译结果可能与预期不符，常见可能产生多个 `return` 点，其返回类型可能不同。

`if constexpr` 不支持短路规则，可能产生错误：

```c++
template<class T>
int any2i(T value) {
    // error: 'npos' is not a member of 'int'
    if constexpr (std::is_same_v<T, std::string> && T::npos == -1) {
        return atoi(value);
    } else {
        return (int) value;
    }
}
```

 上面的例子中，`if constexpr` 首先判断 `T` 类型是否为 `string`。当 `T != string` 时，`T::npos` 不一定存在，但由于没有短路，将产生编译时错误。应改为嵌套 `if`

```c++
template<class T>
int any2i(T value) {
    if constexpr (std::is_same_v<T, std::string>) {
        if (T::npos == -1) {
            return atoi(value);
        }
    } else {
        return (int) value;
    }
}
```
## 虚函数

C++20 后，`constexpr` 支持修饰虚函数。在虚函数被调用时，将会使用常量代替，减少一次函数调用过程。

```c++
struct X {
    constexpr X() {}

    virtual constexpr int f() {
        return 10;
    }
};

struct Y : X {
    constexpr Y() {}

    virtual constexpr int f() {
        return 20;
    }
};

// 10
constexpr int fx = X().f();
// 20
constexpr int fy = Y().f();
```
# consteval

C++20 引入 `consteval` 说明符用于声明立即函数，表示对应函数必须在编译期执行。

`consteval` 与 `constexpr` 的区别在于：
1. `consteval` 只能修饰函数，`constexpr` 还可以修饰变量、
2. `constexpr` 修饰的函数不依赖于编译上下文，在非常量表达式中表现为普通函数；`consteval` 修饰的函数必须在编译期执行，编译期无法完成计算的会产生异常

```c++
constexpr int sqr_expr(int x) {
    return x * x;
}

consteval int sqr_eval(int x) {
    return x * x;
}

// 都没问题
int a1 = sqr_eval(100);
int a2 = sqr_expr(100);

int b = 100;
// error: the value of 'b' is not usable in a constant expression
int b1 = sqr_eval(b);
// 没问题
int b2 = sqr_expr(b);
```
# constinit

C++20 引入 `constinit` 说明符用于确保变量具有静态的存储时间。该说明符的具体作用有：

- 校验声明的变量具有静态存储时间

```c++
constinit int x = 11;

int main() {
    constinit static int y = 42;
    // error: 'constinit' can only be applied to a variable with static or thread storage duration
    constinit int z = 10;
    return 0;
}
```

- 变量具有常量初始化程序

```c++
const char* f() { return "hello"; }
constexpr const char* g() { return "cpp"; }
// error: 'constinit' variable 'str1' does not have a constant initializer
// error: call to non-'constexpr' function 'const char* f()'
constinit const char* str1 = f();
constinit const char* str2 = g();
```

- 用于非初始化声明，表示 `thread_local` 变量已被赋值

```c++
extern thread_local constinit int x;
```
# 常量求值环境

C++20 头文件 `<type_traits>` 新增函数 `std::is_constant_evaluated()`，用于判断当前代码执行环境是在编译期还是运行时，明显的编译期则返回 `true`

```c++
constexpr double power(double b, int x) {
    if (std::is_constant_evaluated() && x >= 0) {
        double r = 1.0, p = b;
        unsigned u = x;
        while (u) {
            if (u & 1) r *= p;
            u /= 2;
            p *= p;
        }
        return r;
    } else {
        return std::pow(b, x);
    }
}
```

在 C++ 文档中，明显常量求值包括：
- 常量表达式
- `if constexpr`
- `constexpr` 变量初始化
- 立即函数及其调用
- 约束表达式
- 可在常量表达式中使用或具有常量初始化的变量初始化程序