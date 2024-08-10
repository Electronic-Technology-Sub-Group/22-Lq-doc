# 网页请求附加 CSRF

为请求附加 `csrf`：

* JSP：使用 `<security:csrfInput/>`
* Thymeleaf：一般情况下不需要手动配置，Spring Security 默认自动处理这一过程

  ```html
  <form ...>
      <input type="hidden" name="_csrf" th:value=${_csrf.token}>
  </form>
  ```
