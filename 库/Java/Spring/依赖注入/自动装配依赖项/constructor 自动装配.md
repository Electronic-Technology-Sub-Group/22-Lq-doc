# constructor 自动装配

```java
public class CustomerRegistrationServiceImpl implements CustomerRegistrationService {
    private CustomerRegistrationDetails customerRegistrationDetails;
    private CustomerRegistrationDao customerRegistrationDao;

    @ConstructorProperties({"customerRegistrationDetails", "customerRegistrationDao"})
    public CustomerRegistrationServiceImpl(CustomerRegistrationDetails customerRegistrationDetails, CustomerRegistrationDao customerRegistrationDao) {
        this.customerRegistrationDetails = customerRegistrationDetails;
        this.customerRegistrationDao = customerRegistrationDao;
    }
}
```

```xml
<bean id="customerRegistrationDetails" class="com.example.mybank.domain.CustomerRegistrationDetails" scope="prototype" />
<bean id="customerRegistrationDao" class="com.example.mybank.dao.CustomerRegistrationDaoImpl" />

<bean id="customerRegistrationService" class="com.example.mybank.service.CustomerRegistrationServiceImpl" autowire="constructor" />
```

注意：

* 若某个属性对应类型在 Context 中不存在对应类型的 bean，Spring 不会设置该属性，也不会产生异常
* 若某个属性对应类型在 Context 中存在多个 bean 实例，而其中有一个 bean 存在 `primary=true` 属性，则会使用该 bean 初始化
* 若某个属性对应类型在 Context 中存在多个 bean 实例，且没有 `primary=true` 或有多个该属性，Spring 会产生异常
