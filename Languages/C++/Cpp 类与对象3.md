# 标准库

标准模板库，STL（`Standard Template Library`），简称标准库，由 C++ 定义的标准类，包括容器、智能指针、迭代器、算法等
- C 库：C++ 兼容所有 C 库，可直接使用 C 的库名，推荐使用 C++ 的命名：`xxx.h` -> `cxxx`
	- cstring：string.h
	- cstdio：stdio.h
- 字符串：string
- 容器与迭代器：可用于存放和查找数据、遍历数据的工具类
- 基本输入输出：iostream
- 内存操作：memory，主要包括智能指针等
- 函数：functional，包括仿函数类和一系列预定义好的标准模板函数
- 多线程模型：主要包括了线程，互斥量，锁等
- 协程：协程是一种程序语言层面模拟的多线程
- 随机数发生器：random
- 日期与时区

## 字符串

### cstring

C 风格字符串相关函数位于 `cstring`/`string.h` 头文件中

> 引入该头文件除了为了处理 c 风格字符串，还常用于 memcpy，memset 等函数的引入

-   字符串长度
	-   `strlen`/`wstrlen`：接受一个 `char*` 或 `wchar_t*`，返回一个到 `\0` 的长度
	-   `strnlen`/`wcsnlen`：接受一个 `char*` 或 `wchar_t*` 和一个 `size_t` 类型的参数，指定最大长度
-   字符串连接
	-   `strcat`：在第一个字符串后链接第二个字符串，已弃用：不一定有足够的连续内存，可能覆盖其他数据
	-   `strcat_s`：安全版本，通过一个 `cerrno` 头文件中定义的返回值判断是否连接成功
		-   0：连接成功
		-   `EINVAL`：源字符串或目标字符串中有一个是 `nullptr`
		-   `ERANCE`：源字符串长度过小，无法容纳目标字符串长度
-   字符串复制
	-   `strcpy`：将字符串复制到目标数组，第一个参数是目标位置，第二个参数是原位置
	-   `strcpy_s`/`wcscpy_s`：安全版本在字符串地址为 `nullptr` 或目标字符串长度不足时抛出异常
-   字符串比较
	-   `strcmp`/`wcscmp`：若所有字符相同，返回 0；否则返回第一个不同字符的差值
-   字符串搜索
	-   `strstr`：返回一个指针，指向第一个参数中第二个参数字符串的位置，不存在则返回 `nullptr`。
- 
```c++
#include<iostream>
#include<cstring>

using namespace std;

int main() {
    const char *pstr{"aaabbbcccdddaaacccbbbdddaaaeeeaaa"};
    const char *psearch{"aaa"};
    int count{0};
    char *pfound, *pcurrent{const_cast<char *>(pstr)};
    while (true) {
        pfound = strstr(pcurrent, psearch);
        if (pfound) {
            ++count;
            // find at 140697701994504
            // find at 140697701994507
            // find at 140697701994519
            // find at 140697701994531
            cout << " find at " << (intptr_t) pcurrent << endl;
            pcurrent = pfound + strlen(psearch);
        } else {
            break;
        }
    }

    // There are 4 aaa in aaabbbcccdddaaacccbbbdddaaaeeeaaa
    cout << "There are " << count << " " << psearch << " in " << pstr << endl;
}
```

### string

C++ 提供的可变字符串类，位于 `string` 头文件中，包含 `string` 和 `wstring` 两个类，派生自 `base_string<T>` 类。

`wstring` 类与 `string` 类仅仅内部存储的类型不同，`wstring` 使用 `wchar_t` 存储，且需要使用 `L` 前缀的字面量创建。

#### 创建

可直接通过字符串字面量和字符串本身创建

```c++
string str1; // ""
string str2(5, 'b'); // bbbbb
string str3 = str2; // bbbbb
string str4 {str2}; // bbbbb
string str5 {"This sentence is false."}; // This sentence is false.
string str6 {str5, 5, 11}; // sentence is <- str5[5, 5+11]
```

#### 连接

- 使用 `+` 运算符连接两个 `string` 类
	- `+` 要求至少有一个是 `string` 类，不能用来连接两个字面量
	- `+` 运算符会创建一个新的 `string` 类，但会尽量使用 `std::move` 语义
	- `+=` 运算符不会创建一个新的`string`，而是将原运算数扩增
