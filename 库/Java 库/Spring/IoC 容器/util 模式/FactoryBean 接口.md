# FactoryBean 接口

```java
public interface FactoryBean<T> {
    @Nullable T getObject() throws Exception;
    @Nullable Class<?> getObjectType();
    default boolean isSingleton() { return true; }
}
```

相关方法说明：

|方法|说明|
| ------| ------------------------|
|`getObject`|由该 `FactoryBean` 管理的对象|
|`getObjectType`|由该 `FactoryBean` 管理的对象类型|
|`isSingleton`|是否是 `singleton` 范围|

实现了 `FactoryBean` 接口的类可以像普通 `bean` 一样在 XML 中配置，但在解析时，通过 `<bean-id>` 获取的是通过 `getObject` 创建的值，通过 `&<bean-id>` 获取的是 `FactoryBean` 对象。

在 XML 中，使用 `&amp;` 代替 `&`（即 `ref="&amp;<bean-id>"`）

```java
public class ExampleFactoryBean implements FactoryBean<String> {

    @Override
    public String getObject() {
        return "hello world";
    }

    @Override
    public Class<?> getObjectType() {
        return String.class;
    }

    @Override
    public boolean equals(Object obj) {
        return getClass() == obj.getClass();
    }
}
```

```xml
<bean id="example-factory" class="com.example.mybank.bean.ExampleFactoryBean" />
```

通过 `example-factory` 获取的是 `getObject` 的返回值 `hello world`

```java
assertEquals("hello world", context.getBean("example-factory"));
```

通过 `&example-factory` 获取的是 `ExampleFactoryBean` 实例

```java
assertEquals(new ExampleFactoryBean(), context.getBean("&example-factory"));
```

‍
