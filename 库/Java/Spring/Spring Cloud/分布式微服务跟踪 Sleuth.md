`Spring Cloud Sleuth` 子项目提供微服务之间调用的链路追踪，通过全局 ID 串联各个微服务节点的请求
- 服务追踪：为同一个用户的请求添加相同的 TraceID，用于识别完整链路
- 服务监控：为每个微服务处理生成独立的 SpanID，记录请求到达和离开时间

`````col
````col-md
flexGrow=1
===
![[../../../../_resources/images/分布式微服务跟踪 Sleuth 2024-10-06 13.35.24.excalidraw|70%]]
````
````col-md
flexGrow=1
===
> [!note] 相关术语
> - Span：Sleuth 基本工作单元，一个请求，一般成对出现，可携带其他数据
> - Trace：一次用户请求涉及的 Span 集合 
> - Annotation：记录时间信息
> 	- cs：Client Send，客户端发送，Span 起点
> 	- sr：Server Received，服务端接收
> 	- ss：Server Send，服务端完成
> 	- cr：Client Received，客户端接收
````
`````

只需要在项目中添加 `org.springframework.cloud:spring-cloud-starter-sleuth` 依赖即可启用

> [!missing] Spring Cloud 2022.0 开始，使用 Micrometer Tracing 替代 Spring Cloud Sleuth，该项目是 Sleuth 的一个分支
> - [ ] Tracing, ELK(Elasticsearch Logstash Kibana), Zipkin
