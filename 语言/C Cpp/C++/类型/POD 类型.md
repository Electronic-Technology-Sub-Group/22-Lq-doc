---
语言: cpp
语法类型: 高级语法
---
> [!note] 使用场景
> 某种特殊类型

Plain Old Data，C++ 内建类型或传统 C 结构体，可以通过 `std::is_pod<T>::value` 判断，特点是完全与 C 兼容