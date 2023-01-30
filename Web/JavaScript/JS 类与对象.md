# 内置类

## Object

`Object` 表示一个普通的空类，`let obj = {}` 实际上就是 `let obj = new Object()` 的简写

## 数组

数组类为 `Array`，`let arr = []` 实际上就是 `let arr = new Array()` 的简写

## 封装类

### Number

数字类型封装类

### String

字符串封装类

方法
- `trim()`：去除首尾空格

## 时间与日期

使用 `Date` 类表示时间与日期
- `new Date()` 获取当前日期与时间
- `new Date(date)` 或 `Date.parse(date)` 可以创建指定时间，`date` 为 ISO 8601 标准写法
- `new Date(long)` 或 `Date.UTC(long)` 可以创建指定时间，`long` 为距 1970.1.1 0:0:0 的毫秒数
- `new Date(year, month, [day], [hours], [minutes], [seconds], [milliseconds])` 创建时间，`month` 取 $[0, 11]$

方法
比较特殊的：
- `getDay()`：获取星期，自 0 开始，0 表示星期日
- `getTime()`：获取 number 类型时间戳
	- `Date.now()` 可跳过实例化直接获取当前时间戳
	- `+date` 可以利用隐式转换快捷的获取时间戳，其中 `date` 为 Date 类对象