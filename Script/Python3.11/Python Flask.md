# 使用

```python
from flask import Flask

# 创建 app
app = Flask(__name__)


@app.route('/')
def hello_world():
    return 'Hello world'


# 运行
app.run()
```

- `run` 参数：
	- host：允许访问的主机，`0.0.0.0` 表示任何主机均可访问。默认仅本地可访问
- 返回字符串即返回到客户端的响应，也可以返回通过库中的 `make_response` 创建的 response 对象

# 路由

使用 `@app.route(路径)` 设置路由，路径是一个字符串，**必须以 `/` 开头**

## 变量

路由中可以使用变量，使用 `<>` 包围，变量名与函数参数名相同

```python
@app.route('/greet/<username>')
def greet(username):
    return f"Hello, {username}"
```

默认传入的参数为不接受 `/` 的字符串，也可以通过 `<类型:变量名>` 的形式指定变量类型，支持以下类型：
- `string:` - 默认，不接受 `/` 的字符串
- `int:` - 整形
- `float:` - 浮点，整数不行
- `path:`  - 可以使用 `/` 的字符串
- `uuid:` - UUID 字符串

```python
@app.route('/calc/<int:a>+<int:b>')
def add(a, b):
    return f"{a}+{b}={a+b}"
```

## 重定向

- 根便须使用 `/`：`@app.route('/')`
- 带有路径时，访问不带 `/` 的路径且存在带 `/` 的路由时会重定向到带 `/` 的路径

```python
# 可通过 /test/ 访问
# 可通过 /test 访问，且重定向到 /test/
@app.route('/test/')
```

- 带有路径时，访问带 `/` 的路径不会重定向到不带 `/` 的路径，可避免搜索引擎重复索引

```python
# 可通过 /test 访问
# 不可通过 /test/ 访问
@app.route('/test')
```

- 通过返回 `redirect(地址)` 可以进行重定向
	- redirect 可以用 app 的，也可以从 `flask` 包中直接导入
	- 不加 `https://` 会以主机为基础向下导航

```python
@app.route('/baidu')
def baidu():
    return app.redirect('https://www.baidu.com')


@app.route('/baidu2')
def baidu():
    # 重定向到 '当前主机/www.baidu.com'
    return app.redirect('www.baidu.com')
```

# HTTP 方法

路由中传入一个字符串列表以定义其访问方法

```python
@app.route('...', methods=['GET', 'POST'])
```

# 渲染模板

[快速上手_Flask中文网](https://flask.net.cn/quickstart.html#id10)

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