# 书写顺序

一个书写习惯，按一下顺序写会提高渲染速度（???）
- 浮动 / display
- 盒子模型，margin，border，padding，宽高，背景色
- 文字样式

# 项目目录

通常，前端项目可以分 pc 和 移动端 两个目录。每个目录下可分
- css：存放独立的 css 样式
	- base.css：应用于全局的公共样式
	- common.css：应用于全局，适用于多个网页相同模块的样式，如 Widget，相同的网页头部、底部等，通常需要标签具有特定 `class` 引入
	- 网页文件.css
- images：存放网站固定图片，如 logo，样式图等
- updates：存放网站中随时会更新的图片，如首页 banner，商品图，宣传图，用户上传图片（头像等）等
- lib：存放第三方内容
- favicon.ico：网页图标

# 全局样式

## 全局

### 内减模式

```CSS
* {
    box-sizing: border-box;
}
```

### 清除默认边距

```CSS
* {
    padding: 0;
    margin: 0;
}
```

### 隐藏溢出内容

```CSS
* {
    overflow: hidden;
}

```

## input

### 清除外部边框

```CSS
input {
    border: none;
    outline: none;
}
```

## a

### 清除下划线

```CSS
a {
    text-decoration: none;
}
```

## img

### 清除 img 默认下间隙

```CSS
img {
    vertical-align: middle;
}
```

### 宽度与父元素相同

```CSS
img {
    width: 100%;
}
```

### Logo 搜索引擎优化

```HTML
<div class="logo">
    <h1><a href="...">{{Title}}</a></h1>
</div>
```

```CSS
.logo {
    width: {{img_width}};
    height： {{img_height}};
}

.logo h1 a {
    display: block;
    width: {{img_width}};
    height： {{img_height}};
    background-image: url({{img_path}});
    background-size: contain;
    font-size: 0;
}
```

## 列表

### 清除默认样式

通常用列表的情况下都会有自己的图标，或作为导航，或仅作为并排元素使用

```CSS
ul, ol {
    list-style: none;
}
```

## 表格

### 合并边距

```CSS
table * {
    border-collapse: collapse;
}
```

# 公共样式

## 版心居中

```CSS
.wrapper {
    margin: 0 auto;
}
```
- 上下 margin=0（这个随意，按设计来）
- 左右 `auto`：浏览器自动设置两边相同

> 版心：网页有效内容

## 绝对定位居中

```CSS
.absolute_box {
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
}
```

## 清除浮动（双伪元素）

```CSS
.clearfix::before, .clearfix::after {
    content: '';
    display: table;
}
.clearfix::after {
    clear: both;
}
```

## 左/右浮动

```CSS
.fl {
    float: left;
}

.fr {
    float: right;
}
```

### 圆形

```CSS
/* 要求：盒子长宽相同 */
.circle {
    border-radius: 50%;
}
```

### 其他特殊图形

- 胶囊按钮：对一个长方形盒子设置 `border-radius: npx`。`npx` 为长度/宽度一半
- 三角形：对宽度和高度都为 0 的矩形，只使用四个方向的边框绘制
