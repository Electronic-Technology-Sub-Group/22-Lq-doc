# 发送 JMS 信息

首先，需要定义 JMS 通道。`amq:queue` 标签创建一个 JMS 队列，`physicalName` 表示在 ActiveMQ 中的名称。当然，也可以在运行时<span data-type="text">📦</span>手动创建队列。

`id` 参数是队列对应 `ActiveMQQueue` bean 对象的 `id`，`physicalName` 才是真正 JMS 队列的名称。后面用到的基本都是 `physicalName`

```xml
<beans ...
       xmlns:amq="http://activemq.apache.org/schema/core"
       xsi:schemaLocation="...
                           http://activemq.apache.org/schema/core http://activemq.apache.org/schema/core/activemq-core.xsd">

    <amq:queue id="fixedDepositDestination" physicalName="aQueueDestination" />
    <amq:queue id="emailQueueDestination" physicalName="emailQueueDestination" />
</beans>
```

> Java 配置
>
> ```java
> @Bean
> public ActiveMQQueue fixedDepositDestination() {
>     return new ActiveMQQueue("aQueueDestination");
> }
> @Bean
> public ActiveMQQueue emailMessageDestination() {
>     return new ActiveMQQueue("emailQueueDestination");
> }
> ```

`JmsTemplate` 和 `JmsTransactionManager` 是一个用于信息的事务管理器，`defaultDestination` 为默认的 JMS 队列

```xml
<beans ...
       xmlns:amq="http://activemq.apache.org/schema/core"
       xsi:schemaLocation="...
                           http://activemq.apache.org/schema/core http://activemq.apache.org/schema/core/activemq-core.xsd">

    <bean id="jmsTemplate" class="org.springframework.jms.core.JmsTemplate">
        <property name="connectionFactory" ref="jmsFactory" />
        <property name="defaultDestination" ref="fixedDepositDestination" />
    </bean>

    <bean id="jmsTxManager" class="org.springframework.jms.connection.JmsTransactionManager">
        <property name="connectionFactory" ref="cachingConnectionFactory"/>
    </bean>

</beans>
```

> Java 配置
>
> ```java
> @Bean
> public JmsTemplate jmsTemplate(ActiveMQConnectionFactory connectionFactory, @Qualifier("fixedDepositDestination") ActiveMQQueue defaultQueue) {
>     JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
>     jmsTemplate.setDefaultDestination(defaultQueue);
>     return jmsTemplate;
> }
> @Bean
> public JmsTransactionManager jmsTxManager(CachingConnectionFactory connectionFactory) {
>     return new JmsTransactionManager(connectionFactory);
> }
> ```

Spring `JmsTemplate` 简化同步和发送 JMS 信息的方式，提供一个统一抽象层，不需要处理底层 API

* 使用 `send` 方法手动构建 `Message` 对象
* 使用 `convertAndSend` 使用内置消息类型转换将 `String`、`byte[]`、`Map` 对象转换为 `Message` 对象

```java
public class FixedDepositServiceImpl extends ServiceTemplate implements FixedDepositService {

    @Resource(name = "fixedDepositDao")
    private FixedDepositDao fixedDepositDao;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Transactional("jmsTxManager")
    public int createFixedDeposit(FixedDepositDetails fixedDepositDetails) {
        jmsTemplate.send("emailQueueDestination", session -> {
            TextMessage message = session.createTextMessage();
            message.setText(fixedDepositDetails.getEmail());
            return message;
        });

        int result = fixedDepositDao.createFixedDetail(fixedDepositDetails);

        jmsTemplate.send("aQueueDestination", session -> {
            ObjectMessage message = session.createObjectMessage();
            message.setObject(fixedDepositDetails);
            return message;
        });

        return result;
    }
}
```

`createFixedDeposit` 指定事务管理器为 `jmsTxManager`。通过 `JmsTemplate` 向 `emailQueueDestination` 和 `aQueueDestination` 两个队列发送消息，不需要显式创建 `Connection` 和 `Session`。

如果需要使用编程式事务管理，可以使用 `TransactionTemplate`，并将 `transactionManager` 属性设置为 `jmsTxManager`

```xml
<bean id="transactionTemplate" class="org.springframework.transaction.support.TransactionTemplate">
    <property name="transactionManager" ref="jmsTxManager" />
</bean>
```

```java
@Autowired
private TransactionTemplate transactionTemplate;

public int createFixedDeposit(FixedDepositDetails fixedDepositDetails) {
    return transactionTemplate.execute(status -> {
        jmsTemplate.send("emailQueueDestination", session -> {
            TextMessage message = session.createTextMessage();
            message.setText(fixedDepositDetails.getEmail());
            return message;
        });
        int result = fixedDepositDao.createFixedDetail(fixedDepositDetails);
        jmsTemplate.send("aQueueDestination", session -> {
            ObjectMessage message = session.createObjectMessage();
            message.setObject(fixedDepositDetails);
            return message;
        });
        return result;
    });
}
```
