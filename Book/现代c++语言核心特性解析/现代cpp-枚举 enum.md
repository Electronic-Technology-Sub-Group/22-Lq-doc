# 枚举底层类型

C++11 允许使用 `:` 指定传统枚举的底层类型

```c++
enum E: unsigned int {
    e1, e2, e3
};
```
# 强枚举类型

传统枚举存在以下问题：
- 安全检查问题：枚举值可以隐式转换为整形，破坏了 C++ 类型安全
- 作用域问题：枚举会将所有枚举值导出到所在作用域，可能造成潜在的命名冲突

C++ 11 提供强枚举类型支持，使用 `enum class` 声明，具有以下特点：

- 属于[[Cpp 特殊类型#POD 类型|POD 类型]]
- 枚举标识符属于枚举类型的作用域

```c++
enum class E {
    e1, e2, e3
};

int main() {
    // error: 'e1' was not declared in this scope; did you mean 'E::e1'?
    E e = e1;
    return 0;
}
```

- 枚举值不会隐式转换为整形；可以进行强制类型转换（`static_cast`）

```c++
enum class E {
    e1, e2, e3
};

int main() {
    // error: cannot convert 'E' to 'int' in initialization
    int ve = E::e1;
    // 应该这么用
    int v =  static_cast<int>(E::e1);
    E e = static_cast<E>(0);
    return 0;
}
```

- 允许指定底层类型，使用 `:` 指定，默认为 int

```c++
enum class E: unsigned int {
    e1 = 0,
    e2 = 1,
    // error: enumerator value '-1' is outside the range of underlying type 'unsigned int'
    e3 = -1
};
```
## {} 初始化

自 C++ 17 开始，具有底层类型的类型（主要是枚举）支持使用 `{}` 初始化 - 包括初始化不存在的枚举值

```c++
enum class E {};

int main() {
    E e0 {0};
    E e1 = E{1};
    return 0;
}
```

该功能的使用场景为：
- 需要一个整形类型
- 该类型需要与其他整形区分（不能互相隐式转换）
## 打开枚举空间

自 C++ 20 开始，使用 `using` 可以打开枚举命名空间 - 即将特定或所有枚举值导出到所在作用域
- `using enum 枚举名`
- `using 枚举名::枚举值`

```c++
enum class E { e1, e2, e3 };

int main {
    E e;
    // ...
    switch (e) {
        using enum E;
        case e1:break;
        case e2:break;
        case e3:break;
        default:;
    }
}
```