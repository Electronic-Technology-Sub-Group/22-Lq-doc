# 基于构造器的 DI

基于构造器的注入包括构造函数和工厂函数。对于构造函数，使用 `<constructor-arg>` 子标签提供，使用 `index` 指定参数位置。

除根据位置注入参数外，还可以根据<span data-type="text" parent-style="color: var(--b3-card-success-color);background-color: var(--b3-card-success-background);">类型</span>或<span data-type="text" parent-style="color: var(--b3-card-success-color);background-color: var(--b3-card-success-background);">参数名</span>注入，这使得当修改构造函数参数顺序时，不必要过多修改 bean 类的配置文件

基于构造函数的 DI 与基于 `setter` 的 DI 可以组合使用

```xml
<bean id="personalBankingService" class="com.example.mybank.chapter02.PersonalBankingService">
    <constructor-arg index="0" ref="jmsMessageSender" />
    <constructor-arg index="1" ref="emailMessageSender" />
    <constructor-arg index="2" ref="webServiceInvoker" />
</bean>
```

使用工厂方法创建对象时，工厂方法的参数被认为是构造函数的参数

设我们有工厂类和工厂方法：

```java
CustomDateEditorpublic class FixedDepositDaoFactory {
    public FixedDepositDao getFixedDepositDao(String type) {
        return switch (type.toLowerCase(Locale.ROOT)) {
            case "jdbc" -> new FixedDepositJdbcDao();
            case "hibernate" -> new FixedDepositHibernateDao();
            case "ibatis" -> new FixedDepositIbatisDao();
            default -> new FixedDepositDao();
        };
    }
}
```

在 `bean` 中创建对应 `factory` 以及对应参数：

```xml
<bean id="daoFactory" 
      class="com.example.mybank.dao.FixedDepositDaoFactory" />

<bean id="databaseInfo" 
      class="com.example.mybank.DatabaseInfo" />

<!-- FixedDepositJdbcDao 中存在 setDatabaseInfo 方法 -->
<bean id="dao" 
      factory-bean="daoFactory" 
      factory-method="getFixedDepositDao">
    <constructor-arg index="0" value="jdbc" />
    <property name="databaseInfo" ref="databaseInfo" />
</bean>
```

‍
