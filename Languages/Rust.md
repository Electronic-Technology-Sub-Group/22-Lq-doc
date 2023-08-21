# 构建与编译

使用 `rustc [rs文件]` 在源码目录编译生成可执行文件

通过 `cargo new` 生成一个 Rust 项目，并通过 `cargo build` 编译，生成于项目下 `target` 目录中；使用 `cargo run` 直接运行
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

#todo 文档注释
# 变量与常量

Rust 使用 `let` 定义一个不可变变量，Rust 更多的推荐使用不可变量，使代码更易于推导，不易出错。变量名推荐以 _snake_case_ 即小写字母+下划线构成。

```ad-note
`println!` 为一个宏调用，作用相当于输出一行文本到控制台
`fn main() { /*...*/ }` 函数为入口函数
```

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
# 数据类型

Rust 是静态类型语言，任何值都需要一个数据类型，数据类型可以分为标量和复合。

编译器通常会推断出我们想要用的数据类型，但如果多个类型都可能符合时，必须增加类型注解

```rust
let num: u32 = "42".parse().expect("Not a number!"); // num = 42
```

## 数字

|       长度       |  有符号  |  无符号  |
|:--------------:|:-----:|:-----:|
|     8 bit      |  i8   |  u8   |
|     16 bit     |  i16  |  u16  |
|     32 bit     |  i32  |  u32  |
|     64 bit     |  i64  |  u64  |
|    128 bit     | i128  | u128  |
| 平台相关(32 or 64) | isize | usize |

整形字面值可以有三部分组成 - 前缀表示进制，中间为值，后缀表示类型

```rust
let x = 1; // i32 类型 十进制 1
let y = 0xffi64; // i64 类型 十六进制 FF
let z = 0o77usize; // usize 类型 八进制 77
let w = b'A'; // u8 类型 'A' (十进制 65)
```

十进制不需要前缀，八进制为 `0o`，十六进制为 `0x`，二进制为 `0b`， char 类型转化使用 `b`。

后缀主要用于指定整型类型，默认为 i32；`b` 前缀用于转化字符的类型只能是 u8。注意这里是字面量的类型，与变量类型可以不同，允许安全转化。

整型溢出：当运算结果超过整型最大值的情况被称为 整型溢出。debug 模式下 Rust 使程序 panic，release 模式下使用二进制补码包装。 
整型溢出一般被认为是一种错误，但若依赖于这种行为，可使用 `Wrapping` 功能。
 - `wrapping_*` 方法总是将溢出值以二进制补码包装，在 debug 模式下仍能运行
 - `checked_*` 方法返回一个 Option 对象，当溢出时返回 None
 - `overflowing_*` 方法返回一个 `(self, bool)` 类型元组，前者以补码包装，后者为是否溢出
 - `saturating_*` 方法对值的最大值或最小值进行饱和处理

```rust
// Integer overflow: 254+1=255
println!("Integer overflow: 254+1={}", 254u8+1u8);
// attempt to compute `254_u8 + 2_u8`, which would overflow
// println!("Integer overflow: 254+2={}", 254u8+2u8);
// Wrapping add: 254+1=255
println!("Wrapping add: 254+1={}", 254u8.wrapping_add(1u8));
// Wrapping add: 254+2=0
println!("Wrapping add: 254+2={}", 254u8.wrapping_add(2u8));
// Check: 254+1=255
println!("Check: 254+1={}", 254u8.checked_add(1u8).get_or_insert(0u8));
// Check: 254+2=0
println!("Check: 254+2={}", 254u8.checked_add(2u8).get_or_insert(0u8));
let oa = 254u8.overflowing_add(1u8);
// Overflowing: 254+1=(255, false)
println!("Overflowing: 254+1=({}, {})", oa.0, oa.1);
let oa = 254u8.overflowing_add(2u8);
// Overflowing: 254+1=(0, true)
println!("Overflowing: 254+1=({}, {})", oa.0, oa.1);
// Saturating: 254+1=255
println!("Saturating: 254+1={}", 254u8.saturating_add(1u8));
// Saturating: 254+2=255
println!("Saturating: 254+2={}", 254u8.saturating_add(2u8));
```

Rust 浮点分为 `f32` 和 `f64` 两种，代表 IEEE-754 标准单精度/双精度浮点数，默认为 f64，因为在现代 CPU 中 64 位浮点数的速度与 32 位几乎相同且精度更高。

```rust
let x = 1.0; // f64
let y: f32 = 2.2; // f32
```

## 字符

Rust 字符类型 `char` 字面量以 `''` 引用，代表一个 4 字节 Unicode 标量值

```rust
let c = 'z';
let z = 'ℤ';
let heart_eyed_cat = '😻';
```

## 布尔

布尔值 `bool` 多用于控制语句，包含 `true` 和 `false` 两个值。
## 元组

元组 tuple 将多个值组合成一起，使用 `()` 创建，使用 `,` 分割元素

元组长度固定，内部元素可以互相不同但每个位置的类型也是固定的。

```rust
let tup1 = (500, 6.4, 1);
let tup2: (i32, f16, u8) = (500, 6.4, 1);
```

访问元组可使用索引，也可以使用模式匹配解构。

```rust
let tup = (500, 6.4, false);

// Tup=(500, 6.4, false)

// 索引访问
println!("Tup=({}, {}, {})", tup.0, tup.1, tup.2);

// 模式访问
let (a, b, c) = tup;
println!("Tup=({}, {}, {})", a, b, c);
```

一个没有任何元素的元组成为单元元组 `()`，单元元组仅有一个值 `()`，所有方法若没有返回值则隐式返回单元元组
## 数组

数组是一组包含了相同元素和固定长度的值，以 `[]` 创建

```rust
// i32 类型数组 [1, 2, 3, 4, 5]
let arr1 = [1, 2, 3, 4, 5];
// 显式指定类型和数量 i64 类型数组 [1, 2, 3, 4, 5]
let arr2: [i64, 5] = [1, 2, 3, 4, 5];
// 指定相同元素和个数 i32 类型数组 [3, 3, 3, 3, 3]
let arr3 = [3; 5];
```

数组使用索引访问

```rust
let arr = [3; 5];
// Value at index 3 = 3
println!("Value at index 3 = {}", arr[3]);
```

当给定索引无效时，将引发 panic：index out of bounds
## never type

Rust 中有一个特殊的类型 `!`，该类型没有任何实例，也无法创建任何实例，表示不返回任何值。由于没有任何值，所以可以是任何类型。

```rust
fn bar() -> ! {
    // do something
}
```

无返回值的函数称为发散函数，但由于 `!` 没有值，所有不可能创建返回值为 `!` 的函数。`!` 主要用于语言中实际没有返回值但语法上有的情况：
- `continue` 可以在 `match` 等分支/循环等结束循环，按语法可能有一个值，但实际没有值而是返回下一个循环的值，此时返回 `!`
- `panic!` 宏的返回值
## 类型别名

