# IoC 容器

IoC 在应用程序中主要由 `BeanFactory` 实现，对应 `org.springframework.beans` 和 `org.springframework.context` 两个包。

`BeanFactory` 提供通过 IoC 管理任何对象的机制，`ApplicationContext` 则扩展了 `BeanFactory` 增加了更多资源管理和上下文相关实现。

被 IoC 管理的对象称为 `bean`，由 Spring IoC 容器组装和管理。Spring IoC 容器支持 XML，注解等多种配置方式，实例先以 XML 配置为例：

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
## bean 类型

一般对象可以直接通过 bean 创建和构造。

`null` 类型使用 `<null />` 标签声明。

除此之外，`bean` 标签还支持创建其他几种特定的类型。
### 元素 id

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
### 内部 bean

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
### 集合

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
### 复合路径

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
## 依赖注入

Spring IoC 支持的注入方式主要支持通过 Setter，构造和工厂方法注入依赖。

为指定属性添加 `@Autowired` 注解表示该属性必须被注入。
### set 注入

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
### 构造器注入

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
### 工厂方法

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

当工厂方法非静态方法时，可以使用 `factory-bean` 属性可以指定工厂方法所在的 bean。此时，class 属性不起效（类型为工厂方法返回值的类型）

```xml
<beans ...>
    <bean id="fac_bean" class="BeanObjFactories"/>
    <bean id="beanObj1"
          factory-method="factory1"
          factory-bean="fac_bean" />
    <bean id="beanObj2"
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
## bean 属性

bean 标签常见属性有：

| bean 属性  | 说明                                                                                   |
| ---------- | -------------------------------------------------------------------------------------- |
| id         | 唯一标识符                                                                             |
| class      | bean 类型（包名+类名），内部类使用 `.` 或 `$` 分隔均可                                 |
| name       | 指定别名，多个别名之间使用空格，逗号或分号分隔                                         |
| lazy-init  | 值为 true 时，会在使用时创建，否则在初始化 ApplicationContext 时创建                   |
| depends-on | 该属性值对应的 bean 将会在该属性之前创建，多个 bean 使用 `,` 分隔，也会影响销毁顺序    |
| scope      | 作用域，详见[[#作用域]]                                                                |
| parent     | 模板继承。父标签的属性和内部其他标签都会被继承，子标签同名属性和标签可以覆盖父标签的值 |
| abstract   | 若值为 false，该 bean 不会被实例化，也不能被获取。通常用于 parent 模板                 | 

除此之外，还可以通过命名空间引入一些额外的属性：
- `p:` 属性需要引入 `xmlns:p="http://www.springframework.org/schema/p"` 命名空间
- `c:` 属性需要引入 `xmlns:c="http://www.springframework.org/schema/c"` 命名空间

bean 中每个 p 属性代表一个 property 标签，方便简单类的编写。
- `"p:属性名"="值"` 等效于 `<property name="属性名" value="值" />`
- `"p:属性名-ref"="值"` 等效于 `<property name="属性名" ref="值" />`

bean 中每个 c 属性代表一个 constructor-arg 标签，详见 [[#构造器注入]]
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
## alias

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
## import

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
## 注入方法

方法注入可以使某个方法返回特定的 bean 对象
### Lookup 注入

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
### Replace 注入

通过 `<replaced-method>` 方法注入，实质是代理，允许覆盖 bean 类中的非 `abstract` 方法，被替换的对象（`replacer` 属性对应的 bean）应该是一个 MethodReplacer 对象

```xml
<bean id="myValueCalculator" class="x.y.z.MyValueCalculator">
	<!-- arbitrary method replacement -->
	<replaced-method name="computeValue" replacer="replacementComputeValue">
		<arg-type>String</arg-type>
	</replaced-method>
</bean>

<bean id="replacementComputeValue" class="a.b.c.ReplacementComputeValue"/>
```

`<arg-type>` 可以用来指定函数形参类型，用于区分不同重载函数的代理。每个 `<arg-type>` 的值应该是一个类型的全类名，或在不引起歧义的情况下可以是类名或类名的前几个字符。如以下几个类型都可以代表 String：
- `java.lang.String`
- `String`，如果不存在其他以 String 开头的类型作为参数
- `Str`，如果不存在其他以 Str 开头的类型作为参数
## 作用域

作用域通过 bean 的 `scope` 属性定义，默认作用域是 `singleton`

- 通用

| 可选作用域 | 说明                                                            |
| ---------- | --------------------------------------------------------------- |
| singleton  | 每个 bean 在容器中是一个单例                                    | 
| prototype  | 每次使用 bean 对象（getBean，配置中bean属性等）都创建一个新对象 |

- Web 应用

| 可选作用域  | 说明                                       |
| ----------- | ------------------------------------------ |
| request     | 每个 bean 在一个 HTTP 请求中是单例的       |
| session     | 每个 bean 在一个 Session 中是单例的        |
| application | 每个 bean 在一个 ServletContext 中是单例的 |
| websocket   | 每个 bean 在一个 WebSocket 通信中是单例的  | 
### scoped-proxy

有时候我们需要将一个小作用域的值赋给一个大作用域的值，这种情况通常要求大作用域的值保留最后一个值。这时可以使用 `<aop:scoped-proxy>` 标签。

下面的例子中，userPreferences 对象作用域为 session，在每个 Session 创建时新建。而 userManager 对象中保留了一个 userPreferences 属性，该属性指向最后一个 userPreferences（可能代表最后一个活跃的用户）

```xml
<bean id="userPreferences" class="com.something.UserPreferences" scope="session">
	<aop:scoped-proxy/>
