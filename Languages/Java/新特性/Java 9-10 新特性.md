# Java 9

## 语法

### Jigsaw 模块系统

在包 package 之上增加一级 模块 module，声明依赖及开放接口

文件：module-info.java

```java
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

### 接口私有方法

允许接口中存在私有方法，私有方法必须在接口中实现，且只能在接口中直接访问，用于对 `default` 函数实现调用

```java
public interface PrivateMethod {
    // private 方法允许在接口中存在
    // 但是 protected 不允许，在实现类中也无法直接访问到
    private void privateMethod() {

    }
}
```

### try-with-resource 改进

Java 9 之前版本中 `try` 内变量必须是语句，通常是赋值语句；现在可以使用变量

```java
AutoCloseable resource = /*...*/;

// 允许直接使用变量而不是语句
try(resource) {
    // do something
}
```

### 匿名内部类泛型推断

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

### _ 关键字

单独一个下划线现作为一个保留关键字

## API

### @Deprecated

> `@Deprecated`：标注被注解的类、方法、成员变量等元素已过时，使用时应尽量避免使用这些元素

`@Deprecated` 注解将包含过时原因及是否将移除该过时 API，可用于编译器和其他工具检查

```java
// 该方法在 1.0 版本中标注过时
@Deprecated(since = "1.0")
void fun1() {
    // something deprecated
}

// 该方法已弃用，且将在未来被删除
@Deprecated(forRemoval = true)
void fun2() {
    // something deprecated
}
```

`@SuppressWarnings("deprecation")` 仅抑制 `forRemoval=false` 的过时警告，`@SuppressWarnings("removal")` 抑制 `forRemoval=true` 的过时警告

JDK 提供工具 jdeprscan 分析代码中使用的过时内容

### @SafeVarargs 

> `@SafeVarargs`：标注于函数，表示函数中存在具有泛型的可变参数，且不会造成堆污染（可变参数实际是一个数组，可能引发 `ClassCastException` 异常）

`@SafeVarargs` 注解允许应用于私有方法

```java
// Java 7 只允许 final 方法，static 方法，构造函数
// Java 9 允许 private 方法
@SafeVarargs
private void method(List<String>... values) {
    for (List<String> value : values) {
        for (String s : value) {
            System.out.println(s);
        }
    }
}
```

### 集合工厂方法

Java9 新增了一系列创建集合的方法，位于 List, Set, Map，通过工厂方法实现了对 collection literals 的支持，但注意：
- 创建的集合都是不可变集合，且不同虚拟机的具体实现类不做强制要求
- 集合元素不允许 `null` 值
- 若集合元素可序列化，则该集合可序列化

```java
// 1234
List.of(1, 2, 3, 4).forEach(System.out::print);
Map<Integer, String> map = Map.of(1, "a", 2, "b", 3, "c");
map.get(1); // a
```

### Process 丢弃输出

Process 允许丢弃输出

```java
System.out.println("Using Redirect.INHERIT");
/*
openjdk 17.0.1 2021-10-19
OpenJDK Runtime Environment (build 17.0.1+12-39)
OpenJDK 64-Bit Server VM (build 17.0.1+12-39, mixed mode, sharing)
*/
new ProcessBuilder("java", "--version")
        .redirectOutput(ProcessBuilder.Redirect.INHERIT)
        .redirectError(ProcessBuilder.Redirect.INHERIT)
        .start().waitFor();
System.out.println();
System.out.println("Using Redirect.DISCARD");
// 无输出
new ProcessBuilder("java", "--version")
        .redirectOutput(ProcessBuilder.Redirect.DISCARD)
        .redirectError(ProcessBuilder.Redirect.DISCARD)
        .start().waitFor();
```

### ProcessHandler

Java 9 新增 `ProcessHandler` 标识一个本地进程，允许查询进程状态并管理进程

**`Process` 表示由 JVM 启动的本地进程，`ProcessHandler` 表示任意本地线程**

`Process` 可通过 `toHandler()` 方法转换为 `ProcessHandler`

```java
ProcessHandle handle = ProcessHandle.current();
ProcessHandle.Info info = handle.info();
// CurrentProcess: 
//     pid=17160
//     cmd=D:\SupportLibraries\Java\jdk-17.0.1\bin\java.exe
//     args=[null]
//     line=<null>
//     start=2021-11-18T12:52:49.013Z
//     user=DESKTOP-EC9OPUI\luqin
System.out.println("CurrentProcess: " +
        "\n    pid=" + handle.pid() +
        "\n    cmd=" + info.command().orElse("<null>") +
        "\n    args=" + info.arguments().map(Arrays::toString).orElse("[null]") +
        "\n    line=" + info.commandLine().orElse("<null>") +
        "\n    start=" + info.startInstant().map(Instant::toString).orElse("<null>") +
        "\n    user=" + info.user().orElse("<null>"));
