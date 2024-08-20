---
语言: cpp
语法类型: 中级语法
---
C++ 可以将左值转换为将亡值，将亡值也同时属于右值，此时可以将其赋给一个右值引用。

此时该变量与之前的变量有相同的生命周期和内存地址。

```cpp
int i = 10;
int &&k = static_cast<int&&>(i);
```

`std::move` 负责将左值转换成右值引用，其实际实现就是一个类型的强制转换，将一个左值转换为右值（将亡值）。

该功能的作用主要是使一个左值使用移动语义：

```cpp
// 一个具有移动构造的类型，包含一块内存数据的指针
BigMemoryPool value;
// 该操作调用的是 BigMemoryPool 的复制构造 
BigMemoryPool value1 = value;
// 该操作调用的是 BigMemoryPool 的移动构造 
BigMemoryPool value2 = std::move(value);
```

使用场景为在一个右值被转换为左值，又需要再次转换为右值的情况，主要在函数传参。

> [!warning] 无论函数形参是左值还是右值，传入的实参都是左值。