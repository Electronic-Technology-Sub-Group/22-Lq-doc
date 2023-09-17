使用 class 类名 构造 来定义类

```scala
class Greeter(prefix: String, suffix: String) {
    def greet(name: String): Unit = println(prefix + name + suffix)
}
// 最简单的类，默认自带一个无参构造
class User
```

使用 new 关键字创建类对象

```scala
class Greeter(prefix: String, suffix: String)
val greeter = new Greeter("Hello, ", "!")
greeter.greet("Scala developer") // Hello, Scala developer!
```
# 构造函数

构造函数支持使用默认参数，支持使用 `val` / `var` 修饰

```scala
class SomeValues(
    var name: String,
    val a: Int = 0,
    val b: Int = 1,
    val c: Boolean = false,
    d: String = ""
)

// 默认参数
val values1 = new SomeValues("Values1")
val values2 = new SomeValues("Values2", 3)
val values3 = new SomeValues("Values3", 5, 7, false, "hello")
// 只要一个默认参数被跳过，后面的参数都应带有参数名
val values4 = new SomeValues("Values3", 5, c = true)

// 使用 var / val 修饰的参数可被外界访问，相当于 public 类型的成员；没有的只能在类内访问到
value1.name // Value1
value1.d // error: value d is not a member of SomeValues
```

辅助构造：可有多个辅助构造器，辅助构造器必须在第一行使用 `this` 调用主构造器

```scala
// 可用 private 修饰构造器
class SomeValues private(
    var name: String,
    val a: Int = 0,
    val b: Int = 1,
    val c: Boolean = false,
    d: String = ""
) {
    // 辅助构造
    def this() {
        this("no-name")
        name = "hhh"
    }
}
```
# 属性

私有成员：默认所有成员为 public，使用 private 修饰的成员可成为私有成员

定义成员变量时，若属性名为 a，则 getter 为 `a`，setter 为 `a_=`，私有成员为 `_a`

```scala
class Point {
    private var _a = 0

    def a = _a
    def a_= (newValue) {
        // setter, 赋值/验证等操作
        _a = newValue
    }
}
val p = new Point()
p.a = 3
p.a // 3
```
# 类型检查

| **Scala**               | **Java**             |
| ----------------------- | -------------------- |
| obj.isInstanceOf[Class] | obj instanceof Class |
| obj.asInstanceOf[Class] | (Class)obj           |
| classOf[Class]          | Class.class          |
# 内部类

路径依赖：内部类是外部类对象的成员，内部类必须绑定外部类对象

```scala
class Graph {
    class Node {
        var connectedNodes: List[Node] = Nil
        var connectedOutsideNodes: List[Graph#Node] = Nil
        def connectTo(node: Node) {
            if (connectedNodes.find(node.equals).isEmpty) {
                connectedNodes = node :: connectedNodes
            }
        }
        // 对于不同 Graph 类的对象产生的 node，被认为为是不同的类，需要用 Graph#Node 代替
        def connectOutsideTo(node: Graph#Node) {
            if (connectedOutsideNodes.find(node.equals).isEmpty) {
                connectedOutsideNodes = node :: connectedOutsideNodes
            }
        }
    }

    var nodes: List[Node] = Nil
    def newNode: Node = {
        val res = new Node
        nodes = res :: nodes
        res
    }
}
val graph: Graph = new Graph()
val node1 = graph.newNode
val node2 = graph.newNode
val node3 = graph.newNode
node1.connectTo(node2)
node3.connectTo(node2)
val graph2: Graph = new Graph()
val node4 = graph2.newNode
node1.connectOutsideTo(node4)
```
# Case Class

封闭类，不可变的类，使用 `case class` 定义，创建对象时无需 `new` 关键字，常用于对不可变数据的建模
- 拥有默认的 `apply` 方法处理构造
- 所有成员变量必须都是 `val` 的
- `==` 比较时，按值比较
- 使用 `copy` 方法复制对象
# object

单例对象，使用 `object` 关键字定义

```scala
object IdFactory {
    private var counter = 0
    def create(): Int = {
        counter += 1
        counter
    }
}
```
## 伴随对象

与类名同名的对象，可访问类的私有成员

```scala
import scala.math._
case class Circle(radius: Double) {
    import Circle._
    def area: Double = calculateArea(radius)
}

object Circle {
    private def calculateArea(radius: Double) = 3.14 * pow(radius, 2.0)
}
```
## 工厂方法

