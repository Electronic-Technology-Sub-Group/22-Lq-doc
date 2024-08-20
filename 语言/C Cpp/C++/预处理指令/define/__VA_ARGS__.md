---
语言: cpp
语法类型: 高级语法
---
> [!note] 使用场景
> 在宏中使用可变参数

#cpp11 

`__VA_ARGS__` 宏用于展开宏可变参数，与 C 同名宏对应。

```cpp
#define LOG(msg, ...) (printf("[%s] %d " #msg, __FILE__, __LINE__, ##__VA_ARGS__))

int main() {
    LOG(hello %s %d\n\n, "aaa", 123);
    return 0;
}
```

引入 `...` 作为可变参数，相当于 `printf("[%s] %d " "msg", 文件名, 行号, ...)`，一般情况下可以正常输出。

# `__VA_OPT__`

#cpp20

当可变参数 `...` 没有参数时，其结果为 `printf("...", ..., 行号, )`，末尾一个逗号多余，从语法上来说有问题。

> [!success] Cling、GCC 经测试可以正常使用

`__VA_OPT__(分隔符) __VA_ARGS__` 表示仅在可变参数存在时，在前面补充一个分隔符，否则不添加。

```cpp
#define LOG(msg, ...) (printf("[%s] %d " #msg, __FILE__, __LINE__ __VA_OPT__(,) ##__VA_ARGS__))

int main() {
    LOG(hello\n\n);
    return 0;
}
```
