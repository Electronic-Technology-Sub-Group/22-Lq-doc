---
语言: cpp
语法类型: 高级语法
---
> [!note] 使用场景
> 未知

#cpp17 

允许 using 声明列表内的包展开，便于可变参数类模板派生于形参包的情况

```cpp
template<class T>
class base {
public:
    base() {}
    base(T t) {}
};

template<class ...Args>
class derived : public base<Args>... {
public:
    using base<Args>::base...;
};
```