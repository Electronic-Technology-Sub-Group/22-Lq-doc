# 数字

Python 支持的数字类型包括
- 整型：`int`
- 浮点类型：`float`
- `Decimal`
- `Fraction`
- 复数：`complex`, 使用 `j` 或 `J` 后缀，如 `3 + 2j`

数字类型支持各种类型的混合运算，并可以自动转换，支持的运算符包括常见的 `+`，`-`，`*`，`/` 等
- 除法 `/` 为一般的算术除法，结果总是一个浮点数 `float`
	- 整除 `//`：取除法的整数，向下取整，类似一般 `c/c++` 等的整数除法
	- 取模 `%`：同普通的取模操作
- 乘方 `**` 表示常见的 `pow` 乘方运算，优先级比 `*/` 高

# 布尔

包括 `True` 和 `False`

通过 `in` 和 `not in` 可以判断元素是否在某个容器中，通过 `is` 和 `is not` 可以判断元素是否是同一类型

与其他语言不同，Python 的比较运算符 `>, <, ==` 支持链式调用：

```python
v1 = a < b == c
v2 = (a < b) and (b == c)
```

v1 与 v2 等效。

布尔运算关键字 `and`，`or`，`not` 的优先级：`not` > `and` > `or`，且 `and` 与 `or` 也有短路特性

为避免 `==` 被误写成 `=`，表达式内部赋值必须使用 `:=`

# 序列

## string

Python 字符串为常量对象，每次涉及到字符串修改的操作都返回一个新的字符串

### 字面量

字符串字面量可以使用 `""` 或 `''` 包围，其意义相同。`""` 中可以包含 `'`，`''` 中可以包含 `"`。

```python
# welcome to python.
print("welcome to python.")  
# Hello world
print('Hello world')  
# say: "Hello world"
print('say: "Hello world"')  
# say: 'Hello world'
print("say: 'Hello world'")
```

`\` 可用于转义，若不想使用转义则使用 `r` 开头表示原始字符串，该标记可用于任何单引号和双引号中

```python
# C:\windows\system32
print("C:\\windows\\system32")  
print(r"C:\windows\system32")
```

使用 `"""..."""` 或 `'''...'''` 可表示多行的字符串，在行末添加 `\` 表示不换行

```python
# line1
# line2
# line3 line3
print("""\
line1  
line2  
line3\
 line3""")
```

字符串长度可以通过 `len(str)` 获取

### 拼接

多段字符串之间直接连写就能表示自动拼接，或使用 `+` 进行拼接

*直接连写进行拼接只用于两个字符串字面量之间拼接，不能用于变量或表达式*

```python
# part1 part 2 part3
print("part1 " "part 2 " + "part3")
```

使用 `*` 进行重复

```python
# AAA
print("A" * 3)
```

### 格式化

字符串格式化有多种方法：

- 使用 `format` 方法进行格式化：使用 `{}` 作为占位符
```python
a = "aaa"
b = "bbb"
c = "ccc"

# aaa, bbb, ccc
s1 = "{}, {}, {}".format(a, b, c)
# bbb aaa aaa bbb
s2 = "{1} {0} {0} {1}".format(a, b)
# aaa bbb ccc
s3 = "{aa} {bb} {cc}".format(aa=a, bb=b, cc=c)
# aaa bbb ccc
s4 = "{0} {1} {other}".format(a, b, other=c)
```

详见 [格式规格迷你语言](https://docs.python.org/zh-cn/3.11/library/string.html#formatspec)

- 使用 `%` 运算符进行格式化：使用 `%` 占位的类 C format 方式进行格式化，多个参数使用括号
```python
a = "aaa"
b = 10
c = 3.14

# aaa, 10, 3.14000
s = "%s, %d, %.05f" % (a, b, c)
```

- 字符串模板：使用 `f` 开头的字面量中使用 `{}` 将 Python 表达式包围
```python
a = 10
b = "abc"

# a=10, b=abc
print(f"a={a}, b={b}")
```

详见 [格式字符串语法](https://docs.python.org/zh-cn/3.11/library/string.html#formatstrings)

### 索引与切片

字符串可以通过索引（下标）访问，其结果为一个长度为 1 的字符串 -- 没有单独的字符类型

字符串索引从 0 开始；也支持负数，`-n` 表示从后往前第 n 个字符 

```python
word = 'Python'  
  
