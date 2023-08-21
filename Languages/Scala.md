# 基本语法

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

- 使用 {} 组合的表达式，最后一个表达式即为该代码块的返回值

```scala
println({
    val x = 1 + 2
    "代码块返回：" + (x + 1)
})
```

# 类型
## Any

![Scala Type Hierarchy](unified-types-diagram.svg)

Any 类是一切类型的基类，又称 top type，定义一系列通用方法，有两个直接子类 AnyVal，AnyRef。

```scala
val list: List[Any] = List(
    /** AnyRef(String) */ "a string",
    /** AnyVal(Int) */ 7312,
    /** AnyVal(Char) */ 'c',
    /** AnyVal(Boolean) */ true,
    /** AnyRef(Lambda) */ () => "An anonymous function[String]"
)
```
### AnyVal

所有值类型的基类，非空，预定义了 9 种类型，对应 Java 8 种基本类型及 Unit
- `Double`
- `Float`
- `Long`
- `Int`
- `Short`
- `Byte`
- `Char`
- `Boolean`
- `Unit`：不携带任何实际信息，只有一个实例，可以使用 () 声明，常用于函数返回值，表示该函数无返回

AnyVal 可以沿一定方向进行单向转换，逆向无法转换

![Scala Type Hierarchy](type-casting-diagram.svg)

```scala
val x: Long = 987654321
val y: Float = x  // 9.8765434E8，会丢失一定的精度
val z: Long = y  // 无法编译通过

val face: Char = '☺'
val number: Int = face  // 9786
```
### AnyRef

一切引用类型的基类，所有非 AnyVal 的类都是该类的子类，Jvm 环境下与 `Object` 类对应
### Nothing

Nothing 为所有类型的子类，也称 bottom type，常用于非正常终止，如抛出异常/无限循环
### Null

Null 为所有 AnyRef 类型的子类，有一个单值，由 null 关键字定义。该类用于兼容其他 JVM 平台语言，几乎不应在 Scala 代码中使用
## 泛型

使用 `[]` 设置，类似 Java 的泛型

```scala
// 带有泛型的特质
trait Iterable[A] {
    def hasNext: Boolean
    def next(): A
}

// 泛型类
class Stack[A] {
    private var elements: List[A] = Nil

    def push(x: A) { elements = x :: elements }
    def peek: A = elements.head
    def pop: A = {
        val currentTop = peek
        elements = elements.tail
        currentTop
    }
}
val stack = new Stack[Int]
// 可以传入所有 Int 类型及其子类型的参数
stack.push(1)
stack.push(2)
println(stack.pop) // 2
println(stack.pop) // 1
```
### 协变与逆变

- 不变：默认情况下，泛型不变，`Class[A]` 与 `Class[B]` 不存在任何继承关系，即使 A 与 B 存在继承关系
- 协变： `+T -> Class[+A]`: 若存在类型 A B，A 为 B 的子类型，则 `Class[A]` 为 `Class[B]` 的子类型

```scala
abstract class Animal {
    def name: String
}
case class Cat(name: String) extends Animal
case class Dog(name: String) extends Animal
def printAnimalNames(animals: List[+Animal] /** 可以接受 List[Cat], List[Dog] */): Unit = {
    animals.foreach(animal => println(animal.name))
}
val cats: List[Cat] = List(Cat("Whiskers"), Cat("Tom"))
val dogs: List[Dog] = List(Dog("Fido"), Dog("Rex"))
printAnimalNames(cats) // Whiskers Tom
printAnimalNames(dogs) // Fido Rex
```

- 逆变: `-T -> Class[-A]`: 若存在类型 A B，A 为 B 的子类型，则 `Class[B]` 为 `Class[A]` 的子类型 

```scala
abstract class Animal {
    def name: String
}
case class Cat(name: String) extends Animal
case class Dog(name: String) extends Animal

abstract class Pointer[-A] {
    def print(value: A): Unit
}

class AnimalPointer extends Pointer[Animal] {
    override def print(animal: Animal): Unit = println("animal: " + animal.name)
}

class CatPointer extends Pointer[Cat] {
    override def print(cat: Cat): Unit = println("cat: " + cat.name)
}
// 逆变
// 接收 Pointer[Cat] 和 Pointer[Animal]
def printMyCat(pointer: Pointer[Cat]): Unit = {
    pointer.print(myCat)
}

val myCat = Cat("Boots")
val catPointer: Pointer[Cat] = new CatPointer
val animalPointer: Pointer[Cat] = new AnimalPointer
ConvarianceTest.printMyCat(catPointer) // cat: Boots
ConvarianceTest.printMyCat(animalPointer) // animal: Boots
```
### 上下界

