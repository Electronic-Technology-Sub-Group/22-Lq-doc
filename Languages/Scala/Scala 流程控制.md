# main

JVM 平台需要实现的一个入口方法，接受一个 `Array[String]` 参数

```scala
object Main {
    def main(args: Array[String]): Unti = {
        // ....
        println("Hello, Scala developer[main]")
    }
}
```


# 异常捕获

Scala 的 catch 子句中，使用的是匹配的方法，内为一系列 case 子句

```scala
try {
    // sth
} catch {
    case ex: Exception => //...
} finally {
    //...
}
```

所有的异常继承自 Throwable。如果没有匹配到则会 throw 出该异常。详见 模式匹配
# 循环

while 循环语法与 Java 基本无异。而 for 循环语法有些许不同，本质是 `for-in` 循环

```scala
for (obj <- enumerators) ...
```
- `enumerators` 常用 `until` 创建 Range，则可实现 `for i` 循环

还可以利用 `for` 与 `yield` 快速创建列表，支持 list, withFilter, map, flatMap 等操作，类似 Python

```scala
val list = for (obj <- enumerators) yield e
```

```scala
case class User(name: String, age: Int)

val userBase = List(
    User("Travels", 28),
    User("Kelly", 33),
    User("Jennifer", 44),
    User("Dennis", 23)
)
// 创建 list
val users = for(user <- userBase if(user.age >= 20 && user.age < 30))
    yield user
users.foreach(println) // 20, 21, ..., 29

def foo(n: Int, v: Int) = 
    for (i <- 0 until n;
         j <- i until n if i + j == v)
            yield (i, j)
foo(10, 10) foreach {
    case (i, j) => print(s"($i, $j)")
} // (1, 9)(2, 8)(3, 7)(4, 6)(5, 5)
println()
```
# 模式匹配

根据模式检查值，用以代替 switch/if-else，有返回值，详见 [[Scala 模式匹配]]

```scala
a match { case xx => ... }
```

使用 `_` 表示没有匹配到的项

```scala
import scala.util.Random
val x: Int = Random.nextInt(10)
def matchText(x: Int): String = x match {
    case 0 => "zero"
    case 1 => "one"
    case 2 => "teo"
    case _ => "many"
}
```
