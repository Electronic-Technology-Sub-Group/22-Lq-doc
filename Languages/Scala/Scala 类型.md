# Any

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
## AnyVal

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
## AnyRef

一切引用类型的基类，所有非 AnyVal 的类都是该类的子类，Jvm 环境下与 `Object` 类对应
## Nothing

Nothing 为所有类型的子类，也称 bottom type，常用于非正常终止，如抛出异常/无限循环
## Null

Null 为所有 AnyRef 类型的子类，有一个单值，由 null 关键字定义。该类用于兼容其他 JVM 平台语言，几乎不应在 Scala 代码中使用
# 泛型

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
## 协变与逆变

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
## 上下界

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
# 隐式转换

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
# 类型成员

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

类型成员与泛型基本等效，可与泛型互换

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
