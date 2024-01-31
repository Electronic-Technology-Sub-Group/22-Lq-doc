# 错误报告

任何一个操作系统执行的任务都有失败的可能，尤其是 IO 操作。当系统失败时，标准库通过 `errno` 变量将信息传递给程序。这个变量位于 `errno.h` 中。

**当且仅当库函数失败时才会设置 `errno`，程序错误不会产生**

`stdio.h` 中定义了 `perror` 函数用于简化错误报告类型：

```c
void perror(const char *message);
```

当 `message` 指向一个非空字符串，则打印该字符串，后接一个用于描述当前 `errno` 的信息
# 终止执行

当 `main` 函数结束时会返回一个 `int`，事实上该返回值会最终传递给 `exit` 函数以结束程序。

`stdlib.h` 头文件定义了 `exit` 函数，程序中也可以手动调用以立即退出程序。

```c
void exit(int status);
```

`status` 指示程序是否正常结束：

- EXIT_SUCCESS：程序成功执行，正常结束
- EXIT_FAILURE：程序运行失败
- 其他：不同编译器意义不同
# IO
## 概念

计算机每种处理输入输出的设备，包括硬盘驱动器、光驱、网络通信、视频适配器等，与操作系统 交互有不同的特性和操作协议。操作系统统一处理负责这些设备的通信细节，并将其抽象成更加简洁统一的程序接口
### 流

对于 C 语言，所有的 IO 操作都可以抽象成简单的字节移入移出的操作。这类字节流称为*流（stream）*。程序只需要创建正确的流并按正确的顺序读写数据即可。

大部分流是 *完全缓冲（fully buffered）* 的，即写入、读出操作并不直接操作对应的设备，而是针对程序的一块内存（称为 *缓冲区（buffer）* ）以提高效率。通常，用于输出的缓冲区只有被写满或手动调用 `flush` 命令时才会将数据写入到设备或文件中，输入则会在缓冲区空时再从设备或文件读取下一部分数据以填充缓冲区。

只有操作系统确定与交互设备无联系时才会使用完全缓冲，其他设备的策略与编译器实现有关。一个常见策略是将输入输出缓冲区联系在一起，在输入时同时刷新输出缓冲区。

由于缓冲区输出不是立即的，因此 `printf` 不一定是在运行时就立即输出（但输出顺序是一定的）。这可能不太便于调试，可使用 `fflush` 立即输出

```c
printf("...");
fflush(stdout);
```

流按内容分为两种：文本流和二进制流，读写内容分别是文本信息和二进制数据。
- 文本流库函数可以完成诸如不同系统换行符差异的翻译
- 二进制流则是完全按照实际 IO 数据进行写入和读出
### 文件

`FILE` 数据结构存于 `stdio.h` 头文件中，用于访问一个流。一般来说这是一个指针。

标准 C 程序运行时都至少提供三个流，其类型都是 `FILE*`
- `stdin`：标准输入流 - 默认输入来源
- `stdout`：标准输出流 - 默认输出设置
- `stderr`：标准错误流 - 错误信息写入位置，默认与 `stdout` 相同

很多系统（DOS、UNIX）都支持通过以下方法重定向输入、输出流

```bash
# 输入流：[data] 文件
# 输出流：[answer] 文件
program < data > answer
```
### 常量

- `EOF`：文件末尾；一些 IO 函数（`fclose`，`fputs` 等）发生错误也返回这个值
	- IO 函数发生错误返回 EOF，未发生错误时返回 0
- `MAX_LINE_LENGTH`：按行读取的缓冲区大小
- `FOPEN_MAX`：同时可以打开的文件数量最大值（包括三个标准流），至少为 8
- `FILENAME_MAX`：编译器支持的最长路径名
## 函数
### 读写函数

对于不同数据类型，有不同的函数族：

| 类型 | 输入 | 输出 | 描述 |
| ---- | ---- | ---- | ---- |
| 字符 | getchar | putchar | 读写单个字符 |
| 文本行 | gets | puts | 按行的输入输出文本 |
| 格式化文本 | scanf | printf | 格式化输入输出文本 |
| 二进制数据 | fread | fwrite | 读写二进制数据 |

