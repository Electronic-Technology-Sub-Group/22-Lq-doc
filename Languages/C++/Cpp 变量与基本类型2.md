# 指针

指针，按我理解应该算一种特殊的值类型，指向某个变量的内存地址。指针实际是一串表示内存地址的数字。指针可以做到间接地操作其它内存，于是有了更多的特性。但仍有一些数据类型特征：
- 通过 `sizeof` 可以验证，所有的指针占用的内存大小相同，与其指向对象大小无关
- 函数传递指针后，针对指针的操作不会影响到原始指针，说明新指针是原始指针的一个副本

## 内存与指针

指针：存储内存地址的变量，指针变量常以 `p`为前缀
-   处理数组数据：数组名指代第一个元素的地址
-   访问函数外的大块数据：使用指针传参，避免复制
-   动态分配内存：使用 `new` 和 `delete` 在堆上动态分配内存，节省空间

内存地址通过对变量使用取址运算符 `&` 获取，使用 `*` 声明

```c++
int value {10};
int *p = &value;
```

一般来说，`*` 符号靠近变量名，表示该变量是一个指针。

```c++
// p 是一个 int 类型指针，未初始化
// a 和 b 是一个整形
// pa 是一个 int 类型指针，指向 a
int *p, a {10}, *pa {&a}, b {20};
```

指针地址可以直接输出，也可以转化成 `intptr_t` 类型将其转化成数字。

未初始化的指针指向不可预测的内存区域，此时该指针代表的地址没有意义，使用起来也很危险。因此，在声明指针后应当使用具体地址初始化。

```ad-warning
若没有具体指向地址，或对象已删除时，应当使用空指针对其初始化。C 时期使用 `0` 或 `NULL`，而 C++ 则提供 `nullptr`，一个指向 `0`的指针，实际类型为 `std::nullptr_t`，类似 `void*`，且可隐式转化为任意类型的指针。
```

指针可以用作 `if` 判断，任何非空指针可隐式转化为 `true`

##  const

**判断方法：靠近谁，`const` 便修饰谁**

-   指向常量对象的指针：对象本身不可修改，指针地址可以修改

```c++
int value {5};
const int *pvalue { &value }; // const 修饰 int 类型
*pvalue = 6; // 错误： pvalue 指向 const int 类型
pvalue = nullptr; // 正确
```

-   指向对象的常量指针：对象本身可以修改，指针地址不可修改

```c++
int value {5};
int *const pvalue { &value }; // const 修饰 pvalue 变量本身
*pvalue = 6; // 正确
pvalue = nullptr; // 错误
```

-   指向常量的常量指针：对象本身和指针地址都不可修改

```c++
int value {5};
const int *const pvalue { &value };
*pvalue = 6; // 错误
pvalue = nullptr; // 错误
```

## 指针运算

指针本身允许进行 `+`/`-` 等算术运算：
- 指针 `+/-` 数字：将指针地址加/减 n 个对应类型的长度
- 指针 `-` 指针：将指针地址相减，并将结果除以类型长度

```c++
int value = 10;
int *pvalue = { &value };
int *pnext = pvalue + 1; // 地址为 pvalue + 1 * sizeof(int)
```

## 动态内存分配

### new

用于在堆上申请内存的关键字和运算符，并返回该内存块的首地址（指针）。可以申请一个任意类型的值或一个数组，返回对应类型的指针。

> 堆：又称空闲存储器，计算机中分配给程序的未使用的内存

```c++
int *a = new int;

int **b = new int[10];

int count = 20;
int **c = new int[count];
```

如果内存空间不足以容纳申请的值，则会返回 `nullptr`。但不需要刻意去检查，如果内存不足程序会直接抛出异常。

```ad-warning
使用 `new` 创建的指针需要使用 `delete` 释放，以免造成内存溢出
```

### delete

用于在堆上释放内存的关键字和运算符，可以释放使用 `new` 申请的堆内存，但不会修改原指针的值。

```c++
int *a = new int;

int **b = new int[10];

int count = 20;
int **c = new int[count];

delete a;
delete [] b, c;
```

可以直接释放 `nullptr`，这时不会进行任何操作。但若被释放的指针不是 `nullptr` 且对应的内存不是由 `new` 申请的，或已经被释放了，则会抛出异常。

```ad-warning
1. 使用 `delete` 释放后的内存，其值是不可预测的，应当立即将指针赋值为 `nullptr` 以免出问题。
2. 尽量遵循 **谁申请谁销毁** 或提供 **足够明确** 的提示
```

