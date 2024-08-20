---
语言: cpp
语法类型: 标准库
---
定义了一系列宏用于变长参数（不定参数）的访问，通常按照以下步骤使用：
1. 声明 `va_list` 实例
2. 使用 `va_start` 初始化
3. 使用 `va_arg` 提取值。因此我们需要知道每个不定参数的类型
4. 使用 `va_end` 释放参数列表

```cpp
#include<cstdarg>

using namespace std;

void fun(const char *name, int arg_count, ...) {
    // fun: a(3)
    cout << "fun: " << name << "(" << arg_count << ")" << endl;

    va_list arg_ptr;
    va_start(arg_ptr, arg_count);
    //  ...[0]: 123
    cout << " ...[0]: " << va_arg(arg_ptr, int) << endl;
    //  ...[1]: hello
    cout << " ...[1]: " << va_arg(arg_ptr, const char*) << endl;
    //  ...[2]: 3.14
    cout << " ...[2]: " << va_arg(arg_ptr, double) << endl;
    va_end(arg_ptr);
}

int main() {
    fun("a", 3, 123, "hello", 3.14);
    return 0;
}
```
