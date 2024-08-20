---
语言: cpp
语法类型: 标准库
---
C++ STL 提供的可变字符串类，位于 `string` 头文件中，包含 `string` 和 `wstring` 两个类，派生自 `base_string<T>` 类。

`wstring` 类与 `string` 类仅仅内部存储的类型不同，`wstring` 使用 `wchar_t` 存储，且需要使用 `L` 前缀的字面量创建。

# 创建

直接通过字符串字面量和字符串本身创建

```cpp
string str1; // ""
string str2(5, 'b'); // bbbbb
string str3 = str2; // bbbbb
string str4 {str2}; // bbbbb
string str5 {"This sentence is false."}; // This sentence is false.
string str6 {str5, 5, 11}; // sentence is <- str5[5, 5+11]
```

# 连接

* 使用 `+` 运算符连接两个 `string` 类
    * `+` 要求至少有一个是 `string` 类，不能用来连接两个字面量
    * `+` 运算符会创建一个新的 `string` 类，但会尽量使用 `std::move` 语义
    * `+=` 运算符不会创建一个新的 `string`，而是将原运算数扩增
* `==`，`<`，`>`等运算符亦被重载，若不同则比较第一个不同的字符
* `at` 或 `[]` 运算符可用来获取和修改某一位置的字符
* `for` 可用于遍历字符串

```cpp
string str {"hello"};
for (auto &s: str) {
    // do something
}
```

* `substr`：截取字符串
* 扩增：`append`，`push_back`
* 查找：`find`，`find_first_of`，`find_last_of`，`find_first_not_of`，`find_last_not_of` 等。若不存在则返回 `string::npos`
