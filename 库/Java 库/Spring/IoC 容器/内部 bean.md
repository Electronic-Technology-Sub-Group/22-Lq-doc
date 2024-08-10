# 内部 bean

如果一个 bean 只是作为某个特定 bean 的依赖使用，可以考虑将该 bean 声明为内部 bean，只需要在对应外部 bean 的 `property` 或 `constructor-arg` 标签中使用 `<bean>` 标签声明即可。

* 内部 `bean` 只能访问到其所在上下文，**无法访问**外部的其他 bean
* 内部 `bean` 本质是**匿名**的，指定了 `id` 属性也是无效的
* 内部 `bean` **总是** **`prototype`** **范围的**，指定了其他 `scope` 属性也是无效的

```xml
<bean id="fixedDepositDao" factory-bean="fixedDepositDaoFactory" factory-method="getFixedDepositDao"
      c:_0="jdbc">
    <property name="databaseInfo">
        <!-- 内部 bean -->
        <bean class="com.example.mybank.beans.DatabaseInfo" />
    </property>
</bean>
```

内部 `bean` 也同样可以使用 `util` 模式等其他创建 bean 的方法

```xml
<bean id="bankDetails" class="com.example.mybank.beans.BankDetails" ...>
    <property name="branchAddresses">
        <!-- 内部 bean -->
        <util:properties location="config/database.properties" />
    </property>
</bean>
```