- `==`，`<`，`>`等运算符亦被重载，若不同则比较第一个不同的字符
- `at` 或 `[]` 运算符可用来获取和修改某一位置的字符
- `for` 可用于遍历字符串

```c++
string str {"hello"};
for (auto &s: str) {
    // do something
}
```

- `substr`：截取字符串
- 扩增：`append`，`push_back`
- 查找：`find`，`find_first_of`，`find_last_of`，`find_first_not_of`，`find_last_not_of` 等。若不存在则返回 `string::npos`

## functional

### 谓词判断

`less<T>`，`less_equal<T>`，`equal_to<T>`，`not_equal_to<T>`，`greater_equal<T>`，`greater<T>`，`not2<T>`

### 函数运算

`plus<T>`，`minus<T>`，`multiplies<T>`，`divides<T>`，`modulus<T>`，`megate<T>`

### function 类

一个可替代函数指针的仿函数

```c++
std::function<return_type (parameter_type_list)>
```

## 随机数生成器

随机算法
- `linear_congruential`：仅生成整形，低质量，中等速度
- `subtract_with_carry`：可生成整形和浮点，质量中等，速度快
- `mersenne_twister`：仅生成整形，质量高，速度快

标准分布
- `uniform_int_distribution`：离散型均匀分布
- `bernoulli_distribution`：伯努利分布
- `geometric_distribution`：几何分布
- `poisson_distribution`：卜瓦松分布
- `binomial_distribution`：二项分布
- `uniform_real_distribution`：离散型均匀分布
- `exponential_distribution`：指数分布
- `normal_distribution`：正态分布
- `gamma_distribution`：伽马分布

```c++
#include <iostream>
#include <random>

using namespace std;

int main() {
    uniform_int_distribution<int> distribution(0, 90);
    mt19937 engine;
    for (int i = 0; i < 10; ++i) {
        cout << "Value " << i << ": " << distribution(engine) << endl;
    }
}
```

## 智能指针

一种模板对象。行为类似指针，但可以以一定规则管理引用，减少手动 `delete`，避免造成内存泄露。

智能指针位于 `memory` 头文件中。
- `unique_ptr<T>`：唯一指针，不允许任何复制构造调用；允许使用 `std::move` 语义移动指针，但移动后原指针失效
- `shared_ptr<T>`：记录指向同一对象的所有指针，当引用计数归零时对象自动删除
- `weak_ptr<T>`：引用 `shared_ptr<T>` 的引用，但不影响其引用计数，可避免循环引用
	- 若 `shared_ptr<T>` 被释放，则对应 `weak_ptr<T>` 全部失效
- atomic 指针：C++20 提供原子智能指针：`atomic<shared_ptr>`，`atomic<weak_ptr>` 等

对于智能指针，可使用带有 `_pointer` 后缀的类型转换符，转换失败则返回一个指向 `nullptr` 的智能指针
- `static_cast` -> `static_pointer_cast`
- `dynamic_cast` -> `dynamic_pointer_cast`
- `const_cast` -> `const_pointer_cast`

智能指针可通过 `get()` 方法获取其裸指针，通过 `reset()` 方法将指针地址重置为 `nullptr`，并将引用计数-1

## 容器与迭代器

### vector

以数组为基础的线性容器

- 允许使用`[]`运算符进行随机访问
- `begin()`，`end()` 方法可用于获取迭代指针，通过 `++`/`--` 移动指针，类型为 `iterator`
	- `r` 前缀版本用于倒序遍历，`c` 前缀版本用于 `const` 指针，防止内容变更
- `size` 方法可用于获取已存的元素，`capacity` 方法获取内置容器的大小
	- `reserve` 可用于更改 `capacity`
- 允许向容器中部任意位置增减元素，但由于需要移动之后的元素，效率较低
	- `insert`：将数据插入为迭代器的下一个元素
	- `emplace`/`emplace_back`：传入参数，由该函数直接调用构造函数创建对象，减少一次对象复制
	- `push_back` 向容器尾部添加元素
- `eraser` 用于移除某个对象，`clear` 清空整个列表
- `swap` 用于交换两个对象位置，`assign` 交换两个列表
- 使用 `algorithm` 头文件中的 `sort` 方法可实现排序
- 对于存入的数据类型 T，实现以下方法可加快运算效率
	- `T()`，`T(const T&)`，`~T()`，`T& operator=(const T&)`

### array

