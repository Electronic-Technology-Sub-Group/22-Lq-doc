
```ad-note
栈：栈内值存取遵循 FILO 规则，所有数据及大小在编译时必须是已知的，速度较快
堆：堆内值缺乏组织性，运行时通过内存分配器在堆上分配内存，访问时需要使用指针，因此速度比栈慢，但更加灵活
```

所有权 ownership 系统是 Rust 无需垃圾回收且能保证内存安全的原因。
 - Rust 每个值都含有一个变量 `owner`
 - 当 `owner` 离开作用域时，使用 `drop` 方法释放内存

下面将以 `String` 为例解释作用域与所有者。String 表示一个可变、可增长的文本片段，使用 `String::from` 创建并在堆上申请内存。
# 移动与复制

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
# 所有权转移
## 作用域传递

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
## 返回值转移

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
# 引用与借用

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
# `Slice`

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
