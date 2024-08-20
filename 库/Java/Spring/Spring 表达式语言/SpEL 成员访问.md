# SpEL 成员访问

# 成员访问 `.`

SpEL 支持使用 `.` 访问对象的方法（包括构造函数）、属性（`getXxx` 方法）、公共字段等

使用 `?.` 表示空安全的成员访问，若原本对象为 `null`，则返回 `null`

属性的首字母不分大小写，但实测是区分大小写的

允许使用 `new` 通过构造函数创建对象

```java
// String 的 bytes 属性，即 String#getBytes() 方法
exp = parser.parseExpression("'Hello World'.bytes");
assertArrayEquals("Hello World".getBytes(), exp.getValue(byte[].class));
// byte[] 的 length 公共字段
exp = parser.parseExpression("'Hello World'.bytes.length");
assertEquals("Hello World".getBytes().length, exp.getValue());
// String 的 length() 方法
exp = parser.parseExpression("'Hello World'.length()");
assertEquals("Hello World".length(), exp.getValue(int.class));
// String 的 length() 方法
exp = parser.parseExpression("'Hello World'.length()");
assertEquals("Hello World".length(), exp.getValue(int.class));
// String 的 charAt() 方法
exp = parser.parseExpression("'Hello World'.charAt(3)");
assertEquals("Hello World".charAt(3), exp.getValue(char.class));
```

使用 `{}` 可以内联创建列表（`java.util.Collections$UnmodifiableRandomAccessList`），创建的列表可以转换为数组

```java
exp = parser.parseExpression("{1,2,3,4,5}");
System.out.println(exp.getValueType());
assertEquals(List.of(1,2,3,4,5), exp.getValue(List.class));
assertArrayEquals(new int[]{1, 2, 3, 4, 5}, exp.getValue(int[].class));
```

可以给 SpEL 配置一个<span data-type="text" parent-style="color: var(--b3-card-success-color);background-color: var(--b3-card-success-background);">上下文</span>，上下文可以设置一个根对象，这样对根对象的调用可以省略。

# 索引 `[]`

对于 Properties、List、Map 等，可直接使用索引器 `[]` 访问数据

Spring 上下文内置 `systemProperties` 属性获取系统变量和虚拟机参数

```java
int[] array = new int[]{0, 1, 2, 3, 4, 5};
exp = parser.parseExpression("[3]");
assertEquals(3, exp.getValue(array));

List<String> list = List.of("a", "b", "c", "d", "e");
exp = parser.parseExpression("[3]");
assertEquals("d", exp.getValue(list));

Map<String, String> map = Map.of("a", "A", "b", "B", "c", "C", "d", "D");
exp = parser.parseExpression("['d']");
assertEquals("D", exp.getValue(map));

Properties properties = new Properties();
properties.put("a", "A");
properties.put("b", "B");
properties.put("c", "C");
exp = parser.parseExpression("['c']");
assertEquals("C", exp.getValue(properties));
```

可以使用 `T(类型)` <span data-type="text" parent-style="color: var(--b3-card-success-color);background-color: var(--b3-card-success-background);">引用</span>一个类型，用于访问静态成员、进行 `instanceof` 运算等
