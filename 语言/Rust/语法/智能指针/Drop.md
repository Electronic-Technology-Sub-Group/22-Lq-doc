实现 `Drop`​ 接口后可自定义离开作用域时执行的代码，类似 C++ 的析构函数

```rust
impl<T> Drop for MyBox<T> {

    fn drop(&mut self) {
        // 一个假的释放方法
        println!("{}", "Pointer dropped!");
    }
}

fn main() {
    let s = MyBox(String::from("abc"));
}
// Pointer dropped!
```

我们无法直接调用 `drop`​ 方法释放一个数据。如果要手动释放的话，需要使用 `std::mem::drop`