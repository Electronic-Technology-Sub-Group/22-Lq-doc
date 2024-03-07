---
状态: 未完成，已弃用
未完成部分: StackMapTable
---
Class 字节码指 Java 或其他运行于 JVM 平台上的语言编译的产物，通常为 `.class` 文件。class 文件可以通过 WinHex 等二进制编辑器打开，或通过 javap 反编译，或通过 jclasslib 等可视化工具查看。
# javap

Class 字节码可通过 `javap` 从二进制格式反编译成可读文件格式。

```bash
javap [options] class文件1 class文件2 ...
```
- -v，-verbose：显示包含常量池的完整信息
- -l：只输出行号和局部变量表
- -public，-protected，-private/-p：显示 public、protected 或 private 成员
- -c：只输出代码
- -s：只输出签名
- -J：VM 选项

不加选项则反编译成 Java 代码，通常使用 -v 查看完整的字节码信息。以下所有代码以 Java 17 为例

```java
package untitled.lq2007;  
  
public class Main {  
    public static void main(String[] args) {  
        System.out.println("Hello World!");  
    }  
}
```

经过 javap -v 反编译后结果为

```java
Classfile /D:/projects/untitledJava/out/production/untitledJava/untitled/lq2007/Main.class
  Last modified 2023年8月24日; size 548 bytes
  SHA-256 checksum ff71a73d0763f9ff11a8df4e07763dbe388e03d2d5e8ebbb393268467ed62b60
  Compiled from "Main.java"
public class untitled.lq2007.Main
  minor version: 0
  major version: 61
  flags: (0x0021) ACC_PUBLIC, ACC_SUPER
  this_class: #21                         // untitled/lq2007/Main
  super_class: #2                         // java/lang/Object
  interfaces: 0, fields: 0, methods: 2, attributes: 1
Constant pool:
   #1 = Methodref          #2.#3          // java/lang/Object."<init>":()V
   #2 = Class              #4             // java/lang/Object
   #3 = NameAndType        #5:#6          // "<init>":()V
   #4 = Utf8               java/lang/Object
   #5 = Utf8               <init>
   #6 = Utf8               ()V
   #7 = Fieldref           #8.#9          // java/lang/System.out:Ljava/io/PrintStream;
   #8 = Class              #10            // java/lang/System
   #9 = NameAndType        #11:#12        // out:Ljava/io/PrintStream;
  #10 = Utf8               java/lang/System
  #11 = Utf8               out
  #12 = Utf8               Ljava/io/PrintStream;
  #13 = String             #14            // Hello World!
  #14 = Utf8               Hello World!
  #15 = Methodref          #16.#17        // java/io/PrintStream.println:(Ljava/lang/String;)V
  #16 = Class              #18            // java/io/PrintStream
  #17 = NameAndType        #19:#20        // println:(Ljava/lang/String;)V
  #18 = Utf8               java/io/PrintStream
  #19 = Utf8               println
  #20 = Utf8               (Ljava/lang/String;)V
  #21 = Class              #22            // untitled/lq2007/Main
  #22 = Utf8               untitled/lq2007/Main
  #23 = Utf8               Code
  #24 = Utf8               LineNumberTable
  #25 = Utf8               LocalVariableTable
  #26 = Utf8               this
  #27 = Utf8               Luntitled/lq2007/Main;
  #28 = Utf8               main
  #29 = Utf8               ([Ljava/lang/String;)V
  #30 = Utf8               args
  #31 = Utf8               [Ljava/lang/String;
  #32 = Utf8               SourceFile
  #33 = Utf8               Main.java
{
  public untitled.lq2007.Main();
    descriptor: ()V
    flags: (0x0001) ACC_PUBLIC
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: return
      LineNumberTable:
        line 3: 0
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       5     0  this   Luntitled/lq2007/Main;

  public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
    flags: (0x0009) ACC_PUBLIC, ACC_STATIC
    Code:
      stack=2, locals=1, args_size=1
         0: getstatic     #7                  // Field java/lang/System.out:Ljava/io/PrintStream;
         3: ldc           #13                 // String Hello World!
         5: invokevirtual #15                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
         8: return
      LineNumberTable:
        line 5: 0
        line 6: 8
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       9     0  args   [Ljava/lang/String;
}
SourceFile: "Main.java"

```
# 结构

