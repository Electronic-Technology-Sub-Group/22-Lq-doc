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
# 可变参数模板

C++11 允许使用`...`定义任意个数、任意类别的模板参数，不必在定义时固定参数个数

```c++
template<typename... TYPES> class Tuple;
```

可变参数模板在模板函数中，配合函数的不定长参数可安全的处理不定长参数的类型

```c++
template<typename... PARAMS> void printf(const std::string &str, PARAMS.. parameters);
```

`PARAMS...` 和 `parameters` 分别代表变长类型集合和变长参数集合，被称为参数包。无法直接解开参数包，通常使用递归方法解包：

```c++
void printf(const char *s) {
    std::cout << s;
}

template<typename T, typename... ARGS>
void printf(const char *s, T value, Args... args) {
    if (*s == '%' && *(++s) != '%') {
        std::cout << value;
        // 当 args 为空时，调用 void printf(cosnt char *s)
        // 当 args 非空时，调用 void printf(const char *s, T value, Args... args)
        printf(*s ? ++s : s, args...);
    }
}
```

虽然没有一个简洁的机制实现**变长参数模板值**的迭代，但配合 `...` 会进行展开，如

```c++
template<typename... BASES>
class AClass: public BASES... {}
```

`BASES` 中所有类型会直接展开成 `AClass` 的基类。

变长参数可通过 `sizeof...(args)` 取得其长度，计算结果为编译期常量。
