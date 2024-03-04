# 模块

C++20 引入模块 `module` 特性，允许将代码拆分成独立的逻辑单元，减少头文件带来的问题：
- 宏和未导出名称对于导入模块不可见
- 编译后信息存储于二进制文件，提高编译效率

模块文件一般使用 `ixx` 为后缀名，使用 `export module` 导出模块，使用 `import` 导入模块

```c++
// 模块文件 helloworld.ixx
import <iostream>

export module helloworld;

export void hello() {
    std::cout << "hello world\n";
}
```

```c++
// 使用
import helloworld;

int main() {
    hello();
    return 0;
}
```