# 一些模板规则

- 类模板名称不能与其他成员相同

```c++
int X;
// 允许
class X {};

int Y;
// 错误
// 'template<class T> struct Y' redeclared as different kind of symbol
// previous declaration 'int Y'
template<class T>
class Y {};
```

- 模板无法拥有 C 链接，可以有 C++ 链接或（编译器支持的）非标准链接

```c++
// 错误 template with C linkage
extern "C"
template <class T>
void invalid();
```
# 实例化

```ad-quote
实例化：根据泛型的模板的定义，生成具体的类型或函数
```
## On-Demand 实例化

C++ 编译器遇到模板使用时，利用所给定的实参对应的模板参数，自动产生模板的特化实例。

On-Demand 实例化又称隐式实例化或自动实例化，需要编译器可以访问到模板的整个定义。若模板参数为 `class` 类型，则作为参数的类型也必须是可见的。
## 延迟实例化


## 显示实例化
## 实例化模型
# 尾置返回值类型

`decltype`：获取一个表达式的返回值类型，常用于判断函数返回值。比如，我现在有一个函数，求两个值的和：

```c++
template<typename T>
?? add(T &a, T &b) {
    return a + b;
}
```

由于 C++ 允许自定义运算符，很有可能 `a + b` 返回值并不是类型 `T`，此时我们需要编译器自动推断其返回值类型。事实上编译器在编译时期已经知道所有变量的类型，在理论上也是可以推断出他们的和的类型的。此时我们就可以使用 `decltype` 去让编译器推断其类型

```c++
template<typename T>
?? add(T &a, T &b) {
    decltype(a+b) result = a + b;
    return result;
}
```

但还有一个问题。C++ 编译器扫描源码时，其顺序是自左向右扫描。因此在指定函数类型时，编译器还无法获得其形参列表，我们还要让函数的返回值往后放一放，至少让编译器知道形参列表再说，此时使用自动推断类型 `auto` 关键字

```c++
template<typename T>
auto add(T &a, T &b) -> decltype(a + b) {
    return a + b;
}
```

以上，一个用于泛型的尾置返回值函数就完成了。

事实上，编译器不会去确切的求 `a+b` 的值是多少，它可以根据 `a` 和 `b` 的类型，去查找对应的运算符。因此，我们还能这么用：

```c++
template<typename T>
auto add(vector<T> &a, vector<T> &b) -> decltype(vector[0] + vector[1]) {
    return vector[0] + vector[1];
}
```

上面用到了一个泛型容器 `vector<T>`，这和数组差不多。由于不知道 `vector<T>` 长度，也就是说他可能没有数据存储在内，直接相加可能出异常，但在 `decltype` 中不需要考虑这些东西，编译器只会去查找 `vector<T>` 的 `[]` 运算符，获取他的返回值类型后再查找 `T` 的 `+` 运算符，使用他的返回值类型。
# 缺省值初始化

当我们需要初始化一个模板类型的默认值，假设模板类型为 `T`，可使用 `T()` 表示默认实现

```c++
template<typename T>
class DefaultValue {
public:
    T value;

    DefaultValue() {
        value = T();
    }
};

int main() {
    // 全部为 0 (或 \0 或 false 等)
    cout << DefaultValue<int>().value << endl;
    cout << DefaultValue<float>().value << endl;
    cout << DefaultValue<double>().value << endl;
    cout << DefaultValue<bool>().value << endl;
    cout << DefaultValue<char>().value << endl;
    return 0;
}
```

也可以在构造函数上初始化列表位置使用类似 `变量名()` 初始化

```c++
template<typename T>
class DefaultValue {
public:
    T value;

    DefaultValue(): value() {
    }
};
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
# 导出模板

导出模板，又称模板的分离模型，使用 `export` 修饰模板的定义即可。

```c++
export template <typename T>  
void print_typeof(T value);
```

如此，即使模板实现不可见的情况下，导出的模板仍可以正常使用。该关键字可用于模板类、函数及类的成员，但 `export` 与 `inline` 无法共存。

```ad-error
至今（2023/09/04），包括 gcc，MSVC 等编译器均不支持 `export` 关键字
```

事实上，分离模型实例化需要查找的两个位置 - 模板定义和实例化位置之间，编译器会在其中建立一些耦合，且对程序员不可见，这就会产生一些新问题
- 需要更多的编译时间
- make、nmake 等基于代码的依赖性管理工具失效
- 可能会出现一些以外的语义
# 模板链接

模板也可以使用 `extern` 声明
# 外部模板

C++11 允许编译器在编译期遇到完整定义的模板时，暂不在特定位置实例化模板，使用`extern`声明：

```c++
extern template class std::vector<MyClass>;
```

这表示，告诉编译器，**不要**在该文件中将该模板类实例化。
# SFINAE

```ad-quote
SFINAE: Substitution failed is not an error，替换失败并非错误，编译时泛型实例化时，若生成失败，不会被认为是编译错误，而是继续尝试匹配其他模板
```

SFINAE 允许试图创建无效的类型，但不允许试图计算无效的表达。

```c++
template<int I> void f1(int (&)[24 / (4 + I)]);
template<int I> void f2(int (&)[24 / (4 - I)]);

int main() {
    // 允许
    &f1<4>;
    // 错误：size of array is not an integral constant-expression
    &f2<4>;
    return 0;
}
```

SFINAE 是函数模板可以重载的重要基础。下面例子中 `g<1>()` 先试图生成 `<int* 1>` 失败，之后尝试生成 `<int 1>` 版本成功。

```c++
template<int* P> int g() { return *P; }
template<int N> int g() { return N; }

int main() {
    int i = g<1>();
    return 0;
}
```

用于类时，则可以配合模板特化用于校验和运算，更多详见 [[Cpp 模板元编程]]