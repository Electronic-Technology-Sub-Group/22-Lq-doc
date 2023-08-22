# 生命周期注释

向编译器提供引用如何关联泛型，允许编译器在很多情况下判断引用值的有效性，用于方法和结构体

生命周期注释使用 `'` 标记，不改变引用的生命周期，仅仅是标记了某些引用的生命周期，使编译器可以检查使用的引用是否符合。

```rust
fn main() {
    // VV
    println!("{}", largest("A", "VV"));
}

fn largest<'a>(a: &'a str, b: &'a str) -> &'a str {
    if a.len() > b.len() {
        a
    } else {
        b
    }
}
```

`<'a>` 表示一个生命周期，在函数中表示 a b 以及返回值三个引用包含相同的生命周期

在某些情况下，生命周期可以省略
 - 每个引用的参数都具有各自的生命周期
 - 若只有一个输入参数，输出与输入参数的生命周期相同
 - 若方法第一个参数为 `&self` 或 `&mut self`，输出与 `&self` 具有相同生命周期

```rust
fn first_word(s: &str) -> &str {
    s[0..1]
}

// 等效于
fn first_word2<'a>(s: &'a str) -> &'a str {
    s[0..1]
}
```
# 静态生命周期

有一种特殊的生命周期 `'static` 静态生命周期，存活于整个程序运行期间

```rust
let s: &'static str = "I have a static lifetime.";
```
