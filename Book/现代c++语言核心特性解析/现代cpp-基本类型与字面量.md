# 整数

- C++11 加入 `long long` 类型为 64 位整形，字面量使用 `ll`/`LL`，`ull`/`ULL` 标记
- C++14 正式加入 `0b`，`0B` 开头表示二进制整数字面量。事实上 GCC 很早就支持了
- C++14 引入 `'` 作为整数分隔符
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
# 浮点数

C++11：
- 16 进制浮点字面量：支持 `0x1.f4p+9` 的形式
- 16 进制浮点数字与字符串转换：`std::hexfloat`，`std::defaultfloat`
C++17
- `cin` 支持 `std::hexfloat`
# 字面量
## 原生字符串字面量

C++11 开始允许使用 `prefixR"delimiter(字符串内容)delimiter"` 作为原生字符串字面量。其中：
- prefix 为字符串类型前缀，包括 `L`，`u`，`U`，`u8`
- delimiter 为任意长度的字符串，可为空
- 字符串内容为任意原生字符串内容，其中换行、转义、引号等都不会进行转义

```c++
// 最简单的原生字符串，prefix 和 delimiter 都为空
char html[] = R"(<!DOCTYPE html>
<html lang="en">
</html>
)";
```
## 用户自定义字面量

C++11 引入用户自定义字面量，可以将字符串、数字等字面量加一个后缀转换为对象，通过定义 `"" 后缀` 运算符来实现。**后缀应以 `_` 开头**。

C++14 后，`""` 与后缀之间的空格可以省略，且后缀允许使用 C++ 关键字，即可以使用诸如 `23if` 这样的字面量

```c++
const size_t LEN_KM = 1000000;
const size_t LEN_M = 1000;
const size_t LEN_CM = 10;

// 以毫米 mm 记录的线段长度
struct Length {
    const size_t len_mm;

    string str() const {
        if (len_mm) {
            stringstream ss;
            size_t v = len_mm;
            while (v) {
                if (v >= LEN_KM) {
                    ss << (v / LEN_KM) << "km";
                    v %= LEN_KM;
                } else if (v >= LEN_M) {
                    ss << (v / LEN_M) << "m";
                    v %= LEN_M;
                } else if (v >= LEN_CM) {
                    ss << (v / LEN_CM) << "cm";
                    v %= LEN_CM;
                } else {
                    ss << v << "mm";
                    break;
                }
            }
            return ss.str();
        } else {
            return "0mm";
        }
    }
};

// 字面量运算符
Length operator "" _mm(size_t v) {
    return Length{v};
}

Length operator "" _cm(size_t v) {
    return Length{v * LEN_CM};
}

Length operator "" _m(size_t v) {
    return Length{v * LEN_M};
}

Length operator "" _km(size_t v) {
    return Length{v * LEN_KM};
}

// 其他运算符
Length operator+(const Length &a, const Length &b) {
    return Length{a.len_mm + b.len_mm};
}

Length operator-(const Length &a, const Length &b) {
    return Length{a.len_mm - b.len_mm};
}

void operator<<(basic_ostream<char> &s, const Length &len) {
    s << len.str();
}

int main() {
    Length a = 5_cm + 10_km - 100_m + 257_mm;
    // 9km900m30cm7mm
    cout << a;
    return 0;
}
```