print(word[0])  # P
print(word[1])  # y
print(word[-1]) # n
print(word[-2]) # o
```

下标中使用 `:` 连接两个数字表示切片，`str[m:n]` 返回字符串从 `str[m]` 开始到 `str[n]` （不含）的字符串。
- 切片第一个值 `m` 省略时表示为 0
- 切片最后一个值 `n` 省略时表示为字符串结尾
- 可以有第三个 `:` 后接步长，默认 1

```python
word = 'Python'  
  
print(word[1:3])  # yt
print(word[:3])   # Pyt
print(word[3:])   # hon
```

对于索引访问字符，越界会报错；而对于切片，Python 则会自动处理

### 其他

- `rjust`，`ljust`，`center`：使用空格扩充字符串长度并右/左/居中对齐，但字符串过长时不会截断字符串
- `zfill`：字符串左侧充0，且可以识别正负号
- `replace`, `split` 等
- `strip(_chars=' ')`：去除前后的字符
	- 默认去除空格，类似其它语言的 `trim`
	- 去除的是前后参数字符串包含的所有字符，而不是参数字符串

[文本序列类型 --- str](https://docs.python.org/zh-cn/3.11/library/stdtypes.html#textseq)
[字符串的方法](https://docs.python.org/zh-cn/3.11/library/stdtypes.html#string-methods)
[格式字符串字面值](https://docs.python.org/zh-cn/3.11/reference/lexical_analysis.html#f-strings)
[格式字符串语法](https://docs.python.org/zh-cn/3.11/library/string.html#formatstrings)
[printf 风格的字符串格式化](https://docs.python.org/zh-cn/3.11/library/stdtypes.html#old-string-formatting)

## list

列表是一种变长复合数据类型

### 创建

使用 `[]` 创建，使用 `,` 分割每一个值，每个值的类型可以不同

```python
list = [1, 2.0, 3 + 2j, "a", "b", "c"]

# [1, 2.0, (3+2j), 'a', 'b', 'c']
print(list)
```

空列表使用 `list()` 或 `[]` 创建

```python
list1 = list()
list2 = []
```

也可以通过列表推导式创建，即通过循环或迭代的方式创建列表，通常表达为一个表达式，附带多个 `for` 和 `if` 子句：

```python
[表达式 for... if...]
```

如 

```python
# 序列中的每一个项由 (x, y) 元组构成
# x 在 [1, 9) 中选取，y 在 [5, 15) 中选取，且满足 x != y
lst = [(x, y) for x in range(1, 10) for y in range(5, 15) if x != y]
```

等价于

```python
lst = []  
for x in range(1, 10):  
    for y in range(5, 15):  
        if x != y:  
            lst.append((x, y))

```

### 下标与切片

列表也支持下标访问和切片，切片返回的是源列表的浅拷贝

```python
list1 = [1, 2.0, 3 + 2j, "a", "b", "c"]  

# 1
print(list1[0])  
# c
print(list1[-1])  
  
list2 = list1[2:-2]  

# [(3+2j), 'a']
print(list2)
```

使用下标和切片可以方便的对列表进行修改

```python
list1 = [1, 2.0, 3 + 2j, "a", "b", "c"]  
  
list1[2] = 1000  
# [1, 2.0, 1000, 'a', 'b', 'c']
print(list1)  
  
list1[2:-2] = ["aaa", "bbb", "ccc"]  
# [1, 2.0, 'aaa', 'bbb', 'ccc', 'b', 'c']
print(list1)  
  
list1[2:-2] = []  
# [1, 2.0, 'b', 'c']
print(list1)

