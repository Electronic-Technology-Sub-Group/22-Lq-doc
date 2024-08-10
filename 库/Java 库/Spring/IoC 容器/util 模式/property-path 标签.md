# property-path 标签

```xml
<util:list id="listType" list-class="java.util.LinkedList">
    <value>A simple String value in list</value>
    <value>Another simple String value in list</value>
</util:list>

<util:property-path id="dataType" path="listType.class" />
```

属性：

|属性|说明|
| ------| ----------------------|
|`path`|指向的属性，格式：`<bean-id>.<bean-property>`|

‍
