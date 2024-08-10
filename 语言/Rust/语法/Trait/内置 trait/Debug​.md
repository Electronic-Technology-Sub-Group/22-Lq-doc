​`fml::Debug​` Trait 允许自动推断创建对应的结构体实现，只需要标注一下即可。

> [!note]
> 所有 std​​ 库的结构体都实现了 `fml::Debug​​`

```rust
// derive 属性会自动创建所需的实现，使这个 struct 能使用 fmt::Debug 打印。
#[derive(Debug)]
struct DebugPrintable(i32);
```
