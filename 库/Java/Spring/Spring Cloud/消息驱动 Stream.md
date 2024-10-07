
`````col
````col-md
flexGrow=1
===
`Spring Cloud Stream` 子项目提供消息收发等功能，可方便的与 `Kafka`、`RabbitMQ` 等中间件集成

`Spring Cloud Bus` 子项目对 Stream 项目进行扩展，可作为消息总线，用于服务器集群中状态的传播。

当微服务 A 要发送数据给微服务 B 时，通过消息中间件负责网络处理。若 B 不在线，A 可以将消息存储于数据库，当 B 上线时再发。
- 消息发送接口 Source：用于 Spring Cloud Stream 与外界通道绑定，序列化待传送对象
- 消息通道 Channel：对消息队列的抽象
- 消息绑定器 Binder：中间层，实现应用程序与具体消息中间件细节的隔离
- 监听接口 Sink：类似消息发送接口 Source，负责反序列化 Java 对象
````
````col-md
flexGrow=1
===
![[../../../../_resources/images/消息驱动 Stream 2024-10-07 00.06.27.excalidraw]]
````
`````
# Spring Cloud Stream 结构

Spring Cloud Stream 使用订阅者模式

# 添加消息功能

## Kafka 消息系统

Spring Cloud Stream 只是一个消息系统的接口，实际还需要一个消息系统实现，支持 Kafka、RabbitMQ 等工具

Apache Kafka 是一个分布式发布-订阅消息系统，使用 Scala 编写

```cardlink
url: https://kafka.apache.org/downloads
title: "Apache Kafka"
description: "Apache Kafka: A Distributed Streaming Platform."
host: kafka.apache.org
favicon: https://kafka.apache.org/images/apache_feather.gif
image: http://apache-kafka.org/images/apache-kafka.png
```

- 主题：Topic，一个消息类别。物理上不同消息分别储存，逻辑上相同消息也可能在多个代理 Broker 中
- 生产者：Producer，消息发布者
- 消费者：Consumer，从消息代理 Broker 获取并处理消息。
	- 消费者组：Consumer Group，消息会随机发送到同一消费者组里的一个消费者
- 消息代理：Broker，Kafka 服务器集群中的一个节点，消费者从代理中获取消息
- 消息分区：Partition，主题消息数据分割为多个分区，每个分区数据分割为多个 Segment。
	- 每个分区中消息是有序地，但多个分区之间是无序的 - 若要保证所有消息有序应指定固定分区 1
	- 偏移量：offset，消息在分区的唯一编号

```bash
# 启动内嵌 ZooKeeper 服务器
.\bin\windows\zookeeper-server-start .\config\zookeeper.properties
# 启动 Kafka 服务器
.\bin\windows\kafka-server-start .\config\server.properties
```

测试（发送端和接收端分别是两个命令行窗口）：

```bash
# 创建 Topic
.\bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic springcloud-msg
# 发送端
.\bin\windows\kafka-console-producer.bat --bootstrap-server localhost:9092 --topic springcloud-msg
# 接收端
.\bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic springcloud-msg --from-beginning
```

在发送端发送的数据，接收端应该可以看到

## Redis 缓存数据库

Redis 是一个将数据缓存在内存中，再定期写入文件中的键值对数据库

> [!warning] 官方版本 Windows 下需要 WSL 支持，也可以使用他人编译的[非官方版本](https://github.com/zkteco-home/redis-windows)

```cardlink
url: https://redis.io/docs/latest/get-started/
title: "Community Edition"
description: "Get started with Redis Community Edition"
host: redis.io
favicon: https://redis.io/docs/latest/images/favicons/favicon-196x196.png
```

Spring 依赖：`org.springframework.boot:spring-boot-starter-data-redis`（在 NoSQL 分类中），内置 Lettuce 客户端，配置后可直接使用 `RedisTemplate` 读写数据库