# IoC 容器

IoC 容器的配置可以通过 `xml` 或者注解完成。这里以 `xml` 的配置为例：

bean：由 Spring IoC 创建的对象

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

* `id` 属性为该对象的唯一名称，后面其他对象使用、Java 代码里都是用该属性获取
* `class` 属性为对象实际类型

`<bean>` 标签下，使用 `<constructor-arg>` 子标签选择构造函数参数（使用<span data-type="text">📦</span>构造函数注入），使用 `<property>` 子标签选择 `setter` 注入对象（使用 `setXxx` 类型的函数）。

根据注入的 xml 文件可以推断出，`FixedDepositService` 有一个无参构造，`FixedDepositController` 有一个带有一个参数的构造函数，该参数类型为 `FixedDepositService`。上面代码如果写成 Java，大概相当于：

```java
FixedDepositService fdServer = new FixedDepositService();
FixedDepositController fdController = new FixedDepositController(fdServer);
```

通过 `setter` 注入方法称为基于属性的注入，属性名与 `setter` 方法名对应。

基于构造器的注入和基于属性的注入可以同时使用

```java
class EmailClassSender {

    public void setHost(String host) { ... }
    public void setEmailProperties(Properties properties) { ... }
}
```

```xml
<bean id="emailProperties" ... />

<bean id="emailMessageSender" class="EmailClassSender">
    <property name="host" value="smtp.163.com" />
    <property name="emailProperties" ref="emailProperties" />
</bean>
```

还可以通过 util 模式构建对象。

注入到 IoC 容器后，代码中可以通过 `ApplicationContext` 获取：

```java
// 设 xml 文件在 resources/applicationContext.xml
ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
// 根据 id 获取对应的对象
FixedDepositController controller = context.getBean("fdController", FixedDepositController.class);
```

> # 面向接口编程
>
> 依赖接口而非具体实现类，使依赖类与依赖项之间松耦合。
>
> * 创建引用依赖接口
> * 定义 `<bean>` 元素，注入具体实现类