声明上界：泛型的类型必须是"某种类型"或某种类型的"子类"

```scala
// 特殊标记：<%，类似 <:，允许使用隐式转换（详见隐式转换）
abstract class Animal {
    def name: String
}

abstract class Pet extends Animal

class Cat extends Pet {
    override def name: String = "cat"
}

class Dog extends Pet {
    override def name: String = "dog"
}

class Lion extends Animal {
    override def name: String = "lion"
}

class PetContainer[P <: Pet /** 不能是 Lion */](p: P) {
    def pet: P = p
}
```

声明下界：泛型的类型必须是"某种类型"或某种类型的"父类"

```scala
trait Node[+B] {
    def prepend[U >: B](elem: U): Node[U]
}

case class ListNode[+B](h: B, t: Node[B]) extends Node[B] {
    def prepend[U >: B](elem: U): ListNode[U] = ListNode(elem, this)
    def head: B = h
    def tail: Node[B] = t
}

case class Nil[+B]() extends Node[B] {
    def prepend[U >: B](elem: U): ListNode[U] = ListNode(elem, this)
}

trait Bird
case class AfricanSwallow() extends Bird
case class EuropeanSwallow() extends Bird

val africanSwallowList = ListNode[AfricanSwallow](AfricanSwallow(), Nil())
val birdList: Node[Bird] = africanSwallowList
birdList.prepend(new EuropeanSwallow)
```
## 隐式转换

实现类型转换的是隐含方法（`implicit`），设待转换对象为 e，则有
- 表达式的类型为 S，且 S 不符合期望类型 T： 搜索转换 c 适用于 e，且结果为 T
- 类型为 S 的实例对象中调用某方法 m，且该方法未在 S 中声明：搜索转换 c 适用于 e，且包含方法 m

```scala
import scala.language.implicitConversions
// 定义在类外部的隐式转换
implicit def list2ordered[A](x: List[A])(implicit elem2ordered: A => Ordered[A]): Ordered[List[A]] = 
    new Ordered[List[A]] {
        def compare(that: List[A]): Int = 1
    }
// 上下文中存在 List[A]=>Ordered[List[A]] 和 Int=>Ordered[Int]
// Int=>Ordered[Int] 位于 scala.Predef.intWrapper
List(1, 2, 3) <= List(4, 5) 

// 一个更简单的隐式转换
class BufferType private(private val value: Int)
// 定义在伴随对象中（详见后面 类与特质 一节）
object BufferType {
  val ARRAY_BUFFER: BufferType = BufferType(GL15.GL_ARRAY_BUFFER)
  implicit def toIntFunction(bufferType: BufferType): Int = bufferType.value
}
```
## 类型推断

Scala 编译器对表达式类型进行推断，因此不必显式声明，但无法根据方法体推断形参列表和返回值的参数
## 类型成员

Trait 和 抽象类都可以包含抽象类型成员，使用 `type` 声明，实际类型可由具体实现确定

```scala
trait Buffer {
    type T
    val element: T
}

abstract class SeqBuffer extends Buffer {
    type U
    type T <: Seq[U] // 给 T 追加类型上界
    def length = element.length
}

// 常和匿名类使用
abstract class IntSeqBuffer extends SeqBuffer {
    type U = Int
}

def newIntSeqBuf(elem1: Int, elem2: Int): IntSeqBuffer = new IntSeqBuffer {
    type T = List[U]
    val element = List(elem1, elem2)
}
val buf = newIntSeqBuf(7, 8)
println("length = " + buf.length)
println("content = " + buf.element)
```

可与泛型互换

```scala
abstract class Buffer2[+T] {
    val element: T
}

abstract class SeqBuffer2[U, +T <: Seq[U]] extends Buffer2[T] {
    def length = element.length
}

def newIntSeqBuf2(elem1: Int, elem2: Int): SeqBuffer2[Int, Seq[Int]] = new SeqBuffer2[Int, List[Int]] {
    val element = List(elem1, elem2)
}
val buf2 = newIntSeqBuf(7, 8)
println("length = " + buf.length)
println("content = " + buf.element)
```
## 复合类型

