正则表达式通过 `re` 标准库实现

# 从头匹配

`match` 方法可以从开头检查字符串是否有符合要求的部分，返回一个 `re.Match` 对象
- `span()`：`tuple(a, b)`，已匹配的部分在整个字符串中的位置
- `group()`：已匹配的字符串

注意，若开头不匹配，则返回 None，即使字符串中间存在匹配部分

```python
import re

s = "python is a language"
result = re.match('python', s)
if result:
    # (0, 6)
    print(result.span())
    # python
    print(result.group())

s = "and python is a language"
result = re.match('python', s)
# 无输出（None）
if result:
    print(result.span())
    print(result.group())

```

# 匹配

使用 `search` 在整个串中进行匹配，查找第一个符合的部分

```python
import re

s = "python is a language"
result = re.search('python', s)
if result:
    # (0, 6)
    print(result.span())
    # python
    print(result.group())

s = "and python is a language"
result = re.search('python', s)
if result:
    # (4, 10)
    print(result.span())
    # python
    print(result.group())

```

# 查找所有

通过 `findall` 可以找出字符串中所有匹配的部分

```python
import re

s = "python is a language, and python is a language"
result = re.findall('python', s)
# ['python', 'python']
print(result)
```