```

### StackWalker

Java 9 之前要遍历当前栈帧通过 `Throwable.getStackTrack()` 获取
- 返回整个栈帧快照，效率低
- 栈帧包含类和方法名，但不包含类引用
- 无法查询虚拟机隐藏的栈帧（虚拟机允许省略某些帧以提升性能）
- 调用者敏感 - 不同方法调用返回的结果不同
- 无法简单的过滤帧

Java 9 提供 `StackWalker` API 以提供简单有效的遍历栈帧方法，自上而下遍历记录，高效而便利

```java
// RETAIN_CLASS_REFERENCE 包含对调用的 Class 类对象的引用
// SHOW_HIDDEN_FRAMES 包含所有隐藏帧
// SHOW_REFLECT_FRAMES 包含反射帧
StackWalker.getInstance(StackWalker.Option.SHOW_REFLECT_FRAMES)
        // lq2007.tools.webdownloader.Java9Demo.main
        .forEach(frame -> System.out.println(frame.getClassName() + "." + frame.getMethodName()));
```

### 响应式流

![[1240.png]]

响应式流用于实现非阻塞背压的异步流处理，元素流从发布者传递到订阅者，不经过任何阻塞
- 订阅者（Subscriber） 通过调用 发布者（Publisher） 的 `subscribe()` 方法订阅，订阅成功则触发订阅者的 `onSubscribe()` 方法向订阅者传递 `Subscription` 对象，否则触发 `onError()` 方法结束对话
-  订阅者通过调用 `Subscription.request(N)` 方法向发布者请求若干元素，可多次请求而不关心之前的请求是否已经发布
- 发布者通过触发订阅者的 `onNext(item)` 向订阅者发布元素，直到达到订阅者请求上限。若发布者再无更多元素发布给订阅者，触发订阅者 `onComplete()` 方法并结束对话
- 订阅者可通过 `Subscription.cancel()` 方法取消订阅并结束对话，但若之前的请求未完成则仍会在后续接收到发布者发布的请求
- 若发布者遇到错误，将调用订阅者的 `onError()` 方法并结束会话

```java
CompletableFuture<Void> future;
// 发布者 Flow.Publisher, Java 提供 SubmissionPublisher 实现类
//   executor: 向订阅者提供元素的线程
//   maxBufferCapacity: 给每个订阅者提供的最大缓冲区大小
//   handler: 当发布-订阅会话出现异常并关闭时的处理函数
// 发布方法
//   offer 非阻塞，当 onDrop 返回 false 时移除，返回正数表示预估缓存剩余元素，负数表示发送失败的尝试次数
//   submit 组设，直到订阅者可用于接受元素
try (SubmissionPublisher<Long> publisher = new SubmissionPublisher<>()) {
    System.out.println("Publisher buffer capacity=" + publisher.getMaxBufferCapacity());
    
    System.out.println("- Register subscriber");
    // consume 将给定方法包装为 ConsumerSubscriber 并注册
    // subscribe 直接注册给定 Subscriber
    future = publisher.consume(System.out::println);
    System.out.println("- Publish data");
    LongStream.range(0, 5).forEach(publisher::submit);
}
if (future != null) {
    try {
        // 等待接受并处理数据
        // 由于前面 try 已经关闭了发布者，这里一次性全部处理所有数据并输出
        // 0 1 2 3 4
        future.get();
    } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
    }
}
```

自定义接收者和非阻塞实例

```java
public static void main(String[] args) {
    try (SubmissionPublisher<Long> publisher = new SubmissionPublisher<>(Executors.newFixedThreadPool(5), 5)) {
        // subscribe 直接注册给定 Subscriber
        publisher.subscribe(new CustomSubscriber("A", 1000));
        publisher.subscribe(new CustomSubscriber("B", 2000));
        publisher.subscribe(new CustomSubscriber("C", 3000));
        PublisherThread thread = new PublisherThread(publisher);
        thread.start();
        thread.join();
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}

static class CustomSubscriber implements Flow.Subscriber<Long> {
    String name;
    long blockTime;
    public CustomSubscriber(String name, long blockTime) {
        this.name = name;
        this.blockTime = blockTime;
    }
    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        System.out.println(name + " subscribed " + subscription);
        // 接受所有数据
        subscription.request(Long.MAX_VALUE);
    }
    @Override
    public void onNext(Long item) {
        try {
            System.out.println(name + " receive " + item + " start.");
            Thread.sleep(blockTime);
            System.out.println(name + " receive " + item + " finished.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onError(Throwable throwable) {
        System.out.println(name + " error " + throwable.getMessage());
        throwable.printStackTrace();
    }
    @Override
    public void onComplete() {
        System.out.println(name + " complete.");
    }
}

static class PublisherThread extends Thread {
    SubmissionPublisher<Long> publisher;
    Random random = new Random();
    public PublisherThread(SubmissionPublisher<Long> publisher) {
        this.publisher = publisher;
    }
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            long value = random.nextLong();
            boolean submit = random.nextBoolean();
            if (submit) {
                // 使用 submit 阻塞发送
                System.out.println("-Submit value.");
                publisher.submit(value);
            } else {
                // 使用 offer 非阻塞发送
                int subscriber = random.nextInt(5);
                System.out.println("-Offer " + subscriber);
                publisher.offer(value, (s, v) -> {
                    switch (subscriber) {
                        case 0: return "A".equals(((CustomSubscriber) s).name);
                        case 1: return "B".equals(((CustomSubscriber) s).name);
                        case 2: return "C".equals(((CustomSubscriber) s).name);
                        default: return subscriber != 3;
                    }
                });
            }
            long waiting = random.nextLong(1000, 3000);
            try {
                Thread.sleep(waiting);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("-Publish finished.");
    }
}
```

> 注意：该实例未调用 `Flow.Subscription.cancel()` 方法，因此程序在发送完所有数据后并未退出

`Flow.Processor` 既是发送者又是接收者，可用来作为接收数据，过滤并发送的过滤器

### Stream API

| 类型            | 方法          | 说明                                                        |
| --------------- | ------------- | ----------------------------------------------------------- |
| Stream 方法     | `dropWhile`   | 丢弃第一个不满足给定条件之前的元素                          |
| Stream 方法     | `takeWhile`   | 丢弃第一个不满足给定条件及之后的元素                        |
| Stream 静态方法 | `ofNullable`  | 取非 null 值                                                |
| Stream 静态方法 | `iterate`     | 根据迭代生成流 `for(e=seed;hasNext;next) { /*provide e*/ }` |
| 收集器          | `filtering`   | 过滤并收集                                                  |
| 收集器          | `flatMapping` | 扁平化元素                                                  |

```java
// stream.dropWhile
List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).stream()
        .dropWhile(i -> i <= 5)
        .forEach(System.out::print); // 6789
// stream.takeWhile
List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).stream()
        .takeWhile(i -> i <= 5)
        .forEach(System.out::print); // 012345
