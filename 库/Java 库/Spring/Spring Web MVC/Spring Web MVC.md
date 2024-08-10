# Spring Web MVC

Spring Web MVC 框架是一个非侵入式 MVC<sup>(Model-View-Controller)</sup> 框架,可用于开发基于 Servlet 的 Web 应用。一个 Spring Web MVC 项目的目录结构可能如下：

|目录|说明|
| ------| --------------------------------------------------------------|
|`src/main/resources/META-INF/spring`|根 Web 应用上下文的 Spring XML 文件，定义 服务和 Dao 类|
|`src/main/webapp/WEB-INF/spring`|Web 应用上下文的 Spring XML 文件，定义控制器、处理程序映射等|
|`src/main/webapp/WEB-INF/jsp`|Web 应用的 JSP 文件|
|`src/main/java`|Java 类|
|`src/test`|调试目录|

一个简单的 `helloworld` 项目只需要包含以下几个文件：

* `HelloWorldController`：请求 MVC 控制器
* `helloworld.jsp`：用于显示的 jsp 文件
* `myapp-config.xml`：Web 应用程序上下文 XML 文件，包含控制器的 bean 定义
* `web.xml`：Web 应用程序部署描述符

```java
public class HelloWorldController implements Controller {

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, String> modelData = new HashMap<>();
        modelData.put("message", "Hello World!");
        return new ModelAndView("helloworld", modelData);
    }
}
```

`HelloWorldController` 为一个 Spring 控制器，负责根据请求处理逻辑，可以实现 `Controller` 接口或 `@Controller` 注解，其中控制函数返回 `ModelAndView` 对象：

* 模型数据：向用户显示数据，通常为一个 Map 作为 JSP 模板的各个值。
* 逻辑名称：展示模型数据的 JSP 页面

<iframe src="/widgets/widget-excalidraw/" data-src="/widgets/widget-excalidraw/" data-subtype="widget" border="0" frameborder="no" framespacing="0" allowfullscreen="true" style="width: 1076px; height: 412px;"></iframe>

使用 `pageContext` 可以获取当前请求的目录，可用于组装链接：  
`<form name="fixedDepositList" method="post" action="${pageContext.request.contextPath}/fixedDeposit/list}">`

```jsp
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:out value="${message}" />
```

`helloworld.jsp` 使用 `c:out` 输出 `message` 参数的值

新版本 Spring 不再支持 jsp，后面使用 <span data-type="text" parent-style="color: var(--b3-card-error-color);background-color: var(--b3-card-error-background);">Thymeleaf</span> 替代。Spring 使用 Thymeleaf 只需要<span data-type="text" parent-style="color: var(--b3-card-error-color);background-color: var(--b3-card-error-background);">简单配置</span>即可。

旧版本 Spring 对于 JSP 提供 `form` 标签支持（`<form:xxx>`），由于 Spring 不再支持 JSP，此部分不再描述

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean name="helloWorldController" class="com.example.mybank.web.HelloWorldController" />

    <bean id="handlerMapping" class="org.springframework.web.servlet.handler.SimpleUrlHandlerMapping">
        <property name="urlMap">
            <map>
                <entry key="/sayHello" value-ref="helloWorldController" />
            </map>
        </property>
    </bean>

    <bean id="viewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="prefix" value="/WEB-INF/jsp/" />
        <property name="suffix" value=".jsp" />
    </bean>
</beans>
```

`myapp-config.xml` 声明控制器定义，声明和配置了三个控制 bean 类。

`HandlerMapping` 类负责将传入的 HTTP 请求映射到负责处理的控制器，`SimpleUrlHandlerMapping` 根据 URL 将请求直接转发给控制器 - 实例中将 `/sayHello` 重定向到了控制器 `helloWorldController`

<iframe src="/widgets/widget-excalidraw/" data-src="/widgets/widget-excalidraw/" data-subtype="widget" border="0" frameborder="no" framespacing="0" allowfullscreen="true" style="width: 1143px; height: 392px;"></iframe>

`InternalResourceViewResolver` 类负责根据 `ModelAndView` 中包含的视图名称查找实际视图，这里指定了前缀和后缀。

```xml
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
         version="3.0">

    <servlet>
        <servlet-name>hello</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>/WEB-INF/spring/myapp-config.xml</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>

    <servlet-mapping>
        <servlet-name>hello</servlet-name>
        <url-pattern>/helloworld/*</url-pattern>
    </servlet-mapping>
</web-app>
```

`web.xml` 为 Web 应用部署描述。该文档根标签为 `<web-app>`。

`<servlet>` 标签声明了一个 Servlet，这里使用 DispatcherServlet，请求由该 Servlet 截取并发送到相应服务器。

`contextConfigLocation` 配置一个 Servlet XML 文件，默认为 WEB-INF 中的 `<servlet-name>-servlet.xml`。

`<mvc:annotation-driven />` 标签对应 `@EnableWebMvc` 注解

Web 应用程序共享 `<web-app>` 配置的对象。可以将程序的 XML 配置在此处通过 `ContextLoaderListener` 加载

```xml
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
         version="3.0">

    ...

    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath*:/META-INF/spring/applicationContext.xml</param-value>
    </context-param>

    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>
</web-app>
```
