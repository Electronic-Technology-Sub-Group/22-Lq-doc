#cpp20 

C++参数依赖查找。当编译器对无限定域的函数调用进行名字查找时，除了当前名字空间域以外，也会把函数参数类型所处的名字空间加入查找的范围。

编译器 ADT 规则支持在参数命名空间中查找带显示指定模板实参的函数模板。

```c++
int h = 0;
void g() {};
namespace N {
    struct A{};
    template<class T> void f(T) {};
    template<class T> void g(T) {};
    template<class T> void h(T) {};
}

int main() {
    // 成功
    f<N::A>(N::A());
    // 成功
    g<N::A>(N::A());
    // 失败：存在变量 h，可被解析成 h <N::A > (N::A())，存在歧义
    h<N::A>(N::A());
    return 0;
}
```
