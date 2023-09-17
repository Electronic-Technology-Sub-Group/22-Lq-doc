Rust 是一门存在指针的语言，最常见的指针是引用。**智能指针**是一类数据结构，表现为指针但拥有额外的元数据和功能，大部分情况下智能指针拥有其所指数据的所有权。

很多库都有自己的智能指针结构，Rust 标准库也提供了一些常用的智能指针
 - `Box<T>`: 用于在堆上分配的值
 - `Rc<T>`: 其数据可有多个所有者，通过引用计数控制
 - `Ref<T>`, `RefMut<T>`: 通过 `RefCall<T>` 访问
# `Box<T>`

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
## cons list

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

# 自定义智能指针

自定义智能指针需要实现实现 `Deref` 和 `Drop` trait
  - `Deref`: 允许结构体实例的表现与引用类似，因此可以作为引用使用
  - `Drop`: 自定义当离开作用域时运行的代码，因此大部分智能指针会在作用域外清理数据
## `Deref`

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
## `Drop`

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
# `Rc<T>`

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
# `RefCell<T>`

类似于 `Box<T>`, `RefCell<T>` 持有数据的唯一所有权。但该结构通过 `unsafe` 允许在运行时判断借用的可变性。

该结构用于在编译时无法符合可变引用的唯一性规则，但实际运行时的确可以保证安全的情况下。在运行时若违反该规则则会产生 panic!
- 给定任意时刻，只能有一个可变引用
- 可变引用存在时，不可以存在不可变引用

类似于 `Rc<T>`, `RefCall<T>` 只能用于单线程，若在多线程中使用则会产生编译错误。
