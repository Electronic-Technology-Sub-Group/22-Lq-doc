# RESTful Web 服务

`REST`，表征状态传输，是一种架构风格。应用程序资源由 URI 统一管理，客户端通过 HTTP 请求与资源进行交互。

与基于 SOAP 的 Web 服务相比，实现更简单，更具有扩展性。传输的数据不必要是 XML，渲染等也可以完全交给客户端完成。

* 在服务器端，Spring 在控制器中可以直接将对象构建为 Json 格式的 REST 信息
* Spring 也提供 `RestTemplate` 和 `WebClient` 用于构建访问 RESTful 服务的客户端
