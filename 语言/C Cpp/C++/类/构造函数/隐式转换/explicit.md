---
语言: cpp
语法类型: 高级语法
---
> [!note] 使用场景
> 禁用[[隐式转换]]

#cpp11 

C++ 默认的隐式转换会产生很多奇怪的结果

````tabs
tab: 例 1

```cpp
template<class T>
class Storage {
private:
    vector<T> data;
public:
    operator bool() const { return data.empty(); }
};

int main() {
    Storage<char> a;
    Storage<double> b;
    auto c = a + b;
    cout << "c=" << c << ", " << typeid(c).name() << endl;
    return 0;
}
```

<br/>

`c=a+b`，`a` 和 `b` 本身应该不允许直接相加，但隐式转换为 `bool` 后可以添加。

tab: 例 2

```cpp
class SomeString {
    friend void PrintStr(const SomeString& str);
public:
    SomeString(const char* s) {}
    SomeString(int alloc_size) {}
};

void PrintStr(const SomeString& str) {}

int main() {
    PrintStr("Hello World");
    PrintStr(42);
    return 0;
}
```

<br/>

`PrintStr` 函数接受字符串，当传入数字时，隐式转换成了 `SomeString`，但本意非如此。
````

`explicit` 关键字修饰的函数不能用于隐式转换，只能用于显式转换

````tabs
tab: 修饰类型转换运算符

```cpp
template<class T>
class Storage {
private:
    vector<T> data;
public:
    explicit operator bool() const { return data.empty(); }
};

int main() {
    Storage<char> a;
    Storage<double> b;
    // Invalid operands to binary expression ('Storage<char>' and 'Storage<double>')
    auto c = a + b;
    return 0;
}
```

tab: 修饰构造函数

```cpp
class SomeString {
    friend void PrintStr(const SomeString& str);
public:
    SomeString(const char* s) {}
    explicit SomeString(int alloc_size) {}
};

void PrintStr(const SomeString& str) {}

int main() {
    PrintStr("Hello World");
    // No matching function for call to 'PrintStr'
    PrintStr(42);
    // ok
    PrintStr(static_cast<SomeString>(42));
    return 0;
}
```
````
可以使用 `explicit` 修饰默认构造，[[explicit 默认构造|将构造函数移动到类外]]

`explicit` 可以通过 [[explicit(bool)]] 实现编译时自动控制该关键字的开启或关闭