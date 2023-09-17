# 命令行

在 Python 中直接执行命令行指令可以通过 `os` 包或 `subprocess` 包实现，`subprocess` 是 Python 引入的新模块（2.4，也不咋新了），用以替代一些旧模块的功能，推荐使用该包

## os

- `os.system('...') -> int`：创建子线程并执行命令
	- 返回执行状态码，0 表示成功，1 表示失败，256 表示无返回结果
	- 若在命行中执行，运行结果直接输出到命令行，但在程序中无法拿到输出结果
- `os.popen('...', mode) -> IO`：以管道形式在子线程运行命令
	- `mode` 类似打开文件的 `mode`，默认是 `'r'`，可调用 `read()` 等方法拿到输出内容

## subprocess

使用子线程运行命令的新 API，通过 `input`，`output`，`error` 管道获取返回信息，便于线程控制和结果监控

一般可以直接使用 `run` 方法，也可以使用其中一些类的构造以达到更精细的控制

方法：

- `subprocess.run(args, *, stdin, input, stdout, stderr, capture_output, ..., timeout, ...)`
	- `arg`，`*`：执行的指令，后面是参数或可以省略
	- `stdin`，`stdout`，`stderr`：标准输入、输出、异常流，默认 `stdin` 为键盘，其余使用 `PIPE` 初始化
	- `capture_output`：合并输出与异常流，不应与三个流设置同时出现。等效于 `stdout=PIPE, stderr=STDOUT`
	- `timeout`：运行超时设置，超时后产生 `TimeoutExpired` 异常

类：

- `CompletedProcess`：当一个线程结束（指令运行完成）时返回
	- `args`：执行的参数
	- `returncode`：一般来说 0 表示正常，POSIX 下 `-N` 表示被信号 N 中断
	- `stdout`，`stderr`：输出流
- `Popen`：线程创建与执行管理，相比 `run` 方法更灵活，可以配合 `with` 使用

变量：

- `DEVNULL`，`PIPE`，`STDOUT`：输出流可选项

# 本地代码

