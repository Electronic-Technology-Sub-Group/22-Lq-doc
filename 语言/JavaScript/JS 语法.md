# 注释

```javascript
// 单行注释

/*
块注释
*/
```

# 语句
 
- JS 中语句末 `;` 可省略，省略后回车相当于换行
- 常用输入输出 API：
	- 输出
		- `document.write(string)`：输出到网页 body 中，支持 HTML 标签
		- `alert(object)`：网页提示框
		- `console.log(object)`：输出到控制台
	- 输入
		- `prompt(string)`：提示框输入，返回一个字符串
- 一般来说，JS 按自上而下运行；例外：
	- var 或无声明变量会提前到作用域内其他 JS 语句之前，所有 var 声明的变量都会位于当前作用域最前面
	- alert，prompt 会提前到所有 JS 和渲染之前

# 类型与变量

## 变量

使用 `let` 声明，不要求声明变量类型且可以被赋值不同类型数据，除 `_` 外还可用 `$` 符号

*尽量使用 let 不要用 var，也不要不声明直接使用变量，会产生声明提升，全局变量等问题*

```javascript
var a = 10 // 直接初始化
let b
let c, d = "abc"
```

使用 `const` 声明常量，且声明时必须赋值

## 类型

JavaScript 主要分基本数据类型：`number`，`string`，`boolean`，`undefined`，`null`，引用数据类型 `object`，及内置的 `function`，`array` 等

### 基本数据类型

- `undefined` 表示未定义数据，当一个声明但未被赋值时为该值，而 `null` 表示一个被赋值为空的对象
- `number` 类型包含整数和小数在内的所有数字，以及特殊数字 `NaN`，`Infinity` 等
	- `NaN` 与任何数字运算结果都是 `NaN`，与任何数字比较都是 `NaN`，包括 `(NaN==NaN) // false`
