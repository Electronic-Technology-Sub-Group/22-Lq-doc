- 表达式即可以计算的语句

```scala
println("Hello scala")
```

- 变量 使用 var 声明（Variable），常量 使用 val 声明（Value）

```scala
var a = 1 // 这里使用了自动编译器的类型推算，可以不必显式声明 Int
var b: Int = 2
a = 3

val c = 1
val d: String = "scala"
c = 2 // 异常：error: reassignment to val
```

- 使用 {} 组合的表达式称为代码块，最后一个表达式即为该代码块的返回值

```scala
println({
    val x = 1 + 2
    "代码块返回：" + (x + 1)
})
```
