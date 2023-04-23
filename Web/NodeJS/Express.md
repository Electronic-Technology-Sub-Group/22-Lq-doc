#npm库

高度包容、快速而极简的 Node.js Web 框架

[Express - Node.js Web 应用程序框架 (expressjs.com)](http://expressjs.com/zh-cn/)

# 路由

使用路由可快速匹配请求 URL 和处理函数：`app.MATHOD(PATH, FUNC)`

```javascript
app.get('/', (req, res) => { ... })
app.post('/', (req, res) => { ... })

app.listen(8000, () => {})
```

路由的匹配过程为按定义顺序自上而下进行匹配

注意：这里面 `res` 使用 `send` 方法向客户端发送数据

## 创建

## 注册

 