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

# 类型与变量

## 变量

使用 `let` 声明，不要求声明变量类型且可以被赋值不同类型数据

*尽量使用 let 不要用 var，也不要不声明直接使用变量，会产生声明提升，全局变量等问题*

```javascript
var a = 10 // 直接初始化
let b
let c, d = "abc"
```

## 类型

JavaScript 主要分基本数据类型：`number`，`string`，`boolean`，`undefined`，`null`，引用数据类型 `object`，`function`，`array`

### 基本数据类型

- `undefined` 表示未定义数据，当一个声明但未被赋值时为该值，而 `null` 表示一个被赋值为空的对象
- `number` 类型包含整数和小数在内的所有数字，以及特殊数字 `NaN`，`Infinity` 等
	- `NaN` 与任何数字运算结果都是 `NaN`，与任何数字比较都是 `NaN`，包括 `(NaN==NaN) // false`
- `string` 字面量允许使用 `""`，`''` 或 \` \` 声明，但需要成对使用
	- 使用 \` \` 声明的字符串支持字符串模板和多行文本块，使用 `${}` 引用代码
- `boolean` 字面量为 `true` 和 `false`

### 对象

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

更详细见 [[JS 类与对象]]

### 数组

数组字面量为 `[...]`，且内部元素类型可以不同，类似可用任意类型的 `vector` 或 Java 的 `List<Object>`

```javascript
let a = [1, 2, "abc"]
```

属性
- `length`：数组长度

方法
- `push(...)`，`unshift(...)`：向数组后/前添加若干元素并返回数组长度
- `pop()`，`shift()`：删除最后一个/第一个元素并返回元素值
- `splice(start, count=length-start)`：从 `start` 位置删除 `count` 个元素

*伪数组：只有 `0, 1, ..., n` 个可通过索引访问的元素和 `length` 属性的对象，不要求实现方法*

## typeof

`typeof` 关键字用于返回变量类型，使用方法为：`typeof 变量名`，返回值为字符串，特例： `null` 返回 `object`
![[Pasted image 20230128164756.png]]

## 运算符

与其他语言基本相同
- `==` / `!=`：比较两个值是否相等，若**类型不同时**转换成字符串再比较
- `===` / `!==`：比较两个值及其类型是否相等
- `/` 除法不是整数除法，而是浮点除法

## 隐式转换

表单，`prompt` 等默认获取的数据都是字符串类型，运算时需要转换成需要的类型

*运算符隐式转换规则：*
- 数字运算转换规则
	- `+` 运算符表示为加法运算时，两个运算数只要有一个为字符串或引用类型，都将自动转换成字符串
		- `+` 表示正号时会自动将后面的值转换成数字
	- 除 `+` 外其他算术运算符会将非数字类型转换成数字类型
- `==` 运算转换规则
	- 对于单个变量的判断时，只有 `null`，`undefined`，`NaN`，`0`，`""` 转换为 `false`，其余均为 `true`
	- `NaN`，`null`，`undefined` 参与的与其他值的比较均为 `false`，例外：
		- `null == undefined` 为 `true`
		- `NaN == NaN` 为 `false`
	- `boolean` 与任何值比较，先将 `boolean` 转化成 `number`
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
- 传参时，若实参个数与形参不匹配，未被赋值的形参值为 `undefined`，超出的参数被舍弃
	- 函数内可通过 `arguments` 访问传入的所有参数，该伪数组类似数组但没有插入删除方法
	- 函数支持参数默认值
- 可以使用 `return` 返回任意类型数据，若没有则返回 `undefined`

函数可以在任何位置声明，也可以被 return 返回，或作为实参传入其它函数

函数本身也是一种数据类型（类似 C 函数指针？）可直接赋值给变量，直接定义的函数其函数名也是函数变量本身，类型为 `function`，可直接使用 `()` 调用

- 使用 `function(形参列表) {...}` 可创建一个匿名函数，多用于参数传递和赋值
![[Pasted image 20230129111406.png]]
- 使用 `(function(){...})();` 或 `(function(){...}());` 的形式可以创建立即执行的函数，即创建一个匿名函数并在声明时立即执行，用于避免全局变量之间的污染
	- *注意：立即执行函数前后如果还有代码，必须有 `;`*
- `this`：上下文对象，一般来说谁调用对象指向谁
	- 浏览器中，最外层 this 一般指向 `window` 对象

## 内置方法

### 显式转换

#### toNumber
- `Number(...)`：整体无法转换为数字即返回 `NaN`，结果类似 `+...`
- `parseInt(...)`：转换到不能继续转换成整数为止
- `parseFloat(...)`：转换到不能转换成浮点数为止，常用于过滤单位

![[Pasted image 20230128170032.png]]

#### toString
- `toString(...)`
- `obj.toString([进制])` 可用于按特定进制转换数字到字符串

### 定时器

- `id = setInterval(func, delay)`：每隔 `delay` ms 调用一次 `func`，返回该定时器 id 非 0
- `clearInterval(id)`：停止一个定时器


