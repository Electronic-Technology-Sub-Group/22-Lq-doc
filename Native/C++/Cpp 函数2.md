# 变长参数

有些函数可以接受不定长度的参数，称为变长参数。

函数参数列表可以使用`...`结尾，表示接受变长参数，但有一些限制
-   函数至少有一个普通形参
-   可选参数必须在所有参数末尾且只能有一个
-   不能指定可选参数的类型

不定参数通过 `cstdarg` 头文件的 `va_start`, `va_arg`, `va_end` 访问
1.  声明`va_list`实例
2.  使用`va_start`初始化
3.  使用`va_arg`提取值。因此我们需要知道每个不定参数的类型
4.  使用`va_end`释放参数列表

```c++
#include<iostream>
#include<cstdarg>

using namespace std;

void fun(const char *name, int arg_count, ...) {
    // fun: a(3)
    cout << "fun: " << name << "(" << arg_count << ")" << endl;

    va_list arg_ptr;
    va_start(arg_ptr, arg_count);
    //  ...[0]: 123
    cout << " ...[" << 0 << "]: " << va_arg(arg_ptr, int) << endl;
    //  ...[1]: hello
    cout << " ...[" << 1 << "]: " << va_arg(arg_ptr, const char*) << endl;
    //  ...[2]: 3.14
    cout << " ...[" << 2 << "]: " << va_arg(arg_ptr, double) << endl;
    va_end(arg_ptr);
}

int main() {
    fun("a", 3, 123, "hello", 3.14);
}
```

# 函数指针

一个函数也可以赋值给一个函数指针，以便作为参数传递给其它函数，或作为函数的别名。

函数指针是一种特殊的指针，它指向一个函数，可以将函数视作一个变量存储和传递。

函数指针类型只包含函数的返回类型和形参列表的形参类型，只要返回类型和形参类型相同的函数都能为其赋值。其格式表示为：`返回类型 (*变量名) (形参类型列表)`

```c++
int add(int a, int b) {
    return a + b;
}

int main() {
    int (*op) (int, int) = add;
    int value = op(3, 5); // 8
}
```

函数指针类型比较复杂，可使用 `auto` 代替

```c++
int main() {
    auto operation = add;
    // 3 + 5 = 8
    cout << "3 + 5 = " << operation(3, 5) << endl;
    operation = mul;
    // 3 * 5 = 15
    cout << "3 * 5 = " << operation(3, 5) << endl;
}
```

函数指针作为一种指针类型，可以作为参数传递，也可以作为返回值返回

```c++
#include<iostream>

using namespace std;

int add(int a, int b) {
    return a + b;
}

int mul(int a, int b) {
    return a * b;
}

int calc(int a, int b, int (*operation)(int, int)) {
    return operation(a, b);
}

int main() {
    // 3 + 5 = 8
    cout << "3 + 5 = " << calc(3, 5, add) << endl;
    // 3 * 5 = 15
    cout << "3 * 5 = " << calc(3, 5, mul) << endl;
}
```

# 引用与地址类型

## 参数

### 按值传递

```c++
#include<iostream>

using namespace std;

static int ID {0};

class A {
public:

    A() { cout << "create A" << ", id=" << id << endl; }
    A(const A &a) { cout << "create A by A&" << ", id=" << id << endl; }
    ~A() { cout << "remove A" << ", id=" << id << endl; }

    int value {};
    int id {ID++};
};

void set_value(A a, int value) {
    a.value = value;
    cout << "Value in set_value is " << a.value << ", id=" << a.id << endl;
}

int main() {
    // create A, id=0
    A a;
    // Value before set_value is 0, id=0
    cout << "Value before set_value is " << a.value << ", id=" << a.id << endl;
    // create A by A&, id=1
    // Value in set_value is 20, id=1
    // remove A, id=1
    set_value(a, 20);
    // Value after set_value is 0, id=0
    cout << "Value after set_value is " << a.value << ", id=" << a.id << endl;
    // remove A, id=0
}
```

