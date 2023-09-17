使用 def 定义

```scala
def name(argMap): returnType = { ... }
```

```scala
// add: (x: Int, y: Int)Int
def add(x: Int, y: Int): Int = x + y
val addResult = add(3, 8) // 8
```
# 形参列表

一个函数可以有0个或多个参数列表，0 个可省略括号

```scala
// 参数列表可以有0个或多个
// getAnswer: Int
def getAnswer: Int = 42
val ans1 = getAnswer // 42
// addThenMultiply: (x: Int, y: Int)(multiplier: Int)Int
def addThenMultiply(x: Int, y: Int)(multiplier: Int) = (x + y) * multiplier
val ans2 = addThenMultiply(3, 5)(7) // 56 [(3+5)*7]
```

- 变长参数：声明类型为 T 的变长参数时参数类型为 `T*`
	- 函数内使用时类型为 `Seq[T]`
	- 仅允许函数最后一个参数为变长参数
	- 变长参数传递时需要显式指明为 `_*` 类型

```scala
// 使用 String* 创建变长参数
def fun1(i: Int, values: String*): Unit = {}
def fun2(values: String*): Unit = {
    // 使用 _* 类型指明该参数为一个变长参数的整体
    fun1(0, values: _*)
}
```

- 部分应用：当一个函数拥有多个参数列表时，可以实现某些列表从而生成新的函数

```scala
// 部分应用 实例
val numbers = List(3, 5, 7)
val numberFunc = numbers.foldLeft(List[Int]()) _ // ((List[Int], Int) => List[Int]) => List[Int]
val squares = numberFunc((xs, x) => xs :+ x*x) // List[Int] = List(9, 25, 49)
val cubes = numberFunc((xs, x) => xs :+ x*x*x) // List[Int] = List(27, 125, 343)
```

- 传名参数：在类型前使用 `=>` 即可将参数转化为传名参数，具有懒加载的特性

```scala
def calculate(input: => Int) = input * 37
def whileLoop(condition: => Boolean)(body: => Unit): Unit {
    if (condition) {
        body
        whileLoop(condition)(body)
    }
}
```

- 默认参数：可在调用时忽略默认参数
	- 若调用参数列表中间忽略参数 则必须全部带名传入

```scala
def log(message: String, level: String = "INFO") = println(s"$level: $message")
log("System starting")  // prints INFO: System starting
log("User not found", "WARNING")  // prints WARNING: User not found

class Point(val x: Double = 0, val y: Double = 0)
val p1 = Point(y = 1)
```

- 具名参数：可通过形参名称来对实参进行重排序

```scala
def printName(first: String, last: String): Unit = println(first + " " + last)

printName("John", "Smith")  // "John Smith"
printName(first = "John", last = "Smith")  // "John Smith"
printName(last = "Smith", first = "John")  // "John Smith"
```
# Lambda 表达式

带有参数的表达式，可有返回值，可以匿名或命名，使用 `参数列表 => 表达式` 声明

```scala
// 一个匿名方法
(x: Int) => x+1
val add = (x: Int, y: Int) => x + y // add: (Int, Int) => Int = $$Lambda$1061/1040079319@1be8c122
val getAnswer = () => 42
val a = add(3, 5) // 8
```
# 多态

Scala 可按照类型和值进行参数化，类似泛型

```scala
def listOfDuplicates[A](x: A, length: Int): List[A] = {
    if (length < 1) Nil
    else x :: listOfDuplicates(x, length - 1)
}
println(listOfDuplicates[Int](3, 4))  // List(3, 3, 3, 3)
println(listOfDuplicates("La", 8))  // List(La, La, La, La, La, La, La, La)
```
## 隐式参数

若参数列表中没有正确传入指定类型的参数 则 Scala 会判断是否能自行获取正确类型的隐式值
- 调用该参数时，在所在代码块中寻找是否有可直接访问的被 implicit 修饰的参数或方法
- 没有 则会在所在类的伴生对象中找与隐式参数相关的被 implicit 修饰的成员

```scala
abstract class Monoid[A] {
    def add(x: A, y: A): A
    def unit: A
}

object ImplicitTest {
    implicit val stringMonoid: Monoid[String] = new Monoid[String] {
        def add(x: String, y: String): String = x concat y
        def unit: String = ""
    }

    implicit val intMonoid: Monoid[Int] = new Monoid[Int] {
        def add(x: Int, y: Int): Int = x + y
        def unit: Int = 0
    }

    def sum[A](xs: List[A])(implicit m: Monoid[A]): A =
        if (xs.isEmpty) m.unit
        else m.add(xs.head, sum(xs.tail))


    def main(args: Array[String]): Unit = {
        println(sum(List(1, 2, 3)))       // 6
        println(sum(List("a", "b", "c"))) // abc
}
```
# 高阶函数

以函数为参数，或以函数为返回值的函数

```scala
val salaries = Seq(20000, 70000, 40000)
val newSalaries = salaries.map(_ * 2) // _ * 2 为匿名函数，参数 Scala 通过上下文推测为一个 Int，因此可以使用 _ 替代
println(newSalaries) // List(40000, 140000, 80000)
```
## 接收函数的函数

```scala
// 接收函数的函数
object SalaryRaiser {
    private def promotion(salaries: List[Double], promotionFunction: Double => Double): List[Double] = 
        salaries.map(promotionFunction)
    def smallPromotion(salaries: List[Double]): List[Double] = promotion(salaries, salary => salary * 1.1)
    def greatPromotion(salaries: List[Double]): List[Double] = 
        promotion(salaries, salary => salary * math.log(salary))
    def hugePromotion(salaries: List[Double]): List[Double] = promotion(salaries, salary => salary * salary)
}
```
## 返回函数的函数

```scala
def urlBuilder(ssl: Boolean, domain: String): (String, String) => String = {
    val schema = if (ssl) "https://" else "http://"
    (endpoint: String, query: String) => s"$schema$domain/$endpoint?$query"
}

val domain = "www.example.com"
val getUrl = urlBuilder(ssl = true, domain)
val endpoint = "users"
val query = "id=1"
val url = getUrl(endpoint, query)
println(url) // https://www.example.com/users?id=1
```
## 嵌套方法

```scala
def factorial(x: Int): Int = {
    def fact(x: Int, accumulator: Int): Int = {
        if (x <= 1) accumulator
        else fact(x-1, x*accumulator)
    }
    fact(x, 1)
}

println("Factorial of 2: " + factorial(2)) // Factorial of 2: 2
println("Factorial of 3: " + factorial(3)) // Factorial of 3: 6
```
# 运算符

Scala 中，运算符即方法，任何具有单个参数的方法都可以作为中缀运算符

```scala
println("+ => " + 10.+(1)) // + => 11
println("+ => " + (10 + 1)) // + => 11
```
## 自定义运算符

```scala
// 可使用任何合法标识符作为运算符
case class Vec(val x: Double, val y: Double) {
    def +(that: Vec) = new Vec(x + that.x, y + that.y)
}

val v1 = Vec(1.0, 1.0)
val v2 = Vec(2.0, 2.1)
val v3 = v1 + v2
println(v3) // Vec(3.0,3.1)
