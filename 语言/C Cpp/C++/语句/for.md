---
语言: cpp
语法类型: 基础语法
---
`for ( <条件>; <迭代表达式> ) <循环体>`

`for (<迭代声明>: <可迭代对象>) <循环体>`
- #cpp20 迭代声明之前可以添加[[初始化语句]]
* 迭代声明：声明被迭代值，常用 `auto` 声明
* [[可迭代对象]]：数组、元组、STL 中的 `vector`、`set`、`map` 等容器都是可迭代对象

范围 for 循环可以看作以下代码的语法糖：

```cpp
auto && __range = range_expression;
// 区别在这里：__begin 与 __end 类型可以不同
auto __begin = __range.begin();
auto __end = __range.end();
for (; __begin != __end; ++__begin) {
    range_expression = *__begin;
    loop_statement
}
```
