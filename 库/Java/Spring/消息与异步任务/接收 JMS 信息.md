# 接收 JMS 信息

既可以使用 `JmsTemplate` 同步接收 JMS 消息，也可以通过 Spring 消息侦听器容器进行异步接收。

# 同步接收

`JmsTemplate` 有各种 `receive` 方法，可以阻塞接收 JMS 消息。`setReceiveTimeout` 可以指定一个接收超时限制。

# 异步接收

使用 `<jms:listener-container>` 注册 `JmsListenerContainerFactory` 实例，指向一个 `MessageListener` 接口实现类 `bean` 对象，当收到对应消息时由 Spring 负责调用接收。

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:jms="http://www.springframework.org/schema/jms"
       xsi:schemaLocation="...
                           http://www.springframework.org/schema/jms http://www.springframework.org/schema/jms/spring-jms.xsd">

    <bean id="fixedDepositsMessageListener" class="com.example.mybank.jms.FixedDepositMessageListener" />
    <bean id="emailMessageListener" class="com.example.mybank.jms.EmailMessageListener" />

    <jms:listener-container connection-factory="cachingConnectionFactory" destination-type="queue" transaction-manager="jmsTxManager">
        <jms:listener destination="aQueueDestination" ref="fixedDepositsMessageListener" />
        <jms:listener destination="emailQueueDestination" ref="emailMessageListener" />
    </jms:listener-container>
</beans>
```

* 使用 `jms:listener-container` 注册 Container，使用 `jms:listener` 注册 Listener
* `jms:listener` 指向对象应实现 `jakarta.jms.ObjectMessage` 接口

> Java 配置：通过 `@JMSConnectionFactory`，或使用 `@JmsListener` 的 `containerFactory` 属性设置 `JmsListenerContainerFactory`
>
> ```java
> @Bean
> public JmsListenerContainerFactory<?> jmsListenerContainerFactory(CachingConnectionFactory connectionFactory, JmsTransactionManager jmsTxManager) {
>     DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
>     factory.setConnectionFactory(connectionFactory);
>     factory.setTransactionManager(jmsTxManager);
>     return factory;
> }
>
> @Component
> @JMSConnectionFactory("jmsListenerContainerFactory")
> public class MyBankJmsListeners {
>
>     private static final Logger LOGGER = LogManager.getLogger();
>
>     @JmsListener(id = "fixedDepositsMessageListener", destination = "aQueueDestination")
>     public void onReceiveFixedDepositDetails(Message message) {
>         ObjectMessage objectMessage = (ObjectMessage) message;
>         try {
>             FixedDepositDetails fdDetails = (FixedDepositDetails) objectMessage.getObject();
>             LOGGER.warn("Received Fixed Deposit Details : {}", fdDetails);
>         } catch (JMSException e) {
>             throw new RuntimeException(e);
>         }
>     }
>
>     @JmsListener(id = "emailMessageListener", destination = "emailQueueDestination")
>     public void onReceiveMessage(Message message) {
>         TextMessage textMessage = (TextMessage) message;
>         try {
>             String email = textMessage.getText();
>             LOGGER.warn("Received Email: {}", email);
>         } catch (JMSException e) {
>             throw new RuntimeException(e);
>         }
>     }
> }
> ```