// Stream.ofNullable
List<String> list = new ArrayList<>();
list.add("item1");
list.add("item2");
list.add("item3,next is null");
list.add(null);
list.add("item5");
String r1 = String.join("/", list);
System.out.println(r1);
String r2 = list.stream().flatMap(Stream::ofNullable).collect(Collectors.joining("/"));
System.out.println(r2);
// Stream.iterate
Stream.iterate(1, n->n<=10, n->n+1); // 1 2 3 4 5 6 7 8 9 10
// Collectors.filtering
List<String> list = Stream.of("a", "b", "c", "d", "ee", "ff", "gg", "h").collect(Collectors.filtering(
        s -> s.length() == 1,  /*元素过滤器*/
        Collectors.toList())); /*下级集器*/
list.forEach(System.out::println); // a b c d h
// Collectors.flatMapping
List<? extends Serializable> list = Map.of("a", 1, "b", 2, "c", 3).entrySet().stream().collect(Collectors.flatMapping(
        e -> Stream.of(e.getKey(), e.getValue()), // 扁平化元素
        Collectors.toList()));                    // 下级收集器
list.forEach(System.out::println); // c 3 b 2 a 1
```

### 日志 API

- 一个服务接口`java.lang.System.LoggerFinder`，它是一个抽象的静态类
- 一个接口`java.lang.System.Logger`，它提供了日志API
- `java.lang.System`类中的重载方法`getLogger()`返回一个`System.Logger`

[Java 9 揭秘（19. 平台和JVM日志） - 林本托 - 博客园 (cnblogs.com)](https://www.cnblogs.com/IcanFixIt/p/7259712.html)

### 线程旋转等待

增加 `Thread.onSpinWait()` 方法，提示处理器该线程暂时无法继续，可优化资源

该功能需要处理器支持

```java
public class SpinWaitTest implements Runnable {
    
