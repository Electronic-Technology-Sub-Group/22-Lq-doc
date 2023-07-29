# 定义

```python
class 类名:
    # do something: 语句或成员
```

- 若只是一个空类，至少应有一个 `pass`
- 类中可以定义变量（属性）和方法，方法通常第一个参数为 `self`
- 类中其他元素之前可以包含一个字符串表示文档字符串

# 成员属性

对于类，支持实例化和成员引用：
- 可以通过 `类名.成员` 的形式完成对成员的引用
- 通过 `obj = 类名()` 的方法实例化一个该类型的变量
	- 类中可以定义一个第一个参数为 `self`，其他参数任意的方法名为 `__init__` 作为构造函数
		- 在 `self` 上定义的属性为对象所有，在类中定义的属性为类所有（类似于静态成员）
	- 通过对象调用方法，相当于第一个参数传入了对象本身，故函数第一个参数通常使用 `self` 作为参数
- Python 没有私有变量，但约定俗成的，以一个下划线 `_` 开头的变量和函数为私有成员

## 内置类成员

- `__doc__` 记录了类的文档字符串
- `__init__()`：当该类实例化时，兑现 `__init__(self, ...)` 调用之前会自动调用该方法

## 内置对象成员

- `__class__`，记录该对象的类型（类）
- `__str__(self)`：将类转换为字符串（类似Java `toString()`）
- `__lt__(self, other)`，`__le__(self, other)`，`__eq__(self, other)`：`<, >`，`<=, >=`，`==, !=` 重载
	- 默认下，`__lt__`，`__le__` 未实现，`__eq__` 比较对象内存地址
## 实例方法成员

- `__self__`：调用方法的对象
- `__func__`：方法对应的函数对象

# 继承与派生

Python 支持继承，且支持多继承，使用 `()` 定义。类中所有的函数都类似于 C++ 的虚函数，即都可被子类重写。

```python
class A:
    pass

class B:
    pass

# 类 AB 继承自 A 和 B
class AB(A, B):
    pass
```

成员查找规则：
1. 当前类中是否有实现
2. 自左向右递归查找基类
3. 动态顺序改变：所有基类有共同的基类（必然，至少全部继承自 `object`），Python 将调整顺序使共同的基类仅被搜索一次

## 调用基类成员

通过 `基类.成员` 或 `super().成员` 访问，注意方法需要传入 `self`（实际是直接调用基类的静态方法方式调用）

## 名称改写

使用至少两个下划线 `__` 开头，且至多一个下划线结尾的成员将被 Python 编译器自动重命名为 `_类名__方法名`，有助于让子类重载方法而不破坏类内方法调用。

```python
class A:
    def __init__(self):
        self.__fun() # 这里调用的是基类定义的 fun，无论子类如何改写都不会有影响

    def fun(self):
        # 这里是基类定义的 fun 函数
        pass

    # 使 __fun 指向基类版本的 fun，子类修改不影响该调用
	__fun = fun
```

该特性适用于类中的成员变量和方法。在类内部可以直接使用 `__xxx` 的形式访问，而在外部不行，也可以实现部分封装的效果

```Python
class A:

    def __init__(self, name):
        self.name = name

    def __hello(self):
        print("Hello")
        print(f"  by {self.name}: ")

    def hello(self, name):
        print(f"{name}:")
        self.__hello()


a = A("aaa")

# emmmm:
# Hello
#   by aaa: 
a.hello("emmmm")

# Hello
#   by aaa: 
a._A__hello()

# AttributeError: 'A' object has no attribute '__hello'. Did you mean: '_A__hello'?
a.__hello()

```

注意：
1. 该方法仅用于避免设计上的冲突和意外，若有意识地覆盖改写后的函数也是可被覆盖的
2. 在类中直接使用改写前的函数名仅针对于经过字节码编译的代码，即需要编译器的参与，以下情况不认为在类中，原因是类似于 `global` 语句效果：
	- 通过 `exec()`，`eval()` 调用
	- `getattr()`，`setattr()`，`delattr()` 及对 `__dict__` 的直接引用

## 类型检查

`isinstance(对象, 类型)` 相当于 `instanceof`，判断对象的 `__class__` 指向的类型是否为给定类型或给定类型的子类

`issubclass(类型1, 类型2)` 用于判断类型 1 是否是类型 2 的子类

# 数据类

类似于 Pascal 的 record 或 C 的 struct，数据类仅包括具名数据。可以使用 `dataclass` 注解

```python
from dataclasses import *


@dataclass
class MyData:
    name: str
    age: int
    desc: str
    salary: float


m = MyData("aaa", 10, "???", 10.1)

```

# 迭代器

支持使用 `for` 循环的对象称为可迭代对象，支撑其循环的类称为迭代器。

一个迭代器有以下特征；
- 定义了 `__next__()` 函数，且每次调用时返回下一个元素
- 当 `__next__()` 调用且没有下一个元素时，抛出 `StopIteration` 异常

一个可迭代类有以下特征：
- 定义了 `__iter__()` 函数，且使用 `__iter__()` 方法会返回一个迭代器对象

## 生成器

生成器通过一定的语法快速创建迭代器。在一个函数中，使用 `yield` 而不是 `return` 返回每次迭代的内容，则调用该函数将会返回一个迭代器，每次 `__next__()` 的返回值即 `yield` 的产物。

```python
def my_iter():
    for v in range(5, 1, -1):
        yield v


itr = my_iter()
# <class 'generator'>
print(itr.__class__)

for i in itr:
    # 5 4 3 2
    print(f"Value is {i}")

```

（好像这个迭代器也是一个可迭代对象）

```python
# <class 'generator'>
print(itr.__iter__().__class__)
# True
print(itr == itr.__iter__())
```

## 生成器表达式

使用类似列表推导式的方式也可以创建生成器