```scala
class Email(val username: String, val domainName: String)

object Email {
  def fromString(emailString: String): Option[Email] = {
    emailString.split('@') match {
      case Array(a, b) => Some(new Email(a, b))
      case _ => None
    }
  }
}

val scalaCenterEmail = Email.fromString("scala.center@epfl.ch")
```
## 提取器

包含 unapply 方法的单例对象
- apply：构造器，接收参数创建对象
- unapply：提取器，返回创建对象时使用的参数 *常用于模式匹配和偏函数*

```scala
import scala.util.Random

object CustomerID {

  def apply(name: String) = s"$name--${Random.nextLong}"

  // 返回值选取：
  //    只是用于测试 - Boolean
  //    单值返回 - Option[T]
  //    多值返回 - Option[(T1, T2, ..., Tn)]
  //    未知个数 - 使用 unapplySeq 方法，返回 Option[Seq[T]]
  def unapply(customerID: String): Option[String] = {
    val stringArray: Array[String] = customerID.split("--")
    if (stringArray.tail.nonEmpty) Some(stringArray.head) else None
  }
}

// 相当于 customer1ID = CustomerID.apply("Sukyoung")
val customer1ID = CustomerID("Sukyoung")  // Sukyoung--23098234908
// 相当于 CustomerID.unapply(customer1ID).get
customer1ID match {
  case CustomerID(name) => println(name)
  case _ => println("Could not extract a CustomerID")
} // Sukyoung

// 可用于提取初始变量
val customer2ID = CustomerID("Nico")
val CustomerID(name) = customer2ID
println(name)  // Nico
val CustomerID(name2) = "--asdfasdfasdf"
println(name2) // ""
// 无匹配：scala.MatchError: -asdfasdfasdf (of class java.lang.String)
// val CustomerID(name3) = "-asdfasdfasdf"
// println(name3)
```
## 包对象

每个包都可以有一个包对象，包对象中的任意定义都被认为是包自身成员。包对象一般放在 package.scala 源文件中（惯例）

```scala
// 包对象
// 可混入/继承
// in file gardening/fruits/package.scala
package gardening
package object fruits extends FruitAliases with FruitHelpers {
  val planted = List(Apple, Plum, Banana)
  def showFruit(fruit: Fruit): Unit = {
    println(s"${fruit.name}s are ${fruit.color}")
  }
}
```

```scala
// in file gardening/fruits/Fruit.scala
package gardening.fruits

case class Fruit(name: String, color: String)
object Apple extends Fruit("Apple", "green")
object Plum extends Fruit("Plum", "blue")
object Banana extends Fruit("Banana", "yellow")
```

```scala
// 包外调用
// in file PrintPlanted.scala
import gardening.fruits._
object PrintPlanted {
  def main(args: Array[String]): Unit = {
    for (fruit <- fruits.planted) {
      showFruit(fruit)
    }
  }
}
```
# Implicit Class

自 Scala 2.10，Scala 支持隐式类用于类型转换

隐式类只能在 Class，object，Trait 中定义

```scala
object Helpers {
  implicit class IntWithTimes(x: Int) {
    def times[A](f: => A): Unit = {
      def loop(current: Int): Unit =
        if(current > 0) {
          f
          loop(current - 1)
        }
      loop(x)
    }
  }
}

5 times println("HI") // 5 被转换成 IntWithTimes 了，因此可以使用 times 方法
```
# 枚举

Scala 没有提供专门的枚举类，但提供了一个类 Enumeration 用于生成枚举

（有点像Java，Java虽然提供了 enum 关键字生成枚举，但该枚举实际上也是个继承自 Enum 的类，内含一个数组包括枚举值以及一系列缓存）

```scala
object EnumType {
    // 推荐加上 type，便于 import
    type AliasType = EnumType.Value
    // val 所有枚举值 = Value
    val Enum0, Enum1, Enum2, Enum3, EnumN = Value
}
```

其中，创建枚举值时候也可以这么写

```scala
// 等同于 val Enum0, Enum1, Enum2, Enum3, EnumN = Value
val Enum0 = Value
val Enum1 = Value
val Enum2 = Value
val Enum3 = Value
val EnumN = Value
```

枚举的 Value 可以传入两个值，一个 Int 为 ID，一个 String 为 name，或其中一个

```scala
val Enum3 = Value(5, "enum")
```
