# util 模式

`xmlns:util="http://www.springframework.org/schema/util"`

`xsi:schemaLocation="... http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util.xsd"`

`util` 模式可用于简化配置 `bean`，用于直接在容器中直接创建对应对象

|元素|描述|
| ---------------------| -------------------------------|
|<list>|创建一个 `java.util.List` 作为一个 `bean`|
|<map>|创建一个 `java.util.Map` 作为一个 `bean`|
|<set>|创建一个 `java.util.Set` 作为一个 `bean`|
|<constant>|将一个 `public static` 字段暴露为一个 `bean`|
|<property-path>|将一个 `beanmy-notebooks` 的属性暴露为一个 `bean`|
|<properties>|创建一个 `java.util.Properties` 作为一个 `bean`|

通用属性：

|属性|说明|
| ------| -----------------------|
|`scope`|暴露 `bean` 范围，默认 `singleton`|

`util` 模式是 FactoryBean 的快捷方式

|元素|FactoryBean 类型|
| ------| ------------------|
|`<list>`|`ListFactoryBean`|
|`<map>`|`MapFactoryBean`|
|`<set>`|`SetFactoryBean`|
|`<constant>`|`FieldRetrievingFactoryBean`|
|`<property-path>`|`PropertyPathFactoryBean`|
|`<properties>`|`PropertiesFactoryBean`|

‍
