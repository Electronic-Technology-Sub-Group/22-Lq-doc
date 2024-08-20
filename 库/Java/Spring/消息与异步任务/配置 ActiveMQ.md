# 配置 ActiveMQ

配置 ActiveMQ，以内嵌模式运行：

依赖（Spring）：`org.springframework.boot:spring-boot-starter-activemq`

依赖（ActiveMQ）：`org.apache.activemq:activemq-all`，若产生 Log 冲突可以单独导入 `broker`，`client`，`spring`，`kahadb-store` 四个包

```xml
<beans ...
       xmlns:amq="http://activemq.apache.org/schema/core"
       xsi:schemaLocation="...
                           http://activemq.apache.org/schema/core http://activemq.apache.org/schema/core/activemq-core.xsd">

    <!-- 开启内嵌模式：ActiveMQ 代理与应用程序运行于同一个 JVM 中 -->
    <!-- 如果使用其他 ActiveMQ 实例，不需要 amq:broker 并设置 brokerUrl -->
    <amq:broker>
        <amq:transportConnectors>
            <amq:transportConnector uri="tcp://localhost:61616" />
        </amq:transportConnectors>
    </amq:broker>

    <amq:connectionFactory brokerURL="vm://localhost" id="jmsFactory">
        <amq:trustedPackages>
            <value>com.example.mybank</value>
            <value>java.util</value>
        </amq:trustedPackages>
    </amq:connectionFactory>

    <bean id="cachingConnectionFactory" class="org.springframework.jms.connection.CachingConnectionFactory">
        <property name="targetConnectionFactory" ref="jmsFactory"/>
    </bean>
</beans>
```

1. `amq:broker` 标签用于声明一个 ActiveMQ 代理
2. 创建 JMS ConnectionFactory 实例，用于创建与 ActiveMQ 的连接

    * `broker` 指定 ActiveMQ 代理 URL，`vm:localhost` 指定当前 VM 虚拟机。
    * `amq:trustedPackages` 定义了 ActiveMQ 信任包，即 ActiveMQ 仅能交换信任包中的对象。由于 `BankAccountDetails` 用到了 `java.util.Date` 类，因此还要信任 `java.util` 包。
3. `CachingConnectionFactory` 是一个 JMS ConnectionFactory 的适配器，提供缓存 JMS Session，MessageProducer 和 MessageConsumer

> Java 配置：
>
> * 需要 `@EnableJms` 开启 JMS
>
>   ```java
>   @Configuration
>   @EnableJms
>   public class JmsConfig {
>
>       @Bean
>       public ActiveMQConnectionFactory jmsFactory(@Qualifier("activemqProperties") Properties properties) {
>           ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
>           factory.setBrokerURL(properties.getProperty("url"));
>           factory.setTrustedPackages(Arrays.stream(properties.getProperty("trustedPackages").split(",")).toList());
>           if (properties.containsKey("username")) {
>               factory.setUserName(properties.getProperty("username"));
>           }
>           if (properties.containsKey("password")) {
>               factory.setPassword(properties.getProperty("password"));
>           }
>           return factory;
>       }
>
>       @Bean
>       public CachingConnectionFactory cachingConnectionFactory(ActiveMQConnectionFactory connectionFactory) {
>           return new CachingConnectionFactory(connectionFactory);
>       }
>   }
>   ```
> * 内嵌模式
>
>   ```java
>   @Bean
>   public XBeanBrokerService activeMQBroker() throws Exception {
>       XBeanBrokerService broker = new XBeanBrokerService();
>       TransportConnector connector = new TransportConnector();
>       connector.setUri(URI.create("tcp://localhost:61617"));
>       broker.setTransportConnectors(List.of(connector));
>       return broker;
>   }
>   ```

‍
