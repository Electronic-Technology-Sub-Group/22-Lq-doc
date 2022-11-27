`auto` 用于使编译器自动推断数据类型。

当 `auto` 关键字与[[变量]]声明和赋值连用时，表示自动推断变量类型。其形式为 `auto x = expr`。

```c++
auto a = 10;
auto b = a * 3;
```

`auto` 只能推断类型（包括指针），但不能推断[[const]]和[[引用]]。而[[decltype]]是可以识别[[引用]]的

```c++
int[100] arr;

for (auto v1: arr) {
    // v1：int 类型
}

for (auto &v2: arr) {
    // v2：int& 类型
}

for (const auto &v3: arr) {
    // v3：const int& 类型
}
```

当 `auto` 关键字用于[[函数]]或[[lambda表达式]]时，表示该函数根据[[return]]语句推断返回值类型。这同样适用于[[函数指针]]

```c++
auto add(int a, int b) {
    // 类型推断：int + int => int
    return a + b;
}

// 函数指针名：p
// 返回值类型：int
auto (*p)() ->int;
// 函数指针名：q
// 返回值类型：根据 p 推导
auto (*q)() -> auto = p;
```

当然对于函数，`auto` 也用于[[尾置返回值]]类型的占位

```c++
auto func(int a, int b) -> int {  
    return a + b;  
}
```

`auto` 关键字还可以用于[[元组]]解构。使用 `auto` 可为解构的元组变量统一增加[[const]]，[[引用]]标记等

```c++
int a[2] = {1, 2};

// x, y: int
auto [x, y] = a;
// m, n: &int
auto& [m, n] = a;
```