---
语言: cpp
语法类型: 高级语法
---
> [!note] 使用场景
> 需要获取变量类型名等信息，可用于测试或编译时反射

`typeid` 可以查询一个类型的具体信息，返回一个 `type_info` 对象，位于 `typeinfo` 头文件中。

```cpp
#include<typeinfo>

int main() {
    auto a {10};

    const std::type_info &info = typeid(a + 10);
    const char *typeName = info.name(); // i
    cout << typeName << endl;

    return 0;
}
```

`type_info` 类重写了 `==` 运算符，并包含了对应类型的类型名等信息。