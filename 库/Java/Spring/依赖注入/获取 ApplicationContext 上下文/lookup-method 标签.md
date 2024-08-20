# lookup-method 标签

在 `bean` 类中定义一个 `bean lookup` 方法，其返回类型表示一个 `bean`，`<lookup-method>` 指示 Spring 生成其实现

```java
public abstract class CustomerRequestServiceImpl implements CustomerRequestService {

    private final CustomerRequestDao customerRequestDao;

    @ConstructorProperties("consumerRequestDao")
    public CustomerRequestServiceImpl(CustomerRequestDao customerRequestDao) {
        this.customerRequestDao = customerRequestDao;
    }

    // 创建一个 abstract 方法，该方法相当于通过 context.getBean 获取一个 bean
    public abstract CustomerRequestDetails getCustomerDetails();
}
```

```xml
<bean id="customerRequestDetails" class="com.example.mybank.domain.CustomerRequestDetails" scope="prototype" />

<bean id="customerRequestService" class="com.example.mybank.service.CustomerRequestServiceImpl"
      c:customerRequestDao-ref="customerRequestDao">
    <!-- 声明 lookup-method 由 Spring 实现 -->
    <!-- 方法 getCustomerDetails 指向 bean id=customerRequestDetails -->
    <lookup-method bean="customerRequestDetails" name="getCustomerDetails" />
</bean>
```

‍
