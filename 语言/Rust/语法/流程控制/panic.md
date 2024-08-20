可以使用 `panic!​` 宏生成一个 `panic`​，程序遇到 `panic`​ 时会自动展开，即对方法进行回溯并清理工作。

```rust
fn main() {
    panic!("This is a panic!");
}
```

异常信息中只标注了调用 `panic!​` 宏的位置，并给出了一个提示 `RUST_BACKTRACE=1`​ 环境变量可以阅读 `backtrace`

```
(Power Shell)
> $env:RUST_BACKTRACE = 1
> cargo run

thread 'main' panicked at 'This is a panic!', src\main.rs:2:5
stack backtrace:
   0: std::panicking::begin_panic_handler
             at /rustc/f1edd0429582dd29cccacaf50fd134b05593bd9c\/library\std\src\panicking.rs:517
   1: core::panicking::panic_fmt
             at /rustc/f1edd0429582dd29cccacaf50fd134b05593bd9c\/library\core\src\panicking.rs:100
   2: rust_demo::main
             at .\src\main.rs:2
   3: core::ops::function::FnOnce::call_once<void (*)(),tuple$<> >
             at /rustc/f1edd0429582dd29cccacaf50fd134b05593bd9c\library\core\src\ops\function.rs:227
note: Some details are omitted, run with `RUST_BACKTRACE=full` for a verbose backtrace.
error: process didn't exit successfully: `target\debug\rust_demo.exe` (exit code: 101)
```
