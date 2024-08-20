---
语言: cpp
语法类型: 高级语法
---
> [!note] 使用场景
> 防止某些类成为[[../../../类型/聚合类型|聚合类]]

#cpp17

允许使用 explicit 修饰默认构造，或将默认构造相关声明移动到类之外，以创建一些特定的非[[../../../类型/聚合类型|聚合类]]

```cpp title:不可实例化的类
struct A {
    explicit A() = delete; // 使用 explicit 声明
}
// or
struct A {
private:
    A();
}
A::A() = default; // 将构造函数移出
```