直接传递对象本值，传递的是对象的副本（使用复制构造），并在函数运行完成后释放

### 指针

指针传参仍然遵循按值传递的规则，但复制的是地址而非对象，因此对象本身并未被复制

```c++
#include<iostream>

using namespace std;

static int ID {0};

class A {
public:

    A() { cout << "create A" << ", id=" << id << endl; }
    A(const A &a) { cout << "create A by A&" << ", id=" << id << endl; }
    ~A() { cout << "remove A" << ", id=" << id << endl; }

    int value {};
    int id {ID++};
};

void set_value(A *a, int value) {
    a->value = value;
    cout << "Value in set_value is " << a->value << ", id=" << a->id << endl;
    a++;
}

int main() {
    // create A, id=0
    A a;
    // Value before set_value is 0, id=0
    cout << "Value before set_value is " << a.value << ", id=" << a.id << endl;
    // Value in set_value is 20, id=0
    set_value(&a, 20);
    // Value after set_value is 20, id=0
    cout << "Value after set_value is " << a.value << ", id=" << a.id << endl;
    // remove A, id=0
}
```

### 引用

引用传递不进行任何复制，传参时也不需任何其他取址等运算，接受 `A&` 类型参数

```c++
#include<iostream>

using namespace std;

static int ID {0};

class A {
public:

    A() { cout << "create A" << ", id=" << id << endl; }
    A(const A &a) { cout << "create A by A&" << ", id=" << id << endl; }
    ~A() { cout << "remove A" << ", id=" << id << endl; }

    int value {};
    int id {ID++};
};

void set_value(A &a, int value) {
    a.value = value;
    cout << "Value in set_value is " << a.value << ", id=" << a.id << endl;
}

int main() {
    // create A, id=0
    A a;
    // Value before set_value is 0, id=0
    cout << "Value before set_value is " << a.value << ", id=" << a.id << endl;
    // Value in set_value is 20, id=0
    set_value(a, 20);
    // Value after set_value is 20, id=0
    cout << "Value after set_value is " << a.value << ", id=" << a.id << endl;
    // remove A, id=0
}
```

### 右值

使用 `T&&` 可接受 `rvalue` 参数

```c++
void fun(int &value) {
    cout << "call fun(int&)" << endl;
}

void fun(int &&value) {
    cout << "call fun(int&&)" << endl;
}

int main() {
    int a = 0;
    // call fun(int&)
    fun(a);
    // call fun(int&&)
    fun(3);
    // call fun(int&&)
    fun(a + 5);
}
```

### 数组参数

若数组长度恒定，可以直接标明数组类型

```c++
void func(int arr[10]);
```

但如果数组长度不定，数组的传递就不是按值传递，而是第一个元素的指针，需要额外传递数组长度。

```c++
void print(int arr[]) {
    cout << "arr type is " << typeid(arr).name() << endl;
    cout << "arr size is " << sizeof arr << endl;
}

int main() {
    int arr[] = {0, 1, 2, 3, 4};
    // arr type is A5_i
    cout << "arr type is " << typeid(arr).name() << endl;
    // arr size is 20
    cout << "arr size is " << sizeof arr << endl;
    cout << "==========================" << endl;
    // arr type is Pi
    // arr size is 8
    print(arr);
}
```

## 返回值

### 返回值类型

值类型会回传对应值的副本。一般来说编译器会使用 `move` 语义优化，实际效率非常高。

```c++
int sum(int a, int b) {
    // 这一句进行的操作：
    //   创建一个临时变量 t = a + b（可以在内存或寄存器中）
    //   将 t 的值复制一份，返回给调用位置
    return a + b;
}
```

### 返回引用

若返回类型是引用类型，值不会进行复制，否则返回的是数据的副本。

一般不会返回一个引用类型，直接返回在一个函数中创建的自动变量的引用是非常危险的

```ad-warning
**永远不要**返回一个在函数栈中创建的对象的指针或引用，也不要将其以任何方法传递到函数之外
```

