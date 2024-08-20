---
语言: cpp
语法类型: 高级语法
---
> [!note] 使用场景
> 约束 [[../../../类型/auto/auto|auto]] 和 [[../../../类型/decltype|decltype]]

约束可以约束 `auto` 和 `decltype(auto)`

```cpp
template<class C>
concept IntType = is_integral_v<C>;

IntType auto i1 = 5;
// error: deduced initializer does not satisfy placeholder constraints
IntType auto i2 = 5.1;

IntType decltype(auto) i3 = 7;
// error: deduced initializer does not satisfy placeholder constraints
IntType decltype(auto) i4 = 7.1;

IntType auto foo1() { return 0; }
// error: deduced return type does not satisfy placeholder constraints
IntType auto foo2() { return 0.1; }

auto bar1 = []() -> IntType auto { return 1; };
// error: deduced return type does not satisfy placeholder constraints
auto bar2 = []() -> IntType auto { return 1.1; };
```
