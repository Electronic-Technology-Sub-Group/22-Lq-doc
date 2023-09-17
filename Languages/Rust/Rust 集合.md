Cargo 提供 Vec，Map，Set，BinaryHeap 四种数据结构作为集合使用，同时 String 类也将在这里进一步说明。所有集合类型在离开作用域时都会自动释放其中的元素。
# Vec

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
# Map

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
# BinaryHeap

一个堆，插入的值会根据某些条件进行排序，适用于只需要查找最大或最小值，或一个优先级队列。
# String

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
# 迭代器

使用集合的 `iter()` 方法可创建一个迭代器，迭代器是惰性求值的。

迭代器实现了 `Iterator` 接口，可使用 `for-in` 循环遍历。可使用 `Iterator` 的方法从闭包创建迭代器
