一个模块除了是包的容器外，还包含其他信息：

- 依赖的其他包列表
- 公开 API 列表
- 公开反射访问列表
- 使用和提供的服务列表
# 问题背景

Jigsaw 是 Java 9 提供的模块系统，主要为了解决以下问题：

- JAR-Hell 问题：
	- 包无法控制包内内容的可见性
	- 除了以java和javax开头的包外，包应该是开放扩展的。如果你在具有包级别访问的JAR中进行了类型化，则可以在其他JAR中访问定义与你的名称相同的包中的类型。 ？？？
	- 运行时无法区别相同包的不同版本 - 即使包含同一个类，虚拟机也只会使用类加载器中找到的第一个
	- 运行时可能会出现类丢失现象，由于缺少某个 JAR 包导致运行时错误
	- 启动时无法检查是否丢失某个类或者包含了错误版本的 JAR 包
- JDK/JRE 单体结构：JRE 最为一个整体，增加了下载和启动时间、内存占用，无法在内存很小的设备上使用
# Java 平台模块

- 标准模块：以 `java` 为前缀，包含于 Java SE 9 平台，如 `java.base`, `java.sql`, `java.xml`, `java.logging` 等，供开发人员使用
	- `java.base` 是原始模块，不依赖于其他任何模块，包含 `java.lang`, `java.io` 等核心基础 Java SE 软件包
- 非标准模块：以 `jdk` 为前缀，作为 JDK 的一部分但不包含于 Java SE 平台规范中，不适用于开发人员，可能会在未经通知的情况下改变，如 `jdk.charsets`, `jdk.compiler`, `jdk.jlink`, `jdk.zipfs` 等
- JavaFX：以 javafx 为前缀，但并非 JavaSE 标准的一部分，如 `javafx.base`, `javafx.controls`, `javafx.fxml` 等

![[4366140-1929153c342862c9.webp]]
# 相关命令

- 可通过 `--add-modules` 在编译、链接、运行时指定模块
- 可通过 `-Xdiag:resolver` 在诊断信息结尾显示模块依赖关系
- `--module-path` 指定所需模块或目录或列表
- `--list-modules` 打印模块信息
- `--module-source-path` 指定模块中包含的源文件
- `--module-version` 设置模块版本号，将被保存到 `module-info.class` 中
- `--main-class` 指定 `main` 方法所在类，，将被保存到 `module-info.class` 及 `MANIFEST.MF` 中
- `--module` 指定运行时与主类一起运行的模块
# 聚合模块

当一个模块不包含任何内容，只是引用了一组其他模块时，称该模块为聚合模块
# 声明模块

```java
[open] module <module> {
    <module-statement>;
    <module-statement>;
    ...
}
```

`open` 可选，表示允许任意模块访问或反射访问当前模块的任意内容 
## 模块名

模块名可以是任何 Java 合法的标识符或由 `.` 连接的多个标识符组成

JDK 9 中，`open`, `module`, `requires`, `transitive`, `exports`, `opens`, `to`, `uses`, `provides`, `for` 为受限关键字，只在对应位置出现时有特殊意义，其本身仍可以作为合法标识符使用
## 修饰符

- `exports`：将模块中指定包导出到所有模块或指定模块列表

```java
mdoule M {
    exports <package>;
    exports <package> to <module1>, <module2>, ...;
}
```

- `opens`：允许所有模块或指定模块列表通过反射访问指定包内容

```java
module M {
    opens <package>;
    opens <package> to <module1>, <module2>, ...;
}
```

- `requires`：声明当前模块依赖于其他模块。所有模块都直接或间接依赖于 `java.base`
	- `static` 表示对编译时是必须的，但在运行时是可选的
	- `transitive` 表示传递依赖，任何其他依赖于当前模块 M 的其他模块都会隐式的依赖于该模块 \<module>

```java
module M {
    requires [static] [transitive] <module>;
}
```

- `uses`：使用语句指定要使用的服务，Java 将会使用 `java.util.ServiceLoader` 加载

```java
module M {
    uses <serivce-interface>;
}
```

- `providers`：指定接口的一个或多个实现类

```java
module M {
    providers <service-interface> with <service-impl-class1>, <service-impl-class2>, ...;
}
```
# 描述文件

模块描述文件放置于 `module-info.java` 文件中，该文件置于模块源文件层次结构的根目录下（与其他包并列）

```java
Module module = Welcome.class.getModule();
// Module: lq2007.demo.modules
System.out.println("Module: " + module.getName());
```
# 资源访问

```java
Module module = ...;
module.getResourceAsStream();
```

Java 9 获取运行时镜像资源不会再指向一个 Jar 包，而是通过 module 路径构建 URL

```java
String resource = "java/lang/Object.class";
URL url = ClassLoader.getSystemResource(resource);
// java8: jar:file:C:/java8/jre/lib/rt.jar!/java/lang/Object.class
// java9: jrt:/java.base/java/lang/Object.class -> jrt:/<module-name>/<path>
System.out.println(url);
```
# 内部 API

JDK 中的 公共 API 包括 `java.*`, `javax.*`, `org.*`，这些 API 可以保证在任何支持 Java 平台的系统中运行，且若能在 JDK N 版本中运行，则能够在 JDK N+1 版本中运行。

