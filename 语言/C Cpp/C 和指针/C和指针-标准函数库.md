# 数字函数
## 整形

部分整形参数位于 `<stdlib.h>` 中

数学运算：
- `int abs(int value)`：绝对值
	- `long labs(long value)`
- `div_t div(int numerator, int denominator)`：整形除法
	- `ldiv_t div(long numerator, long denominator)`

`div_t` 结构为：

```c
struct div_t {
    int quot;  // 商
    int rem;   // 余数
}
```

随机数：
- `int rand()`：获取一个 $[0, RAND\_MAX]$ 的随机数，`RAND_MAX` 至少为 32767
- `void srand(unsigned int seed)`：设置随机数种子

字符串转换：
- `int atoi(const char *str)`：字符串转换为数字，忽略前导空格，转换到第一个无效数字串
	- `long atol(const char *str)`：长整型版本
- `long strtol(const char* str, char **unused, int base)`：字符串转换为数字
	- `unsigned long strtoul(const char* str, char **unused, int base)`
	- `unused`：转换后一个字符的指针
	- `base`：基数（进制数）
	- 若值太大，返回 `LONG_MAX` 或 `ULONG_MAX`；值太小返回 `LONG_MIN`；同时，`errno=ERANGE
## 算法

`stdio.h` 存在部分算法：

- `void qsort(void *base, size_t n_elements, size_t el_size, int(*compare)(const void *, const void *))`：对数组进行就地快排
- `void *bsearch(const void *key, const void *base, size_t n_element, size_t el_size, int(*compare)(const void *, const void *))`：二分查找一个特定元素

| 参数 | 说明 |
| ---- | ---- |
| base | 操作的数组 |
| n_elements | 数组元素个数 |
| el_size | 每个元素长度 |
| compare | 大小比较函数 |
| key | 待查找元素 |
## 浮点

浮点运算多在 `math.h` 头文件中，大多数类型为 `double` 的。包含了大多数数学运算、三角函数等，详见文档
# 时间日期

简化时间和日期处理，位于 `time.h` 

- `clock_t clock(void)`：获取程序开始运行到现在的运行时间（近似）
	- 单位：处理器刻，-1 表示处理器无法提供时间或值过大
	- 常量：`CLOCKS_PER_SEC`，每秒包含的处理器刻
	- 若想获取精确值，应当在 `main` 开始时调用一次，然后求差值

- `time_t time(time_t *time)`：获取当前时间和日期
	- 无法提供时间和日期，或值过大，返回 -1
	- 若参数非 `NULL` ，则参数为存储时间和日期值的变量
	- 标准库并未定义 `time_t` 的具体格式类型
- `char *ctime(time_t *time)`：指向一个格式化后的字符串指针
	- 可能得实现方式为 `asctime(localtime(time))`

```c
time_t time;
time(&time);
// Sun Jul 4 04:02:48 1976\n\0
const char *str = ctime(&time);
```

- `double difftime(time_t *time1, time_t *time2)`：获取两个时间之差，单位为秒
- `struct tm *gmtime(const time_t *time)`：将 `time_t` 转换为 UTC 时间  `tm` 结构体
	- `struct tm *localtime(const time_t *time)`
	- `tm` 结构体可方便访问时间日期的各部分值，均为 `int` 类型

| 成员 | 范围 | 说明 |
| ---- | ---- | ---- |
| tm_sec | 0-61 | 秒 |
| tm_min | 0-59 | 分 |
| tm_hour | 0-23 | 时 |
| tm_mday | 1-31 | 日 |
| tm_mon | 0-11 | 月（0 为一月） |
| tm_year | >=0 | 年（0 为 1900 年） |
| tm_wday | 0-6 | 星期（0 为星期日） |
| tm_yday | 0-365 | 一年第几天（0 为一月一日） |
| tm_isdat |  | 是否为夏令时 |
- `char *asctime(const struct tm *tm_ptr)`：将 `tm` 结构转化为字符串
	- `size_t strftime(char *str, size_t max_size, const char *format, const struct tm *tm_ptr)`：将一个 `tm` 结构体转化为字符串，并按 `format` 定义的模板格式化
		- 字符串长度应小于 `max_size`
		- 返回字符串长度，超过该长度或格式化失败返回 -1
- `time_t mktime(struct tm *tm_ptr)`：将一个 `tm` 结构体转化为 `time_t`
# 非本地跳转

`setjmp.h` 头文件提供类似 `goto` 的跳转机制甚至可以跨作用域跳转。

- `int setjmp(jmp_buf state)`：初始化一个跳转上下文；此时调用的函数称为顶层函数
	- 若用于初始化 `jmp_buf` 变量，返回值为 0
	- 若在其他地方跳转回来，此时返回值为 `longjmp` 函数 `value` 参数的值
- `void longjmp(jmp_buf state, int value)`：跳转到 `state` 指定位置
	- 跳转到 `setjmp` 刚执行完的场景，并将其返回值置为 `value`
# 信号

程序中大多数事件都是程序本身产生的，例如各种语句和输入输出等。还有一些特殊事件由系统或其他程序产生，如中断程序等。

*信号（signal）* 表示一种事件，可能异步发生，有点类似中断。当遇到某个事件后，程序需要做出反应，或执行默认行为。

标准没有规定信号的缺省行为，但通常为终止程序。`signal.h` 头文件记录了标准定义的信号和处理方法。

| 信号 | 说明 |
| ---- | ---- |
| `SIGABRT` | 程序请求异常终止，常由 `abort` 函数引发 |
| `SIGFPE` | 算术错误，常见有算术上溢、下溢，除零错误 |
| `SIGILL` | 非法指令，可能由于不正确的编译器设置（CPU指令集设置错误）或程序执行流错误（使用未初始化的函数指针调用函数） |
| `SIGSEGV` | 内存非法访问，如访问非法地址或违反内存对齐的边界要求 |
| `SIGINT` | 异步，收到交互性注意信号，常因为用户试图中断程序 |
| `SIGTERM` | 异步，收到终止程序请求，通常不配备信号处理函数 |

- `int raise(int sig)`：引发一个信号
- `void (*signal(int sig, void(*handler)(int) ) )(int)`：指定信号处理函数
	- `sig`：被处理信号
	- `handler`：信号处理函数指针
	- `SIG_DFL`：可作为 `handler` 传入，表示默认函数
	- `SIG_IGN`：可作为 `handler` 传入，表示忽略信号

信号处理函数相对于普通函数，存在一些限制：
- 所有异步信号的处理函数不能用除 `signal.h` 外的任何库函数
- 无法访问除 `volatile sig_atomic_t` 外的其他静态变量
- 除 `SIGFPE` 外，处理函数返回后从信号发生点恢复执行
# 打印可变参列表

打印可变参数需要同时导入 `stdio.h` 和 `stdarg.h`
- `int vprintf(const char *format, va_list arg)`
- `int vfprintf(FILE *stream, const char *format, va_list arg)`
- `int vsprintf(char *buffer, const char *format, va_list arg)`
# 执行环境

- `stdlib.h`：定义了一些退出程序的方法和系统信息方法
	- `void abort()`：表示不正常的终止程序，引发 `SIGABRT` 信号
	- `void atexit(void (func)(void))`：将某函数注册为退出函数，在程序退出时调用
	- `void exit(int status)`：正常终止程序，会按倒序依此调用 `atexit` 注册的函数
	- `char *getenv(const char *name)`：获取当前系统环境，包括环境变量
	- `int system(const char *command)`：由系统执行命令（cmd、bash）
		- 返回值由编译器定义
		- `system(NULL)` 的返回值表示是否存在命令处理器
- `assert.h`：断言，声明某个变量应当为真，否则产生异常
	- `void assert(int expression)`
		- `assert(expression)`：宏定义版
	- 当存在 `#define NDEBUG` 标记时，忽略所有断言
# locale

C 支持设置 locale，这可能会影响部分库的行为，以适应不同地区的使用习惯（如语言、时间等）。

- `char *setlocale(int category, const char *locale)`
	- 设置整个或部分 locale
	- `locale==NULL` 时返回当前 locale 值

| category | 说明 |
| ---- | ---- |
| LC_ALL | 全局 locale |
| LC_COLLATE | 对照序列，影响 `strcoll` 与 `strxfrm` 的行为 |
| LC_CTYPE | 影响 `ctype.h` 中函数使用的字符类型分类信息 |
| LC_MONETARY | 影响格式化货币使用符号 |
| LC_NUMERIC | 影响非货币的数字字符和小数点符号 |
| LC_TIME | 影响 `strftime` 函数行为 |
