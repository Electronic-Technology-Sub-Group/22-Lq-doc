Flask 是 Python 的一个 Web 开发微框架

```python
from flask import Flask

app = Flask(__name__)


@app.route('/')
def hello_world():  # put application's code here
    return 'Hello World!'


if __name__ == '__main__':
    app.run()

```

以 Flask 类为入口，传入的参数：
- `import_name`：可用于 Log 及根据相对路径查找资源

```ad-note
在运行参数中加入 `--host=0.0.0.0` 可以允许除本地外其他设备访问 Flask 服务器
在运行参数中加入 `--port=xxx` 可以将监听端口设置为 xxx（默认 5000）
多个运行参数使用空格分割

![[Pasted image 20230730085139.png]]
```

以下内容默认创建的 Flask 对象名为 `app`
- [[Flask 路由]]
- [[Flask Session]]
- [[Flask 修饰器]]
- [[Flask 模板渲染]]
- [[Flask 配套插件]]

# Debug mode

Debug 模式下，允许我们在文件变化并保存后自动重载，不需要重新启动程序即可看到修改后的结果。

在 Pycharm 中，可以直接配置 Debug Mode 开启：

![[Pasted image 20230730084421.png]]

实际上，这是通过环境变量实现的

```bash
# windows
set FLASK_DEBUG=1
# linux
export FLASK_DEBUG=1
```

注意：当 `FLASK_ENV` 环境变量为 `development` 时默认开启调试模式，但 `FLASK_ENV` 默认为 `production`