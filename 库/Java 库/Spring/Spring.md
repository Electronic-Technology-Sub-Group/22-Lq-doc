# Spring

Spring 框架为开发者提供一个专注于编写应用业务逻辑的基础设施，各个模块组包括：

|模块组|说明|
| -------------------------| ---------------------------------------------------------------------------------------------------------------------------|
|Core container|基础模块：<br />`spring-core` 与 `spring-beans` 提供了基础的 DI 功能和 IoC 容器实现；<br />`spring-expressions` 提供了 Spring 表达式语言（SpEL）支持<br />|
|AOP and instrumentation|提供支持 AOP 的类和工具：<br />`spring-aop` 提供 Spring AOP 功能；<br />`spring-instrument` 提供对类工具的支持<br />|
|Messaging|`spring-messaging` 用于简化开发基于消息的应用|
|Data Access/Integration|简化数据库交互模块：<br />`spring-jdbc` 简化使用 JDBC 与数据库交互；<br />`spring-orm` 提供 ORM（对象关系映射）集成；<br />`spring-tx` 提供编程式、声明式事务管理<br />|
|Web|简化开发 Web 与 portlet 应用：<br />`spring-web`、`spring-webmvc` 用于开发 Web 应用和 RESTful Web 服务；<br />`spring-websocket` 用于使用 WebSocket 开发 Web 应用<br />|
|Test|`spring-test` 模块简化单元测试、集成测试流程|

依赖项：某个对象需要调用的其他对象

依赖注入：DI，一种设计模式，对象的依赖项通过构造函数和 `setter` 方法成为对象的参数

控制反转：IoC，由容器负责创建和注入依赖，而不是应用程序手动创建

Spring 核心：依赖注入 DI<sup>(Dependency Injection)</sup>

Spring 机制：控制反转 IoC<sup>(Inversion of Control)</sup> 容器

使用 Spring 框架将带来以下好处：

* 由 Spring 负责应用程序对象的创建并注入他们的依赖项，简化 Java 应用程序组成
* Spring 推动以 POJO 的形式开发应用程序

具体包括：

* 事务：通过 `@Transactional` 注解对应事务方法，由 Spring 选择和使用事务方式，统一本地事务（JDBC）与全局事务（JIA）的使用
* 安全：通过 `@Secured` 可以指定允许调用某函数的身份认证授权，实现方法级的安全
* Java 管理扩展：Spring 提供对 ((20240311161549-q2f3url "JMX")) 的支持，通过 `@ManagedResource` 将类注册到 MBean 服务器，通过 `@ManagedOperation` 暴露 JMX 操作方法，不需要直接使用 JMX API
* Java 消息服务：Spring 通过 `JmsTemplate` 简化了与 JMS 提供者之间的消息传递，不需要直接处理 JMS API
* 缓存：Spring 缓存抽象提供一致的使用缓存的方法，简化不同缓存方案之间的交互