Rust 允许使用 `type` 为类型创建同义词

```rust
type Kilometers = i32;

let x = 5;
// y is i32
let y: Kilometers = 10;
let z = x + y;
```

类型别名可以简化类型，详见[[#泛型]]

```rust
type Trunk = Box<dyn Fn() + Send + 'static>;

fn main() {
    let f: Box<dyn Fn() + Send + 'static> = Box::new(|| println!("hi!"));
    let f2: Trunk = f;
    call(f2);
}

fn call(f: Trunk) {
    f();
}
```

类型别名也常与 `Result` 类型搭配使用，详见[[#Result]]

```rust
type Result<T> = std::result::Result<T, std::io::Error>;

pub trait Write {
    fn write(&mut self, buf: &[u8]) -> Result<usize>;
    fn flush(&mut self) -> Result<()>;
}
```
# 语句与表达式
## 语句

语句 Statements 为执行一些操作但无返回值的指令。不同于 Java 或 C++，赋值语句是一个语句，不产生返回值。

```rust
// ok, x = 6
let x = 6;
// expected expression, found statement (`let`)
let y = let z = 6;
```
## 表达式

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
# 函数

函数使用 `fn` 关键字声明，由函数名，参数列表和可选的返回值组成。函数名推荐以 _snake_case_ 即小写字母+下划线构成。

```rust
fn 函数名(形参列表) -> 返回值 {
    函数体
}
```
## 入口

`main` 函数为整个 Rust 代码的入口函数，该函数无参，返回值类型为 `()` 或 `Result<(), E>`
 - `()` 类型即无返回值函数的返回值类型，详见元组
 - `Result<(), E>` 类型类似 Java 的 `throw`，详见[[#错误处理]]

```rust
fn main() {
    // do something
}
```
## 参数

参数需要参数名和参数类型，以 `,` 分割多个参数

```rust
fn fun(i: i32, c: char) {
    // do something
}
```
## 返回值

使用 `->` 指定返回值类型，在函数体中使用 `return` 提前返回值，或使用函数最后一条表达式（不带有 `;`）作为返回值。

```rust
// 返回 i32 类型 5，这里 5 没有分号，是一个表达式
fn five() -> i32 { 5 }

fn add(a: i32, b: i32) -> i32 { a+b }
```
# 控制流

## if

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

## match

分支使用 `match` 进行模式匹配，详细使用方法见后续[[#模式]]

```rust
match a {
    1 => println!("a=1"),
    2 => println!("a=2"),
    3 => println!("a=3"),
    4 => println!("a=4"),
    _ => {}
}
```
## 循环
### 跳出循环

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
### loop

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
### for

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
# 所有权

```ad-note
栈：栈内值存取遵循 FILO 规则，所有数据及大小在编译时必须是已知的，速度较快
堆：堆内值缺乏组织性，运行时通过内存分配器在堆上分配内存，访问时需要使用指针，因此速度比栈慢，但更加灵活
```

所有权 ownership 系统是 Rust 无需垃圾回收且能保证内存安全的原因。
 - Rust 每个值都含有一个变量 `owner`
 - 当 `owner` 离开作用域时，使用 `drop` 方法释放内存

下面将以 `String` 为例解释作用域与所有者。String 表示一个可变、可增长的文本片段，使用 `String::from` 创建并在堆上申请内存。
## 移动与复制

在 Rust 中，类似 `s1 = s2` 这样的指针操作在以下情况下触发移动，s2 获取 s1 的一份浅拷贝后 s1 会被丢弃
- 分配在栈上的变量
- 分配在堆上，但未实现 `Copy` Trait 的对象

```rust
let s1 = String::from("Hello");
let s2 = s1;

// let s1 = String::from("Hello");
//     -- move occurs because `s1` has type `String`, which does not implement the `Copy` trait
// let s2 = s1;
//          -- value moved here
println!("{}", s1);
```

对于栈上变量，类似 `s1 = s2` 的做法实际上是对值的复制

```rust
let s1 = "123";
let s2 = s1;

// 123
println!("{}", s1);
// 123
println!("{}", s2);
```

Rust 中有一个名为 `Copy` 的 trait，实现后可实现类似栈上变量的直接复制，Rust 某些类已经实现了 `Copy`
 - 所有整数类型，浮点类型，布尔类型，字符类型。
 - 元组中，当且仅当其包含的类型都实现了 `Copy`, 如 `(i32, i64)` 实现了 `Copy`, 但 `(i32, String)` 未实现。
## 所有权转移
### 作用域传递

```rust
fn main() {
    let x = 5; // x 进入作用域
    makes_copy(x); // 由于 i32 实现了 Copy，因此 x 复制到了方法中
    println!("{}", x); // x 原始变量仍有效

    let s = String::from("hello"); // s 进入作用域
    takes_ownership(s); // 由于 String 未实现 Copy，因此 String 传递到了方法中
    
    // let s = String::from("hello");
    //     - move occurs because `s` has type `String`, which does not implement the `Copy` trait
    // takes_ownership(s);
    //                 - value moved here
    // println!("{}", s);
    //                ^ value borrowed here after move
    // println!("{}", s); // 由于 s 在 takes_ownership 方法中已经释放，这里不再有效
}

fn takes_ownership(str: String) { // str 进入作用域
    println!("{}", str);
} // str 离开所用域，并使用 drop 方法释放内存

fn makes_copy(a: i32) {
    println!("{}", a);
} // i32 类型实现了 Copy，因此不会有特殊操作
```
### 返回值转移

`return` 可用于转移所有权

```rust
fn main() {
    let s1 = gives_ownership(); // gives_ownership 通过 return 将 some_string 的所有权转移给 s1
    let s2 = String::from("hello"); // s2 进入作用域
    let s3 = takes_and_gives_back(s2); // s2 移动到 takes_and_gives_back 中，takes_and_gives_back 将返回值再次转移给 s3

    println!("{}", s1); // hello
    // let s2 = String::from("hello");
    //     -- move occurs because `s2` has type `String`, which does not implement the `Copy` trait
    // let s3 = takes_and_gives_back(s2);
    //                               -- value moved here
    // ...
    // println!("{}", s2);
    //                ^^ value borrowed here after move
    // println!("{}", s2);
    println!("{}", s3); // hello
} // s1 和 s3 通过 drop 释放，s2 已被释放无事发生

fn gives_ownership() -> String {
    let some_string = String::from("hello"); // some_string 进入作用域
    some_string // return 将 some_string 作用域移交给函数调用者
}

fn takes_and_gives_back(str: String) -> String {
    str // return 将 str 作用域移交给函数调用者
}
```
## 引用与借用

每次都将参数传递给函数，然后通过 `return` 再从函数中传递出来，虽然可以通过元组传递多个值，但也是比较麻烦的。
Rust 支持直接传递变量的引用，使用 `&` 标记，允许使用值但不获取其所有权。创建引用的过程叫做借用

引用的存在遵循以下规则：
  - 任意给定时间（作用域内），无法同时存在超过一个可变引用，也无法在不可变引用存在的情况下存在可变引用
  - 引用必须总是有效

```rust
fn main() {
    let s1 = String::from("hello");
    // 传递的是一个 &String 类型，即对 String 的引用，可访问到 s1 但不拥有它
    let len = calculate_length(&s1);
    // hello.len=5
    println!("{}.len={}", s1, len);
}

fn calculate_length(s: &String) -> usize {
    s.len()
}
```

引用的值无法被修改

```rust
fn main() {
    let s1 = String::from("hello");
    change(&s1);
    println!("{}", s1);
}

fn change(s: &String) {
    // fn change(s: &String) {
    //              ------- help: consider changing this to be a mutable reference: `&mut String`
    //     s.push_str(", world");
    //     ^^^^^^^^^^^^^^^^^^^^^ `s` is a `&` reference, so the data it refers to cannot be borrowed as mutable
    s.push_str(", world");
}
```

若想要修改引用值，首先要将变量转化为可变变量，然后使用 `&mut` 作为变量类型。

```rust
fn main() {
    let mut s1 = String::from("hello");
    change(&mut s1);
    // hello, world
    println!("{}", s1);
}

fn change(s: &mut String) {
    s.push_str(", world");
}
```

但同一时刻，只能有一个对某变量的可变引用

```rust
let mut s1 = String::from("hello");

let r1 = &mut s1;
// let r1 = &mut s1;
//          ------- first mutable borrow occurs here
// let r2 = &mut s1;
//          ^^^^^^^ second mutable borrow occurs here
// 
// println!("{}, {}", r1, r2);
//                    -- first borrow later used here
let r2 = &mut s1;

println!("{}, {}", r1, r2);
```

也不能在持有不可变引用的同时持有可变引用

```rust
fn main() {
    let mut s1 = String::from("hello");

    let r1 = &s1;
    let r2 = &s1;
    // let r1 = &s1;
    //          --- immutable borrow occurs here
    // let r2 = &s1;
    // let r3 = &mut s1;
    //          ^^^^^^^ mutable borrow occurs here
    // 
    // println!("{}, {}. {}", r1, r2, r3);
    //                        -- immutable borrow later used here
    let r3 = &mut s1;

    println!("{}, {}. {}", r1, r2, r3);
}
```

注意一个引用的作用域是从声明开始，到最后一次使用为止。因此以在不同作用域中使用，也可以在使用后声明，这不是同时拥有

```rust
fn main() {
    let mut s1 = String::from("hello");

    {
        let r1 = &mut s1;
    }

    let r2 = &s1;
    let r3 = &s1;
    println!("{} and {}", r2, r3);
    // r1 已脱离作用域，r2 r3 已被使用都失效了，因此此时声明不再是同时拥有
    let r4 = &mut s1;
    println!("{}", r4);
}
```

对于其他存在指针概念的语言中，易由于释放内存时保留指向它的指针而形成悬垂指针。但 Rust 不允许在作用域外存在对应指针。

```ad-note
悬垂指针：指向的内存已被分配给其他持有者
```

```rust
fn main() {
    let reference_to_nothing = dangle();
}
// fn dangle() -> &String {
//                ^ expected named lifetime parameter
// help: this function's return type contains a borrowed value, but there is no value for it to be borrowed from
// help: consider using the `'static` lifetime
// fn dangle() -> &'static String {
//                ~~~~~~~~
fn dangle() -> &String {
    let s = String::from("hello");
    &s
}
```

解决方法为直接返回 String 本身，转移所有权
## `Slice`

slice 是除了引用外另一个没有所有权的数据类型，表示对集合中一段连续元素或序列的引用。

```rust
fn main() {
    let s = String::from("Hello world!");
    let word = first_word(&s);
    // First word is Hello
    println!("First word is {}", word)
}

fn first_word(str: &String) -> &str {
    let bytes = str.as_bytes();

    for (i, &item) in bytes.iter().enumerate() {
        if item == b' ' {
            return &str[0..i];
        }
    }

    &str[..]
}
```

String 的 slice 类型为 `&str`, 这也是直接使用字面量字符串赋值的类型；其他类型使用 `&[type]`, 如 `&[i32]`。

slice 使用 `..` 表示范围，若从 0 开始则可省略起点，到最后一个元素为止则可省略结束

```rust
fn main() {
    let a = [1, 2, 3, 4, 5];
    // sa1: 2, 3
    let sa1 = &a[1..3];
    println!("sa1: {}, {}", sa1[0], sa1[1]);
    // sa2: 1, 2, 3
    let sa2 = &a[..3];
    println!("sa2: {}, {}, {}", sa2[0], sa2[1], sa2[2]);
    // sa3: 4, 5
    let sa3 = &a[3..];
    println!("sa3: {}, {}", sa3[0], sa3[1]);
    // sa4: 1, 2, 3, 4, 5
    let sa4 = &a[..];
    println!("sa4: {}, {}, {}, {}, {}", sa4[0], sa4[1], sa4[2], sa4[3], sa4[4]);
}
```
# 结构体

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
## 元组结构体

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
## 类单元结构体

没有任何字段的结构体称为类单元结构体，常用于要在某个类型上实现某个 `trait` 但又不想附加任何数据的情况

```rust
struct AlwaysEqual;

fn main() {
    let subject = AlwaysEqual;
}
```
## 结构体所有权

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
## 输出结构体

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
## 结构体方法

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
# 枚举

枚举允许通过列举可能的成员来定义一个类型，通过 `enum` 声明，通过 `::` 引用

```rust
enum IpAddrKind {
    V4, V6
}

fn main() {
    let ipV4 = IpAddrKind::V4;
    let ipV6 = IpAddrKind::V6;
}
```

成员可以包含一些数据，因此枚举量的个数严格来说并不一定可数，甚至每个枚举值包含的数据类型都不一定相同

```rust
enum Message {
    Quit, // 不包含任何数据
    Move { x: i32, y: i32 }, // 包含一个匿名结构体
    Write(String), // 包含一个字符串
    ChangeColor(i32, i32, i32) // 包含一个元组
}
```
#  泛型

泛型是具体类型或其他属性的抽象替代。

```rust
// 方法
fn get_first<T>(list: &[T]) -> &T {
    // ...
    list.get(0).unwrap()
}

// 结构体
struct Point<T, U> {
    x: T,
    y: U
}

impl<T, U> Point<T, U> {
    // ...
}

// 枚举
enum Option<T> {
    None,
    Some<T>
}
```

Rust 通过泛型代码的单态化实现了泛型使用时性能零损耗（相当于 C++ 的模板特化）
- 单态化：通过填充编译时使用的具体类型，将通用代码转化为特定代码的过程。

可以给泛型增加默认值

```rust
struct A;

// 默认 T=实现trait的类型，V=i32
trait B<T=Self, V=i32> {
    fn c(&self, p: T) -> V;
}

impl B for A {
    fn c(&self, p: Self) -> i32 {
        todo!()
    }
}

impl B<u32> for A {
    fn c(&self, p: u32) -> i32 {
        todo!()
    }
}
```
# Trait

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
## Trait 与结构体

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
## 运算符重载

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

## 关联类型

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
## 同名函数

Rust 不能避免实现的 trait 具有同名方法，也不能阻止为同一个结构体实现这样的 trait 或在 `impl` 上实现与 trait 同名方法，
因此访问这种方法时需要使用完全的限定符 `<StructType as TraitType>::method_name(...)`

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
## trait 继承

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
## newtype 模式

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
## 动态大小类型

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
## trait 对象

面向对象的程序是由对象组成的，一个对象包含数据和操作这些数据的过程，这些过程通常被称为方法或操作。虽然 Rust 结构体和枚举不称为对象，但提供了与对象相同的功能

Rust 无法定义结构体继承父类结构体数据，但可以定义相同的行为 `trait`。当对象安全时可认为是 trait 对象，可使用 `Box<dyn T>`
 - 方法返回值不包含 `Self`
 - 方法没有任何泛型类型参数
# 模式匹配
## 模式

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
## 匹配失效

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
## 匹配字面值

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
## 匹配守卫

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
## 多模式匹配

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
## `..=` 匹配范围

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
## 解构

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
## 忽略

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
## @ 绑定

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
## 枚举匹配

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
# 生命周期
## 生命周期注释

向编译器提供引用如何关联泛型，允许编译器在很多情况下判断引用值的有效性，用于方法和结构体

生命周期注释使用 `'` 标记，不改变引用的生命周期，仅仅是标记了某些引用的生命周期，使编译器可以检查使用的引用是否符合。

```rust
fn main() {
    // VV
    println!("{}", largest("A", "VV"));
}

fn largest<'a>(a: &'a str, b: &'a str) -> &'a str {
    if a.len() > b.len() {
        a
    } else {
        b
    }
}
```

`<'a>` 表示一个生命周期，在函数中表示 a b 以及返回值三个引用包含相同的生命周期

在某些情况下，生命周期可以省略
 - 每个引用的参数都具有各自的生命周期
 - 若只有一个输入参数，输出与输入参数的生命周期相同
 - 若方法第一个参数为 `&self` 或 `&mut self`，输出与 `&self` 具有相同生命周期

```rust
fn first_word(s: &str) -> &str {
    s[0..1]
}

// 等效于
fn first_word2<'a>(s: &'a str) -> &'a str {
    s[0..1]
}
```
## 静态生命周期

有一种特殊的生命周期 `'static` 静态生命周期，存活于整个程序运行期间

```rust
let s: &'static str = "I have a static lifetime.";
```
# 宏

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
## 声明宏

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
## 过程宏

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
## 类属性宏

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
## 类函数宏

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
# 闭包

闭包为可以以变量或参数形式存在的匿名函数。使用 `||` 声明闭包参数，`->` 声明返回值，`{}` 声明函数体。以下是写法及几种简写

```rust
fn add_one(x: u32) -> u32 { x+1 }

fn main() {
    let add_one_v1 = |x: u32| -> u32 { x+1 };
    // 参数和返回值类型可省略，编译器根据调用时的参数自动推断
    let add_one_v2 = |x| { x + 1 };
    // 闭包只有一行，大括号也可以省略了
    let add_one_v3 = |x| x+1;
}
```

根据捕获外部环境值的方法，闭包实现了 `Fn`, `FnMut` 或 `FnOnce` 中的一个 `trait`，完整类型可能是 `Fn(i32)`, `Fn(i32) -> i32` 等形式
 - `FnOnce`: 闭包从周围作用域捕获变量并获取其所有权，使用 `move` 关键字可强制获取所有权
 - `FnMut`: 闭包从周围作用域获取可变引用
 - `Fn`: 闭包从周围作用域获取不可变引用

```rust
struct Cached<T> where T: Fn(u32) -> u32 {
    calculation: T,
    value: Option<u32>
}

impl<T> Cached<T> where T: Fn(u32) -> u32 {

    fn new (calculation: T) -> Cached<T> {
        Cached {
            calculation,
            value: None
        }
    }

    fn value(&mut self, arg: u32) -> u32 {
        match self.value {
            Some(v) => v,
            None => {
                let v = (self.calculation)(arg);
                self.value = Some(v);
                v
            }
        }
    }
}

fn main() {
    let f = |x| {
        println!("Called with {}", x);
        x+1
    };
    let mut cached = Cached::new(f);
    // Called with 5
    // 6
    println!("{}", cached.value(5));
    // 6
    println!("{}", cached.value(5));
    // 6
    println!("{}", cached.value(5));
    // 6
    println!("{}", cached.value(5));
    // 6
    println!("{}", cached.value(5));
}
```
## 函数指针

函数名可以直接作为函数指针。函数指针类型为 `fn(...)->...` 

```rust
fn add_one(x: i32) -> i32 { x + 1 }

fn do_twice(f: fn(i32) -> i32, arg: i32) -> i32 {
    f(arg) + f(arg)
}

fn main() {
    let ans = do_twice(add_one, 5);
    // Answer is 12
    println!("Answer is {}", ans);
}
```

`fn` 类型实现了 `Fn`, `FnMut`, `FnOnce`, 因此可以直接作为参数传入接受闭包的函数或者成为其返回值。
# 集合

Cargo 提供 Vec，Map，Set，BinaryHeap 四种数据结构作为集合使用，同时 String 类也将在这里进一步说明。所有集合类型在离开作用域时都会自动释放其中的元素。
## Vec

`Vec<T>` 允许在一个数据结构中存储多于一个值，他们在内存中相邻排列（数组）。每个 `Vec` 进存储一种元素。创建 `Vec` 可通过 `Vec::new` 或 `vec!` 宏

```rust
fn main() {
    let vec1: Vec<i32> = Vec::new();
    let vec2 = vec![5, 2, 3];
}
```

构造器：

|       方法名       | 说明             |
|:---------------:|----------------|
|      `new`      | 创建一个不限制长度的队列   |
|    `new_in`     | 指定 `Allocator` |
| `with_capacity` | 创建并初始化数组长度     |
|     `from`      | 从其他可迭代对象中创建队列  |

并附带大量方法，如 `len()`, `is_empty()` 等属性，`swap()`, `reverse()` 等编辑方法，`first()`, `get()` 等获取方法，`iter`, `windows`, `chunks` 等遍历方法。
- 带有 `_mut` 后缀的版本返回可变指针
- 带有 `_unchecked` 后缀的版本表示不检查下标上下界
- 带有 `r` 前缀版本表示从右向左

Vec
- 希望元素按插入顺序排列，新数据只会追加到结尾
- 需要一个可调整大小、在堆上分配的数组
- 需要模拟一个堆栈

VecDeque
- 希望在队列的两端都可以插入数据
- 需要一个双端队列

LinkedList
- 需要未知规模的 Vec，且不能容忍经常的重建、复制数组
- 有效的拆分、追加列表
- 功能确实需要一个链表实现
## Map

`HashMap<K, V>` 存储了一组键值对，通过 `insert` 插入，`get` 获取，`entry` 可以返回一个 `Entry`, 包装了是否存在该键，或根据旧值更新新值

```rust
// 检查若不存在则插入
let mut scores = HashMap::new();
scores.insert(String::from("Blue"), 10);

scores.entry(String::from("Yellow")).or_insert(50);
scores.entry(String::from("Blue")).or_insert(50);

// 更新
let text = "hello world wonderful world";
let mut map = HashMap::new();

for word in text.split_whitespace() {
let count = map.entry(word).or_insert(0);
    *count += 1;
}
```

HashMap
- 需要一个键值对集合

BTreeMap
- 需要一个键值对集合
- 键值对需要按一定顺序排列，以便遍历所需
- 关心键的大小，需要比较键的大小
- 需要根据键的大小获取一系列值或查找最大、最小键的值

HashSet: 值为 `()` 的 HashMap
- 仅需要放置一系列不重复的值
- 需要知道是否包含某些值

BTreeSet: 值为 `()` 的 BTreeMap
- 需要放置一系列不重复的值
- 关心值的大小
## BinaryHeap

一个堆，插入的值会根据某些条件进行排序，适用于只需要查找最大或最小值，或一个优先级队列。
## String

`String` 是 Rust 标准库提供的 UTF-8 可变字符串结构。

在 Rust Core 中，字符串只有一种类型 `str`, 它常以引用的形式 `^str` 出现，因此我们常提到的字符串包含了 `String` 类型和切片 `&str` 类型。

```rust
let a = "123"; // &str
```

Rust 还存在其他一系列字符串类型，如 `OsString`, `OsStr`, `CString`, `CStr` 等，用的不多。其他 Crate 也提供了更多字符串类型。
- OsString, OsStr 主要为了解决 Windows API 使用未经检查的 UTF16 编码，而 Rust 则使用 UTF8 编码，为了保证转换不出错而设置的
- CString, CStr 主要为了兼容 C 库中使用 `'\0'` 结尾的字符串而设计，Rust 核心中的 `str` 类型本身保存长度因此不需要 `'\0'`

String 类型可以通过以下几个方法创建：

```rust
fn main() {
    let str1 = "hello".to_string(); // 通过 &str 创建
    let str2 = String::new(); // 空字符串
    let str3 = String::from("hello"); 
}
```

可通过 `push`, `push_str`, `add`/`+`, `format!` 连接字符串

```rust
fn main() {
    // ""
    let mut str = String::new();
    // "Hello"
    str.push_str("Hello");
    // "Hello!"
    str.push('!');
    // "Hello!~~~"
    let str2 = str + "~~~"; // String::add
    // "Hello!~~~, !!!"
    let str3 = format!("{}, {}", str2, "!!!");

    assert_eq!(str3, "Hello!~~~, !!!")
}
```

字符串切片和长度都是字节长度，其长度不一定是字符串字符个数

```rust
fn main() {
    let str = "你好！";
    // len=9
    println!("len={}", str.len());
    // 0..3=你
    println!("0..3={}", &str[0..3]);
    // thread 'main' panicked at 'byte index 1 is not a char boundary; it is inside '你' (bytes 0..3) of `你好！`', src\main.rs:5:26
    // UTF-8 中，一个汉字使用三个字节表示
    println!("0..1={}", &str[0..1]);
    for x in str.bytes() {
        // 228, 189, 160, 229, 165, 189, 239, 188, 129, 
        print!("{}, ", x)
    }
}
```
## 迭代器

使用集合的 `iter()` 方法可创建一个迭代器，迭代器是惰性求值的。

迭代器实现了 `Iterator` 接口，可使用 `for-in` 循环遍历。可使用 `Iterator` 的方法从闭包创建迭代器
# 智能指针

Rust 是一门存在指针的语言，最常见的指针是引用。**智能指针**是一类数据结构，表现为指针但拥有额外的元数据和功能，大部分情况下智能指针拥有其所指数据的所有权。

很多库都有自己的智能指针结构，Rust 标准库也提供了一些常用的智能指针
 - `Box<T>`: 用于在堆上分配的值
 - `Rc<T>`: 其数据可有多个所有者，通过引用计数控制
 - `Ref<T>`, `RefMut<T>`: 通过 `RefCall<T>` 访问
## `Box<T>`

`Box<T>` 是最简单的智能指针，允许将值放入堆中，栈上保留指向堆内存的指针。除指向堆内存外该指针无额外性能消耗，也不提供任何额外功能。
 - 递归类型：编译时未知大小，但实际使用时有确切大小
 - 改善拷贝性能：有大量数据且希望不拷贝的情况下转移所有权
 - trait 对象：只关心值是否实现了某 `trait` 但不关心具体类型时

```rust
fn main() {
    let b = Box::new(5);
    // b = 5
    println!("b = {}", b);
}
```
### cons list

`cons list` 来源于 `Lisp` 语言，利用当前值和另一个列表来构建新列表，表示将一个值连接到另一个值的开头，最后一项为 `Nil` 表示结束，有点像链表。
但若直接创建，由于数据大小无法确定，编译器会报错，同时也会给予提示使用智能指针

```rust
enum List {
// ^^^^^^ recursive type has infinite size
    Cons(i32, List),
 // ---- recursive without indirection
    Nil
}

fn main() {
    // [1, 2, 3]
    let list = Cons(1, Cons(2, Cons(3, Nil)));
}

// help: insert some indirection (e.g., a `Box`, `Rc`, or `&`) to make `List` representable
```

使用 `Box<T>` 实现

```rust
use crate::List::{Cons, Nil};

enum List {
    Cons(i32, Box<List>),
    Nil
}

fn main() {
    // [1, 2, 3]
    let list =
     Cons(1, Box::new(
        Cons(2, Box::new(
            Cons(3, Box::new(Nil))))));
}
```

## 自定义智能指针

自定义智能指针需要实现实现 `Deref` 和 `Drop` trait
  - `Deref`: 允许结构体实例的表现与引用类似，因此可以作为引用使用
  - `Drop`: 自定义当离开作用域时运行的代码，因此大部分智能指针会在作用域外清理数据
### `Deref`

智能指针允许通过解引用获取内部数据的引用

```rust
fn main() {
    let a = 5;
    let b = &a;
    let c = Box::new(a);

    assert_eq!(a, 5);
    assert_eq!(a, *b);
    // 智能指针解引用
    assert_eq!(a, *c);
}
```

实现 `Deref` 允许重载解引用运算符 `*`，可以将引用转换为被引用数据

```rust
use std::ops::Deref;

struct MyBox<T>(T);

impl MyBox<T> {
    fn new(x: T) -> MyBox<T> {
        MyBox(T)
    }
}

impl<T> Deref for MyBox<T> {
    type Target = T;

    fn deref(&self) -> &Self::Target {
        &self.0
    }
}

fn main() {
    let x = 5;
    let y = MyBox(x);

    assert_eq!(5, *y)
}
```

`deref` 方法返回一个解引用时返回数据的引用值。实现后可通过 `*` 将指针结构体解引用成指定数据类型 `type Target T;` 定义了关联类型。
实现了 `Deref` 的结构作为引用参数传递时可隐式转换为对应类型的引用
 - `T: Deref<Target=U>`: `&T` -> `&U`
- `T: Deref<Target=U>`: `&mut T` -> `&U`
- `T: DerefMut<Target=U>`: `&mut T` -> `&mut U`

```rust
fn print(s: &str) {
    println!("{}", s);
}

fn main() {
    let s = MyBox(String::from("abc"));
    // &MyBox<String> -> &String
    // &String -> &str
    print(&s);
}
```
### `Drop`

实现 `Drop` 接口后可自定义离开作用域时执行的代码，类似 C++ 的析构函数

```rust
impl<T> Drop for MyBox<T> {

    fn drop(&mut self) {
        // 一个假的释放方法
        println!("{}", "Pointer dropped!");
    }
}

fn main() {
    let s = MyBox(String::from("abc"));
}
// Pointer dropped!
```

我们无法直接调用 `drop` 方法释放一个数据。如果要手动释放的话，需要使用 `std::mem::drop`
## `Rc<T>`

`Rc<T>` 通过引用计数，允许使用多个所有权，适用于希望在程序多个部分读取，且编译时无法确定哪一部分最后完成的堆内存。但只适用于单线程。

在存储数据时使用 `Rc.clone(Rc)` 方法为一个数据创建多个 `Rc<T>` 结构并增加引用计数，当最后一个 `Rc<T>` 释放，即引用计数为 0 时数据销毁。

```rust
use std::rc::Rc;

fn main() {
    let a = 123;
    let p1 = Rc::new(a);
    // Counter: 1
    println!("Counter: {}", Rc::strong_count(&p1));
    let p2 = p1.clone();
    // Counter: 2
    println!("Counter: {}", Rc::strong_count(&p1));
    let p3 = p1.clone();
    // Counter: 3
    println!("Counter: {}", Rc::strong_count(&p1));

    use_data(p1);
    // Counter: 2
    println!("Counter: {}", Rc::strong_count(&p3));
    use_data(p2);
    // Counter: 1
    println!("Counter: {}", Rc::strong_count(&p3));
    use_data(p3);
}

fn use_data(data: Rc<i32>) {}
```

循环引用易造成内存泄漏，可使用 `Rc::downgrade` 获取弱引用代替，弱引用不属于所有权关系，不会造成引用循环。弱引用类型为 `Weak<T>`

```rust
use std::cell::RefCell;
use std::rc::{Rc, Weak};

struct Node {
    value: i32,
    // 当 parent 移除时，children.parent 也应释放，反之不行
    // 即 实际上 children 不应持有 parent 的所有权 避免循环引用
    parent: Weak<Node>,
    // RefCell 类似 &mut, 可在运行时修改数据
    children: RefCell<Vec<Rc<Node>>>
}

fn main() {
    let root = Rc::new(Node {
        value: 0,
        // 指向 Option::None
        parent: Weak::new(),
        children: RefCell::new(vec![])
    });

    let leaf = Rc::new(Node {
        value: 1,
        parent: Rc::downgrade(&root),
        children: RefCell::new(vec![])
    });

    root.children.borrow_mut().push(leaf);
}

```
## `RefCell<T>`

类似于 `Box<T>`, `RefCell<T>` 持有数据的唯一所有权。但该结构通过 `unsafe` 允许在运行时判断借用的可变性。

该结构用于在编译时无法符合可变引用的唯一性规则，但实际运行时的确可以保证安全的情况下。在运行时若违反该规则则会产生 panic!
- 给定任意时刻，只能有一个可变引用
- 可变引用存在时，不可以存在不可变引用

类似于 `Rc<T>`, `RefCall<T>` 只能用于单线程，若在多线程中使用则会产生编译错误。

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
# 并发

## 线程

使用 `thread::spawn` 创建一个线程并返回一个 `JoinHandle`, 可以此调用 `join()` 等方法

```rust
use std::thread;
use std::time::Duration;

fn main() {
    let handle = thread::spawn(|| {
        for i in 1..10 {
            println!("Hi number {} from the spawned thread!", i);
            thread::sleep(Duration::from_millis(1));
        }
    });

    for i in 1..5 {
        println!("Hi number {} from the main thread!", i);
        thread::sleep(Duration::from_millis(1));
    }

    handle.join().unwrap();
}

```
## 消息队列

```ad-tip
不要通过共享内存来通讯，而是通过通讯来共享内存
Do not communicate by sharing memory; instead, share memory by communicating.
```

通道使用 `mpsc::channel` 创建通道，每个通道可以有多个写入数据端，但只有一个读取数据端

```rust
use std::sync::mpsc::channel;
use std::thread;

fn main() {
    let (tx, rx) = channel();

    // 需要子线程闭包捕获 tx
    thread::spawn(move || {
        tx.send(String::from("Hi")).unwrap();
    });

    let received = rx.recv().unwrap();
    println!("Get {}", received)
}
```

使用 `clone()` 创建多个发送者

```rust
use std::sync::mpsc::channel;
use std::thread;

fn main() {
    let (tx, rx) = channel();

    let tx1 = tx.clone();
    thread::spawn(move || {
        tx1.send(String::from("Hi")).unwrap();
    });

    thread::spawn(move || {
        tx.send(String::from("Hello")).unwrap();
    });

    for received in rx {
        println!("Get {}", received)
    }
}

```
## 共享内存与锁

虽然 Rust 不推荐直接共享内存来共享数据，但还是提供了锁来保证共享内存的安全性。

互斥器 `Mutex<T>` 是一把锁，用于保证同一时刻只允许一个线程访问数据。线程通过获取互斥器的锁来访问数据，锁记录了谁有数据的排他性访问权。
通常使用 `Arc<T>` 创建智能指针引用。`Arc<T>` 相当于线程安全的 `Rc<T>`, 有着相同的 API

```rust
use std::sync::{Arc, Mutex};
use std::thread;

fn main() {
    let m = Mutex::new(0);
    let counter = Arc::new(m);
    let mut handles = vec![];

    for _ in 0..10 {
        let counter = counter.clone();
        let handle = thread::spawn(move || {
            // Mutex 具有内部可变性
            *counter.lock().unwrap() += 1;
        });
        handles.push(handle);
    }

    for handle in handles {
        handle.join().unwrap();
    }

    // Result=10
    println!("Result={}", counter.lock().unwrap())
}

```
## Send 与 Sync

`Send` trait 表示一个值可以在多个线程之间转移所有权。大多数 Rust 类型都实现了该 `trait`, 除了 `Rc<T>`。
一个所有成员都实现了 `Send` 的结构默认也实现了 `Send`，除了裸指针。

`Sync` trait 表示可以安全的在多个线程中获取其中所拥有值的引用，即对任意实现类型 `T`，若 `&T` 实现了 `Send`, 则 `T` 实现了 `Sync`。

基本类型都是 `Sync` 的，完全由 `Sync` 类型组成的结构也是 `Sync` 的。`Rc<T>`, `Cell<T>` 系列如 `RefCell<T>` 都不是 `Sync` 的，`Mutex<T>` 是 `Sync` 的

通常不需要手动实现 `Sync` 和 `Send`, 手动实现是不安全的。
# 不安全 Rust

Rust 允许使用不安全 Rust，关闭部分编译器静态检查。其存在意义在于
 - 静态分析本质上是保守的，某些代码可能合法但编译器无法获取足够信息
 - 底层计算机硬件固有的不安全性，Rust 需要直接与操作系统交互

使用 `unsafe` 关键字切换到不安全 Safe，不安全意味着可以
 - 解引用裸指针
 - 调用不安全函数
 - 访问或修改可变静态变量
 - 实现不安全 `trait`
 - 访问 `union` 字段
#### 裸指针

裸指针包括 `*const T` 和 `*mut T`，这里的 `*` 是作为类型的一部分而非运算符，与智能指针的区别在于
 - 允许忽略借用规则，可同时有多个可变和不可变指针
 - 不保证指向有效内存
 - 允许为空
 - 不被自动清理

允许在安全 Rust 中创建裸指针，但只能在不安全 Rust 中解引用

```rust
fn main() {
    let mut num = 5;
    let r1 = &num as *const i32;
    let r2 = &mut num as *mut i32;

    unsafe {
        // r1 is 5, r2 is 5
        println!("r1 is {}, r2 is {}", *r1, *r2)
    }
}
```

裸指针很多时候是不安全的

```rust
use std::slice;

fn main() {
    let address = 0x012345usize;
    // 指向 0x012345 内存，但谁知道这里有啥玩意...
    let r = address as *mut i32;
    let slice = unsafe {
        // 谁知道这块内存有啥...
        slice::from_raw_parts_mut(r, 10000)
    };
}

```
### 不安全成员

使用 `unsafe` 声明的函数为不安全函数

```rust
unsafe fn dangerous() {}

fn main() {
    unsafe {
        dangerous();
    }
}
```

trait 中只要有一个声明 `unsafe` 的方法，则必须将该 trait 声明为 `unsafe impl`

```rust
unsafe trait Foo {
    unsafe fn unsafe_fn();
}

unsafe impl Foo for i32 {
    unsafe fn unsafe_fn() {
        // do something
    }
}
```
### 外部函数接口

有时候要与其他语言代码交互，此时应使用 `extern` 关键字声明使用 FFI（外部函数接口）。需要声明 ABI（应用二进制接口）

```rust
// C 遵循 C语言 API
extern "C" {
    fn abs(input: i32) -> i32;
}

fn main() {
    unsafe {
        let r = abs(-3);
        // Absolute value of -3 according to C: 3
        println!("Absolute value of -3 according to C: {}", r);
    }
}
```

`extern` 也允许将 Rust 程序暴露给别的语言使用

```rust
#[no_mangle]
pub extern "C" fn call_from_c() {
    println!("Just called a Rust function from C");
}
```

`#[no_mangle]` 表示关闭编译器 mangle 功能，禁止编译器将函数名重命名
### 静态变量

Rust 允许全局变量，使用 `static` 声明，但不推荐使用。静态变量只能保存 `'static` 生命周期的引用，且常以 SCREAMING_SNAKE_CASE 命名法。任何读写静态变量的代码必须位于 `unsafe` 内

```rust
static mut COUNTER: u32 = 0;

fn add_to_count(inc: u32) {
    unsafe {
        COUNTER += inc;
    }
}

fn main() {
    add_to_count(3);
    unsafe {
        println!("COUNTER: {}", COUNTER);
    }
}
```
### union

`union` 类似 `struct`, 是 C 中的结构体，主要用于与 C 代码中的 `union` 类型交互
# 工作区，包与模块

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
## 包和 Crate

> Crate: 一个二进制项或库
> Crate root: 一个源文件。Rust 编译器以它为起点构建 Crate 根模块

> 包: 提供一系列功能的一个或多个 Crate，包含一个 `Cargo.toml` 来阐述如何构建 Crate
 - 一个包中最多包含一个库 Crate (Library crate), 可以有任意数量的二进制 Crate (Binary crate)
 - 一个包中至少包含一个 Crate, 无论是库 Crate 还是二进制 Crate

通过 `cargo new` 命令创建一个包，通过给定项目名，Cargo 会创建一个 `Cargo.toml` 配置文件和 `src` 目录，并生成一个
`main.rs`。

根据约定，`src/main.rs` 即与包同名的二进制 Crate 的 Crate 根；若包含 `src/lib.rs` 文件，则说明包含与包名同名的库 Crate
## 模块与私有性

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
### `use`

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
### `pub use`

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
### 外部包

通过在 `Cargo.toml` 中的 `dependencies` 块可以添加外部包，Cargo 将在 [crates.io](https://crates.io/) 搜索并下载对应包

```toml
[dependencies]
rand = '0.8.4'
```

标准库 `std` 对于程序来说也是外部 crate，只是不需要在 `Cargo.toml` 中声明。
## Workspace

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
# 命令行 IO

 - 运行参数：`env::args()`
 - 环境变量：`env::var()`
# 自动化测试

Rust 提供一系列用于编写测试的功能，包括 `test` 属性，一些宏和 `should_panic` 属性
## 测试函数

`cargo new` 创建的项目会自动生成一个 `tests` module，为测试函数的样板。一个测试函数应当以 `#[test]` 属性注释，之后可通过 `cargo test` 运行测试

```rust
#[test]
fn a_test_method() {
   // do something
}
```
## 测试结果检测

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
## 运行测试

使用 `cargo test` 启动测试，Cargo 测试也有若干参数

|         属性         | 说明                                |
|:------------------:|:----------------------------------|
| `--test-threads=n` | 使用 n 个线程并行测试                      |
|   `--nocapture`    | 显示 print 和 println 的输出            |
|    method_name     | 运行特定测试，所有测试方法中以 method_name 开头的方法 |
|    `--ignored`     | 不执行有 `#[ignore]` 属性的测试方法          |
## 测试模块

以 `#[cfg(test)]` 注解的模块表示该模块只有在执行 `cargo test` 时才编译和运行的代码，而 `cargo build` 时不运行。
在测试函数中允许使用私有函数，即测试代码可以测试私有函数。
## 集成测试

集成测试用于测试库的共有 API，其对于项目来说是一个独立库。集成测试代码位于 `tests` 文件夹中，该文件夹与 `src` 同级。

不需要在 `tests` 中使用 `#[cfg(test)]`，Cargo 可自动识别只有在 `cargo test` 时运行。
# Cargo 与 Cargo.io

## 发布配置

Rust 的发布配置是预定义的一组构建参数，默认有两个 `dev` 和 `release`。当运行 `cargo build` 时，使用 dev 配置，运行
`cargo build --release` 则使用 release 配置。

在 `Cargo.toml` 中可配置发布配置 profile

```toml
[profile.dev]
opt-level = 0

[profile.release]
opt-level = 3
```
## 文档

Rust 中使用 `///` 声明文档注释，格式支持 Markdown。通常一个文档注释应当包含 Panics，Errors，Safety 三部分
- Panics 列举了函数可能发生的 `panic!`的场景
- Errors 若函数返回 Result，该部分描述了函数何时返回什么错误
- Safety 若函数使用了 `unsafe` 代码，这部分表示期望函数调用者支持确保 `unsafe` 块正常运行的条件

还有另一种文档注释 `//!` 声明包含该元素的上层元素的注释，主要用于 crate 根文件。

使用 `crago doc` 在 `target/doc` 文件夹中生成文档，`cargo doc --open` 生成并打开。
## 发布到 crates.io

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
## 从 crate.io 安装二进制文件

使用 `cargo install` 向本地安装对应包的二进制 crate，其安装目录是 Rust 安装目录的 `bin` 子目录
## 使用自定义命令扩展 Cargo

Cargo 允许在不修改 Cargo 的前提下向 Cargo 添加子命令。

在 $PATH 中若存在类似 `cargo-something` 的二进制文件，则可以通过 `cargo something` 的形式调用。可使用 `cargo --list` 列举所有自定义命令。
# 附录
## 关键字

|   关键字    | 作用                                                                                                          |
|:--------:|-------------------------------------------------------------------------------------------------------------|
|    as    | 变量：强制类型转换                                                                                                   |
|    as    | 消除特定包含项的 trait 歧义                                                                                           |
|    as    | 对 use 或 extern crate 的项重命名                                                                                  |
|  break   | 跳出循环                                                                                                        |
|  const   | 定义常量或不变裸指针                                                                                                  |
| continue | 跳入下个循环                                                                                                      |
|  crate   | 链接一个外部 crate 或代表宏定义的 crate 的宏变量                                                                             |
|   dyn    | 动态分发 trait 对象                                                                                               |
|   else   | if/if let 的 fallback                                                                                        |
|   enum   | 定义枚举                                                                                                        |
|  extern  | 链接一个外部 crate，函数或变量                                                                                          |
|  false   | bool 字面量 false                                                                                              |
|    fn    | 定义函数或函数指针类型                                                                                                 |
|   for    | 遍历迭代器                                                                                                       |
|   for    | 为结构体实现 trait                                                                                                |
|   for    | 指定更高级的生命周期                                                                                                  |
|    if    | 基于条件的分支                                                                                                     |
|   impl   | 实现自有或 trait 功能                                                                                              |
|    in    | for 循环语法的一部分                                                                                                |
|   let    | 绑定变量                                                                                                        |
|   loop   | 无条件循环                                                                                                       |
|  match   | 模式匹配                                                                                                        |
|   mod    | 自定义模块                                                                                                       |
|   move   | 使闭包捕获所有权                                                                                                    |
|   mut    | 引用、裸指针或模式绑定的可变性                                                                                             |
|   pub    | 结构体、字段或 impl、模块的公有可见性                                                                                       |
|   ref    | 通过引用绑定                                                                                                      |
|  return  | 从函数返回                                                                                                       |
|   Self   | 实现 trait 的具体结构名                                                                                             |
|   self   | 表示方法本身或当前模块                                                                                                 |
|  static  | 全局变量或静态生命周期                                                                                                 |
|  struct  | 定义结构体                                                                                                       |
|  super   | 父模块                                                                                                         |
|  trait   | 定义 trait                                                                                                    |
|   true   | bool 字面量 true                                                                                               |
|   type   | 定义类型别名或关联类型                                                                                                 |
|  unsafe  | 声明不安全代码块、方法、函数、trait                                                                                        |
|   use    | 引入外部空间成员                                                                                                    |
|  where   | 约束类型从句                                                                                                      |
|  while   | 基于表达式结果的循环                                                                                                  |
|  保留关键字   | abstract, async, await, become, box, do, final, macro, override, priv, try, typeof, unsized, virtual, yield |
## 运算符重载

|     可重载运算符     |  重载 trait  |
|:--------------------:|:------------:|
|      `!=`, `==`      |  PartialEq   |
| `<`, `<=`, `>`, `>=` |  PartialOrd  |
|          !           |     Not      |
|          %           |     Rem      |
|          %=          |  RemAssign   |
|          &           |    BitAnd    |
|          &=          | BitAndAssign |
|          ^           |    BitOr     |
|          ^=          | BitOrAssign  |
|          *           |     Mul      |
|         `*=`         |  MulAssign   |
|          +           |     Add      |
|          +=          |  AddAssign   |
|          -           |     Neg      |
|          -           |     Sub      |
|          -=          |  SubAssign   |
|          /           |     Div      |
|          /=          |  DivAssign   |
|          <<          |     Shl      |
|         <<=          |  ShlAssign   |
|          >>          |     Shr      |
|         >>=          |  ShrAssign   |
## 内置 trait

运算符重载相关 trait 详见 运算符重载

|  Trait  | 说明                                                                  |
|:-------:|---------------------------------------------------------------------|
|  Debug  | 允许在调试时通过 `{:?}` 输出实例信息                                              |
|   Eq    | 必须同时实现 PartialEq, 表示 this == this。`HashMap<K, V>` 中的 K 必须实现 Eq      |
|  Clone  | 实现 `clone()` 方法，允许数据进行深拷贝。只有在所有成员都实现了 Clone 的结构体上才能实现 Clone         |
|  Copy   | 允许通过只拷贝栈上的位来复制而不需要额外的代码。可以假设复制的速度很快。任何使用 Copy 都可以使用 Clone 实现        |
|  Hash   | 允许通过 hash 函数生成一个固定大小的值，其字段必须全部实现 Hash。`HashMap<K, V>` 的 K 必须实现 Hash |
| Default | 允许使用 `default()` 方法获取一个默认实例                                         |
