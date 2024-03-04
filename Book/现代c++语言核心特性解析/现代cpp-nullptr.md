# nullptr
## NULL

传统 C 与 C++11 之前，表示空指针的值即 0。并且由于 C++ 类型安全，`void*` 不能直接隐式转换为各类指针，因此其定义就是 0 而非 `(void*) 0`

```c++
cout << typeid(NULL).name() << endl; // x
cout << typeid(0LL).name() << endl;  // x -- 证明 NULL 的类型就是长整型
```

我们可以模拟一个 NULL 宏定义：

```c++
#ifndef MY_NULL
    #ifdef __cplusplus                    // c++
        #define MY_NULL 0
    #elif                                 // c
        #define MY_NULL ((void*) 0)
    #endif
#endif
```

在 C 没问题，C 不支持函数重载。但这在 C++ 就这会引起二义性问题：

```c++
void f(int v) {
    cout << "int " << v;
}

void f(char* c) {
    cout << "str " << c;
}

int main() {
    // gcc：call of overloaded 'f(NULL)' is ambiguous
    // 据书上说 vs 下调用的是 int 的重载
    f(NULL);
    return 0;
}
```

上面的例子中，`NULL` 即 `0LL` 既可以被隐式转换为 `int`，又可以隐式转换为 `char*`，根据重载规则产生二义性问题。不同编译器对运行结果处理不同。

```c++
int main() {
    // 编译正常，运行提示 construction from null is not valid
    cout << string(false) << endl;
    // 编译失败：no matching function for call to basic_string(bool)'
    cout << string(true) << endl;
    return 0;
}
```

上面的例子中严格上来说并非 `NULL` 的问题，但本质如出一辙。`false` 先被隐式转换 `0`，然后转换为 `char*`，因此可以通过编译，但指向的地址为 `NULL` 无效，因此运行时出错。`true` 隐式转换成数字为 `1`，无法隐式转换为任何指针，因此过不了编译。（环境：GCC，C++98标准）
## nullptr

C++11 新增 `nullptr` 关键字，表示一个 `std::nullptr_t` 类型的纯右值表示一个空指针
- 禁止进行数学运算，禁止与非指针类型进行比较
- 可以隐式转换为任何类型指针类型，无法隐式转换为任何非指针类型
- 长度为 `sizeof(void*)`

```c++
using nullptr_t = decltype(nullptr);

nullptr_t my_null;
```

- 相同点：
	- `my_null` 可以与 `nullptr` 进行比较，结果为 `true`
	- `my_null` 在指针上的性质与 `nullptr` 相同
	- `sizeof(my_null) == sizeof(void*)`
- 不同点：
	- `nullptr` 是关键字，`my_null` 不是
	- `nullptr` 是纯右值，`my_null` 是左值，`&nullptr` 是错误的
## 应用

- 利用 `nullptr_t` 配合模板，我们可以对空指针进行特化处理

```c++
template<typename T>
void use(T *p) {
    cout << "p is " << typeid(*p).name() << endl;
}

template<>
void use<nullptr_t>(nullptr_t *p) {
    cout << "p is nullptr" << endl;
}

int main() {
    // p is nullptr
    use<nullptr_t>(nullptr);
    // p is So
    use(&cout);
    return 0;
}
```
