# 解释器

```bash
python -c [command] [args]
```

该命令将运行一条 Python 语句，其中 `[command]` 为一条合法的 Python 语句，`[args]` 为其参数

```bash
python -m [module] [args]
```

该命令用于执行一个 Python 模块，其中 `[module]` 为模块名，`[args]` 为参数

模块可接受
- 有效绝对模块名，但不需要 `.py` 扩展名
- 包名，包括命名空间包，此时将以 `<pkg>.__main__` 作为主模块执行

```bash
python [file]
```

`[file]` 指向一个 Python 脚本，将直接执行该脚本；或指向一个目录或 `zip` 包，将执行 `__main__.py` 

更多详细见[[Python 解释器]]

# Python 语法

从 Hello Word 开始：

```python
# hello world  
print("welcome to python.")  
print("Hello world")
```

- [[Python 注释]]：使用 `#` 标记该行剩余部分为注释。文档开头的注释可能会有特殊用处
- Python 每一行语句末尾不需要 `;` 等任何标记
- 缩进是 Python 的语句组织方式，通过缩进来区分多个不同的代码块作用域，相同缩进相当于 C 的同一个代码块 `{}`
- 变量直接用，不需要声明
	- 多重赋值：可以同时为多个变量进行赋值，使用 `,` 分隔：`a, b = b, a+b`
- [[Python 输入输出]]：使用 `print` 输出内容
	- `end` 可用于替换换行符：`print(sth, end=',')`
- [[Python 类型]]：包括数字，字符串，列表等及其运算
- [[Python 控制语句]]：包括条件，循环，分支等
- [[Python 函数]]：Python 支持函数，使用 `def` 定义
- [[Python 模块]]：导入到当前环境中，定义在其他文件中的内容
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