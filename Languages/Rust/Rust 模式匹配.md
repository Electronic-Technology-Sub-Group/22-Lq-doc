# 模式

模式为 Rust 中的特殊语法，用于匹配类型中的结构，可由以下内容组成
 - 字面值
 - 解构的数组、枚举、结构体、元组
 - 变量
 - 通配符
 - 占位符

模式可以出现在很多地方，不只是 `match`
 - `match` 分支
```rust
match VALUE {
    PATTERN => EXPRESSION,
    PATTERN => EXPRESSION,
    PATTERN => EXPRESSION
}
```
 - `if let` 表达式
```rust
if let PATTERN = VALUE {
    EXPRESSION;
}
```
 - `while let` 条件循环
```rust
while let PATTERN = VALUE {
    EXPRESSION;
}
```
 - `for` 循环
```rust
for PATTERN in VALUE {
    EXPRESSION;
}
```
 - `let` 语句
```rust
let PATTERN = VALUE;
```
 - 函数参数
```rust
fn function(PATTERN: VALUE) {
    EXPRESSION;
}
```
# 匹配失效

模式有两种形式：refutable 可反驳 和 irrefutable 不可反驳。若一个模式必须匹配所有值成为不可反驳的，如 `let`；否则为可反驳，如 `if let`。

函数参数，`let` 语句和 `for` 循环都只接受不可反驳模式，若值无法与模式相匹配，程序无法继续正常运行；

```rust
fn main() {
    let some_option_value: Option<String> = None;
    // let Some(x) = some_option_value;
    //     ^^^^^^^ pattern `None` not covered
    let Some(x) = some_option_value;
}
```

`if let`, `while let` 可以接受可反驳模式，因为这里是用来测试是否匹配，不匹配其他代码仍然有效

```rust
fn main() {
    let some_option_value: Option<String> = None;

    // Value is none
    if let Some(x) = some_option_value {
        println!("Value is {}", x);
    } else {
        println!("Value is none");
    }
}
```

`match` 各匹配分支必须使用可反驳模式，但最后一个分支需要使用能匹配所有剩余值的不可反驳模式。

综上，若程序模式必须与值相匹配则使用不可反驳模式，若允许匹配失败则使用可反驳模式。
# 匹配字面值

```rust
fn main() {
    let x = 3;

    // Three
    match x {
        1 => println!("One"),
        2 => println!("Two"),
        3 => println!("Three"),
        _ => println!("Other"),
    }
}
```
# 匹配守卫

通过 if 可以额外增加一个条件

```rust
fn main() {
    let x = Some(5);
    let y = 10;

    // Default
    match x {
        Some(50) => println!("Got 50"),
        // Here! a = 5
        Some(a) if a == y => println!("Matched y={}", y),
        _ => println!("Default"),
    }
}
```
# 多模式匹配

使用 `|` 匹配多个模式

```rust
fn main() {
    let x = 1;

    // one or two
    match x {
        1 | 2 => println!("one or two"),
        3 => println!("three"),
        _ => println!("other")
    }
}
```
# `..=` 匹配范围

`..=` 可用于匹配一个闭区间内所有值，可用于数字类型和 `char`

```rust
fn main() {
    let x = 'c';

    // early ASCII letter
    match x {
        'a' ..= 'j' => println!("early ASCII letter"),
        'k' ..= 'z' => println!("late ASCII letter"),
        _ => println!("other")
    }
}
```
# 解构

可用于解构结构体、枚举、元组等

使用 `let` 解构结构体

```rust
struct Point {
    x: i32,
    y: i32,
}

fn main() {
    let p = Point { x: 5, y: 7 };

    let Point { x: a, y: b } = p;
    // Point (5, 7)
    println!("Point ({}, {})", a, b);

    // 简化 直接使用结构体字段名
    let Point { x, y } = p;
    // Point (5, 7)
    println!("Point ({}, {})", x, y);
}
```

也可以将字面值作为结构体模式的一部分用于解构，用于匹配

