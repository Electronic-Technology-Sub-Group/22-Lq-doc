![[Pasted image 20230922012528.png]]

Spring Framework 主要体系结构有：
- 核心模块 Core Container：实现 IoC 容器，扩展 BeanFactory 并实现了 Context，提供表达式语言 EL 的支持
- AOP 模块：提供 AOP Alliance 的规范实现，整合 AspectJ 框架
- 数据库访问集成模块 Data Access/Integration：包括 JDBC，ORM，OXM，JMS 和事务管理
	- JDBC：提供 JDBC 的样例模板，消除冗长的 JDBC 编码和事务控制
	- ORM：与 对象-关系 模型框架集成，包括 Hibernate，JPA，MyBatis 等
	- OXM：提供 Object/XML 映射实现，包括 AXB，Castor，XMLBeans 和 XStream
	- JMS：Java Messaging Service，提供消息的 生产者-消费者 模板模型
- Web 模块：建立在 ApplicationContext 上，提供 Web 功能，提供 Spring MVC 框架且可以整合 Struts2 等其他 MVC 框架
- 测试模块 Test：用非容器依赖的方法实现几乎所有测试工作，支持 JUnit 和 TestNG 等框架
# 核心模块
## IoC 容器

IoC 在应用程序中主要由 `BeanFactory` 实现，对应 `org.springframework.beans` 和 `org.springframework.context` 两个包。

`BeanFactory` 提供通过 IoC 管理任何对象的机制，`ApplicationContext` 则扩展了 `BeanFactory` 增加了更多资源管理和上下文相关实现。

Spring IoC 容器支持 XML，注解等多种配置方式。以下先以 XML 
### bean

被 IoC 管理的对象称为 `bean`，由 Spring IoC 容器组装和管理。以 XML 配置为例：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="hello" class="com.example.demo.bean.Hello">
        <property name="hello" value="string" />
    </bean>
</beans>
```

其中，相关类的定义为：

```kotlin
class Hello {
    // kotlin 自动生成 setHello(String) 方法
    // 如果是 Java 需要自己声明
    lateinit var hello: String
}
```

XML 文档位于 resources 目录下，通过上下文加载该 XML，即可通过 `getBean` 获取创建后的对象

```kotlin
// resources/hello.xml
val context = ClassPathXmlApplicationContext("hello.xml")
val hello = context.getBean("hello") as Hello
```

bean 标签常见属性有：

| bean 属性      | 说明                                                                                |
| -------------- | ----------------------------------------------------------------------------------- |
| id             | 唯一标识符                                                                          |
| class          | bean 类型（包名+类名），内部类使用 `.` 或 `$` 分隔均可                              |
| name           | 指定别名，多个别名之间使用空格，逗号或分号分隔                                      |
| destory-method | 当对象销毁时所调用的函数                                                            |
| lazy-init      | 值为 true 时，会在使用时创建，否则在初始化 ApplicationContext 时创建                |
| depends-on     | 该属性值对应的 bean 将会在该属性之前创建，多个 bean 使用 `,` 分隔，也会影响销毁顺序 |
| scope          | 作用域，详见[[#作用域]]                                                             |
#### 命名空间属性

`p:` 属性需要引入 `xmlns:p="http://www.springframework.org/schema/p"` 约束
`c:` 属性需要引入 `xmlns:c="http://www.springframework.org/schema/c"` 约束

bean 中每个 p 属性代表一个 property 标签，方便简单类的编写。
- `"p:属性名"="值"` 等效于 `<property name="属性名" value="值" />`
- `"p:属性名-ref"="值"` 等效于 `<property name="属性名" ref="值" />`

bean 中每个 c 属性代表一个 constructor-arg 标签
- `"c:属性名"="值"` 等效于 `<construcor-arg name="属性名" value="值" />`
- `"c:属性名-ref"="值"` 等效于 `<construcor-arg name="属性名" ref="值" />`
- `"c:_1"="值"` 等效于 `<constructor-arg index="1" value="值" />`
- `"c:_1-ref"="值"` 等效于 `<constructor-arg index="1" ref="值" />`

下面两种写法是等效的

```xml
<beans>
    <bean id="beanObj" class="BeanObj">
        <property name="username" value="admin" />
        <property name="password" value="123456" />
    </bean>
