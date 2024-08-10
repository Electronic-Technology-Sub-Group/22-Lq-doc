# 使用 BeanPostProcessor 处理 bean

`BeanPostProcessor` 用于在新建的 bean 实例的初始化之前或之后与其进行交互，执行自定义逻辑，直接在 `XML` 中创建其实例 `bean` 即可生效

`BeanPostProcessor` 不会检查其他 `BeanPostProcessor` 类型的 bean 对象

当存在多个 `BeanPostProcessor` 时，为每个 `BeanPostProcessor` 实现 `Order` 接口设置其执行过程

```java
public interface BeanPostProcessor {
	@Nullable
	default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}
	@Nullable
	default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}
}
```

* `postProcessBeforeInitialization`：在 `bean` 初始化方法被调用之前触发
* `postProcessAfterInitialization`：在 `bean` 初始化方法被调用之后触发

对于使用 `FactoryBean` 接口创建的对象，由于其创建的对象是间接的，无论 `isSingleton()` 的返回值是什么， `FactoryBean` 的实现类是 `singleton` 的，`BeanPostProcessor` 和其他生命周期接口调用有以下特点：

* 对于 `FactoryBean` 的实现类，Spring 同时调用 `Before` 和 `After` 方法
* 对于 `FactoryBean#getObject` 创建的对象，只会调用 `After` 方法
* 对于类似 `InitializingBean` 的生命周期接口，最多只会调用一次，针对 `FactoryBean` 对象而不是 `getObject`

此时，可以使用 `BeanFactoryPostProcessor`。该接口可以获取所有 `bean` 的信息。

|方法|说明|
| ------| --------------------------|
|`getBeanDefinitionNames`|获取所有 `bean` 对象的 id|
|`getBeanDefinition`|根据名称获取某个 `bean` 对象|

|类|说明|
| ------| ------------------|
|`BeanDefinition`|表示一个 `bean` 定义|

‍
