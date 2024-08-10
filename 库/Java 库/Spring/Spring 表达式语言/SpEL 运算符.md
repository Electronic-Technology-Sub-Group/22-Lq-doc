# SpEL 运算符

# 关系运算符

除一般的关系运算符外，SpEL 还支持 `instanceof` 和 `matches`

* 支持字符串的大小比较，任何值总是大于 `null` 的

  ```java
  Expression exp = parser.parseExpression("'abc' < 'ad'");
  assertTrue(exp.getValue(boolean.class));
  // 任何值都大于 null 值
  exp = parser.parseExpression("100 > null");
  assertTrue(exp.getValue(boolean.class));
  ```
* 支持 `instanceof` 运算符进行类型检查

  ```java
  Expression exp = parser.parseExpression("'abc' instanceof T(int)");
  assertFalse(exp.getValue(boolean.class));

  exp = parser.parseExpression("'abc' instanceof T(String)");
  assertTrue(exp.getValue(boolean.class));
  ```
* 支持 `matches` 运算符进行正则匹配

  ```java
  Expression exp = parser.parseExpression("'5.00' matches '^-?\\d+(\\.\\d{2})?$'");
  assertTrue(exp.getValue(boolean.class));

  exp = parser.parseExpression("'5.0067' matches '^-?\\d+(\\.\\d{2})?$'");
  assertFalse(exp.getValue(boolean.class));
  ```

# 逻辑运算符

逻辑运算符使用 `&&`，`||`，`!`，也可以使用 `and`，`or`

`!` 运算符优先级低于 `&&`、`||`

# 算术运算符

* `+` 适用于数字、字符串、日期，`-` 适用于数字、日期，其余算术运算符适用于数字
* 支持取模 `%`、幂运算 `^`

# 赋值运算符

`=` 赋值运算符，也可以使用 `Expression` 对象的 `setValue` 方法

# 类型运算符

使用 `T(类名)` 可以获取指定类型的 `Class` 对象，也可以用来访问类静态成员

`String` 可以直接用，其他类需要使用全类名

```java
// T 运算结果可以直接作为 Class 使用
Expression exp = parser.parseExpression("T(String)");
assertEquals(String.class, exp.getValue());
// 也可以用于访问静态成员
exp = parser.parseExpression("T(Boolean).TRUE");
assertEquals(Boolean.TRUE, exp.getValue());
```

类型定位通过上下文 `TypeLocator` 实现，`StandardEvaluationContext` 内置 `StandardTypeLocator` 解析类。

# 三元运算符

SpEL 支持 `?:` 运算符和 `Elvis` 运算符

* `displayName = name != null ? name : 'Unknown'`
* `displayName = name ?: 'Unknown'`

# 上下文查找

使用 `#名称` 可以查找使用 `setVariable` 注册的变量和使用 `registerFunction` 注册的方法。

```java
// 访问变量
Calendar calendar = Calendar.getInstance();
EvaluationContext context = new StandardEvaluationContext();
context.setVariable("calendar", calendar);
Expression exp = parser.parseExpression("#calendar.time");
assertEquals(calendar.getTime(), exp.getValue(context));
// 访问方法
context = new StandardEvaluationContext();
// String SpringUtil#reverseString(String)
MethodHandle reverse = MethodHandles.lookup().findStatic(SpringUtil.class,
        "reverseString", MethodType.methodType(String.class, String.class));
context.registerFunction("reverse", reverse);
exp = parser.parseExpression("#reverse('abc')");
assertEquals("cba", exp.getValue(context));
```

`#root` 表示对根对象的引用

```java
Calendar calendar = Calendar.getInstance();
EvaluationContext context = new StandardEvaluationContext(calendar);
Expression exp = parser.parseExpression("#root.time");
assertEquals(calendar.getTime(), exp.getValue(context));
```

`#this` 表示对评估对象的引用，用于表示迭代访问的上下文

```java
// create an array of integers
List<Integer> primes = new ArrayList<Integer>();
primes.addAll(Arrays.asList(2,3,5,7,11,13,17));

// create parser and set variable 'primes' as the array of integers
ExpressionParser parser = new SpelExpressionParser();
StandardEvaluationContext context = new StandardEvaluationContext();
context.setVariable("primes",primes);

// all prime numbers > 10 from the list (using selection ?{...})
// evaluates to [11, 13, 17]
List<Integer> primesGreaterThanTen = (List<Integer>) parser
                                         .parseExpression("#primes.?[#this>10]")
                                         .getValue(context);
```

`@bean` 表示从在上下文中的 `BeanResolver` 查找 bean 类，通常为 SpringApplication 容器。

# 容器迭代

对于可迭代容器，SpEL 提供三种语法对其进行访问：

SpEL 迭代方式通过流式访问，类似于 Java 的 `Stream`

SpEL 迭代中对象引用到 `#this`，此时可以省略 `#this` 直接访问其成员

* 选择器 `selection`：`?[表达式]`、`^[表达式]`、`$[表达式]`

  * `?[表达式]` 相当于 `filter`
  * `^[表达式]` 相当于 `findFirst().filter`
  * `$[表达式]` 相当于 `findLast().filter`
* 映射器 `projection`：`![表达式]` 相当于 `map`