</beans>
```

```xml
<beans>
    <bean id="beanObj" class="BeanObj"
          p:username="admin"
          p:password="123456"/>
</beans>
```
#### 元素 id

有时候我们需要传递一个元素 id 作为值，我们可以使用一个字符串进行传递。

```xml
<bean id="targetElementId" />
<bean>
    <property name="target" value="targetElementId" />
</bean>
```

使用 `idref` 标签可以让 Spring 帮忙检查该 id 是否存在，以防拼写错误

```xml
<bean id="targetElementId" />
<bean>
    <property name="target">
        <idref bean="targetElementId" />
    </property>
</bean>
```
#### 内部 bean

在 property 标签中，可以包含一个 bean 标签。该标签可以没有 id 属性。

```xml
<bean id="outer" class="...">
	<property name="target">
		<bean class="com.example.Person"> <!-- this is the inner bean -->
			<property name="name" value="Fiona Apple"/>
			<property name="age" value="25"/>
		</bean>
	</property>
</bean>
```
#### 集合

Spring 支持 `java.util.Properties` 类型变量，在 `<property>` 中使用 `<value>` 标签，之后每行一组键值对即可

```xml
<bean>
    <property id="this_is_a_property_object">
        <value>
            key1=value1
            key2=value2
        </value>
    </property>
</bean>
```

也可以使用 `<props>` 以 xml 形式定义

```xml
<bean>
    <property id="this_is_a_property_object">
        <props>
            <prop key="key1">value1</prop>
            <prop key="key2">value2</prop>
        </props>
    </property>
</bean>
```

集合支持 List，Map 和 Set，集合内值可以是 `value`，`bean`，`ref`，`idref`，`null`，其他集合或 Properties。

```xml
<bean id="moreComplexObject" class="example.ComplexObject">
	<!-- 列表 java.util.List -->
	<!-- 数组、Set（java.util.Set）与列表相似，只是标签名为 array，set -->
	<property name="someList">
		<list>
			<value>a list element followed by a reference</value>
			<ref bean="myDataSource" />
			<null />
		</list>
	</property>
	<!-- Map（java.util.Map） -->
	<!-- Map 内使用 entry 标签，key 为键，值与 property 相似 -->
	<property name="someMap">
		<map>
			<entry key="an entry" value="just some string"/>
			<entry key="a ref" value-ref="myDataSource"/>
		</map>
	</property>
</bean>
```

Spring 支持 Java 泛型约束，当属性中的集合包含泛型时，配置文档中的值也应符合泛型定义。

同种集合或 Properties 之间的值可以合并，子集合包含所有父集合内容。

子集合若包含与父集合相同的内容（如 Map 或 Properties 数据中包含相同的 key，Set 中包含相同的值），子集合的值将覆盖父集合的值。

```xml
<beans>
	<bean id="parent" abstract="true" class="example.ComplexObject">
		<property name="adminEmails">
			<props>
				<prop key="administrator">administrator@example.com</prop>
				<prop key="support">support@example.com</prop>
			</props>
		</property>
	</bean>
	
	<bean id="child" parent="parent">
		<property name="adminEmails">
			<!-- the merge is specified on the child collection definition -->
			<props merge="true">
				<prop key="sales">sales@example.com</prop>
				<prop key="support">support@example.co.uk</prop>
			</props>
		</property>
	</bean>
<beans>
```
#### 复合路径

`name` 属性的值可以是 bean 对象的属性的属性，之间通过 `.` 相连接

```xml
<bean id="something" class="things.ThingOne">
	<property name="fred.bob.sammy" value="123" />
