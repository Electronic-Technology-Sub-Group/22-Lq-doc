# Spring JDBC

开发一个基于 Spring JDBC 进行数据库交互的应用程序，需要执行以下两步操作：

1. 配置标识数据源的 `javax.sql.DataSource` 对象，有两种方法：

    * 使用 Apache Commons DBCP 2 或其他库提供的数据源

      ```xml
      <bean id="dataSource" class="org.apache.commons.dbcp2.BasicDataSource" destroy-method="close">
          <property name="url" value="${database.url}" />
          <property name="username" value="${database.username}" />
          <property name="password" value="${database.password}" />
          <property name="driverClassName" value="${database.driverClass}" />
      </bean>

      <context:property-placeholder location="config/datasource.properties" />
      ```
    * 使用 JNDI 查找：使用 `jee` 模式的 `<jndi-lookup>` 元素

      ```xml
      <beans ...
             xmlns:jee="http://www.springframework.org/schema/jee"
             xsi:schemaLocation="...
                                 http://www.springframework.org/schema/jee http://www.springframework.org/schema/jee/spring-jee.xsd">
          <jee:jndi-lookup jndi-name="java:comp/env/jdbc/bankAppDb" id="dataSource" />
      </beans>
      ```

      使用 Java 配置，使用 `JndiLocatorDelegate`。注意清除默认 `destroyMethod`，因为 JNDI 绑定的 DataSource 由应用程序服务器管理

      ```java
      @Bean(destroyMethod = "")
      public JdbcTemplate jdbcTemplate(DataSource dataSource) {
          JdbcTemplate template = new JdbcTemplate();
          template.setDataSource(dataSource);
          return template;
      }
      ```
2. 实现使用 Spring 的 JDBC 模块类进行交互的 Dao

    |数据库操作类|说明|
    | --------------------------------| ------------------------------------------------------------|
    |`javax.sql.DataSource`|JavaEE<sup>(现在叫 Jakarta)</sup> 中表示一个数据源|
    |JdbcTemplate|Spring JDBC 基础数据库操作类，负责管理和执行 SQL 语言|
    |NamedParameterJdbcTemplate|`JdbcTemplate` 的包装类，可以使用 `Map` 传递 SQL 中的参数<br />|
    |SimpleJdbcInsert|通过 `DataSource` 创建，简化 `insert` 语句|
    |`SimpleJdbcCall`|执行存储过程和函数|
    |MappingSqlQuery|以面向对象形式访问关系数据库，将每条 `ResultSet` 数据转换为一个对象|

‍