JDK 中还存在一系列内部 API，用于实现 JDK 本身，包括 `com.sun.*`, `sun.*`, `jdk.*`，这些 API 通常依赖于特定的 JDK 实现。这些类可以用但有风险，如 `BASE64Encoder`, `Unsafe` 等。在 Java 9 中，这些内部 API 中某些常用类被释放到公共 API 中，另一些被放置在 `jdk.unsupported` 模块中。
# 打破封装

仅在必要情况下使用：
- 启用白盒测试或使用不受支持的 JDK 内部 API
- 向后兼容，应用程序依赖于之前公开但现在封装到 JDK 内部 API 中的 API

可以通过运行时附加特殊的命令行，或者在 MANIFEST 中声明特殊部分来打破封装，其对应关系如下

| Moule 关键字 | 命令行选项    | Manifest 属性 |
| ------------ | ------------- | ------------- |
| exports      | --add-exports | Add-Exports   |
| opens        | --add-opens   | Add-Opens     |
| requires     | --add-reads   | 无            |
## exports

通过 --add-exports 可以将特定模块中的包暴露给所有模块或指定模块

```bash
java ... --add-exports <source-module>/<package>=<target-module1>,<target-module2>,...
```

如

```bash
--add-exports java.base/sun.util.logging=test.module1,test.module2
```

等效于

```java
module java.base {
    exports sun.util.logging to test.module, test.module2;
}
```

```java
// MANIFEST
// Add-Exports: <source-module1>/<target-module1> <source-module2>/<target-module2> ...
Add-Exports: java.base/sun.util.logging
```

可使用 `ALL-UNNAMED` 表示所有未命名模块
## opens

```bash
--add-opens <source-module>/<package>=<target-module1>,<target-module2>,...
```

等效于

```java
module <source-module> {
    opens <package> to <target-module1>, <target-module2>, ...;
}
```

```java
// MANIFEST
Add-Opens: <source-module1>/<target-module1> <source-module2>/<target-module2> ...
```
## requires

不用于打破封装，但可用于调试时增强可读性

```bash
--add-reads <source-module>=<target-module1>,<target-module2>,...
```

等效于

```java
module <source-module> {
    requires <target-module1>;
    requires <target-module2>;
    ...
}
```
## permission-illegal-access

允许任何未命名模块中的代码反射访问任何命名模块的成员，在 Java 10 中移除
# Module API

| 类                                 | 描述                                                         |
| ---------------------------------- | ------------------------------------------------------------ |
| Module                             | 表示运行时模块。                                             |
| ModuleDescriptor                   | 表示模块描述。 这是不可变类。                                |
| ModuleDescriptor.Builder           | 用于以编程方式构建模块描述的嵌套构建器类。                   |
| ModuleDescriptor.Exports           | 表示模块声明中的`exports`语句的嵌套类。                      |
| ModuleDescriptor.Opens             | 表示模块声明中的`opens`语句的嵌套类。                        |
| ModuleDescriptor.Provides          | 表示模块声明中的`provides`语句的嵌套类。                     |
| ModuleDescriptor.Requires          | 表示模块声明中的`requires`语句的嵌套类。                     |
| ModuleDescriptor.Version           | 表示模块版本字符串的嵌套类。 它包含一个从版本字符串返回其实例的`parse(String v)`工厂方法。 |
| ModuleDescriptor.Modifier          | 枚举类，其常量表示在模块声明中使用的修饰符，例如打开模块的`OPEN`。 |
| ModuleDescriptor.Exports.Modifier  | 枚举类，其常量表示在模块声明中用于`exports`语句的修饰符。    |
| ModuleDescriptor.Opens.Modifier    | 枚举类，其常量表示在模块声明中的`opens`语句上使用的修饰符。  |
| ModuleDescriptor.Requires.Modifier | 枚举类，其常量表示在模块声明中的`requires`语句上使用的修饰符。 |
| ModuleReference                    | 模块的内容的引用。 它包含模块的描述及其位置。                |
| ResolvedModule                     | 表示模块图中已解析的模块。 包含模块的名称，其依赖关系和对其内容的引用。 它可以用于遍历模块图中模块的所有传递依赖关系。 |
| ModuleFinder                       | 用于在指定路径或系统模块上查找模块的接口。 找到的模块作为`ModuleReference`的实例返回。 它包含工厂方法来获取它的实例。 |
| ModuleReader                       | 用于读取模块内容的接口。 可以从`ModuleReference`获取`ModuleReader`。 |
| Configuration                      | 表示解析模块的模块图。                                       |
| ModuleLayer                        | 包含模块图（`Configuration`）以及模块图中的模块与类加载器之间的映射。 |
| ModuleLayer.Controller             | 用于控制`ModuleLayer`中的模块的嵌套类。 `ModuleLayer`类中的方法返回此类的实例。 |
# 模块层

模块安排成层。一个模块层是一组解析的模块，具有将每个模块映射到负责加载该模块中所有类型的类加载器的功能。

解析模块的集合称为配置。层次分层排列。层除了空层以外还有至少一个父层，顾名思义，它不含任何模块，主要用作引导层的父层。

引导层由启动时由JVM创建，通过针对一组可观察模块解析应用程序的初始模块（根模块）。

可以创建自定义层。模块层允许将同一模块的多个版本加载到不同的层中，并在同一个JVM中使用。