![[Pasted image 20230824222144.png]]
class 字节码的基本类型只有四个，u1、u2、u4、u8 分别代表 1字节、2字节、4字节、8字节无符号整型，用于表示数字、引用、UTF8字符串等

其他数据类型如 `cp_info` 等，表示一个表（结构体），表类型名习惯以 `_info` 结尾，整个字节码文件就是一张表

```
class_info {
  // 魔数与版本号
  u4 magic = 0xCAFEBABE;
  u2 minor_version;
  u2 major_version;
  // 常量池
  u2 constant_pool_count;
  cp_info[] constant_pool[constant_pool_count - 1];
  // 类信息
  u2 access_flags;
  u2 this_class;
  u2 super_class;
  u2 interfaces_count;
  u2[] interfaces[interfaces_count];
  // 方法与字段
  u2 fields_count;
  field_info[] fields[fields_count];
  u2 methods_count;
  methods_info[] methods[methods_count];
  // 属性（注解等）
  u2 attributes_count
  attribute_info attributes[attributes_count];
}
```
# 魔数与版本号

class 字节码前四字节为魔数，表示生成该字节码的编译器，固定为 0xCAFEBABE

后四个字节表示编译的 Java 版本号，与 Java 编译器版本相关，主版本号 52 表示 Java8，61 表示 Java17
# 常量池

版本号之后的一个 u2 量表示常量池计数，表示共有 n-1 个常量（对他的计数是从 1 开始的）。

每个常量由一个结构体表示，这些结构体统称为 `cp_info`，每个结构体第一个值固定为 u1 类型的 tag，表示常量类型。

```
cp_info {
  u1 tag;
}
```

常量池内存储的主要有两种：字面量和符号引用。
## 字面量

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
  u8 bytes; // 高位在前，低位在后
}
```

- 字符串与字面量

```
CONSTANT_Utf8_info {
  u1 tag = 1;
  u2 length;
  u1 bytes[length];
}
```

## 符号引用

类完全限定符，字段、方法名称和描述符

| tag | 类型                               | 说明          |     |
| --- | -------------------------------- | ----------- | --- |
| 7   | CONSTANT_Class_info              | 类或接口的完全限定名  |     |
| 8   | CONSTANT_String_info             | String 常量   |     |
| 9   | CONSTANT_Fieldref_info           | 类中字段引用      |     |
| 10  | CONSTANT_Methodref_info          | 类中方法引用      |     |
| 11  | CONSTANT_InterfaceMethodref_info | 类实现的接口方法    |     |
| 12  | CONSTANT_NameAndType_info        | 字段或方法的名称和类型 |     |
| 15  | CONSTANT_MethodHandle_info       | 方法句柄        |     |
| 16  | CONSTANT_MethodType_info         | 方法类型        |     |
| 17  | CONSTANT_Dynamic_info            | 动态计算常量      |     |
| 18  | CONSTANT_InvokeDynamic_info      | 动态方法调用点     |     |
| 19  | CONSTANT_Module_info             | 模块属性        |     |
| 20  | CONSTANT_Package_info            | 模块中导出的包属性   |     |

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

- 字段与方法引用：包含声明该成员的类或接口、成员名和描述符两部分

```
CONSTANT_Fieldref_info, CONSTANT_Methodref_info, CONSTANT_InterfaceMethodref_info {
  u1 tag = 9, 10, 11;
  u2 index;        // 指向一个 CONSTANT_Class_info 表示声明该成员的类或接口描述符
  u2 index;        // 指向一个 CONSTANT_NameAndType_info 表示成员描述符
}

