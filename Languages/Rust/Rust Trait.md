`Trait` 类似接口，定义了泛型行为方法，也支持默认实现

```rust
pub trait Summary {
    // 行为方法
    fn summarize(&self) -> String;

    // 默认实现
    fn summarize_more(&self) -> String {
        String::from("(Read More...)")
    }

    // 使用 self
    fn print(&self) {
        println!("{}", self.summarize())
    }
}
```
# Trait 与结构体

在结构体上实现时，分别针对不同结构体创建 `trait` 的 `impl`，使用 `for` 标注结构体

```rust
pub struct News {
    pub headline: String,
    pub location: String,
    pub author: String,
    pub content: String,
}

impl Summary for News {
    fn summarize(&self) -> String {
        format!("{} by {}({})", self.headline, self.author, self.location)
    }
}

pub struct Tweet {
    pub username: String,
    pub content: String,
    pub reply: bool,
    pub retweet: bool,
}

impl Summary for Tweet {

    fn summarize(&self) -> String {
        format!("{}: {}", self.username, self.content)
    }
}
```

在函数中，使用 `impl trait名` 作为参数类型表示某参数需要实现某 `trait`

```rust
fn main() {
    let tweet = Tweet {
        username: "a".to_string(),
        content: "m".to_string(),
        reply: false,
        retweet: false
    };
    // a: m
    notify(&tweet)
}

pub fn notify(msg: &impl Summary) {
    msg.print()
}
```

若要同时实现多个 `trait`，使用 `+` 连接

```rust
pub fn notify(msg: &impl Summary + Display + Clone) {
    // do something
}
```

在更复杂的情况下，可使用 `trait bound`（泛型）

```rust
pub fn notify2<T: Summary + Display>(item1: &T, item2: &T) {
    // do something
}

// 等效于
pub fn notify22(item1: &impl Summary + Display, item2: &impl Summary + Display) {
    // do something
}
```

可使用 `where` 将 `trait bound` 滞后设置

```rust
//            trait bound                               具体类型
pub fn notify3<T, U>(item1: &T, item2: &T) -> i32 where T: Summary + Display, U: Clone + Display {
    // do something
    0
}
```

使用 `trait bound` 可以针对某些特定的泛型类型实现结构体方法

```rust
use std::fmt::Display;

struct Point<T> { x: T, y: T }

impl<T> Point<T> {
    fn new(x: T, y: T) -> Point<T> {
        Point { x, y }
    }
}

impl<T: Display + PartialOrd> Point<T> {
    fn cmp_display(&self) {
        if self.x >= self.y {
            println!("The largest number is {}", self.x);
        } else {
            println!("The largest number is {}", self.y);
        }
    }
}

fn main() {
    let p = Point::new(3, 5);
    // The largest number is 5
    p.cmp_display();
}
```

当函数返回一个 `trait` 时同样可以使用 `impl` 或 `trait bound`

```rust
pub fn new_tweet<T: Summary>(username: String, content: String) -> T {
    Tweet {
        username,
        content,
        reply: false,
        retweet: false
    }
}
```

但由于 Rust 的泛型机制（单态化）无法在一个方法中返回不同的类型

```rust
pub fn new_summary<T: Summary>(username: String, content: String, is_news: bool) -> T {
    if is_news {
        News {
            headline: "headline".to_string(),
            location: "location".to_string(),
            author: username,
            content
        }
    } else {
        Tweet {
            username,
            content,
            reply: false,
            retweet: false
        }
    }
}

fn main() {
    let s = new_summary("a".to_string(), "b".to_string(), false);
    //  ^ consider giving `s` a type
}
```

若就是想返回不同的结构体，则可以使用 `Box<dyn T>`

```rust
fn main() {
    // Box<dyn Summary>
    let s = new_summary("a".to_string(), "b".to_string(), false);
    // a: b
    s.print();
}

pub fn new_summary(username: String, content: String, is_news: bool) -> Box<dyn Summary> {
    if is_news {
        Box::new(News {
            headline: "headline".to_string(),
            location: "location".to_string(),
            author: username,
            content
        })
    } else {
        Box::new(Tweet {
            username,
            content,
            reply: false,
            retweet: false
        })
    }
}
```
# 关联类型

