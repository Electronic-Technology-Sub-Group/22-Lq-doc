# WebBindingInitializer

由 `Controller` 类中 `@InitBinder` 注解的方法仅对该 `Controller` 生效，而 `WebBindingInitializer` 初始化对全局 `WebDataBinding` 初始化都生效。

要使用 `WebBindingInitializer` 需要自定义  `RequestMappingHandlerAdapter`，然后在 `RequestMappingHandlerAdapter` 中添加 `WebBindingInitializer`。

`ConfigurableWebBindingInitializer` 是一个 `WebBindingInitializer` 的实现类

```java
@Bean
public RequestMappingHandlerMapping handlerMapping() {
    return new RequestMappingHandlerMapping();
}

@Bean
public RequestMappingHandlerAdapter handlerAdapter(ConfigurableWebBindingInitializer myInitializer) {
    RequestMappingHandlerAdapter handlerAdapter = new RequestMappingHandlerAdapter();
    handlerAdapter.setWebBindingInitializer(myInitializer);
    return handlerAdapter;
}

@Bean
public ConfigurableWebBindingInitializer myInitializer() {
    ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
    initializer.setPropertyEditorRegistrar(registrar ->
            registrar.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd-MM-yyyy"), false)));
    return initializer;
}
```

‍
