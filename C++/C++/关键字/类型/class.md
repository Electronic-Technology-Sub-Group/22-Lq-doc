用于对[[类]]的声明

- 声明类
```c++
class Foo;
```

- 声明 `C++` [[枚举]]类型
```c++
enum class E {}
```

- [[模板]]类型的类声明
```c++
template <class T>
void func(T value) {}
```

- 当作用域中存在与类名同名的变量时，用于消去歧义
```c++
class Bar {};

int main() {
    Bar Bar;
    class Bar Bar2; 
}
```