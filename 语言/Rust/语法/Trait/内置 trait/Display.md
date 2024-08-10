> [!success]
> std​​ 标准库中所有泛型结构体都没有实现 `fmt::Display` ​​ Trait，非泛型结构体都实现了 `fmt::Display​​`

```rust
pub trait Display {
    fn fmt(&self, f: &mut Formatter<'_>) -> Result;
}
```

实现该 Trait 的结构体允许直接在格式化输出中使用 `{}​` 占位符输出，可以利用 `write!​` 向 `fmt::Formatter`​ 写入结构体内容

```rust
struct Structure(i32);

impl fmt::Display for Structure {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        write!(f, "{}", self.0)
    }
}
```