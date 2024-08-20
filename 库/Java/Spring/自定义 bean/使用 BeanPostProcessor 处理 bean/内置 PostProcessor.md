# 内置 PostProcessor

# 内置 BeanPostProcessor

Spring 内置的 <span data-type="text" parent-style="color: var(--b3-card-warning-color);background-color: var(--b3-card-warning-background);">BeanPostProcessor</span> 默认没有注入到 `ApplicationContext` 中，需要显式注册

* `RequiredAnnotationBeanPostProcessor`：检查 `bean` 对象中的属性是否已被配置

  该 `BeanPostProcessor` 是一个强制的属性初始化检查。在开启检查后，对于 `bean` 类中所有被 `@Required` 注解的 `setter` 属性，如果没有配置其 `<property>` 或其他方法进行配置，Spring 将产生异常。

  `RequiredAnnotationBeanPostProcessor` 仅用于检查属性是否被配置，不赋值结果是否正确，即赋值为 `null` 也是正确的。

  `@Required` 在新版本的 Spring 中已被弃用，被 `@Autowire` 或 `@Inject` 等其他注解取代
* `DestructionAwareBeanPostProcessor`：表示一个 bean 定义
* `CommonAnnotationBeanPostProcessor`：启用 JSR250 注解支持

# 内置 BeanFactoryPostProcessor

## `PropertySourcesPlaceholderConfigurer`

`PropertySourcesPlaceholderConfigurer` 在 Spring 实现配置文件模板。在属性中，将 `${<property_name>}` 替换为 `locations` 属性配置的文档中的对应值

```xml
<bean class="org.springframework.context.support.PropertySourcesPlaceholderConfigurer">
    <property name="locations">
        <list>
            <value>classpath:database.properties</value>
            <value>classpath:web-service.properties</value>
        </list>
    </property>
    <property name="ignoreUnresolvablePlaceholders" value="false" />
</bean>
```

配置一个 `PropertySourcesPlaceholderConfigurer` 对象，并在 `locations` 属性中设置一个列表，内包含所有加载的属性文件

```properties
# database.properties
database.url=some_url
database.username=some_username
database.password=some_password
database.driverClass=some_driverClass
```

```properties
# web-service.properties
webservice.url=http://localhost:8080/webservice
```

之后，便可以在 `bean` 中引用对应的属性值

```xml
<bean id="datasource" class="com.example.mybank.bean.DataSource">
    <!-- <property name="url" value="some_url" /> -->
    <property name="url" value="${database.url}" />
    <!-- <property name="username" value="some_username" /> -->
    <property name="username" value="${database.username}" />
    <!-- <property name="password" value="some_password" /> -->
    <property name="password" value="${database.password}" />
    <!-- <property name="driverClass" value="some_driverClass" /> -->
    <property name="driverClass" value="${database.driverClass}" />
</bean>

<bean id="webServiceConfiguration" class="com.example.mybank.bean.WebServiceConfiguration">
    <!-- <property name="url" value="http://localhost:8080/webservice" /> -->
    <property name="url" value="${webservice.url}" />
</bean>
```

`PropertySourcesPlaceholderConfigurer` 可用的属性有：

|属性|类型|说明|
| ------| ------| ------------------------------------------------|
|`locations`|`List<String>`|加载的配置文档文件相对路径|
|`localOverride`|`boolean`|本地属性优先，即可以使用 `properties` 的配置覆盖配置文件|
|`properties`|`Properties`|当 `localOverride=true` 时允许使用该属性的配置覆盖配置文件的配置|

`context:property-placeholder`：`PropertySourcesPlaceholderConfigurer` 的一个快捷方式

`xmlns:context="http://www.springframework.org/schema/context"`  
`xsi:schemaLocation="http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd ..."`

```xml
<context:property-placeholder ignore-unresolvable="false" location="database.properties, web-service.properties" />
```

## `PropertyOverrideConfigurer`

`PropertyOverrideConfigurer`：作用类似 `PropertySourcesPlaceholderConfigurer`，是其易用版，区别有

* 不能用于构造函数注入，只能用于属性
* 允许使用默认值

`PropertyOverrideConfigurer` 根据配置键名自动替换对应 `bean` 的属性值。外部配置的属性名应为 `<bean-id>.<property-name>`

```properties
# database.properties
database.url=some_url
database.username=some_username
database.password=some_password
database.driverClass=some_driverClass
# web-service.properties
webServiceConfiguration.url=http://localhost:8080/webservice
```

设置配置文件的方法与 `PropertySourcesPlaceholderConfigurer` 类似，也是设置 `locations` 属性

```xml
<bean class="org.springframework.beans.factory.config.PropertyOverrideConfigurer">
    <property name="locations">
        <list>
            <value>database.properties</value>
            <value>web-service.properties</value>
        </list>
    </property>
</bean>
```

之后，只需要创建好 `bean` 对象即可

```xml
<bean id="datasource" class="com.example.mybank.bean.DataSource">
    <property name="url" value="test url property" />
    <property name="username" value="test username property" />
    <property name="password" value="test password property" />
    <property name="driverClass" value="test driverClass property" />
</bean>

<bean id="webServiceConfiguration" class="com.example.mybank.bean.WebServiceConfiguration">
    <property name="url" value="test url property" />
</bean>
```

`PropertyOverrideConfigurer` 会自动将配置文档中键为 `<bean-id>.<Property-name>` 的值替换 `XML` 中原本的配置。

`context:property-override`：`PropertyOverrideConfigurer` 的一个快捷方式

`xmlns:context="http://www.springframework.org/schema/context"`  
`xsi:schemaLocation="http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd ..."`

‍
