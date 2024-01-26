这里主要记录一些以前没有注意的小知识点

- 判断读完字符串
	- `scanf` 会返回一个 int，当值为 0 时表示没有读到任何值
	- `getchar()` 返回 `EOF`
- `getchar()` 返回一个 `int`，因为 `EOF` 比 `char` 可以表示的最大值更大
- 宏定义：`stdio.h` 定义了 `EXIT_FAILURE` 和 `EXIT_SUCCESS` -- `EXIT_SUCCESS=0`，这两个值是预定义的 `main` 函数返回值，也可以传入 `exit()` 函数中用于结束程序
- 命名规则：有时定义常量 `NUL` 表示 ASCII 的 `\0`，与 `NULL` 区分
