Hello Word：

```python
# hello world  
print("welcome to python.")  
print("Hello world")
```

- [[Python 解释器]]：以交互模式运行 Python 代码及运行 Python 脚本
- [[Python 语句及注释]]：基本的语句（主要是表达式语句）规则及注释
- [[Python 输入输出]]：处理控制台或文件的输入输出
- [[Python 内置类型]]：包括数字，字符串，列表等及其运算
- [[Python 控制语句]]：包括条件，循环，分支等
- [[Python 函数]]：使用 `def` 定义 Python 函数
- [[Python 模块]]：导入到当前环境中，定义在其他文件中的内容
	- [[Python 标准库]]：Python 提供的一系列内置方法和内置类
- [[Python 类]]：Python 中有 类 的概念，使用 `class` 声明
- [[Python 异常处理]]

# 编码风格

以下风格不是强制性规定，但是 Python 约定俗成的指南，遵循 `PEP 8` 风格

[**PEP 8**](https://peps.python.org/pep-0008/)
- 缩进：以 4 个空格为缩进而不是 Tab
- 长度：一行字符个数不应超过 79 个字符
- 空格：用一个空格分隔函数、类以及函数内较大的代码块
- 注释最好单独一行，而不是紧跟语句之后
- 使用文档字符串
- 运算符前后、`,` 之后都要加空格，但括号内紧贴括号部分不要使用空格
- 类名使用 `UpperCamelCase` 命名方法，函数与方法使用 `lowercase_with_underscores` 命名方法，类中函数第一个参数总是 `self`
- 使用 utf-8 或 ascii 等常用编码，不要使用生僻编码
- 类之后空两行，文档结尾空一行