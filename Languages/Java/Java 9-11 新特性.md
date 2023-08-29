# Java 9
## Jigsaw 模块

在包 package 之上增加一级 模块 module，声明依赖及开放接口

```java
// module-info.java
// open 关键字允许从模块外通过反射访问模块内的任意内容
open module myModuleName {
    // 依赖于 module.requires1.name 模块
    requires module.requires1.name;
    // 静态依赖：仅编译时依赖于 module.requires2.name 模块
    requires static module.requires2.name;
    // 传递依赖：依赖于该模块的其他模块也能直接使用 module.requires3.name 模块
    requires transitive module.requires2.name;
    
    // 开放 package.exports1.name 包对任意依赖于该模块的程序访问
    exports package.exports1.name;
    // 仅对 package.special 模块开放 package.exports2.name 包
    exports package.exports2.name to package.special;
    
    // 依赖于 service.Interface 服务接口或抽象类
    uses service.Interface;
    
    // 提供 service.Interface 服务的一个实现 service.InterfaceImpl
    // 服务仍通过 ServiceLoader.load 获取
    providers service.Interface with service.InterfaceImpl;
    
    // 开放 package.open1.name 包对任意依赖于该模块的程序反射访问
    opens package.open1.name;
    // 仅对 module.special1, module.special2, module.special3 模块开放 package.open2.name 包反射访问
    opens package.open2.name to module.special1, module.special2, module.special3;
}
```
## 接口私有方法

允许接口中存在私有方法，私有方法必须在接口中实现，且只能在接口中直接访问，用于对 `default` 函数实现调用

```java
public interface PrivateMethod {
    // private 方法允许在接口中存在
    // 但是 protected 不允许，在实现类中也无法直接访问到
    private void privateMethod() {

    }
}
```
## try-with-resource

Java 9 之前版本中 `try` 内变量必须是语句，通常是赋值语句；现在可以使用变量

```java
AutoCloseable resource = /*...*/;

// 允许直接使用变量而不是语句
try(resource) {
    // do something
}
```
## 泛型推断

Java 9 之前创建匿名内部类时，内部类的泛型类型必须指定。现在可以自动推断了。

```java
private void privateMethod() {
    // A<> 推断为 A<String>
    A<String> a = new A<>() {
        @Override
        public void a(String p) {
            System.out.println(a);
        }
    };
}

interface A<T> {
    void a(T p);
}
```
## _ 关键字

单独一个下划线现作为一个保留关键字
## JShell

通过 JShell API 可以模拟 JShell 在代码中直接执行 String 类型的 Java 代码

```java
// JShell 表示一个会话
try (JShell shell = JShell.create()) {
    for (SnippetEvent event : shell.eval("int i = 100;")) {
        // Snippet 表示一个代码片段
        Snippet snippet = event.snippet();
        // Snippet: Snippet:VariableKey(i)#1-int i = 100;
        System.out.println("Snippet: " + snippet);
        // Kind: VAR
        System.out.println("Kind: " + snippet.kind());
        // Sub-Kind: VAR_DECLARATION_WITH_INITIALIZER_SUBKIND
        System.out.println("Sub-Kind: " + snippet.subKind());
        // Previous Status: NONEXISTENT
        System.out.println("Previous Status: " + event.previousStatus());
        // Current Status: VALID
        System.out.println("Current Status: " + event.status());
        // Value: 100
        System.out.println("Value: " + event.value());
    }
}
```

JShell 是交互式访问 Java 语言的命令行工具（交互式编程语言环境 Read-Eval-Print Loop, REPL），也是一个开发与 JShell 类似功能的 API，`位于 jdk/bin/jshell.exe`

![[image-20211119151301606-16373059853301.png]]
- --start 启动参数
	- --start DEFAULT 导入常用的几个 import
	- --start PRINTING 增加 `print`, `println`, `printf` 方法，指向 `System.out` 的对应方法
- /help 查看帮助
	- /help intro 显示 JShell 本身的简单介绍
- /exit 退出 JShell
- JShell 可直接运行 Java 语句，不需要添加 `;`
	- i，$2 都是变量名，可通过 getClass 或 改变反馈等级查看类型
- /vars 可查看所有变量
- /list 可查看已输入过的代码片段，/drop 可移除
- /edit 可打开一个 GUI 编辑，可使用 /edit 名称
- /imports 查看所有引用
- /history 查看历史
- /save /open 可保存、导入代码
- /reset 重置状态（/set /env 将被保留）
- /reload 将重置（会话开始/reset/reload）之后的所有命令并重新执行
## 其他

- 新的 Java 版本规则：`$major.$minor.$security`, `$security` 安全级别不随前两个版本更新而重置
- javadoc 支持 HTML5

```bash
javadoc -html5
```

- `java.io.ObjectInputFilter` 接口对反序列化进行控制，可中断
# Java 10

## 局部变量推断

增加 `var` 关键字自动推断类型

```java 
var a = "This is a String";
```

仅用于方法的局部变量，for 和 增强 for 循环中，必须初始化
## 其他

- 并行垃圾回收器 G1
- AppCDS 通过在不同的 Java 进程间共享公共类元数据来减少占用空间，改善启动时间。
# Java11 LTS
## Lambda Var

允许在 lambda 表达式中使用 `var` 类型推断

```java
// interface: (int, int) -> void
(var x, var y) -> { /*...*/ }
```
## 运行单一 java 文件

JDK 11 现允许直接运行单一 Java 文件程序

```bash
java HelloWorld.java
```

至此，Java 启动器支持以下四种方法启动：
- 单一 class 文件
- Jar 包中包含 main 方法的类
- 模块中包含 main 方法的类
- 单一 java 文件
## 垃圾收集器

- Epsilon 垃圾收集器：什么也不做的垃圾收集器（包括 `System.gc()`），堆内存用完后直接退出，通过 `-XX:+UseEpsilonGC` 参数开启，常用于：
	- 测试：性能测试，内存压力测试，VM 接口测试
	- 微服务等非常短小的 JOB 任务
	- Last-drop 延迟 & 吞吐改进
- ZGC 垃圾收集器：并发、压缩性、基于 region 的高效率垃圾收集器，暂停时间不会超过 10ms
	- 实验性功能，Java 15 转正
	- 通过 `-XX:+UnlockExperimentalVMOptions -XX:+UseZGC` 启用
## 其他

- 动态文件常量：JEP309 Dynamic Class-File Constants，`class` 文件支持新的常量池格式 `CONSTANTDyanmic`，会将创建方法委托给 `bootstrap` 方法
- 飞行记录器：Flight Recorder，低开销的 Java 应用排错和 JVM 问题数据收集框架