将类型占位符与 trait 相关联，因此可以在方法签名中使用这些占位符类型，使用 `type` 声明，使用 `Self` 访问。`Self` 指向实现了当前 trait 的类型。

```rust
pub trait Iterator {
    type Item;
    
    fn next(&mut self) -> Option<Self::Item>;
}

impl Iterator for Counter {
    type Item = u32;

    fn next(&mut self) -> Option<Self::Item> {
        // do something
    }
}
```

与泛型不同的是，关联类型无法多次实现。若是 `fn next -> Option<T>` 的形式，则必须对每个类型实现一个 trait，如

```rust
pub struct Counter;

pub trait Iterator {
    type Item;

    fn next(&mut self) -> Option<Self::Item>;
}

impl Iterator<i32> for Counter {
    fn next(&mut self) -> Option<i32> {
        todo!()
    }
}

impl Iterator<u32> for Counter {
    fn next(&mut self) -> Option<u32> {
        todo!()
    }
}

impl Iterator<String> for Counter {
    fn next(&mut self) -> Option<String> {
        todo!()
    }
}
```

显然这不可能覆盖所有类型，而是用关联类型则可以实现

```rust
pub struct Counter;

pub trait Iterator {
    type Item;
    
    fn next(&mut self) -> Option<Self::Item>;
}

impl<T> Iterator for Counter {
    
    type Item = T;

    fn next(&mut self) -> Option<T> {
        todo!()
    }
}
```
# 同名函数

Rust 不能避免实现的 trait 具有同名方法，也不能阻止为同一个结构体实现这样的 trait 或在 `impl` 上实现与 trait 同名方法，因此访问这种方法时需要使用完全的限定符：

`<StructType as TraitType>::method_name(...)`

```rust
struct A;

trait Fn1 {
    fn fun();
}

impl A {
    fn fun() { println!("Fun in A"); }
}

impl Fn1 for A {
    fn fun() { println!("Fun in fn1"); }
}

fn main() {
    A::fun();
    <A as Fn1>::fun();
}
```
# trait 继承

尽管结构体无法继承，但 trait 还是可以有继承关系的。使用 `:` 声明继承关系

```rust
trait A: fmt::Display {
    fn fun_a();

    fn outline_print(&self) {
        let output = self.to_string();
        let len = output.len();
        println!("{}", "*".repeat(len + 4));
        println!("*{}*", " ".repeat(len + 2));
        println!("* {} *", output);
        println!("*{}*", " ".repeat(len + 2));
        println!("{}", "*".repeat(len + 4));
    }
}
```
# newtype 模式

`impl` 有一个限制是 结构体或 trait 需要在当前 crate 中，如要在 `Vec<T>` 上实现 `Display` 是不允许的，因为 `Vec<T>` 和 `Display` 都不在当前包中。

若的确要实现，可使用元组结构体封装，因为元组结构体对于 crate 是本地的。为了可以在其上使用 `Vec<T>` 的方法，可实现 `Deref`，详见智能指针。

```rust
use std::fmt;
use std::fmt::{format, Formatter, write};
use std::ops::Deref;

struct VecWrapper(Vec<String>);

impl fmt::Display for VecWrapper {
    fn fmt(&self, f: &mut Formatter<'_>) -> fmt::Result {
        write!(f, "[{}]", self.0.join(", "))
    }
}

impl Deref for VecWrapper {
    type Target = Vec<String>;

    fn deref(&self) -> &Self::Target {
        &self.0
    }
}

fn main() {
    let vec = vec!["A".to_string(), "B".to_string(), "C".to_string()];
    let d = VecWrapper(vec);
    println!("{}", d);
}
```
# 动态大小类型

动态大小类型：dynamically sized types, DST 或 unsized type。需要根据实际的值确定内存大小的类型，在声明时无法确定需要的内存占用。这类类型允许我们处理只有在运行时才知道大小的类型。

动态大小类型的一个规则是 必须将动态大小类型的值置于某种指针之后，他们有一些额外的元信息来存储动态信息大小。

一个很常见的动态大小类型为 `str`, 即 字符串。当我们直接使用字符串字面量时获得的是一个
`&str` 类型值，这个指针不同于普通指针 - 他存储了 `str` 值指向的内存和大小。`str` 可以直接与 `Box<T>`, `Rc<T>` 等智能指针结合。

