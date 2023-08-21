# 类型系统

- 所有类和成员变量的访问权限默认 `public`，使用 `@PackageScope` 注解表示 package-private

- 可使用 `as` 表示类型强转

```groovy
float a = 10
// as 和 Java 的形式都是可以的
int b = a as int
int c = (int) a
```

- 可以使用类名表示 class 对象

```groovy
// class java.lang.String
println String
```

- 一个文件中，一旦包含任何其他不在类中的代码，将会认为是脚本，详见 [[Groovy 脚本依赖]]
# 鸭子类型

类内部可以直接调用实现类中的方法而无需声明，也可以实现 `methodMissing`，`propertyMissing` 和 `setProperty` 方法，详见 [[Groovy 元编程]]

对于对象，可使用 `as` 将一个未实现某特征的类实现该接口或 [[Groovy Trait|Trait]]，其结果与原对象不是同一个实例

对于对象，可使用 `withTraits` 方法代替 `as`，一次性向对象附着多个特征
# 构造函数
## 位置参数

类似 Java 的构造函数
- 可以通过 TupleConstructor 注解注入构造
- 可通过 new，as 或直接赋予 List 调用构造函数

```groovy
class Person {
    String name
    int age

    Person(String name, int age) {
        // ...
    }
}

// 用法1 与 java 相同
Person p1 = new Person('p1', 1)
// 用法2 由列表分配
Person p2 = ['p2', 2]
// 用法3 使用 as 强转
def msg = ['p2', 2]
Person p3 = msg as Person
```
## 命名参数

可通过映射方式创建对象，也可以通过 MapConstructor 注解注入一个接收 map 的构造

```groovy
@ToString
class Person {
    String name
    int age
}

// Person(p1, 1)
println new Person(name: 'p1', age: 1)
// Person(p2, 2)
println new Person([name: 'p2', age: 2])
```
# 内部类

使用 `new Outer.Inner(outer)` 而不是 `outer.new Inner()` 创建内部类
- Groovy 3 后支持 Java 方式的内部类创建

```groovy
class Outer {
    class Inner {
        def hello() { println "Hello" }
    }
}

Outer a = new Outer()
Outer.Inner b = new Outer.Inner(a)
b.hello()
```
# 字段与属性

- 类型可选，可以是 `def`

- 字段：单个独立的成员变量

- 属性：没有可见性修饰符的字段会自动转换成属性（自动增加 `getter` 和 `setter` 方法）
	- 无访问修饰符，但可以有 `static`，`final`，`synchronized` 等修饰
	- `final` 修饰则不产生 setter
# 接口

- 不支持接口默认实现（default 方法）
	- 自 Groovy 3 后支持 default 方法

- 鸭子类型：若一个类中有接口对应方法，即使没有实现该接口，也可以使用 as 强转为该接口对象
# 注解

- 与 java 注解基本相同，但允许对同一个元素应用多个相同的注解（不需要额外注解）

- 不支持 TYPE_PARAMETER 和 TYPE_USE 元素类型

- 可将闭包作为注解值，类型声明为 Class

- 自定义注解处理器：继承自 `AnnotationCollectorTransform` 类，并在注解上使用 `@AnnotationCollector` 注解指定
# 枚举

与 Java 的枚举基本相同，且可以直接从 String 到枚举转换

```groovy
enum State {
    up,
    down
}
State st = 'up'
assert st == State.up
State switchState(State st) {
    switch (st) {
        case 'up':
            return State.down // explicit constant
        case 'down':
            return 'up' // implicit coercion for return types
    }
}
```
# 静态化
## 静态类型检查

使用 `@groovy.transform.TypeChecked` 注解激活静态类型检查，通过 `@ClosureParams` 注解闭包，完成对闭包的类型检查与推断
- 激活类型推断
- 方法调用在编译期解决
- 可能出现所有静态语言可能的错误：找不到属性、方法等
## 静态编译

使用 `@groovy.transform.CompileStatic` 注解激活静态编译，激活后有如下效果：
- 类型安全
- 性能提升
- metaclass 无效