Javapoet 用于更方便的使用代码生成 Java 源代码，可用于以下多种情况：
- Java 编译时注解
- Gradle 插件
- 运行时生成代码后使用 `CompileUnit` 等工具编译

```java
// main()
MethodSpec main = MethodSpec.methodBuilder("main")
        // public static
        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
        // main(String[] args)
        .addParameter(String[].class, "args")
        // System.out.println("Hello World");
        .addStatement("$T.out.println($S)", System.class, "Hello World")
        .build();

// class HelloWorld
TypeSpec HelloWorld = TypeSpec.classBuilder("HelloWorld")
        // public final
        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
        // public static main(String[] args) { ... }
        .addMethod(main)
        .build();

// com.example.helloworld.HelloWorld
JavaFile file = JavaFile.builder("com.example.helloworld", HelloWorld)
        .build();

// 输出到控制台
file.writeTo(System.out);
```

输出结果为

```
package com.example.helloworld;

import java.lang.String;
import java.lang.System;

public final class HelloWorld {
  public static void main(String[] args) {
    System.out.println("Hello World");
  }
}
```

Java 语言的每一个部分由一个 `Spec` 类对象表示（`JavaFile`、`CodeBlock` 除外），通过对应类的 `builder()` 创建。`builder()` 中的参数为必须包含的内容，还可以通过 `addXxx` 方法添加其他部件。
# Java 组分

## JavaFile

一个 `JavaFile` 对象表示一个 Java 源码文件。

必备组件
- 包名，即 `package` 部分，必有
- 类，一个 Java 文件中至少有一个同名 Java 类，该类由一个 `TypeSpec` 表示

可选组件
- 静态导入内容，通过 `addStaticImport` 添加，第二个参数为具体导入的成员，可以为 `*`
- 文档注释，通过 `addFileComment` 添加

除静态导入外，其他导入 Javapoet 会在代码块、声明等处自动收集信息
## TypeSpec

一个 `TypeSpec` 对象表示一个类，确切说是类、接口、枚举、注解等，包括匿名内部类，通过选择不同的 `builder` 表示。

必备组件
- 类名

可选组件
- `superclass`，`addSuperinterface`：基类和实现接口
- `addModifiers`：其他修饰符
- `addMethod`、`addField`、`addType`：类成员，方法、字段、内部类
- `addAnnotation`、`addTypeVariable`：对类的注解、泛型
- `addEnumConstant`：若类型为枚举，添加枚举常量
- `addInitializerBlock`、`addStaticBlock`：构造函数与静态初始化块
## FieldSpec

一个 `FieldSpec` 对象表示一个字段声明

必备组件
- 字段类型
- 字段名

可选组件
- `addModifiers`：其他修饰符
- `addAnnotation`、`addJavadoc`：注解与注释
- `initializer`：初始化块
## MethodSpec

一个 `MethodSpec` 对象表示一个函数，包括普通函数、构造函数、`override` 重写
- `overriding` 方法可直接复制一个已存在的函数，并自动添加 `@Override` 注释

必备组件
- 函数名（构造函数不需要）

可选组件
- `returns`：返回值类型
- `addModifiers`：其他修饰符
- `addTypeVariable`、`addAnnotation`：泛型，注解
- `addComment`、`addJavadoc`：文档注释、普通注释
- `addParameter`，`varargs`：形参列表、变长参数
- `addException`：异常声明
- `addCode`、`addNamedCode`：代码块和具名代码块
- `defaultValue`：当应用到一个注解时的注解默认值
- `addStatement`：普通语句
- `beginControlFlow`、`nextControlFlow`、`endControlFlow`：控制语句
## ParameterSpec

一个 `ParameterSpec` 表示一个形参声明

必备组件：类型，变量名
可选组件：注释，注解，修饰符
## CodeBlock

一个 `CodeBlock` 表示一个代码块，可通过 `builder()` 创建，也可以通过 `format`，`join` 等从字符串创建

针对某些代码中的特殊部分，有特定的占位符，使用 `$` 开头

| 占位符 | 接收类型                           | 说明                                 |
| ------ | ---------------------------------- | ------------------------------------ |
| $L     | 字符串、基本类型、注解、其他代码块 | 常量                                 |
| $N     | 字符串、NameAllocator              | 标识符，包括变量、字段、方法、类型等 |
| $T     | 字符串、Class、Type、TypeName 等   | 类型，会自动处理必要的 import        |
| $S     | 字符串                             | 字符串字面量                         |
| `$$`   | 无                                 | `$`                                  |
# 辅助工具
## NameAllocator

申请一个标识符，自动校验是否符合要求
## TypeName

表示一个类型名，可以表示普通类型、基本类型、数组、泛型类型等，也可添加注解
## CodeWriter

可以将一个 JavaFile 转换成字符串