# 面向接口编程

依赖接口而非具体实现类，使依赖类与依赖项之间松耦合。
- 创建引用依赖接口
- 定义 `<bean>` 元素，注入具体实现类
# 工厂方法

Spring 允许通过工厂方法创建 `bean`

设我们有工厂类：

```java
public class FixedDepositDaoFactory {
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

然后在 `bean` 中创建对应 `factory` 以及对应参数：

```xml
<bean id="daoFactory" class="com.example.mybank.dao.FixedDepositDaoFactory" />

<bean id="databaseInfo" class="com.example.mybank.DatabaseInfo" />

<!-- FixedDepositJdbcDao 中存在 setDatabaseInfo 方法 -->
<bean id="dao" factory-bean="daoFactory" factory-method="getFixedDepositDao">
    <constructor-arg index="0" value="jdbc" />
    <property name="databaseInfo" ref="databaseInfo" />
</bean>
```
# 基于构造函数的 DI

基于构造函数的注入使用 `<constructor-arg>` 子标签提供，使用 `index` 指定参数位置。

```xml
<bean id="personalBankingService" class="com.example.mybank.chapter02.PersonalBankingService">
    <constructor-arg index="0" ref="jmsMessageSender" />
    <constructor-arg index="1" ref="emailMessageSender" />
    <constructor-arg index="2" ref="webServiceInvoker" />
</bean>
```

- 基于构造函数的 DI 与基于 `setter` 的 DI 可以组合使用
- 使用工厂方法创建对象时，工厂方法的参数被认为是构造函数的参数
# 注入数据类型

DI 注入的数据分为两种
- 使用 `ref` 引用的在 `IoC` 容器中配置注入的对象，此时 `ref` 的值为对象的 `id`
- 使用 `value` 属性表示某些数据的字面量
- 使用 `value` 子标签表示某些内置的数据，包括所有基本类型，`String`，`null`，`List`，`Set`，`Map` 等
# bean 作用域

通过 `scope` 属性指定对象作用域，默认为 `singleton`。常见的有：

| scope     | 说明                    |
| --------- | --------------------- |
| singleton | 默认，对象在当前容器内共享         |
| prototype | 每次从 IoC 请求对象时，创建一个新对象 |
| request   | （Web）单个 HTTP 请求       |
| session   | （Web）单个会话内            |
