main [[函数]]是 `C++` 程序的标准入口，是程序的开端。其完整声明如下：

```c++
int main(int count, char*[] args) {
    // do something
}
```

其中，`count` 和 `args` 分别是程序运行时带的参数。当然，如果不关心参数则可以省略

```c++
int main() {
    // do something
}
```

这样也可以。

main  函数的返回值是一个 `int`，当程序正常运行结束时应当返回 0，或可以忽略

```c++
int main() {
    int i = 0;
    i++;
    // 程序正常结束，但没有写 return，这在 main 函数中是允许的，相当于 return 0
    // return 0;
}
```

```ad-info
main 函数虽然地位特殊，但也是一个普通函数，遵循普通函数的一切要求，也具有普通函数的一切功能，如递归调用等
```

```ad-warning
在 Windows 平台下，有时也可以用 `_tmain` 或 `wmain` 函数作为程序入口，但这不是 C++ 标准的一部分
```
