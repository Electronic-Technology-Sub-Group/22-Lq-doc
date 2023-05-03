>栅格系统：在 Bootstrap 3 中，默认将网页完整的一行等分成 12 份，每个内容占用整数份，根据屏幕尺寸选择显示的份数

# 栅格

![[Pasted image 20230123161835.png]]

根据屏幕宽度不同，将父元素平均分成 12 列，可以为版心内每一行元素元素设置不同的块数，为不同的显示宽度选择不同的列数：`col-宽度别名-列数`
- [[宽度别名.bs|宽度别名]]：相应屏幕宽度对应的别名，不同屏幕都被分割为 12 列，列间隙默认都是 30px
- 列数：整数，范围为 $1\leq n\leq12$

Bootstrap 允许一行的列数超过 12。当一行的总列数超过 12 时，超过的元素将另起一行排列

```html
<div class="container" style="color: white">  
  <!-- 从第 3 个 div 开始这一行已经超过 12 个格了 -->  
  <div class="col-lg-5" style="background-color: red">01</div>  
  <div class="col-lg-5" style="background-color: green">02</div>  
  <div class="col-lg-5" style="background-color: blue">03</div>  
  <div class="col-lg-5" style="background-color: yellow">04</div>  
</div>
```

![[Pasted image 20230123211610.png]]

## 列偏移

使用 `col-offset` 可以将列向右移 n 个格，附加在包含 `col` 的类上：`col-宽度别名-offset-n`
- 宽度别名：[[宽度别名.bs]]
- n：向右偏移格数

*使用 `margin-left` 实现的元素向右偏移*

## 列移动

以下标签需要添加在带有 `row` 类的元素上：
- 使元素向右移动 n 格：`col-宽度别名-push-n`，通过 `right` 实现
- 使元素向左移动 n 格：`col-宽度别名-pull-n`，通过 `left` 实现
	- 宽度别名：[[宽度别名.bs]]
	- n：格数

## 取消偏移

在附加有 `row` 类的元素上附加 `row-no-gutters` 类用以取消行内列之间的间隔，实质是

添加在带有 `row` 类的元素上，取消行左右侧的偏移，实质是设定元素 `margin-left` 和 `margin-right` 为 0

# 版心

Bootstrap 中使用 `container` 类作为版心，具有版心居中、左右内边距 15px 的特性
- container：在不同宽度的设备（或浏览器尺寸）上有不同的宽度

| 屏幕宽度 | 容器宽度 |
| -------- | -------- |
| <768px   | 100%     |
| >=768px  | 750px    |
| >=992px  | 970px    |
| >=1200px | 1170px   |

- container-fluid：宽度固定为 100% ，不常用

`row` 类表示版心中没有左右缩进的一行，带有该类的元素将自带左右 -15px 的 `margin`，用于抵消 `container` 类左右 15px 的内边距

```html
<body>  
<div class="container" style="background-color: gray">  
  <div class="col-lg-3" style="background-color: red">01</div>  
  <div class="col-lg-3" style="background-color: green">02</div>  
  <div class="col-lg-3" style="background-color: blue">03</div>  
  <div class="col-lg-3" style="background-color: yellow">04</div>  
</div>  
<div class="container" style="background-color: gray">  
  <div class="row">  
    <div class="col-lg-3" style="background-color: red">01</div>  
    <div class="col-lg-3" style="background-color: green">02</div>  
    <div class="col-lg-3" style="background-color: blue">03</div>  
    <div class="col-lg-3" style="background-color: yellow">04</div>  
  </div>  
</div>  
</body>
```

![[Pasted image 20230123170329.png]]

**一般来说，`container` 的直接子元素都应是 `row` 类，`row` 类的所有直接子元素都应是 `col` 类**

