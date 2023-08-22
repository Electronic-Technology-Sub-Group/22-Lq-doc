Rust 是静态类型语言，任何值都需要一个数据类型，数据类型可以分为标量和复合。

编译器通常会推断出我们想要用的数据类型，但如果多个类型都可能符合时，必须增加类型注解

```rust
let num: u32 = "42".parse().expect("Not a number!"); // num = 42
```
# 数字

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
# 字符

Rust 字符类型 `char` 字面量以 `''` 引用，代表一个 4 字节 Unicode 标量值

```rust
let c = 'z';
let z = 'ℤ';
let heart_eyed_cat = '😻';
```
# 布尔

布尔值 `bool` 多用于控制语句，包含 `true` 和 `false` 两个值。
# 元组

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
# 数组

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
# never type

Rust 中有一个特殊的类型 `!`，该类型没有任何实例，也无法创建任何实例，表示不返回任何值。由于没有任何值，所以可以是任何类型。

```rust
fn bar() -> ! {
    // do something
}
```

无返回值的函数称为发散函数，但由于 `!` 没有值，所有不可能创建返回值为 `!` 的函数。`!` 主要用于语言中实际没有返回值但语法上有的情况：
- `continue` 可以在 `match` 等分支/循环等结束循环，按语法可能有一个值，但实际没有值而是返回下一个循环的值，此时返回 `!`
- `panic!` 宏的返回值
# 类型别名

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

类型别名也常与 `Result` 类型搭配使用，详见[[Rust 流程控制]]

```rust
type Result<T> = std::result::Result<T, std::io::Error>;

pub trait Write {
    fn write(&mut self, buf: &[u8]) -> Result<usize>;
    fn flush(&mut self) -> Result<()>;
}
```
