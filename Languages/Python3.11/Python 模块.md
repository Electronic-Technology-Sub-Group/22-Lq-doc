- 编写较长程序时，最好将脚本拆分成多个文件
- 不同程序调用同一代码时，不需要重复书写

模块是包含 Python 定义和语句的文件，文件名即模块名（不包含 `.py` 后缀）

# 内置变量

- `__name__` 获取模块名。任何正在运行的模块名都为 `__main__`，否则为模块名（文件名）
	- 可以通过 `if __name__ == "__main__"` 编写测试代码块
- `__file__` 获取文件完整路径，可能为 `None`
- `__all__` 为一个 list，表示使用 `*` 导入时实际导入的成员，默认为所有成员

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

标准库是 Python 自带的一系列标准模块，详见 [[Python 标准库简介]]

- os：提供与操作系统交互的方法
    - getcwd()：获取工作目录（current working directory）
    - chdir()：修改工作目录
    - system()：执行命令
- shutil：日常文件与目录管理任务
    - copyfile()：复制文件
    - move()：移动文件
- glob：在目录中使用通配符搜索文件
    - glob()：使用通配符搜索文件列表
- sys
    - argv：所有执行参数
    - ps1：字符串，命令行交互模式时的主要提示字符串，默认 ">>>"
    - ps2：字符串，命令行交互模式时的辅助提示字符串，默认 "..."
    - path：列表，默认为 PYHTONPATH 环境变量值，包含解释器搜索模块的位置
    - stdin/stdout/stderr：标准 IO 流
    - exit()：退出
- argparse：处理命令行参数
- re：正则工具
- 数学
    - math：对浮点数学的底层 C 库函数访问
    - random：随机选择及随机数
    - statistics：计算数值数据的基本统计属性（均值，中位数，方差等）
    - decimal：十进制浮点数运算
- urllib.request：从 URL 检索数据，网络访问
- smtplib，poplib：收发邮件
    - email：用于管理电子邮件的库，包括 MIME
- datetime：操作日期和时间类
- 数据压缩
    - 支持 zlib，gzip，bz2，lzma，zipfile，tarfile
- timeit：性能测试
- doctest：扫描模块并验证程序文档字符串中嵌入的测试
- unittest：允许在一个单独的文件中维护更全面的测试集
- xmlrpc.client，xmlrpc.server
- 数据序列化
    - 支持 json，csv 等包
    - xml 由 xml.etree.ElementTree，xml.dom，xml.sex 提供支持
- sqlite3：SQLite 数据库包装
- 国际化
    - gettext，local，codecs
- 格式化输出
    - reprlib：提供定制化 repr() 方法
    - pprint：更加复杂的输出方法
    - textwrap：格式化段落
    - locale：处理与特定地域文化相关的数据格式
    - string.Template：模板类。若未提供对应占位符，会抛出 KeyError 异常；使用 safe_substitute() 方法可以在数据缺失时将占位符原样保留
        ```python
        >>> from string import Template
        >>> t = Template('${village}folk send $$10 to $cause.')
        >>> t.substitute(village='Nottingham', cause='the ditch fund')
        'Nottinghamfolk send $10 to the ditch fund.'
        ```
        
- struct：提供 pack() 与 unpack() 方法，处理不定长二进制数据
- threading：线程，对于非顺序依赖的多个任务进行解耦的技术
- logging：日志记录系统
- weakref：弱引用
    ```python
    >>> import weakref, gc
    >>> class A:
    ...     def __init__(self, value):
    ...         self.value = value
    ...     def __repr__(self):
    ...         return str(self.value)
    ...
    >>> a = A(10)                   # create a reference
    >>> d = weakref.WeakValueDictionary()
    >>> d['primary'] = a            # does not create a reference
    >>> d['primary']                # fetch the object if it is still alive
    10
    >>> del a                       # remove the one reference
    >>> gc.collect()                # run garbage collection right away
    0
    >>> d['primary']                # entry was automatically removed
    Traceback (most recent call last):
      File "<stdin>", line 1, in <module>
        d['primary']                # entry was automatically removed
      File "C:/python38/lib/weakref.py", line 46, in __getitem__
        o = self.data[key]()
    KeyError: 'primary'
    ```
    
- 列表
    - array：提供 array() 对象，只能存储类型一致的数据且存储密度更高
    - collections：提供 deque 对象，类似列表，从左端添加/删除速度更快，中间查找较慢
    - bisect：排序
    - heapq：基于常规列表实现堆
- builtins：内置函数和变量名称
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

## 第三方包

使用 pip 可方便的安装第三方包

```bash
pip install 包名
```

可以使用国内镜像提高安装速度

```bash
# 清华源地址为 https://pypi.tuna.tsinghua.edu.cn/simple
pip install -i 镜像地址 包名
```

也可以使用三方源作为默认下载源

```bash
# 升级 pip 到最新版本
python -m pip install --upgrade pip
# 也可以使用三方源更新 pip
# python -m pip install -i https://pypi.tuna.tsinghua.edu.cn/simple --upgrade pip

# 设置源
pip config set global.index-url 镜像地址
# 也可以设置多个源，可以设置 https://mirrors.cernet.edu.cn/list/pypi
pip config set global.extra-index-url "<url1> <url2>..."
```
