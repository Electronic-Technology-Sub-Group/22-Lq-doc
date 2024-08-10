# properties 标签

```xml
<util:properties id="propertiesType">
    <property name="branchAddress" ref="branchAddress">
</util:properties>
```

属性：

|属性|说明|
| ------| ----------------------------------------------------|
|`location`|可选，可以将 properties 内容放到一个外部 `.properties` 文件中|

```xml
<util:properties id="branchAddresses"
	location="classpath:META-INF/addresses.properties" />
```

```properties
x = Branch X's address
y = Branch Y's address
```

‍
