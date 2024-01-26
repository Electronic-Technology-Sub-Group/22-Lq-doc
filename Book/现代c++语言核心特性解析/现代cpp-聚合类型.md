聚合类型详见[[Cpp 特殊类型#聚合类]]
# 扩展定义

C++ 17 开始允许聚合类具有基类继承关系。对于有基类的类型，聚合类具有以下特征：
- 所有继承都是 `public` 的，没有私有或保护的基类
- 非虚继承
- 不需要调用基类构造函数

可以使用 `<type_traits>` 中的 `is_aggregate`、`is_aggregate_v` 判断某类型是否为聚合类

```c++
class A {
private:
    int a_a;
public:
    int a_b, a_c;
    A(int a, int b, int c): a_a(a), a_b(b), a_c(c) {}
};

class B {
private:
    float b_a;
public:
    float b_b, b_c;
    B(float a, float b, float c): b_a(a), b_b(b), b_c(c) {}
};

class NewAggregateType: public A, public B {
public:
    int a;
    float b;
    double c;
};

// NewAggregateType is aggregate type
if (is_aggregate_v<NewAggregateType>) {
    cout << "NewAggregateType is aggregate type";
} else {
    cout << "NewAggregateType is not aggregate type";
}
```

初始化时，按从左向右的顺序先初始化基类，再初始化其他成员。各基类构造可以用 `{}` 聚合起来，也可以传入每个基类的实例 -- *即假设基类是聚合类的一个特殊成员*

```c++
NewAggregateType value1{
        {1, 2, 3},
        {1.0f, 2.0f, 3.0f},
        1, 2.0, 3.0f
};

A a{1, 2, 3};
B b{1.0f, 2.0f, 3.0f};
NewAggregateType value2{
        a, b, 1, 2.0, 3.0f
};
```
# 聚合类型初始化

C++ 20 之后，允许使用 `()` 完成构造函数初始化（以前使用 `{}`）
# 聚合类型构造函数

C++17 允许带有继承的类成为聚合类，由于聚合类的创建方式并非常规构造函数调用，可能产生一些问题
## 构造冲突

原本不是聚合类的类型变成了聚合类，但没有调用默认构造（通常由于隐式调用了基类保护构造）

```c++
class A {
public:
    int a;

protected:
    A() = default;
};

class B: public A {
};

int main() {
    // error: 'constexpr A::A()' is protected within this context
    B b {};
    return 0;
}
```

以上代码在 C++ 11 可以编译通过，但 C++ 17 产生异常。
- C++17 之前，B 不是一个聚合类，`B{}` 只是使用了 B 的默认构造，间接调用 A 默认构造在 B 类之内，可以访问到 `protected` 成员
- C++17 及之后，B 是一个聚合类，`B{}` 变成了一个聚合类型初始化，初始化时基类 A 被认为是 B 的一个简单成员，没有提供则使用默认值（查找无参构造），此时位置在 B 类之外，无法调用 A 的 `protected` 成员

解决方案：
- 使用 `B b` 形式，隐式调用 B 的默认构造
- （C++20之前）使用 `B b()` 形式
- 为 B 声明自定义构造函数，使之不再是聚合类型
# 默认构造

C++ 17 允许使用 `explicit` 修饰默认构造，或将默认构造相关声明移动到类之外，以创建一些特定的非聚合类

- 无法实例化的类：C++ 17 之前可以通过下面的方法创建一个无法实例化的类

```c++
struct A {
    A() = delete;
}
// or
struct A {
private:
    A() = default;
}
```

但 C++ 17 上面的代码被识别为聚合类，可以通过聚合类型初始化的方法创建 A 对象

```c++
A a {};
```

替代方法：

```c++
struct A {
    explicit A() = delete; // 使用 explicit 声明
}
// or
struct A {
private:
    A();
}
A::A() = default; // 将构造函数移出
```

C++ 20 开始，类中声明 `=default`，`=delete` 的构造函数不再是聚合类

```c++
class A {
public:
    A() = default;
}
```

以上代码类 A 在 C++17 中是聚合类，在 C++20 后就不再是聚合类了。