- `string` 字面量允许使用 `""`，`''` 或 \` \` 声明，但需要成对使用
	- 使用 \` \` 声明的字符串支持字符串模板和多行文本块，使用 `${}` 引用代码
- `boolean` 字面量为 `true` 和 `false`

### 对象基础

`Object` 表示一个普通的空类，`let obj = {}` 实际上就是 `let obj = new Object()` 的简写

对象通过 `{}` 创建，有点类似于 `Map<Object, Object>`，无序：

```javascript
let empty = {}
let obj = {
    // 属性
    key1: value1,
    key2: value2,
    /// 方法
    key3: function() {...}
    ...
}
```

key，value 可以是任意类型，当 key 为 string 时，且为合法不冲突标识符时可省略引号
*key 通常为 string 或 Symbol 类型*

- 对象成员使用 `.key` 或 `[key]` 访问、修改和增加，使用 `.` 只能访问可省略引号的字符串或 Symbol 类型成员，而 `[]` 可以用于任意类型
- 使用 `delete obj.key` 或 `delete obj[key]` 删除对象成员
- 通过 `console.dir(obj)` 方法获取对象所有 `key`

更多详见[[#对象]]

## typeof

`typeof` 关键字用于返回变量类型，使用方法为：`typeof 变量名` 或 `typeof(变量名)`，返回值为字符串，特例： `null` 返回 `object`
![[Pasted image 20230128164756.png]]

## 运算符

与其他语言基本相同
- `==` / `!=`：比较两个值是否相等，若**类型不同时**转换成字符串再比较，详见[[#隐式转换]]
- `===` / `!==`：比较两个值及其类型是否相等
- `/` 除法不是整数除法，而是浮点除法

其他
- 展开运算符：`...arr`，可将运算符后的数组展开，常用于函数传参时依次传入数组中的所有值。或合并数组等

```javascript
let arr1 = [1, 2, 3]
let arr2 = [4, 5, 6]
// 传参，等同于 Math.max(arr1[0], arr1[1], arr1[2])
let max = Math.max(...arr1)
// 合并，等同于 [arr1[0], arr1[1], arr1[2], arr2[0], arr2[1], arr2[2]]
let arr = [...arr1, ...arr2]
```

## 隐式转换

表单，`prompt` 等默认获取的数据都是字符串类型，运算时需要转换成需要的类型

*运算符隐式转换规则：*
- 数字运算转换规则
	- `+` 运算符表示为加法运算时，两个运算数只要有一个为字符串或引用类型，都将自动转换成字符串
		- `+` 表示正号时会自动将后面的值转换成数字
	- 除 `+` 外其他算术运算符会将非数字类型转换成数字类型
	- `""`, `null` 隐式转化为 number 时为 0，undefined 为 NaN
- 对于单个变量转换为 bool 时，只有 `null`，`undefined`，`NaN`，`0`，`""` 转换为 `false`，其余均为 `true`
- `==` 运算转换规则
	- `NaN`，`null`，`undefined` 参与的与其他值的比较均为 `false`，例外：
		- `null == undefined` 为 `true`
		- `NaN == NaN` 为 `false`
	- `boolean` 与任何值比较，先将 `boolean` 转化成 `number` (0/1)
	- `string` 与 `number` 比较，会将 `string` 转换成 `number` 比较
	- 基本数据类型与引用类型比较，通过[[ToPrimitive 规则.js]]转换成基本数据类型后再比较

# 控制语句

分支的 if-else 结构，switch 结构与 C 类似
循环的 `while`，`for` 循环与 C 类似
`break`，`continue` 与 C 语言类似，没有 `goto`

## for-in

使用 `for(let k in obj)` 可遍历对象 `key`，或数组索引
![[Pasted image 20230129140309.png]]

# 函数

函数使用 `function 函数名(形参列表) {...}` 声明，不需要标明返回值和形参类型
- 可以使用 `return` 返回任意类型数据，若没有则返回 `undefined`
- `this`：环境对象，一般为调用该函数的对象

函数可以在任何位置声明，也可以被 return 返回，或作为实参传入其它函数

函数本身也是一种数据类型（类似 C 函数指针？）可直接赋值给变量，直接定义的函数其函数名也是函数变量本身，类型为 `function`，可直接使用 `()` 调用

- 使用 `function(形参列表) {...}` 可创建一个匿名函数，多用于参数传递和赋值
![[Pasted image 20230129111406.png]]
- 使用 `(function(){...})();` 或 `(function(){...}());` 的形式可以创建立即执行的函数，即创建一个匿名函数并在声明时立即执行，用于避免全局变量之间的污染
	- *注意：立即执行函数前后如果还有代码，必须有 `;`*
	- 还有一种写法，`[op]function(...){...}(...)`，其中 `[op]` 表示任意二元运算符，常使用 `!`
- `this`：上下文对象，一般来说谁调用对象指向谁
	- 浏览器中，最外层 this 一般指向 `window` 对象

## 函数参数

函数支持参数默认值

传参时，实参与形参不需要严格匹配。具体规则如下：
- 未被赋值的形参值为 `undefined`，可通过 `||` 运算符为 `undefined` 的变量赋默认值
- 函数内可通过 `arguments` 访问传入的所有参数，该伪数组类似数组但没有插入删除方法

### 剩余参数

```javascript
function sum(a, b, ...other) {
	// ...
	// other 是个数组
}
```

当形参形如 `...形参名` 类型时，表示该参数为剩余参数
- 形参名代表的变量是一个真实的数组
- 剩余参数只能位于形参列表最后

## this

一般来说，this 指向调用函数的对象，直接调用则为全局对象（浏览器中为 window）

## 闭包

内层函数中可以访问外层环境的变量，函数本身持有了外部某些变量的引用

闭包主要表示了回调函数使用的外部变量是其引用而不是拷贝，可实现变量的私有化

```javascript
function counter() {
    let count = 0
    return function() {
        return count++
    }
}

let c = counter()
c() // 0
c() // 1
```

以上代码片段中，count 变量存在于 counter 作用域中，可被内部的函数访问，但无法被 counter 外部的函数访问和修改。每次调用 `counter()` JS 都会创建一个新的上下文和新的 count

**问题：返回的函数若没有及时销毁，可能造成内存泄漏**

## 箭头函数

类似 lambda 表达式？用于替代匿名函数的情况
- 具有闭包的性质
- 无 this，arguments 等函数内置变量（根据作用域链，沿用上层函数 this）

```javascript
(形参列表) => {
    // 函数内容
}
```

- 当形参只有一个时，可省略括号
- 当只有一条语句时，可省略大括号，返回语句的执行结果
- 允许直接返回一个对象，但要被 `()` 包裹，完整形如 `() => ({...})` 的格式

# 解构

解构类似其它语言中的模式匹配，在两侧形式相同时（数组，对象），可以批量为左侧的元素赋值。

```javascript
// 需要被赋值的变量
let a, b, c

