Io 没有过多的语法糖，很多其他语言中作为语法形式出现的功能都以函数（方法）的形式出现
# 循环

- loop：无限循环
```io
loop(循环语句)
```

- while：条件循环
```io
while(判断条件, 带发送者的消息)
```

![[Pasted image 20230810112720.png]]
- for：条件循环，for 循环会自动处理循环变量，不需要提前声明
```io
for(循环变量, 起始值, 终止值, 带发送者的消息)
for(循环变量, 起始值, 终止值, 步长, 带发送者的消息)
```

![[Pasted image 20230810112936.png]]
# 条件

```io
if(条件, true code, false code)
if(条件) then(true code) else (false code)
```
# 运算符

不同于其他循环指令，运算符属于语法糖。在 Io 中，可以使用 `OperatorTable` 查看所有运算符及其优先级，以及自定义运算符的方法
![[Pasted image 20230810133652.png]]

运算符的是语法糖为：`a + b` 相当于 `a +(b)`

```io
// 自定义运算符
OperatorTable addOperator("xor", 11)
// 实现运算符
true xor := method(bool, if(bool, false, true))
false xor := method(bool, if(bool, true, false))
```

![[Pasted image 20230810135004.png]]

![[Pasted image 20230810135032.png]]
# 消息反射

Io 中一切行为都是消息的传递。消息分为三部分：发送者 `sender`，目标 `target`，参数 `argument`。对于任意一个消息，都可以通过 `call` 获取消息的元信息。

1. 测试环境： `postOffice` 对象，该对象 `packageSender` 槽行为用于获取消息的发送者

```io
postOffice := Object clone
postOffice packageSender := method(call sender)
```

2. 测试行为：`mailer` 对象，该对象 `deliver` 槽方法向 `postOffice` 发送 `packageSender` 消息

```io
mailer := Object clone
mailer deliver := method(postOffice packageSender)
```

![[Pasted image 20230810140522.png]]
- `sender`：消息的发送者对象（若不通过对象直接调用，发送者为全局上下文 `Lobby`）
- `target`：消息的接收者对象（若不通过对象直接调用，接收者为全局上下文 `Lobby`）
- `message`：消息信息
	- `message name`：消息名
	- `message arguments`：参数列表（List）
		- `message argAt(i)`：第 i 个参数

```io
postOffice packageSender := method(call sender)
postOffice messageTarget := method(call target)
postOffice messageArgs := method(call message arguments)
postOffice messageName := method(call message name)
postOffice messageFirstArg := method(call message argAt(0))
```

借助该元信息，同时配合 `doMessage` 方法，实现 `unless` （if 的反义词）的方法如下：

```io
unless := method(
    (call sender doMessage(call message argAt(0)))
        ifFalse(call sender doMessage(call message argAt(1)))
        ifTrue(call sender doMessage(call message argAt(2)))
)
```
