# 类型别名

`C` 语言时期，可以使用 `define` 预处理指令实现类型别名，其本质是在编译时对源码的字符串替换。

```c++
// 为 long long 类型提供别名 big_int
#define big_int long long
```

`C++` 早期，可以使用 `typedef` 关键字定义类型别名

```c++
// 为 long long 类型提供别名 big_int
typedef long long big_int;
```

以上两种方法对于定义函数指针和数组时会非常别扭，且无法定义模板类型。

```c++
// 为返回值为 int，接收两个 int 类型参数的函数指针别名为 Op
typedef int (*Op) (int, int);
// 为包含 5 个元素的 int 数组定义别名 Int5
typedef int Int5[5];
```

`C++ 11` 之后，可以使用 `using` 关键字定义类型别名，并提供统一的定义方法 `using 类型别名 = 实际类型`：

```c++
// 为 long long 类型提供别名 big_int
using big_int = long long;
// 为返回值为 int，接收两个 int 类型参数的函数指针别名为 Op
using Op = int (*) (int, int);
// 为包含 5 个元素的 int 数组定义别名 Int5
using Int5 = int[5];
// 为模板类型创建别名
template<typename T>
using TemplateT = T;
```

# 自动推断

`C++`  是一种强类型语言，编译器在编译时所有数据的类型就已经明确了。

## auto

代码中我们可以通过 `auto` 让编译器自动推断一个变量的类型，称为类型推断。其形式为 `auto x = expr`。

```c++
auto a = 10;
auto b = a * 3;
```

`auto` 可用于对函数指针的简化写法

```c++
#include<iostream>

using namespace std;

int add(int a, int b) {
    return a + b;
}

int main() {  
    auto add_func = add;
    cout << add_func(5, 7) << endl;
    cout << typeid(add_func(5, 7)).name() << endl;
    cout << typeid(add_func).name() << endl;
}
```

![[auto_type.png]]

```ad-warning
可以在使用 `auto` 的情况下使用初始化列表，但不要在使用初始化列表的同时使用 `=` 运算符赋值，当直接使用 `=` 将初始化列表赋值给一个 `auto` 变量时，实际上是将初始化列表对象赋值给了它。
```

```c++
#include<iostream>

using namespace std;

int add(int a, int b) {
    return a + b;
}

int main() {  
    int a {5};  
    auto b {5};  
    int c = {5};  
    auto d = {5};  
    cout << "Type of a is " << typeid(a).name() << endl;  
    cout << "Type of b is " << typeid(b).name() << endl;  
    cout << "Type of c is " << typeid(c).name() << endl;  
    cout << "Type of d is " << typeid(d).name() << endl;  
}
```

![[auto_with_initializer.png]]

`auto` 只能推断类型（包括指针），但不能推断 `const` 修饰的类型和引用类型。可通过 `auto` 向类型添加 `const` 和 `&`

```c++
int[100] arr;

for (auto v1: arr) {
    // v1：int 类型
}

for (auto &v2: arr) {
    // v2：int& 类型
}

for (const auto &v3: arr) {
    // v3：const int& 类型
}
```

`auto` 关键字还可以用于元组解构。使用 `auto` 可为解构的元组变量统一增加 `const`，`&` 标记等

```c++
int a[2] = {1, 2};

// x, y: int
auto [x, y] = a;
// m, n: &int
auto& [m, n] = a;
```

当 `auto` 关键字用于函数或 `lambda` 表达式时，表示该函数根据 `return` 语句推断返回值类型。这同样适用于函数指针

```c++
auto add(int a, int b) {
    // 类型推断：int + int => int
    return a + b;
}

// 函数指针名：p
// 返回值类型：int
auto (*p)() -> int;
// 函数指针名：q
// 返回值类型：根据 p 推导
auto (*q)() -> auto = p;
```

## decltype

使用 `decltype` 关键字可在编译期获取一个表达式的类型

```c++
int someInt;
decltype(someInt) otherInt = 10;

该关键字也可用于其他表达式

vector<A> vec1;
vector<B> vec2;
decltype(vec1[0] + vec2[0]) v;
```

上面例子 v 的类型为 `A` 与 `B` 对象相加的类型，如果存在的话（自定义运算符）。

`decltype` 和 `auto` 推断的类型可能不同，主要差异在 `const` 和 `&`

![[Pasted image 20230104215444.png]]

## 尾置返回值

对于函数，`auto` 用于尾置返回值类型的占位

```c++
auto func(int a, int b) -> int {  
    return a + b;  
}
```

尾置返回值常用于模板中推断返回值类型，将在模板中详细讨论

# 类型信息

# 类型信息

## typeid

可以通过 `typeid` 查询一个类型的具体信息，包括使用 `auto` 推断的类型，该关键字返回一个 `type_info` 对象，该类位于 `typeinfo` 头文件中。

```c++
#include<typeinfo>

using namespace std;

int main() {
    auto a {10};

    const type_info &info = typeid(a + 10);
    const char *typeName = info.name(); // i
}
```

`type_info` 类重写了 `==` 运算符，并包含了对应类型的类型名等信息。

## sizeof

不同类型占用的内存空间不同，可使用 `sizeof` 关键字获取其占用内存大小（字节）。

该关键字用于获取类型和数组占用内存的大小（单位字节）。该类型返回一个 `size_t` 类型的数字，该类型是一个 `unsigned` 的整型，根据平台不同，实际类型也不同。

该关键字可用于变量，表达式和具体类型，但使用方式不同：

```c++
int main() {
    int i {5};

    size_t size1 = sizeof i; // 4
    size_t size2 = sizeof(int); // 4
}
```

# 类型转换

不同类型之间可以进行类型转换，但要注意类型转换可能**丢失精度**。

`C++` 中类型转换分为两种：隐式转换和显式转换。由编译器自发进行的转换称为隐式转换，一般来说都是比较安全的转换方式；而用户也可以强制将一种类型转换成另一种类型，但可能存在潜在风险。

## 显式转换

显式转换是通过强制转换运算符实现的类型转换，依赖于类型检查系统，更加安全。

- `C++` 风格的强制转换。以下几个都是运算符。
	- `static_cast<T>(value)`：将 `value` 强制转换为 `T` 类型，在编译时检查其类型是否可以转换
	- `dynamic_cast<T>(value)`：将 `value` 强制转换为 `T` 类型，在运行时检查其类型是否可以转换
	- `const_cast<T>(value)`：将 `value` 强制转换为 `T` 类型，用于去除 `const` 修饰符
	- `reinterpret_cast<T>(value)`：将 `value` 强制转换为 `T` 类型，在任何条件下都发生转换
	- `T(value)`：等效于 `reinterpret_cast<T>(value)`
- `C` 风格的强制转换
	- `(T) value`：等效于 `reinterpret_cast<T>(value)`