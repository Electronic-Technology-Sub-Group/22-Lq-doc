---
语言: cpp
语法类型: 基础语法
---

`main` 函数是 `C++` 程序的标准入口，是程序的开端。其完整声明如下：

```cpp
int main(int argc, char*[] args) {
    // do something
}
```

`argc` 和 `args` 分别是程序运行时带的参数个数和参数，可以省略

```cpp
int main() {
    // do something
}
```

main  函数的返回值是一个 `int`，当程序正常运行结束时应当返回 0，或可以忽略

```cpp
int main() {
    int i = 0;
    i++;
    // 在 main 函数允许省略 return 0
    // return 0;
}
```

main 函数也是一个普通函数，遵循普通函数的一切要求，具有普通函数的一切功能，如递归调用等

> [!note] 在 Windows 平台下使用 VS 创建 GUI 应用时，有时也可以用 `_tmain` 或 `wmain` 函数作为程序入口，但这不是 C++ 标准的一部分
