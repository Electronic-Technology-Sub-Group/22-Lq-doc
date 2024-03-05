# class 文件结构

使用 javac 将源文件编译成 `.class` 文件，使用十六进制工具即可打开查看。也可以或通过 javap 反编译，或通过 jclasslib 等可视化工具直接查看 `.class` 文件。

![[Pasted image 20240306012524.png]]

> [!note]- 使用 javap 反编译 class 文件
> Class 字节码可通过 `javap` 从二进制格式反编译成可读文件格式。
> ```bash
> javap [options] class文件1 class文件2 ...
> ```
> - -v，-verbose：显示包含常量池的完整信息
> - -l：只输出行号和局部变量表
> - -public，-protected，-private/-p：显示 public、protected 或 private 成员
> - -c：只输出代码
> - -s：只输出签名
> - -J：VM 选项
> 
> 不加选项则反编译成 Java 代码，通常使用 -v 查看完整的字节码信息。

数据结构：
- 基本类型：`u1`，`u2`，`u4` 分别表示 1,2,4 字节无符号整形
- 表（table）：保存相同类型数据的变长结构，由一个代表长度的表头和紧跟着的 n 个数据组成
- 结构体：存储一组数据的数据结构

一个 Java8 的 class 文件由十个部分组成

| 部分    | 名称                  |
| ----- | ------------------- |
| 魔数    | Magic Number        |
| 版本号   | Minor&Major Version |
| 常量池   | Constant Pool       |
| 类访问标记 | Access Flag         |
| 类索引   | This Class          |
| 超类索引  | Super Class         |
| 接口表索引 | Interface           |
| 字段表   | Field               |
| 方法表   | Method              |
| 属性表   | Attribute           |
![[Pasted image 20230824222144.png]]
## 魔数

魔数是 `JVM` 识别 `.class` 文件的标志，位于 `class` 文件的前 4 个字节，固定是 `0xCAFEBABE`。若不是这四个字节会产生 `ClassFormatError` 异常

> [!note]
> 很多文件都以恒定的几个字节开头作为魔数：
> - PDF：`0x255044462D`，`%PDF-`
> - png：`0x89504E47`，`\x89PNG`
## 版本号

版本号为魔数后的 4 个字节，分别表示副版本号 `Minor Version` 和主版本号 `Major Version`。

主版本号 52（0x34） 表示 Java8，61（0x3D） 表示 Java17。当文件版本号高于 JVM 版本时产生 `UnsupportedClassVersionError` 异常
## 常量池

存放字符串和较大的整数等，作用类似 C 的符号表，结构为：

```
cp_info {
  u2 constant_pool_count;
  cp_info constant_pool[constant_pool_count - 1];
}
```
## Access Flag
## this_class, super_name, interfaces
## 字段表
## 方法表
## 属性表