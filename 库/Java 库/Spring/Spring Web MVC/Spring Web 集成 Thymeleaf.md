# Spring Web 集成 Thymeleaf

Spring Web 内置 Thymeleaf 支持，只需要简单配置即可：

1. 引入 Spring 的 Thymeleaf 依赖：`org.springframework.boot:spring-boot-starter-thymeleaf`
2. 在 `application.properties` 中进行配置

    ```properties
    spring.thymeleaf.prefix=classpath:/templates/
    spring.thymeleaf.suffix=.html
    spring.thymeleaf.mode=HTML5
    spring.thymeleaf.encoding=UTF-8
    spring.thymeleaf.cache=true
    ```

‍