一个对象的类型是其他多种类型的子类型

```scala
trait Cloneable extends java.lang.Cloneable {
    override def clone(): Cloneable = {
        super.clone().asInstanceOf[Cloneable]
    }
}

trait Resetable {
    def reset: Unit
}

// 将 obj 同时指定为 Cloneable 和 Resetable
def cloneAndReset(obj: Cloneable with Resetable): Cloneable = {
    val cloned = obj.clone()
    obj.reset
    cloned
}
```
# 方法

类似 Function，使用 def 定义

```scala
def name(argMap): returnType = { ... }
```

```scala
// add: (x: Int, y: Int)Int
def add(x: Int, y: Int): Int = x + y
val addResult = add(3, 8) // 8
```
### 参数列表

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
### Lambda 表达式

带有参数的表达式，可有返回值，可以匿名或命名，使用 `参数列表 => 表达式` 声明

```scala
// 一个匿名方法
(x: Int) => x+1
val add = (x: Int, y: Int) => x + y // add: (Int, Int) => Int = $$Lambda$1061/1040079319@1be8c122
val getAnswer = () => 42
val a = add(3, 5) // 8
```
## 多态

Scala 可按照类型和值进行参数化，类似泛型

```scala
def listOfDuplicates[A](x: A, length: Int): List[A] = {
    if (length < 1) Nil
    else x :: listOfDuplicates(x, length - 1)
}
println(listOfDuplicates[Int](3, 4))  // List(3, 3, 3, 3)
println(listOfDuplicates("La", 8))  // List(La, La, La, La, La, La, La, La)
```
### 隐式参数

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
## 高阶函数

以函数为参数，或以函数为返回值的函数

```scala
val salaries = Seq(20000, 70000, 40000)
val newSalaries = salaries.map(_ * 2) // _ * 2 为匿名函数，参数 Scala 通过上下文推测为一个 Int，因此可以使用 _ 替代
println(newSalaries) // List(40000, 140000, 80000)
```
### 接收函数的函数

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
### 返回函数的函数

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
### 嵌套方法

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
## 运算符

Scala 中，运算符即方法，任何具有单个参数的方法都可以作为中缀运算符

```scala
println("+ => " + 10.+(1)) // + => 11
println("+ => " + (10 + 1)) // + => 11
```
### 自定义运算符

```scala
// 可使用任何合法标识符作为运算符
case class Vec(val x: Double, val y: Double) {
    def +(that: Vec) = new Vec(x + that.x, y + that.y)
}

val v1 = Vec(1.0, 1.0)
val v2 = Vec(2.0, 2.1)
val v3 = v1 + v2
println(v3) // Vec(3.0,3.1)
```
### 运算符优先级

1. (characters not shown below)
2. \* / %
3. \+ -
4. :
5. = !
6. < \>
7. &
8. ^
9. |
10. (all letters)
## main 方法

JVM 平台需要实现的一个入口方法，接受一个 `Array[String]` 参数

```scala
object Main {
    def main(args: Array[String]): Unti = {
        // ....
        println("Hello, Scala developer[main]")
    }
}
```
## 异常捕获

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
# 类与特质
## 类

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
### 构造函数

构造函数中支持使用默认参数，支持使用 val/var 修饰

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

// 使用 var/val 修饰的参数可被外界访问，相当于 public 类型的成员；没有的只能在类内访问到
value1.name // Value1
value1.d // error: value d is not a member of SomeValues
```

辅助构造：可有多个辅助构造器，辅助构造器必须在第一行使用 this 调用主构造器

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
### Getter/Setter

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
### 类型检查

| **Scala**               | **Java**             |
| ----------------------- | -------------------- |
| obj.isInstanceOf[Class] | obj instanceof Class |
| obj.asInstanceOf[Class] | (Class)obj           |
| classOf[Class]          | Class.class          |
## 内部类

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
## Case Class

封闭类，不可变的类，使用 case class 定义，创建对象时无需 new 关键字，常用于对不可变数据的建模
- 拥有默认的 apply 方法处理构造
- 所有成员变量必须都是 val 的
- == 比较时，按值比较
- 使用 copy 方法复制对象
## object

单例对象，使用 object 关键字定义

```scala
object IdFactory {
    private var counter = 0
    def create(): Int = {
        counter += 1
        counter
    }
}
```
### 伴随对象

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
### 工厂方法

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
### 提取器

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
### 包对象

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
## Trait

特质，类似于 Java 的接口，使用 trait 定义，可被继承/实现/混入，但不能实例化

```scala
trait GreeterTrait {
    def greet(name: String): Unit
}
```
### 扩展

继承：使用 extends 继承，使用 override 标记实现基类/接口方法

```scala
class Greeter extends GreeterTrait {
    override def greet(name: String) = println("hello, " + name + "!")
}
new Greeter().greet("Scala") // hello, Scala!
```

混入：另一种实现特质的方式。混入可使一个类扩展多个特质

```scala
abstract class A {
    val message: String
}

