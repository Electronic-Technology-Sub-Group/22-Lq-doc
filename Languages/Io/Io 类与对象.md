- Io 自左向右执行，先是对象，之后是消息，表示将消息传递给对象
```io
"Hi ho, Io" print
```

![[Pasted image 20230810084321.png]]
# 对象

Io 是一门原型语言，没有类的概念，创建一个新对象就是复制原有对象，`Object` 是一个根对象
```io
新对象 := 旧对象 clone
```

![[Pasted image 20230810084815.png]]
## 槽

对象是槽的容器。槽 相当于对象的属性，或者可以接受的消息，就如刚刚创建出的对象就自带的 `type`。槽可以直接访问，可以通过 `=` 修改其值。

`:=` 表示创建一个槽，`::=` 表示创建对应槽并创建 setter 方法，修改槽值并返回对象本身

```io
对象名 槽名 := 值
```

![[Pasted image 20230810095148.png]]

```io
对象名 槽名 ::= 值
```

![[Pasted image 20230810111733.png]]

通过 `slotNames` 可以获取对象的所有槽
```io
对象 slotNames
```

![[Pasted image 20230810095411.png]]

通过 `getSlot` 可以获得槽的内容
```io
对象 getSlot("槽名")
```
## 原型

Io 也有基于原型链的继承机制，当对象请求不存在的槽时，会从被复制的对象上寻找

![[Pasted image 20230810100053.png]]
可见 `Vechicle` 中存在 `description` 槽，而从 `Vechicle` 复制的 `Car` 没有。通过 `Car` 访问 `description` 槽，访问的是 `Vechicle` 的槽。

![[Pasted image 20230810100348.png]]
当一个对象以大写开头时，Io 认为其作为原型类型使用，而其他对象则为普通对象。因此，只有首字母大写的对象，Io 会自动向其添加 `type` 槽

通过 `proto` 可以获取一个对象的原型对象

![[Pasted image 20230810102849.png]]

`Lobby` 是主命名空间，所有直接创建的对象都保存在其中

![[Pasted image 20230810103009.png]]
## 方法

使用 `method()` 创建方法
```io
method(方法体)
```

方法也是对象，对象类型为 Block

![[Pasted image 20230810100920.png]]
所以，方法也可以放入槽中，直接给出方法信号即可调用
```
对象 槽名 := method(...)
```

![[Pasted image 20230810102452.png]]

使用 `getSlot` 获得的是方法的内容，而不会直接调用方法

![[Pasted image 20230810103639.png]]

也可以使用 `doMessage(block)` 直接执行 method 对象
# method_missing

当访问不存在的槽时，通常会抛出一个异常。但可以通过重写 `forward` 方法实现自定义代理

![[Pasted image 20230810151930.png]]
# 集合
## 列表

列表 `List` 是 Io 内置原型对象之一，表示任意对象的有序集合，使用 `list()` 创建
```io
对象名 := list(空或多个对象)
```

![[Pasted image 20230810103357.png]]

列表包含多个常用槽：
![[Pasted image 20230810103807.png]]
![[Pasted image 20230810110214.png]]
## 散列表

散列表即 `Map`
```
对象名 := Map clone
```

![[Pasted image 20230810110451.png]]
- at：获取元素
- atPut：插入或更新元素
# 单例

`true`，`false`，`nil` 等对象都是单例，即每次 clone 获取的都是该对象本身

![[Pasted image 20230810110835.png]]

要实现单例也很简单，只要重定义下 `clone` 槽即可

```
对象 clone = 对象本身
```

![[Pasted image 20230810111049.png]]