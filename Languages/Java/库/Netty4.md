---
类型: 网络
版本: 4.1.97 Final
参考资料: https://waylau.gitbooks.io/netty-4-user-guide/content/
包名: io.netty:netty-all、
---
```ad-note
Netty 5 由于设计问题停止开发，现在最新版就是 Netty 4
```

Netty 为一个使用异步事件驱动（asynchronous event-driven）的网络应用框架，基于 NIO，简化网络程序的开发过程，用于解决通用通信协议无法处理的问题，如通过网络通信处理大文件、邮件或实时信息，通过专有协议与旧系统进行通信等。

![[components.png]]
# 例1 DISCARD

抛弃服务 DISCARD 是一个最简单的服务器实现。这种服务器接收任何数据，并将其直接抛弃，不响应任何数据

服务器类：Netty4 服务器端的基本使用方法
- `EventLoopGroup`：处理传输的 IO 多线程事件循环器，通常使用 `NioEventLoopGroup`，负责线程池和接收信息的调度
	- boss：接收进来的连接，会将连接传递给 worker
	- worker：处理要发送出去的数据
- `ServerBootstrap`：服务器 NIO 启动辅助类。一般不会直接从该类使用 Channel
	- `group`：注册事件循环器
	- `channel`：如何将传入的连接转换成 `Channel`，这里使用 `NioServerSocketChannel`
	- `childHandler`：处理已接收的 `Channel` 的处理器
		- `ChannelInitializer`：初始化一个处理管道，使一个 `Channel` 依次经过多个处理器
		- `DiscardServerHandler`：自定义的处理器，仅丢弃数据
	- `option`：`Channel` 实现的配置参数，`TCP/IP` 服务器可实现 `SO_KEEPALIVE` 等
		- `option`：传递给 `NioServerSocketChannel` 接收的连接
		- `childOption`：传递给由父管道 `SocketChannel` 接收的连接
	- `bind()`：绑定监听端口并阻塞等待，返回的是一个 `Future`，表示一个（可能）还未发生的 IO 事件，Netty 的所有操作都是异步的
		- `closeFuture()`：关闭连接，同样也是个异步事件
		- `sync()`：将异步事件转换为同步事件，阻塞等待事件完成

```java
public class DiscardServer {

    private final int port;

    public DiscardServer(int port) {
        this.port = port;
    }

    public void run() throws InterruptedException {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new DiscardServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = bootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } finally {
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }
    }
}
```

其中，`DiscardServerHandler` 为接收的数据处理器，直接销毁数据
- `ChannelInboundHandlerAdapter` 类可用于处理输入数据，只需要重写对应方法即可
- `ReferenceCountUtil` 工具类提供对数据对象的工具方法。该实例 `msg` 的类型是 `ByteBuf`，但可以使用该类的方法提供一致性行为
-  数据处理完成后使用 `release()` 方法销毁

```java
class DiscardServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ReferenceCountUtil.release(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
```
# 例2 Echo 服务器

应答服务器，接收数据并原样返回。比起 `Discard` 抛弃服务多了一步返回数据，所以只需要将 `DiscardServerHandler` 改动一下即可。这里将其名称重命名为 `EchoServerHandler`

```java
class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ctx.write(msg);
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
```

`ChannelHandlerContext` 类提供了大量与流相关的操作，`write` 和 `flush` 分别表示写入缓冲区、提交缓冲区。当然也可以使用 `writeAndFlush` 一步到位
# 例3 时间服务器与客户端

服务器方面，同样只是将 Handler 改变一下即可。

由于我们不关心任何客户端发送过来的数据，凡是客户端发送的数据，我们都直接回复当前时间，因此我们重写 `channelActive` 方法而不是 `channelRead` - 不进行读操作。
- 在准备数据时，我们使用了 `ChannelHandlerContext#alloc()` 返回的申请器申请了一个 `ByteBuf`
- 发送数据返回的仍是一个 `Future`，Netty 的操作都是异步的，我们需要注册监听器等待发送完成后关闭连接

```java
class TimerServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // 准备时间数据
        ByteBuf buffer = ctx.alloc().buffer(4);
        buffer.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));
        // 发送数据
        ChannelFuture future = ctx.writeAndFlush(buffer);
        future.addListener((ChannelFutureListener) f -> ctx.close());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
```

客户端方面，创建客户端的步骤与服务器差距不大，但有以下区别：
- 使用 `Bootstrap` 代替 `ServerBootstrap`，没有传输模式选择
- 通道使用 `NioSocketChannel` 代替 `NioServerSocketChannel`
- 客户端连接相对简单，只需要定义一个 `EventLoopGroup` 即可，该对象同时承担 `worker` 和 `boss` 的职责
- 由于客户端 `SocketChannel` 没有 `parent`，因此无需设置 `childOption`
- 连接时使用 `connect` 表示连接一个服务器，而不是使用 `bind` 绑定监听端口

```java
public class TimeClient {
    private final int port;
    
    public TimeClient(int port) {
        this.port = port;
    }
    
    public void run() throws InterruptedException {
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(worker)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new TimeClientHandler());
                        }
                    });
                    
            ChannelFuture future = bootstrap.connect("localhost", port).sync();
            future.channel().closeFuture().sync();
        } finally {
            worker.shutdownGracefully();
        }
    }
}
```

`TimeClientHandler` 是自定义的处理方法，内部直接读取时间信息并输出

```java
class TimeClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf buf = (ByteBuf) msg;
        try {
            long time = (buf.readUnsignedInt() - 2208988800L) * 1000L;
            System.out.println(new Date(time));
        } finally {
            ctx.close();
            buf.release();
        }
    }
}
```
# Decoder