CONSTANT_NameAndType_info {
  u1 tag = 12;
  u2 index;        // 指向一个 CONSTANT_Utf8_info 表示成员名称
  u2 index;        // 指向一个 CONSTANT_Utf8_info 表示字段或方法描述符
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
# 类信息

常量池后一个 u2 类型的值为该类的访问标志，即以下声明

```java 
public interface AInterface {} 
```

中的 `public interface`。常见的有 `public` 等访问修饰符，`final`，`interface`、`abstract` 等，多个访问标志使用 `|` 组合

之后是该类的继承关系。先是一个 u2 类型表示类索引，指向常量池中类全限定名。其次是一个 u2 指向常量池中的基类，除 `Object` 外该值均非 0。再往后是一个 u2 表示该类实现的接口数（若为接口，表示接口继承的其他接口数），之后是 u2 数组指向常量池中实现的接口索引。
# 字段与方法

字段包括类级和实例变量，不包含基类的变量，但可能出现原本代码中不存在的字段。这些字段通常由编译器产生，如内部类中为保持对外部类的访问性，持有的外部类实例。

字段包含一个 u2 类型存储字段数量，以及一个 field_info 表表示字段信息。

方法同样不包含基类方法，但会包含一些编译器自动生成的代码。某些方法有一些特殊名称：
- 构造函数名称为 `<init>`
- 类构造器名称为 `<cinit>`

方法包含一个 u2 类型存储方法数量，以及一个 method_info 表表示方法信息。
```
field_info, methods_info {
  u2 access_flags;
  u2 name_index;
  u2 descriptor_index;
  u2 attributes_count;
  attribute_info[] attributes[attributes_count];
}
```

方法代码在属性段的 Code 属性中。
# 属性

包含方法代码、各种泛型、调试信息等
属性结构体统称 `attribute_info`，所有的属性都符合以下结构：

```
attribute_info {
  u2 attribute_name_index;  // 指向一个 CONSTANT_Utf8_info 表示属性名
  u4 attribute_length;
  u1[] info[attribute_length];
}
```

其中，`info` 只是表示该属性内容占有 attribute_length 字节，实际不是 `u1[]` 的类型

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

- Code：方法代码

```
attribute_info {
  u2 attribute_name_index = &"Code";
  u4 attribute_length;
  // 栈帧深度最大值，任何情况下操作数栈的深度都不超过该值
  u2 max_stack;
  // 局部变量表的变量槽（Slot）数，同时生存的最大局部变量数和类型
  //   long, double 占用 2 个槽，其他基本类型或对象引用占用 1 个槽
  //   注意包含指向自身的参数 this
  u2 max_locals;
  u4 code_length;
  u1[] code[code_length];
  u2 exception_table_length;
  exception_info[] exception_table[exception_table_length];
  u2 attributes_count;
  attribute_info attributes[attributes_count];
}
```
# 字节码指令
## 异常处理

异常处理使用的不是普通跳转，而是 `TRYCATCHBLOCK` 指令。

```java
public static void sleep(long d) {
     try {
          Thread.sleep(d);
     } catch (InterruptedException e) {
          e.printStackTrace();
     }
}
```

```
TRYCATCHBLOCK try catch catch java/lang/InterruptedException
try:
     LLOAD 0
     NVOKESTATIC java/lang/Thread sleep (J)V
     RETURN
catch:
     INVOKEVIRTUAL java/lang/InterruptedException printStackTrace ()V
     RETURN
```
# 附录
## 描述符

### 完全限定符

一个类的全类名，将 `.` 替换为 `;` 即类的完全限定符，如 `java.lang.String` 的完全限定符为 `java/lang/String`
### 字段描述符

字段描述符为描述一个变量类型的描述符

| 类型     | 字段描述符 | 说明                                                                           |
| -------- | ---------- | ------------------------------------------------------------------------------ |
| char     | C          |                                                                                |
| byte     | B          |                                                                                |
| short    | S          |                                                                                |
| int      | I          |                                                                                |
| long     | J          |                                                                                |
| float    | F          |                                                                                |
| double   | D          |                                                                                |
| boolean  | Z          |                                                                                |
| void     | V          | VoidDescriptor，用于函数返回值                                                 |
| 引用类型 | L          | `L` + 完全限定符 + `;`，如 `Ljava/lang/String;`                                | 
| 数组     | `[`        | 有几维就有几个 `[`，如 `int[]` 为 `[I`，`Object[][]` 为 `[[Ljava/lang/Object;` |
### 函数描述符

方法描述符描述一个方法签名，格式为 `(形参字段描述符)返回值字段描述符`，形参字段描述符可有多个对应函数多个形参，每个参数之间没有间隔，如 `int add(int x, int y); `的函数描述符为 `(II)V`
## 访问标志

可用于类的访问标志：

| 标识符            | 值      | 说明                      |     |
| -------------- | ------ | ----------------------- | --- |
| ACC_PUBLIC     | 0x0001 | public                  |     |
| ACC_FINAL      | 0x0010 | final                   |     |
| ACC_SUPER      | 0x0020 | 是否允许 invokespecial(总为真) |     |
| ACC_INTERFACE  | 0x0200 | interface               |     |
| ACC_ABSTRACT   | 0x0400 | abstract 或接口也有          |     |
| ACC_SYNTHETIC  | 0x1000 | 该类由编译器产生                |     |
| ACC_ANNOTATION | 0x2000 | @interface              |     |
| ACC_ENUM       | 0x4000 | enum                    |     |
| ACC_MODULE     | 0x8000 | module                  |     |

可用于字段的访问标志：

| 标识符         | 值     | 说明               |
| -------------- | ------ | ------------------ |
| ACC_PUBLIC     | 0x0001 | public             |
| ACC_PRIVATE    | 0x0002 | private            |
| ACC_POTECTED   | 0x0004 | protected          |
| ACC_STATIC     | 0x0008 | static             |
| ACC_FINAL      | 0x0010 | final              |
| ACC_VOLATILE   | 0x0040 | volatile           |
| ACC_TRANSLIENT | 0x0080 | transient          |
| ACC_SYNTHETIC  | 0x1000 | 该字段由编译器产生 | 
| ACC_ENUM       | 0x4000 | enum               |

可用于方法的访问标志：

| 标识符           | 值     | 说明                         |
| ---------------- | ------ | ---------------------------- |
| ACC_PUBLIC       | 0x0001 | public                       |
| ACC_PRIVATE      | 0x0002 | private                      |
| ACC_POTECTED     | 0x0004 | protected                    |
| ACC_STATIC       | 0x0008 | static                       |
| ACC_FINAL        | 0x0010 | final                        |
| ACC_SYNCHRONIZED | 0x0020 | synchronized                 |
| ACC_BRIDGE       | 0x0040 | 该方法为编译器产生的桥接方法 |
| ACC_VARAGES      | 0x0080 | 该方法接收不定参数           |
| ACC_NATIVE       | 0x0100 | native                       |
| ACC_ABSTRACT     | 0x0400 | abstract                     |
| ACC_STRICT       | 0x0800 | strictfp                     | 
| ACC_SYNTHETIC    | 0x1000 | 该类由编译器产生             |

可用于方法参数的访问标志：

| 标识符           | 值      | 说明           |     |
| ------------- | ------ | ------------ | --- |
| ACC_FINAL     | 0x0010 | final        |     |
| ACC_SYNTHETIC | 0x1000 | 该类由编译器产生     |     |
| ACC_MANDATED  | 0x8000 | 隐式定义（this 等） |     |

可用于模块的访问标志：

| 标识符           | 值      | 说明        |     |
| ------------- | ------ | --------- | --- |
| ACC_OPEN      | 0x0020 | open      |     |
| ACC_SYNTHETIC | 0x1000 | 该模块由编译器产生 |     |
| ACC_MANDATED  | 0x8000 | 该模块隐式定义   |     |
## 虚拟机字节码指令表

![[JVM 虚拟机字节码指令表.xlsx]]