</bean>

<bean id="userManager" class="com.something.UserManager">
	<property name="userPreferences" ref="userPreferences"/>
</bean>
```

使用 `<aop:scoped-proxy/>` 后，实际引用的是一个代理。默认 Spring 使用 CGLIB 生成的代理，为该标签添加 `proxy-target-class="false"` 属性可以生成 JDKInterface 标准代理

同时，使用 `<aop:scoped-proxy/>` 标签需要引入 aop 命名空间。

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd">
</beans>
```

一个完整实例如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd">

    <bean id="userPreference1"
          class="com.example.demo.bean.UserPreference" 
          factory-method="by1" 
          scope="prototype"/>
    <bean id="userPreference2" 
          class="com.example.demo.bean.UserPreference" 
          factory-method="by2" 
          scope="prototype">
        <aop:scoped-proxy/>
    </bean>
    <bean id="userManager" class="com.example.demo.bean.UserManager">
        <constructor-arg index="0" ref="userPreference1"/>
        <constructor-arg index="1" ref="userPreference2"/>
    </bean>
</beans>
```

其中，`UserPreference` 类为需要传递的 bean 类，通过计数判断创建了几个对象。注意需要 `<aop:scoped-proxy/>` 的类必须是可继承的

```kotlin
open class UserPreference(val id: Long) {
    companion object {
        private var OBJ_ID = 0L

        @JvmStatic fun by1(): UserPreference {
            val v = UserPreference(OBJ_ID++)
            println("Create by bean1: $v")
            return v
        }

        @JvmStatic fun by2(): UserPreference {
            val v = UserPreference(OBJ_ID++)
            println("Create by bean2: $v")
            return v
        }
    }

    override fun toString() = "UserPreference id=$id"
}

data class UserManager(val user1: UserPreference, val user2: UserPreference)
```

测试代码与结果如下：

```kotlin
fun main() {
    val context = ClassPathXmlApplicationContext("hello.xml")
    println("==============================================")
    val manager = context.getBean("userManager") as UserManager
    println(manager.user1.javaClass)
    println(manager.user2.javaClass)
    println(manager)
    println("==============================================")
    context.getBean("userPreference1")
    context.getBean("userPreference1")
    println(manager)
    println("==============================================")
    context.getBean("userPreference2")
    context.getBean("userPreference2")
    println(manager)
    println("==============================================")
}
```

```
Create by bean1: UserPreference id=0
==============================================
class com.example.demo.bean.UserPreference
class com.example.demo.bean.UserPreference$$SpringCGLIB$$0
Create by bean2: UserPreference id=1
UserManager(user1=UserPreference id=0, user2=UserPreference id=1)
==============================================
Create by bean1: UserPreference id=2
Create by bean1: UserPreference id=3
Create by bean2: UserPreference id=4
UserManager(user1=UserPreference id=0, user2=UserPreference id=4)
==============================================
Create by bean2: UserPreference id=5
UserManager(user1=UserPreference id=0, user2=UserPreference id=5)
==============================================
```
### 自定义作用域

1. 创建类，自定义作用域实现 `org.springframework.beans.factory.config.Scope` 接口。
2. 通过 `context.registerScope(name, scope)` 注册作用域

```kotlin
interface Scope {
    // 根据 bean 名获取 bean
    fun get(name: String, objectFactory: ObjectFactory<*>): Any
    // 移除指定 bean，返回 bean 对象或 null
    fun remove(name: String): Any?
    // 当 bean 移除时调用
    fun registerDestructionCallback(name: String, callback: Runnable)
    // 可用于获取非 IoC 容器资源
    fun resolveContextualObject(key: String): Any?
    // 作用域名，每个作用域名称唯一，可返回 null
    fun getConversationId(): String?
}
```

Spring 提供了一些预定义的作用域，如 `SimpleThreadScope`，默认不注册到 Context 中。
## 容器与 bean 交互

容器与 bean 交互通过生命周期和 `Aware` 两种方法。
### 生命周期回调

生命周期主要包括初始化和销毁两个阶段的回调。

初始化回调在创建对象，并将容器对象设置到 IoC 容器之后调用，通过以下几种方法设置：
- 实现 `InitializingBean` 接口
- 使用 `@PostConstruct` 注解回调方法（仅注解配置，XML 无用）
- 使用 `init-method` 属性指定 bean 初始化方法（包括 `@Bean` 的 initMethod 属性，自定义配置等）
	- 使用 `default-init-method` 属性设置 beans 内所有 bean 的默认初始化方法 

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans default-init-method="defaultInitMethod"
       default-destroy-method="defaultDestroyMethod">

    <!-- InitializingBean -->
    <!-- default-init-method -->
    <bean id="init-bean-default" 
          class="com.example.demo.bean.InitBeanDemo"/>
    <!-- InitializingBean -->
    <!-- init-method -->
    <bean id="init-bean"
          class="com.example.demo.bean.InitBeanDemo"
          init-method="initMethod"/>
</beans>
```

