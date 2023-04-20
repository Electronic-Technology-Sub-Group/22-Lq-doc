[Axios 中文文档 | Axios 中文网 | Axios 是一个基于 promise 的网络请求库，可以用于浏览器和 node.js (axios-http.cn)](https://www.axios-http.cn/)

专用于网络数据请求的库

# 请求

## 即时请求

`axios(config).then(response) ...`
- `axios(url[, config])`
- `axios.request(config)`
- `axios.get(url[, config])`
- `axios.delete(url[, config])`
- `axios.post(url[, data][, config])`
- `axios.put(url[, data][, config])`
- `axios.head(url[, config])`
- `axios.options(url[, config])`
- `axios.patch(url[, data][, config

## 请求配置

![[axios 请求配置]]

## 默认配置

`axios` 全局的默认请求配置位于 `axios.defaults` 中，配置合并的优先级为：
`lib/defaults.js` < 请求实例的 `defaults` 对象 < 请求的 `config` 对象

### 请求实例

使用自定义的 `config` 创建 `axios` 实例，该实例的默认配置将根据 `config` 的值修改

`const instance = axios.create(config)`

可从任意 `axios` 实例创建实例，可从 `axios.request`，`axios.get`，`axios.post` 等 `axios` 实例创建实例

## 取消请求

使用 `fetchAPI` 取消请求

```javascript
const controller = new AbortController()

// 请求
axios({..., signal: controller.signal})

// 取消请求
controller.abort()
```

# 响应

`axios` 发起请求后返回的值为一个 `Promise` 对象，通过 `then` 函数可以传入响应处理函数

`response` 对象结构如下

![[axios 响应结构]]

# 拦截

在 `axios.interceptors` 中可以设置请求和响应的拦截器
- 请求拦截：`const ireq = axios.interceptors.request.use((config) => config, (error) => ...)`
	- 移除：`axios.interceptors.request.eject(ireq)`
- 响应拦截：`const irsp = axios.interceptors.response.use((response) => response, (error) => ...)`
	- 移除：`axios.interceptors.resonse.eject(irsp)`

# 异常处理

返回的是 `Promise` 对象，因此可以通过 `catch` 函数传入异常处理函数
- `error.request`
- `error.response`
- `error.message`
- `error.config`
- `error.toJSON()`
- `config.validateStatus: (status) => bool`：异常处理函数仅处理某些状态码