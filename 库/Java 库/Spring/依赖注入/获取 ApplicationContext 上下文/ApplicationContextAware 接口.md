# ApplicationContextAware 接口

```java
public interface ApplicationContextAware extends Aware {
    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
}
```

`Spring` 在初始化时通过 `setApplicationContext` 方法传入 `ApplicationContext` 对象。

```java
public class ConsumerRequestServiceImpl implements ConsumerRequestService, ApplicationContextAware {

    private final ConsumerRequestDao consumerRequestDao;
    private final ConsumerRequestDetails consumerRequestDetails;
    private ApplicationContext applicationContext;

    @ConstructorProperties({"consumerRequestDao", "consumerRequestDetails"})
    public ConsumerRequestServiceImpl(ConsumerRequestDao consumerRequestDao, ConsumerRequestDetails consumerRequestDetails) {
        this.consumerRequestDao = consumerRequestDao;
        this.consumerRequestDetails = consumerRequestDetails;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
```

`ApplicationContextAware` 接口是一个生命周期接口。`setApplicationContext` 方法会在 `bean` 对象实例化后，初始化前调用。
