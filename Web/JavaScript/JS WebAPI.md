# DOM

## DOM 对象

### 分类
DOM 中每个节点都对应一个对象，包括：
- **元素节点**对应每个标签，DOM 树主要以元素节点组成
- **属性节点**对应标签的每个属性
- **文本节点**对应标签内容中的文本
- 注释，事件等...

*后面 elem 代表在 js 中获取的 DOM 元素节点对象*

### 获取
获取的是 HTML 文档中的元素节点
- `document.querySelector('选择器')`：通过 CSS 选择器查询对象，返回 `null` 或查询到的第一个元素对象
- `document.querySelectorAll('选择器')`：获取匹配的所有元素，返回 `NodeList` 伪数组
- `elem.parentNode`，`elem.children`，`elem.[next/previous]ElementSibling` 属性
- `getElement(s)(By...)`

### 内容
- `innerText` 属性：将文本作为标签内容（`<></>` 之间的内容），但作为文本插入，不会解析为新的 DOM 元素
- `innerHTML` 属性：将文本作为标签内容（`<></>` 之间的内容），可以解析为新的 DOM 元素

### HTML 属性
直接通过 `elem.key=value` 修改即可，属性通常与 HTML 属性相同

### CSS 样式
- 直接修改 `elem.style` 对象属性修改样式，会被解析成元素本身的 `style` 属性
	- 连字符转化为小写驼峰命名：`font-size` -> `fontSize`
- 修改 `elem.className` 覆盖对象 `class` 属性
- 通过 `elem.classList` 修改对象 `class` 属性：`add`，`remove`，`toggle` 方法

### 表单属性
- type
- value，checked，selected 等

### DOM 树编辑
- 创建节点：`node = document.createElement(标签名)`，标签名如 `"div"`，`"a"` 等
- 添加节点：`elem.appendChild(node)`，`elem.insertBefore(node, nodeExisted)` 等
- 复制节点：`elem.cloneNode(copyChildren = false)`
- 删除节点：`elem.remove(node)`

## 事件

### 事件监听

DOM L2：通过 `elem.addEventListener(event, func)` 注册事件
- elem：事件源，事件触发的元素
- event：事件类型，是一个 string 字符串
- func：事件回调函数，当事件触发时调用
DOM L0：通过 `elem.on[event]=func` 注册

### 事件对象
注册事件监听的回调函数时可以接受一个变量，作为事件对象
- 通用
	- `type`：事件类型。不区分大小写
- KeyboardEvent
	- `key`：按下的键，详见[Key Values - Web API 接口参考 | MDN (mozilla.org)](https://developer.mozilla.org/zh-CN/docs/Web/API/UI_Events/Keyboard_event_key_values)
	- `altKey`，`ctrlKey`，`metaKey`，`shiftKey`：`Alt`，`Ctrl`，`Win`，`Shift` 键是否按下（用于组合键）
- MouseEvent
	- `offsetX, offsetY`：鼠标相对于 DOM 元素左上角位置
	- `clientX, clientY`：鼠标相对于浏览器窗口左上角位置

*后面 event 或 ev 代表在 js 中的事件对象*

### 常用事件
- 鼠标：click，mouseenter，mouseleave，mouseover，mousemove
- 焦点：focus，blur
- 键盘：keydown，keyup
- 表单：input

## 渲染

![[Pasted image 20230129220717.png]]

1. 解析 HTML 与 CSS，分别生成 DOM 树与样式规则
2. 结合 DOM 树与样式规则，生成渲染树
3. 进行布局 Layout，描述节点 几何信息
4. 绘制与展示
5. 回流/重排：当渲染树发生变化，如 DOM 树发生变化或部分元素尺寸、位置、布局等发生变化时，重新渲染部分或全部文档
	- 页面刷新，浏览器窗口变化
	- 元素大小或位置变化，字体大小变化，CSS 伪类激活
	- input 内容或图片大小变化造成的元素大小变化，添加或删除可见 DOM 元素
6. 重绘：节点样式改变但不影响在文档布局中的位置时的重新渲染称为重绘，如 `color`，`outline`，`background-color` 等 CSS 属性变化
*回流一定会引起重绘，重绘不一定需要回流*

# BOM