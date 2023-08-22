# if

```rust
fn condition1() -> bool { true }
fn condition2() -> bool { true }
fn condition3() -> bool { false }

fn fun_if() {
    if condition1() {
        println!("In condition 1");
    } else if condition2() {
        println!("In condition 2");
    } else if condition3() {
        println!("In condition 3");
    } else {
        println!("In else");
    }
}
```

`if` 语句是一个表达式，因此也可以用于赋值等

```rust
fn condition1() -> bool { true }
fn condition2() -> bool { true }
fn condition3() -> bool { false }

fn fun_if() {
    let str = if condition1() {
        "In condition 1"
    } else if condition2() {
        "In condition 2"
    } else if condition3() {
        "In condition 3"
    } else {
        "In else"
    }
}
```

# match

分支使用 `match` 进行模式匹配，详细使用方法见后续[[Rust 模式匹配]]

```rust
match a {
    1 => println!("a=1"),
    2 => println!("a=2"),
    3 => println!("a=3"),
    4 => println!("a=4"),
    _ => {}
}
```
# 循环

任何循环结构都可以通过 `break` 跳出循环，或通过 `continue` 开始下次循环。

当有多层循环时，可添加标签选择跳出的循环，标签以 `'` 开头

```rust
let mut count = 0;
'continue_up: loop {
    println!("count={}", count);
    let mut remaining = 10;
    loop {
        println!("remaining={}", remaining);
        if remaining == 9 {
            break;
        }
        if count == 2 {
            break 'continue_up;
        }
        remaining -= 1;
    }
    count += 1;
}
println!("End count={}", count)
```

当循环有返回值时，可使用 break 后加表达式

```rust
let mut counter = 0;
let result = loop {
    counter += 1;
    if counter == 10 {
        break counter * 2;
    }
};
println!("Result={}", result); // 20
```
## loop

`loop` 表示无限循环，需要手动使用 `break` 跳出

```rust
loop {
    // ...
}
```

可使用 `if`, `loop` 组合实现其他语言 `while` 的效果

```rust
fn condition() -> bool { true }

fn while_exp() {
    while condition() {
        // do something
    }
}

fn loop_exp() {
    // while 可以通过 loop if 实现
    loop {
        if !condition() {
            break;
        }
        
        // do something
    }
}
```
## for

`for in` 循环主要用于数组、列表等可迭代变量

```rust
fn main() {
    // 遍历数组
    let arr = [10, 20, 30, 40, 50];
    for element in arr.iter() {
        println!("Value is {}", element);
    }
    
    // 遍历 Range + 倒序
    for num in (1..4).rev() {
        println!("Value is {}", num);
    }
}
```
# 错误处理

Rust 中没有异常，只有可恢复错误 `Result<T, E>` 和不可恢复错误 `panic!`, 当遇到 `panic!` 则程序必定退出。
（`Result<T, E>` 类似 Java 中 catch 的 Exception，`panic!` 类似 Error 和未被 catch 的 Exception）
## panic

可以使用 `panic!` 宏生成一个 `panic`，程序遇到 `panic` 时会自动展开，即对方法进行回溯并清理工作。

```rust
fn main() {
    /* thread 'main' panicked at 'This is a panic!', src\main.rs:2:5
     * note: run with `RUST_BACKTRACE=1` environment variable to display a backtrace
     * error: process didn't exit successfully: `target\debug\rust_demo.exe` (exit code: 101)
     */
    panic!("This is a panic!");
}
```

异常信息中只标注了调用 `panic!` 宏的位置，并给出了一个提示 `RUST_BACKTRACE=1` 环境变量可以阅读 backtrace

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
## Result

`Result<T, E>` 是一个枚举，包含 `Ok` 和 `Err` 两个值。

```rust
use std::fs::File;

fn main() {
    let f = File::open("hello.txt");

    // Can't open file because Os { code: 2, kind: NotFound, message: "系统找不到指定的文件。" }
    let f = match f {
        Ok(file) => file,
        Err(error) => panic!("Can't open file because {:?}", error)
    };
}
```

`Result` 存在一系列简写方法: `unwarp` 系列方法和 `expect` 方法，`expect` 允许自定义错误信息

```rust
use std::fs::File;

fn main() {
    // 解包，若有错误则执行闭包
    let f0 = File::open("hello0.txt").unwrap_or_else(|error| {
        // Warn: 系统找不到指定的文件。 (os error 2)
        println!("Warn: {}", error);
        File::create("hello0.txt").unwrap()
    });

    // 解包，若有错误直接通过 panic 抛出
    // thread 'main' panicked at 'called `Result::unwrap()` on an `Err` value: Os { code: 2, kind: NotFound, message: "系统找不到指定的文件。" }'
    let f1 = File::open("hello1.txt").unwrap();
}
```

传递异常也是很常见的操作，将异常传递给外部方法处理

```rust
fn read_from_file(file: &str) -> Result<String, io::Error> {
    let f = File::open(file);

    let mut f = match f {
        Ok(file) => file,
        // 传递 1
        Err(error) => return Err(error)
    };

    let mut result = String::new();
    match f.read_to_string(&mut result) {
        Ok(_) => Ok(result),
        // 传递 2
        Err(e) => Err(e)
    }
}
```

此时可通过 `?` 运算符简写，函数返回值必须是 Result 类型。`main()` 函数允许返回 `Result<(), E>` 类型

```rust
fn read_from_file(file: &str) -> Result<String, io::Error> {
    // 传递 1
    let mut f = File::open(file)?;
    let mut result = String::new();
    // 传递 2
    f.read_to_string(&mut result)?;
    Ok(result)
}
```

`?` 支持链式调用

```rust
fn read_from_file(file: &str) -> Result<String, io::Error> {
    let mut result = String::new();
    //            传递 1                        传递 2
    File::open(file)?.read_to_string(&mut result)?;
    Ok(result)
}
```
