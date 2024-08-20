---
语言: cpp
语法类型: 编译器
---
#cpp17 

对于所有满足 `constexpr` 要求的 λ 表达式，都将默认声明为 `constexpr` 的。经验证，以下三个例子中，GCC 全部可以编译，MSVC（VS 2022）编译错误：C2131 表达式的计算结果不是常数

```cpp title:'例1 正常编译通过'
auto get_size = [](int i) { return i * 2; };
// get_size 函数体满足编译时常量表达式的要求，自动转换为 constexpr 的
char buffer[get_size(10)] = {0};
```

```cpp title:例2
int k = 5;
char buffer2[get_size(k)] = {0};
```

```cpp title:例3
auto get_size2 = [](int i) {
    static int times = 2;
    return i * times;
};
char buffer3[get_size2(10)] = {0};
```
