# 概述

Spring 是一套分层 Java 一站式轻量级开发框架，以 `IoC` 和 `AOP` 为核心。

> [!Note]- 控制反转
> 控制反转 `IoC`，Inversion of Control，将设计好的对象交给容器控制，而不是在对象内部直接控制
> - 控制：控制对象创建和外部资源获取
> - 反转：由容器负责创建对象和注入依赖

> [!Note]- 依赖注入
> 依赖注入 `DI`，Dependency Injection，组件之间的依赖关系由容器在运行期决定，并由容器动态的注入到组件中
> - 依赖：`IoC` 容器管理的对象依赖于 `IoC` 容器，`IoC` 管理的对象依赖于 `IoC` 容器提供的对象
> - 注入：`IoC` 容器将对象依赖的外部资源（对象，资源，常量等）注入到管理的对象
> 
> 依赖注入是控制反转的实现方式，控制反转是依赖注入的目的

> [!Node]- 面向切面编程
> 面向切面编程 `AOP`，Aspect Oriented Programming，通过预编译和运行时动态代理等方式，实现对程序某一特定功能的统一维护的技术

Spring 主要优点有：
- 方便解耦，简化开发，通过 IoC 避免硬编码
- 提供 AOP 支持
- 声明式事务，提高开发效率和质量
- 便于调试，可使用非容器依赖的方式完成测试工作
- 方便集成，对各种框架提供直接支持

![[Pasted image 20230922012528.png]]

Spring 主要体系结构有：
- 核心模块 Core Container：实现 IoC 容器，扩展 BeanFactory 并实现了 Context，提供表达式语言 EL 的支持
- AOP 模块：提供 AOP Alliance 的规范实现，整合 AspectJ 框架
- 数据库访问集成模块 Data Access/Integration：包括 JDBC，ORM，OXM，JMS 和事务管理
	- JDBC：提供 JDBC 的样例模板，消除冗长的 JDBC 编码和事务控制
	- ORM：与 对象-关系 模型框架集成，包括 Hibernate，JPA，MyBatis 等
	- OXM：提供 Object/XML 映射实现，包括 AXB，Castor，XMLBeans 和 XStream
	- JMS：Java Messaging Service，提供消息的 生产者-消费者 模板模型
- Web 模块：建立在 ApplicationContext 上，提供 Web 功能，提供 Spring MVC 框架且可以整合 Struts2 等其他 MVC 框架
- 测试模块 Test：用非容器依赖的方法实现几乎所有测试工作，支持 JUnit 和 TestNG 等框架
# Spring Initializer

Spring Initializer 是 Spring 提供的 Spring 项目样板自动生成的工具，已集成到 Idea 等 IDE 中。

可以通过 [Spring Initializr](https://start.spring.io/) 访问。
# 目录

- [[Spring Boot]]