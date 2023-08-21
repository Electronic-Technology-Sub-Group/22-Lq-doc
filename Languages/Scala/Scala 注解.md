将元信息与定义发生关联，使用 Java 注解 需要 `-target:jvm-1.8` 参数

除了 value 可以不写，其他必须要使用具名参数，其他差不多

```scala
object DeprecationDemo extends App {
    // 使用该方法时，编译器会发生警告
    @deprecated("deprecation message", "release # which deprecates method")
    def hello = "hola"

    hello  
}

def factorial(x: Int): Int = {

    // 该注解保证该方法为尾递归
    @tailrec
    def factorialHelper(x: Int, accumulator: Int): Int = {
      if (x == 1) accumulator else factorialHelper(x - 1, accumulator * x)
    }
    factorialHelper(x, 1)
}
```
