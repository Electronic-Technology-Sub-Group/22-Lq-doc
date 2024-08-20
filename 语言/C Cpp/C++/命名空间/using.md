---
语言: cpp
语法类型: 基础语法
---
可以使用 `using` 将其他命名空间的成员引入到当前作用域中，前提是没有冲突。
* `using 命名空间::成员名;`：将指定命名空间的成员导入到当前作用域
* `using namespace 命名空间;`：将指定命名空间的所有成员导入到当前作用域

```cpp
#include<iostream>
#include "a.h"

// 引入 aaa 作用域的所有成员
using namespace aaa;
// 引入 std 作用域的 cin 成员
using std::cout;

void main() {
    // std::cout, aaa::get() 都已被导入到当前作用域（的父作用域）中，因此可以直接访问
    // std::endl 还没有被导入，因此需要域解析
    cout << get() << std::endl;
  
    using std::endl;
    // 这里导入了 std::endl，因此可以直接使用了
    // 但注意的是 std::endl 导入在 main 函数的作用域中，using 之前和 main 之外仍未导入
    cout << endl;
}
```