# 基于范围的 for

C++11 新增基于范围的 `for` 循环，用于遍历容器内容

```c++
for (range_declaration: range_expression) loop_statement
```

- `range_declaration`：迭代声明，容纳被迭代值
- `range_expression`：表达式，返回一个可迭代的对象
- `loop_statement`：循环体

```c++
vector<int> a;
for (auto& i: a) { // int& i
    cout << i << endl;
}
```

可迭代对象定义如下（二选一即可）：
- 对象类型定义了 `begin()` 和 `end()` 函数
- 作用域中定义了以迭代对象类型（或其引用）为参数的 `begin(T)` 和 `end(T)` 函数
- `begin`、`end` 返回值必须支持以下运算：`*`（解引用），`++`（自增-前缀版本），`!=`
- C++17 开始 `begin`、`end` 返回值可以不同

```c++
class Array {
private:
    int values[10]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
public:
    int *begin() {
        return values;
    }

    int *end() {
        return values + 10;
    }
};

int main() {
    Array arr;
    // 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
    for (int v : arr) {
        cout << v << ", ";
    }
    return 0;
}
```

使用独立的函数：

```c++
class Array {
public:
    int values[10]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
};

int *begin(Array& arr) {
    return arr.values;
}

int *end(Array& arr) {
    return arr.values + 10;
}

// main 函数和输出与上一代码片段相同
```

基于范围的 for 循环可以看作以下代码的语法糖：

```c++
auto && __range = range_expression;
for (auto __begin = __range.begin(), __end = __range.end();
	 __begin != __end;
	  ++__begin) {
    range_expression = *__begin;
    loop_statement
}
```

C++17 开始 `begin` 与 `end` 返回值类型可以不同，可以看作以下代码的语法糖：

```c++
auto && __range = range_expression;
// 区别在这里：__begin 与 __end 类型可以不同
auto __begin = __range.begin();
auto __end = __range.end();
for (; __begin != __end; ++__begin) {
    range_expression = *__begin;
    loop_statement
}
```
# 初始化语句

C++17 后，`if`，`switch` 支持初始化语句：

```
if (init; condition) statement
switch (init; condition) statement
```

```c++
bool foo();

if (bool b = foo(); b) {
    // do something
}
```

另外，`if` 的 `else-if` 分支也支持初始化语句。

C++20 后，基于范围的 `for` 循环支持初始化语句：

```c++
class T {
public:
    vector<int>& items();
};

T get();

for (T t = get(); auto x: t.items()) {
    // do something
}
```
