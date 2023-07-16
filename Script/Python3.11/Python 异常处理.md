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

# 异常类

所有异常类继承自 `Exception`