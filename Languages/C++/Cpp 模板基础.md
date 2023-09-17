 模板使用 `template` 关键字声明，用于根据其中的信息生成对应的代码。

可作为模板参数值的必须是编译期常量，可作为模板参数的类型包括：
- 类型，使用 `class` 或 `typename` 声明，值可以是各种类、结构体、基本类型等
	- 值不能是局部类、匿名类（如 `typedef struct { ... } *TPtr` 中的 `TPtr`）
- 整型，包括 `int`，`long long`，`size_t` 等各种整型类型
- 类、枚举、引用，可以包含其他泛型 

```c++
// 期望 Container 是一个类似 C<typename T> 的模板类 
template<typename<typename> class Container>
```

- 指针

浮点数、空指针、字符串不能作为模板参数的值使用。

> [!note]- 类型参数
> 通过关键字 class 或 typename 引入，关键字后应是一个简单标识符

> [!note]- 非类型参数
> 编译器或链接期可以确定的常量
# 函数模板

函数模板是用于生成一组函数的代码。

```c++
template<class T>
T max(const T values[], size_t length);
```

模板中可使用任何类型参数，若 `T` 为类型（即作为泛型编程）时，使用 `class` 或 `typename`。但模板的取值只能是类型或常量等编译器可以在编译时访问到的内容

当使用不同类型调用该函数（模板）时，编译器会自动生成其对应的代码。

```c++
template<typename T, size_t length>
T* new_array() {
    return new T[length];
}

int main() {
    int *array_int = new_array<int, 5>();
    long *array_long = new_array<long, 3>();

    delete [] array_int;
    delete [] array_long;
}
```

上面那段代码等效于

```c++
int* new_array_int() {
    return new int[5];
}

long* new_array_long() {
    return new long[3];
}

int main() {
    int *array_int = new_array_int();
    long *array_long = new_array_long();

    delete [] array_int;
    delete [] array_long;
}
```

当然生成的函数究竟叫什么 这个看编译器，但原理就是编译器在编译时，将函数模板**实例化**成实际的函数。详见[[Cpp 模板#On-Demand 实例化]]

当使用只带有模板类型的方法时，若编译器可以推断出每个类型的具体值，则不需要手动指定模板类型，这个过程称为实参演绎

> [!note]- 实参演绎
> 当使用模板类型参数，且没有手动指定时，编译器可以根据参数类型确定对应模板类型的实际类型
> 
> 注意实参演绎中不允许自动类型转换，且当以引用形式使用（T&）时字符串不会演绎成 `char*`，而是带有具体长度的字符数组

当同时存在普通函数和模板函数可匹配调用时，编译器会选择普通函数而非模板函数。

> [!warning]
> 使用字符串作为模板类型，且模板类型使用引用时可能会出问题，如：
> 
> `template<typename T>`
> `void print(const T &v1, const T &v2) { ... }`
> 
> 以 `print("Hello", "World!");` 方式调用会有异常，此时 T 被识别为 `char[5]`，而 `"World!"` 识别为 `char[6]` 类型。此时可以将模板方法声明为 
> 
> `template<typename T>`
> `void print(const T v1, const T v2)`
> 
> 则可以正常使用，T 被识别为 `char*`，此时发生了实参演绎

当同时存在类型参数和非类型参数时，为了利用实参演绎自动识别类型，可以将类型参数放在所有参数之后

```c++
template<int count, typename T>
void fill(T arr[count]) {
    for (int i = 0; i < count; ++i) {
        arr[i] = T();
    }
}

int main() {
    int arr[10];
    // 相当于 fill<10, int>
    fill<10>(arr);
    return 0;
}
```
# 类模板

```c++
template<class T, size_t size>
class Simple {
public:
    T value[size];
}

int main() {
    Simple<int, 5> a;
}
```

凡是模板类中涉及到模板的方法必须定义在头文件中，这与编译器编译和连接过程有关。

编译器在编译时，每个文件作为一个编译单元单独编译。当所有编译单元编译完成后，将其组织成一个整体的过程叫做连接。

当编译器编译一个带有模板的类中的函数时，由于要实例化模板，编译器会根据类模板生成类，在这个过程中也要实例化里面的函数。若带有模板参数的函数未在该文件中定义，则无法实例化。

对于类模板，在有继承关系时有时会有些问题：访问基类成员时，`this->` 或 `BaseClass::` 前缀不能省略

```c++
template<typename T>
class BaseClass {
public:
    void print() {
        cout << "Hello, BaseClass" << endl;
    }
};

template<typename T>
class CustomClass : BaseClass<T> {
public:
    void say() {
        // 直接使用 print(); 无法编译
        this->print();
    }
};
```

> [!warning]
> 虚函数不能有自己的模板，即如下形式是不行的
> 
> `template<typename T> virtual void fun(T value);`
> 
> 原因是无法确定虚表长度

> [!hint]
> 由于不同模板值生成各自独立的类模板，模板类中 `static` 变量在不同模板之间互相独立
# 参数默认值

对于类模板，模板默认值需要从右向左依次出现，不能有间隔

```c++
#include <iostream>

using namespace std;

template<typename A, class B = float, int C = 5, bool D = false>
class Type {
public:
    void print() {
        cout << "A=" << typeid(A).name() << ", B=" << typeid(B).name() << ", C=" << C << ", D=" << D << endl;
    }
};

int main() {
    Type<int> a;
    Type<float, double> b;
    Type<int, string, 7> c;
    Type<double, double, 100, true> d;

    // A=i, B=f, C=5, D=0
    a.print();
    // A=f, B=d, C=5, D=0
    b.print();
    // A=i, B=NSt7__cxx1112basic_stringIcSt11char_traitsIcESaIcEEE, C=7, D=0
    c.print();
    // A=d, B=d, C=100, D=1
    d.print();
}
```

而对于函数模板，没有这个限制

```c++
template<typename A = int, int B, int C = 7, bool D>
void f() {
    cout << "A=" << typeid(A).name() << ", B=" << B << ", C=" << C << ", D=" << D << endl;
}

int main() {
    // A=i, B=7, C=17, D=0
    f<int, 7, 17, false>();
}
```
# 模板特化

C++ 允许对特定的模板值设定特定的实现，称为模板特化。形式上，将需要特化的模板从尖括号中移除，并在特定位置将对应值替代即可。

```c++
#include <cstring>

template<typename T>
T add(T a, T b) {
    return a + b;
}

// 将 T 为 const char* 的类型进行特化
template<>
const char *add(const char *a, const char *b) {
    size_t len = strlen(a) + strlen(b);
    char *new_str = new char[len];
    memcpy(new_str, a, strlen(a) * sizeof(char));
    strcat(new_str, b);
    return new_str;
}

int main() {
    // 8
    cout << add(3, 5) << endl;
    // Hello World
    cout << add("Hello ", "World") << endl;
    return 0;
}
```

模板特化可以只特化部分模板参数。对于类的部分特化又称为局部特化

```c++
template<typename T, int time>
T addAndMul(T a, T b) {
    return (a + b) * time;
}

template<int time>
const char *addAndMul(const char *a, const char *b) {
    char *tmp_str = new char[strlen(a) + strlen(b)];
    memcpy(tmp_str, a, strlen(a) * sizeof(char));
    strcat(tmp_str, b);

    char *new_str = new char[strlen(tmp_str) * time];
    new_str[0] = '\0';
    for (int i = 0; i < time; ++i) {
        strcat(new_str, tmp_str);
    }

    return new_str;
}
```
