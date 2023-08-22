结构体类似于一个为每个值命名的元组，使用 `struct` 声明，使用 `,` 分隔，最后一个量的 `,` 可保留

```rust
struct User {
    username: String,
    email: String,
    sign_in_count: i64,
    active: bool,
}
```

在代码中，使用 `结构体名 {}` 创建结构体，并在大括号中使用 `key: value` 形式赋值

```rust
let user = User {
    username: String::from("some_user_123"),
    email: String::from("some_user_123@email.com"),
    sign_in_count: 1,
    active: true,
};
```

在字段名与参数名相同时，可以使用字段初始化的简化写法

```rust
fn main() {
    let username = String::from("some_user_123");
    let user = build_user(username);
    // some_user_123: email=some_user_123@email.com, active=true
    println!("{}: email={}, active={}", user.username, user.email, user.active);
}

fn build_user(username: String) -> User {
    let email = String::from(&username) + "@email.com";
    User {
        // 字段初始化简化写法
        username,
        email,
        sign_in_count: 1,
        active: true,
    }
}
```

若需要从一个旧的结构体复制出一个新的结构体，可使用结构体更新语法，使用 `..` 指定剩余未显式设置字段使用的旧结构体，但此时旧结构体若没有实现 `Copy` 则不再可用，就像 `a=b` 一样

```rust
fn main() {
    let unactivated_user = User {
        username: String::from("a_user"),
        email: String::from("u@email.com"),
        sign_in_count: 1,
        active: false
    };

    let activated_user = User {
        active: true,
        sign_in_count: unactivated_user.sign_in_count + 1,
        ..unactivated_user
    };
}
```
# 元组结构体

使用元组作为结构体，有着结构体名称提供的含义，但没有具体字段名，使用 `struct name(type, type, ...)` 声明。

```rust
struct Point(f64, f64, f64);
struct Color(u16, u16, u16, u16);

fn main() {
    let point = Point(0.0, 5.2, 3.5);
    let color = Color(0xF0, 0xF2, 0xEE, 0xFF);
}
```

元组结构体与元组相似，可通过解构或下标访问
# 类单元结构体

没有任何字段的结构体称为类单元结构体，常用于要在某个类型上实现某个 `trait` 但又不想附加任何数据的情况

```rust
struct AlwaysEqual;

fn main() {
    let subject = AlwaysEqual;
}
```
# 结构体所有权

结构体定义中，我们使用了拥有自身所有权的 `String` 而非 `&str`，由此可以保证结构体拥有该成员的所有权。

若想要使用引用需要指定生命周期，在结构体中存储一个引用而不指定生命周期将是无效的。

```rust
struct User {
    //   |
    // 2 |     username: &str,
    //   |               ^ expected named lifetime parameter
    //   |
    // help: consider introducing a named lifetime parameter
    //   |
    // 1 ~ struct User<'a> {
    // 2 ~     username: &'a str,
    //   |
    username: &str,
    email: &str,
    sign_in_count: i64,
    active: bool,
}

fn main() {
    let user = User {
        username: "a_user",
        email: "u@email.com",
        sign_in_count: 1,
        active: false
    };
}
```
# 输出结构体

`println!()` 宏可以处理很多类型，但直接输出结构体则需要结构体实现 `Display` （接口？ trait，暂且这么说吧），若直接输出可以看到很多提示

```rust
// the trait `std::fmt::Display` is not implemented for `Rectangle`
// in format strings you may be able to use `{:?}` (or {:#?} for pretty-print) instead
// this error originates in the macro `$crate::format_args_nl` (in Nightly builds, run with -Z macro-backtrace for more info)
println!("The area of the rectangle is {} square pixels.", rect);
```

可以看到，使用 `{:?}` 或 `{:#?}` 可以用于输出。若直接使用则又会提示

```rust
// help: the trait `Debug` is not implemented for `Rectangle`
// note: add `#[derive(Debug)]` to `Rectangle` or manually `impl Debug for Rectangle`
// note: this error originates in the macro `$crate::format_args_nl` (in Nightly builds, run with -Z macro-backtrace for more info)
println!("{:?}", rect);
```

因此我们需要配合 `#[derive(Debug)]` 标记使用。

```rust
#[derive(Debug)]
struct Rectangle {
    width: u32,
    height: u32
}

fn main() {
    let rect = Rectangle { width: 30, height: 50 };

    // Rectangle { width: 30, height: 50 }
    println!("{:?}", rect);
    
    // Rectangle {
    //     width: 30,
    //     height: 50,
    // }
    println!("{:#?}", rect);
}
```

除此之外，还可以使用 `dbg!()` 宏，该宏接受一个表达式的所有权，输出宏调用的文件和行号，返回表达式结果。该方法输出在 `stderr` 中

```rust
#[derive(Debug)]
struct Rectangle {
    width: u32,
    height: u32
}

fn main() {
    let rect = Rectangle { width: 30, height: 50 };

    // [src\main.rs:10] &rect = Rectangle {
    //     width: 30,
    //     height: 50,
    // }
    dbg!(&rect);
}
```
# 结构体方法

与函数类似，方法可以有参数和返回值，不同于函数的是需要在结构体上下文中定义，且第一个参数始终是 `self`。

定义结构体方法需要把方法放入一个 `impl` 块中（implementation）。方法第一个参数可以是 `self`, `&self`, `*self`, `&mut self` 等。与 C 语言的 `->` 运算符不同，Rust 在结构体变量使用 `.` 运算符时会自动根据方法推断出调用者的签名与其匹配，即 自动引用和解引用。

```rust
struct Rectangle {
    width: u32,
    height: u32
}

impl Rectangle {
    fn area(&self) -> u32 {
        self.width * self.height
    }
}

fn main() {
    let rect = Rectangle { width: 30, height: 50 };
    // The area of the rectangle is 1500 square pixels.
    println!("The area of the rectangle is {} square pixels.", rect.area());
}
```

在 `impl` 块中定义的且不以 `self` 为第一参数的函数称为关联函数。通常这些函数用作结构体的构造函数，如 `String::from`。`impl` 块可以有多个。

```rust
impl Rectangle {
    fn square(size: u32) -> Rectangle {
        Rectangle { width: size, height: size }
    }
}

fn main() {
    let square = Rectangle::square(20);
}
```
