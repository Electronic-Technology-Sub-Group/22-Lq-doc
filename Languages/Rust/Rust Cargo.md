# 发布配置

Rust 的发布配置是预定义的一组构建参数，默认有两个 `dev` 和 `release`。当运行 `cargo build` 时，使用 dev 配置，运行
`cargo build --release` 则使用 release 配置。

在 `Cargo.toml` 中可配置发布配置 profile

```toml
[profile.dev]
opt-level = 0

[profile.release]
opt-level = 3
```
# 文档注释

Rust 中使用 `///` 声明文档注释，格式支持 Markdown。通常一个文档注释应当包含 Panics，Errors，Safety 三部分
- Panics 列举了函数可能发生的 `panic!`的场景
- Errors 若函数返回 Result，该部分描述了函数何时返回什么错误
- Safety 若函数使用了 `unsafe` 代码，这部分表示期望函数调用者支持确保 `unsafe` 块正常运行的条件

还有另一种文档注释 `//!` 声明包含该元素的上层元素的注释，主要用于 crate 根文件。

使用 `crago doc` 在 `target/doc` 文件夹中生成文档，`cargo doc --open` 生成并打开。
# 发布到 crates.io

首先，在 `crates.io` 上注册一个账号并获取一个 API token，在 cargo 登录 `cargo login [token]`。

在发布之前，需要在 `Cargo.toml` 的 `[package]` 部分添加元信息

|      信息名      | 说明                                                           |
|:-------------:|:-------------------------------------------------------------|
|     name      | 项目名，所有发布在 crate.io 的项目不可同名                                   |
|    license    | 许可，根据 [Linux 基金会](http://spdx.org/licenses/) 列出的标识符，使用 OR 分隔 |
| license-file  | 许可，若不使用 spdx 的许可，通过该字段指定许可文件                                 |
|    version    | 根据 [Semantic Versioning](https://semver.org/) 的版本号           |
|    author     | 列表，作者                                                        |
|    edition    | 编译用 Rust 版本，可选 2015 和 2018，默认 2018                           |
|  description  | 描述                                                           |

准备好后，使用 `cargo publish` 发布到 crate.io

若某个版本有重大问题或被破坏，可使用 `cargo yank --vers [version]` 撤回版本。撤回后已依赖该版本的项目仍能从 crate.io 下载该版本，但新项目无法依赖该版本。

撤回并不能删除代码，可使用 `cargo yank --vers [version] --undo` 撤回撤回（...）
# 从 crate.io 安装二进制文件

使用 `cargo install` 向本地安装对应包的二进制 crate，其安装目录是 Rust 安装目录的 `bin` 子目录
## 使用自定义命令扩展 Cargo

Cargo 允许在不修改 Cargo 的前提下向 Cargo 添加子命令。

在 $PATH 中若存在类似 `cargo-something` 的二进制文件，则可以通过 `cargo something` 的形式调用。可使用 `cargo --list` 列举所有自定义命令。
