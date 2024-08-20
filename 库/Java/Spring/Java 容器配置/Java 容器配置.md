# Java 容器配置

可以使用 Java 注解替代 XML 配置 Spring 容器，其核心为 `@Configuration` 和 @Bean 注解。

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BankAppConfiguration {
  
    @Bean
    public FixedDepositService fixedDepositService() throws Exception {
        return new FixedDepositServiceImpl(Constants.EVENT_SENDER_PROPERTY_FILE_PATH);
    }
}
```

配置好的容器通过 `AnnotationConfigApplicationContext` 创建。

* 将所有被 `@Configuration` 注解的类作为参数传入 `AnnotationConfigApplicationContext` 中

  * 如果在 `@Component` 或 `@Named` 注解的类使用了 `@Bean`，则被注解类也应作为容器配置类传入
* 可以先创建 `AnnotationConfigApplicationContext` 再注入容器配置类，最后要通过 `refresh()` 刷新容器

  * 通过 `scan` 传入包名，由 Spring 扫描容器配置类
  * 通过 `register` 传入容器配置类
* 作为容器配置在构造函数传入的类也被初始化为 `bean`，因此可以通过 `getBean` 获取

```java
@SpringBootApplication
public class BankAppWithAnnotation {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(BankAppConfiguration.class);
        BankAppConfiguration configuration = context.getBean(BankAppConfiguration.class);
    }
}
```

`@Configuration` 声明了一个容器配置，相当于一个 `<beans>` 标签。该注解依赖于 GCLIB 库，由 GCLIB 负责子类化，因此不能将其声明为 `final` 的。

`@Configuration` 也受 `@Profile` 注解的影响

`<beans>` 的其他子标签通过对应的元素在其中配置：

* `<bean>`：通过 `@Bean` 修饰<span data-type="text">📦</span>注入方法
* `<component-scan>`：使用<span data-type="text">📦</span>路径扫描或组件索引
* `<import>`：<span data-type="text">📦</span>导入其他 Java 容器配置
* `<util:properties>`：泛指导入 `properties` 配置文件的方式，使用 @PropertySource 注解