```rust
struct Point {
    x: i32,
    y: i32,
}

fn main() {
    let p = Point { x: 0, y: 7 };

    // On the y axis at 7
    match p {
        Point { x, y: 0 } => println!("On the x axis at {}", x),
        Point { x: 0, y } => println!("On the y axis at {}", y),
        Point { x, y } => println!("At ({}, {})", x, y)
    }
}
```

解构枚举、元组等与结构体类似
# 忽略

使用 `_` 可以匹配并不绑定一个值，常用于 `match` 最后一个分支，但也可以用于其他可使用模式的地方

```rust
fn foo(_: i32, y: i32) { /*...*/ }

fn main() {
    let numbers = (2, 4, 8, 16, 32);
    let (first, _, third, _, fifth) = numbers;
    // 2, 8, 32
    println!("{}, {}, {}", first, third, fifth);
}
```

使用 `..` 忽略剩余多个值并不绑定

```rust
struct Point {
    x: i32,
    y: i32,
    z: i32
}

fn main() {
    let point = Point { x: 0, y: 1, z: 2 };
    let Point { z, .. } = point;
    // z = 2
    println!("z = {}", z)
}
```

但 `..` 的使用必须是无歧义的

```rust
fn main() {
    let numbers = (2, 4, 8, 16, 32);
    // let (.., second, ..) = numbers;
    //      --          ^^ can only be used once per tuple pattern
    //      |
    //      previously used here
    let (.., second, ..) = numbers;
}
```
# @ 绑定

使用 `@` 可以绑定一个变量的同时测试

```rust
struct Hello {
    id: i32
}

fn main() {
    let msg = Hello { id: 5 };

    // Found id in range 5
    match msg {
        // 将 id 绑定为 var_id (当然也可以叫 id) 并进行匹配
        Hello { id: var_id @ 3..=7 } => println!("Found id in range {}", var_id),
        // 将 id 进行匹配，但没有绑定
        Hello { id: 10..=12 } => println!("Found id in another range {}", msg.id),
        Hello { id } => println!("Found other id {}", id)
    }
}
```
# 枚举匹配

枚举数据可以通过 `match` 访问

```rust
enum Message {
    Quit, // 不包含任何数据
    Move { x: i32, y: i32 }, // 包含一个匿名结构体
    Write(String), // 包含一个字符串
    ChangeColor(i32, i32, i32) // 包含一个元组
}

impl Message {
    fn call(&self) {
        match self {
            Message::Quit => println!("Quit!!!"),
            Message::Move {x, y} => println!("Move to ({}, {})", x, y),
            Message::Write(str) => println!("Write {}", str),
            Message::ChangeColor(r, g, b) => println!("Change to ({}, {}, {})", r, g, b)
        }
    }
}

fn main() {
    // Quit!!!
    Message::Quit.call();
    // Move to (2, 3)
    Message::Move { x: 2, y: 3 }.call();
    // Write messages...
    Message::Write(String::from("messages...")).call();
    // Change to (5, 3, 2)
    Message::ChangeColor(5, 3, 2).call();
}
```

若 `match` 没有覆盖所有可能情况，使用 `other` 或 `_` 作为兜底；若不需要任何操作，使用 `()`

```rust
impl Message {
    fn call(&self) {
        match self {
            Message::Move {x, y} => println!("Move to ({}, {})", x, y),
            Message::Write(str) => println!("Write {}", str),
            Message::ChangeColor(r, g, b) => println!("Change to ({}, {}, {})", r, g, b)，
            // 现在 我不需要 Quit 做任何事了
            _ => ()
        }
    }
}
```

if let 表达式可以看作是 `match` 的一个语法糖，当 `match` 过于啰嗦时可以考虑使用 `if let` 处理匹配

```rust
fn main() {
    let some_u8_value1 = Some(3u8);
    // Value1 is 3
    if let Some(i) = some_u8_value1 {
        println!("Value1 is {}", i)
    }

    // Value2 is NONE
    let some_u8_value2: Option<u8> = None;
    if let Some(i) = some_u8_value2 {
        println!("Value2 is {}", i)
    } else {
        println!("Value2 is NONE")
    }
}
```
