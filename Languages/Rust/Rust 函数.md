函数使用 `fn` 关键字声明，由函数名，参数列表和可选的返回值组成。函数名推荐以 _snake_case_ 即小写字母+下划线构成。

```rust
fn 函数名(形参列表) -> 返回值 {
    函数体
}
```
# 入口

`main` 函数为整个 Rust 代码的入口函数，该函数无参，返回值类型为 `()` 或 `Result<(), E>`
 - `()` 类型即无返回值函数的返回值类型，详见元组
 - `Result<(), E>` 类型类似 Java 的 `throw`，详见[[Rust 流程控制]]

```rust
fn main() {
    // do something
}
```
# 参数

参数需要参数名和参数类型，以 `,` 分割多个参数

```rust
fn fun(i: i32, c: char) {
    // do something
}
```
# 返回值

使用 `->` 指定返回值类型，在函数体中使用 `return` 提前返回值，或使用函数最后一条表达式（不带有 `;`）作为返回值。

```rust
// 返回 i32 类型 5，这里 5 没有分号，是一个表达式
fn five() -> i32 { 5 }

fn add(a: i32, b: i32) -> i32 { a+b }
```
# 函数指针

函数名可以直接作为函数指针。函数指针类型为 `fn(...)->...` 

```rust
fn add_one(x: i32) -> i32 { x + 1 }

fn do_twice(f: fn(i32) -> i32, arg: i32) -> i32 {
    f(arg) + f(arg)
}

fn main() {
    let ans = do_twice(add_one, 5);
    // Answer is 12
    println!("Answer is {}", ans);
}
```

`fn` 类型实现了 `Fn`, `FnMut`, `FnOnce`, 因此可以直接作为参数传入接受闭包的函数或者成为其返回值。
