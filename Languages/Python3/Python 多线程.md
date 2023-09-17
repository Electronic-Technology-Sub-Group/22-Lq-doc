Python 多线程模型通过 `threading` 实现

# Thread

```python
thread_obj = threading.Thread(group=None, target=None, name=None, args=(), kwargs=None, *, daemon=None)
```

Thread 类表示一个线程，其构造函数中的参数有：
- group：暂时无用
- target：目标任务（函数）
- args：目标任务参数，元组
- kwargs：目标任务参数，字典
- name：线程名

## 执行

```python
thread_obj.start()
```