```c++
int *a = new int;
delete a;
a = nullptr;
```

```ad-note
- 悬垂指针：指针经 `delete`释放后，原地址理论上来说就无效了，再通过该地址访问数据获取的是不可预测的数据。这种指针称为悬垂指针
- 内存泄漏：手动`new`申请的内存没有`delete`，当该地址丢失后，指向的内存永远无法被C++释放，这种情况称为内存泄漏
```

### 使用

一般值，类，结构体等，直接通过 `new` 和 `delete`即可

```c++
// 申请内存
double *pvalue {};
pvalue = new double;
*pvalue = 999.0;
// 以上申请和赋值可以简化成这一句
double *pvalue2 { new double { 999.0 } };
// 释放内存
delete pvalue;
delete pvalue2;
// 防止悬垂指针
pvalue = nullptr;
pvalue2 = nullptr;
```

数组变量使用 `[]`
-   可在运行时决定数组长度
-   可创建每个维度长度不同的多维数组

```c++
int *pdata { new int[5] { 1, 3, 5, 7, 9 } };
delete [] pdata;
pdata = nullptr;
```

# 数组

数组在 C++ 中表示一段连续的内存空间，存有**固定个数**的**同类型数据**，并通过索引访问和修改。

C++ 数组使用 `[]` 声明：`type array_name[length];`

-   `type`：数组内成员类型
-   `length`：数组成员个数，C++20 前必须是常量，即编译期编译器可知。新版本 C++ 支持使用变量。

```c++
long height[6];
```

使用索引访问，索引从 0 开始，到 `length - 1`

```c++
height[3] = 5;
long h = height[3];
```

使用 `{}` 初始化数组，允许使用多维数组

```c++
int arr1[3] {1, 2, 3}; // 1 2 3
int arr2[5] {}; // 0 0 0 0 0
int arr3[] {2, 3, 4}; // 2 3 4
int arr4[8] {1, 2, 3}; // 1 2 3 0 0 0 0 0
int arr[2][3][5] {
    {
        {0, 0, 0, 0, 0},
        {1, 1, 1, 1, 1},
        {2, 2, 2, 2, 2}
    },
    {
        {3, 3, 3, 3, 3},
        {4, 5, 4, 5, 4},
        {5, 4, 5, 4, 5}
    }
}
```

数组大小可通过 `sizeof` 运算符获取

```c++
// 占用内存空间
int arr[] {1, 3, 5, 7, 9};

size_t total_size = sizeof arr; // 20，5 个 int 占用的空间
size_t element_size = sizeof arr[0]; // 4，1 个 int 占用的空间
size_t length = total_size / element_size; // 5
```

```ad-warning
1. Microsoft 提供 `_countof` 方法可计算数组长度，但这不是标准 C++ 包含的
2. 当数组变成指针时，`sizeof` 不再表示数组大小，而是指针大小
```

## 数组与指针

对于数组，数组名可以直接当作数组第一个元素的地址使用

```c++
int array[] {1, 2, 3, 4, 5};
int *parr0 = array;
```

```ad-warning

注意：数组名和第一个元素的地址还是有点区别的，数组名对应的类型是数组，包含了数组长度，而指针类型是指针，不包含数组长度，不能用 `sizeof`等计算数组长度


```c++
int arr[] {0, 1, 2, 3};
int *parr = arr;

