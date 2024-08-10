# 注入 Bean

`@Bean` 表示声明一个 `bean`，相当于一个 `<bean>` 标签。如果存在多个相同的 `bean`，在后面 `register` 的容器配置类中的 `bean` 会覆盖前面的

|`@Bean` 属性|等效 `<bean>` 属性|说明|
| -----------| --------------| -----------------------------------------------------------------|
|`value`，`name`|`id`|如果留空，使用方法名|
|`autowireCandidate`|`autowire-candidate`|<br />|
|`initMethod`|`init-method`|<br />|
|`destroyMethod`|`destroy-method`|如果一个 bean 定义了一个公有的 `close()` 或 `shutdown()`，则将其作为默认销毁方法|

|若要取消该默认的 `close()` 和 `shutdown()` 销毁方法，使用 `destroyMethod=""` 即可

其他属性则由额外的注解实现：`@Scope`，`@DependsOn`，`@Lazy`，`@Primary`，`@Autowired`，`@Value`

`@Bean` 注解还可以在被 `@Component` 或 `@Named` 注解的类中的方法上使用。

> 获取 `@Bean` 创建的 `bean` 的方法有三种：
>
> * 通过 `ApplicationContext` 的 `getBean()` 和显式调用返回依赖项的 `@Bean` 方法
> * 将依赖项指定为 `@Bean` 方法的参数，Spring 负责调用与依赖项相对应的 `@Bean` 方法并作为参数提供
>
>   ```java
>   @Bean("accountStatementDao")
>   public AccountStatementDao accountStatementDao() {}
>
>   @Bean("accountStatementService")
>   public AccountStatementService accountStatementService(AccountStatementDao accountStatementDao) {}
>   ```
> * 在 `bean` 类使用 `@Autowired`，`@Inject`，`@Resource` 注解注入

# 注入 BeanPostProcessors 与 BeanFactoryPostProcessors

使用注解配置容器，不需要再注入 `CommonAnnotationBeanPostProcessor`，<span data-type="text" parent-style="color: var(--b3-card-success-color);background-color: var(--b3-card-success-background);">JSR250 </span>也会生效

使用注解配置容器，不需要再注入 `MethodValidationPostProcessor`，<span data-type="text" parent-style="color: var(--b3-card-success-color);background-color: var(--b3-card-success-background);">JSR380</span> 也会生效

`BeanPostProcessors` 和 `BeanFactoryPostProcessors` 可以直接以 `@Bean` 的方式注入，但需要用 `static` 函数

```java
@Bean
public static ApplicationConfigurer applicationConfigurer() {
    ApplicationConfigurer configurer = new ApplicationConfigurer();
    configurer.setOrder(0);
    return configurer;
}
```

# 条件注入

可以通过 `@Conditional` 自定义注入条件。该注解也可以作为元注解。

# 函数式注册

可以直接使用 `context.registerBean(name, ...)` 注册 `bean`，使用 `context.registerAlias(name, ...)` 注册 `bean` 别名

函数式注册后，应使用 `refresh()` 方法刷新容器
