边框图片，需要指定四条线将图片切成 9 部分，其中四个角直接显示，四条边可设置平铺，拉伸或铺满

![[Pasted image 20230417072051.png]]

- border-image-source：边框图片路径
- border-image-slice：裁剪尺寸，按上右下左顺序的四条线
- border-image-width：边框图片宽度
- border-image-repeat：填充样式，可选 repeat|round|stretch，默认 stretch 拉伸
	- stretch：拉伸，缩放到所需尺寸
	- repeat：平铺，按原始大小复制多份并居中
	- round：铺满，按原始大小复制多份，左对齐