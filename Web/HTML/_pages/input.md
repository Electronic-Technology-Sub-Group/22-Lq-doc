#单标签

必选属性：
- type：输入类型

| type           | 说明                                                       |
| -------------- | ---------------------------------------------------------- |
| text           | 单行文本，默认值                                           |
| password       | 单行文本，密码（不显示明文）                               |
| url            | 单行文本，URL                                              |
| email          | 单行文本，邮箱地址（浏览器支持）                           |
| search         | 单行文本，用于搜索，换行会自动去除，可能会带有一个删除按钮 |
| tel            | 单行文本，电话号码                                         |
| number         | 单行文本，数字                                             |
| range          | slider 用于不需要精准调整大小的数字输入                    |
| radio          | 单选框                                                     |
| checkbox       | 多选框                                                     |
| file           | 文件选择                                                   |
| submit         | 提交按钮                                                   |
| image          | 显示图片的提交按钮（src，alt属性）                         |
| reset          | 重置按钮                                                   |
| button         | 普通按钮                                                   |
| color          | 取色器                                                     |
| date           | 日期（年月日）                                             |
| week           | 日期（年+周）                                              |
| time           | 时间（不包含时区）                                         |
| datetime-local | 日期与时间                                                 |
| hidden         | 隐藏域，不显示的控件，但仍可以被提交                       |

其他属性：
- 文本输入（`text`，`password` 等）
	- placeholder：占位符，输入的提示信息
	- maxlength：用户最多可输入字符的个数
	- minlength：用户最少要输入字符的个数
	- pattern：正则验证
	- spellcheck：拼写检查
	- autocomplete：自动填充，可选 `off`, `on` 及其他补全类型，详见[autocomplete](https://developer.mozilla.org/zh-CN/docs/Web/HTML/Element/input#attr-autocomplete)
	- list：接受一个[[datalist]]标签的 `id` 属性，表示一组输入提示
- 选择（`radio`，`checkbox`）
	- checked：该元素当前是否为选中状态。可以无属性值，或者用 `checked='checked'` 的写法
- 文件（`file`）
	- multiple：允许多选。该属性也可以用于邮件
	- accept：接收文件类型
		- 以 `.` 开头，以 `,` 分割的多个文件扩展名，如 `.jpg,.png`
		- 有效的 `MIME` 类型
		- 音频文件：`audio/*`
		- 视频文件：`video/*`
		- 图片文件：`image/*`
- 按钮（`submit`，`reset`，`button`）
	- onclick：点击时执行的动作
	- formmethod，formaction：使按钮提供 `submit` 功能且提交的方法和地址与 `form` 定义的不同
- 范围（`date`, `week`, `time`, `range` 等）
	- max：最大值
	- min：最小值
	- step：数值增量
- 其他
	- value：值，按钮或文本时表示显示的文字，其他表示选项提交的文本内容
	- readonly：只读，适用于除 `hidden`, `range`, `color`, `checkbox`, `radio`, `file` 外的其他属性（不以 `value` 属性作为值的元素）
	- `require`：提交时用户必须填写，`submit`, `reset`, `button`, `input[type=image]` 不可用