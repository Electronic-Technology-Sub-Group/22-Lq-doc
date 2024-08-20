# bean 自定义初始化

注意：注意：Java9 后，JSR250 不再是 JavaSE 标准的一部分，因此需要额外依赖自定义的初始化和销毁方法不能有任何参数，但可以抛出异常。

自定义初始化逻辑将在容器设置完所有属性后调用，实现自定义 `bean` 初始化的方法有三种：

* 在 `XML` 配置的 `<bean>` 标签中，通过 `init-method` 指定自定义初始化和清理逻辑的函数

  ```xml
  <bean id="fixedDepositDao" ...
        init-method="initializeDbConnection" />
  ```

  ```java
  package com.example.mybank.dao;

  public class FixedDepositJdbcDao extends FixedDepositDao {

      private DatabaseConnection connection;

      public void initializeDbConnection() {
          System.out.println("FixedDepositJdbcDao: Initializing database connection");
          connection = DatabaseConnection.getInstance();
      }

      // ...
  }

  ```
* 在 `<beans>` 标签的 `default-init-method` 属性中设置所有 `bean` 的默认初始化函数，但该函数会被具体 `bean` 的 `init-method` 覆盖
* 在 bean 类中实现 `InitializingBean` 接口的对应函数
* 在 bean 类中使用 `JSR250` 的 `@PostConstruct` 注解标注对应函数

  注意：Java9 后，JSR250 不再是 JavaSE 标准的一部分，，但 Spring 已自动引入，无需额外依赖

  要使 JSR250 生效，需要添加 `org.springframework.context.annotation.CommonAnnotationBeanPostProcessor`，详见 <span data-type="text" parent-style="color: var(--b3-card-info-color);background-color: var(--b3-card-info-background);">PostProcessor</span>

‍
