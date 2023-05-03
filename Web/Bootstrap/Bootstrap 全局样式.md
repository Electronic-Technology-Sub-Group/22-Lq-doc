全局样式主要是一些对各标签元素渲染效果本身的调整优化

详见 [全局 CSS 样式 · Bootstrap v3 中文文档 | Bootstrap 中文网 (bootcss.com)](https://v3.bootcss.com/css/?#type)，以下是感觉会比较常用的

# 排版

-  `<code>`，`<mark>`，`<kbd>`，`<pre>`，`<var>` 等标签显示优化

# 列表

- `list-unstyled` 应用于 `<ul>`、`<ol>` 等列表标签，其直接子元素移除 `list-style` 属性和左外边距

# 表格

- `table` 类添加在 `<table>` 标签上，为表格添加水平分割线和适当的 `padding`
- 行高亮
	- 在带有 `table` 类的元素上添加 `table-striped` 类为表格奇偶行显示不同颜色，便于阅读
	- 在带有 `table` 类的元素上添加 `table-hover` 类高亮鼠标悬停的行
	- `active`，`success`，`info`，`warning`，`danger` 可以添加到表格、行、列等，添加相应的背景色

![[情景样式.bs#表格]]

- `table-bordered` 为表格整体和每个单元格添加边框
- 在带有 `table` 类的元素上添加 `table-responsive` 类创建响应式表格，显示尺寸过小时添加滚动条

# 表单

**注意：一定要添加 `<label>` 标签。若不需要显示可使用 `sr-only` 类使其隐藏**

- 为任何表单控件元素添加 `form-control` 类，会为其添加 `width: 100%`，将其放置在带有 `form-group` 类的外层标签中可获得最佳效果
- 为 `<label>` 添加 `control-label` 类会有更好效果，包括右对齐等
- 为 `<form>` 添加 `form-inline` 类可使其内容左对齐并表现为 `inline-block`，要求宽度至少为 768px
- 为 `<form>` 添加 `form-horizontal` 类可使 `<label>` 及控件组水平排列，配合栅格类 `col-` 系列进行排版
	- `form-group` 行为将被改变，使其类似 `row`，应使用 `form-group` 替代 `row`

```html
<form class="table form-horizontal">
  <div class="form-group">
    <label for="email" class="col-sm-2 control-label">Email</label>
    <div class="col-sm-10">
      <input id="email" type="email" class="form-control" placeholder="Email">
    </div>
  </div>
  <div class="form-group">
    <label for="pwd" class="col-sm-2 control-label">Password</label>
    <div class="col-sm-10">
      <input id="pwd" type="password" class="form-control" placeholder="Pwd">
    </div>
  </div>
  <div class="form-group">
    <div class="col-sm-10 col-sm-offset-2">
      <div class="checkbox">
        <label><input type="checkbox">Remember me</label>
      </div>
    </div>
  </div>
  <div class="form-group">
    <div class="col-sm-10 col-sm-offset-2">
      <button type="submit" class="btn btn-default">Sign in</button>
    </div>
  </div>
</form>
```

![[Pasted image 20230123225123.png]]

- 为多个 `checkbox` 或 `radio` 添加 `checkbox-inline` 和 `radio-inline` 类，使其排列在同一行
- `form-control-static` 类放在 `<p>` 标签上，使其与 `<label>` 标签置于同一行
- 状态类：`disabled`，`readonly`
	- 校验状态：`has-warning`，`has-error`，`has-success` 等
	- `has-feedback`：为文本输入框添加对应的校验状态图标，添加在其外部元素上，并在带有 `form-control-feedback` 类的元素上附带正确的图标类

# 按钮

- 按钮类 `btn` 可用于 `<a>`，`<button>`，`<input>` 标签，为其赋予按钮样式
- `btn-default/primary/success/info/warning/danger/link` 类赋予带有 `btn` 类的元素上，应用不同预定义样式

![[情景样式.bs#按钮]]

- `btn-lg`，`btn-sm`，`btn-xs` 可获取不同尺寸大小的按钮
- 状态类包括 `active`，`disabled`

# 图片

- 形状：为类添加 `img-rounded`，`img-circle`，`img-thumbnail` 将图片表示为圆角矩形、圆形、缩略图

# 辅助

- `close` 类：可用于关闭模态框和警告框
- `pull-left`，`pull-right`：快速浮动
- `clearfix`：清除浮动
- `show`，`hidden`：可见性
- 响应式：
	- `visible-宽度别名-n`：当屏幕为指定宽度类型时，元素可见且占用 n 格，否则隐藏
	- `hidden-宽度别名`：当屏幕为指定宽度类型时，元素隐藏，否则可见
	- `visible-宽度别名-[block/inline/inline-block]`：当屏幕为指定宽度类型时，设置其 `display` 属性值