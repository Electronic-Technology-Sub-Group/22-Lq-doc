# Java9

## @Deprecated

`@Deprecated` 注解将包含过时原因及是否将移除该过时 API，可用于编译器和其他工具检查
- `@SuppressWarnings("deprecation")` 仅抑制 `forRemoval=false` 的过时警告
- `@SuppressWarnings("removal")` 抑制 `forRemoval=true` 的过时警告

JDK 提供工具 jdeprscan 分析代码中使用的过时内容

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
## @SafeVarargs 

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
## Collection

Java9 新增了一系列创建集合的方法，位于 `List`，`Set`，`Map`，通过工厂方法实现了对 collection literals 的支持，但注意：
- 创建的集合都是不可变集合，且不同虚拟机的具体实现类不做强制要求
- 集合元素不允许 `null` 值
- 若集合元素可序列化，则该集合可序列化

```java
// 1234
List.of(1, 2, 3, 4).forEach(System.out::print);
Map<Integer, String> map = Map.of(1, "a", 2, "b", 3, "c");
map.get(1); // a
```
## Process

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
## ProcessHandler

Java 9 新增 `ProcessHandler` 标识一个本地进程，允许查询进程状态并管理进程，`Process` 可通过 `toHandler()` 方法转换为 `ProcessHandler`

> [!info]
> `Process` 表示由 JVM 启动的本地进程，`ProcessHandler` 表示任意本地线程

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
## StackWalker

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
## 响应式流

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
## Stream API

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
## 日志 API

- 一个服务接口`java.lang.System.LoggerFinder`，它是一个抽象的静态类
- 一个接口`java.lang.System.Logger`，它提供了日志API
- `java.lang.System`类中的重载方法`getLogger()`返回一个`System.Logger`
## 线程旋转等待

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
## StrictMath 新方法

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
## HTTP/2 Client API

实验性功能：[[#HTTP Client]]
# Java11 LTS
## HTTP Client

`java.net.http` 模块。API 上有点类似于 Apache Http Client
## Curve25519 及 Curve448 密钥协议

新接口 `XECPublicKey` 和 `XECPrivateKey` 接口

```java
public void curve25519()   
        throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeySpecException, InvalidKeyException {  
    KeyPairGenerator kpg = KeyPairGenerator.getInstance("XDH");  
    NamedParameterSpec paramSpec = new NamedParameterSpec("X25519"); // curve448: X448  
    kpg.initialize(paramSpec);  
    KeyPair kp = kpg.generateKeyPair();  
    // 公钥  
    KeyFactory kf = KeyFactory.getInstance("XDH");  
    BigInteger u = ...; // u  
    XECPublicKeySpec pubSpec = new XECPublicKeySpec(paramSpec, u);  
    PublicKey pubKey = kf.generatePublic(pubSpec);  
      
    // 私钥  
    KeyAgreement ka = KeyAgreement.getInstance("XDH");  
    ka.init(kp.getPrivate());  
    ka.doPhase(pubKey, true);  
    byte[] secret = ka.generateSecret();  
}
```
## 移除某些已弃用内容

- CORBA
- JavaEE：JAX-WS，JAXB，JAF，Common Annotations，通过 maven 仓库重新引用
- Nashorn 引擎
- Pack200 Tools
## 其他

- 基于嵌套的访问控制：JEP181 Nest-Based Access Control，多个类同属于同一个代码块，但分散编译成多个 `class` 文件时，彼此访问各自私有成员时可直接访问（反射）
- ChaCha20 与 Poly1305 加密算法
- 支持 Unicode 10
- 支持 TLS 1.3