class B extends A {
    val message = "I'm an instance of class B"
}

trait C extends A {
    def loudMessage = message.toUpperCase()
}
// 一个 Class 只能有一个基类 用 extends 定义
// 可以有多个 Mixin 用 with 定义，Mixins 可以与类有相同基类
class D extends B with C
```
### 自类型

用于声明一个 Trait 的上下文（this）类型

```scala
trait User {
    def username: String
}

trait Tweeter {
    this: User => // 重新赋予 this 类型
    def tweet(tweetText: String) = println(s"$username: $tweetText")
}

class VerifiedTweeter(val username_ : String) extends Tweeter with User { // 必须混入 User
    def username = s"real $username_"
}

val realBeyoncé = new VerifiedTweeter("Beyoncé")
// real Beyoncé: Just spilled my glass of lemonade
realBeyoncé.tweet("Just spilled my glass of lemonade")
```
## Implicit Class

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
## 枚举

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
## Tuple

元组：包含多个不同种类元素的容器，不可变，常用于函数返回多个值，Tuple 类一直到 Tuple22（即22个）

```scala
// Tuple2[String, Int]，可简写为 (String, Int)
val ingredient = ("Sugar", 25)
```

使用 `_n` 访问元组中的值，从 1 开始

```scala
ingredient._1 // "Sugar"
ingredient._2 // 25
```
# 包

- Scala 文件头部声明一个或多个包名创建包
  
  ```scala
  package users
  class User
  ```

- 使用 package 代码块
  
  ```scala
  package users {
      package administrators {
          class AdminUser
      }
      package normalusers {
          class NormalUser
      }
  }
  ```

- 使用 import 导入其他包的成员 包括 类，Trait
	- 函数等，同包成员不需要导入
	- `scala`，`java.lang`，`object Predef` 包已被默认导入
	- Scala 可以在任意位置导入，若存在命名冲突且从项目根目录导入，使用 `_root_`

```scala
import users._ // 导入 users 包所有成员
import users.User // 导入 users 包的 User 类
import users.{User, UserPreferences} // 导入 users 包的部分成员
import users.{UserPreferences => UPrefs} // 导入 users 包的 UserPreferences 成员并为其设置别名
import _root_.users._
```

```ad-hint
Scala 包命名惯例为：
 - 包名与文件目录名相同，但不强求
 - 包名应全部小写
 - 前三层包一般为 `<top-level-domain>.<domain-name>.<project-name>`
```
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

根据模式检查值，用以代替 switch/if-else，有返回值

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
## 匹配 case class

```scala
abstract class Notification
case class Email(sender: String, title: String, body: String) extends Notification
case class SMS(caller: String, message: String) extends Notification
case class VoiceRecording(contactName: String, link: String) extends Notification

def showNotification(notification: Notification): String = {
    notification match {
        case Email(sender, title, _) =>
          s"You got an email from $sender with title: $title"
        case SMS(number, message) =>
          s"You got an SMS from $number! Message: $message"
        case VoiceRecording(name, link) =>
          s"you received a Voice Recording from $name! Click the link to hear it: $link"
    }
}
val someSms = SMS("12345", "Are you there?")
val someVoiceRecording = VoiceRecording("Tom", "voicerecording.org/id/123")
// You got an SMS from 12345! Message: Are you there?
println(showNotification(someSms))
// you received a Voice Recording from Tom! Click the link to hear it: voicerecording.org/id/123
println(showNotification(someVoiceRecording)) 
```
## 仅匹配某类型

```scala
abstract class Device
case class Phone(model: String) extends Device{
  def screenOff = "Turning screen off"
}
case class Computer(model: String) extends Device {
  def screenSaverOn = "Turning screen saver on..."
}

