---
语言: cpp
语法类型: 基础语法
---

`#define` 指令用于编译时的常量替换，常用于定义编译时常量和宏。

```cpp
#define VALUE_NAME value
#define MICRO_NAME(x) ((x)+1)
```

该指令会在编译期完成文本或宏的替换。

常量替换不限于对值的替换，还可以用于替换关键字、实现类型别名等

````tabs
tab: 类型别名
```cpp
#define UINT unsigned int

int main() {
    UINT a = 30u;
    return 0;
}
```

tab: 编译结果
```cpp
# 1 "main.cpp"
# 1 "<built-in>"
# 1 "<command-line>"
# 1 "main.cpp"


int main() {
    unsigned int a = 30u;
    return 0;
}
```
````
[[宏]]则可以实现部分函数的功能


