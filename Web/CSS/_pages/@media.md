媒体查询

```CSS
@media 媒体类型 (媒体特性=...) {
    选择器 { CSS 属性 } ...
}
```

- 媒体类型：描述设备的一般类型，可用 `not`，`and`，`only` 等修饰符
	- all：默认，适用于所有设备
	- print：适用于打印预览模式，且用于打印
	- screen：用于显示屏幕（手机，电脑...）
	- 逻辑操作符：and，not，only，`,`，or
- 媒体特性：用户代理，输出设备或环境的具体特征，可选
	- any-hover：设备允许用户将指针悬停在任意位置
		- hover：设备是否允许用户将指针悬停
	- any-pointer：设备有可以在任何位置的指针
		- pointer：设备是否有指针
	- aspect-ratio：设备宽高比
	- color：设备输出像素比特值；若不支持彩色，则该值为 0
	- color-gamut：设备支持的大致色域
	- color-index：输出设备的颜色查询表条目；若设备不使用颜色查询表，该值为 0
	- display-mode：应用的显示模式
	- dynamic-range：设备输出支持的亮度、对比度、颜色深度组合
	- forced-colors：用户代理是否限制使用调色板
	- grid：设备使用的是网格屏还是点阵屏
	- width/height：设备宽、高
	- orientation：视口旋转方向
	- resolution：设备分辨率
	- scripting：设备是否允许使用 JavaScript 等脚本