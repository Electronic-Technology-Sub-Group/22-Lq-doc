---
语言: cpp
语法类型: 高级语法
---
#cpp20

[[../../../constexpr|constexpr]] 修饰的虚函数在被调用时，将会使用常量代替，减少一次函数调用过程。

```cpp
struct X {
    constexpr X() {}

    virtual constexpr int f() {
        return 10;
    }
};

struct Y : X {
    constexpr Y() {}

    virtual constexpr int f() {
        return 20;
    }
};

// 10
constexpr int fx = X().f();
// 20
constexpr int fy = Y().f();
```
