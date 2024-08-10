# replaced-method 标签

可以使 Spring 重新实现 bean 对象的某个特定方法，使用 `<replaced-method>` 标签指定。

首先，创建 `bean` 类，下面的 `getMyBean` 方法即要重写的方法

```java
public class ReplacedMethodBean {
  
    public Object getMyBean(String beanName) {
        return null;
    }
}
```

然后，我们要生成重写该方法的代理类。该类应实现 `MethodReplacer` 接口。由于这也是一个 `bean` 类，我们也可以让他实现 `ApplicationContextAware` 接口。`reimplement` 的返回值即代理方法的返回值。

```java
@Setter
public class MyMethodReplacer implements MethodReplacer, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public Object reimplement(Object obj, Method method, Object[] args) throws Throwable {
        if ("getMyBean".equals(method.getName())) {
            return applicationContext.getBean((String) args[0]);
        }
        return null;
    }
}
```

最后，在 `XML` 中进行配置：

```xml
<bean id="myMethodReplacer" class="com.example.mybank.bean.MyMethodReplacer" />
<bean id="replacedMethod" class="com.example.mybank.bean.ReplacedMethodBean">
    <!-- 使用 myMethodReplacer 代理 getMyBean  -->
    <replaced-method name="getMyBean" replacer="myMethodReplacer" />
</bean>
```

除了根据名称选择 `MethodReplacer`，我们还可以通过参数选择函数重载

```xml
<bean id="replacedMethod" class="com.example.mybank.bean.ReplacedMethodBean">
    <!-- 选择重载: getMyBean(String, String) -->
    <replaced-method name="getMyBean" replacer="myMethodReplacer">
        <arg-type>java.lang.String</arg-type>
        <arg-type>java.lang.String</arg-type>
    </replaced-method>
</bean>
```
