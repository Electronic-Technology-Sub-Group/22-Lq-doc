# Java 12
## Compact Number Formatting

针对不同地区格式化数字

```java
int value = 1_234_554_321;
System.out.println(NumberFormat.getCompactNumberInstance().format(value)); // 12亿
System.out.println(NumberFormat.getCompactNumberInstance(Locale.SIMPLIFIED_CHINESE, NumberFormat.Style.SHORT).format(value)); // 12亿
System.out.println(NumberFormat.getCompactNumberInstance(Locale.SIMPLIFIED_CHINESE, NumberFormat.Style.LONG).format(value)); // 12亿
System.out.println(NumberFormat.getCompactNumberInstance(Locale.TRADITIONAL_CHINESE, NumberFormat.Style.SHORT).format(value)); // 12億
System.out.println(NumberFormat.getCompactNumberInstance(Locale.ENGLISH, NumberFormat.Style.SHORT).format(value)); // 1B
```
## JVM 常量 API

引入 `java.lang.invoke.constant` 包，用于描述一系列基于值的符号引用类型，用于描述可加载常量

定义一系列符号引用类型 `ClassDesc`，`MethodTypeDesc`，`MethodHandleDesc`，`DynamicConstantDesc` 包含描述这些常量的 `nominal` 信息
## 其他 API

- `String.transform`, `String.indent`

  ```java
  // 69609650
  System.out.println("Hello".transform(String::hashCode));
  //    Hello
  System.out.println("Hello".indent(3));
  ```

- `Files.mismatch()`
- `Collectors.teeing()` 聚合两个收集器的结果
- `CompletionStage.exceptionallyAsync`, `CompletionStage.exceptionallyCompose`, `CompletionStage.exceptionallyComposeAsync`
# Java 14

[[Java 18-21 API 新特性#Foreign Function & Memory API]]
# Java 15

- CharSequence.isEmpty()
- TreeMap 增加以下方法：
	- `putIfAbsent()`
	- `compute()`，`computeIfAbsent()`，`computeIfPresent()`
	- `merge()`
- Nashorn JavaScript Engine 彻底移除
- 新增基于 EdDSA 的数字签名算法（Ed25519）

```Java
public void EdDSA(String msg) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {  
    KeyPairGenerator kpg = KeyPairGenerator.getInstance("Ed25519");  
    KeyPair kp = kpg.generateKeyPair();  
    Signature sig = Signature.getInstance("Ed25519");  
    sig.initSign(kp.getPrivate());  
    sig.update(msg.getBytes(StandardCharsets.UTF_8));  
    byte[] s = sig.sign();  
}
```

- 隐藏类：用于框架，不可被程序正常调用，只能用于反射
	- 通过 `Lookup#defineHiddenClass()` 创建并返回 `Lookup` 对象使用
	- `Class::getName` 返回的不是名称字符串
	- `Class::getCanonicalName` 返回 `null`
	- 所有字段不可被修改；类对象不可被 `instrumentation` 等修改
# Java 16
## Vector API

[[Java 18-21 API 新特性#Java 21 LTS#Vector API]]
# Java 17 LTS
## 伪随机发生器

为所有伪随机数提供统一 API：`RandomGenerator`

```java
// 选择伪随机发生器类型  
RandomGeneratorFactory<RandomGenerator> rf = RandomGeneratorFactory.of("L128X256MixRandom");  
// 创建伪随机发生器  
RandomGenerator random = rf.create(System.currentTimeMillis());
```
## 外部函数和内存 API

[[Java 18-21 API 新特性#Java 21 LTS#Foreign Function & Memory API]]
## 其他 API

- 移除 Appplet API
