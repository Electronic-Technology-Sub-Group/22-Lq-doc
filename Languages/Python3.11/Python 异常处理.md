# 捕获

```python
try:
    # 可能发生异常的代码
except 异常类型 as 变量:        # 捕获特定异常
    # 异常处理代码
except (异常1, 异常2) as 变量:  # 捕获多种异常
    # 异常处理代码
except:                        # 捕获任意异常
    # 异常处理代码
finally:
    # finally 代码块
```

`finally` 执行规则：
- 任何情况下，`try`，`except` 正常执行完成后都会执行 `finally` 代码块
- 若 `except` 块中存在异常，则该异常将在 `finally` 执行完成后抛出
	- 若 `finally` 中存在 `break`，`continue`，`return` 等语句，`except` 中的异常不会触发
- 若 `expect`，`try` 语句中有 `break`，`continue`，`return` 等语句，则在 `break`，`continue`，`return` 之前执行 `finally` 代码块
	- 若 `finally` 中存在 `break`，`continue`，`return` 等语句，返回的是 `finally` 块的返回值

# 异常类

所有异常类继承自 `Exception`

# 触发

通过 `raise` 抛出异常。所有异常应继承自 `Exception` 类。

```python
raise NameError("Here is an exception")
```

也可以直接抛出异常类型，表示使用无参构造创建异常对象

```python
# 等效于 raise ValueError()
raise ValueError
```

当位于 `except` 块中时，可直接抛出原异常，也可以构造一条异常链
- 使用不带异常的 `raise` 直接抛出当前上下文的异常
- 使用 `raise 异常类型 from exc` 构造异常链，`exc` 可以是 `Exception` 的对象或 `None`

```python
try:
    # ...
except ValueError:
    # 直接抛出该异常
    raise
except NameError as e:
    # 构造一条异常链
    raise RuntimeError from e
```

触发异常时，可以通过 `add_note` 为异常附加一些信息，多用于重新抛出的异常

```python
try:
    # ...
except Exception as e:
    # 添加一些信息
    e.add_note("Some information...")
    e.add_note("Other information...")
    raise
```

# 异常组

可以同时抛出多个异常，使用 `ExceptionGroup` 异常类

```python
# 待触发的
excs = [OSError(), SystemError()] 
raise ExceptionGroup("异常组信息", excs)
```

要同时捕捉异常组中的异常，通过 `except*` 为每个异常类型创建异常分支，符合条件的异常将依次匹配。

```python
try:
    excs = [OSError(), SystemError()] 
    raise ExceptionGroup("异常组信息", excs)
except* OSError:
    # 触发 OSError 异常
    pass
except* SystemError:
    # 触发 SystemError 异常
    pass
```

但若一个异常组中带有另一个异常组，子异常组中的信息不会被捕获

```python
try:
    excs = [OSError(), SystemError(), ExceptionGroup]
    raise ExceptionGroup("异常组信息", excs)
except* OSError:
    # 触发 OSError 异常
    pass
except* SystemError:
    # 触发 SystemError 异常
    pass
```

> 但实际测试中存在问题：捕获的 e 只会触发一次，类型为 ExceptionGroup；或者，不用捕获的异常变量，但未捕获变量却不会触发

