`typeid` [[关键字]]可以用于查询一个变量，表达式或类型的具体信息，该关键字返回一个 `type_info` [[类]]的对象，该类位于[[typeinfo]]头文件中。

```c++
#include<typeinfo>

using namespace std;

int main() {
    auto a {10};

    const type_info &info = typeid(a + 10);
    const char *typeName = info.name(); // i
}
```

`type_info` 类重写了 `==` [[运算符]]，并包含了对应类型的类型名等信息。