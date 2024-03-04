# 内存偏移

C++11 新增两个关键字：`alignof` 用于测量类型的对齐字节长度，`alignas` 用于修改对齐字节长度，要求必须是 2 的整数幂。

C++11 在 STL 中定义了一系列与内存偏移相关的内容
- `std::max_align_t`：平凡类型。C++ 要求所有标量类型都适应其对齐长度。可以通过 `alignof` 获取其对齐长度。
- `std::alignment_of<class>`：类模板，`value` 属性用于获取对齐字节长度
- `std::aligned_storage<size_t, size_t>`：类模板，`type` 表示一个类型，该类型具有指定对齐长度和大小的内存
- `std::aligned_union<size_t, class...>`：类模板，`type` 表示一个 `union` 类型，该类型具有指定对齐长度和给定类型的最长长度
- `std::align`：函数模板，接收一个指定大小的缓冲区空间指针和一个对其字节长度，返回该缓冲区中最接近的能找到给定对齐字节长度的指针，通常是缓冲区大小+对齐字节长度。

C++17 允许在自定义 `new` 运算符中获取对象的对齐字节长度

```c++
void *operator new(std::size_t, std::align_val_t);
void *operator new[](std::size_t, std::align_val_t);
```
# `std::launder()`

该方法主要为了解决 C++ 的一个核心问题：

```c++
struct X {
    const int n;
};

union U {
    X x;
    float f;
};

int main() {
    U u = { .x = {1} };
    X *p = new (&u.x) X(2);
    // ...
    return 0;
}
```

上面的程序中做了两件事：
1. 初始化了一个联合体 U，内含一个结构体 X。X 有一个常量 n 值为 1
2. 使用 `replace new` 在 `u.x` 地址上创建了一个新的结构体 X，初始化其中常量 n 值为 2

那么，此时 `u.x.n` 是 1 还是 2？
- 从内存角度上来说，结果应该为 2，因为 `replace new` 替换了 `u.x` 的对象
- 从编译器的角度上来说，结果可能为 1，因为 `u.x.n` 是一个常量，编译器优化时可以将其使用 1 替换以提高效率
- 从标准上来说，这个行为是未定义的。在使用 `replace new` 后，不能使用原本的 `u.x`，只能使用 `p` 访问

> [!quote]
> C++ 规定，如果新对象在已被某个对象占据的内存上进行构建，则原始对象的指针、引用及对象名都会自动转向新对象，除非对象是一个常量类型或对象中有常量数据成员或引用类型

C++17 引入 `std::launder()` 方法，作用是防止编译器追踪到数据的来源以阻止编译器对数据的优化

```c++
// 可以确定这个值为 2
int n = *std::launder(&u.x.n);
```