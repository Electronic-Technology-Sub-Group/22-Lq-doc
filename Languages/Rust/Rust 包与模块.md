在大型项目中，通过不同功能划分不同代码，在项目中依赖多个其他模块和其他文件，便于管理和代码重用。对于一个一系列包组成的项目，
Cargo 提供 工作空间 Cargo Workspaces 功能。

> 作用域：代码所在的嵌套上下文的一组定义为 `in scope` 的名称
> 
> 阅读、编写、编译代码时需要知道特定的名称是否引用了变量、常量、函数、结构体、枚举、模块或其他有意义的项。同一个作用域不能包含两个相同名字的项。

Rust 用于管理代码的组织被称为 模块系统 module system，功能包括控制哪些内容可公开，哪些内容为私有，作用域名称等
 - 包 package: Cargo 的功能，允许构建、测试、分享 crate
 - Crates: 一个模块的树形结构，形成了库或二进制项目
 - 模块 Module: 允许控制作用域的路径和私有性
 - 路径 path: 命名结构体、模块、函数等项的方式
# 包和 Crate

> Crate: 一个二进制项或库
> Crate root: 一个源文件。Rust 编译器以它为起点构建 Crate 根模块

> 包: 提供一系列功能的一个或多个 Crate，包含一个 `Cargo.toml` 来阐述如何构建 Crate
 - 一个包中最多包含一个库 Crate (Library crate), 可以有任意数量的二进制 Crate (Binary crate)
 - 一个包中至少包含一个 Crate, 无论是库 Crate 还是二进制 Crate

通过 `cargo new` 命令创建一个包，通过给定项目名，Cargo 会创建一个 `Cargo.toml` 配置文件和 `src` 目录，并生成一个
`main.rs`。

根据约定，`src/main.rs` 即与包同名的二进制 Crate 的 Crate 根；若包含 `src/lib.rs` 文件，则说明包含与包名同名的库 Crate
# 模块与私有性

模块可将一个 crate 进行分组，提高代码可读性和可重用性；可控制私有性，对外部代码隐藏某些内容。

通过 `cargo new --lib [lib_name]` 创建一个库，之后在 `src/lib.rs` 使用 `mod` 定义模块

```rust
mod front_of_house {
    
    mod hosting {
        
        fn add_to_waitlist() {}
        
        fn seat_at_table() {}
    }
    
    mod serving {
        
        fn take_order() {}
        
        fn server_order() {}
        
        fn take_payment() {}
    }
}
```

访问模块中的项需要使用路径，路径分割标识符为 `::`
 - 绝对路径：从 crate 根开始，以 crate 名或字面值 `crate` 开头
 - 相对路径：以 `self`, `super` 或当前模块标识符开头

Rust 模块中默认所有项都是私有的。使用 `pub` 标记后项成为公共成员，此时可被模块外访问到。同时，父模块无法访问到子模块的私有成员，但子模块可以访问到父模块的私有成员。

若一个结构体中含有私有成员，但没有提供可以创建结构体的公共方法，则无法在模块外创建该结构体实例。

```rust
pub mod front_of_house {

    pub mod hosting {

        pub fn add_to_waitlist() {}
    }

    mod tester {
        
        fn eat_at_restaurant() {
            // 绝对路径 - 以字面量 crate 开头
            crate::front_of_house::hosting::add_to_waitlist();
            // 相对路径 - 以 self 或 super 开头（这里都用了）
            self::super::hosting::add_to_waitlist();
        }
    }
}

fn eat_at_restaurant() {
    // 相对路径 eat_at_restaurant 与 front_of_house 并列
    front_of_house::hosting::add_to_waitlist();
}
```

若 `mod` 关键字后为 `;` 而非大括号，说明该模块在与之同名同路径的 `rs` 文件中

`in src/lib.rs`
```rust
pub mod front_of_house;

pub fn eat_at_restaurant() {
    // 相对路径 eat_at_restaurant 与 front_of_house 并列
    front_of_house::hosting::add_to_waitlist();
}
```

`in src/front_of_house.rs`
```rust
pub mod hosting {

    pub fn add_to_waitlist() {}
}
```
## `use`

直接使用模块路径显得冗长且重复，可使用 `use` 将路径一次性引入到作用域中。

```rust
mod front_of_house {
    pub mod hosting {
        pub fn add_to_waitlist() {}
    }
}

use crate::front_of_house::hosting;

pub fn eat_at_restaurant() {
    hosting::add_to_waitlist();
}
```