cout << typeid(arr).name() << endl; // A4_i
cout << typeid(parr).name() << endl; // Pi
```

根据指针运算规则，可以通过指针访问数组元素

```c++
int main() {
    int arr[]{0, 1, 2, 3, 4};
    int *parr = arr;

    // parr     = 0x413b7ffc50 = 0
    cout << "parr     = " << parr << " = " << *arr << endl;
    // parr + 1 = 0x413b7ffc54 = 1
    cout << "parr + 1 = " << (parr+1) << " = " << *(arr+1) << endl;
    // parr + 2 = 0x7d831ff7c8 = 2
    cout << "parr + 2 = " << (parr+2) << " = " << *(arr+1) << endl;
}
```

指针每次运算的偏移量是根据他的类型决定的，因此可以通过更改指针类型更改便宜量。

`void*`类型可以转换为任何指针类型

```c++
int main() {
    int arr[]{0, 1, 2, 3, 4};
    int *parr = arr;

    // parr     = 0x81b0fffa00 = 0
    cout << "parr     = " << parr << " = " << *arr << endl;
    // parr + 1 = 0x81b0fffa04 = 1
    cout << "parr + 1 = " << (parr+1) << " = " << *(arr+1) << endl;
    // parr + 2 = 0x81b0fffa08 = 2
    cout << "parr + 2 = " << (parr+2) << " = " << *(arr+2) << endl;

    void *parr2 = parr;
    long long *parr2l = static_cast<long long *>(parr2);

    // parr + 1L= 0x81b0fffa08 = 2
    // 转换成 long long* 类型，每次运算偏移量为 sizeof(long long) = 8
    // 因此 parr2l+1 == parr+2
    // 在输出时候，重新强转回 int*，否则结果会出错
    cout << "parr + 1L= " << (parr2l+1) << " = " << *(int*)(void*)(parr2l+1) << endl;
}
```

于是，有以下特性
-   `data[0]` 等效于 `*data`，地址等效 `&data[0]`
-   `data[i]` 等效于 `*(data + i)`，地址等效 `&data[i]`

## 多维数组

在申请时，最外层数组长度必须是明确的。而释放内存时，无论多少维，均使用 `delete []`

```c++
double (*pvalues)[5] { new double[3][5] };
delete [] pvalues;
pvalues = nullptr;
```

# C 风格字符串

C 语言中，使用字符指针 `char*` 表示字符串。C++ 11 之后，仅允许使用 `const char*` 接受字符串。

C++03 字符串分为 `char` 和 `wchar_t`，其中 `wchar_t` 为了对 `Unicode` 编码的兼容。

```c++
char str[6] {"hello"};
const char* str2 = "every one";
wchar_t str3[] {L"Unicode chars"};
```

**注意：字符串以 `\0` 结尾，因此字符串数组长度应比字符串长度多 1**

可使用 `getline` 从控制台获取字符串

```c++
char name[80];
// 获取最多 80 个字符，遇到 \n 停止
cin.getline(name, 80, '\n');
```

C/C++ 内置了大量处理字符串的函数，位于 `cstring` 头文件中（C 中的 `string.h`）

## 字面量

使用 `""` 表示字符串字面量：`"this is a string"`

- 宽字符串：用于 Unicode 字符串（UTF-8/UTF-16 等），使用 `L` 前缀

```c++
wchar_t s[] { L"Hello, I'm 鹿钦." };
```

-   C++ 支持多行字符串

```c++
char s[] {
    "This is a very long string that "
    "has been spread over two lines."
};
```

- C++11 又增加了 `char16_t` 用于支持 `UTF-16` 编码和 `char32_t` 用于支持 `UTF-32` 编码字符。
	- `u8"I'm a UTF-8 string. Char \u2018"`：`const char*` 类型
	- `u"I'm a UTF-16 string. Char \u2018"`：`const char16_t*` 类型
	- `U"I'm a UTF-32 string. Char\U00002018"`：`const char32_t*` 类型

- C++11 增加了对原始字符串的支持，在这类字符串中不会处理转义字符

```c++
R"delimiter(The String Data \ Stuff " )delimiter";
```

字符串内容为 `delimiter(` 与 `)delimiter` 之间的内容，`delimiter` 为最多 16 个字符（可以没有），但不可包含空格，`(`，`)`，`\`，控制字符，可搭配 `u8`，`u`，`U` 等结合

```c++
u8R"XXX(I'm a "raw UTF-8" string.)XXX";
uR"*(This is a "raw UTF-16" string.)*";
UR"(This is a "raw UTF-32" string.)";
```

# 枚举

C++ 中，使用 `enum`定义枚举

## C 风格枚举

默认情况下，枚举类型为 `int`，从 0 开始递增。

```c++
enum Weekday {
    Mon, Tues, Wed, Thurs, Fri, Set, Sun
}
```

与 C 不同的是，C 风格枚举在 C++ 中不是 `int` 类型的，但使用时可隐式转换为对应实际类型

```c++
int main() {
    // Mon=0, Tues=1
    cout << "Mon=" << Mon << ", Tues=" << Tues << endl;
    // 7Weekday
    cout << typeid(Tues).name() << endl;
}
```

枚举可以自定义其内部数据类型和值，只要是整形即可（可以进行 `+1`操作）

```c++
// 内部数据以 unsigned long 类型存储
enum Weekday: unsigned long {
    Mon, Tues = 3, Wed, Thurs = 2, Fri, Set, Sun = 7
};

