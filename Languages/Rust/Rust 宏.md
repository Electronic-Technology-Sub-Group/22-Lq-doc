宏 Macro 是 Rust 提供的一系列功能，包括一种声明宏和三种过程宏。 从根本上说宏是为写其他代码而写代码的方式，即元编程。
 - 使用 `macro_rules!` 的声明宏
 - 自定义 `#[derive]` 宏在结构体和枚举上指定通过 `derive` 属性添加的代码
 - 类属性宏定义用于任意项目的自定义属性
 - 类函数宏类似函数，但作用于作为参数传递的 `token` 

元编程可用于减少大量代码编写和维护代码，在某些位置也扮演了函数的作用。其与函数的差异在于
 - 函数必须声明固定的参数列表和返回值，宏则可以接受不同数量的参数
 - 宏可以在编译器翻译代码之前展开，如给一个给定类型实现 trait 等
 - 实现宏比函数更复杂，因为要间接生成代码，因此比直接写代码更加复杂 更难阅读
 - 在文件中调用宏之前必须定义或引用它，而函数则可以在任何地方定义和调用
# 声明宏

声明宏允许编写一些类似 Rust `match` 的代码

```rust
// 一个 vec! 简化版本
#[macro_export]
macro_rules! vec {
    ($(x:exper), *) => {
        {
            let mut temp_vec = Vec::new();
            $(
            temp_vec.push($x);
            )*
            temp_vec
        }
    };
}

fn main() {
    let v1 = vec![1, 2, 3];
    
    // 等同于
    let v2 = {
        let mut temp_vec = Vec::new();
        temp_vec.push(1);
        temp_vec.push(2);
        temp_vec.push(3);
        temp_vec
    };
}
```
 - `#[macro_export]` 表示该宏是可见的，否则不可能被外部通过导入包引入到作用域。
 - 声明宏名称：`macro_rules! 宏名称` 
 - 大括号内语法类似 `match` 表达式的结构，`=>` 左面括号内为一个单边模式，右边为替代的代码
	 - 模式以 `$` 开头，`x:exper` 表示匹配一个表达式，并在右边可以以 `$x` 替代
	 - 第一个 `$` 外后接一个 `,`，表示后面可有 0 或 1 个 `,`
	 - `*` 表示匹配 0 次或多次之前的模式，在右边可以使用 `$()` 循环访问
# 过程宏

过程宏以 Rust 代码作为输入，在这些代码中操作后产生另一些代码作为输出（`TokenStream`）。过程宏包括自定义派生(derive)，类属性和类函数。自定义派生宏适用于结构体和枚举，一般过程为

 1. 在根项目中声明 trait

```rust
pub trait HelloMacro {
    fn hello_macro();
}
```

 - 过程宏必须在自己的 crate 内，因此需要创建一个新的 lib `cargo new hello_macro_derive --lib`
 - 在 `hello_macro_derive/Cargo.toml` 中声明过程宏 crate 及一些常用的辅助库

```toml
# 声明过程宏 crate
[lib]
proc-macro = true

[dependencies]
# 将字符串中的 Rust 代码解析成 AST
syn = "1.0.82"
# 将 syn 解析的 AST 重新转化为 Rust 代码
quote = "1.0.10"
```

 2. 在 `hello_macro_derive/src/lib.rs` 中声明导出宏，并创建对应函数处理代码

```rust
// 至 1.31.0 仍需要 extern
extern crate proc_macro;

use proc_macro::TokenStream;
use quote::quote;
use syn::DeriveInput;

// 实现函数
#[proc_macro_derive(HelloMacro)]
pub fn hello_macro_derive(input: TokenStream) -> TokenStream {
   println!("{}", input.to_string());
   let ast: DeriveInput = syn::parse(input).unwrap();
   // impl_hello_macro
   // name 可在实现中使用 #name 引用
   let name = &ast.ident;
   // 构建新代码
   let gen = quote! {
      impl HelloMacro for #name {
            fn hello_macro() {
                // stringify! 宏使输入的表达式直接转化为字符串，如 stringify!(1+2) => "1+2"
                println!("Hello, Macro! My name is {}", stringify!(#name));
            }
        }
    };
   gen.into()
}
```

 3. 在代码中使用

```rust
use rust_demo::HelloMacro;
use hello_macro_derive::HelloMacro;

#[derive(HelloMacro)]
struct Pancakes;

fn main() {
   // Hello, Macro! My name is Pancakes
    Pancakes::hello_macro();
}

```
# 类属性宏

类似于自定义派生宏，类属性宏用来可创建新属性，可用于任意项，包括函数等

```rust
#[route(GET, "/")]
fn index() {
   // do something
}
```

类属性宏的函数接受两个 `TokenStream`，第一个表示属性本身，即 `GET, "/"`，第二个代表属性标记的项，即 `fn index() {...}`

```rust
#[proc_macro_attribute]
pub fn route(attr: TokenStream, item: TokenStream) -> TokenStream {
   // ...
}
```
# 类函数宏

类函数宏类似于过程宏，使用 `TokenStream` 实现过程宏的功能

```rust
let result = sql!(SELECT * FROM ports WHERE id=1);
```

其函数应定义如此

```rust
#[proc_macro]
pub fn sql(input: TokenStream) -> TokenStream {
   // do something
}
```