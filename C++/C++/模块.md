优点：
- 没有头文件
- 声明与实现可分离
- 可显式指定导出类或函数
- 不需要头文件重复导入宏
- 模块名可相同，且不会冲突
- 编译时模块只会处理一次，速度更快
- 预处理宏只会在模块内有效
- 模块引入与引入顺序无关

模块在 `MSVC` 中以 `.ixx` 为后缀名，使用 `export` 导出，使用 `import` 导入

```c++
// test.ixx
export module cppdemo.test

export auto GetValue()
{
	return _GetValue();
}

auto _GetValue()
{
	return "Hello World!";
}
```

```c++
#include <iostream>

import cppdemo.test;

int main()
{
    std::cout << GetValue();
}
```