int main() {
    // 0, 3, 4, 2, 3, 4, 7
    cout << Mon << ", " << Tues << ", " << Wed << ", " << Thurs << ", " 
         << Fri << ", " << Set << ", " << Sun << endl;
    // 1
    cout << (Tues == Fri) << endl;
}
```

-   在没有自定义值的情况下，枚举值为上一个枚举值 +1
-   枚举值可以相同，`==` 运算符实际运算的是比较其中保存的值

## C++ 风格枚举

C 风格枚举虽然能用，但使用中也有很多问题

-   `==`运算符判断的是内部存储的实际的值，不包含枚举本身的类型
-   所有值是直接暴露在命名空间中，同一个命名空间内不同枚举不能有相同的值

```c++
enum Weekday: unsigned long {
    Mon, Tues = 3, Wed, Thurs = 2, Fri, Set, Sun = 7
};

enum Weekend {
    Set, Sun
};

int main() {
    cout << Set << endl;
}
```

像这样是编译不过的，提示 `'Set' conflicts with a previous declaration`

C++ 引入了类的概念，故其枚举也是一个类，这样使得允许在相同命名空间中，不同枚举有相同名的值。

```c++
enum class Weekday {
    Mon, Tues = 3, Wed, Thurs = 2, Fri, Set, Sun = 7
};

enum class Weekend {
    Set, Sun
};

int main() {
    cout << typeid(Weekday::Set).name() << endl; // 7Weekday
    cout << typeid(Weekend::Set).name() << endl; // 2Weekend
}
```

# 结构体

结构体一种自定义的数据结构，继承自 C 并对其进行了一些扩充，使用 `struct` 声明，使用 `{}` 初始化，使用 `.` 访问和修改元素，结构体指针使用 `->` 访问和修改元素

```c++
struct Rectange {
    int left;
    int top;
    int width;
    int height;
};

Rectange r { 5, 7, 20, 40 };
r.left = 10;
(&r) -> top = 15;
```

由于结构体内数据紧密排布，且与 C 排布方式相同，可直接强制转换相同元素，不同类型的结构体指针，也可以使用结构体兼容调用 C 库，且只要内存排布相同，结构体之间就能安全强转。

```c++
struct RECT {
    int left_value;
    int top_value;
    int width_size;
    int height_size;
};

int main() {
    Rectange r { 10, 20, 30, 40 };
    Rectange *pr { &r };
    RECT *prect { (RECT*) (void*) pr };
    // left=10
    cout << "left=" << prect->left_value;
}
```

C++ 对结构体的扩展使结构体与类几乎相同。结构体内也可以有函数，构造函数和析构函数，私有成员，甚至还能有继承。与类唯一的区别是，默认成员权限是 `public` 的。具体概念详见[[Cpp 类与对象1]]

# 联合体

联合体使用一组内存空间表示两种类型，占用两种类型中内存占用最大的类型相同的空间，使用时二选一

C++ 基本不再使用联合体，大多数情况下 `union` 只用于与 C 的兼容

# 前向声明

有时，我们不得不在结构体、类、枚举等类型声明完成之前使用该类型：
- 链表中，我们需要在节点内部保存一个当前节点类型的指针指向下一个节点
- 项目中，引用其他文件中的类型，而另一个文件中已经引入了当前文件的头文件，如果引入那个文件的头文件的话可能造成循环引用

此时，我们可以使用前向声明，仅声明类型名而不定义类型的成员。但由于前向声明没有成员，只能使用类型本身，即指针、引用等，但不能访问其成员。

```c++
// 前向声明
struct Node;

struct Node {
	// 此处在 Node 结构体未完全声明的情况下仍可以有限的使用 Node
    Node *next;
}
```

# 引用

引用可以看成其他对象的别名。虽然很像指针，但不是指针。引用常用于方法传参，可防止对象复制

使用 `&` 声明，该 `&` 不是指针的取址符，而是表示引用类型

## lvalue 引用

左值引用一个变量的持久性位置

```c++
long number {10};
long &rnumber {number};
rnumber += 10L;
// ref=20, val=20 -- 引用类型值变化，原数据值也会变
cout << "ref=" << rnumber << ", val=" << number << endl;
// ref type=l -- 引用类型与原数据类型相同
cout << "ref type=" << typeid(rnumber).name() << endl;
```

对于常量，则应当使用常引用

```c++
const int &rvalue {5};
```

**lvalue 完全等价于被引用对象**

对于可能导致数据复制的情况，应尽量使用引用，以减少对象的复制，如增强 for 循环，函数传参等

```c++
double values[] {1, 2, 3, 45, 6};
// 若为 auto value，即 double value，会进行一次数据复制
for (auto &value: values) {
    // do something
}

