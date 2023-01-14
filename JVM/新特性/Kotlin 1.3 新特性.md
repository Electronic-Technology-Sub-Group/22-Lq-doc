# 协程

[[Kotlin 协程]]

# 契约
#实验性功能 

让一个函数能够以编译器理解的方式显式描述其行为，类似 `String?::isNullOrEmpty()` 都是由此实现的，包括
- 通过声明函数调用的结果与所传参数值之间的关系来改进智能转换分析

```kotlin
@OptIn(ExperimentalContracts::class)  
fun require(condition: Boolean) {  
    // 当函数正常返回时，编译器可知 condition 为 true  
    contract { returns() implies condition }  
  
    if (!condition) throw IllegalArgumentException()  
}  
  
fun foo(s: Any) {  
    require(s is String)  
    // 此处 s 可被智能转换成 String
}
```

- 在存在高阶函数的情况下改进变量初始化的分析

```kotlin
@OptIn(ExperimentalContracts::class)  
fun synchoronize(block: () -> Unit) {  
    // block 方法在该方法内调用且仅调用一次  
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }  
}  
  
fun foo(s: Any) {  
    val x: Int  
    // 块只调用一次，因此不会提示重复初始化  
    synchoronize {   
        x = 100  
    }  
}
```

# 内联类
#实验性功能 

有些时候我们需要对某些类型创建包装器，但会造成额外的内存开销和运行时开销，若被包装的数据时原生类型也会使大量编译器优化失效。

Kotlin 引入内联类用于包装类，在生成代码时，使用内联类的位置 Kotlin 会尽可能使用内部包含的元素去替换包装类，在不必要的情况下不会实际创建包装类对象。一般来说，只要将内联类用于另一种类型，他们就会被装箱（生成包装类对象）

内联类使用 `value` 声明，使用 `@JvmInline` 注解，构造函数有且只有一个对象表示内联的对象

```kotlin
@JvmInline  
value class Password(private val s: String)
```

内联类允许有属性和函数，以及 `init` 代码块，但属性不允许有 `field`，不允许有 `lateinit` 和代理属性

```kotlin
@JvmInline  
value class Name(private val s: String) {  

    init {  
        require(s.isNotBlank())  
    }  
    
    val length get() = s.length  
    
    fun greet() {  
        println("Hello $s")  
    }  
}
```

内联类允许实现接口，但不允许继承其他类，且内联类始终都是 `final` 的

涉及到函数重载时，由于内联类会被自动替换为原始类型，会在函数名后增加一个 hash 值；若需要与其他语言（Java 等）共同使用，应使用 `@JvmName` 为其重命名

```kotlin
@JvmInline  
value class Name(private val s: String)  
  
fun call(name: Name) {  
    println("Call name $name")  
}  
  
fun call(name: String) {  
    println("Call string $name")  
}  
  
@JvmName("callName")  
fun call2(name: Name) {  
    println("Call name $name")  
}  
  
fun call2(name: String) {  
    println("Call string $name")  
}
```

反编译生成 Java 代码后为

```java
public static final void call_75PUH38/* $FF was: call-75PUH38*/(@NotNull String name) {  
   Intrinsics.checkNotNullParameter(name, "name");  
   String var1 = "Call name " + Name.toString-impl(name);  
   System.out.println(var1);  
}  

public static final void call(@NotNull String name) {  
   Intrinsics.checkNotNullParameter(name, "name");  
   String var1 = "Call string " + name;  
   System.out.println(var1);  
}  

@JvmName(  
   name = "callName"  
)  
public static final void callName(@NotNull String name) {  
   Intrinsics.checkNotNullParameter(name, "name");  
   String var1 = "Call name " + Name.toString-impl(name);  
   System.out.println(var1);  
}  

public static final void call2(@NotNull String name) {  
   Intrinsics.checkNotNullParameter(name, "name");  
   String var1 = "Call string " + name;  
   System.out.println(var1);  
}
```

从某些方面来说，内联类很像类型别名，但内联类会创建新类型

# 无符号类型
#实验性功能

[无符号整型 · Kotlin 官方文档 中文版 (kotlincn.net)](https://book.kotlincn.net/text/unsigned-integer-types.html)

| 类型   | 说明            | 范围          |
| ------ | --------------- | ------------- |
| UByte  | 8 位无符号整数  | $[0, 255]$    |
| UShort | 16 位无符号整数 | $[0, 65535]$  |
| UInt   | 32 位无符号整数 | $[0, 2^{32})$ | 
| ULong  | 64 位无符号整数 | $[0, 2^{64})$ |

无符号整数实质上是内联类，支持大多数有符号整数的运算

| 类型        | 说明        |
| ----------- | ----------- |
| UByteArray  | UByte 数组  |
| UShortArray | UShort 数组 |
| UIntArray   | UInt 数组   |
| ULongArray  | ULong 数组  | 

与此类似的，还有 `UIntRange`，`UIntProgression`，`ULongRange`，`ULongProgression` 等类型，类似原类型一样都没有开装箱开销

无符号整型的字面量使用 `u/U`，`ul/UL` 为后缀，表示 `UInt`，`ULong` 类型

## @JvmDefault
#实验性功能 

用于标注接口中的函数默认实现，生成 JVM 平台的 `default` 方法，会导致无法兼容 Java7 及之前版本

# 其他

- 允许无参 `main` 函数：`void main() { ... }`
- 元函数增加到 `Function42`
- 注解类允许包含的内嵌类、接口、对象、伴生对象
- 可用 `@JvmStatic` 和 `@JvmField` 注解接口伴生对象的成员，能够将其提升成类 `static` 成员
- `kotlin.random.Random` 平台统一随机数生成器
- `isNullOrEmpty`，`orEmpty`，`ifEmpty`，`ifBlank` 已添加到集合，映射及对象数组
- `array.copyInto` 方法用于快速复制数组
- `key.associateWith { value }` 可用于根据键创建 `Map`
- `KClass.sealedSubclasses` 可通过反射获取密封类所有直接子类型
