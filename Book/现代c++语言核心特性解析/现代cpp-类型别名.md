# 类型别名

C++ 11 后，可以使用 `using` 声明类型别名，类似 `#define` 声明别名。相比 `#define`，`using` 允许声明函数指针、数组和带模板的类型的别名

```c++
// 普通类型: using 别名 = 类型名
using LL = long long;
// 函数指针: 类型名那里将变量名省略
using op = int(*)(int, int);
// 指针数组
using ps = int*[10];
// 模板类型
template<typename T>  
using vec = std::vector<T>;

int main() {
    // 实例：函数指针
    int add(int a, int b);
    op p_add = add;
    // 实例: 模板类型
    vec<int> v;
    v.push_back(10); 
}
```