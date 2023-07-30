# HTTP 方法

路由中传入一个字符串列表以定义其访问方法

```python
@app.route('...', methods=['GET', 'POST'])
```

# Request

通过从 `flask` 包中导入 `request` 成员可以获取当前客户端访问的请求。
- `request.method`：请求类型
- `request.json`：请求附带的 JSON 数据
	- `request.get_json()`，`request.is_json`
- `request.form`：请求附带的表单数据

# Cookie

通过 `request.cookies` 获取 cookie

通过 `response.set_cookie()` 设置 cookie

更推荐使用 Session，更安全

# Session

通过导入库中的 `session` 访问

```python
from flask import session

# 使用 session 需要先设定一个密钥
app.secret_key = '密钥'
# 设置 session 信息
session['key'] = 'value'
# 检查是否存在
v = session.get('key')
if v is None:
    # 数据不存在
    pass
# 清空 session 信息
session.clear()
```