list1[:] = []  
# []
print(list1)
```

### 常用方法

- 插入
	- `lst.append(x)`：在列表结尾添加元素，相当于 `lst[len(lst)]=x`
	- `lst.extend(iterable)`：在列表后插入元素，相当于 `lst[len(lst)]=iterable`
	- `lst.insert(p, x)`：在列表第 `p` 个元素之前插入元素 `x`
- 移除
	- `lst.remove(x)`：从列表中删除第一个值为 `x` 的元素，若未找到则抛出 `ValueError` 异常
	- `lst.pop(i=len(lst)-1)`：从列表中删除指定位置的元素并返回被删除的元素，相当于 `del lst[i]`
	- `lst.clear()`：清空列表，相当于 `del a[:]`
- 查找
	- `lst.index(i, start=0, end=len(lst))`：返回列表中第 `i` 个元素，且 $start\leq i\leq end$，找不到则抛出 `ValueError` 异常
- 其他
	- `lst.count(x)`：统计元素 `x` 出现的次数
	- `lst.sort(*, key=None, reverse=False)`：排序，但不是所有的值都可以排序，要求列表内元素是互相可以比较大小的
	- `lst.reverse()`：翻转
	- `lst.copy()`：浅拷贝，相当于 `lst[:]`

### 运算符

- `lst1 + lst2`：返回两个列表连接后的新列表

### 关键字

- `del`：按索引或切片，而不是按值从列表中移除元素：
```python
lst = [1, 2, 3, 4, 5, 6, 7, 8, 9]
del lst[0] # ret 1, lst=[2,3,4,5,6,7,8,9]
del lst[1] # ret 3, lst=[2,4,5,6,7,8,9]
del lst[1:3] # lst=[2,6,7,8,9]
del lst[:]   # lst=[]
```

`del` 也可以删除整个变量

```python
del lst
```

- `in`：`x in lst` 表示元素 `x` 是否存在于列表 `lst` 中，返回一个布尔值

### 队列

通过 `append` 配合 `pop` 可以模拟栈和队列，但列表在开头添加和删除元素非常慢（反之，在结尾添加和删除元素很快）。因此，通过列表模拟队列效率不很好，最好使用 `collections.deque` 类，允许快速在两端插入和删除元素

```python
from collections import deque  
  
q = deque("a", "b", "c")  
  
# a b c d  
q.append("d")  
  
# 0 a b c d  
q.appendleft("0")  
  
# ret: d, q: 0 a b c  
q.pop()  
  
# ret: 0, q: a b c  
q.popleft()

```

## tuple

### 创建

元组通过一个括号创建，括号可省略，包含有限个元素，每个元素的类型可以不同

```python
t = (1, 2.0, "hello") # 或 tuple()
t2 = 1, 2.0, "hello"
```

0 个元素的元组必须要用括号创建，1 个元素的元组只要在数值之后加一个 `,` 即可

```python
t0 = ()
t1 = 1,
```

元组是不可变的，这与列表有区别。

### 解包

元组的元素同样可以通过遍历、下标与切片访问。除此之外，还可以通过解包的方法访问元素

```python
t = 123, 456, "hello"
x, y, z = t
# x=123, y=456, z=hello
print(f"x={x}, y={y}, z={z}")
```

## range

`range` 是一个可迭代对象，用于按起点，终点与步长生成一组整数序列

```python
range(start)
range(start, stop, step=1)

r = range(10)
```

## 总结

1. 序列都支持下标访问、切片等操作
2. 可变序列都支持 pop、clear 等操作
3. 使用 len 获取长度，max、min 可以获取最值，sorted 可用于排序

# set

集合 set 是一种由不重复元素组成的无序容器，通过 `{}` 或 `set()` 创建，但空集合只能通过 `set()` 创建

```python
s = {1, 2, 3}
```

集合的常见用法主要有成员检测，消除重复元素等，支持的操作包括合集（`union`），交集，差集（`difference`），对称差分等数学运算

集合也支持通过列表推导式的形式创建

```python
s = {x for x in "abracadabra" if x not in "abc"}
```

# dict

字典值为任意类型，关键字为索引，关键字为字符串，数字或其他不可变类型及其组成的元组

字典通过 `{a:b, c:d}` 的形式创建，也可以通过 `dict(元组列表)` 的形式创建。空字典可通过 `{}` 创建

```python
tel = {'jack': 400, "sape": 654}
```

也可以通过字典推导创建字典

```python
{x: x ** 2 for x in (2, 4, 6)}
```

字典访问和修改可通过索引实现，通过 `del` 可删除某键值对

```python
tel = {'jack': 400, "sape": 654}

tel["guido"] = 222
del tel["jack"]
```

可通过 `sorted(dict)` 的形式根据键进行排序，默认按插入顺序排序。

通过 `in` 和 `not in` 可以检查某键是否包含在字典中

# 枚举

# type

使用 `type(值)` 可以获取一个值的类型信息（class）

# 转换

使用 `类型(值)` 将对应值强转成某种类型
- 任何对象都可以转换成字符串
- 若某种类型无法转换成相应的类型，抛出 `ValueError`