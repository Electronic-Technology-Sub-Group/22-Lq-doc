C++20 新增 *太空船运算符（spaceship）* `<=>` 可用于三向比较，即其一个返回的结果可以明确区分 `>`，`=`，`<` 三种关系。
$$
a<=>b\begin{cases}
>0&a>b\\ 
=0&a=b\\
<0&a<b
\end{cases}
$$
**注意：`<=>` 返回值不是整形，只能和同类型或 `0` 进行大小比较**

根据约束力的强弱，`<=>` 运算符的返回值有三种类型可选：

| 类型 | 可选值 | 约束力说明 | 示例 |
| ---- | ---- | ---- | ---- |
| `std::strong_ordering` | `less`，`equal`，`greater` | 强调可替换性，当 `a<=>b` 值为 `equal` 时，`a`，`b` 可以互相替换 | 整形，布尔值 |
| `str::weak_ordering` | `less`，`equivalent`，`greater` | 强调不可替换性，当 `a<=>b` 非 `equivalent` 时，`a`，`b` 不可替换，但反之不一定成立 | 标准库中无，如某些大小写不敏感的字符串类 |
| `str::partial_ordering` | `less`，`equivalent`，`greater`，`unordered` | 约束力最弱，支持两个值不可比较大小的情况 | 浮点数，存在 `NaN` 不可与其他数字比较 |
- 枚举之间进行比较，若基类型不同，则不可比较；否则转换为整形比较
- 枚举与整形之间比较，枚举转换为整形后进行比较；枚举不能与浮点数比较
- 布尔值只能与布尔值进行比较
- 数组之间不能直接进行比较，指针只能与另一个（可隐式转换为）同类型的指针进行比较
# 自动生成

早期，C++ `std::rel_ops` 头文件允许根据 `==`，`<` 两个运算符生成其他自定义运算符。

C++20 之后，只要实现了 `<=>` 运算符，编译器将自动生成对应的 `<`，`>`，`<=`，`>=` 四种运算符。

*注意：由于性能问题，`==` 运算符并没有从 `<=>` 中派生。但定义了 `==` 运算符的对象可以直接生成 `!=` 运算符*

在实现了 `<`，`==` 运算符后，可以手动声明实现默认的 `<=>` 运算符

```c++
struct Data {
    bool operator==(const Data &rhs) const { ... }
    bool operator<(const Data &rhs) const { ... }
};

struct ThreeWay {
    Data m;
    // 调用了 Data 的对应函数
    std::strong_ordering operator<=>(const ThreeWay &rhs) const = default;
```