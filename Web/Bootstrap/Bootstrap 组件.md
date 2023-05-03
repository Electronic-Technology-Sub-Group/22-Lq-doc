组件包括字体图标和其他需要多种元素组合而成的套件，包括下拉菜单，导航，弹出框等

# 字体图标

## Glyphicons 图标

Bootstrap 允许免费使用 `Glyphicons` 图标，所有图标详见：[组件 · Bootstrap v3 中文文档 | Bootstrap 中文网 (bootcss.com)](https://v3.bootcss.com/components/#glyphicons-glyphs)

在 `<span>` 标签中使用，为该标签添加 `glyphicon` 类和对应图标名类即可，如：

```html
<span class="glyphicon glyphicon-menu-left"></span>  
<span class="glyphicon glyphicon-grain"></span>  
<span class="glyphicon glyphicon-menu-right"></span>
```

![[Pasted image 20230124153755.png]]

注意：
- 字体图标只能在 `<span>` 标签中使用
- 字体图标所在的 `<span>` 标签内容必须为空，且不与其他 Bootstrap 类兼容
- 字体文件默认在 `../fonts/` 目录下
- 图标实际为一个字体，适用于 `color`，`font-size` 等属性

## 其它图标

- `caret` 类表示为一个向下的三角符号；若其位于向上的弹出菜单中（`dropup`）时，则为向上的三角符号

# 输入组

`input-group` 用于文本输入类型的 `<input>` 元素的父类上，为其前后添加文字或按钮
- 避免使用 `<select>`，对 `<textarea>` 支持也不好
- 不要直接在 `input-group` 元素上使用栅格列 `col` 系列等属性，为其创建一个父元素
- `<input>` 一定要存在对应的 `<label>`

## 容器

在存在 `input-group` 类的元素，常为 `<div>` 等
- 尺寸：`input-group-lg`，`input-group-sm` 调整大小

## 元素

容器中的元素，用于组成输入组。其内容包括：
- 有且只有一个文本输入类 `<input>` 标签，通常带有 `form-control` 类
- `<input>` 之前最多可以有一个带有 `input-group-addon` 或 `input-group-btn` 的元素，为左侧元素
- `<input>` 之后最多可以有一个带有 `input-group-addon` 或 `input-group-btn` 的元素，为右侧元素
- `input-group-btn` 的元素内可以包含一个或多个按钮

通常，左右侧元素被包围在一个 `<span>` 元素或 `<div>` 元素中。其内容可以包括大多数元素，甚至可以为一个菜单。

由于 `input-group-addon` 本身包含 `position: relative`，因此可以直接作为菜单的容器

```html
<label for="input1">Email: </label>  
<div class="input-group">  
  <span class="input-group-addon glyphicon glyphicon-grain"></span>  
  <input type="email" id="input1" class="form-control">  
  <span class="input-group-btn"><a class="btn btn-primary" href="#">@qq.com</a></span>  
</div>
```

![[Pasted image 20230125001833.png]]

# 路径导航

通过对列表（`<ul>` 或 `<ol>`）添加 `breadcrumb` 类创建路径导航
- 对 `<li>` 标签添加 `active` 类表示当前位置

```html
<ol class="breadcrumb">  
  <li><a href="#">Root</a></li>  
  <li><a href="#">Folder 1</a></li>  
  <li><a href="#">Folder 2</a></li>  
  <li><a href="#">Folder 3</a></li>  
  <li>Folder 4</li>  
  <li class="active">Current</li>  
</ol>
```

![[Pasted image 20230125194519.png]]

# 分页

为列表元素增加 `pagination` 或 `pager` 类可用于创建分页，前者为一组页码，后者为前后页按钮
- 大小：为带有 `pagination` 类的元素增加 `pagination-lg`，`pagination-sm` 可调整分页栏大小
- 对齐：在 `pager` 元素中，为 `<li>` 元素应用 `previous` 类可置于最左侧，`next` 类用于最右侧
- 状态：为 `<li>` 元素可应用 `active` 与 `disabled` 类

```html
<ol class="pagination">  
  <li><a href="#"><<</a></li>  
  <li><a href="#">1</a></li>  
  <li class="active"><a href="#">2</a></li>  
  <li><a href="#">3</a></li>  
  <li class="disabled"><a href="#">4</a></li>  
  <li><a href="#">>></a></li>  
</ol>
```

![[Pasted image 20230125195603.png]]

```html
<ol class="pager">  
  <li class="previous"><a href="#">首页</a></li>  
  <li><a href="#">上一页</a></li>  
  <li><a href="#">下一页</a></li>  
  <li class="next"><a href="#">尾页</a></li>  
</ol>
```

![[Pasted image 20230125195620.png]]

# 标签与徽章

标签与徽章都是为 `<span>` 元素准备，为其增加 `label 样式` 可创建标签，增加 `badge` 可创建徽章

标签支持[[情景样式.bs#标签]]

```html
<h3>新消息 <span class="badge">42</span></h3>
<h3><a class="btn btn-primary" href="#">新消息 <span class="badge">42</span></a></h3>
<div><span class="label label-warning">New!</span></div>
```

![[Pasted image 20230125202105.png]]

# 缩略图

使用 `thumbnail` 类可以很容易的创建缩略图，或带有缩略图的块，其显示为带一个细线框的圆角矩形

```html
<div class="row">
  <div class="col-sm-3"><a href="#" class="thumbnail"><img src="img/dc1.jpg" alt=""></a></div>
  <div class="col-sm-3"><a href="#" class="thumbnail"><img src="img/dc2.jpg" alt=""></a></div>
  <div class="col-sm-3"><a href="#" class="thumbnail"><img src="img/dc3.jpg" alt=""></a></div>
  <div class="col-sm-3"><a href="#" class="thumbnail"><img src="img/dc4.jpg" alt=""></a></div>
  <div class="col-sm-3"><a href="#" class="thumbnail"><img src="img/dc5.jpg" alt=""></a></div>
  <div class="col-sm-3"><a href="#" class="thumbnail"><img src="img/dc6.jpg" alt=""></a></div>
  <div class="col-sm-3"><a href="#" class="thumbnail"><img src="img/dc7.jpg" alt=""></a></div>
  <div class="col-sm-3"><a href="#" class="thumbnail"><img src="img/dc8.jpg" alt=""></a></div>
</div>
```

![[Pasted image 20230125203812.png]]

# 进度条

进度条需要一个外层包含 `progress` 类的元素和至少一个内层包含 `progress-bar` 类的元素，通常都是 `<div>`，分别表示进度条的轮廓和填充，进度条的相关内容基本都在填充元素，即带有 `progress-bar` 的元素中设置
- 填充元素的宽度（`width` 属性）代表进度条的进度，常使用百分比的形式设置
	- 存在多个元素时可以横向叠加
- 可在填充元素内添加内容，将居中显示在已填充区域，常用于显示进度
	- 为使填充内容能正常显示，通常可以设置 `min-width` 属性
- 为填充元素增加 `progress-bar-striped` 可以增加条纹效果
	- 同时包含 `active` 类，可增加自左向右的动画效果
- 填充元素支持[[情景样式.bs#进度条]]

```html
<div class="progress">
  <div class="progress-bar progress-bar-danger" style="width: 20%"></div>
  <div class="progress-bar progress-bar-striped" style="width: 30%"></div>
  <div class="progress-bar progress-bar-info" style="width: 10%">10%</div>
  <div class="progress-bar progress-bar-striped active" style="width: 30%"></div>
</div>
```

![[Pasted image 20230125224718.png]]

# 列表组

列表组 `list-group` 类渲染一个列表，或所有元素等级并列的一个 `<div>` 或其他元素
- 如果是一组文本，使用 `<ul>` 或 `<ol>`；如果是一组按钮（链接），使用 `<div>` 等容器为父元素

列表组内容 `item-group-item` 类可以为普通文本或 `<a>` 或 `<button>`，但不要使用 `btn` 类
- 若包含徽章 `badge`，徽章会统一放在列表组最右侧
- 支持 `disabled` 类与 `active` 类
- 支持定制内容，`item-group-item` 元素可以包含一个带有 `item-group-item-heading` 类的元素和一个带有 `item-group-item-text` 类的元素
- 支持[[情景样式.bs#列表组]]

```html
<ul class="list-group">  
  <li class="list-group-item">AAA</li>  
  <li class="list-group-item list-group-item-success">BBB</li>  
  <li class="list-group-item">CCC <span class="badge">10</span></li>  
</ul>
```

![[Pasted image 20230126001610.png]]

```html
<div class="list-group">  
  <a class="list-group-item" href="#">DDD</a>  
  <button class="list-group-item">EEE</button>  
  <a class="list-group-item disabled" href="#">FFF</a>  
</div>
```

![[Pasted image 20230126001623.png]]

# 面板

适用于将一组元素放在一个盒子里的情况。一个面板的元素包含在一个带有 `panel` 类的元素中，通常为 `<div>`。此类也可以额外带一个 [[情景样式.bs#面板]] 类描述面板样式

在包含 `panel` 类的元素内部包含以下元素，通常都是 `<div>`：
- 面板标题：带有 `panel-heading` 类的元素，根据样式高亮显示；`<hn>` 字体大小会被覆盖
- 面板内容：带有 `panel-body` 类的元素
- 面板脚注：带有 `panel-footer` 类的元素
- 表格、列表组：表格 `<table>` 元素应带有 `table` 类，列表组也应携带 `list-group` 类，不应在上面三类中

```html
<div class="panel panel-primary">
  <div class="panel-heading"><h1>Hello panel</h1></div>
  <div class="panel-body"><p>Basic panel example</p></div>
  <div class="list-group">
    <a class="list-group-item" href="#">Call: A</a>
    <a class="list-group-item" href="#">Call: B</a>
    <a class="list-group-item" href="#">Call: C</a>
    <a class="list-group-item" href="#">Call: D</a>
    <a class="list-group-item" href="#">Call: E</a>
  </div>
  <table class="table">
    <caption>备注</caption>
    <tr>
      <th>Name</th>
      <th>Age</th>
      <th>Phone</th>
    </tr>
    <tr>
      <td>A</td>
      <td>10</td>
      <td>1000000</td>
    </tr>
    <tr>
      <td>B</td>
      <td>20</td>
      <td>2000000</td>
    </tr>
    <tr>
      <td>C</td>
      <td>30</td>
      <td>3000000</td>
    </tr>
    <tr>
      <td>D</td>
      <td>40</td>
      <td>4000000</td>
    </tr>
    <tr>
      <td>E</td>
      <td>50</td>
      <td>5000000</td>
    </tr>
  </table>
  <div class="panel-footer"><p></p>Panel footer</div>
</div>
```

![[Pasted image 20230126102141.png]]

# 墙

通过 `well` 类实现类似内嵌的显示效果，可以通过 `well-lg` 和 `well-sm` 调整大小

```html
<div class="well">Look, I'm in a well!</div>
<div class="well well-lg">Look, I'm in a <strong>LARGE</strong> well!</div>
```

![[Pasted image 20230126144919.png]]

# 组件 JS API

## 事件

| 事件          | 说明                                                                                  |
| ------------- | ------------------------------------------------------------------------------------- |
| show.bs.组件  | 调用 `show` 方法或按下按钮后立即触发；通过按钮触发则可以通过 `evt.relatedTarget` 获取 |
| shown.bs.组件 | 组件完全显示出来且过渡动画完成后触发；通过按钮触发则可以通过 `evt.relatedTarget` 获取 |
| hide.bs.组件  | 组件 `hide` 方法调用后触发                                                            |
| hiden.bs.组件 | 组件完全关闭后触发                                                                    |

组件可选 `modal`，`dropdown`，`tab`，`tooltip`，`popover`，`collapse`

| 事件             | 说明                                                   |
| ---------------- | ------------------------------------------------------ |
| inserted.bs.组件 | show 方法之后，需要根据模板生成元素并插入 DOM 中时触发 |

组件可选 `tooltip`，`popover`

| 事件           | 说明                          |
| -------------- | ----------------------------- |
| close.bs.组件  | 当 `close` 方法调用后立即触发 |
| closed.bs.组件 | 当组件完全关闭后立即触发      | 

组件可选 `alert`

| 事件          | 说明                      |
| ------------- | ------------------------- |
| slide.bs.组件 | 当 `slide` 方法调用时触发 |
| slid.bs.组件  | 当完成滑动后              |

组件可选 `carousel`

## 方法

`$().show()`：显示组件
`$().hide()`：隐藏组件
`$().组件名(options)`：带有组件自定义属性弹出组件
`$().组件名()`：JS 控制弹出菜单
`$().组件名('toggle')`：JS 控制切换菜单显示/隐藏
- 大多数还存在 `组件名('show')`，`组件名('hide')`
- 大多数还存在 `组件名('destroy')`

## 自定义属性

某些需要 js 配合的组件存在自定义属性，通常自定义属性使用方法有以下两种
- 通过网页元素触发：在触发打开组件的元素上增加 `data-参数名="参数值"` 属性
- 在 JavaScript 中，使用 `组件名(options)` 方法

# 提示 弹出框

**需要引入 Bootstrap JS 支持，对应 `tooltip.js` 文件或 `popover.js` 文件**

提示 Tooltip 与 弹出框 Popover 使用基本相同，放一起了。Tooltip 在鼠标移开后直接消失，Popover 可以较长时间显示，且比较大可以包含更多内容

**使用注意**：
- 要使用 Tooltip，需要 `$("[data-toggle='tooltip']").tooltip()` 开启
- 要使用 Popover，需要 `$("[data-toggle='popover']").popover()` 开启
- 在 `btn-group`，`input-group` 或其他表格元素，需要添加 `container: "body"` 自定义属性
- `data-tigger="focus"` 的 `popover` 组件不能用 `<button>`，应使用 `<a>`

```html
<script>
  $(function () {
      // 启用 Tooltip
      $("[data-toggle='tooltip']").tooltip()
      // 启用 Popover
      $("[data-toggle='popover']").popover()
  })
</script>

<a class="btn btn-primary" href="#"
   data-toggle="tooltip" title="Some tooltip text!">Tooltip Button</a>

<a class="btn btn-info" href="#"  
   data-toggle="popover" title="Some popover text!"  
   data-content="And here's some amazing content. Right?">Popover Button</a>
```

![[Pasted image 20230127182214.png]]

自定义属性包括：

| 属性      | 类型                              | 默认值                                     | 说明                                                         |
| --------- | --------------------------------- | ------------------------------------------ | ------------------------------------------------------------ |
| animation | boolean                           | true                                       | 是否包含 CSS 渐变动画                                        |
| container | string<br>false                   | false                                      | 自定义上下文，以避免窗口大小调整造成的浮动                   |
| delay     | number<br>object                  | 0                                          | 延迟（毫秒）<br>对象形式: `{"show": number, "hide": number}` |
| placement | top<br>bottom<br>left<br>right    | tooltip: "top"<br>popover: "right"         | 弹出方向                                                     |
| template  | string                            | 略                                         | HTML 模板                                                    |
| trigger   | click<br>hover<br>focus<br>manual | tooltip: "hover focus"<br>popover: "click" | 触发方式                                                     |
| title     | string<br>function                | ""                                         | 标题，当需要自定义或元素 `title` 属性不可用时使用            |
| content   | string<br>function                | ""                                         | 内容，仅 `popover` 使用                                      |

# 菜单

**需要引入 Bootstrap JS 支持，对应 `dropdown.js` 文件**

## 容器

下拉菜单需要一个包含 `dropdown` 类或样式有 `position:relative` 的元素作为父元素，上弾菜单则需要 `dropup` 类，通常是 `<div>`；可使用 `btn-group` 代替 `dropdown`。`btn-group` 是行内块

> `dropdown` 仅仅增加了 `position:relative` 属性，`btn-group` 则额外增加 `display: inline-block` 以及其他边距、对齐等属性

## 内容

在容器标签内组成菜单的元素，包括以下两点：
- 一个带有 `dropdown-toggle` 类的按钮、连接或其他元素，作为触发器
	- 该标签需要带有 `data-toggle="dropdown"` 属性
- 一个带有 `dropdown-menu` 类的列表，可以是有序或无序列表，列表元素即下拉菜单元素，内容常为一个 `<a>` 标签
	- 带有 `divider` 类的 `<li>` 元素可以代表一个分割线
	- 带有 `dropdown-header` 类的 `<li>` 元素可以作为一个标题
	- 带有 `disabled` 类的 `<li>` 元素表示禁用的菜单项

```html
<link rel="stylesheet" href="./lib/bootstrap-3.4.1-dist/css/bootstrap.min.css">  
<script src="lib/jquery-3.6.3.min.js"></script>  
<script src="lib/bootstrap-3.4.1-dist/js/bootstrap.min.js"></script>

<div class="dropdown">  
  <button class="dropdown-toggle btn btn-default" data-toggle="dropdown">Click: Dropdown</button>  
  <ul class="dropdown-menu">  
    <li><a href="#">Action</a></li>  
    <li><a href="#">Another action</a></li>  
    <li class="divider" role="separator"></li>  
    <li><a href="#">Separator link</a></li>  
  </ul>  
</div>
```

![[Pasted image 20230124163541.png]]

# 按钮

**需要引入 Bootstrap JS 支持，对应 `button.js` 文件**

- 为 `<button>` 或带有 `btn` 类的 `<input type="checkbox">`，`<input type="radio">` 引入 `data-toggle="button"` 可显示按钮形式的开关，打开的样式为带有 `active` 类
- `$().button()`：
	- `$().button("toggle")`：切换按钮激活状态（加减 `active` 类）
	- `$().button("reset")`：将按钮文本返回原始内容，异步
	- `$().button(string)`：更改文本内容

# 按钮组

**需要引入 Bootstrap JS 支持**

按钮组即把若干按钮放在一行一组显示，需要将他们放在同一个元素中，父元素带有 `btn-group` 类或 `btn-group-vertical` 类，分别表示横向或纵向的按钮组

按钮组的各按钮将合并在一起显示，只有整组的四个顶点有圆角，且将整组转换为行内块

## 容器

带有 `btn-group` 类或 `btn-group-vertical` 类的容器元素，通常为 `<div>` 等
- `btn-group-lg/sm/xs`：按钮类尺寸
- `btn-group-justified`：使按钮尺寸相同并占满父级元素宽/高

## 内容

按钮组中的按钮
- 若按钮用于弹出菜单（`dropup`，`dropdown`），应在按钮上添加 `container: "body"` 属性以避免显示异常
- 菜单：按钮组允许包含菜单，对于菜单的容器元素，其容器元素直接使用 `btn-group` 类
	- 若为向上弹出菜单，在使用 `btn-group dropup`
- `btn-lg`，`btn-sm`，`btn-xs`：按钮大小

```html
<link rel="stylesheet" href="./lib/bootstrap-3.4.1-dist/css/bootstrap.min.css">  
<script src="lib/jquery-3.6.3.min.js"></script>  
<script src="lib/bootstrap-3.4.1-dist/js/bootstrap.min.js"></script>

<div class="btn-group">  
  <button class="btn btn-primary">Hello,</button>  
  <div class="btn-group">  
    <button class="btn btn-info dropdown-toggle" data-toggle="dropdown">[Someone]</button>  
    <ul class="dropdown-menu">  
      <li><a>AAA</a></li>  
      <li><a>BBB</a></li>  
      <li><a>CCC</a></li>  
      <li><a>DDD</a></li>  
    </ul>  
  </div>  
</div>
```

![[Pasted image 20230124170737.png]]

## 工具栏

将多个按钮组放在一个父元素中，并在父元素上添加 `btn-toolbar` 类，使之组成的更复杂的组件可以组成一个工具栏

**按钮组是行内块元素，工具栏则占一整行**

```html
<div class="btn-toolbar" ...>
  <div class="btn-group" ...>...</div>
  <div class="btn-group" ...>...</div>
  <div class="btn-group" ...>...</div>
</div>
```

# 导航

**需要引入 Bootstrap JS 支持，对应 `tab.js` 文件**

几种导航都需要引入 `nav` 类，用于列表标签上（`<ul>` 或 `<ol>`）

## 标签页

`nav-tabs` 或 `nav-pills` 类
- 带有 `nav-stacked` 类的 `<ul>/<ol>` 可创建纵向的标签页
- 带有 `active` 类的 `<li>` 标签可标记为当前页
- 带有 `disabled` 类的 `<li>` 标签可标记为禁用 
- 可以为 `<li>` 添加 `dropdown` 或 `dropup` 类创建弹出菜单

```html
<div>
  <ul class="nav nav-tabs">
    <li class="active"><a href="">Home</a></li>
    <li><a href="#">Profile</a></li>
    <li><a href="#">Message</a></li>
  </ul>
</div>
```

![[Pasted image 20230125172657.png]]

```html
<div>
  <ul class="nav nav-pills">
    <li class="active"><a href="">Home</a></li>
    <li><a href="#">Profile</a></li>
    <li><a href="#">Message</a></li>
  </ul>
</div>
```

![[Pasted image 20230125172715.png]]

```html
<div>
  <ul class="nav nav-pills nav-stacked">
    <li class="active"><a href="">Home</a></li>
    <li><a href="#">Profile</a></li>
    <li><a href="#">Message</a></li>
  </ul>
</div>
```

![[Pasted image 20230125172729.png]]

标签页下页面为带有 `tab-content` 类的 `<div>`，内含多个 `<div>`，每个包含 `tab-pane` 类并含有一个 `id` 属性，标签页部分 `<a>` 使用 `href="#页面id" data-toggle="tab"`，活动页面添加 `active` 类，也可以增加一个[[过渡动画.bs]]类

```html
<!-- 导航 -->
<ul class="nav nav-tabs">
  <li class="active"><a href="#home" data-toggle="tab">Home</a></li>
  <li><a href="#page1" data-toggle="tab">Page 1</a></li>
  <li><a href="#page2" data-toggle="tab">Page 2</a></li>
  <li><a href="#page3" data-toggle="tab">Page 3</a></li>
</ul>

<!-- 标签页 -->
<div class="tab-content">
  <div class="tab-pane active" id="home">Page: Home</div>
  <div class="tab-pane" id="page1">Page: page 1</div>
  <div class="tab-pane" id="page2">Page: page 2</div>
  <div class="tab-pane" id="page3">Page: page 3</div>
</div>
```

在 JS 中，使用 `$().tab("页面 id")` 切换

## 导航条

`navbar-nav` 类为普通导航条
- 或者，将 `navbar` 类应用于 `<nav>` 标签
- `navbar-inverse` 类可用于创建反色导航条

```html
<ul class="nav navbar-nav">
  <li class="active"><a href="#">Home</a></li>
  <li><a href="#">Link</a></li>
  <li class="dropdown">
    <a href="#" class="dropdown-toggle" data-toggle="dropdown">Dropdown <span class="caret"></span></a>
    <ul class="dropdown-menu">
      <li><a href="#">Action</a></li>
      <li><a href="#">Another action</a></li>
      <li><a href="#">Something else here</a></li>
      <li role="separator" class="divider"></li>
      <li><a href="#">Separated link</a></li>
      <li role="separator" class="divider"></li>
      <li><a href="#">One more separated link</a></li>
    </ul>
  </li>
</ul>
```

- Logo：为 `<img>` 添加一层外层元素，通常是一层 `<a>`，并对其增加 `navbar-brand` 类
- 表单：为 `<form>` 添加 `navbar-form navbar-left`，可增加左对齐且在窄视口中折叠
- 按钮：为 `<button>` 添加 `navbar-btn` 类
- 文本：为文本增加一侧耳根外层元素，通常是一层 `<p>`，并对其增加 `navbar-text` 类
	- 链接：在文本中的链接 `<a>` 标签中增加 `navbar-link` 类
- 位置：为带有 `navbar-nav` 的类增加 `navbar-fixed-top`，`navbar-static-top`，`navbar-fixed-bottom`，`navbar-static-bottom` 可将其固定/静止在顶部/底部

```html
<ul class="nav navbar-nav">  
  <li><p class="navbar-text">Some text</p></li>  
  <li><p class="navbar-text">Text with <a class="navbar-link" href="#">link</a></p></li>  
  <li><button class="navbar-btn btn btn-success">Button</button></li>  
  <li><form class="navbar-form navbar-left">  
    <label><input class="form-control"></label>  
    <button class="btn">Search</button>  
  </form></li>  
</ul>
```

![[Pasted image 20230125193442.png]]

# 警告框

**需要引入 Bootstrap JS 支持，对应 `alert.js` 文件**

使用 `alert 样式` 类创建警告框
- 若警告框带有链接，为 `<a>` 增加 `alert-link` 类
- 若需要关闭警告框，为 `alert` 类元素增加 `alert-dismissible` 类，为 `<button>` 或其他对应元素增加 `close` 类及 `data-dismiss="alert"` 属性
- 警告框支持[[情景样式.bs#警告框]]

```html
<div class="alert alert-success alert-dismissible">  
  <button class="close" data-dismiss="alert">&times;</button>  
  <strong>Success!</strong> Your operation succeed.  
</div>
```

![[Pasted image 20230125205544.png]]

# 模态框

**导航需要引入 Bootstrap JS 支持，对应 `modal.js` 文件**

模态框通过 `modal` 类创建，实现类似对话框的形式，但有几点要注意：
- 不支持同时打开多个模态框
- 模态框的 HTML 部分应尽量为 `body` 的直接子元素
- 关闭模态框的按钮只需要增加一个 `data-dismiss="modal"` 即可

模态框的元素结构为：
- `<div>.modal`
	- `<div>.modal-dialog`
		- `<div>.modal-content`
			- `div.modal-header`：通常还带有一个 <span>&times;</span> 按钮用于关闭模态框
				- `modal-title`
			- `<div>.modal-body`：主体部分，该部分支持栅格系统
			- `<div>.modal-footer`

`modal` 类标签为模态框所在总区域
- 通常设置 `tabindex=-1` 属性，使整个模态框不会被 <kbd>tab</kbd> 选中
- 通常带有一个 `id` 属性，可用于通过按钮弹出模态框
	- 对于打开按钮，应增加 `data-toggle="modal" data-target="#模态框id"` 属性
- 可带有一个模态框显示/隐藏的[[过渡动画.bs]]类

`modal-dialog` 类标签为模态框对话框，可带有一个 `modal-lg` 或 `modal-sm` 类用于设置模态框大小

```html
<!-- 模态框打开按钮 -->
<button class="btn btn-primary" data-toggle="modal" data-target="#myModal">Open modal dialog</button>

<!-- 模态框主体 -->
<div class="modal fade" tabindex="-1" id="myModal">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">Modal Title</h4>
      </div>
      <div class="modal-body"><p>One fine body&hellip;</p></div>
      <div class="modal-footer">
        <button class="btn btn-default" data-dismiss="modal">Close</button>
        <button class="btn btn-primary">Save changes</button>
      </div>
    </div>
  </div>
</div>
```

![[Pasted image 20230126162125.png]]

通过在打开模态框按钮元素中添加 `data-*` 属性可为初始化模态框提供某些信息，之后在 `show.bs.modal` 事件中处理

```html
<button class="btn btn-primary" data-toggle="modal" data-target="#myModal"
		data-mydata="这里是附加信息">Open the modal</button>
<div class="modal" id="myModal"> ... </div>
```

```javascript
// 假设模态框元素 id 为 myModal，自定义属性为 data-mydata
$(function() {
    // 在 JQuery 中注册事件
    $('#myModal').on('show.bs.modal', function(event) {
        var button = $(event.relatedTarget) // 触发打开模态框的按钮
        var msg = button.data("mydata") // 获取 data-mydata 属性值
        var modal = $(this) // 模态框
        // do something ...
    })
})
```

模态框自定义属性包括：

| 参数名   | 类型              | 默认值 | 说明                                                                                                     |
| -------- | ----------------- | ------ | -------------------------------------------------------------------------------------------------------- |
| backdrop | 'static' 或布尔值 | true   | true 框外变暗，点击框外关闭模态框<br>false 框外不变暗，点击框外不关闭<br>static 框外变暗，点击框外不关闭 |
| keyboard | 布尔              | true   | 按下 <kbd>esc</kbd> 是否关闭                                                                             |
| show     | 布尔              | true   | 初始化后是否立即显示                                                                                     |

- `modal('handleUpdate')`：重新计算模态框大小以更新滚动条状态，仅当模态框在打开时高度会发生变化时使用

# 隐藏域

**导航需要引入 Bootstrap JS 支持，对应 `collapse.js` 文件**

为一个元素增加 `collapse` 类创建隐藏域，隐藏域默认不显示，可以通过类控制其行为
- `collapse`：隐藏
- `collapsing`：过渡动画
- `collapse in` ：显示

隐藏域也可以配合其他控件使用：
- 按钮：增加 `data-toggle="collpase" data-target="#隐藏域id"` 属性

``` html
<div class="collapse" id="myCollapse">
  <div class="well">
    Something in the collapse well
  </div>
</div>

<button class="btn btn-info" 
		data-toggle="collapse" data-target="#myCollapse">Show collapse area</button>
```

![[Pasted image 20230127225928.png]]

- 面板：使用 `panel-collapse` 类包围 `panel-body` 类，且要设置 `parent` 属性

```html
<div class="panel-group col-sm-6" id="collapse-panels">
  <div class="panel panel-default">
    <div class="panel-heading">
      <h4 class="panel-title">
        <a href="#collapse1" 
           data-toggle="collapse" data-parent="#collapse-panels">Collapsible #1</a>
      </h4>
    </div>
    <div class="panel-collapse collapse in" id="collapse1">
      <div class="panel-body">...</div>
    </div>
  </div>
  <div class="panel panel-default">
    <div class="panel-heading">
      <h4 class="panel-title">
        <a href="#collapse2" 
           data-toggle="collapse" data-parent="#collapse-panels">Collapsible #2</a>
      </h4>
    </div>
    <div class="panel-collapse collapse" id="collapse2">
      <div class="panel-body">...</div>
    </div>
  </div>
  <div class="panel panel-default">
    <div class="panel-heading">
      <h4 class="panel-title">
        <a href="#collapse3"
           data-toggle="collapse" data-parent="#collapse-panels">Collapsible #2</a>
      </h4>
    </div>
    <div class="panel-collapse collapse" id="collapse3">
      <div class="panel-body">...</div>
    </div>
  </div>
</div>
```

![[Pasted image 20230127231450.png]]

自定义属性包括：

| 属性   | 类型            | 默认值 | 说明                                                                               |
| ------ | --------------- | ------ | ---------------------------------------------------------------------------------- |
| parent | false<br>选择器 | false  | 被选择器选中的元素内所有子元素中的隐藏域只会展开一个，当有新域展开时其他域都会关闭 |

# 轮播

**导航需要引入 Bootstrap JS 支持，对应 `carousel.js` 文件**

轮播类结构：
- `<div data-ride="carousel" id="轮播域id">.carousel slide`：轮播域
	- `<ul/ol>.carousel-indicators`：选择器（下面几个小点）
		- `<li data-target="#轮播域id" data-slide-to="索引">`：点，带有 `active` 类为当前项
	- `<div>.carousel-inner`：轮播图片显示区域
		- `<div>.item`：每张图片，带有 `active` 的元素为当前显示
			- `<img>`：图片
			- `<div>.carousel-caption`：可选，描述内容
	- `<a data-slide="prev">.left carousel-control`：按钮，上一张
	- `<a data-slide="next">.right carousel-control`：按钮，下一张

```html
<div id="myCarousel" class="carousel slide" data-ride="carousel">
  <ol class="carousel-indicators">
    <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
    <li data-target="#myCarousel" data-slide-to="1"></li>
    <li data-target="#myCarousel" data-slide-to="2"></li>
  </ol>
  
  <div class="carousel-inner">
    <div class="item active">
      <img src="img/dc1.jpg" alt="">
      <div class="carousel-caption">
        <h3>...</h3>
        <p>...</p>
      </div>
    </div>
    <div class="item">
      <img src="img/dc2.jpg" alt="">
      <div class="carousel-caption">
        <h3>...</h3>
        <p>...</p>
      </div>
    </div>
    <div class="item">
      <img src="img/dc3.jpg" alt="">
      <div class="carousel-caption">
        <h3>..</h3>
        <p>...</p>
      </div>
    </div>
  </div>
  
  <a href="#myCarousel" class="left carousel-control" data-slide="prev">
    <span class="glyphicon glyphicon-chevron-left"></span>
  </a>
  <a href="#myCarousel" class="right carousel-control" data-slide="next">
    <span class="glyphicon glyphicon-chevron-right"></span>
  </a>
</div>
```

![[Pasted image 20230127234546.png]]

轮播自定义属性包括：

| 属性     | 类型                 | 默认值  | 说明                                                          |
| -------- | -------------------- | ------- | ------------------------------------------------------------- |
| interval | number(int)<br>false | 5000    | 轮播自动切换间隔（毫秒）<br>false 表示不自动播放              |
| pause    | hover<br>null        | "hover" | 暂停。`"hover"` 表示在鼠标悬停时暂停自动播放，`null` 则不暂停 |
| wrap     | boolean              | true    | 播放到最后一张后是否自动循环，否则停止                        |
| keyboard | boolean              | true    | 是否响应键盘事件                                              | 

轮播 JS 方法有：
- `$().carousel(options)`：初始化自定义属性
- `$().carousel("cycle")`：自左向右开始播放
- `$().carousel("pause")`：暂停
- `$().carousel("prev")`：上一张
- `$().carousel("next")`：下一张
- `$().carousel(number)`：切换到第 n 张，自 0 开始

# 其他

- 巨幕：占满整个父元素的区域，用于显示关键内容，使用 `jumpbotron` 类创建
- 页头：为 `<h1>` 元素增加适当的空间，支持 `small` 元素，使用 `page-header` 类创建
- 媒体对象：`meida` 类可以构建媒体与文字之间的关系，子元素应使用 `media-left`，`media-right` 修饰包含媒体的元素表示相对文本的位置，`media-object` 修饰媒体元素，`media-body` 包含文本
	- 在列表元素上增加 `media-list` 类可以包含多个 `media` 类，构成媒体列表
- 过渡：`transition.js` 包含针对 `transitionEnd` 事件的辅助工具，可通过 `$.support.transition=false` 关闭
- 滚动监听：`scrollspy.js` 可用于滚动到特定位置
- 固定位置：`affix.js`