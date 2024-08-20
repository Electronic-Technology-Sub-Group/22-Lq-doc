# bean 自定义销毁

自定义清理逻辑将在容器被销毁之前执行，实现自定义 `bean` 销毁方法有三种：

* 在 `XML` 配置的 `<bean>` 标签中，通过 `destroy-method` 指定自定义初始化和清理逻辑的函数
* 在 `<beans>` 标签的 `default-destroy-method` 属性中设置所有 `bean` 的默认初始化函数，但该函数会被具体 `bean` 的 `destroy-method` 覆盖
* 实现 `DisposableBean` 接口的对应函数
* 使用 `JSR250` 的 `@PreDestroy` 注解标注对应函数

  注意：Java9 后，JSR250 不再是 JavaSE 标准的一部分，但 Spring 已自动引入，无需额外依赖

  要使 JSR250 生效，需要添加 `org.springframework.context.annotation.CommonAnnotationBeanPostProcessor`，详见 <span data-type="text" parent-style="color: var(--b3-card-info-color);background-color: var(--b3-card-info-background);">PostProcessor</span>

注意：要启用自定义销毁函数，需要向 JVM 注册一个关闭钩子。有两种方法：

* 在程序初始化时调用 `AbstractApplicationContext#registerShutdownHook()` 方法。包括 `ClassPathXmlApplicationContext` 的大多数 Spring 内置 `ApplicationContext` 都实现了该方法。
* 在程序结束时调用 `ConfigurableApplicationContext` 的 `close()` 方法。

注意：销毁方法对 `prototype` 作用域的 bean 无效，应由 bean 的获取者进行销毁操作