// 使用 const 可避免数据被修改
for (const auto &value: values) {
    // do something
}
```

## rvalue 引用

临时对象，又称右值，`R-value`，因其常位于赋值运算符右侧而得名。

C++03及其之前的标准中，右值通常**无法被改变**，通常等效于 `const T&`。

C++11 引入右值引用类型，是一种非常量引用，标记为 `T&&`，用于绑定临时对象，允许对象右值引用在初始化之后进行修改，主要为了实现 `move` 语义。

以下示例仅供演示，实际应用一般在完美转发和类的移动构造上

```c++
int x{5};
int &&rexpr { 2 * x + 3 }; // rvalue 引用
int &rx {x}; // lvalue 引用
```

## move/forward 语义

`std::move` 实际就是一个类型的强制转换，将左值转换成右值引用。

同理，`std::forward` 可以将右值引用转换成左值引用，实际也是一个类型强转。

## 完美转发

右值引用作为类型引入，使得函数重载可以区分左右值。但出于安全考虑，具名变量被称为左值，因此作为右值传入的值使用时也成为了左值。

若该函数是作为一个中间函数将值向下一个函数传递，我们希望右值被传递到右值，默认行为但并非如此：

```c++
void b(int &&b) {
    cout << "b&&=" << b << endl;
}

void b(int &b) {
    cout << "b&=" << b << endl;
}

void a(int &&a) {
    cout << "a&&=" << a << endl;
    b(a);
}

void a(int &a) {
    cout << "a&=" << a << endl;
    b(a);
}

int main() {
    int v = 10;
    // a&=10
    // b&=10
    a(v);
    // a&&=10
    // b&=10 -- 预期应该是 b&&=10
    a(10);
}
```

利用 `std::move`，可将右值直接传递给接收右值的函数，实现完美转发

```c++
void b(int &&b) {
    cout << "b&&=" << b << endl;
}

void b(int &b) {
    cout << "b&=" << b << endl;
}

void a(int &&a) {
    cout << "a&&=" << a << endl;
    b(std::move(a)); // 这里用了 move 语义
}

void a(int &a) {
    cout << "a&=" << a << endl;
    b(a);
}

int main() {
    int v = 10;
    // a&=10
    // b&=10
    a(v);
    // a&&=10
    // b&&=10
    a(10);
}
```

# 作用域

作用域是一个成员可以被访问到的范围。

对于直接在一个源文件中声明的变量，类，函数等，其作用域为整个程序（包括include了该文件的）代码中：

```c++
// a.h
int a {5};

int b() {
    return 10;
}
```

```c++
// main.h
#include "a.h"

int main() {
    int c = b(); // 10
    int d = c + a; // 10 + 5 = 15
}
```

若该成员在一个命名空间中，则所在命名空间便是其作用域，不同文件相同命名空间的成员在同一个作用域，命名空间相关详见 [[Cpp 函数1]]

若该成员在一个函数，类或代码块中，则其所在的上下文（即最近一层 `{}` 包围的区域）便是其作用域

同一个作用域中不能存在标识符相同的成员，但若成员所在作用域的上层作用域中存在同名成员是允许的，该成员会在其生效后（声明后）隐藏上层同名成员

```c++
int a {10};

int getA();

int main() {
    int b {10};
    int c1 = a + b; // c1 = 10 + 10 = 20
    int a {5}; // 没问题，会自动隐藏值为 10 的那个 a
    int c2 = a + b; // c2 = 5 + 10 = 15
    int c3 = getA() + b; // c3 = 10 + 10 = 20
}

int getA() {
    // 由于其作用域不在 main 中，a 仍然访问的是值为 10 的 a
    return a; // 10
}
```

函数参数的作用域位于函数内，`for`，`while` 等循环语句初始化列表的作用域位于循环体内。

## 代码块

使用 `{}` 包围的一段代码，具有独立的作用域

```c++
void main() {
    int a {10};
    {
        int b {20};
    }
    // 编译错误：找不到变量 b
    b = 30;
}
```

# 生存期

## 自动存储时间

在代码块中直接声明的变量，生存期从声明开始，直到代码块结束。

直接声明的变量存在于栈中，`C++` 默认一个函数的栈帧长为 1MB

## 静态存储时间

在代码块内部通过 `static` 声明的变量，或在所有代码块和类之外声明的变量

生存期从程序创建开始，直到程序释放结束。

## 动态存储时间

使用 `new` 在堆上申请的内存，生存期持续到 `delete` 释放