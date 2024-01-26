C++11 引入 `decltype` 关键字用于在编译期确定某个表达式的具体类型。`decltype` 允许作为普通成员变量类型（`auto` 只能用于静态成员变量）

> [!note]
> 在 `decltype` 之前，可用于编译期获取类型信息的方法：
> - auto 根据目标表达式值推断类型
> - decltype 根据目标表达式每一项类型推断类型
> - typeof 运算符由 gcc 扩展，非 c++ 标准，类似 decltype
> 
> 可用于运行时获取类型信息方法：
> - typeid 运算符将类型信息保存在 `std::type_info` 中，但其 `name()` 输出内容与编译器相关
# 推导规则

- 若被推导表达式是一个未加括号的表达式或类成员 e，推断结果为 e 的类型 T；若 e 为一组重载函数，则无法推断

```c++
int i = 10;
// decltype(i) 推断为 int
decltype(i) i_ret;
```

![[Pasted image 20240108092223.png]]

```c++
struct STRUCT {
    int x;
    double y;
} *j;
// decltype(j) 推导为 STRUCT*
decltype(j) j_ret;
// decltype(*j) 推导为 STRUCT&
decltype(*j) j__ret;
// decltype(j->y) 推导为 double
decltype(j->y) jx_ret;
```

![[Pasted image 20240108092232.png]]

- 若推导表达式为一个函数或仿函数调用，推断类型为函数返回值。注意此时函数并不会被调用

```c++
const int&& foo();
// decltype(foo()) 推断为 int&&
decltype(foo()) foo_result;

int fun(char);
double fun(int);
char fun(double);
// decltype(fun(3.0)) 推断为 char
decltype(fun(3.0)) fun_d_result;
```

![[Pasted image 20240108093953.png]]

- 若被推导值为类型为 T 的左值，则结果为 `T&`；若被推导值为将亡值，则结果为 `T&&`

```c++
// i=3 结果左值，decltype(i=3) 推断结果为 int&
decltype(i=3) lv_result;
// (int&&)i 结果为右值，decltype((int&&)i) 推断结果为 int&&
decltype((int&&)i) rv_result;
```

![[Pasted image 20240108094440.png]]

- 其他类型直接返回被推导值的类型（比较奇怪的是函数）

```c++
void foo();
// foo_type 类型为 void(void)
// typeid(foo_type).name() 为 FvvE
// 这个不知道有什么用
decltype(foo) foo_type;
// p_foo_type 类型为 void(*)(void)
// typeid(p_foo_type).name() 为 PFvvE
// 这个是函数指针
decltype(foo) *p_foo_type;
```

- 推断保留 const，volatile 修饰符
# 函数返回类型后置

当使用函数返回值后置时，若返回值类型不确定（如使用模板等），可以使用 `decltype` 对其进行推断

```c++
// 实际返回值类型为 a + b 的类型
// a b 类型为模板类型 A B，在编译时确定，编写时不确定
// 实际返回值类型将在模板实例化时由编译器推断确定
template<typename A, typename B>
auto add1(A a, B b) -> decltype(a + b) {
    return a + b;
}
```

这么写的原因是在决定返回值类型（`auto` 的位置）不能访问到输入的参数的。当然，严格来说这种情况也可以不用后置返回类型来写，不过很麻烦，比如：

```c++
// 这种写法隐式访问了 A 和 B 的无参构造
// 因此要求两个类型必须有无参构造
template<typename A, typename B>
decltype(A() + B()) add(A a, B b) {
    return a + b;
}
```

或者这么写，由于不实际完成计算，可以利用 nullptr 获取两个类型的值用于推断：

```c++
template<typename A, typename B>
decltype(*static_cast<A*>(nullptr) + *static_cast<B*>(nullptr))
add(A a, B b) {
    return a + b;
}
```

或者利用 `std::declval` 模板方法。该方法位于 `type_traits` 头文件中，可以将给定类型转换为其引用类型（但没有实现，因此用于模板和类型推断），与强转的原理类似

```c++
#include <type_traits>
using std::declval;

template<typename A, typename B>
decltype(declval<A>() + declval<B>())
add(A a, B b) {
    return a + b;
}
```
# decltype(auto)

C++14 引入 `decltype(auto)` 结合使用表示用 `decltype` 的推导规则推导 `auto`。该组合必须单独使用，不能结合指针、引用、const、volatile 使用，其他用法与 `auto` 几乎完全相同只是推导规则不同。

自 C++17 后，`decltype(auto)` 还可以用于非类型模板的类型，详见[[现代cpp-auto 占位符#非类型模板|auto非类型模板]]。

```c++
int i = 3;
int &j = i;
int &&foo();

// a1 a2 均为 int
auto a1 = i;
decltype(auto) a2 = i;
// b1 为 int，b2 为 int&
auto b1 = j;
decltype(auto) b2 = j;
// c1 为 int，c2 为 int&&
auto c1 = foo();
decltype(auto) c2 = foo();
// d1 d2 均为 int
auto d1 {3};
decltype(auto) d2 {3};
// e1 为 initializer_list<int>
// e2 无法编译，因为 {3, 5} 不是表达式
auto e1 = {3, 5};
decltype(auto) e2 = {3, 5};
// *f1 为 int*
// f2 无法编译，因为 decltype(auto) 只能独立使用
auto *f1 = &i;
decltype(auto) *f2 = &i;
```

该组合用法可以代替函数返回类型后置：

```c++
template<typename A, typename B>
decltype(auto) add2(A a, B b) {
    return a + b;
}
// 等效于
template<typename A, typename B>
auto add1(A a, B b) -> decltype(a + b) {
    return a + b;
}
```