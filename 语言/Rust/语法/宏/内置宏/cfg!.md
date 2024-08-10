仅当调试时才执行内部的代码：

```rust
if cfg!(debug_assertions) {
    // do something
}
```