类似于 `TCP/IP` 等基于流的传输方法，每次连接都是接收一系列字节包。若传输过程中进行了拆分，一次 `channelRead` 接收的数据可能是不完整的。且即使发送端通过多次 `flush` 发送数据，每次接收的也不一定是与每次发送的相同，只能保证最后所有的字节连接起来是相同的

比如，发送一条信息 `ABCDEFGHI`，可能发送的过程被拆分成多个部分如下：

![[Pasted image 20230902124645.png]]

但多次 `channelRead` 中，每次接收的数据可能是：

![[Pasted image 20230902124706.png]]

只能保证最后接收的数据一定是 `ABCDEFGHI`，因此需要在 `Handler` 中合并。示例中使用了以下生命周期方法进行初始化和释放资源
- `handleAdded`：当连接加入 Netty 事件序列时调用
- `handlerRemoved`：当连接从 Netty 事件序列移除时调用

```java
public class ComposeBufferHandler extends ChannelInboundHandlerAdapter {

    private ByteBuf buffer;
    
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        buffer = ctx.alloc().buffer();
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        ReferenceCountUtil.release(buffer);
        buffer = null;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        buffer.writeBytes((ByteBuf) msg);
        ReferenceCountUtil.release(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        // 数据读取完成，使用数据
        System.out.println(buffer.toString());
    }
}
```
## ByteToMessageDecoder

Netty 提供 `ByteToMessageDecoder` 类用于处理流数据。该类是 `ChannelInboundHandler` 的一个实现，应重写 `decode` 方法
- 自动累计接收的 `ByteBuf` 数据流，第二个参数 `in` 即累积的数据流
- 每当判断数据流可以组成一个完整的数据，在方法中构造出对应数据并加入第三个参数 `out` 列表中

以下示例中，当接收的数据超过 4 个字节，就读出一个无符号整型数据存入输出列表

```java
public class MyByteToMessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (in.readableBytes() >= 4) {
            out.add(in.readUnsignedInt());
        }
    }
}
```

Netty 内置了一系列 `ByteToMessageDecoder` 实现，用于处理不同数据协议（如 HTTP，SSL 等）、不同数据类型（XML，JSON，Zlib 等）

![[Pasted image 20230902144618.png]]

经过这类 Decoder 后，通过管道传递到后面的对象就不是 ByteBuf 了，对应 `channelRead` 的 `msg` 对象就是对应上个 Decoder 输出的类型了。
## MessageToMessageDecoder

`MessageToMessageDecoder` 用于处理来自 `ByteToMessageDecoder` 输出的数据，其中泛型 `<T>` 是`ByteToMessageDecoder` 解析出的数据类型。
# Encoder

接收数据有 `ByteToMessageDecoder`、`MessageToMessageDecoder`，而发送数据也有对应的 `MessageToByteEncoder`，`MessageToMessageEncoder` 将 Java 对象转换成 ByteBuf，对应 `ChannelOutboundHandlerAdapter`
# Buffer

Netty 的缓冲区使用自己的 `ByteBuf` 而非 NIO 的 `ByteBuffer`，相对起来有如下优势：
- 自定义缓冲类型，读写操作更方便
- 透明零拷贝实现，使用 `Unpooled#wrappedBuffer()` 方法可以将多个 Buffer 直接合并
- 开箱即用的动态缓冲区，支持自动扩充缓冲区大小
- 不需要 `flip()` 操作
- 正常情况下，响应效率比 `ByteBuffer` 更好，没有复杂的边界和索引补偿
# ChannelHandler

ChannelHandler 接口可以方便的实现自己的业务，如日志、编解码、过滤等。该接口只定义了 handlerAdded，handlerRemoved，exceptionCaught 方法。
- handlerAdded：添加到实际上下文并开始处理事件时调用
- handlerRemoved：从上下文中移除时调用
- exceptionCaught：处理时出现异常时调用
- Sharable：注解，表示该 ChannelHandler 可以在多个 ChannelPipeline 中共享
## Adapter

由于 ChannelHandler 过于简单，具体业务在各种 ChannelHandlerAdapter 中实现。
## flush

flush 负责将内容写入到 SocketChannel 并发送
## ip 过滤

位于 io.netty.handler.ipfilter 下，Netty 内置了一套实现 IP 地址过滤的机制，可以方便的实现白名单、黑名单等功能。
### RuleBasedIpFilter

允许根据一定的规则进行过滤，设定对应 IP 的 ACCEPT 或 REJECT，对于单个 IP 可以通过 `IpSubnetFilterRule` 定义
### UniqueIPFilter

保证每个 ip 在同一时间只有一个连接到服务器的通道。

### 流量整形

限制服务端发送速度，防止客户端来不及接收造成丢包

原理：报文发送过快时，将其进行缓存，当缓冲区满时向客户端发送信息告知服务器不再接受消息

流量整形可能增加延迟。

Netty 提供了 AbstractTrafficShapingHandler 用于流量整形，同时也提供了 TrafficCounter 进行流量统计。

- 管道流量整形：ChannelTrafficShapingHandler
- 全局管道流量整形：GlobalChannelTrafficShapingHandler
- 全局流量整形：GlobalTrafficShapingHandler
### TrafficCounter

该类用于统计限速流量。

在给定 checkInterval 内，周期性计算入站、出站流量信息，并调用 AbstractTrafficShapingHandler.doAccounting 方法。若 checkInterval == 0 则不进行计数。