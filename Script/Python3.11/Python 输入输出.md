# 标准输入输出

- 标准输入输出流为 `sys.stdin` 和 `sys.stdout`
- 使用 `print` 方法可将表达式结果进行输出到控制台，使用 `input()` 可从控制台输入字符串
	- `input(sth)` 等效于 `print(sth); input()`
	- `end` 可用于替换换行符：`print(sth, end=',')`

# 读写文件

## 文件

通过 `open(filename, mode='r', encoding=None)` 打开一个文件，并返回一个 `file` 对象
- filename：文件名
- mode：打开方式
	- r：只读
	- w：只写且覆盖同名文件
	- a：追加到文件末尾
	- r+：打开文件读写
	- b：以二进制形式打开，追加在其他模式末尾
- encoding：编码类型，默认与平台相关，当以二进制模式打开时不可指定

### 关闭

使用 `with` 语句，或使用 `close()` 方法关闭文件。

```ad-danger
调用 `write()` 修改文件后，若未关闭文件，即使程序正常退出也可能导致文件没有完全写入硬盘
```

```python
with open("file", "rw", encoding="utf-8") as f:  
    # do something  
    pass  
  
f2 = open("file", "rw", encoding="utf-8")  
# do something  
f2.close()

```

### 访问
- `read([size])`：读取文件内容，返回字符串或字节串对象（二进制模式）
	- 省略 size 或 size<0，返回整个文件
	- 文件大小超过内存两倍时，会出现问题
	- 若已达到文件末尾，返回空字符串
- `write()`：写入内容，返回写入的字符数
- `readline()`：读取一行；若达到末尾，返回空字符串
	- `readlines()`：读取所有行到一个列表中，相当于 `list(file)`
	- 也可以直接通过 `for line in file` 按行遍历文件
- `tell()`：当前文件位置，二进制下为字节数，文本模式下意义未知
	- `seek(offset, whence=0)` 可改变文件对象位置，相对 `whence` 偏移 
		- 0：相对于文件开头
		- 1：相对于当前位置
		- 2：相对于文件末尾

## json

```python
import json
```

通过 `json.dumps(obj)` 将对象转换为字符串

通过 `json.dump(obj, f)` 将文件序列化到文件

通过 `json.load(f)` 将文件反序列化到一个对象（返回 list 或 dict）

## pickle

`pickle` 模块用于 Python 对象的序列化/反序列化协议，仅用于 Python，不能用于其他语言编写的程序通信