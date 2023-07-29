# 循环

## while

使用 `while` 表示条件循环

```python
while [condition]:
    # do something
```

```python
a, b = 0, 1  
while a < 1000:  
    print(a, end=',')  
    a, b = b, a + b

# 0,1,1,2,3,5,8,13,21,34,55,89,144,233,377,610,987,
```

## for

不同于其他语言，`for` 并不表示带有步进迭代的循环，而是仅用于按顺序迭代一个序列（可迭代对象），如列表，字符串，字典等

```python
for [variable] in [list]:
    # do something

```

```python
words = ['cat', 'window', 'defenestrate']
for w in words:
    print(w, len(w))

```

可迭代对象的特点是，带有一个 `__iter__()` 或 `__getitem__()` 方法以返回迭代器或值

在遍历时修改元素可能会有错误的结果，要涉及到修改序列元素的情况下，尽量使用新序列或遍历序列副本

```python
# Create a sample collection
users = {'Hans': 'active', 'Éléonore': 'inactive', '景太郎': 'active'}

# 遍历副本
for user, status in users.copy().items():
    if status == 'inactive':
        del users[user]

# 新序列
active_users = {}
for user, status in users.items():
    if status == 'active':
        active_users[user] = status

```

若要实现遍历数字，可使用 `range()` 函数

```python
for i in range(5):
    print(i, end=",")

# 0,1,2,3,4
```

若只是为了同时遍历索引和值，则使用 `enumerate` 函数更方便

```python
l = ["A", "B", "C"]  
  
for i, char in enumerate(l):  
    print(i, ":", char)

# 0 : A
# 1 : B
# 2 : C
```

enumerate 还可以额外接受一个整数表示 `start`，默认为 0

```python
l = ["A", "B", "C"]  
  
for i, char in enumerate(l, 2):  
    print(i, ":", char)

# 2 : A
# 3 : B
# 4 : C
```

对于字典，通过 `items()` 方法可以同时遍历键值对

```python
knights = {'gallahad': 'the pure', 'robin': 'the brave'}
for k, v in knights.items():
    # do something

```

对于多个序列时，可通过 `zip()` 将其一一匹配，以类似字典的方式遍历

```python
questions = ['name', 'quest', 'favorite color']
answers = ['lancelot', 'the holy grail', 'blue']

for quest, ans in zip(questions, answers):
    # do something

```

其他对循环有辅助作用的方法有：
- `reversed()`：逆向
- `sorted()`：排序
- `set()`：去重，但会打乱顺序

# 条件

## if

`if` 可以包含多个 `elif` 子句和最多一个 `else` 子句结尾

```python
if [condition1]:
    # do something
elif [condition2]:
    # do something
elif [condition3]:
    # do something
#...
else:
    # do something
```

```python
x = int(input("Please input an integer: "))  
  
if x < 0:  
    x = 0  
    print("Negative: change to 0")  
elif x == 0:  
    print("Zero")  
elif x == 1:  
    print("One")  
else:  
    print("Positive: ", x)

```

## match

`match` 实现模式匹配，类似于 Rust 的 `match`，可以实现 `switch` 的功能但更加强大
- 可实现类似 `switch` 的条件匹配
- 可对值进行解包，类似元组和列表的模式解包，且能匹配任何序列，但不能匹配迭代器或字符串
- 序列解包支持扩展解包操作 `*`，类似于解包赋值，且支持 `*_`
- 允许解包映射，但 `**_` 是多余的，不允许使用
- 允许使用 `as` 关键字捕获子模式
- 大多数字面量按值的相等性比较，但单例对象 `True`，`False`，`None`  等按标识符比较
- 模式可使用命名常量，但必须用 `.` 防止被解读为捕获变量

```python
match [variable]:
    case [pattern1]:
	    # do something
    case [pattern2]:
	    # do something
    case [pattern3]:
	    # do something
    case _:
	    # do something
```

```python
# 使用 match 实现其他语言的 switch 功能 
def http_error(status):  
    match status:  
        case 400:  
            return "Bad request"  
        case 404:  
            return "Not found"  
        case 418:  
            return "I'm a teapot"  
        case _:  
            return "Other error"

```

对于匹配相同的值，可以使用 `|` 合并

```python
match num:
    case 1 | 2:
        print("num is 1 or 2")

```

不只是一般值，类似 Rust，使用 `match` 可以匹配解包任何对象并对其绑定；对于类的模式匹配位置可以通过 `__match_args__` 属性指定，默认为各自的属性名

```python
class Point:  
    x: int  
    y: int  
  
  
def where_is(point):  
    match point:  
        case Point(x=0, y=0):  
            print("Origin")  
        case Point(x=0, py):  
            print(f"Y={py}")  
        case Point(x=px, y=0):  
            print(f"X={px}")  
        case Point():  
            print("Somewhere else")  
        case _:  
            print("Not a point")

```

允许捕获 `list`，`map` 等解包

```python
class Point:  
    x: int  
    y: int  
  
  
def match_list_or_map(value):  
    match value:  
        case []:  
            print("empty list")  
        case {}:  
            print("empty map")  
        case [Point(x, y)]:  
            print(f"Only point {x}, {y}")  
        case [Point(0, y1), Point(0, y2)]:  
            print(f"Two point at Y: {y1} - {y2}")  
        case {"aa": va, "bb": vb}:  
            print(f"aa={va}, bb={vb}")
        case ["aa", *rest]:
            print(f"list contains aa, else is {rest}")

```

可使用 `as` 捕获子模式，使用 `if` 子句增加额外的匹配条件

```python
class Point:  
    x: int  
    y: int  
  
  
def where_is(point):  
    match point:  
        case Point(x, y) if x == y:  
            print(f"Point x=y={x}")  
        case (Point(x1, y1), Point(x2, y2) as p2):  
            print(f"two point, p2 is {p2}")

```

# 其他控制关键字

## break

类似 C，跳出最近的 `for` 或 `while`

## continue

类似 C，进行下一次迭代

## else

Python 的 `else` 除了与 `if` 搭配，还可以与循环（`for`，`while`）搭配，当循环自然结束时调用，包括：
- `for` 循环迭代完成所有元素
- `while` 循环条件为 `false` 时
使用 `break` 或因异常结束的循环不会执行 `else` 子句

`else` 也可以与 `try` 同用，块中没有任何异常时调用

## pass

`pass` 语句表示不执行任何操作，类似 C 的空 `;`。该句用于某个代码块没有任何子句时：

```python
while True:
	pass

class EmptyClass:
    pass

def emptyFunc(*args):
    pass

```