但若两个路径中含有相同的项则无法使用，可使用 `as` 为路径指定别名以消除冲突

```rust
use std::fmt::Result;
use std::io::Result as IoResult;

fn function1() -> Result {
    // --snip--
}

fn function2() -> IoResult<()> {
    // --snip--
}
```

对于多个前缀相同的路径，可通过 `[]` 引入嵌套路径

```rust
// use std::cmp::Ordering;
// use std::io;
use std::[cmp::Ordering, io];

// use std::io;
// use std::io::Write;
use std::io::[self, Write];
```

也可以一次性将某个路径内所有共有项引入域，使用 `*`

```rust
use std::collections::*;
```
## `pub use`

使用 `pub use` 可重导出某个名称，此时可通过该路径访问重导出的成员，如

```rust
std::str::EncodeUtf16::from(/*...*/)
```

访问的实际是 `core::str::EncodeUtf16`, 可以看到 str 里面使用的大量的 `pub use`

```rust
#[stable(feature = "rust1", since = "1.0.0")]
pub use core::str::pattern;
#[stable(feature = "encode_utf16", since = "1.8.0")]
pub use core::str::EncodeUtf16;
#[stable(feature = "split_ascii_whitespace", since = "1.34.0")]
pub use core::str::SplitAsciiWhitespace;
#[stable(feature = "split_inclusive", since = "1.53.0")]
pub use core::str::SplitInclusive;
#[stable(feature = "rust1", since = "1.0.0")]
pub use core::str::SplitWhitespace;
#[stable(feature = "rust1", since = "1.0.0")]
pub use core::str::{from_utf8, from_utf8_mut, Bytes, CharIndices, Chars};
#[stable(feature = "rust1", since = "1.0.0")]
pub use core::str::{from_utf8_unchecked, from_utf8_unchecked_mut, ParseBoolError};
#[stable(feature = "str_escape", since = "1.34.0")]
pub use core::str::{EscapeDebug, EscapeDefault, EscapeUnicode};
#[stable(feature = "rust1", since = "1.0.0")]
pub use core::str::{FromStr, Utf8Error};
#[allow(deprecated)]
#[stable(feature = "rust1", since = "1.0.0")]
pub use core::str::{Lines, LinesAny};
#[stable(feature = "rust1", since = "1.0.0")]
pub use core::str::{MatchIndices, RMatchIndices};
#[stable(feature = "rust1", since = "1.0.0")]
pub use core::str::{Matches, RMatches};
#[stable(feature = "rust1", since = "1.0.0")]
pub use core::str::{RSplit, Split};
#[stable(feature = "rust1", since = "1.0.0")]
pub use core::str::{RSplitN, SplitN};
#[stable(feature = "rust1", since = "1.0.0")]
pub use core::str::{RSplitTerminator, SplitTerminator};
```
## 外部包

通过在 `Cargo.toml` 中的 `dependencies` 块可以添加外部包，Cargo 将在 [crates.io](https://crates.io/) 搜索并下载对应包

```toml
[dependencies]
rand = '0.8.4'
```

标准库 `std` 对于程序来说也是外部 crate，只是不需要在 `Cargo.toml` 中声明。
# Workspace

当项目持续增大，一个库 Crate 不断增大，可以将一个 Crate 再拆分为多个 Crate，此时可在 `Cargo.toml` 中进行配置

```toml
[workspace]
members = [
    'test_crate'
]
```

之后，在项目目录中新建 Crate:

```bash
cargo new test_crate
# Created binary (application) `test_crate` package
```

此时，项目目录中已经存在 `test_crate` 目录及对应的 `src`, `Cargo.toml` 文件，但没有 `Cargo.lock` 文件。在主项目中添加对应依赖则使用 `path`

```toml
[dependencies]
test_crate = { path='./test_crate' }
```

之后运行时，需要指定依赖的二进制包 `cargo run -p test_crate`。

同一个工作空间中只存在一个 `Cargo.lock`，也就是说只存在一份依赖列表，保证了使用的依赖版本一致性。但子 Crate 之间依赖并不共享，顶级 `Cargo.toml` 指定的依赖则可被所有子 Cargo 访问

使用 `cargo publish` 发布时，应将所有子 Cargo 分别发布。

使用 `cargo test` 进行单元测试时，在顶级目录中运行会同时测试所有子 Crate 的测试方法。

有关 Crate 相关可见 [[Rust Cargo]]
