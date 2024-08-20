# HTML 模式

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Good Thymes Virtual Grocery</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" type="text/css" href="../css/thymeleaf.css" data-th-href="@{/css/thymeleaf.css}" />
</head>
<body>
<div>
    <img src="../images/thymeleaf-logo.png" alt="logo">
    <p data-th-text="#{home.welcome}">Welcome to our grocery store!</p>
</div>
</body>
</html>
```

使用 `th:` 的方式进行标注，若 IDE 出错，可以添加 `xmlns:th` 命名空间：`xmlns:th="http://www.thymeleaf.org"`。  
*该命名空间是否添加对模板引擎的处理没有影响*

使用 `th:` 的方式不符合 HTML5 的格式要求（虽然浏览器会自动忽略不识别的标签），可使用 `data-` 前缀属性设置：`th:text` 等效于 `data-th-text`

`th-属性` 也可以作为方言之一，符合 W3C 自定义元素规范：`th:text` 等效于 `th-text`

* 修改标签内容
* 修改标签属性

Thymeleaf 很多功能由特定属性完成，不同属性的优先级如下（自上而下逐渐递减）：

* 片段引用：`th:insert`，`th:replace`
* 迭代：`th:each`
* 条件评估：`th:if`，`th:unless`，`th:switch`，`th:case`
* 局部变量：th:object，th:with
* 属性修改

  * 常规属性修改：`th:attr`，`th:attrprepend`，`th:attrappend`
  * 特定属性修改：`th:属性名` 形式
* 文本修改：`th:text`，`th:utext`
* 片段声明：`th:fragment`
* 片段删除：`th:remove`
