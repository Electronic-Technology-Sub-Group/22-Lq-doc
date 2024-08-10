# 配置 Spring Data JPA

依赖：`org.springframework.boot:spring-boot-starter-data-jpa`

与其他 `bean` 类相似，Spring Data  JPA 直接声明 `@Bean` 类即可

`TransactionManager` 类型 bean 对象的名称必须与 `@EnableJpaRepositories(transactionManagerRef)` 的值相同，默认 `transactionManager`

```java
@Configuration
@EnableJpaRepositories(basePackages = "com.example.mybank.dao")
public class JpaDataProfileConfig {

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        // 配置 DataSource
        entityManagerFactory.setDataSource(dataSource);
        // 配置 Hibernate
        entityManagerFactory.setPackagesToScan("com.example.mybank.domain");
        entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        Properties properties = new Properties();
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.id.new_generator_mappings", "false");
        entityManagerFactory.setJpaProperties(properties);
        return entityManagerFactory;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public TransactionTemplate transactionTemplate(PlatformTransactionManager platformTransactionManager) {
        return new TransactionTemplate(platformTransactionManager);
    }
}
```

使用 Spring Boot 3 以后上面的代码都可以省略，只保留 `@EnableJpaRepositories(basePackages = "com.example.mybank.dao")` 就行了，其余都是默认创建  
Spring 默认 JPA 提供器就是 `hibernate`，`properties` 在 `application.properties` 中配置

```properties
# jpa
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.id.new-generator-mappings=false
```

* `@EnableJpaRepositories` 注解表示启用 Spring Data JPA

  |参数|说明|
  | ------| -------------------------------------------|
  |`basePackages`|指定实现了 `Repository` 接口的类所在包|
  |`repositoryImplementationPostfix`|查找实现了 `Repository` 接口的类的后缀，默认 `Impl`|
  |`transactionManagerRef`|事务管理的 `PlatformTransactionManager` bean 对象引用|
  |`queryLookupStrategy`|查询策略，即从方法名称解析 SQL 语句的方法|

  使用 XML 注册，则需要使用 `xmlns:jpa` 命名空间

  ```xml
  <beans ... 
         xmlns:jpa="http://www.springframework.org/schema/data/jpa"
         xsi:schemaLocation="http://www.springframework.org/schema/jpa http://www.springframework.org/schema/data/jpa/spring-jpa.xsd
                             ...">

      <jpa:repositories base-package="..." />
  </beans>
  ```
  使用 `jpa:repositories base-package` 指定注册的 `Repository` 实现可以通过 `ref="首字母小写的类名"` 引用
* `LocalContainerEntityManagerFactoryBean`：配置 JPA 的 `EntityManagerFactory`，使用 `setJpaVendorAdapter` 设置 JPA `PersistenceProvider` 实现，并使用 `setJpaProperties` 配置 JPA
