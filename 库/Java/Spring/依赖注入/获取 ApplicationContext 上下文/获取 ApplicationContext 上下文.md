# 获取 ApplicationContext 上下文

我们使用 `bean` 对象中可能需要获取当前对象的 `ApplicationContext` 上下文，如在一个 `singleton` 对象中获取一个新的 `prototype` 对象

生命周期接口：定义一个或多个由 Spring 容器调用的回调方法，在 `bean` 生命周期内适当的时候被调用

方法注入：Spring 生成一个继承自指定类型的子类对象，并覆盖（或实现）其对应方法

Spring 使用 `GCLIB` 执行 `bean` 的子类化。自 Spring 3.2 开始，`GCLIB` 被打包在 `spring-core` 库中

* 使用生命周期接口： ApplicationContextAware 接口，但会使 `ApplicationContext` 与对应 `bean` 相耦合
* 使用方法注入： `bean` 模式的 <lookup-method> 和 <replaced-method> 元素

‍