</bean>
```

等效于

```java
import things.ThingOne;

ThingOne something = new ThingOne();
// fred.bob.sammy = "123"
something.getFred().getBob().setSammy("123");
```
### 依赖注入

Spring IoC 支持的注入方式主要支持通过 Setter，构造和工厂方法注入依赖。

为指定属性添加 `@Autowired` 注解表示该属性必须被注入。
#### set 注入

通过 `<bean> -> <property>` 标签指定 set 方法和值，在完成构造后调用

```xml
<beans ...>
    <bean id="objName" class="ObjClass">
        <property name="prop1" value="value1" />
        <property name="prop2" ref="otherObj" />
    </bean>

    <bean id="otherObj" class="OtherObjClass" />
</beans>
```

当使用 `ClassPathXmlApplicationContext` 加载以上 XML 配置时，相当于

```java
// <bean name="otherObj" class="OtherObjClass" />
OtherObjClass otherObj = new OtherObjClass(); 

/*
<bean name="objName" class="ObjClass">
    <property name="prop1" value="value1" />
    <property name="prop2" ref="otherObj" />
</bean>
*/
ObjClass objName = new ObjClass();
objName.setProp1("value1");
objName.setProp2(otherObj);
```
#### 构造器注入

默认 Spring IoC 使用无参构造创建 bean 对象，可以在构造函数中传入对应依赖，即使用有参的构造

通过 `<bean> -> <constructor-arg>` 标签按顺序、类型或名称指定构造函数需要的对象

- 一般情况下，构造函数通过类型匹配。在所有参数类型不冲突的情况下，可以不加任何额外属性直接使用

```xml
<beans ...>
    <bean id="dep1" class="Dep1">
    <bean id="dep2" class="Dep2">
    
    <bean id="beanObj" class="BeanObj">
        <constructor-arg ref="dep1" />
        <constructor-arg ref="dep2" />
    </bean>
</beans>
```

```kotlin
class Dep1
class Dep2
// Dep1 与 Dep2 没有任何继承关系，不存在潜在的冲突
class BeanObj(Dep1 d1, Dep2 d2)
```

- 对于字符串和基本类型，可以通过 `type` 属性限定

```xml
<beans ...>
    <bean id="beanObj" class="BeanObj">
        <constructor-arg type="int" value="10" />
        <constructor-arg type="boolean" value="true" />
        <constructor-arg type="java.lang.String" value="20" />
    </bean>
</beans>
```

- 可以使用 `index` 属性精确控制传入参数的位置。索引从 0 开始

```xml
<beans ...>
    <bean id="beanObj" class="BeanObj">
        <constructor-arg index="0" value="10" />
        <constructor-arg index="1" value="20" />
    </bean>
</beans>
```

- 可以通过 `name` 属性通过参数名指定。使用该功能需要开启 `debug` 标志或在对应构造函数上添加 `@ConstructorProperties` 注解

```xml
<beans ...>
    <bean id="beanObj" class="BeanObj">
        <constructor-arg name="val1" value="a" />
        <constructor-arg name="val2" value="b" />
        <constructor-arg name="val3" value="c" />
    </bean>
</beans>
```

```kotlin
class InjectByName
@ConstructorProperties("val1", "val2", "val3")
constructor(val1: String, val2: String, val3: String)
```
#### 工厂方法

通过 `bean` 的 `factory-method` 属性可以指定对应的工厂方法

工厂方法可以带有参数，参数使用 `constructor-arg` 指定，规则与[[#构造函数]]相同

```xml
<beans ...>
    <bean id="beanObj1" class="BeanObj" factory-method="factory1" />
    <bean id="beanObj2" class="BeanObj" factory-method="factory2" />
