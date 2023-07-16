Python 是一种解释性脚本语言，可以直接通过命令行运行。基本运行脚本方法如下：

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

# 非交互模式

```bash
python [option] [script] [args] 
```

Python 的解释器命令行分为三部分：选项，脚本，参数。

## script

这里 `script` 为要执行的命令，可以是 Python 语句，待执行的脚本或模块，文档中表示为接口选项

### 语句

```bash
python -c <command> <args>
```

`<command>` 为一条有效的 Python 语句，也可以用换行符输入多条语句

使用该方法执行时，`sys.argv` 参数列表的首个元素为 `-c`，`sys.path` 开头为当前目录

### 模块

```bash
python -m <module-name> <args>
```

在 `sys.path` 中搜索 `<module-name>` 模块，并作为 `__main__` 模块运行

模块名也可以是包名或命名空间包名。若使用包名时，则以 `<pky>.__main__` 为主模块开始执行

使用该方法执行时，`sys.argv` 参数列表的首个元素为 `-` 和模块文件的完整名（定位模块时为 `-m`），`sys.path` 开头为当前目录

`-l` 隔离模式下，`sys.path` 不包含当前目录和用户 `site-packages` 目录，忽略 `PYTHON*` 环境变量

### 脚本

```bash
python <script>
```

执行 `<script>` 文件，该文件应当为相对或绝对路径，指向
- 任意 Python 文件
- 包含 `__main__.py` 的目录或 `zip` 包

使用此方法运行时，`sys.argv` 第一个元素为命令行中指定的脚本名称；若 `<script>` 直接指向一个 Python 文件，则该文件所在目录将被加入 `sys.path` 开头，该文件将作为 `__main__` 模块运行；若 `<script>` 指向一个包含 `__main__.py` 的目录或 `zip` 包，则把脚本名添加到 `sys.path` 开头，

`-l` 隔离模式下，`sys.path` 不包含脚本目录和用户 `site-packages` 目录，忽略 `PYTHON*` 环境变量

## args

运行脚本提供的参数，可通过 `sys.argv` 访问

## option

- `-?`，`-h`，`--help`：输出所有命令选项和环境变量的简短解释
	- `--help-env`：输出 Python 特定的环境变量的简短解释
	- `--help-xoptions`：输出具体实现的 -X 选项的描述
	- `--help-all`：输出所有完整信息
- `-V`，`--version`：输出当前 Python 版本
	- `-VV`：输出当前 Python 版本的详细信息，包括构建信息
- `-d`：开启解析器调试输出
- `-E`：忽略 `PYTHON*` 环境变量
- `-I`：隔离模式，相当于 `-E -P -s`
- `-O`：忽略所有 `assert` 及 `__debug__` 值作为条件的代码
- `-P`：不将当前目录添加到 `sys.path` 中
- `-s`：不将用户的 `site-packages` 目录添加到 `sys.path` 中
- `-W arg`：警告信息控制
- `-x`：跳过源文件的第一行，以允许使用非 Unix 的 `#!cmd`
- `-X`：保留用于各种编译器具体实现的专属选项

详细信息可见 [1. 命令行与环境 — Python 3.11.1 文档](https://docs.python.org/zh-cn/3.11/using/cmdline.html#generic-options)

# 交互模式

直接运行 `python` 即可进入交互模式。交互模式下，每输入一行语句，命令行会运行并输出其结果。

以 `>>>` 为前缀时，表示接收一行语句；以 `...` 为前缀时，表示前一行语句未完成，或进入了某代码块，需要输入完成后才能执行。

退出交互模式可使用 EOF 快捷键（Win：`ctrl+z`；Linux：`ctrl+d`）或 `quit()`

# 环境变量

- `PYTHONHOME`：标准 Python 库位置
- `PYTHONPATH`：额外 Python 模块搜索路径

详细信息可见 [1. 命令行与环境 — Python 3.11.1 文档](https://docs.python.org/zh-cn/3.11/using/cmdline.html#environment-variables)
