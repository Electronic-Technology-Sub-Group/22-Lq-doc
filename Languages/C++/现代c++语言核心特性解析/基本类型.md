# long long

`long long` 类型为 64 位整形，于 c++11 加入，字面量使用 `ll`/`LL`，`ull`/`ULL` 标记。与之同时还包括 `numeric_limits` 头文件的相关模板。
# 字符

c++11 增加用于记录 UTF-16 编码字符的类型 `char16_t` 和 UTF-32 编码字符类型 `char32_t`，以及 `u8`，`u`，`U` 的字面量前缀分别对应 UTF-8，UTF-16，UTF-32 字面量前缀。但 c++17 前 `u8` 只可用于字符串字面量而不能用于字符字面量。c++20 增加用于记录 UTF-8 编码的 `char8_t` 类型。

同时还包括 `cuchar`，`string` 等标准库相对应类型支持，如 `u32string` 类型。

| 字符类型 | 前缀 |
| ---- | ---- |
| char8_t | u8 |
| char16_t | u16 |
| char32_t | u32 |

```c++
// 用于字符
char8_t u8char = u8'a';
char16_t u16char = u'你';
char32_t u32char = U'好';

// char8_t 可以直接转换成 char，但有警告
char ch = u8char;

// 用于字符串
auto str8 = u8"hello world";
auto str16 = u"你好世界";
auto str32 = U"你好世界";
```
