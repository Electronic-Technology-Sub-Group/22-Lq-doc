Groovy 完全兼容 Java 的语法，任何 Java 代码都可以直接执行。在语法上，Groovy 也有一些特定的语法扩展

- 脚本语言：可以不声明类直接写代码，自动生成对应类
	- 甚至入口函数 main 也可以省略

```groovy
int a = 1
int b = 2

println(a + b)
```

反编译后，可以看到自动生成了继承自 Script 的主类和入口函数

![[Pasted image 20230821093023.png]]

- 行末 `;` 可省略

```groovy
int a = 1
int b = 2
```

- 可使用关键字 `def` 声明变量，类似于 `var`，表示动态类型或依赖于编译器类型推断

```groovy
var a = 1
def b = 2
```

- 新若干关键字，包括 `it`，`in`，`as`，`def`，`trait` 等

- 某些常用 JDK 和 GDK 包被默认导入了
	- java.lang.*
	- java.util.*
	- java.io.*
	- java.net.*
	- java.math.BigInteger
	- java.math.BigDecimal
	- groovy.lang.*
	- groovy.util.*

- 可以使用 as 定义导入的别名，适用于普通类导入和静态导入

