# bean 定义继承

使一个 `bean` 定义从另一个 `bean` 定义继承配置信息。

```xml
<bean id="daoTemplate" abstract="true">
    <property name="databaseOperations" ref="databaseOperations" />
</bean>

<bean id="databaseInfo" class="com.example.mybank.chapter03.DatabaseInfo" />

<bean id="fixedDepositDao" factory-bean="daoFactory" factory-method="getFixedDepositDao" parent="daoTemplate">
    <constructor-arg value="jdbc" />
</bean>
```

在  `<bean>` 中使用 `parent` 属性引用其他 `bean` 标签，即可引用其内容，包括：
- `<property>`，`<constructor-arg>` 子标签
- 方法覆盖
- 初始化和销毁方法
- 工厂方法（`factory-bean`，`factory-method`）属性

添加了 `abstruct="true"` 的 `bean` 标签不能被实例化，不能被引用，只能通过 `parent` 属性引用，因此可以不必要包含用于实例化的属性（如 `class` 等）。

`parent` 引用的标签不一定是 `abstruct` 的。使用 `parent` 后仍可以使用 `<constructor-arg>`，`<property>` 等其他标签和属性。
# 构造函数参数匹配

在[[Spring-框架基础#基于构造函数的 DI|基于构造函数的 DI]]章节中使用了基于构造函数的 `bean` 标签。在那个例子中，使用 `index` 指定第几个参数，如果值为 `bean` 对象引用，使用 `ref` 指定 `id`；如果是简单的 Java 类型（基本类型、`String`），则使用 `value` 指定值字面量。
## 基于类型的匹配

当几个参数之间没有继承关系时，可以直接引用对象，Spring 可以根据类型判断实参位置。

```java
// 仅截取构造函数
public ServiceTemplate(JmsMessageSender jmsMessageSender, 
					   EmailMessageSender emailMessageSender, 
					   WebServiceInvoker webServiceInvoker) {  
    this.jmsMessageSender = jmsMessageSender;  
    this.emailMessageSender = emailMessageSender;  
    this.webServiceInvoker = webServiceInvoker;  
}
```

```xml
<!-- 仅 ServiceTemplate 的 bean 节选 -->
<bean id="serviceTemplate" class="com.example.mybank.chapter03.service.ServiceTemplate">
    <constructor-arg ref="webServiceInvoker" />
    <constructor-arg ref="jmsMessageSender" />
    <constructor-arg ref="emailMessageSender" />
</bean>
```

上面的例子中，`webServiceInvoker`，`jmsMessageSender`，`emailMessageSender` 三个参数的顺序与构造函数的顺序不同，且没有使用 `index` 等参数限定顺序，但 Spring 能正确初始化，因为 `JmsMessageSender`，`EmailMessageSender`，`WebServiceInvoker` 三个类没有继承关系，Spring 能正确识别每个参数的位置。

若两个参数类型有继承关系，使用 `type` 属性确定类型

若存在多个参数类型相同，则应使用 `index` 而非 `type`
## 基于名称的匹配

使用 `name` 属性可以指定构造函数参数名称

```java
@ConstructorProperties({"webServiceUrl", "active", "timeout", "numberOfRetrialAttempts"})
public TransferFundsServiceImpl(String webServiceUrl,
								boolean active,
								long timeout,
							    int numberOfRetrialAttempts) {
    this.webServiceUrl = webServiceUrl;
    this.active = active;
    this.timeout = timeout;
    this.numberOfRetrialAttempts = numberOfRetrialAttempts;
}
```

```xml
<bean id="transferFundsService" class="com.example.mybank.chapter03.service.TransferFundsServiceImpl">
    <constructor-arg name="webServiceUrl" value="http://someUrl.com/xyz" />
    <constructor-arg name="active" value="true" />
    <constructor-arg name="numberOfRetrialAttempts" value="5" />
    <constructor-arg name="timeout" value="200" />
</bean>
```

使用 `name` 属性需要至少满足以下条件之一，以保证形参名在运行时可见：
- 编译时启动了参数名称发现的调试标志（`javac -parameters`）
- 构造函数使用 `java.beans.ConstructorProperties` 注解标记

由于 `@ConstructorProperties` 仅能修饰构造函数，因此开启调标志是工厂方法使用 `name` 属性的唯一选择。
# 配置 bean 和构造参数
# 内置属性编辑器
# 注册属性编辑器
# p c 命名空间
# util 模式
# FactoryBean 接口
# 模块化 bean 配置