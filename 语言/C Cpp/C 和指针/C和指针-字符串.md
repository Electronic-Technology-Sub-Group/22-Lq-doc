# 字符串

- 操作多个字符串的字符串函数，若操作的字符串地址有重叠部分，其结果是未定义的
- 字符串常量（表示为 `char*`）存放在静态存储区，即使没有加 const 也是无法修改的。若要修改字符串值需要使用 `char[]`，如 `char[] = "hello"`

常见字符串函数前、中缀意义

| 内容 | 位置 | 意义 |
| :--- | ---- | ---- |
| str | 前缀 | 字符串函数 |
| strn | 前缀 | 需要提供字符串长度，而不是根据 `NUL` 判断字符串结束 |
| mem | 前缀 | 操作内存的函数 |
| r | 中缀 | 查找时，从后往前查找 |
| c | 中缀 | 查找时，匹配不满足给定条件的情况 |
# 字符串长度

- `strlen(str)` 返回值类型为 `size_t`，是一个无符号整形，因此与 0 进行大小比较是没有意义的

```cpp
const char* str1, str2;

if (strlen(str1) - strlen(str2) >= 0) {
   // always true
}

if (strlen(str1) - 10 >= 0) {
   // always true
}
```

以上两个 `if` 判断结果总是 `true`，因为无符号与有符号运算完成后结果为无符号，总是大于 0。可能解决的方法有：
- 直接进行大小比较，不进行减法操作
- 运算时转换为有符号整形，但可能溢出（？但一般不会出现）
# 长度受限的字符串

- 带有 `strn` 前缀的字符串函数需要指定字符串长度，如 `strncpy`，`strncat` 等
- 长度受限字符串函数不会检查字符串中的 `NUL`（`\0`）字符，因此在继续使用 `str` 开头的函数或 `printf("%s")` 时需要特别注意
	- `strncat` 例外，它总会在结果字符串后增加一个 `\0`
# 字符串查找

- `strchr`：类似 `strstr`，但针对字符
- `strpbrk`：查找字符串 str 中任意一个字符串 group 中的字符的位置；否则返回 `NULL`
	- `char* strpbrk(const char* str, const char* group)`

```c
void printCharPosition(const char *str, const char *p) {
    if (p) printf("Pos at %lld\n", p - str);
    else printf("Not found\n");
}

// in main function
const char *string = "Hello there, honey";
char *ans = strpbrk(string, "aeiou");
printCharPosition(string, ans);
```

- `strspn`：查找 `buffer` 中满足 `group` 中的字符的前缀长度
	- `size_t strspn(const char* buffer, const char* group)`
	- `size_t strcspn(const char* buffer, const char* group)`

```c
size_t len1, len2;
const char *buffer = "25,142,330,Smith,J,239-4123";
// len("25") = 2
len1 = strspn(buffer, "0123456789");
// len("25,142,330,") = 11
len2 = strspn(buffer, ",0123456789");
```

- `strtok`：根据字符串 `sep` 中的任意字符将一个字符串 `str` 分割为多个字符串（每个子串被称为 `token`），并将标记丢弃 **（会改变字符串本身）**
	- `char* strtok(char *str, const char* sep)`
	- 第一个参数 `str` 可以为 `NULL`，默认为上次搜索位置 + 1
	- 类似于其他语言的 `split` 方法，但会修改字符串本身

```c
// 注意不能使用 char str*，否则字符串存在常量区， strtok 无法写入
char str[] = "Hello there, honey";
char *sep = " ,";
size_t len = strlen(str);

for (char *token = strtok(str, sep); token; token = strtok(NULL, sep)) {
    // 输出切分后的字符串
    printf("token: %s\n", token);
    // 输出切分后的原始字符串，使用 | 表示 NUL
    printf("origin: ");
    for (int i = 0; i < len; ++i) {
        if (str[i]) {
            printf("%c", str[i]);
        } else {
            printf("|");
        }
    }
    printf("\n\n");
}
```

输出结果：

```
token: Hello
origin: Hello|there, honey

token: there
origin: Hello|there| honey

token: honey
origin: Hello|there| honey
```
# 异常信息

当调用系统函数时，若失败有时会返回一个整形错误代码 `errno`，该标记指向一个用于描述错误的字符串指针，通过 `strerror` 获取

```
char* strerror(int errno);
```
# 字符操作

在 `ctype.h` 中存在很多字符操作相关函数。使用标准库函数可以提高可移植性

| 函数 | 作用 |
| ---- | ---- |
| iscntrl | 判断字符为控制字符 |
| isspace | 判断字符为空白字符（` `，`\f`，`\n`，`\r`，`\t`，`\v`） |
| isdigit | 判断字符为十进制数字 0-9 |
| isxdigit | 判断字符为十六进制数字 0-9，a-f，A-F |
| islower | 判断字符为小写字母 a-z |
| isupper | 判断字符为大写字母 A-Z |
| isalpha | 判断字符为字母，a-z，A-Z |
| isalnum | 判断字符为字母或数字，a-z，A-Z，0-9 |
| ispunct | 判断字符为标点符号，任何不属于字母或数字的图形字符 |
| isgraph | 判断字符为任何图形字符 |
| isprint | 判断字符为可打印字符，包括图形字符和空白字符 |
| toupper | 将小写字母转化为大写字母 |
| tolower | 将大写字母转化为小写字母 |
# 内存操作

`string.h` 还包括一组直接操作内存的函数

- `memcpy` 操作源和目标内存有重叠时结果未定义，但 `memmove` 允许源和目标重叠