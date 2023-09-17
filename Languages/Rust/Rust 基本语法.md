# 注释

使用 `//` 表示单行注释，`/* ... */` 表示多行注释

```rust
// line1
// line2
/*
line3
line4
 */
```
# 变量

Rust 使用 `let` 定义一个不可变变量，Rust 更多的推荐使用不可变量，使代码更易于推导，不易出错。变量名推荐以 _snake_case_ 即小写字母+下划线构成。

> [!note]
> `println!` 为一个宏调用，作用相当于输出一行文本到控制台
> `fn main() { /*...*/ }` 函数为入口函数

```rust
let x = 5;
// The value of x is 5
println!("The value of x is {}", x);
// cannot assign twice to immutable variable
x = 6;
println!("The value of x is {}", x);
```

Rust 也支持变量，某些大型数据结构适当使用变量可能比复制更快。使用 `let mut` 定义一个变量。

```rust
let mulet mut x = 5;
// The value of x is 5
println!("The value of x is {}", x);
x = 6;
// The value of x is 6
println!("The value of x is {}", x);
```

使用 `const` 声明常量。常量总是不可变的，因此不存在 `mut` 修饰，必须显式指定数据类型，且值只能为常量表达式。

```rust
const THREE_HOURS_TO_SECONDS: i32 = 3 * 60 * 60;
```

同一个作用域中可以定义同名变量，后声明的变量将覆盖先声明的。与 `mut` 不同的是，因为实际上是新建了一个变量，允许变量类型不同

```rust
let x = 5;
// Value x is 5
println!("Value x is {}", x);
let x = x + 1; // shadowing!
// Value x is 6
println!("Value x is {}", x);
{
    let x = x * 2; // shadowing!
    // Value x is 12
    println!("Value x is {}", x);
}
// Value x is 6
println!("Value x is {}", x);
let x = "New value"; // shadowing!
// Value x is New value
println!("Value x is {}", x);
```
# 语句

语句 Statements 为执行一些操作但无返回值的指令。不同于 Java 或 C++，赋值语句是一个语句，不产生返回值。

```rust
// ok, x = 6
let x = 6;
// expected expression, found statement (`let`)
let y = let z = 6;
```
# 表达式

Rust 大部分代码都是由表达式构成的，表达式 Expressions 为执行计算且产生一个值的指令。

`5+6` 是一个表达式，`let y = 6;` 中 `6` 也是一个表达式，宏调用是一个表达式，一个代码块作用域也可以是一个表达式。

```rust
// y = 6
let y = {
    let x = 1 + 2;
    x + 3
}
```

注意，块中最后一行 `x+3` 末尾没有分号，这表示一个语句，加上分号就是一个表达式了，而表达式是没有返回值的。这适用于有返回值的函数。
# 命令行 IO

 - 运行参数：`env::args()`
 - 环境变量：`env::var()`
