# Spring IoC 容器

> [!note]
> 依赖项：某个对象需要调用的其他对象
> 
> 依赖注入：DI，一种设计模式，对象的依赖项通过构造函数和 `setter` 方法成为对象的参数
> 
> 控制反转：由容器负责创建和注入依赖，而不是应用程序手动创建
> 
> bean：Spring IoC 容器创建的对象

IoC 容器的配置可以通过 `xml` 或者注解完成。这里以 `xml` 的配置为例：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="fdController" class="com.example.mybank.FixedDepositController">
        <constructor-arg ref="fdServer" />
    </bean>

    <bean id="fdServer" class="com.example.mybank.FixedDepositService" />
</beans>
```

Spring 使用 `<beans>` 为根标签，每个由 IoC 创建的对象都由 `<bean>` 标签配置。
- `id` 属性为该对象的唯一名称，后面其他对象使用、Java 代码里都是用该属性获取
- `class` 属性为对象实际类型

`<bean>` 标签下，使用 `<constructor-arg>` 子标签选择构造函数参数（使用构造函数注入），使用 `<property>` 子标签选择 `setter` 注入对象（使用 `setXxx` 类型的函数）。

根据注入的 xml 文件可以推断出，`FixedDepositService` 有一个无参构造，`FixedDepositController` 有一个带有一个参数的构造函数，该参数类型为 `FixedDepositService`。上面代码如果写成 Java，大概相当于：

```java
FixedDepositService fdServer = new FixedDepositService();
FixedDepositController fdController = new FixedDepositController(fdServer);
```

注入到 IoC 容器后，代码中可以通过 `ApplicationContext` 获取：

```java
// 设 xml 文件在 resources/applicationContext.xml
ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
// 根据 id 获取对应的对象
FixedDepositController controller = context.getBean("fdController", FixedDepositController.class);
```
# Spring 好处

使用 Spring 框架将带来以下好处：
- 由 Spring 负责应用程序对象的创建并注入他们的依赖项，简化 Java 应用程序组成
- Spring 推动以 POJO 的形式开发应用程序

具体包括：
- 事务：通过 `@Transactional` 注解对应事务方法，由 Spring 选择和使用事务方式，统一本地事务（JDBC）与全局事务（JIA）的使用
- 安全：通过 `@Secured` 可以指定允许调用某函数的身份认证授权，实现方法级的安全
- Java 管理扩展：Spring 提供对 [[Java JMX|JMX]] 的支持，通过 `@ManagedResource` 将类注册到 MBean 服务器，通过 `@ManagedOperation` 暴露 JMX 操作方法，不需要直接使用 JMX API
- Java 消息服务：Spring 通过 `JmsTemplate` 简化了与 [[JMS(Java消息服务)入门教程|JMS]] 提供者之间的消息传递，不需要直接处理 JMS API
- 缓存：Spring 缓存抽象提供一致的使用缓存的方法，简化不同缓存方案之间的交互