另一个常用的 DST 是 trait。使用 trait 对象时可使用 `dyn` 之后，如 `&dyn Trait` 或 `Box<dyn Trait>` 等。

为了处理 DST，Rust 提供了一个特殊的 trait - `Sized`，该 trait 为编译器提供了一个编译时获取类型大小的实现。另外 Rust 隐式为每个泛型增加了 `Sized` bound。

```rust
fn generic<T>(t: T) {}

// 等同于

fn generic2<T: Sized>(t: T) {}
```

可使用 `?` 移除该 trait

```rust
fn generic<T: ?Sized>(t: &T) {}
```

`?Sized` 表示 T 可能不是 Sized，但由于 DST 只能在某个指针之后使用，因此参数中使用了 `&T`。
# trait 对象

面向对象的程序是由对象组成的，一个对象包含数据和操作这些数据的过程，这些过程通常被称为方法或操作。虽然 Rust 结构体和枚举不称为对象，但提供了与对象相同的功能

Rust 无法定义结构体继承父类结构体数据，但可以定义相同的行为 `trait`。当对象安全时可认为是 trait 对象，可使用 `Box<dyn T>`
- 方法返回值不包含 `Self`
- 方法没有任何泛型类型参数
# 运算符重载

大多数运算符重载只要实现对应的 Trait 即可，详见 [[#运算符 Trait]]

```rust
struct A {
    a: i32;
}

impl Add for A {
    type Output = A;

    fn add(self, rhs: Self) -> Self::Output {
        let a = self.a + rhs.a;
        A { a }
    }
}
```
# 内置 trait
## 功能性 Trait

|  Trait  | 说明                                                                                                        |
|:-------:| ----------------------------------------------------------------------------------------------------------- |
|  Debug  | 允许在调试时通过 `{:?}` 输出实例信息                                                                        |
|   Eq    | 必须同时实现 PartialEq, 表示 this == this。`HashMap<K, V>` 中的 K 必须实现 Eq                               |
|  Clone  | 实现 `clone()` 方法，允许数据进行深拷贝。只有在所有成员都实现了 Clone 的结构体上才能实现 Clone              |
|  Copy   | 允许通过只拷贝栈上的位来复制而不需要额外的代码。可以假设复制的速度很快。任何使用 Copy 都可以使用 Clone 实现 |
|  Hash   | 允许通过 hash 函数生成一个固定大小的值，其字段必须全部实现 Hash。`HashMap<K, V>` 的 K 必须实现 Hash         |
| Default | 允许使用 `default()` 方法获取一个默认实例                                                                   |
|  Drop   | 析构函数中的自定义代码                                                                                      |
|   Try   | `?` 运算符和 try 代码块                                                                                     | 
## 运算符 Trait

- 可赋值运算符：同时具有配合 `Assign` 版本，对应 `X=` 运算符（如 `Add` 对应 `+`，`AddAssign` 对应 `+=`）

| Trait  | 运算符 |
| ------ | ------ |
| Add    | `+`    |
| Sub    | `-`    |
| Mul    | `*`    |
| Div    | `/`    |
| Rem    | `%`    |
| Shl    | `<<`   |
| Shr    | `>>`   |
| BitAnd | `&`    |
| BitOr  | `|`    |
| BitXor | `^`    | 

- 可变运算符：同时具有 `Mut` 版本，对应可变版本（如 `Deref` 对应解引用 `*`，`DerefMut` 对应可变变量版本的解引用 `*`）

| Trait | 运算符        |
| ----- | ------------- |
| Deref | `*` 解引用    |
| Index | `[]` 下标索引 |

- 其他运算符

| Trait         | 运算符                            |
| ------------- | --------------------------------- |
| OneSidedRange | `..a`，`b..`，`..=c`，单侧范围    |
| RangeBounds   | `..`，`a..b`，`a..=b`，双侧范围   |
| Try           | `?` 运算符                        |
| Neg           | `-` 取负                          |
| Not           | `!` 取反                          |
| Fn            | `()` 闭包，不可捕获可变变量       |
| FnMut         | `()` 闭包，可捕获可变变量         |
| FnOnce        | `()` 闭包，运行一次后不可再次运行 | 