def goIdle(device: Device) = device match {
  case p: Phone => p.screenOff
  case c: Computer => c.screenSaverOn
}
```
## 匹配元组

实际是提取器的功能

- 拆分元组

  ```scala
  val (name, quantity) = ingredient
  name // Sugar
  quantity // 25
  ```

- foreach

  ```scala
  val planets = List(
      ("Mercury", 57.9), ("Venus", 108.2), ("Earth", 149.6), ("Mars", 227.9), ("Jupiter", 778.3)
  )
  planets.foreach{
      case("Earth", distance) => println(s"Our planet is $distance million kilometers from the sun")
      case _ =>
  } // Our planet is 149.6 million kilometers from the sun
  ```
## 正则

使用 .r 将任意 String 转化为正则

```scala
import scala.util.matching.Regex

val numberPattern: Regex = "[0-9]".r

numberPattern.findFirstMatchIn("awesomepassword") match {
  case Some(_) => println("Password OK")
  case None => println("Password must contain a number")
} // Password must contain a number

// 带有搜索
val keyValPattern: Regex = "([0-9a-zA-Z-#() ]+): ([0-9a-zA-Z-#() ]+)".r

val input: String =
  """background-color: #A03300;
    |background-image: url(img/header100.png);
    |background-position: top center;
    |background-repeat: repeat-x;
    |background-size: 2160px 108px;
    |margin: 0;
    |height: 108px;
    |width: 100%;""".stripMargin

/**
key: background-color value: #A03300
key: background-image value: url(img
key: background-position value: top center
key: background-repeat value: repeat-x
key: background-size value: 2160px 108px
key: margin value: 0
key: height value: 108px
key: width value: 100
*/
for (patternMatch <- keyValPattern.findAllMatchIn(input))
    println(s"key: ${patternMatch.group(1)} value: ${patternMatch.group(2)}")
