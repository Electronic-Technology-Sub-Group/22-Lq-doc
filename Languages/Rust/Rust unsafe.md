Rust 允许使用不安全 Rust，关闭部分编译器静态检查。其存在意义在于
 - 静态分析本质上是保守的，某些代码可能合法但编译器无法获取足够信息
 - 底层计算机硬件固有的不安全性，Rust 需要直接与操作系统交互

使用 `unsafe` 关键字切换到不安全 Safe，不安全意味着可以
 - 解引用裸指针
 - 调用不安全函数
 - 访问或修改可变静态变量
 - 实现不安全 `trait`
 - 访问 `union` 字段
# 裸指针

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
# 不安全成员

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
# 外部函数接口

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
# 静态变量

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
# union

`union` 类似 `struct`, 是 C 中的结构体，主要用于与 C 代码中的 `union` 类型交互