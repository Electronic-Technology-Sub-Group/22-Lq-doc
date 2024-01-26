`C++` 的函数包括函数头和函数体两个部分。函数头记录了函数的返回类型，函数名，形参列表等基本信息，一个函数的函数名应当是一个合法的标识符，函数体则包含了函数的实现代码，由若干条语句组成。
- 函数头：`return_type function_name(parameter_list)`
	-   `return_type`：返回值类型，无返回值则使用 `void`
	-   `function_name`：任意合法标识符表示的函数名
	-   `parameter_list`：参数列表，若不存在参数则留空或使用`void`
- 函数体：使用`{}`包裹的具体函数代码
- 函数原型：为编译器提供的用于检查函数调用的基本信息，常放在头文件中的最前面。**函数在调用之前必须声明其定义或原型**: `return_type function_name(parameter_list);`
	-   `return_type`：同原函数的返回值
	-   `function_name`：同原函数的函数名
	-   `parameter_list`：同原函数的参数列表类型，而参数名可省略，可不同

```C++
int sum(int a, int b) {
   return a + b;
}
```

以上就是一个简单的名为 `sum`，用于求两个整型之和的函数。于是我们可以在之后调用它：

```C++
int sum(int a, int b) {
   return a + b;
}

int main() {
    // a = 5
    int a = sum(3, 5);
}
```

# main 函数

main 函数是 `C++` 程序的标准入口，是程序的开端。其完整声明如下：

```c++
int main(int argc, char*[] args) {
    // do something
}
```

其中，`argc` 和 `args` 分别是程序运行时带的参数个数和参数。当然，如果不关心参数则可以省略

```c++
int main() {
    // do something
}
```

main  函数的返回值是一个 `int`，当程序正常运行结束时应当返回 0，或可以忽略

```c++
int main() {
    int i = 0;
    i++;
    // 程序正常结束，但没有写 return，这在 main 函数中是允许的，相当于 return 0
    // return 0;
}
```

> [!info]
> main 函数虽然地位特殊，但也是一个普通函数，遵循普通函数的一切要求，也具有普通函数的一切功能，如递归调用等

> [!warning]
> 在 Windows 平台下使用 VS 创建 GUI 应用时，有时也可以用 `_tmain` 或 `wmain` 函数作为程序入口，但这不是 C++ 标准的一部分
# 形参列表

形参列表是函数体中表示函数参数的部分。一般来说一个函数可以接受 0 个或多个参数

```c++
int func(int a, float b, double c, size_t d) {
    // do something
}
```

当函数不接受任何参数时，可以使用 `void` 填充形参列表，或留空

```c++
void no_parameter_fun1() {
    // do something
}

void no_parameter_fun2(void) {
    // do something
}
```

> [!info]- 形参
> 函数头和函数原型的形参列表中的参数

> [!info]- 实参
> 实际调用函数时传入的参数
## 形参默认值

形参允许使用默认值，带有默认值的形参必须在普通参数之后

```c++
void fun(int a, double b, long c = 5L, bool d = false);
```

# 函数原型

由于 `C++` 编译器在编译时是自上而下读取文件，一个函数无法访问在其后声明的函数。为解决这个问题，我们可以在前面声明目标函数的函数原型，告诉编译器这个函数是存在的。

函数原型需要声明的主要有函数名，返回类型和形参列表中的形参类型。函数体应当省略，形参名称则可以省略，可以与具体函数名相同，也可以与具体实现的参数名不同

> [!note]
> 事实上，我们也经常为函数原型声明提供一个较长，描述性更强的形参名，在具体实现中则使用较短的名称

```c++
// 函数原型
int add(int first_value, int second_value, int third_value, int forth_value);
int add(int a, int b, int c);
int add(int, int);

// 具体实现
int add(int v1, int v2, int v3, int v4) {
    return add(v1, v2, v3) + v4;
}

int add(int a, int b, int c) {
    return add(a, b) + c;
}

int add(int a, int b) {
    return a + b;
}
```

# 静态变量

在函数中声明`static`修饰的变量，这类变量的生存期在整个程序，不会在函数返回后释放空间，其值也不会再下次调用函数时重新初始化

```c++
#include<iostream>

using namespace std;

int initialize_value() {
    cout << "initialize value" << endl;
    return 10;
}

void fun() {
    static int value { initialize_value() };
    cout << "value=" << (value++) << endl;
}

int main() {
    // initialize value
    // value=10
    fun();
    // value=11
    fun();
    // value=12
    fun();
    // value=13
    fun();
    // value=14
    fun();
}
```

# 函数重载

`C++` 允许在同一个作用域中存在多个同名函数，但要求其形参列表不同且没有歧义，这种现象称为函数重载。
-   函数重载只看**参数列表**，与返回值无关
-   为兼容 C 语言，函数重载实际在编译器编译时期，将同名重载函数重命名

编译器会自动查找适合的函数，并在编译期确定使用哪个函数。
-   实参类型和形参类型完全匹配（类型本身或其引用），或添加 `const` 修饰符的函数
-   通过默认参数进行匹配的函数
-   通过类型提升（子类 -> 父类）可匹配的函数
-   通过算术类型转换（详见类型基础 - 类型转换 - 隐式转换）可匹配的函数
-   通过类类型转换可匹配的函数

当函数无法匹配任何函数，或可同时匹配多个函数，将产生编译时异常
