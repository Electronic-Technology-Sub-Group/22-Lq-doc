在文档中添加一系列输入、按钮等交互控件，使用 `meta-bind` 代码块创建，大部分控件也支持 \`\` 在行内添加，如 `button`、`inlineSelect` 等

````
```meta-bind
INPUT[<inputType>(<arguments>):<meta>]
```
````

- `<inputType>`：输入类型，包括各种选择器、列表等，类似 HTML 的 `<input>`
- `<arguments>`：（可选）输入控件可能需要的参数，如 `inlineSelect` 有 `option` 属性
- `<meta>`：（可选）绑定到一个 `Metadata` 属性
# Input

输入控件  [[../../../_resources/codes/Meta Bind/Input|Input]]，相见 [Meta Bind Docs](https://www.moritzjung.dev/obsidian-meta-bind-plugin-docs/reference/inputfields/date/)

![[../../../_resources/images/Pasted image 20240911164556.png]]

# 模板

![[../../../_resources/images/Pasted image 20240911164849.png]]

在设置中可以添加输入模板

````
```meta-bind
INPUT[<模板名>][<覆盖属性>]
```
````

覆盖属性可以覆盖属性或 Metadata 绑定

# 视图

使用 `VIEW` 可以在文档中查看 `Metadata` 字段属性，支持行内：[[../../../_resources/codes/Meta Bind/View|View]]

# 按钮

![[../../../_resources/images/Pasted image 20240911172758.png]]

使用 `meta-bind-button` 创建按钮，也可以使用 `Button Builder`。

````
```meta-bind-button
style: <style>
label: <label>
actions:
  - type: <type>
    command: <command>
```
````

- `style`：样式，如 `primary`，`destructive` 等
- `label`：按钮上显示的文本
- `id`：可以设置一个 `id`，用于行内按钮
- `hidden`：是否隐藏
- `actions`：行为列表，由 `type` 和 `command` 组成；若只有一个则可用 `action`

## 行内按钮

在文档中插入一个按钮，并在行内使用 ` BUTTON[<button-id>]` 引用

`[]` 中可以引用多个按钮 `id`，形成一个按钮组

# 嵌入

无缝嵌入，与 `![[]]` 相比可以将另一个文档的任何字段也都绑定到当前文档

````
```meta-bind-embed
[[<another note>]]
```
````

# 字段

相关语法相见 [Bind Target](https://www.moritzjung.dev/obsidian-meta-bind-plugin-docs/guides/bindtargets/)，即 `VIEW` 的属性或 `INPUT` 中 `:` 后的部分

````
```meta-bind
VIEW[<storageType>^<storagePath>#<property>]
```
````

- `<storageType>`：字段位置
	- `frontmatter` 默认，`Metadata` 中的数据
	- `memory`：读写文档相关的内存字段，重启 Obsidian 后数据清空
	- `globalMemory`：读写全局内存字段，重启 Obsidian 后数据清空
	- `scope`：使用另一个文件的内存字段，禁用 `<storagePath>`
- `<storagePath>`：文件路径，默认当前文件
- `<property>`：属性名
	- 包含空格时使用 `bracket` 模式： `["property name"]`
	- 支持嵌套，可使用 `.` 或 `bracket` 模式：`this.is.nested`，`this["is"].nested`

# JS 动态绑定

https://www.moritzjung.dev/obsidian-meta-bind-plugin-docs/guides/advancedusecases/

# API

用于 JS 或 Dataview

https://www.moritzjung.dev/obsidian-meta-bind-plugin-docs/guides/api/

# 样式

使用 CSS 片段调整样式

https://www.moritzjung.dev/obsidian-meta-bind-plugin-docs/guides/stylingandcss/