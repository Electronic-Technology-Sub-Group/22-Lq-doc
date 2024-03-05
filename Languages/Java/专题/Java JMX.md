---
参考资料: https://juejin.cn/post/7039480570203602975
---
Java Management Extensions（**JMX**）技术是 Java SE 平台的标准功能，提供了一种简单的、标准的监控和管理资源的方式，对于如何定义一个资源给出了明确的结构和设计模式，主要用于监控和管理 Java 应用程序运行状态、设备和资源信息、Java 虚拟机运行情况等信息。 **JMX** 是可以动态的，所以也可以在资源创建、安装、实现时进行动态监控和管理，JDK 自带的 jconsole 就是使用 **JMX** 技术实现的监控工具。

使用 **JMX** 技术时，通过定义一个被称为 **MBean** 或 **MXBean** 的 Java 对象来表示要管理指定的资源，然后可以把资源信息注册到 **MBean Server** 对外提供服务。MBean Server 充当了对外提供服务和对内管理 MBean 资源的代理功能，如此优雅的设计让 MBean 资源管理和 MBean Server 代理完全独立开，使之可以自由的控制 MBean 资源信息。

JMX 不仅仅用于本地管理，JMX Remote API 为 JMX 添加了远程功能，使之可以通过网络远程监视和管理应用程序。

JMX 技术为 Java 开发者提供了一种简单、灵活、标准的方式来监测 Java 应用程序，得益于相对独立的架构设计，使 JMX 可以平滑的集成到各种监控系统之中。
# 资源监视 MBean

JMX 对 JVM 的资源检测类，都可以直接使用。

|资源接口|管理的资源|Object Name|VM 中的实例个数|
|:--|:--|:--|:--|
|ClassLoadingMXBean|类加载|java.lang:type= ClassLoading|1个|
|CompilationMXBean|汇编系统|java.lang:type= Compilation|0 个或1个|
|GarbageCollectorMXBean|垃圾收集|java.lang:type= GarbageCollector, name=collectorName|1个或更多|
|LoggingMXBean|日志系统|java.util.logging:type =Logging|1个|
|MemoryManagerMXBean|内存池|java.lang: typeMemoryManager, name=managerName|1个或更多|
|MemoryPoolMXBean|内存|java.lang: type= MemoryPool, name=poolName|1个或更多|
|MemoryMXBean|内存系统|java.lang:type= Memory|1个|
|OperatingSystemMXBean|操作系统|java.lang:type= OperatingSystem|1个|
|RuntimeMXBean|运行时系统|java.lang:type= Runtime|1个|
|ThreadMXBean|线程系统|java.lang:type= Threading|1个|

下面的代码示例演示了使用 JMX 检测 JVM 某些信息的代码示例。

```java
OperatingSystemMXBean osMXBean = ManagementFactory.getOperatingSystemMXBean();
String osName = osMXBean.getName();
String osVersion = osMXBean.getVersion();
int processors = osMXBean.getAvailableProcessors();
System.out.printf("操作系统：%s，版本：%s，处理器：%d 个%n", osName, osVersion, processors);

MemoryMXBean memMXBean = ManagementFactory.getMemoryMXBean();
MemoryUsage heapMemUsage = memMXBean.getHeapMemoryUsage();
MemoryUsage nonHeapMemUsage = memMXBean.getNonHeapMemoryUsage();
long maxNonHeap = nonHeapMemUsage.getMax();
long usedNonHeap = nonHeapMemUsage.getUsed();
long maxHeap = heapMemUsage.getMax();
long usedHeap = heapMemUsage.getUsed();
System.out.printf("使用内存[Heap]：%.2fMB/%.2fMB%n", usedHeap / 1024.0 / 1024, maxHeap / 1024.0 / 1024);
System.out.printf("使用内存[NonHeap]：%.2fMB/%.2fMB%n", usedNonHeap / 1024.0 / 1024, maxNonHeap / 1024.0 / 1024);

List<GarbageCollectorMXBean> gcMXBeans = ManagementFactory.getGarbageCollectorMXBeans();
String gcNames = gcMXBeans.stream()
        .map(MemoryManagerMXBean::getName)
        .collect(Collectors.joining(", "));
System.out.println("垃圾收集器：" + gcNames);
```
# 资源代理 MBean Server

资源代理 MBean Server 是 MBean 资源的代理，通过 MBean Server 可以让 MBean 资源用于远程管理， MBean 资源和 MBean Server 往往都是在同一个 JVM 中，但这不是必须的。

想要 MBean Server 可以管理 MBean 资源，首先要把资源注册到 MBean Server，任何符合 JMX 的 MBean 资源都可以进行注册，最后 MBean Server 会暴露一个远程通信接口对外提供服务。

MBean 的编写必须遵守 JMX 的设计规范，MBean 很像一个特殊的 Java Bean，它需要一个接口和一个实现类。

**MBean 资源接口总是以 MBean 或者 MXBean 结尾**。

```java
public interface MyMemoryMBean {

    long getTotal();

    void setTotal(long memory);

    long getUsed();

    void setUsed(long memory);
    
    String getMemoryInfo();
}
```

**实现类则要以接口去掉 MBean 或 MXBean 之后的名字来命名。**

```java
public class MyMemory implements MyMemoryMBean {

    long total, used;

    @Override
    public long getTotal() {
        return total;
    }

    @Override
    public void setTotal(long memory) {
        total = memory;
    }

    @Override
    public long getUsed() {
        return used;
    }

    @Override
    public void setUsed(long memory) {
        used = memory;
    }

    @Override
    public String getMemoryInfo() {
        return String.format("Memory: %dMB/%dMB", used / 1024 / 1024, total / 1024 / 1024);
    }
}
```

MBean 资源需要注册到 MBean Server 进行代理才可以暴露给外部进行调用，所以我们想要通过远程管理我们自定义的 MyMemory 资源，需要先进行资源代理。

```java
// 初始化 MyMemory
MyMemory memory = new MyMemory();
memory.setTotal(4L * 1024 * 1024 * 1024 * 1024);
memory.setUsed(0);
// 注册
MBeanServer server = ManagementFactory.getPlatformMBeanServer();
ObjectName objectName = new ObjectName("com.example.jmx;type=memory");
server.registerMBean(memory, objectName);
```
# 远程访问

要想使 JMX 控制的资源被外部访问到，需要添加 JVM 参数开放端口

```bash
$ java -Dcom.sun.management.jmxremote=true \  # 开启远程访问
-Dcom.sun.management.jmxremote.port=8398 \		# 自定义 JMX 端口
-Dcom.sun.management.jmxremote.ssl=false \		# 是否使用 SSL 协议，生产环境一定要开启
-Dcom.sun.management.jmxremote.authenticate=false \ # 是否需要认证，生产环境一定要开启
-Djava.rmi.server.hostname=150.158.2.56 YourClass.java # 当前机器 ip
```

之后，通过 `jconsole` 连接和控制