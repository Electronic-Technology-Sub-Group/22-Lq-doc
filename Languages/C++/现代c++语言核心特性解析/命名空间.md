# 内联命名空间

c++11 内联命名空间允许将某个命名空间内的成员导出到父命名空间中

```c++
namespace A {
    inline namespace B {
        void foo();
    }

    namespace C {
        void foo();
    }
}

// 等效于 A::B::foo();
A::foo();
A::C::foo();
```

该功能主要用于对旧 lib 升级时保证兼容性使用
# 嵌套命名空间

c++17 允许将多个嵌套的命名空间缩写成一个，之间使用 `::` 连接，支持 `inline`

```c++
namespace A::B::inline C::D {
    foo();
}
```

以上代码等效于：

```c++
namespace A {
    namespace B {
        inline namespace C {
            namespace D {
                foo();
            }
        }
    }
}
```