一个有固定长度的数组
- 允许使用 `[]` 随机访问元素，并实现了 `<`，`<=`，`==`，`>=`，`>` 运算符
- 包含 `fill`，`size`，`back`，`begin`，`end`，`swap` 等常用方法

### deque

双端序列，类似 `vector`，可在头部和尾部进行高效的增删方法，但由于有更复杂的内存管理办法，其他操作稍慢

### list

链表，失去直接随机访问能力，但可以根据迭代器高效的在列表中间增删元素
-   链表常用于遍历，可指定遍历范围
-   包含 `remove`，`unique`，`splice`，`remove_if` 等常用方法

### forward_list

单向链表，只能从头部开始遍历

### queue

实现了 FIFO 的队列，没有迭代器，只能访问头部或尾部的成员

#### priority_queue

一种利用优先级的 `queue`

### stack

实现了 FILO 的队列，默认由 `deque` 实现

### tuple

元组，包含了多个特定类型值的容器；可通过 `get` 方法访问

### map

映射。以 `pair<K, V>` 存储数据
- `utility` 头文件中的 `std::make_pair<K, V>` 可用于创建 `std::pair` 对象
- 支持 `begin`，`end` 等方法获取迭代器
- 支持 `swap`，`clear`，`size`，`empty`，`max_size` 等容器通用方法
- `lower_bound`，`upper_bound`，`equal_range` 可用于根据 `>=`，`<=` 等比较 `key`，获取符合结果的迭代器
- `at` 方法可用于获取其键对应值的引用

### 迭代器

位于 `iterator` 头文件中的模板类，主要用于为算法提供数据
- 输入流：`istream_iterator<T>` 类
- 插入迭代器：向容器插入数据
	- `insert_iterator<T>`
	- `front_insert_iterator<T>`
	- `back_insert_iterator<T>`
- 输出流：`ostream_iterator<T>` 类

## 日期与时区

### Calendar

