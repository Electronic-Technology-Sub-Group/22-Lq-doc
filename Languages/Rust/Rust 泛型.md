泛型是具体类型或其他属性的抽象替代。

```rust
// 方法
fn get_first<T>(list: &[T]) -> &T {
    // ...
    list.get(0).unwrap()
}

// 结构体
struct Point<T, U> {
    x: T,
    y: U
}

impl<T, U> Point<T, U> {
    // ...
}

// 枚举
enum Option<T> {
    None,
    Some<T>
}
```

Rust 通过泛型代码的单态化实现了泛型使用时性能零损耗（相当于 C++ 的模板特化）
- 单态化：通过填充编译时使用的具体类型，将通用代码转化为特定代码的过程。

可以给泛型增加默认值

```rust
struct A;

// 默认 T=实现trait的类型，V=i32
trait B<T=Self, V=i32> {
    fn c(&self, p: T) -> V;
}

impl B for A {
    fn c(&self, p: Self) -> i32 {
        todo!()
    }
}

impl B<u32> for A {
    fn c(&self, p: u32) -> i32 {
        todo!()
    }
}
```
