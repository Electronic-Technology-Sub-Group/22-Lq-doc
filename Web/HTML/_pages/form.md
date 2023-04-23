必选属性：
- action：提交地址

可选属性：
- target：提交地址打开方式
	- `_self`：默认，当前网页
	- `_blank`：新窗口
- method：提交方法，可选 `'get'` 或 `'post'`，默认为 `get`
- enctype：对提交的数据进行编码方式
	- `application/x-www-form-urlencoded`：默认，对所有数据进行 URL 编码
	- `multipart/form-data`：不编码，当用于文件传输时必选
	- `text/plain`：仅将空格替换成 `+`
