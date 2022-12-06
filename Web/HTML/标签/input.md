#单标签 #表单

通常在[[form]]标签中，该标签用于收集用户输入信息

```HTML
<input type="text"/>
```

必选属性
- type：输入类型
| type     | 说明                         |
| -------- | ---------------------------- |
| text     | 单行文本，默认值             |
| password | 单行文本，密码（不显示明文） |
| radio    | 单选框                       |
| checkbox | 多选框                       |
| file     | 文件选择                     |
| submit   | 提交按钮                     |
| reset    | 重置按钮                     |
| button   | 普通按钮                     | 
- name：组件名。实际可以省略，但若表单用于提交，`name` 是必须的，作为数据的键；同时，该属性也用于 `radio` 的分组

其他属性
- 通用
	- form：表单元素可以放在网页任意位置。当其位于 `form` 标签之外时，该属性用于指定所属的表单
- 文本输入（`text`，`password`）
	- placeholder：占位符，输入的提示信息
	- value：值，也用于按钮显示的文字
- 选择（`radio`，`checkbox`）
	- checked：默认选中
- 文件（`file`）
	- multiple：允许多选
- 按钮（`submit`，`reset`，`button`）
	- value：按钮显示的文字。不存在时 `submit` 和 `reset` 本身带有提交和重置文字显示
	- onclick：点击时执行的动作

效果：

<input type="text" value="asd"/>
<input type="password" value="asd"/>
<input type="radio"/> <input type="radio" checked/>
<input type="checkbox"/> <input type="checkbox" checked/>
<input type="file"/>
<input type="submit"/>
<input type="reset"/>
<input type="button" value="custom"/>
