该[[关键字]]用于获取类型和数组占用内存的大小（单位字节）。该类型返回一个 `size_t` 类型的数字，该类型是一个 `unsigned` 的整型，根据平台不同，实际类型也不同。

该关键字可用于变量，表达式和具体类型，但使用方式不同：

```c++
int main() {
    int i {5};

    size_t size1 = sizeof i; // 4
    size_t size2 = sizeof(int); // 4
}
```