---
语言: cpp
语法类型: 高级语法
---
> [!note] 使用场景

使用 `sizeof` 关键字获取一个类型、变量（可以是数组）、表达式占用内存字节数，类型为 `size_t`。

```cpp
int main() {
    int i {5};

    size_t size1 = sizeof i; // 4
    size_t size2 = sizeof(int); // 4
}
```
