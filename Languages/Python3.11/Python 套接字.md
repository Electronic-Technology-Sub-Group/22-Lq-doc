Python 套接字编程使用 `socket` 实现

# 服务器

1. 构建 socket 对象
2. 绑定要监听的 IP 和端口
3. 设置连接数量，开始监听
4. 等待客户端连接，连接后返回连接对象和地址
	- 接收消息：`conn.recv(长度_字节)`，返回 `bytes` 对象，可通过 `decode` 方法转换为字符串
	- 发送消息：`conn.send(数据)`
	- 关闭连接：`conn.close()`
5. 不再接收消息后，使用 `socket.close()` 关闭服务端

```python
import socket

socket_server = socket.socket()
socket_server.bind(('localhost', 8080))
socket_server.listen(10)

# 阻塞等待连接，连接后返回值类型：tuple(conn, tuple(host, port))
conn, (hostaddr, port) = socket_server.accept()
# 已连接
print(f"已连接，来自 {hostaddr}:{port}")
# 接收消息
data = conn.recv(1024).decode('utf-8')
# 发送数据
conn.send(data.encode('utf-8'))
# 结束通信
conn.close()

socket_server.close()
```

# 客户端

1. 创建 socket 对象
2. 连接服务端
3. 通信
4. close() 关闭

```python
socket_client = socket.socket()
# 阻塞并等待连接
socket_client.connect(('localhost', 8080))
# 发送数据
socket_client.send(data.encode('utf-8'))
# 接收数据
data = socket_client.recv(1024).decode('utf-8')
# 关闭
socket_client.close()
```
