---
参考资料: 《Spring 学习指南（第四版）》
版本: Java17，Spring 5
---

Spring 核心是依赖注入 DI（Dependency Injection），机制是控制反转 IoC（Inversion of Control）容器。

Spring 框架为开发者提供一个专注于编写应用业务逻辑的基础设施，各个模块组包括：

| 模块组                     | 说明                                                                                                                         |
| ----------------------- | -------------------------------------------------------------------------------------------------------------------------- |
| Core container          | 基础模块，`spring-core` 与 `spring-beans` 提供了基础的 DI 功能和 IoC 容器实现，`spring-expressions` 提供了 Spring 表达式语言支持                         |
| AOP and instrumentation | 提供支持 AOP 的类和工具，`spring-aop` 提供 Spring AOP 功能，`spring-instrument` 提供对类工具的支持                                                 |
| Messaging               | `spring-messaging` 用于简化开发基于消息的应用                                                                                           |
| Data Access/Integration | 简化数据库交互模块。`spring-jdbc` 简化使用 JDBC 与数据库交互，`spring-orm` 提供 ORM（对象关系映射）集成，`spring-tx` 提供编程式、声明式事务管理                           |
| Web                     | 简化开发 Web 与 portlet 应用。`spring-web`、`spring-webmvc` 用于开发 Web 应用和 RESTful Web 服务，`spring-websocket` 用于使用 WebSocket 开发 Web 应用 |
| Test                    | `spring-test` 模块简化单元测试、集成测试流程                                                                                              |

# 目录

- [[Spring 概述]]
- [[Spring 框架基础]]
- [[Spring 配置 bean]]
- [[Spring 依赖注入]]
- [[Spring 自定义 bean]]
- [[Spring 注解驱动开发]]
- [[Spring 基于 Java 的容器配置]]
- [[Spring 数据库]]
- [[Spring Data]]
- [[Spring 消息、异步与缓存]]
- [[Spring 切面编程]]
- [[Spring Web MVC]]
- [[Spring Web MVC 验证和数据绑定]]
- [[Spring Web MVC REST 服务]]
- [[Spring Web MVC 国际化、文件与异步]]
- [[Spring Security 保护]]
- [[Spring 函数式编程]]
- [[Spring RxJava2 反应式编程]]
- [[Spring WebFlex、Data、Security 开发反应式 RESTful 应用]]
- [[Spring 6 升级]]