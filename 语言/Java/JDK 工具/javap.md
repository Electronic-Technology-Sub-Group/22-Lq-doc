`javap` 是 Java 提供的工具，可以查看 class 文件内部细节。Class 字节码可通过 `javap` 从二进制格式反编译成可读文件格式。

```console
$ javap [options] class文件1 class文件2 ...
```
* -v，-verbose：显示包含常量池的完整信息
* -l：只输出行号和局部变量表
    * 局部变量表要求 `javac` 编译时带有 `-g` 属性
* -public，-protected，-private/-p：显示 public、protected 或 private 成员
    * 默认显示 `public`，`protected`，和默认级别的方法
* -c：输出代码的字节码指令
* -s：只输出类型描述符签名
* -J：VM 选项

不加选项则反编译成 Java 代码，通常使用 -v 查看完整的字节码信息。