    private volatile boolean dataReady = false;
    @Override
    public void run() {
        // Wait while data is ready
        while (!dataReady) {
            // Hint a spin-wait
            Thread.onSpinWait();
        }
        processData();
    }
    
    private void processData() {
        // Data processing logic goes here
    }
    
    public void setDataReady(boolean dataReady) {
        this.dataReady = dataReady;
    }
}
```

### StrictMath 新方法

| 方法            | 说明                                                                                                                                             |
| --------------- | ------------------------------------------------------------------------------------------------------------------------------------------------ |
| `floorDiv`      | 小于或等于 $x\div y$ 代数商最大长度                                                                                                              |
| `floorMod`      | 最小模数，$x-(floorDiv(x,y) * y)$                                                                                                                |
| `fma`           | IEEE 754-2008 fusedMultiplyAdd（$a\times b+c$）                                                                                                  |
| `multiplyExact` | 乘                                                                                                                                               |
| `multiplyFull`  | 确切乘                                                                                                                                           |
| `multiplyHigh`  | 长度是两个 64 位参数的 128 位乘积的最高有效 64 位。 当乘以两个 64 位长的值时，结果可能是 128 位值。 因此，该方法返回`significant (high)` 64 位。 | 

```java
System.out.println("Using StrictMath.floorDiv(long, int):");
System.out.printf("  floorDiv(20L, 3) = %d%n", floorDiv(20L, 3)); // 6
System.out.printf("  floorDiv(-20L, -3) = %d%n", floorDiv(-20L, -3)); // 6
System.out.printf("  floorDiv(-20L, 3) = %d%n", floorDiv(-20L, 3)); // -7
System.out.printf("  floorDiv(Long.Min_VALUE, -1) = %d%n", floorDiv(Long.MIN_VALUE, -1)); // -9223372036854775808

System.out.println("\nUsing StrictMath.floorMod(long, int):");
System.out.printf("  floorMod(20L, 3) = %d%n", floorMod(20L, 3)); // 2
System.out.printf("  floorMod(-20L, -3) = %d%n", floorMod(-20L, -3)); // -2
System.out.printf("  floorMod(-20L, 3) = %d%n", floorMod(-20L, 3)); // 1

System.out.println("\nUsing StrictMath.fma(double, double, double):");
System.out.printf("  fma(3.337, 6.397, 2.789) = %f%n", fma(3.337, 6.397, 2.789)); // 24.135789

System.out.println("\nUsing StrictMath.multiplyExact(long, int):");
System.out.printf("  multiplyExact(29087L, 7897979) = %d%n", multiplyExact(29087L, 7897979)); // 229728515173
try {
    System.out.printf("  multiplyExact(Long.MAX_VALUE, 5) = %d%n", multiplyExact(Long.MAX_VALUE, 5));
} catch (ArithmeticException e) {
    System.out.println("  multiplyExact(Long.MAX_VALUE, 5) = " + e.getMessage()); // long overflow
}

System.out.println("\nUsing StrictMath.multiplyFull(int, int):");
System.out.printf("  multiplyFull(29087, 7897979) = %d%n", multiplyFull(29087, 7897979)); // 229728515173

System.out.println("\nUsing StrictMath.multiplyHigh(long, long):");
System.out.printf("  multiplyHigh(29087L, 7897979L) = %d%n", multiplyHigh(29087L, 7897979L)); // 0
System.out.printf("  multiplyHigh(Long.MAX_VALUE, 8) = %d%n", multiplyHigh(Long.MAX_VALUE, 8)); // 3
```

### HTTP/2 Client API

实验性功能：[[Java 11 LTS 新特性#HTTP Client]]

### JShell API

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

## JShell

JShell 是交互式访问 Java 语言的命令行工具（交互式编程语言环境 Read-Eval-Print Loop, REPL），也是一个开发与 JShell 类似功能的 API，位于 jdk/bin/jshell.exe

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

- 新的 Java 版本规则
`$major.$minor.$security`, $security 安全级别不随前两个版本更新而重置

```java
String versionCode = System.getProperty("java.version");
Runtime.Version version = Runtime.Version.parse(versionCode);
System.out.println(version); // 17.0.1
```

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
