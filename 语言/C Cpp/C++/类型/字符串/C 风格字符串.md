---
语言: cpp
语法类型: 基础语法
---
C 语言中，使用字符指针 `char*` 或字符数组 `char[]` 表示字符串。 #cpp11 仅允许使用 `const char*` 接受字符串。

C++03 字符串分为 `char` 和 `wchar_t`，其中 `wchar_t` 为了对 `Unicode` 编码的兼容。

```cpp
char str[6] {"hello"};
const char* str2 = "every one";
wchar_t str3[] {L"Unicode chars"};
```

> [!warning] 字符串以 `\0` 结尾，因此字符串**数组**长度应比字符串长度多 1

可使用 `getline` 从控制台获取字符串，且位于 `cstring` 头文件中（C 中的 `string.h`）内置了大量处理字符串的函数

```cpp
char name[80];
// 获取最多 80 个字符，遇到 \n 停止
cin.getline(name, 80, '\n');
```

使用 `""` 表示字符串字面量：`"this is a string"`

* 宽字符串：用于 Unicode 字符串（UTF-8/UTF-16 等），使用 `L` 前缀

```cpp
wchar_t s[] { L"Hello, I'm 鹿钦." };
```

* C++ 支持多行字符串

```cpp
char s[] {
    "This is a very long string that "
    "has been spread over two lines."
};
```