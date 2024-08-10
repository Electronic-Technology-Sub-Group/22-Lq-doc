- 可使用 `def` 声明函数，返回值类型自动推断（相当于 Object）
# 省略语法

- 函数调用时，若需要参数，括号可省略，参数与函数名之间以空格分割
	- 若一个函数没有任何参数，括号不可省略
	- 若是一个函数的参数是另一个函数的结果，需要加括号防止歧义

```groovy
static int add(int a) {
    return a + 1
}

static void main(String[] args) {
    int b = add 10
    println b
    // add 10 加括号防止歧义
    println (add 10)
}
```

- 参数最后一个为闭包时，闭包可以在括号外部；没有其他参数时，括号可省略

```groovy
static int exec(int a, Closure<Integer> op) {
    return op(a)
}

static void main(String[] args) {
    int r = exec(10) {
        return it * 2
    }

    println r
}
```

- 函数返回值为最后一行时，可省略 `return` 关键字

```groovy
static void main(String[] args) {
    println hello()
}

def hello() {
    "Hello"
}
```

- 允许省略异常声明 
# 函数重载

在 Groovy 中，重载函数调用哪一个由运行时的实际类型决定，而不是编译时决定

```java
int method(String arg) { return 1; }
int method(Object arg) { return 2; }

Object o = "object";
// Java：2
// Groovy：1
method(o); 
```

以上代码，如果为 Java 调用，返回值为 2，因为编译时对象 o 类型被显式指定为 Object；而在 Groovy 中调用，返回值为 1，因为 Groovy 调用函数使用的是运行时的类型，该类型为 String
# 具名参数

当方法第一个参数为 Map 时，允许使用映射方式调用，且具名参数位置可选

```groovy
static void func(Map args) {
    println "msg=$args.message, arg=$args.argument"
}
static void func(Map args, int value, String pre) {
    println "[$pre:$value]msg=$args.message, arg=$args.argument"
}

// msg=Msgs, arg=Args
func(message: 'Msgs', argument: 'Args')
// [aaa:5]msg=Msg, arg=Arg
func(message: 'Msg', argument: 'Arg', 5, 'aaa')
// [bbb:5]msg=Msg, arg=Arg
func(5, 'bbb', message: 'Msg', argument: 'Arg')
// [bbb:5]msg=Msg, arg=Arg
func(5, message: 'Msg', argument: 'Arg', 'bbb')
```
# 默认参数

可为一个参数提供默认值

```groovy
static void fun(int a = 100, int b, int c = 1000, int d) {
    println a + b + c + d
}
// 1108：a=100，b=3，c=1000，d=5
fun(3, 5)
// 1009：a=1，b=3，c=1000，d=5
fun(1 ,3, 5)
// 11：a=1，b=2，c=3，d=5
fun(1, 2, 3, 5)
```