```
# 注解

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
# 集合

Scala 集合包括可变集合与不可变集合，分别在 `scala.collection.mutable` 和 `scala.collection.immutable` 包中。

一般来说，若要同时使用可变和不可变集合，约定只导入 `scala.collection.mutable` 包，使用 mutable.Xxx 作为可变集合，Xxx 作为不可变集合。

`scala.collection.generic` 包中包含了集合的抽象构建块。

除了 Buffer 外，所有集合类都存在 根、可变、不可变 三种变体（Buffer 只存在可变）
### 继承关系

- 根继承：
![image-20200627145127884](image-20200627145127884.png)
- 可变：
![image-20200627145207479](image-20200627145207479.png)
- 不可变：
![image-20200627145252581](image-20200627145252581.png)
### 通用迭代方法

集合均实现了 Iterable 特征，该特征定义了以下几种基本方法

```scala
val list = List(0, 1, 2, 3, 4, 5)
```

- iterator、foreach：获取 Iterator 迭代器，或对其每个元素进行访问迭代
  
  ```scala
  val list = List(1, 2, 3, 4, 5)
  // Iterator[Int] = <iterator>
  val iter = list.iterator
  ```

- concat：连接两个集合
  
  ```scala
  val list0 = List(0, 1, 2)
  val list1 = List(3, 2, 1)
  // list = List(0, 1, 2, 3, 2, 1)
  val list = list0 + list1
  ```

- map、flatMap、collect：通过原本集合的元素生成新集合
	- collect 方法：接收一个偏函数（可以是普通函数，此时与 map 无异；也可以是 case 子句，相当于 filter+map）
  
  ```scala
  def list = List(0, 1, 2, 3, 4, 5)
  // List(0, 1, 10, 11, 100, 101)
  val retMap = list.map(i => i.toBinaryString)
  // List(0, 0, 1, 0, 1, 2, 0, 1, 2, 3, 0, 1, 2, 3, 4, 0, 1, 2, 3, 4, 5)
  val retFlatMap = list.flatMap(i => 0.to(i))
  // List(10, 1, 12, 3, 14, 5)
  val retCollect = list.collect {
    case i if (i % 2 == 0) => i+10 // 0=>10 2=>12 4=>14
    case i => i // 1=>1 3=>3 5=>5
  }
  // List(10, 12, 14)
  val retCollect2 = list.collect {
    case i if (i % 2 == 0) => i+10 // 0=>10 2=>12 4=>14（map），其他因为没有 case 匹配，抛弃了（filter）
  }
  ```

- to 及 toList 等一系列转换方法：将该集合整体转换为其他类型集合；若该集合本就是该类型，则返回 this
  
  ```scala
  def list = List(0, 1, 2)
  // Set(0, 1, 2)
  println(list.toSet)
  // List(0, 1, 2)
  println(list.toList)
  // true
  println(list.toList == list)
  ```

- copyToArray：复制到数组
  
  ```scala
  val list = List(0, 1, 2, 3, 4, 5)
  val array = Array(-1, -1, -1, -1, -1)
  list.copyToArray(array, 1, 3)
  // -1, 0, 1, 2, -1
  println(array.mkString(", "))
  ```

- isEmpty，notEmpty，size，sizeCompare：判断该集合是否可以获取其存储的元素数目（如 LazyList 可能具有无限的元素等），获取集合元素数目等
	- knownSize：类似 size，一般来说可以通过有限的时间计算出 size 则会返回 size，否则（如需要遍历整个列表）返回 -1
	- sizeIs：提供一种 sizeCompare 的简化操作，list.sizeIs < size <==> list.sizeCompare(size) < 0，支持大小比较、\=\=、!= 的比较
- head、last、headOption、lastOption、find：集合元素检索/搜索
- tail、init、slice、take、drop、filter：通过某些选择或过滤，生成集合的子集合
	- tail/init：除 head/last 外的所有元素
	- slice：截取 `[fromInclude, toExclude]` 的元素

```scala
// List(1, 2, 3, 4, 5)
val tail = list.tail
// List(0, 1, 2, 3, 4)
val init = list.init
// List(1, 2, 3)
val slice = list.slice(1, 4)
```

- take/drop：选取前/后 n 个元素
	- takeWhile/dropWhile：选取/丢弃前 n 个连续的符合要求的元素后的集合
	- takeRight/dropRight：同 take/drop，倒序选取
  
  ```scala
  // List(0, 1, 2)
  val take = list.take(3)
  // List(3, 4, 5)
  val takeRight = list.takeRight(3)
  // List(0) 仅选取了 0
  val takeWhile = list.takeWhile(i => i % 2 == 0)
  // List(3, 4, 5)
  val drop = list.drop(3)
  // List(0, 1, 2)
  val dropRight = list.dropRight(3)
  // List(1, 2, 3, 4, 5) 仅丢弃了 0
  val dropWhile = list.dropWhile(i => i % 2 == 0)
  ```

- filter/filterNot：过滤
	- withFilter：非严格过滤器，会在后续的 map/flatMap/foreach/withFilter 等操作时才进行过滤运算
  
  ```scala
  // List(0, 2, 4)
  val filter = list.filter(i => i % 2 == 0)
  // List(1, 3, 5)
  val filterNot = list.filterNot(i => i % 2 == 0)
  // scala.collection.IterableOps$WithFilter
  // foreach: 0 2 4
  val withFilter = list.withFilter(i => i % 2 == 0)
  ```

- splitAt、span、partition、partitionMap、groupBy、groupMap、groupMapReduce、grouped、sliding：将一个集合拆分成多个子集
	- splitAt/span：在某个或第一个不符合某个条件的位置断开，返回 Tuple
	- partition/partitionMap：根据某个条件断开，返回 Tuple
	- groupBy/groupMap/groupMapReduce：将 list 折叠成 Map
	- grouped & sliding：将集合按元素数目拆分成迭代器
    
```scala
// (List(0, 1, 2),List(3, 4, 5))
val splitAt = list.splitAt(3)
// (List(0, 1, 2),List(3, 4, 5))
val span = list.span(i => i == 0 || i % 3 != 0)
// (List(0, 3),List(1, 2, 4, 5))
val partition = list.partition(i => i % 3 == 0)
// HashMap(0 -> List(0, 3), 1 -> List(1, 4), 2 -> List(2, 5))
val groupBy = list.groupBy(i => i % 3)
// Map(0 -> List(0, 11), 1 -> List(1, 100), 2 -> List(10, 101))
val groupMap = list.groupMap(i => i % 3)(i => i.toBinaryString)
// Map(0 -> 0+11, 1 -> 1+100, 2 -> 10+101)
val groupMapReduce = list.groupMapReduce(i => i % 3)
                                        (i => i.toBinaryString)
                                        ((i, j) => s"$i+$j")
