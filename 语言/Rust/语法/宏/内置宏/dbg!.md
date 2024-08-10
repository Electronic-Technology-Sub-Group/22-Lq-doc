使用 `dbg!()` ​ 宏，接受一个表达式，输出宏调用的文件、行号、表达式结果。

该方法输出在 `stderr`​ 中

```run-rust
#[derive(Debug)]
struct Rectangle {
    width: u32,
    height: u32
}

fn main() {
    let rect = Rectangle { width: 30, height: 50 };
    dbg!(&rect);
}
```
