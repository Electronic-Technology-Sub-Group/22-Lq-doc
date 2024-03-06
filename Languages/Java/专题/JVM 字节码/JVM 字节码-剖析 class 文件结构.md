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

![[Pasted image 20240306093746.png]]

> [!note]
> 很多文件都以恒定的几个字节开头作为魔数：
> - PDF：`0x255044462D`，`%PDF-`
> - png：`0x89504E47`，`\x89PNG`
## 版本号

版本号为魔数后的 4 个字节，分别表示副版本号 `Minor Version` 和主版本号 `Major Version`。

![[Pasted image 20240306093847.png]]

主版本号 52（0x34） 表示 Java8，61（0x3D） 表示 Java17。当文件版本号高于 JVM 版本时产生 `UnsupportedClassVersionError` 异常
## 常量池

存放字符串和较大的整数等，作用类似 C 的符号表，结构为：

```
cp_info {
  u2 constant_pool_count;
  cp_info constant_pool[constant_pool_count - 1];
}
```

`constant_pool_count` 为常量池大小，表示共有 n-1 个常量（下标从 1 到 n-1）。

`cp_info` 是一个结构体，其中 `info` 具体类型与 `tag` 有关。19、20 两种 `tag` 是 Java9 增加的（用于模块）。

```
cp_info {
  u1 tag;
  ? info;
}
```

常量池内存储的主要有两种：常量和符号引用。
### 常量

包括各类型字面量，final 声明的常量，基本数据类型值和其他

| tag | 类型                    | 类型     | Java 类型 |
| --- | --------------------- | ------ | ------- |
| 1   | CONSTANT_Utf8_info    | 字符串常量  | 字符串字面量  |
| 3   | CONSTANT_Integer_info | 4 字节整型 | int     |
| 4   | CONSTANT_Float_info   | 4 字节浮点 | float   |
| 5   | CONSTANT_Long_info    | 8 字节整型 | long    |
| 6   | CONSTANT_Double_info  | 8 字节浮点 | double  |

- 4 字节整型与浮点：直接存储

```
CONSTANT_Integer_info, CONSTANT_Float_info {
 u1 tag = 3, 4;
 u4 bytes;
}
```

- 8 字节整型与浮点

```
CONSTANT_Long_info, CONSTANT_Double_info {
  u1 tag = 5, 6;
  u8 bytes; // u4 high_byte + u4 low_byte
}
```

- 字符串字面量：使用 MUTF-8 编码存储
	- ASCII 字符（0x0001-0x007F），使用一个字节保存一个字符
	- 0x0080-0x07FF，使用两个字节表示，`0xXXXXXXXX` -> `110XXXXX 10XXXXXX`
	- 0x0000 0800-0x0000 FFFF：三字节，`0xXXXXXXXX` -> `1110XXXX 10XXXXXX 10XXXXXX`
	- 0x0001 0000-0x0010 FFFF：四字节，`0xXXXXXXXX` -> `11110XXX 10XXXXXX 10XXXXXX 10XXXXXX`
- UTF-8 与 MUTF-8 之间的区别主要在于：
	- `\0` 使用两个字节表示，保证字符串中不会出现空字符
	- 没有四字节的表示方式，在 U+FFFF 之上的字符（如 emoji 表情），使用代理对（两个字符）表示

```
CONSTANT_Utf8_info {
  u1 tag = 1;
  u2 length;
  u1 bytes[length];
}
```
### 符号引用

类完全限定符，字段、方法名称和描述符

| tag | 类型                               | 说明          |
| --- | -------------------------------- | ----------- |
| 7   | CONSTANT_Class_info              | 类或接口的完全限定名  |
| 8   | CONSTANT_String_info             | String 常量   |
| 9   | CONSTANT_Fieldref_info           | 类中字段引用      |
| 10  | CONSTANT_Methodref_info          | 类中方法引用      |
| 11  | CONSTANT_InterfaceMethodref_info | 类实现的接口方法    |
| 12  | CONSTANT_NameAndType_info        | 字段或方法的名称和类型 |
| 15  | CONSTANT_MethodHandle_info       | 方法句柄        |
| 16  | CONSTANT_MethodType_info         | 方法类型        |
| 17  | CONSTANT_Dynamic_info            | 动态计算常量      |
| 18  | CONSTANT_InvokeDynamic_info      | 动态方法调用点     |
| 19  | CONSTANT_Module_info             | 模块属性        |
| 20  | CONSTANT_Package_info            | 模块中导出的包属性   |

- 字符串引用：String 类型字符串常量，指向一个字符串字面量

```
CONSTANT_String_info {
  u1 tag = 8;
  u2 string_index;  // 指向一个 CONSTANT_Utf8_info
}
```

- 类与接口的完全限定名