[ctypes --- Python 的外部函数库 — Python 3.11.4 文档](https://docs.python.org/zh-cn/3.11/library/ctypes.html)

执行本地代码即执行外部函数库。Python 自动将 Python 类型与 C 类型进行映射，主要使用 `ctypes` 库

## 载入

使用 `ctypes.cdll` 可以以 `cdecl` 约定导入 C 的动态链接库，使用 `windll` 或 `oledll` 可以导入 `stdcall` 约定调用的动态链接库，导入失败则产生 `OSError` 异常

```python
# 使用 LoadLibrary 导入
from ctypes import cdll
dll_or_so = cdll.LoadLibrary('动态链接库路径')

# 也可以创建 CDLL 类
from ctypes import CDLL
dll_or_so = CDLL('动态链接库路径')
```

在 Windows 下，Windows 可以自动补全 `.dll` 后缀名，因此可以有更简单的写法
- `ctypes.windll.kernel32`：导入 Windows 的 kernel32.dll
- `ctypes.cdll.msvcrt`：导入微软 C 标准库的 msvcrt.dll

导入后，可以直接使用成员名访问，也可以使用 `getattr` 访问，访问不存在的成员时产生 `AttributeError` 异常

```python
# 调用 windll.kernel32.GetModuleHandleA
import windll from ctypes
kernel32 = windll.kernel32
kernel32.GetModuleHandleA( ... )

# 访问 msvcrt.dll 的 ??2@YAPAXI@Z 成员
import cdll from ctypes
msvcrt = cdll.msvcrt
value = getattr(msvcrt, "??2@YAPAXI@Z")
```

## 类型

### 简单类型映射

| Python        | C 类型                                               | ctypes 类型                                |
| ------------- | ---------------------------------------------------- | ------------------------------------------ |
| `bool`        | `_Bool`                                              | c_bool                                     |
| 单字符字符串  | char, wchar_t                                        | c_char, c_wchar                            |
| int           | 从 char 到 long long 的有无符号数字, size_t, ssize_t | c_byte 到 c_ulonglong, c_size_t, c_ssize_t |
| float         | float, double, long float                            | c_float, c_double, c_longdouble            |
| 字符串或 None | `char*`, `wchar_t*`                                  | c_char_p, c_wchar_p                        |
| int 或 None   | `void*`                                              | c_void_p                                   |
| None          | void                                                 | None                                       | 

- 整数、字符串可以直接传入，其他必须使用 `ctype.ctypes类型(值)` 创建，通过 `value` 属性访问和修改其具体值
- 指针类型（`c_xxx_p`）修改的是指针的内存地址
- 可以通过 `ctypes.create_string_buffer()` 创建可变的内存块，其内容可通过 `raw` 获取和修改

### 类作为参数

若需要传入自定义的 Python 类对象，使用 `ctype.ctype类型` 进行转换时转换的是对象的 `_as_parameter_` 属性。若需要即时计算，可以使用 `property` 函数创建

```python
class AClass:
	def __init__(self):
		self._as_parameter_ = 5
```

如果需要直接传入类对象，可以实现 `from_param(v)` 函数，其中接受的参数为待转换对象

### 指针或引用

`ctypes.POINTER(类型)(值)` 函数可以将数据转化为其指针，`ctypes.BYREF(类型)(值)` 可以创建数据的引用。

使用 `BYREF` 的效率会更高一些，因为 `POINTER` 会创建出一个真实的指针对象，但 `BYREF` 无法在 Python 中拿到这个对象的指针。

`POINTER` 创建出的指针可以通过 `contents` 属性获取指向的对象。但注意的是该对象并非原始对象，而是原始对象的一个副本。指针对象也可以使用下标索引，其行为类似 C。`NULL` 指针可隐式转换为 False，对 `NULL` 解引用会产生 `ValueError` 异常

`ctypes.cast(指针类型, 其他类型)` 可将一个指针指向的实例转换成另一种 `ctype` 类型并返回第二个类型的实例。两个实例指向同一块内存。

### 结构体与联合体

当需要传递结构体或联合体类型时，需要对应的类继承 `Structure` 或 `Union`，并通过 `_fields_` 属性定义其内部的属性名和类型（`ctypes` 类型），该属性为一个二元组的列表，二元组第一个值为属性名，第二个值为类型。

若一个类型为结构体或联合体，则其对象中会自动包含对应的属性，创建对象时也有对应的构造函数可以直接按顺序传入值或使用其名称创建

各类型之间可以互相嵌套，`_fields_` 二元组的第二个值（类型）也可以是另一个结构体或联合体

```python
from ctypes import c_int
# 结构体
from ctypes import Structure 
class POINT(Structure):
	_fields_ = [ ('x', c_int) , ('y', c_int) ]
```
- `_pack_` 属性可以用来修改字节对齐方式，默认与 C 相同
- `Structure` 默认使用本地字节序。如需要自定义，可使用 `XxxStructure`，`XxxUnion` 等类型，其中 `Xxx` 可选值为 `BigEndian` 或 `LittleEndian`
- `_fields_` 列表中也可以存在三元组。三元组第三个值表示包含的位长度，只能用于整型类型

当出现需要前向声明的情况时，可以通过先声明类型后设置 `_fields_` 属性的方式解决

```python
from ctype import Structure, c_int, POINTER

# 相当于前向声明
class Node(Structure):
	pass

Node._fields_ = [('next', POINTER(Node)), ('value', c_int)]
```

### 数组

数组类型可以通过 `基本类型 * 数量` 方式创建，基本类型包括：
- `ctypes` 类型
- 实现了 `Structure` 或 `Union` 的类型
- 指针或引用类型

### 函数指针

允许需要传入一个 Python 函数作为函数类型。使用 `CFUNCTYPE()` 创建 `cdecl` 约定调用的回调函数，使用 `WINFUNCTYPE()` 创建 `stdcall` 约定调用的回调函数类型。第一个参数为返回值类型，剩下参数为传参类型。

```python
# Python 编写的回调函数
def py_cmp_func(a, b):
	return a[0] - b[0]
# 函数返回一个 int，接受两个 int*
cmp_func_type = CFUNCTYPE(c_int, POINTER(c_int), POINTER(c_int))
# 生成的回调函数对象
cmp_func = cmp_func_type(py_cmp_func)
```

## 指定函数原型

使用 `argtypes` 属性可以设置链接库函数需要的参数类型，使用 `restype` 属性可以定义返回值类型

```python
# 方法参数为一个 char*，返回一个 int
func.argtypes = [ctypes.c_char_p]
func.restype = ctypes.c_int
func(...)
```

函数原型涉及的类型不一定需要与实际类型相符，只是在调用之前 Python 会检查一遍给定的数据是否符合要求，不符合则返回 `ArgumentError` 异常

设置函数原型后，传入 Python 类型时，Python 可以自动进行适当的类型转换

大部分情况下可变参数能正常使用。但某些特定平台需要先固定需要的类型（ARM64 的 苹果平台等）

## 访问导出变量

使用 `ctypes类型.in_dll(dll, name)` 访问动态链接库导出的变量，其类型为前面的 `ctypes类型`

# Win32API

若要调用 Windows 自带 dll 的方法，Python 提供两个快捷库

- `ctypes.windll`：快速引入以 `stdcall` 约定的 Windows dll 库。以下方法均可以使用 `kernel32` 变量访问 `kernel32.dll` 的方法：

```python
from ctypes import windll
# 完整的写法
kernel32 = windll.LoadLibrary('kernel32.dll')
# 直接使用 dll 名，自动补全 ".dll" 后缀名
kernel32 = windll.kernel32
# 使用 [] 也行
kernel32 = windll['kernel32.dll']
```

- `ctypes.wintypes`：提供 Windows 函数各种类型和常用结构体的转换函数

# find_library

- `ctypes.util.find_library(name)`：查找动态链接库并返回其路径
	- Linux：利用 `ldconfig`，`gcc`，`objdump`，`ld` 程序及 `LD_LIBRARY_PATH` 环境变量查找
	- MacOS：调用几种预定义命名方案（`c`，`m`，`bz2`，`AGL`）查找
	- Windows：在系统路径中搜索