let arr = [10, 20, 30]
// 用于数组的解构赋值
[a, b, c] = arr

let obj = {
    a: 10,
    b: 20,
    c: 30
}
// 用于对象的解构赋值
// 变量名与对象中的对应属性名必须一致
{a, b, c} = obj
// 或者进行更名
// 此处为 c 赋值 cc 的变量，需要进行一次更名，保证两侧形式的一致性
{a, b, cc: c} = {a: 10, b: 20, cc: 30}
```

应用样例：交换变量

```javascript
let a = 10
let b = 20; // 此处 ; 必须加，防止 obj[...] 的语法歧义

// swap
[a, b] = [b, a]
```

解构也可以在声明变量中使用

```javascript
let [a, b] = [10, 20]
```

缺少的值默认为 `undefined`，超出的值丢弃，可通过[[#剩余参数]]的形式接收

```javascript
// 剩余参数, k = [30, 40, 50, 60, 70, 80, 90, 100]
let [m, n, ...k] = [10, 20, 30, 40, 50, 60, 70, 80, 90, 100]
```

在解构赋值时，可以跳过某些值

```javascript
// a = 10, b = 30, 20 被跳过
let [a, , b] = [10, 20, 30]
```

解构支持多维数组、对象数组、多级对象等，只要两侧形式一致即可

解构也支持函数传参。形参列表实际上类似省略了 let 的变量声明过程

```javascript
function fun({data: myData}) {
    // myData 即传入对象的 data 属性
}

fun({
    code: 200,
    data: [...]
})
```

# 对象

对象的一些基本用法见[[#对象基础]]

## 创建对象

- 字面量
- new Object() 或其他构造函数

## 构造函数

一种特殊的函数，用于初始化对象，通常约定为大写字母开头

构造函数也是一个普通函数，通常配合 `new` 关键字使用，该过程称为实例化。实例化过程类似于：
1. new 关键字会创建一个空对象，以下以 obj 代替
2. 将 obj 作为构造函数的 this，调用构造函数
3. 返回 obj

## 静态成员

在构造函数中定义的属性和方法称为静态成员，只能使用构造函数来访问
- 构造函数本身也是一个普通函数，函数是一类对象，因此可以包含自己的属性和方法
- 构造函数对应的函数对象的属性和方法不会自动的拷贝给由他创造出的对象

## 原型

### 原型对象

构造函数通过原型分配共享数据和共享方法，每个构造的 `prototype` 属性对应的对象在所有由其创建的对象中共享

构造函数和原型对象的 this 都指向新对象

### constructor

每个默认的 `prototype` 对象都有一个 `constructor` 属性，指向所属的函数对象（构造函数）

### 对象原型

每个实例化的对象都有一个 `__prop__` 属性，指向构造函数的 `prototype` 对象

**`__prop__` 非标准 JS 属性，有些地方也显示为 `[[prototype]]`**

```javascript
function Obj() {}
Obj.prototype.constructor === Obj // true

let obj = new Obj()
obj.__proto__ === Obj.prototype // true
```

### 原型继承

```javascript
function Parent() { /* ... */}

function Child() { /* ... */ }

// 原型继承
Child.prototype = new Parent()
Child.prototype.constructor = Child
```

原型链：查找属性/方法时，若当前对象中不存在，会从原型对象中查找，以此类推

`instanceof`：查找每一个原型对象的 constructor 对象，判断其是否包含给定构造函数

### 组合继承

原型继承不会继承父类的成员变量，只继承了原型（通常是方法）。可以在原型继承的基础上，通过调用父类的构造将默认成员复制过来，称为组合继承

```javascript
function Parent() { /* ... */}

function Child() {
	// 组合继承
	Parent.call(this, ...)
	// do something
}

// 原型继承
Child.prototype = new Parent()
Child.prototype.constructor = Child
```

# 异常处理

- 抛异常：`throw 异常信息`，通常来说异常信息使用 `new Error(msg)` 包含栈信息
- 捕获：`try { ... } catch(msg) { ... } finally { ... }`
- `debugger`：关键字，通知浏览器添加一个调试断点