```kotlin
class InitBeanDemo: InitializingBean {
    fun initMethod() = println("init-method")
    fun defaultInitMethod() = println("default-init-method")
    override fun afterPropertiesSet() = println("InitializingBean")
}
```

销毁回调在 bean 从容器销毁时调用，通过以下几种方法设置：
- 实现 `DisposableBean` 接口
- 使用 `@PreDestroy` 注解回调方法（仅注解配置，XML 无用）
- 使用 `destroy-method` 属性指定 bean 销毁方法
	- 使用 `default-destroy-method` 属性设置 beans 内所有 bean 的默认销毁方法 

每个 bean 的相同的生命周期可以同时被多种途径声明。若各途径声明的方法不同，则按上述顺序依次执行。若指定的方法相同，则该方法只会被执行一次。
### Lifecycle 接口

实现 Lifecycle 接口可以使对象有自己的生命周期，当 `ApplicationContext` 开始或结束时会调用对应的方法。需要调用 `context.registerShutdownHook()` 启用。

`LifecycleProcessor` 是 `Lifecycle` 接口的一个扩展，提供刷新和关闭时调用的方法。

`SmartLifecycle` 是另一个扩展接口，可以在上下文开始或结束时自动调用，并通过 `isAutoStartup` 方法控制。
### Aware 接口

`Aware` 接口可以被 bean 类实现，由对应 BeanFactory 调用，不同的 `Aware` 接口会为 bean 注入特定的对象
- `ApplicationContextAware`：由 `ApplicationContext` 调用，注入 `ApplicationContext` 
- `BeanNameAware`：由 `ApplicationContext` 调用，注入 bean id

> [!info]- 表：一些 Aware
> 
| Name                             | Injected Dependency                                                                                      | Explained in…​                                                                                                                                        | 
| -------------------------------- | -------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------- |
| `ApplicationContextAware`        | Declaring `ApplicationContext`.                                                                          | [`ApplicationContextAware` and `BeanNameAware`](https://docs.spring.io/spring-framework/reference/core/beans/factory-nature.html#beans-factory-aware) |
| `ApplicationEventPublisherAware` | Event publisher of the enclosing `ApplicationContext`.                                                   | [Additional Capabilities of the `ApplicationContext`](https://docs.spring.io/spring-framework/reference/core/beans/context-introduction.html)         |
| `BeanClassLoaderAware`           | Class loader used to load the bean classes.                                                              | [Instantiating Beans](https://docs.spring.io/spring-framework/reference/core/beans/definition.html#beans-factory-class)                               |
| `BeanFactoryAware`               | Declaring `BeanFactory`.                                                                                 | [The `BeanFactory` API](https://docs.spring.io/spring-framework/reference/core/beans/beanfactory.html)                                                |
| `BeanNameAware`                  | Name of the declaring bean.                                                                              | [`ApplicationContextAware` and `BeanNameAware`](https://docs.spring.io/spring-framework/reference/core/beans/factory-nature.html#beans-factory-aware) |
| `LoadTimeWeaverAware`            | Defined weaver for processing class definition at load time.                                             | [Load-time Weaving with AspectJ in the Spring Framework](https://docs.spring.io/spring-framework/reference/core/aop/using-aspectj.html#aop-aj-ltw)    |
| `MessageSourceAware`             | Configured strategy for resolving messages (with support for parameterization and internationalization). | [Additional Capabilities of the `ApplicationContext`](https://docs.spring.io/spring-framework/reference/core/beans/context-introduction.html)         |
| `NotificationPublisherAware`     | Spring JMX notification publisher.                                                                       | [Notifications](https://docs.spring.io/spring-framework/reference/integration/jmx/notifications.html)                                                 |
| `ResourceLoaderAware`            | Configured loader for low-level access to resources.                                                     | [Resources](https://docs.spring.io/spring-framework/reference/web/webflux-webclient/client-builder.html#webflux-client-builder-reactor-resources)     |
| `ServletConfigAware`             | Current `ServletConfig` the container runs in. Valid only in a web-aware Spring `ApplicationContext`.    | [Spring MVC](https://docs.spring.io/spring-framework/reference/web/webmvc.html#mvc)                                                                   |
| `ServletContextAware`            | Current `ServletContext` the container runs in. Valid only in a web-aware Spring `ApplicationContext`.   | [Spring MVC](https://docs.spring.io/spring-framework/reference/web/webmvc.html#mvc)                                                                   |
## 容器扩展