// groupedList: Iterator[List[Int]]
//  0: List(1, 2, 3)
//  1: List(4, 5)
val groupedList = list grouped 3
// slidingList: Iterator[List[Int]]
//  0: List(1, 2, 3)
//  1: List(2, 3, 4)
//  2: List(3, 4, 5)
val slidingList = list sliding 3
```

- exists、forall、count：测试或遍历元素
	- exists：列表中存在符合某个条件的元素
	- forall：列表中所有元素都符合某条件
	- count：列表中符合某条件的元素数目
- foldLeft、foldRight、reduceLeft、reduceRight：折叠、展开元素
- sum、product、min、max：进行一些数字或可比较元素的具体操作折叠
- mkString、addString：将集合转换为字符串，addString 方法会将该字符串添加到一个 StringBuilder 中
- view：视图操作，返回一个 SeqView 对象
### Seq

Seq 特征表示序列。有序：从 0 开始；可迭代：length 已知

```scala
val seq0 = Seq(0, 1, 2, 3, 4, 5)
val seq1 = mutable.Seq(-1, -2, 2, 5, 10, 1)
```

- apply、isDefinedAt、indices：索引相关
	- apply：获取第 i 个元素，即 list(i)
- isDefinedAt：索引 i 是否包含在索引表 indices 中
    
    ```scala
    seq0.isDefinedAt(5) // true
    seq0.isDefinedAt(10) // false
    ```

- indices：所有可用索引
  
  ```scala
  seq0.indices // Range 0 until 6
  ```

- length、lengthCompare：长度相关。length 就是 size 的一个别名
- indexOf，segmentLength，startsWith，endsWith，contains，corresponds，search：搜索相关
	- indexOf/lastIndexOf/search：搜索元素第一次/最后一次出现的位置
	- indexOfSlice/lastIndexOfSlice：序列版本
      
      ```scala
      seq0.indexOfSlice(mutable.Seq(2, 3)) // 2
      seq0.indexOfSlice(mutable.Seq(3, 5)) // -1
      ```

- indexWhere/lastIndexWhere：满足要求的版本
- segmentLength：满足要求的连续子序列的最长长度
- contains：列表包含某元素
	- sameElements：两个列表包含相同元素且序列相同，一般与 == 可替代
	- containsSlice：是否包含某序列
	- corresponds：二元谓词测试，将两个列表相同位置的元素作为一对进行测试
- startsWith/endsWith：以某元素开头或结尾
- prepended，append，padTo：增加元素
  - prepended/appended：返回在列表前/后增加元素后的新列表
    - prependedAll/appendedAll：连接列表的版本
  - padTo：在指定位置添加元素后的新序列
- updated，patch，update：对列表的某些值进行替换
  - updated：将列表某位置的元素替换，即使是可变列表也返回改变后的新列表。
  - update：仅可变列表，用于更新列表的元素，list.update(i, a) 相当于 list(i)=a
- sorted，sortWith，sortBy：排序
- reverse，reverseIterator：返回反转的列表或倒序迭代器
- intersect，diff，distinct，distinctBy：集合间的操作
  - intersect：求两集合交集
    
    ```scala
    // List(1, 2, 5)
    seq0.intersect(seq1)
    ```

- diff：求两集合差异
  
  ```scala
  // List(0, 3, 4)
  seq0.diff(seq1)
  ```

- distinct：去重后的列表
- distinctBy：删除元素后的元素
# 并发

Scala 支持 actor 并发模型，以网络访问为例：
- actor：线程池与队列池
	- 使用 `!` 运算符向其中添加任务
- receive：接收消息，即任务结果

```scala
import scala.io._
import scala.actors._
import Actor._

val caller = self
// 假设所有网址存在 urls 列表中
for (url <- urls) {
    actor {
        caller ! (url, Source.fromURL(url).mkString)
    }
}

for (i <- 1 to urls.size) {
    receive {
        case (url, content) => {
            // do something
        }
    }
}
```