具体函数族包含的函数：

| 函数族 | 用于所有流 | 仅用于标准流 | 用于内存字符串 |
| ---- | ---- | ---- | ---- |
| getchar | fgetc，getc | getchar | （直接使用下标） |
| putchar | fputc，putc | putchar | （直接使用下标） |
| gets | fgets | gets | （strcpy） |
| puts | fputs | puts | （strcpy） |
| scanf | fscanf | scanf | sscanf |
| printf | fprintf | printf | sprintf |
- `getc`，`putc`，`getchar`，`putchar` 不是函数，是宏
- `int ungetc(int char, FILE *stream);` 可以将之前读出的字符重新塞回缓冲区中
### 流操作函数

- `FILE *fopen(const char *name, const char *mode);`
	- 打开一个流。`name` 是文件名，`mode` 为模式

| 操作对象 | 读取 | 写入（覆盖） | 追加 | 更新 |
| ---- | ---- | ---- | ---- | ---- |
| 文本数据 | r | w | a | a+ |
| 二进制数据 | rb | wb | ab | ab+ |
- `FILE *freopen(const char *name, const char *mode, FILE *stream);`
	- 重新打开流：先试图关闭已知流，然后重新按前面的参数打开
	- 关闭或重新打开失败时返回 NULL
### 刷新、定位函数

- `int fflush(FILE *stream)`：强迫刷新输出缓冲区
- `long ftell(FILE *stream)`：返回当前流读写位置
	- 二进制流：当前位置距离文件起始位置间的字节数
	- 文本流：可用于 `fseek` 的偏移量，但不一定准确反应当前位置到起始位置的字符数
- `int fseek(FILE *stream, long offset, int from)`：设置流读写位置，`from` 表示相对位置
	- 副作用：`ungetc` 返回的字符被废弃

| `from` 值 | 相对位置 | offset |
| ---- | ---- | ---- |
| SEEK_SET | 文档开头 | 非负值 |
| SEEK_CUR | 当前位置 | 可正可负 |
| SEEK_END | 文档末尾 | 可正可负，正值定位到文件尾之后 |
- `void rewind(FILE *stream)`：将读写指针设置回流起始位置，并清除错误提示
- `int fgetpos(FILE *stream, fpos_t *position)`
	- 将文件流当前位置存储到 `position` 中
- `int fsetpos(FILE *stream, const fpos_t *position)`
	- 将文件位置还原到某个流中
### 改变缓冲区

- `setbuf(FILE *stream, char *buf)`
	- 将设置流缓冲区，`buf` 长度必须是 `BUFSIZ`，该常量定义于 `stdio.h`
	- `buf=NULL` 时，关闭流缓冲区，直接读写流
- `setvbuf(FILE *stream, char *buf, int mode, size_t size)`
	- 设置流缓冲区，可以定义缓冲区类型和大小，通常应该为 `BUFSIZ` 或其整数倍
	- `buf=NULL` 时，`size` 必须为 0
	- `mode` 指定缓冲区类型

| `mode` 值 | 说明 |
| ---- | ---- |
| `_IOFBF` | 指定一个完全缓冲流 |
| `_IONBF` | 指定一个不缓冲流 |
| `_IOLBF` | 指定一个行缓冲流（写入换行符时刷新） |
### 流状态信息

- `int feof(FILE *stream)`：流是否位于文件尾
- `int ferror(FILE *stream)`：流是否有错误状态
- `void clearerr(FILE *stream)`：清除流错误状态
### 临时文件

- `FILE *tmpfile(void)`：以 `wb+` 模式创建一个临时文件，文件被关闭或程序结束时自动删除
- `char *tmpnam(char *name)`：创建临时文件名称，每次调用返回一个新名称
	- `name` 为 `NULL` 时，返回一个指向静态数组的指针，即临时文件名
	- `name` 非 `NULL` 时，默认为长度不小于 `L_tmpnam` 的字符串指针为文件名
	- 在不超过 `TMP_MAX` 次调用下，每次返回的值保证不重复且不存在
### 文件操纵函数

- `int remove(const char *name)`：删除文件
- `int rename(const char *old, const char *new)`：重命名或移动文件