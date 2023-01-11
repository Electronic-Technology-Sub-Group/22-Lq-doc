# cin cout

c++ `cin` 和 `cout` 非常方便，但效率偏低，主要是因为 c++ 为了兼容 `stdio` 将其输入输出流绑定到了一起。可通过以下方法取消绑定

```c++
std::ios::sync_with_stdio(false);
```

关闭同步后的副作用是，不能同时使用 `cin` 和 `scanf`，也不能同时使用 `cout` 和 `printf`，但 `cin` + `printf` 和 `cout` + `scanf` 的组合还是可以用的

# tie

c++ 将 `cin` 和 `cout` 也绑定到了一起，每次执行 `cout` 的 `<<` 运算符后都会清空一次缓冲区，在需要短时间多次输出大量数据时也会造成一定的 IO 压力，可使用 `tie` 方法解绑。

```c++
cin.tie(0);
```

解绑后的副作用是，在每次 `cout` 之后，如果有需要使用 `cin` 读入的内容，需要手动 `flush` 缓冲区以保证输出内容在读入内容之前。可通过输出一个 `flush` 或 `endl` 实现。

# cctype

`cctype` 和 `ctype.h` 头文件提供了一些用于判断输入内容的方法
- isdigit：输入字符是否是数字（$['0', '9']$）