```
CONSTANT_Class_info {
  u1 tag = 7;
  u2 name_index;   // 指向一个 CONSTANT_Utf8_info 表示类完全限定名
}
```

- 字段与方法描述：包含声明该成员的类或接口、成员名和描述符两部分

```
CONSTANT_Fieldref_info, CONSTANT_Methodref_info, CONSTANT_InterfaceMethodref_info {
  u1 tag = 9, 10, 11;
  u2 class_index;  // 指向一个 CONSTANT_Class_info 表示成员所以在类或接口信息
  u2 name_and_type_index;  // 指向一个 CONSTANT_NameAndType_info 表示成员描述符
}

CONSTANT_NameAndType_info {
  u1 tag = 12;
  u2 name_index;          // 指向一个 CONSTANT_Utf8_info 表示成员名称
  u2 descriptor_index;    // 指向一个 CONSTANT_Utf8_info 表示字段或方法描述符
}
```

- 方法句柄

```
CONSTANT_MethodHandle_info {
  u1 tag = 15;
  u1 reference_kind;    // 1-9，表示不同的方法句柄类型
  u2 reference_index;   // 对常量池的有效索引
}
```

- 方法类型

```
CONSTANT_MethodType_info {
  u1 tag = 16;
  u2 descriptor_index;  // 指向一个 CONSTANT_Utf8_info 表示方法描述符
}
```

- invokedynamic

```
CONSTANT_Dynamic_info, CONSTANT_InvokeDynamic_info {
  u1 tag = 17, 18;
  u2 bootstrap_method_attr_index; // 指向一个引导方法表 bootstrap_methods[] 的索引
  u2 name_and_type_index;         // 指向一个 CONSTANT_NameAndType_info 表示方法
}
```

- 模块与包属性

```
CONSTANT_Module_info, CONSTANT_Package_info {
  u1 tag = 19, 20;
  u2 name_index;    // 指向一个 CONSTANT_Utf8_info 表示模块或包名
}
```

