# 流程控制

大部分与 Java 相同，不同之处有：
- switch：兼容 Java，且可以支持更多类型参数

  ```groovy
  def x = 1.23
  def result = ""
  
  switch ( x ) {
      // 类实例：实例匹配
      case "foo":
          result = "found foo"
      case "bar":
          result += "bar"
      // 集合：使用 in 匹配
      case [4, 5, 6, 'inList']:
          result = "list"
          break
      case 12..30:
          result = "range"
          break
      // 类型匹配
      case Integer:
          result = "integer"
          break
      case Number:
          result = "number"
          break
      // 正则：toString() 正则匹配
      case ~/fo*/:
          result = "foo regex"
          break
      // 表达式；可使用 it 代替 x
      case { it < 0 }: // or { x < 0 }
          result = "negative"
          break
      default:
          result = "default"
  }
  
  assert result == "number"
  ```

- 逻辑判断中，一个对象可通过 `asBoolean()` 方法隐式转换为布尔值。常见类型默认实现如下：
	- Collection/String：!isEmpty()
	- Matcher：至少有一个匹配
	- Iterator/Enum：hasNext()
	- Number：!= 0
	- Object：!= null
# 异常处理

- 异常处理中 catch 块可以简化：类型可以省略，表示接收剩余所有异常

- 函数方法可省略异常声明
