# 语法
## switch 表达式增强
#实验性功能 

Switch 表达式可判断对象类型了

```java
static String formatterPatternSwitch(Object o) {
    return switch (o) {
        case null      -> "null"
        case Integer i -> String.format("int %d", i);
        case Long l    -> String.format("long %d", l);
        case Double d  -> String.format("double %f", d);
        case String s  -> String.format("String %s", s);
        default        -> o.toString();
    };
}
```
# API
## 伪随机发生器

为所有伪随机数提供统一 API：`RandomGenerator`

```java
// 选择伪随机发生器类型  
RandomGeneratorFactory<RandomGenerator> rf = RandomGeneratorFactory.of("L128X256MixRandom");  
// 创建伪随机发生器  
RandomGenerator random = rf.create(System.currentTimeMillis());
```
## sealed classes

密封类：指定类或接口仅能由指定子类继承，其实现的子类必须为 `final`，`sealed` 或 `non-sealed` 修饰；`record` 由于隐含 `final` 也可

```java
/**
 * SealedClass 仅有三种子类 SC1, SC2, SC3
 */
public sealed class SealedClass permits SC1, SC2, SC3 {
}

final class SC1 extends SealedClass {
}

sealed class SC2 extends SealedClass {
}

/**
 * 允许其他子类继承自 SC3
 */
non-sealed class SC3 extends SealedClass {
}
```
## 外部函数和内存 API
#实验性功能 

一系列用于访问堆外内存的 API，仍在孵化中，目前包括

- JEP 370：Foreign-Memory Access API
- JEP 383：Foreign-Memory Access API（2nd incubator）
- JEP 393：Foreign Linker API
- JEP 412：Foreign Function & Memory API
## 其他 API

- 移除 Appplet API
# 其他

- 移除 AOT 及 JIT Compiler，保留 JVMCI
- 废弃 Security Manager