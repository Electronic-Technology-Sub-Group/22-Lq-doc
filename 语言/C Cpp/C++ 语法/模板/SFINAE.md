#cpp11 

> [!note] SFINAE
> Substitution failed is not an error，替换失败并非错误。在函数模板重载时，若
> -  *模板形参替换为指定的实参* 
> - *由模板实参推导出模板形参* 
> 
> 的过程中出现了失败，则放弃这个重载而不是抛出一个编译错误。

> [!note] 替换失败：在直接上下文中使用模板实参替换形参后，类型或表达式不合法。

> [!error] 编译错误：替换后在非直接上下文中产生副作用导致错误。

SFINAE 允许试图创建无效的模板类型，但不允许试图计算无效的表达。

```run-cpp
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

```run-cpp
template<int* P> int g() { return *P; }
template<int N> int g() { return N; }

int main() {
    int i = g<1>();
    cout << i << endl;
    return 0;
}
```

用于类时，则可以配合模板特化用于校验和运算
# 替换失败
#cpp11 

编译错误比替换失败更容易列举，这里只列举编译失败的例子，除开编译失败外其他模板错误均为替换失败：

1. 处理表达式外部某些实体时发生错误

```c++
class bar {
public:
    bar() {};
    // error: expected ',' or '...' before '&&' token
    // error: invalid constructor; you probably meant 'bar (const bar&)'
    bar(bar&&) {};
};

template<class T>
T foo(T& t) {
    T tt(t);
    return t;
}

void foo(...) {
}

int main() {
    bar b;
    foo(b);
    return 0;
}
```

`foo` 在根据函数声明进行推断时，可以匹配 `T foo(T&)`，`T` 推断为 `bar`。

在执行到 `foo` 函数中时，发现 `T tt(t)` 一步无法生成复制构造，造成编译错误

2. 由于实现受限导致错误：代码可能正确，但由于编译器实现上的限制造成了错误

3. 访问违规

```c++
class bar {
    bar() {};
};

template<class T>
T foo(T*) {
    // error: 'bar::bar()' is private within this context
    return T();
}

void foo(...) {
}

int main() {
    foo(static_cast<bar*>(nullptr));
    return 0;
}
```

`foo` 在进行推断时，可匹配 `T foo(T*)`，`T` 推断为 `bar`。

在执行到 `foo` 函数中时，发现 `T()` 一步无法访问私有构造，造成编译错误

4. 由于同一个函数的不同声明的词法顺序不同，导致替换顺序不同或无法替换

```c++
template<class T> struct A { using X = typename T::X; };
template<class T> T::X foo(typename A<T>::X);
template<class T> void foo(...) {}
template<class T> auto bar(typename A<T>::X) -> T::X;
template<class T> void bar(...);

int main() {
    foo<int>(0);
    // error: 'int' is not a class, struct, or union type
    bar<int>(0);
    return 0;
}
```

第一个 `foo<int>(0)` 编译通过。匹配 `T::X foo(A<T>::X)` 时找不到 `int::X`，产生替换失败，可以正常匹配到 `foo(...)`。

第二个 `bar<int>(0)` 编译错误。在匹配 `bar(A<T>::X)` 时，返回值后置，编译器由于 `A<int>::T` 实例化了一个模板，此时不再是直接上下文环境，不会触发替换失败，直接发生编译错误。
# 实例

```run-cpp
struct X {};
struct Y {
    Y(X) {}
};

X foo(Y, Y) { return X(); }

template<class T>
auto foo(T t1, T t2) -> decltype(t1 + t2) {
    return t1 + t2;
}

int main() {
    X t1, t2;
    auto t3 = foo(t1, t2);
    cout << typeid(t3).name() << endl;
    return 0;
}
```

`t1`，`t2` 类型为 X，`X + X` 并没有对应的类型，触发 SFINAE，模板不会进行实例化，转而调用 `X foo(Y, Y)`；而 `Y` 可以由 `X` 隐式转换而来，可以正常编译，`t3` 类型为 `X`

```run-cpp
template<int I>
void foo(char(*)[I % 2 == 0] = nullptr) {
    cout << "I % 2 == 0\n";
}

template<int I>
void foo(char(*)[I % 2 != 0] = nullptr) {
    cout << "I % 2 != 0\n";
}

int main() {
    char a[1];
    // I % 2 != 0
    foo<5>(&a);
    // I % 2 == 0
    foo<4>(&a);
    return 0;
}
```

根据 `I` 值的奇偶性可以引出 `foo` 后面的形参的两种情况：
* `char(*)[0]=nullptr`：传入的数组长度为 1，替换失败
* `char(*)[1]=nullptr`：编译通过

通过这种方法，可以实现编译时的函数选择

```cpp
class SomeObj {
public:
    void Dump2File() const {
        cout << "dump the object to file\n";
    }
};

template<class T>
auto DumpObj(const T &t) -> decltype(((void) t.Dump2File(), void())) {
    t.Dump2File();
}

void DumpObj(...) {
    cout << "object must have function Dump2File()\n";
}

int main() {
    SomeObj v1;
    string v2;
    // dump the object to file
    DumpObj(v1);
    // object must have function Dump2File()
    DumpObj(v2);
    return 0;
}
```

一个有一定实用性的例子。`DumpObj` 方法要求给定对象需要有 `Dump2File` 方法，通过 `decltype` 检查了一次 `Dump2File` 方法，若没有该方法触发替换失败，调用 `void DumpObj(...)` 重载。
# 约束
#cpp11 

STL 标准库中提供了便于使用 SFINAE 的模板函数 `enable_if` 称为约束，位于 type_traits 头文件中。该头文件中还存在一系列用于判断的条件。

```c++
template<class T, class U = enable_if_t<is_integral_v<T>>>
struct X {};

int main() {
    // 编译成功
    X<int> x1;
    // 编译失败
    X<string> x2;
    return 0;
}
```