完全限定符、描述符详见 [[#描述符]]
## Access Flag

类访问标记，u2。共 16 个标记。这些标记并没有全部使用完成，且类、字段、方法的标记有重合复用

| 十六进制值  | 访问标记名              | 对应标记               | 可修饰对象类型 |
| ------ | ------------------ | ------------------ | ------- |
| 0x0001 | `ACC_PUBLIC`       | `public`           | 类，字段，方法 |
| 0x0002 | `ACC_PRIVATE`      | `private`          | 字段，方法   |
| 0x0004 | `ACC_PROTECTED`    | `protected`        | 字段，方法   |
| 0x0008 | `ACC_STATIC`       | `static`           | 字段，方法   |
| 0x0010 | `ACC_FINAL`        | `final`            | 类，字段，方法 |
| 0x0020 | `ACC_SUPER`        | `super`            | 类       |
| 0x0020 | `ACC_SYNCHRONIZED` | `synchronized`     | 方法      |
| 0x0040 | `ACC_VOLATILE`     | `volatile`         | 字段      |
| 0x0040 | `ACC_BRIDGE`       | 无，编译器生成的 bridge 方法 | 方法      |
| 0x0080 | `ACC_TRANSIENT`    | `transient`        | 字段      |
| 0x0080 | `ACC_VARARGS`      | 无，使用了变长参数          | 方法      |
| 0x0100 | `ACC_NATIVE`       | `native`           | 方法      |
| 0x0200 | `ACC_INTERFACE`    | `interface`        | 类       |
| 0x0400 | `ACC_ABSTRACT`     | `abstract`         | 类，方法    |
| 0x0800 | `ACC_STRICT`       | `strictfp`         | 方法      |
| 0x1000 | `ACC_SYNTHETIC`    | 无，标记代码由编译器生成       | 类，字段，方法 |
| 0x2000 | `ACC_ANNOTATION`   | `@interface`       | 类       |
| 0x4000 | `ACC_ENUM`         | `enum`             | 类，字段    |

## 继承关系

access_flag 之后是用于标记继承关系的三个常量池索引，每个值都是 u2：
- this_class：类或接口信息，指向常量池中的一个 `CONSTANT_Class_info` 结构
- super_name：基类信息，除 `Object` 外均非 0
- interfaces：实现（或接口继承的）的接口数，后接 n 个 u2 指向接口
## 字段表

字段包括类级和实例变量，不包含基类的变量，但可能出现原本代码中不存在的字段。这些字段通常由编译器产生，如内部类中为保持对外部类的访问性，持有的外部类实例。

```
{
  u2 fields_count;
  field_info fields[fields_count];
}

field_info {
  u2 access_flags;
  u2 name_index;
  u2 descriptor_index;
  u2 attributes_count;
  attribute_info[] attributes[attributes_count];
}
```

字段描述符为描述一个变量类型的描述符                    

| 类型      | 字段描述符 | 说明                                                                 |     |
| ------- | ----- | ------------------------------------------------------------------ | --- |
| byte    | B     |                                                                    |     |
| char    | C     |                                                                    |     |
| double  | D     |                                                                    |     |
| float   | F     |                                                                    |     |
| int     | I     |                                                                    |     |
| long    | J     |                                                                    |     |
| short   | S     |                                                                    |     |
| boolean | Z     |                                                                    |     |
| void    | V     | VoidDescriptor，用于函数返回值                                             |     |
| 引用      | L     | `L` + 完全限定符 + `;`，如 `Ljava/lang/String;`                           |     |
| 数组      | `[`   | 有几维就有几个 `[`，如 `int[]` 为 `[I`，`Object[][]` 为 `[[Ljava/lang/Object;` |     |
一个类的全类名，将 `.` 替换为 `;` 即类的完全限定符，如 `java.lang.String` 的完全限定符为 `java/lang/String`

字段属性最常见的是 `ConstantValue`，详见[[#属性表]]
## 方法表

方法同样不包含基类方法，但会包含一些编译器自动生成的代码。某些方法有一些特殊名称：
- 构造函数名称为 `<init>`
- 类构造器名称为 `<cinit>`

方法包含一个 u2 类型存储方法数量，以及一个 method_info 表表示方法信息。

```
{
  u2 methods_count;
  method_info methods[fields_count];
}

method_info {
  u2 access_flags;
  u2 name_index;
  u2 descriptor_index;
  u2 attributes_count;
  attribute_info[] attributes[attributes_count];
}
```

方法代码在属性段的 Code 属性中。
## 属性表

```
{
  u2 attributes_count;
  attribute_info[] attributes[attributes_count];
}

attribute_info {
  u2 attribute_name_index;  // 指向一个 CONSTANT_Utf8_info 表示属性名
  u4 attribute_length;
  u1[] info[attribute_length];
}
```

属性表广泛存在于类、字段、方法中，不同虚拟机可以有自己的属性。
### ConstantValue

```
ConstantValue_attribute {
  u2 attribute_name_index;
  u4 attribute_length;
  u2 constantvalue_index;
}
```
### Code


### 常见属性表

| 属性名                                  | 位置           | 含义                      |
| ------------------------------------ | ------------ | ----------------------- |
| Code                                 | 方法           | 翻译成字节码的 Java 代码         |
| ConstantValue                        | 字段           | static 修饰的常量            |
| Deprecated                           | 类、方法、字段      | 被 deprecated 声明         |
| Exceptions                           | 方法           | 异常列表                    |
| EnclosingMethod                      | 类            | 当类为匿名类或内部类时，表示所在外围方法    |
| InnerClasses                         | 类            | 内部类列表                   |
| LineNumberTable                      | Code         | 源码行号与字节码指令的对应关系         |
| LocalVariableTable                   | Code         | 局部变量表                   |
| StackMapTable                        | Code（最多一个）   | 用于类加载时的类型检查验证           |
| Signature                            | 类、方法、字段      | 泛型签名                    |
| SourceFile                           | 类            | 源文件名                    |
| SourceDebugExtension                 | 类（最多一个）      | 额外调试信息                  |
| Synthetic                            | 类、方法、字段      | 修饰成员由编译器而非用户代码生成        |
| LocalVariableTypeTable               | 类            | 描述泛型参数化类型               |
| RuntimeVisibleAnnotations            | 类、方法、字段      | 运行时可见的注解                |
| RuntimeInvisibleAnnotations          | 类、方法、字段      | 运行时不可见注解                |
| RuntimeVisibleParameterAnnotations   | 方法           | 运行时可见的参数注解              |
| RuntimeInvisibleParameterAnnotations | 方法           | 运行时不可见的参数注解             |
| AnnotationDefault                    | 方法           | 注解元信息默认值                |
| BootstrapMethod                      | 类            | invokedynamic 引导方法      |
| RuntimeVisibleTypeAnnotations        | 类、方法、字段、Code | 运行时可见的类注解（JSR 308）      |
| RuntimeInvisibleTypeAnnotations      | 类、方法、字段、Code | 运行时不可见的类注解（JSR 308）     |
| MethodParameters                     | 方法           | 支持将方法名编译进 Class         |
| Module                               | 类            | 模块名称及相关信息               |
| ModulePackage                        | 类            | 模块中被 exports 或 opens 的包 |
| ModuleMainClass                      | 类            | 模块主类                    |
| NestHost                             | 类            | 内部类的宿主类                 |
| NestMembers                          | 类            | 宿主类的嵌套类                 |
