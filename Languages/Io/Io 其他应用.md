# DSL

使用 Io 可以快速实现 DSL 的解析。以下代码为一个解析以下格式文件的代码：

```
{
    "name": "phone number"
}
```

DSL 解析代码

```io
// 当遇到大括号时，自动执行 --> 解析 {}
curlyBrackets := method(
    r := Map clone
    call message arguments foreach(arg, r doMessage(arg))
    r
)
// 自定义运算符 “:” --> 解析 "":""
OperatorTable addAssignOperator(":", "atPutNumber")
Map atPutNumber := method(
    self atPut(
        call evalArgAt(0) asMutable removePrefix("\"") removeSuffix("\"")
        call evalArgAt(1)
    )
)
// 从文件加载数据并执行转换
s := File with ("phonebook.txt") openForReading contents
phoneNumbers := doString(s)
phoneNumbers   keys println
phoneNumbers values println
```
# 并发

Io 并发主要由协程、actor、future 组成
## 协程

协程是 Io 并发的基础，是进程的自发挂起和恢复的机制，表现为特定的 `method`（异步消息）
- 以 `yield` 表示挂起当前过程
- 使用 `@方法名` 或 `@@方法名` 触发异步消息，有不同返回值
	- `@`：返回 `future`
	- `@@`：返回 `nil` 并在自身线程中触发消息（actor）

```io
vizzini := Object clone
vizzini talk := method(
    "Fezzik, are there rocks ahead?" println
    yield
    "No more rhymes now, I mean it." println
    yield
)

fezzik := Object clone
fezzik talk := method(
    yield
    "If there are, we'll all be dead." println
    yield
    "Anybody want a peanut?" println
)

vizzini @@talk
fezzik @@talk

Coroutine currentCoroutine pause
```

其运行顺序为：

```
// vizzini
Fezzik, are there rocks ahead?
// fezzik
If there are, we'll all be dead.
// vizzini
No more rhymes now, I mean it.
// fezzik
Anybody want a peanut?
// Coroutine currentCoroutine pause
Scheduler: nothing left to resume so we are exiting
```
## actor

`actor` 是 Io 中通用并发原语。通过异步消息机制，实现多个线程之间的有序执行

```io
slower := Object clone
slower start := method(wait(2); writeln("slowly"))

faster := Object clone
faster start := method(wait(1); writeln("faster"))

// faster
// slowly
slower @@start; faster @@start; wait(3)
```
## future

通过 `@` 访问返回一个 `feature` 对象。该对象是函数返回结果的引用。当使用 `feature` 时，Io 会阻塞直到函数执行完成。