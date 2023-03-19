
# 内置方法

### 显式转换

#### toNumber
- `Number(...)`：整体无法转换为数字即返回 `NaN`，结果类似 `+...`
- `parseInt(...)`：转换到不能继续转换成整数为止
- `parseFloat(...)`：转换到不能转换成浮点数为止，常用于过滤单位

![[Pasted image 20230128170032.png]]

#### toString
- `toString(...)`
- `obj.toString([进制])` 可用于按特定进制转换数字到字符串

# 内置类

## Object

方法
- `Object.keys() => []` ：获取所有属性名
- `Object.values() => []` ：获取所有属性值
- `Object.assign(dst, src)`：将 src 对象的内容拷贝到 dst，常用于属性追加（合并对象）

## Array

数组字面量为 `[...]`，且内部元素类型可以不同，类似 `list<void*>` 或 Java 的 `List<Object>`

数组类为 `Array`，`let arr = []` 实际上就是 `let arr = new Array()` 的简写

```javascript
let a = [1, 2, "abc"]
```

属性
- `length`：数组长度

方法
- `push(...)`，`unshift(...)`：向数组后/前添加若干元素并返回数组长度
- `pop()`，`shift()`：删除最后一个/第一个元素并返回元素值
- `splice(start, count=length-start)`：从 `start` 位置删除 `count` 个元素，也可以用于替换数组单元
- `map, forEach, filter` 等不会修改原始数组
- `find, findIndex` 用于查找元素
- `every, some` 用于检查元素是否符合某些条件
- `reduce(function(k, v)[, begin])`：累计处理，`k` 为上次调用的返回结果，`v` 为当前处理的数组值，第一次调用时 `k = begin`
	- `begin` 可以省略，省略时从数组第二个元素开始遍历，`k` 为数组第一个元素
	- 返回处理完最后一个元素后的返回值
- `join(split), concat(arr)`：连接数组元素和数组
- `reverse(), sort()`：翻转，排序，不修改原始数组
- `Array.from(obj)`：将伪数组转化为数组

*伪数组：只有 `0, 1, ..., n` 个可通过索引访问的元素和 `length` 属性的对象，不要求实现方法*

## 封装类

### Number

数字类型封装类

方法
- `toFixed(int)`：保留 n 位小数

### Boolean

布尔类型封装类

### String

字符串封装类，字符串创建时会自动封装成该类

方法
- `split, startsWith, endsWith, indexOf, trim, replace`
- `substring(start[, end])`，`substr(start, len)` 已弃用
- `includes(str), match(reg)`：是否包含 str 及正则匹配
- `toUpperCase, toLowerCase`

## 时间与日期

使用 `Date` 类表示时间与日期
- `new Date()` 获取当前日期与时间
- `new Date(date)` 或 `Date.parse(date)` 可以创建指定时间，`date` 为 ISO 8601 标准写法
- `new Date(long)` 或 `Date.UTC(long)` 可以创建指定时间，`long` 为距 1970.1.1 0:0:0 的毫秒数
- `new Date(year, month, [day], [hours], [minutes], [seconds], [milliseconds])` 创建时间，`month` 取 $[0, 11]$

方法
比较特殊的：
- `getDay()`，`getMonth()`：获取星期、月份，自 0 开始，其中星期中 0 表示星期日
- `getDate()`：日
- `getTime()`：获取 number 类型时间戳（毫秒数）
	- `Date.now()` 可跳过实例化直接获取当前时间戳
	- `+date` 可以利用隐式转换快捷的获取时间戳，其中 `date` 为 Date 类对象

## RegExp

JavaScript 原生支持正则匹配，使用 `/正则语法/` 字面量即可创建正则对象，可直接用于正则匹配
- `test(str)`：匹配，返回 `true` / `false`
- `exec(str)`：搜索，返回一个结果数组，失败则返回 `null`
- `str.replace(reg, 被替换文本)`

具体正则语法见 Tools 部分

## Function

变量
- prototype：原型（用于构造函数继承）

方法：
- `fun.call(thisObj, ...args)`：将 this 修改成 thisObj 并调用
- `fun.apply(thisObj, [] args)`：类似 call，第二个参数必须是数组
- `fun.bind(thisObj, ...args)`：不立即调用函数，返回一个新函数，为原函数绑定 this 和参数后的新函数