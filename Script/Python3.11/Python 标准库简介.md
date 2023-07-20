# os

操作系统交互
- `getcwd()`：获取当前工作路径
- `chdir(path)`：更改当前工作路径
- `system(cmd)`：用过系统终端执行命令

# shutil

更高级的文件和目录管理
- `copyfile`，`move` 等

# sys

- `argv`：获取执行的命令行参数，获取的是一个字符串列表
- `stdin`，`stdout`，`stderr`：基本输入输出

# argparse

提供 `ArgumentParser` 类用于处理和构建复杂命令行

# re

模式匹配
- `findall(reg, str)`：查找所有匹配内容，`reg` 为正则字符串，返回字符串列表
- `sub`

当然如果只是简单替换，使用字符串的 `replace` 方法即可

# 数学库

## math

数学计算库

## random

随机数库
- `choice(list)`：从列表中随机选取一个元素
- `sample(list, count)`：从指定列表中随机选取多个元素，返回一个列表
- `random()`：随机生成一个浮点数
- `randrange(value)`：从 `range(value)` 中随机生成一个数字

## statistics

统计计算，包括平均数、中位数、方差等。更高级的统计库使用 [SciPy](https://scipy.org/)

# 网络

- `urllib.request`：访问网页，使用 `urlopen` 访问网站，注意需要 `with as` 或 `close()`，返回 `response` 可迭代（类似文件）
- `smtplib`，`poplib`：访问 SMTP/POP 邮件服务器
- `xmlrpc.client`，`xmlrpc.server`：远程调试相关

# datetime

时间与日期，对象可加减
- `today()`：当前日期/时间

# 压缩

Python 内置 `zlib`, `gzip`, `bz2`, `lzma`, `zipfile` 和 `tarfile`。

# 性能测试

- `timeit`：提供 `Timer` 类，用于细粒度的性能测试（测试几条语句执行时间）
- `profile`，`pstats`：较大代码块中识别时间关键部分 

# 测试

- `doctest`：测试函数中每个成员的文档字符串中是否包含测试
- `unittest`：在单独 Python 文件中进行测试

# 文档数据

- `json`：Json 工具包
- `csv`：CSV 工具包
- `xml.etree.ElementTree`，`xml.dom`，`xml.sax`：XML 工具包

# sqlite3

提供 SQLite 数据库访问包装

# 国际化

`gettext`，`locale`，`codecs` 等包。

# 其他

- `glob`：使用通配符搜索文件
- 段落格式化与字符串模板，二进制，多线程，弱引用，日志，列表工具类，基于十进制的精确浮点运算等
[11. 标准库简介 —— 第二部分 — Python 3.11.4 文档](https://docs.python.org/zh-cn/3.11/tutorial/stdlib2.html)