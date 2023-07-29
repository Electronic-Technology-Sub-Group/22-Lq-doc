
# 运算符重载

在 C++ 中，一个类型的大多数运算符都可以重载，除了
-   `::` 作用域解析运算符，用于访问类的静态成员
-   `?:` 三元运算符
-   `.` 直接成员选择运算符，用于通过对象直接访问成员（没错这也是运算符）
-   `sizeof` 操作符，用于计算类占用内存字节数
-   `.*` 指针解引用操作符

运算符重载通过声明以 `operator` + 运算符为名的函数重载

```c++
class A {
public:
    bool operator<(const A& other) const;
}
```

`operator` 和运算符之间可以没有空格，也可以添加空格。但若操作符是单词，如 `new` 和 `delete`，则 `operator` 之后需要空格避免歧义。

运算符支持 `const` 修饰符，当重载的运算符不修改原对象时，应当使用 `const` 修饰。

若运算符需要不止一个操作数，本身（即方法中 `this` 指针）为运算符的最左侧操作数。
-   `operator()()` 运算符重载是正确的，他重载了 `()` 运算符，这类对象可像函数一样调用。`lambda` 表达式即一种这类对象
-   `operator=()` 可重载赋值运算符
-   `++`，`--` 重载时，前后缀通过参数列表体现：
	-   `T& operator++()` --> ++a
	-   `T& operaotr++(T)`--> a++
-   比较运算符只需要重载 `<` 和 `==` 运算符即可，剩下的可以通过使用标准库中 `std::rel_ops` 命名空间的相关函数直接实现。

## 仿函数

若我们通过运算符重载，重载了一个类或结构体的 `()` 运算符，我们就可以像函数一样使用该类型的对象，称这种对象为仿函数，我们也可以将其赋值给一个函数指针或对应 `function` 类型变量。

## 移除函数

使用 `=delete` 移除函数，用于运算符重载。

一般来说，除了 `operator=()` 外所有运算符重载都会被派生类继承。可在派生类中使用 `=delete` 移除。

```c++
class A {
public:
    A& operator+(const A &other) const { /*...*/ };
}

class B: A {
    public:
    A& operator+(const A &other) const = delete;
}
```

# 自定义字面量

C++11 支持数字、字符、字符串的用户自定义字面量类型。通过自定义运算符 `""_suffix` 实现

```c++
using namespace std;

struct A {
    int value;
};

A operator""_m5(unsigned long long value) {
    return A {(int) value * 5};
}

A operator""_m15(unsigned long long value) {
    return A {(int) value * 15};
}

int main() {
    A a = 7_m5;
    A b = 7_m15;
    // 35 105
    cout << a.value << " " << b.value << endl;
}
```

## 标准库类字面量
- `s`：对于 `const char*`，创建`std::string`
- `h`，`min`，`s`，`ms`，`us`，`ns`：对于数字，创建 `std::chrono::duration`
- `if`，`i`，`il` 创建 `std::complex<float>`，`std::complex<double>`，`std::complex<long double>`

## 类字面量

字符串字面量可额外接受一个`size_t`类型参数，表示字符串长度

```c++
S operator ""_mysuffix(const char* string_values, size_t num_chars) //字符串字面量
{
    S  sv_ (string_values);
    return sv_;
}

S sv {"hello"_mysuffix} ;
```

# 类型转换符

实现自定义类型转换操作的函数称为类型转换符，属于运算符重载。确切说，实现类型转换的语法本身就是运算符

```c++
class A {
public:
    int v;
    
    operator bool() {
        return v % 2 == 0;
    } 
};
```

单变量的构造可以被视为隐式转换的条件

```c++
class A {
public:
    int v;
    A (int n): v{n} {} 
};

int main() {
    A a = 3;
}
```

C++11 中，允许 `explicit` 修饰类型转换符，此时禁止隐式转换，适用于构造和运算符

```c++
class A {
public:
    int v;
    
    explicit operator bool() {
        return v % 2 == 0;
    } 
};

int main() {
    A v {3};
    // 错误：No viable conversion from 'A' to 'bool'
    // 移除 explicit 即可
    bool b = v;
}
```

但在`if`，循环，逻辑运算等需要 `bool` 的地方，视为显示转换

```c++
int main() {
    A v {3};
    if (v) {
        // 正确
        cout << v.v;
    }
}
```
