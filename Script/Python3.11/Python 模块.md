- 编写较长程序时，最好将脚本拆分成多个文件
- 不同程序调用同一代码时，不需要重复书写

模块是包含 Python 定义和语句的文件，文件名即模块名（不包含 `.py` 后缀）
- 可通过 `__name__` 获取模块名
- 可通过 `__file__` 获取文件完整路径，可能为 `None`

# 导入

使用 `import` 导入模块，导入后可通过模块名访问模块内的成员。导入模块搜索目录包括：
- Python 内置模块，位于 `sys.builtin_module_names`
- 在 `sys.path` 给出的目录中搜索该模块文件
	- 输入脚本的目录或当前目录
	- `PYTHONPATH` 环境变量
	- 依赖于安装位置的默认值，包括 `site-packages` 目录，通过 `site.getusersitepackages()` 查看

```python
# fibo.py
def fib(n):  
    a, b = 0, 1  
    while a < n:  
        print(a, end=' ')  
        a, b = b, a + b  
    print()  
  
  
def fib2(n) -> list:  
    a, b = 0, 1  
    result = []  
    while a < n:  
        result.append(a)  
        a, b = b, a + b  
    return result

```

```python
# main.py
import fibo  

# 0 1 1 2 3 5 8 
fibo.fib(10)
```

若模块内存在可执行代码，将会在第一次 `import` 该文件（或其一部分）时执行

可通过 `from [module] import [members]` 将模块的某些成员导入到当前文件的公共区域

```python
from fibo import fib, fib2  

# 0 1 1 2 3 5 8 
fib(10)  

# [0, 1, 1, 2, 3, 5, 8, 13]
print(fib2(20))

```

若需要将模块所有不以 `_` 开头的成员都导入到公共区域，使用 `*` 即可，但不建议这么用

```python
from fibo import *
```

可通过 `as` 为导入的成员绑定一个别名，可防止出现名称冲突

```python
import fibo as fib_lib
from fibo import fib as fib_fun
```

# 编译

编译后的模块存放于 `__pycache__` 目录，文件名为 `module.pyversion.pyc`

一般情况下，Python 会对比编译版本与源码修改日期以检查是否需要重编译。但有两种情况例外：
- 在命令行中直接载入模块，只会编译，不会存储编译结果
- 仅存在编译后的文件，没有源文件的情况下，编译后的 `.pyc` 文件与其他模块源文件放在一起，且绝不能有源文件

开启 `-O` 或 `-OO` 的情况下可减少编译后的文件大小，`-O` 去掉断言，`-OO` 会去掉断言和 `__doc__`，通过该属性编译后的模块带有 `opt-` 标签

从 `.pyc` 加载的模块不比从 `.py` 加载的模块运行速度快，但加载速度会快一点

`compileall` 模块可以编译一个目录下的所有模块

# 标准库

标准库是 Python 自带的一系列标准模块，详见 [[Python 标准库]]

# dir()

内置的 `dir` 函数可用于查看模块中定义的名称，包括 Python 自动创建的，返回值是一个经过排序的字符串列表

```python
import fibo  

# ['__builtins__', '__cached__', '__doc__', '__file__', '__loader__', '__name__', '__package__', '__spec__', 'fib', 'fib2']
print(dir(fibo))

```

没有传入任何模块时，显示的是当前模块的定义，包括导入到公共区域的模块成员。但 `dir` 不会列出内置函数和变量，这些内容定义在 `builtins` 模块内，通过 `dir(builtins)` 查询

```python
from fibo import fib  

# ['__annotations__', '__builtins__', '__cached__', '__doc__', '__file__', '__loader__', '__name__', '__package__', '__spec__', 'fib']
print(dir())

```

# 包

包是通过 `.` 访问的多层目录，Python 只将带有 `__init__.py` 文件的目录当做包。一般情况下，`__init__.py` 只是一个空文件，但也可以在包里执行一些用于初始化的 Python 代码，或设置 `__all__` 变量

![[Pasted image 20230106102645.png]]

```python
import pkg1.pkg2.hello  

# Hello world
pkg1.pkg2.hello.hello()

```

若对于一个包使用 `*` 导入：`from [package] import *`，并不会将包内所有模块的所有非 `_` 元素导入到当前文件公共区域，而是
- 导入包内 `__init__.py` 的非 `_` 成员并执行其中的初始化代码
- 导入 `__init__.__all__` 列表中的模块

## 子包

通过 `.` 可以以当前模块所在包为定位导入其他包，`..` 为当前包的上级包导入，以此类推

`__init__.__path__` 的列表包含了包所有文件和目录，通过修改此变量可影响根据包搜索模块的结果