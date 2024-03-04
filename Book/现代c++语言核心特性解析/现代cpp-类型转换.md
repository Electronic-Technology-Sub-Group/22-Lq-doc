# 显式自定义类型转换

C++ 之前，隐式类型转换可能出现很多奇怪的问题：

```c++
// 例1
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
    // 0
    auto c = a + b;
    return 0;
}
```

例1，`c=a+b`，`a` 和 `b` 本身应该不允许直接相加，但隐式转换为 `bool` 后可以添加。

```c++
// 例2
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

例2，`PrintStr` 函数接受字符串，当传入数字时，隐式转换成了 `SomeString`，但本意非如此。

C++11 新增 `explicit` 声明，必须显式类型转换

```c++
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

```c++
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
# 允许数组转化为未知范围数组

```c++
void foo(int (&)[]) {}

int main() {
    int a[1] {0};
    // ok
    foo(a);
    return 0;
}
```