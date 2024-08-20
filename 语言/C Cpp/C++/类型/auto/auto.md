---
语言: cpp
语法类型: 中级语法
---
#cpp11 

`auto` 可让编译器自动推断一个变量的类型，称为类型推断：`auto x = expr`

```cpp
auto a = 10;
auto b = a * 3;
```

同时声明多个变量时，遵循自左向右推导规则，取第一个变量的推导类型

```cpp
int n = 5;
// 根据 &n 推断 auto 为 int，故 m 也为 int
auto *pn = &n, m = 10;
// 编译错误：根据 &n 推断 auto 为 int，不符合 m2 类型
auto *pn2 = &n, m2 = 10.0;
```

可用于对函数指针的简化写法

```cpp
int add(int a, int b) {
    return a + b;
}

int main() {  
    auto add_func = add;
    cout << add_func(5, 7) << endl;
    cout << typeid(add_func(5, 7)).name() << endl;
    cout << typeid(add_func).name() << endl;
    return 0;
}
```

> [!danger] 不要在使用初始化列表的同时使用 `=` 运算符赋值给 `auto` 变量，因为赋值的是初始化列表对象

```cpp
int add(int a, int b) {
    return a + b;
}

int main() {  
    int a {5};  
    auto b {5};  
    int c = {5};  
    auto d = {5};  
    cout << "Type of a is " << typeid(a).name() << endl;  
    cout << "Type of b is " << typeid(b).name() << endl;  
    cout << "Type of c is " << typeid(c).name() << endl;  
    cout << "Type of d is " << typeid(d).name() << endl;  
    return 0;
}
```

可用在 `new` 关键字之后，但不常用

```cpp
// 经过两次推导，第一次推导右侧 auto 类型为 int，第二次推导 i 类型为 int*
auto i = new auto(5);
// 经过两次推导，第一次推导右侧 auto 类型为 int，第二次推导 *j 类型为 int
auto *j = new auto(7);
```

可以用结构化绑定，为解构的元组变量统一增加 `const`，`&` 标记等

```cpp
int a[2] = {1, 2};

// x, y: int
auto [x, y] = a;
// m, n: &int
auto& [m, n] = a;
```

`auto` 类型推断的规则如下：

````tabs
tab: `cv` 限定符
若推导类型为值类型（非指针、引用），忽略 `const`，`volatile` 修饰符
<br/>

```cpp
const int i = 5;
// auto 推断为 int 而非 const int
auto j = i;
```

tab: 引用
若推导类型为引用类型，则忽略引用。使用  `auto &` 声明引用类型
<br/>

```cpp
int i = 5;
int &j = i;
// auto 推断为 int 而非 int&
auto m = j;
// 若需要引用，需要这么写
auto &n = j;
```
<br/>

> [!warning] 但若声明为万能引用，则左值被推导为引用

<br/>

```cpp
int i = 3;
// 由于引用折叠，m 的类型为 int&
auto&& m = i;
// n 的类型为 int&&
auto&& n = 3;
```

tab: 函数/数组
推导类型为对应类型的指针
<br/>

```cpp
int add(int a, int b) {
    return a + b;
}

int arr[10];

auto a = add;
auto b = arr;

int main() {
    cout << typeid(add).name() << endl;
    cout << typeid(a).name() << endl;
    cout << typeid(arr).name() << endl;
    cout << typeid(b).name() << endl;
    return 0;
}
```

tab: 初始化列表
- 直接使用，必须使用单元素
- 通过 `=` 连接，推导为 `std::initializer_list<T>` 类型且元素类型必须相同
<br/>

```cpp
// auto 推断为 std::initializer_list<int>
auto x1 = {1, 2};
// auto 推断为 std::initializer_list<int>
auto x2 = {1};
// 编译错误，元素类型必须相同
auto x3 = {'a', 2.0};
// auto 推断为 int，值为 1
auto x4 {1};
// 编译错误，必须使用单元素
auto x5 {1, 2};
```
````
`auto` 还支持其他用法：
- 实现[[对象复制]]
- 推断[[形参和返回值推断|函数形参和返回值]]
- 推断[[λ 表达式推断|λ 表达式形参及返回值]]
- 推断[[非类型模板]]类型
- 推断[[类成员声明|类成员声明]]

也可以使用 `decltype` 的[[decltype(auto)|类型推断规则]]