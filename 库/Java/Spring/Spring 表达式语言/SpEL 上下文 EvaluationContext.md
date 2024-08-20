# SpEL 上下文 EvaluationContext

`EvaluationContext` 在计算 SpEL 表达式时，提供上下文的属性、方法、字段等，也可以实现类型转换。

除此之外，还负责反射、缓存等实现

`StandardEvaluationContext` 是 `EvaluationContext` 的一个实现。

`StandardEvaluationContext` 构造成本较高且自带缓存，应尽可能复用上下文

# 上下文

* `setRootObject`：设置根对象，根对象可以忽略对象名直接访问成员，也可以通过 `#root` 访问

  ```java
  // 设置上下文
  Calendar calendar = Calendar.getInstance();
  EvaluationContext context = new StandardEvaluationContext(calendar);

  Expression exp = parser.parseExpression("time");
  assertEquals(calendar.getTime(), exp.getValue(context));
  ```
* `setVariable`：注册上下文对象，使用 `#对象名` 访问
* `registerFunction`：注册上下文函数，使用 `#函数名` 访问
* 自定义 `ConstructorResolver`，`MethodResolver`，`PropertyAccessor` 扩展 SpEL 表达式

# 类型转换

默认情况下，SpEL 使用 Spring Core 的 `ConversionService` 服务进行类型转换。

* 内置常用转换，同时也保留了可扩展性
* 支持泛型感知

```java
Simple simple = new Simple();
simple.booleanList.add(true);
StandardEvaluationContext simpleContext = new StandardEvaluationContext(simple);
Expression exp = parser.parseExpression("booleanList[0]");
exp.setValue(simpleContext, false);
Assertions.assertFalse(simple.booleanList.get(0));
```

‍
