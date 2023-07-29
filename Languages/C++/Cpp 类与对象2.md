# 特殊构造函数

一个类的构造函数没有返回值，函数名当与类名相同。当一个类没有构造函数时，C++ 默认生成一个 `public` 的无参构造，实现也为空。

当类中存在任何其他构造函数时，默认构造函数不会生成。也可以手动声明创建默认构造，使用 `= default` 即可

```c++
class A {
public:
    // 任意其他构造
    A(int a) {}
    // 默认构造
    A() = default;
}
```

当然，构造函数只是有特定意义的函数，因此允许有默认值。

## 初始化列表

构造函数中可以使用初始化列表为类成员变量赋值，而不必显示赋值

```c++
class A {
    public:
    int value;
    
    A(int v): value {v} {};
}
```

上面例子中 `value {v}` 就是初始化列表。以 `,` 分割，可以为函数成员变量直接赋值

## 隐式调用（隐式转换）

如果一个构造函数只接受一个参数，且没有 `explicit` 修饰，就可以将构造接受的参数直接赋值给该类型变量，编译器会隐式调用该构造函数创建对象。

```c++
class Box {
public:
    int width;
    int height;
    
    Box(int i): width {i}, height {i} {
        cout << "Create by int " << i << endl;
    }
};

int i = 5;
// Create by int 5
Box b1 = i;
// Create by int 10
Box b2 = 10;
```

## 委托构造

C++11 允许构造函数调用其他构造函数，这种做法称为委托构造，使用 `:` 指定。

```c++
class SomeType {
  int number;
  string name;
  SomeType( int i, string& s ) : number(i), name(s){}
public:
  SomeType( )           : SomeType( 0, "invalid" ){}
  SomeType( int i )     : SomeType( i, "guest" ){}
  SomeType( string& s ) : SomeType( 1, s ){ PostInit(); }
};
```

派生类允许使用 `using` 导入基类的构造函数（但不允许选择导入哪些构造，只能全部导入），使用基类构造创建派生类

```c++
class BaseClass {
public:
  BaseClass(int iValue);
};

class DerivedClass : public BaseClass {
public:
  using BaseClass::BaseClass;
};
```

以上代码 `DerivedClass` 类相当于有一个需要 `int` 类型的构造，因此不会有默认构造：

```c++
DerivedClass dc = DerivedClass(5);
```

派生类不能包含与基类相同签名的构造；若涉及到多继承，导入的几个类不能含有相同签名的构造。

## 复制构造

复制构造是另一个 C++ 自动生成的构造函数。复制构造实际是一个接受当前类引用的构造函数。只要不创建自己的复制构造，编译器就会创建该类的复制构造，内容是直接复制所有类成员的变量。

```c++
class A {
public:
    A(A &other) {
    // custom copy constructor
    }
}
```

复制构造触发的场景包括使用赋值运算符将一个同类型变量复制给另一个变量，函数调用的直接传参，函数返回值返回对象等

```c++
#include<iostream>
#include<string>

using namespace std;

class A {
    string name;
    int times;
public:
    A(const char* name) : name{name}, times{0} {
        cout << "Create A " << name << endl;
    }

    A(const A &another) : name{another.name}, times{another.times + 1} {
        cout << "Copy from " << name << ", copy times " << another.times << " -> " << times << endl;
    }

    void print() {
        cout << "A " << name << "[" << times << "]" << endl;
    }
};

A print_a(A a) {
    a.print();
    return a;
}

int main() {
    // Create A a
    A a {"a"};
    // Copy from a, copy times 0 -> 1
    A a2 = a;
    // Copy from a, copy times 1 -> 2 --> 发生在实参传参过程中
    // A a[2]
    // Copy from a, copy times 2 -> 3 --> 发生在函数返回过程中
    a2 = print_a(a2);
    // A a[3]
    a2.print();
}
```

默认复制构造只是复制了所有值，但如果一个类包含了指针，当原类被释放，若其中的指针也被释放，则复制的对象指针指向的地址行为不可预测。这很危险，**任何涉及到手动分配内存，保存其指针的类，都应重写复制构造函数并在复制构造中使用 `memcpy` 等方式深复制其数据**。

```c++
class A {
    int* value;

public:
    A() {
        value = new int[5];
    }
    
    A(const A &another) {
        value = new int[5];
        memcpy(value, another.value, 5 * sizeof(int));
    }
    
    ~A() {
        delete [] value;
        value = nullptr;
    }
};
```

以上类 `A` 在构造函数中使用 `new` 动态申请了一个数组，并在析构函数中释放。这里在复制构造中重新申请了一块内存并复制了这组数据。这样在原对象释放后，不影响新对象。

## 移动构造

移动构造是一个接受当前类型 `rvalue` 引用的构造函数 `(T&&)`。在没有创建自己的移动构造的情况下，默认调用复制构造。移动构造通常在将一个右值赋给变量时调用。

## 初始化列表构造

C++11 将初始化列表使用 `std::initializer_list` 类实现，使类也能够使用任意初始化列表构造：

```c++
class A {
    public:
    A(std::inititalizer_list<int> list);
}

A a = {1, 3, 5, 7, 9};
```

这类构造函数称为**初始化列表构造函数**，`vector<T>` 等标准容器类型都包含了这类构造。

`std::inititalizer_list<T>` 类为标准类，因此可以在其他地方使用，如函数：

```c++
void f(std::initializer_list<float> list);

f({1f, 2f, 3f});
```

# 析构函数

析构函数没有参数，函数名为 `~类名`，在复制构造最后一个例子中 `~A()` 函数即析构函数，通常用于释放类维护（申请）的内存。当一个对象释放时调用，通常包括：
-   对于 `new` 申请的内存，使用 `delete` 释放时
-   普通变量在其生存期结束时（如函数返回时）

若类不存在析构函数，则编译器会默认生成一个非虚函数的无任何实现的析构函数。
对于继承的类，析构函数的调用规则为：
-   析构函数为虚函数时，总是会调用子类的虚函数
-   析构函数为非虚函数时，若值对应的类型是子类，则调用子类构造，否则调用基类构造

```c++
using namespace std;

class A {
public:
    ~A() {
        cout << "~A" << endl;
    }
};

class B : public A {
public:
    ~B() {
        cout << "~B" << endl;
    }
};

int main() {
    B *b1 = new B{};
    B *b2 = new B{};
    A *a1 = b1;
    // ~A
    delete a1;
    // ~B
    // ~A
    delete b2;
}
```

因此，一个类若重写了析构函数，且该类可能派生子类，则必须将该类的析构函数声明称虚函数
