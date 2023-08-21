特质，类似于 Java 的接口，使用 trait 定义，可被继承/实现/混入，但不能实例化

```scala
trait GreeterTrait {
    def greet(name: String): Unit
}
```

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
# 自类型

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
# 复合类型

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
