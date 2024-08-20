# 使用 BeanPostProcessor 实现 InstanceValidator 接口 bean 的自查

这是一个自定义 BeanPostProcessor 的实现

```java
public class FixedDepositJdbcDao extends FixedDepositDao implements InstanceValidator {

    @Override
    public void validateInstance() {
        LOGGER.info("FixedDepositJdbcDao: Validating instance");
        if (connection == null) {
            LOGGER.error("FixedDepositJdbcDao: Failed to obtain DatabaseConnection instance.");
        }
    }
}

@Setter
@Getter
public class InstanceValidationBeanPostProcessor implements BeanPostProcessor, Ordered {

    private static final Logger LOGGER = LogManager.getLogger();

    private int order;

    public InstanceValidationBeanPostProcessor() {
        LOGGER.info("Create InstanceValidationBeanPostProcessor");
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) {
        LOGGER.info("PostProcessing bean {}", beanName);
        if (bean instanceof InstanceValidator validator) {
            LOGGER.info("Validating instance of {}", beanName);
            validator.validateInstance();
        }
        return bean;
    }
}
```

```xml
<bean class="com.example.mybank.postprocessor.InstanceValidationBeanPostProcessor" p:order="1" />
```

![[image-20240329081104-vztnglp.png]]
