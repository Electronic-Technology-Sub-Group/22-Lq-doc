# 路由

对一个方法使用 `@app.route(...)` 方法装饰即可创建一个路由，内第一个参数（rule）即相对主机名后的路径。在测试环境下主机名为 `localhost`，`127.0.0.1` 或当前 IP 地址（及端口号）

```python
@app.route('/')
def hello_world():  # put application's code here
    return 'Hello World!'
```

以上代码创建了一个 `localhost/` 和 `localhost` 的路由，其访问后显示内容为 `Hello World!`

![[Pasted image 20230730090016.png]]

```ad-tip
1. 所有路由必须以 `/` 开头
2. 给定路由若以 `/` 结尾，则访问地址可以省略 `/`
   - `app.route('/hello/')` 既可以被 `/hello/` 命中，又可以被 `/hello` 命中
3. 给定路由若不以 `/` 结尾，则访问地址也不可以加 `/`
   - `app.route('/hello')` 只能被 `/hello` 命中，不能被 `/hello/` 命中
```

## 参数

### 拼接在 URL 中

若我们要匹配某些带有参数的地址，如访问用户页面需要类似 `/profile/用户id` 之类的格式，可通过 `<>` 修饰，并在被修饰函数中传入同名参数即可

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
- `any(a,b,c)`：定义几种固定的取值（枚举），每种取值之间以 `,` 分割（给定例子是 a b 或 c）

```python
@app.route('/calc/<int:a>+<int:b>')
def add(a, b):
    return f"{a}+{b}={a+b}"
```

### GET 查询

即形如 `xxx?a=xx&b=xxx` 的形式，需要通过 `request` 对象的 `args` 属性获取，该属性对象为一个类字典对象

```python
from flask import request

@app.route('/book/list')
def book_list():
    """
    匹配：
    /book/list
    /book/list?page=n
    :return:
    """
    page = request.args.get('page', 1, int)
    return f'您当前访问的是第 {page} 页'
```

# 重定向

通过返回 `redirect(地址)` 可以进行重定向
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
