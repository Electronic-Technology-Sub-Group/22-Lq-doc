# 结构基础知识

```c
struct SIMPLE { int a; char b; float c; };
typedef struct { int a; char b; float c; } Simple;

struct SIMPLE a;
Simple b;
```

- 使用 `struct 名称 {}` 声明的名称为标签（`tag`），后续通过 `struct 名称` 声明其他结构体
- 使用 `typedef struct {} 名称` 声明的名称为一个新类型，后续直接通过名称声明其他结构体

```c
struct A {
    // 错误：循环引用
    struct A perr;
    // 正确
    struct A *pok;
}
```

注意结构体允许自引用，但只能用于**指针**。

- 不完整声明：当两个结构体之间需要互相引用时，可以使用不完整声明，此声明不带有结构体内容（`{}`）

```c
// 不完整声明
struct B;

struct A {
    struct B *ptr;
};

struct B {
    struct A *ptr;
};
```

但不完整声明只能用于**指针**。

- 结构体在内存中不是紧密排布的，需要满足一定的内存偏移规律以提高访问性能 - 变量地址相对结构体位置的偏移量应当被 4 或 8 整除。
	- 通过 `stddef.h` 中的 `offsetof(type, member)` 检查 `type` 结构体中 `member` 成员的内存偏移量
# 位段

位段 bit field 的声明类似结构体，区别在于：
- 成员类型必须是 `int`，`signed int`，`unsigned int`
- 成员名后加一个 `:` 后一个整数，表示该整形占用的位数

```c
struct CHAR {
    unsigned ch: 7;
    unsigned font: 6;
    unsigned size: 19;
};
```

位段保存于一个或多个整形之中。其优点有：
- 允许将长度为奇数的数据包装在一起，节省内存空间
- 方便访问一个整形的部分内容，多用于硬件编程中，如某磁盘控制器的寄存器定义如下：

![[Pasted image 20240122150939.png]]

转换为 C 结构体可以为（注意内存从右向左读）：

```c
struct DISK_REGISTER_FORMAT {
    unsigned command        : 5;
    unsigned sector         : 5;
    unsigned track          : 9;
    unsigned error_code     : 8;
    unsigned head_loaded    : 1;
    unsigned write_protect  : 1;
    unsigned disk_spinning  : 1;
    unsigned error_occurred : 1;
    unsigned ready          : 1;
};

// 设 0xc0200142 为硬件寄存器地址
#define DISK_REGISTER ((DISK_REGISTER_FORMAT*) 0xc0200142)
```

> [!warning]
> 位段具有一定的不可移植性。根据平台不同，以下行为可能不同：
> - `int` 是否有符号，因此最好显式指定 `signed` 与 `unsigned`
> - 位段的最大长度。有的环境限制位段长度为一个整形值
> - 位段成员自左向右还是自右向左分配
> - 某成员值溢出时的行为

> [!note]
> 位段仅为方便使用。任何使用位段实现的功能都可以通过位运算和位移实现。
# 联合

`union` 类型初始化只能初始化成第一个类型

```c
union {
    int a;
    float b;
    char c[4];
} x = { 5 };
```