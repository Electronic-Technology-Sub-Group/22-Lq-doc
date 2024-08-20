# 注解声明 Spring bean

Spring 注解 `@Component` 相当于 XML 中的 `<bean>` 标签，可以用来声明一个 Spring bean。

`@Component` 注解用于修饰类，相当于 `<bean>` 的 `class` 属性。

`@Component` 还可以用于元注解，使其他自定义注解也具有声明 Spring bean 的能力。带有 `@Component` 功能的注解包括：`@Service`，`@Controller`，`@Repository` 等。

`@Component` 声明的 bean 名称（相当于 `id` 属性）默认为首字母小写的类名，可以通过传入 `value` 属性修改。

```java
@Component
public class EmailMessageSender {
}
```

```java
@Service("txService")
public class TxServiceImpl implements TxService {
}
```

除 `id` 外，其他属性也有对应的注解。比较简单的有：

|注解|XML 属性|说明|
| -------------------| -------------------| -------------------------------------------------------------------------------------|
|`@Scope`|`scope`|接收一个 String，其常量定义在 ConfigurableBeanFactory<sup>(SCOPE_SINGLETON、SCOPE_PROTOTYPE)</sup> 和 WebApplicationContext<sup>(与 Web 相关的作用域，包括 SCOPE_REQUEST，SCOPE_SESSION，SCOPE_APPLICATION)</sup> 中|
|`@Lazy`|`lazy-init`|`singleton` 作用域的 `bean` 延迟初始化；<br />与 `@Autowire` 等自动装配注解配合，延迟字段依赖项的自动装配<br />|
|`@DependsOn`|`depend-on`|指定依赖|
|`@Primary`|`primary`|优先自动装配|