</beans>
```

```kotlin
class BeanObj private constructor() {
    companion object {
        @JvmStatic fun factory1(): BeanObj {}
        @JvmStatic fun factory2(): BeanObj {}
    }
}
```

当工厂方法非静态方法时，可以使用 `factory-bean` 属性可以指定工厂方法所在的 bean

```xml
<beans ...>
    <bean id="fac_bean" class="BeanObjFactories"/>
    <bean id="beanObj1" class="BeanObj"
          factory-method="factory1"
          factory-bean="fac_bean" />
    <bean id="beanObj2" class="BeanObj"
          factory-method="factory2"
          factory-bean="fac_bean" />
</beans>
```

```kotlin
class BeanObj

class BeanObjFactories {
    fun factory1(): BeanObj {}
    fun factory2(): BeanObj {}
}
```
### alias

可以通过 `alias` 创建一个别名。别名与指向的 bean 是同一个对象

```xml
<beans ...>
    <bean id="beanObj" class="BeanObj" />
    <alias name="beanObj" alias="anotherName" />
</beans>
```

事实上，在声明 bean 时可以指定别名

```xml
<beans ...>
    <bean id="beanObj" name="anotherName" class="BeanObj" />
</beans>
```
### import

将多个配置导入到同一个配置中，多用于团队开发不同功能

```xml
<!-- a.xml -->
<beans>
    <bean id="beanObj1"/>
</beans>
```

```xml
<!-- b.xml -->
<beans>
    <bean id="beanObj2"/>
</beans>
```

```xml
<!-- c.xml -->
<beans>
    <bean id="beanObj3" />
</beans>
```

```xml
<!-- app.xml -->
<beans>
    <import resource="a.xml"/>
    <import resource="b.xml"/>
    <import resource="c.xml"/>
</beans>
```
### 注入方法

方法注入可以使某个方法返回特定的 bean 对象
#### Lookup 注入

通过 `<lookup-method>` 标签注入方法，实质是创建给定类的子类，要求符合以下条件：
- 被注入类和方法没有 `final` 修饰
- 方法必须是 `public` 或 `protected` 的，允许 `abstract` 方法
- 函数必须无参且有对应的返回类型，可以使用 `@Lookup` 注解也可以省略
- 函数所在 bean 不能使用构造器注入或工厂方法注入

```xml
<bean id="myCommand" class="fiona.apple.AsyncCommand" scope="prototype" />

<bean id="commandManager" class="fiona.apple.CommandManager">
	<lookup-method name="createCommand" bean="myCommand"/>
</bean>
```

```kotlin
abstract class CommandManager {
    @Lookup
	protected abstract fun createCommand(): Command
}
```
#### Replace 注入

通过 `<replaced-method>` 方法注入，实质是代理，允许覆盖 bean 类中的任意方法，用处不大

```xml
<bean id="myValueCalculator" class="x.y.z.MyValueCalculator">
	<!-- arbitrary method replacement -->
	<replaced-method name="computeValue" replacer="replacementComputeValue">
		<arg-type>String</arg-type>
	</replaced-method>
</bean>

<bean id="replacementComputeValue" class="a.b.c.ReplacementComputeValue"/>
```
### 作用域

作用域通过 bean 的 `scope` 属性定义，默认作用域是 `singleton`

| 可选作用域  | 说明                                                            |
| ----------- | --------------------------------------------------------------- |
| singleton   | 每个 bean 在容器中是一个单例                                    |
| prototype   | 每次使用 bean 对象（getBean，配置中bean属性等）都创建一个新对象 |
| request     | 每个 bean 在一个 HTTP 请求中是单例的，**仅 Web 应用**           |
| session     | 每个 bean 在一个 Session 中是单例的，**仅 Web 应用**            |
| application | 每个 bean 在一个 ServletContext 中是单例的，**仅 Web 应用**     |
| websocket   | 每个 bean 在一个 WebSocket 通信中是单例的，**仅 Web 应用**      | 

