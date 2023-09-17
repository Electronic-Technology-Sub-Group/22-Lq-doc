Rust 提供一系列用于编写测试的功能，包括 `test` 属性，一些宏和 `should_panic` 属性
# 测试函数

`cargo new` 创建的项目会自动生成一个 `tests` module，为测试函数的样板。一个测试函数应当以 `#[test]` 属性注释，之后可通过 `cargo test` 运行测试

```rust
#[test]
fn a_test_method() {
   // do something
}
```
# 测试结果检测

当测试不通过应触发 `panic` 即可，Rust 提供了 `assert!`, `assert_eq!`, `assert_ne!` 宏用来进行比较。

若测试结果为 触发 `panic` 则通过，需要对方法使用 `#[should_panic]` 属性。

```rust
#[test]
#[should_panic]
fn a_test_method() {
   // do something
   panic!("Test pass");
}
```
# 运行测试

使用 `cargo test` 启动测试，Cargo 测试也有若干参数

|         属性         | 说明                                |
|:------------------:|:----------------------------------|
| `--test-threads=n` | 使用 n 个线程并行测试                      |
|   `--nocapture`    | 显示 print 和 println 的输出            |
|    method_name     | 运行特定测试，所有测试方法中以 method_name 开头的方法 |
|    `--ignored`     | 不执行有 `#[ignore]` 属性的测试方法          |
# 测试模块

以 `#[cfg(test)]` 注解的模块表示该模块只有在执行 `cargo test` 时才编译和运行的代码，而 `cargo build` 时不运行。
在测试函数中允许使用私有函数，即测试代码可以测试私有函数。
# 集成测试

集成测试用于测试库的共有 API，其对于项目来说是一个独立库。集成测试代码位于 `tests` 文件夹中，该文件夹与 `src` 同级。

不需要在 `tests` 中使用 `#[cfg(test)]`，Cargo 可自动识别只有在 `cargo test` 时运行。
