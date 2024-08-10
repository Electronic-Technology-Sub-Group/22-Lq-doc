# 使用 Hibernate 框架

Spring ORM 模块提供与 Hibernate，Java Persistence API（JPA），Java 数据对象（JDO）的集成。

Hibernate 是一个 JPA 提供程序，可以使用 JPA 注解将持久实体映射到数据库表。

依赖：`org.hibernate.orm:hibernate-core`

依赖：`org.springframework:spring-orm`

1. 配置持久类，将持久类使用 JPA 的 `@Entity` 等注解配置好
2. 配置 `SessionFactory`：创建 Hibernate 的 Session 对象的工厂

    在声明的 `sessionFactory` 变量中，`dataSource` 属性指定 `DataSource` 对象，之后使用 `packageToScan` 指定一个包用于自动扫描，或使用 `annotatedClasses` 手动指定持久类。

    ```xml
    <bean id="sessionFactory" class="org.springframework.orm.hibernate5.LocalSessionFactoryBean">
        <property name="dataSource" ref="dataSource" />
        <property name="packagesToScan" value="com.example.mybank.domain" />
        <property name="annotatedClasses">
            <list>
                <value>com.example.mybank.domain.BankAccountDetails</value>
                <value>com.example.mybank.domain.FixedDepositDetails</value>
            </list>
        </property>
    </bean>
    ```
3. 创建使用 `Hibernate API` 交互的 Dao

    ```java
    @Autowired
    private SessionFactory sessionFactory;

    public FixedDepositDetails getFixedDeposit(int id) {
        String hql = "from FixedDepositDetails where FixedDepositDetails.id=" + id;
        return sessionFactory.getCurrentSession().createQuery(hql, FixedDepositDetails.class).uniqueResult();
    }

    public int createFixedDetail(FixedDepositDetails fixedDepositDetails) {
        sessionFactory.getCurrentSession().persist(fixedDepositDetails);
        return fixedDepositDetails.getId();
    }
    ```

‍
