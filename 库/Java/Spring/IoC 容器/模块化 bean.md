# 模块化 bean

Spring 允许使用 `<import>` 标签在一个 XML 配置文件中引入其他 XML 配置，将应用程序配置进行模块化或结构化。

```xml
<beans ...>
  <import resource="xml 文件" />
</beans>
```

在不同 `XML` 文件中定义的 `bean` 存在依赖关系时在应用程序启动时由 Spring 解析。
