# 线程

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
# 消息队列

> [!tip]
> 不要通过共享内存来通讯，而是通过通讯来共享内存
> Do not communicate by sharing memory; instead, share memory by communicating.

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
# 共享内存与锁

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
# Send 与 Sync

`Send` trait 表示一个值可以在多个线程之间转移所有权。大多数 Rust 类型都实现了该 `trait`, 除了 `Rc<T>`。
一个所有成员都实现了 `Send` 的结构默认也实现了 `Send`，除了裸指针。

`Sync` trait 表示可以安全的在多个线程中获取其中所拥有值的引用，即对任意实现类型 `T`，若 `&T` 实现了 `Send`, 则 `T` 实现了 `Sync`。

基本类型都是 `Sync` 的，完全由 `Sync` 类型组成的结构也是 `Sync` 的。`Rc<T>`, `Cell<T>` 系列如 `RefCell<T>` 都不是 `Sync` 的，`Mutex<T>` 是 `Sync` 的

通常不需要手动实现 `Sync` 和 `Send`, 手动实现是不安全的。
