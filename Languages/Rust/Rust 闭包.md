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
