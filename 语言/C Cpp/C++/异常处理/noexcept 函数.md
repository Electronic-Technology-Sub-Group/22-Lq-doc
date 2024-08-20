---
语言: cpp
语法类型: 编译器
---
#cpp17 

`noexcept` 是类型声明的一部分，此时 `f()` 与 `f() noexcept` 是不同的。

```cpp
void (*fp1)() noexcept = nullptr;
void (*fp2)() = nullptr;

void foo1();
void foo2() noexcept;

int main() {
  fp1 = foo1;
  fp2 = foo2;
}
```

> 对 `fp1` 的赋值会产生错误，即带有 `noexcept` 的函数不能赋值给不带该标记的函数指针上；反之，`fp2` 的赋值不会出错。

虚函数的重写也会区分 `noexcept` 修饰符。