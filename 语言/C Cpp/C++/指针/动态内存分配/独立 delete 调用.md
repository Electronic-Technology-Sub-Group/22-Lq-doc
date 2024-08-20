---
语言: cpp
语法类型: 高级语法
---
#cpp20 

> [!note] 使用场景
> 使用 delete 时自定义[[../../类/析构函数|析构函数]]调用时间 

允许通过自定义运算符时添加 `std::destorying_delete_t` 参数，禁止在 `delete` 运算符前调用析构函数。此时应该手动在合适的位置调用析构函数。

> [!note] `std::destorying_delete_t` 参数本身没有用途，只是向编译器发出通知。

```cpp
struct X {
    ~X() {
        cout << "dtor X\n";
    }

    void* operator new(size_t s) {
        return ::operator new(s);
    }

    // 这里添加 std::destroying_delete_t 参数 ↓↓↓
    void operator delete(X* ptr, std::destroying_delete_t) {
        cout << "delete X\n";
        ptr->~X(); // ← 手动调用析构函数
        ::operator delete(ptr);
    }
};

int main() {
    X *x = new X;
    // delete X
    // dtor X
    delete x;
    return 0;
}
```