主要原因是，函数中的自动变量位于函数自己的栈中，当函数返回时，其栈空间内存会自动释放（根据编译器不同，可能表示为清空或标记为无效）。释放过的内存可以被重新申请，其存储的值是不可预测的。

# 仿函数

仿函数指实际并非函数，但行为与函数相同的类型，主要包括以下几种：
- 使用 `#define` 预处理指令定义的宏
- 重载了 `()` 运算符的类、结构体等，详见 [[Cpp 类与对象3]]

## 宏

通过 `#define` 定义的仿函数，即宏，使用 `()` 表示一个变量

```c++
#define ADD(x, y) ((x) + (y))

int main() {
    int result = ADD(5, 12);
}
```

其预编译结果为

```c++
# 1 "main.cpp"
# 1 "<built-in>"
# 1 "<command-line>"
# 1 "main.cpp"

int main() {
    int result = ((5) + (12));
}
```

# 编译期函数

一般来说函数是在运行时才会被调用，但有一种编译期函数可以在编译时便由编译器计算出结果。

*该部分可以先跳过，等用的时候再说，涉及到了模板和类的部分知识*

编译期函数是指使用 `constexpr` 修饰的普通函数或类构造函数，可以在编译时便计算出值，有更快的速度和更低的内存占用。但这类函数不能有复杂的运算，其限制如下：
- 返回值必须为文本类型
	- 可以是 `void`
	- 特殊类和结构体，基本数据类型及其指针或数组，其中特殊类应符合以下要求：
		 - 有一个普通的析构函数
		 - 至少有一个 `constexpr` 修饰的构造函数
		 - 没有移动构造和复制构造
		 - 所有非静态成员都是不可变类型，且其类型也应当是文本类型
- 可调用其他编译期函数，且允许递归调用
- 不能包含任何 `goto` 或 `try`
- 不能是虚函数，但允许 `=default` 或 `=delete`
- 可以是 `constexpr` 修饰的模板，或模板的某个模板特化被 `constexpr` 修饰

```ad-quote
`constexpr` 也可以用于修饰变量，修饰变量时等同于 `const`，即声明为常量
```

使用 `constexpr` 修饰构造函数时，对修饰的构造和类有一定的要求：
- 类的析构函数必须是 `=default` 的
- 使用 `constexpr` 修饰的构造必须在编译单元（文件）被使用前定义，且函数体必须为空
- 建议：将复制构造和符合常量表达式定义的运算符重载都声明为 `constexpr`

# λ 表达式

`λ` 表达式：又称匿名函数，一种没有名称，不需要要显式类定义的函数对象

```c++
[[capture]] ([parameters]) [mutable] [exception] -> [return_type] {function}
```

- `[capture]`：捕获值。用于向`λ`表达式传入局部变量，使用`,`分割。设作用域中存在变量`a`，`b`，`c`：
	- 值捕获：直接传入值的副本：设仅要以值捕获变量a：`[a]`
	- 引用捕获：使用`&`：设要以值捕获a，以引用捕获b：`[a, &b]`
	- 隐式捕获：让编译器根据函数体推断需要捕获哪些变量，并根据`=`或`&`标记进行捕获：
		- 使用值捕获a，剩下使用引用捕获隐式捕获：`[a, &]`
		- 全部使用值捕获隐式捕获：`[=]`，但不会捕获 `this`
	- 捕获`this`：`[this]`
- `[parameters]`：参数列表
	- 不允许默认参数
	- 不允许可变参数
	- 参数必须有参数名
- `[mutable]`：若存在 `mutable` 标记，表示捕获的变量可变
- `[exception]`：异常声明，可为空：`throw()`
- `[return_type]`：返回值类型，可连带前面的箭头一起省略。省略后，若函数体无 `return` 则为 `void`，否则为其返回值类型
- `{function}`：函数体

可将 `λ` 表达式传入一个接受函数类型的函数，如仿函数或函数指针

> 闭包：closure，一个包括了与其相同作用域的引用的 `lambda` 表达式
