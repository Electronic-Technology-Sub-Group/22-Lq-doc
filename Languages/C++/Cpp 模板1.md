模板使用 `template` 关键字声明，用于根据其中的信息生成对应的代码

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

当编译器编译一个带有模板的类中的函数时，由于要实例化模板（根据传入的模板参数，生成带入模板后的实际的类），编译器会根据类模板生成类，在这个过程中也要实例化里面的函数。若带有模板参数的函数未在该文件中定义，则无法实例化。

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

当然生成的函数究竟叫什么 这个看编译器，但原理就是编译器在编译时，将函数模板**实例化**成实际的函数。

## 尾置返回值类型

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
