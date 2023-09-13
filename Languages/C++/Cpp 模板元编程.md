模板元编程发生在编译期。C++模板元编程主要由模板、模板特化、SFINAE 技术。以下代码表示了一个利用模板在编译期计算阶乘的方法：

```c++
#include <iostream>

using namespace std;

template<int B, int N>
struct Pow {
    enum {value = B * Pow<B, N - 1>::value};
};

template<int B>
struct Pow<B, 0> {
    enum {value = 1};
};

int main() {
    // pow(3, 5)=243
    cout << "pow(3, 5)=" << Pow<3, 5>::value << endl;
}
```
