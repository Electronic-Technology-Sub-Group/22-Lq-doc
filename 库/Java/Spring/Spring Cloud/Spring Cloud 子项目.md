# 服务治理

Spring Cloud 的核心，可选基于 `Eureka` 的 `Spring Cloud Netflix` 和基于 `Consul` 的 `Spring Cloud Consul`

```cardlink
url: https://spring.io/projects/spring-cloud-consul
title: "Spring Cloud Consul"
description: "Level up your Java code and explore what Spring can do for you."
host: spring.io
favicon: https://spring.io/favicon-32x32.png?v=96334d577af708644f6f0495dd1c7bc8
image: https://spring.io/img/og-spring.png
```

`Eureka` 在 Spring Cloud 中分为 `Spring Cloud Netflix Eureka Server` 和 `Eureka Client` 两个依赖：
- `Eureka Server`：提供服务注册与发现功能
- `Eureka Client`：向 `Eureka Server` 注册服务，从 `Eureka Server` 获取服务，客户端负载均衡等

# 客户端负载均衡

`Spring Cloud` 对 `Netflix` 的 `Ribbon` 二次封装，与 `Eureka` 无缝整合，实现客户端负载均衡。

`Spring Cloud OpenFeign` 集成了 `OpenFeign`（`Netflix Feign` 的社区版），默认集成了 `Ribbon`

# 容错与降级

Spring Cloud 集成 `Netflix` 的 `Hystrix` 项目，通过断路器模式处理服务降级

> [!fail] 由于 `Hystrix` 不再积极维护，Spring Cloud 推荐使用其他断路器实现，如 `Resilience4j` 等 

# 服务网关

Spring Cloud 集成 `Zuul` 项目实现网关功能
- 路由：将外部请求转发至微服务，实现统一访问入口
- 过滤：干预请求过程，实现请求校验、服务聚合等功能

`Zuul` 提供反向代理功能，可实现路由寻址

# 消息中间件

`Spring Cloud Stream` 子项目提供消息收发等功能，可方便的与 `Kafka`、`RabbitMQ` 等中间件集成

`Spring Cloud Bus` 子项目对 Stream 项目进行扩展，可作为消息总线，用于服务器集群中状态的传播

# 分布式配置

`Spring Cloud Config` 子项目具有中心化、版本控制、动态更新和语言独立等特性
- `Config Server`：配置服务器，集中管理所有配置文件
- `Config Client`：配置客户端

# 微服务链路追踪

`Spring Cloud Sleuth` 子项目提供微服务之间调用的链路追踪，通过全局 ID 串联各个微服务节点的请求

可以将采集的数据转发到 `Zipkin` 存储、统计和分析

# 微服务安全

`Spring Cloud Security` 提供认证和鉴权机制，默认支持 `OAuth 2.0`

# 其他

| 组件                       | 功能                             |
| ------------------------ | ------------------------------ |
| `Spring Cloud Cli`       | 使用命令行管理微服务                     |
| `Spring Cloud Data Flow` | 基于原生云，用于开发、执行大数据处理的统一编程模型和托管服务 |
| `Spring Cloud Task`      | 用于短时间的任务管理和调度微服务管理             |
| `Spring Cloud Contract`  | 契约框架，用于微服务测试                   |

# 参考

```cardlink
url: https://spring.io/projects/spring-cloud
title: "Spring Cloud"
description: "Level up your Java code and explore what Spring can do for you."
host: spring.io
favicon: https://spring.io/favicon-32x32.png?v=96334d577af708644f6f0495dd1c7bc8
image: https://spring.io/img/og-spring.png
```