```c++
// creating a year
auto y1 = year{ 2021 };
auto y2 = 2021y;

// creating a mouth
auto m1 = month{ 9 };
auto m2 = September;

// creating a day
auto d1 = day{ 24 };
auto d2 = 24d;

weeks w{ 1 }; // 1 周
days d{ w };  // 将 1 周转换成天数
std::cout << d.count();

hours h{ d };  // 将 1 周转换成小时
std::cout << h.count();

minutes m{ w }; // 将 1 周转换成分钟
std::cout << m.count();

struct DaysAttr {
    sys_days sd;
    sys_days firstDayOfYear;
    sys_days lastDayOfYear;
    year y;
    month m;
    day d;
    weekday wd;
};

DaysAttr GetCurrentDaysAttr() {
    // 目的获取今年的第一天和最后一天，统一初始化
    DaysAttr attr;
    attr.sd = floor<days>(system_clock::now());
    year_month_day ymd = attr.sd;
    attr.y = ymd.year();
    attr.m = ymd.month();
    attr.d = ymd.day();
    attr.wd = attr.sd;
    attr.firstDayOfYear = attr.y / 1 / 1;
    attr.lastDayOfYear = attr.y / 12 / 31;

    return attr;
}

// 一年中过去的天数，以及一年中剩余的天数
void OverDaysOfYear() {
    // 这会打印出一年中的天数，其中1月1日为第1天，然后还会打印出该年中剩余的天数（不包括）sd。执行此操作的计算量很小。
    // 将每个结果除以days{1}一种方法可以提取整整类型中的天数dn并将其dl分成整数，以进行格式化。

    auto arrt = GetCurrentDaysAttr();
    auto dn = arrt.sd - arrt.firstDayOfYear + days{ 1 };
    auto dl = arrt.lastDayOfYear - arrt.sd;
    std::cout << "It is day number " << dn / days{ 1 } << " of the year, "
        << dl / days{ 1 } << " days left." << std::endl;
}

// 该工作日数和一年中的工作日总数
void WorkDaysOfYear() {
    // wd是|attr.wd = attr.sd|计算的星期几（星期一至星期日）。
    // 要执行这个计算，我们首先需要的第一个和最后一个日期wd的当年y。|arrt.y / 1 / arrt.wd[1]|是wd一月的第一个，|arrt.y / 12 / arrt.wd[last]|则是wd十二月的最后一个。
    // wd一年中的总数仅是这两个日期之间的周数（加1）。子表达式[lastWd - firstWd]是两个日期之间的天数。将该结果除以1周将得到一个整数类型，该整数类型保存两个日期之间的周数。
    // 星期数的计算方法与星期总数的计算方法相同，不同的是星期数从当天开始而不是wd一年的最后一天开始|sd - firstWd|。

    auto arrt = GetCurrentDaysAttr();
    sys_days firstWd = arrt.y / 1 / arrt.wd[1];
    sys_days lastWd = arrt.y / 12 / arrt.wd[last];
    auto totalWd = (lastWd - firstWd) / weeks{ 1 } + 1;
    auto n_wd = (arrt.sd - firstWd) / weeks{ 1 } + 1;
    std::cout << format("It is {:%A} number ", arrt.wd) << n_wd << " out of "
        << totalWd << format(" in {:%Y}.}", arrt.y) << std::endl;;
}

// 该工作日数和一个月中的工作日总数
void WorkDaysAndMonthOfYear() {
    // 从wd年月对的第一个和最后一个开始|arrt.y / arrt.m|,而不是整个全年开始

    auto arrt = GetCurrentDaysAttr();
    sys_days firstWd = arrt.y / arrt.m / arrt.wd[1];
    sys_days lastWd = arrt.y / arrt.m / arrt.wd[last];
    auto totalWd = (lastWd - firstWd) / weeks{ 1 } + 1;
    auto numWd = (arrt.sd - firstWd) / weeks{ 1 } + 1;
    std::cout << format("It is {:%A} number }", arrt.wd) << numWd << " out of "
        << totalWd << format(" in {:%B %Y}.", arrt.y / arrt.m) << std::endl;;
}

// 一年中的天数
void DaysOfYear() {
    auto arrt = GetCurrentDaysAttr();
    auto total_days = arrt.lastDayOfYear - arrt.firstDayOfYear + days{ 1 };
    std::cout << format("Year {:%Y} has ", y) << total_days / days{ 1 } << " days." << std::endl;;
}

// 一个月中的天数
void DaysOfMonth() {
    // 表达式|arrt.y / arrt.m / last|是年份-月份对的最后一天,|arrt.y / arrt.m|就是|arrt.y / arrt.m / 1|月份的第一天。
    // 两者都转换为sys_days，因此可以减去它们以得到它们之间的天数。从1开始的计数加1。

    auto arrt = GetCurrentDaysAttr();
    auto totalDay = sys_days{ arrt.y / arrt.m / last } - sys_days{ arrt.y / arrt.m / 1 } + days{ 1 };
    std::cout << format("{:%B %Y} has ", arrt.y / arrt.m) << totalDay / days{ 1 } << " days." << std::endl;;
}
```

构造初始化为

```c++
// 例如：
sys_days newYear = y/1/1;
sys_days firstWd = y/1/wd[1];
sys_days lastWd = y/12/wd[last];

// 可以替换为：
sys_days newYear = year_month_day{y, month{1}, day{1}};
sys_days firstWd = year_month_weekday{y, month{1}, weekday_indexed{wd, 1}};
sys_days lastWd = year_month_weekday_last{y, month{12}, weekday_last{wd}};
```

### Timezone

```c++
int main()
{
    constexpr std::string_view locations[] = {
        "Africa/Casablanca",   "America/Argentina/Buenos_Aires",
        "America/Barbados",    "America/Indiana/Petersburg",
        "America/Tarasco_Bar", "Antarctica/Casey",
        "Antarctica/Vostok",   "Asia/Magadan",
        "Asia/Manila",         "Asia/Shanghai",
        "Asia/Tokyo",          "Atlantic/Bermuda",
        "Australia/Darwin",    "Europe/Isle_of_Man",
        "Europe/Laputa",       "Indian/Christmas",
        "Indian/Cocos",        "Pacific/Galapagos",
    };
    constexpr auto width = std::ranges::max_element(locations, {},
        [](const auto& s) { return s.length(); })->length();
 
    for (const auto location : locations) {
        try {
            // may throw if `location` is not in the time zone database
            const std::chrono::zoned_time zt{location, std::chrono::system_clock::now()};
            std::cout << std::setw(width) << location << " - Zoned Time: " << zt << '\n';
        } catch (std::chrono::nonexistent_local_time& ex) {
            std::cout << "Error: " << ex.what() << '\n';
        }
    }
}
```
