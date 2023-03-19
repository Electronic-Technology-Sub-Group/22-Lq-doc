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

### 自定义属性

在 HTML 中可以使用带有 `data-` 前缀的属性，例如 `data-name`，可以在 JS 中该元素对象的 `dataset` 属性对象中的对应属性， 如 `data-name` 对应的就是 `dataset.name` 访问到

```HTML
<div class="box" data-id="10" />

<script>
let box = document.querySelector('.box')
// 10, 因为元素上带有属性 data-id='10'
let id = box.dataset.id
</script>
```

### 位置与大小
- `offsetLeft`，`offsetTop`：相对于定位元素的左、上位置
- `scrollTop`，`scrollLeft` 对应方向被隐藏内容，可读写
	- 整个页面：document.documentElement.scrollTop/Left（对应 `<html>` 标签）
- `clientWidth`，`clientHeight`：内容+padding
- `offsetWidth`，`offsetHeight`：内容+padding+border
- `getBoundingClientRect()`：获取一个对象，包含**相对于视口**的位置和元素本身大小

### DOM 树编辑
- 创建节点：`node = document.createElement(标签名)`，标签名如 `"div"`，`"a"` 等
- 添加节点：`elem.appendChild(node)`，`elem.insertBefore(node, nodeExisted)` 等
- 复制节点：`elem.cloneNode(copyChildren = false)`
- 删除节点：`elem.remove(node)`

## 事件

### 事件监听

DOM L2：通过 `elem.addEventListener(event, func)` 注册事件，`removeEventListener` 移除
- elem：事件源，事件触发的元素
- event：事件类型，是一个 string 字符串
- func：事件回调函数，当事件触发时调用
	- **坑：传入一个 function 对象，this=事件源；传入 lambda 表达式，this=window**
DOM L0：通过 `elem.on[event]=func` 注册，赋值为 `null` 可解绑

### 常用事件
- 鼠标：click，dbclick，mouseenter，mouseleave，mousemove
	- mouseover，mouseout：带有冒泡版本的 enter/leave
- 焦点：focus，blur
- 键盘：keydown，keyup
- 表单：input
- 滚动：scroll，用于 window 或其他带有滚动行为（滚动条）的对象
- 触屏：touchstart，touchmove，touchend
- 媒体：ontimeupdate，onloadeddata
	- ontimeupdate：播放位置（时间轴）发生变化时触发
	- onloadeddata：当前帧数据加载完成且没有播放完时的下一帧触发

- 文档：
	- load：标签内资源（图片，外联 CSS/JS 等）全部加载完成，且文档已经解析完毕，可用于 window 或 img 等标签中
	- DOMContentLoaded：仅 HTML 文档完全加载和解析完成，不保证外部资源加载完成，常用于 document 对象
	- resize：应用于 window 或其他元素，当窗口或元素大小发生变化时触发

### 事件对象
注册事件监听的回调函数时可以接受一个变量，作为事件对象
- 通用
	- `type`：事件类型。不区分大小写
- KeyboardEvent
	- `key`：按下的键，详见[Key Values - Web API 接口参考 | MDN (mozilla.org)](https://developer.mozilla.org/zh-CN/docs/Web/API/UI_Events/Keyboard_event_key_values)
	- `altKey`，`ctrlKey`，`metaKey`，`shiftKey`：`Alt`，`Ctrl`，`Win`，`Shift` 键是否按下（用于组合键）
- MouseEvent
	- `offsetX, offsetY`：鼠标相对于 DOM 元素左上角位置
	- `pageX, pageY`：鼠标相对于网页文档左上角位置
	- `clientX, clientY`：鼠标相对于浏览器窗口左上角位置

*后面 event 或 ev 代表在 js 中的事件对象*

### 事件流

> 事件完整执行过程中的流动路径

事件流分捕获和冒泡两个阶段
- 捕获：从 `document` 到具体触发元素依次触发对应事件
	- 捕获阶段可被触发的事件，在注册时 `addEventListener` 函数第三个参数应当为 `true`，默认 `false`
	- DOM L0 形式设置的事件只参与冒泡，不参与捕获
- 冒泡：当元素事件被触发时，该事件会在该元素所有祖先元素中依次触发，直到 `document` 或被阻止
	- 从触发元素到 `document`，依次触发他们的对应事件处理器

使用 `event.stopPropagation()` 方法阻止事件传播（捕获/冒泡）

使用 `event.preventDefault()` 方法阻止默认行为（链接的跳转，表单 submit 的提交等）

### 事件委托
#未完成  #JQuery

事件委托由 JQuery 提供：

使用 `$('祖先元素选择器').on('事件名', '子元素选择器', 事件函数)` 注册

使用这种方法注册的事件注册在父元素中，利用冒泡+选择器选择子元素，且其 this 指向选择的子元素，可减少注册事件个数，便于动态添加元素的事件操作

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

## 网页特效

# BOM

浏览器对象模式 BOM，最根本的对象是 window 对象，代表当前打开的浏览器

window 也是当前浏览器的顶层对象（上下文），因此调用其内容可以省略 window 对象，且声明的函数和 var 声明的变量都会存储在 window 中

BOM 一般包含以下内容：
- document：DOM 模型
- navigator：浏览器信息
- location：地址跳转相关
- history：历史记录
- screen：屏幕

## 内置函数

- 间歇函数
	- `id = setInterval(fun, delay)`：每隔 `delay` ms 调用一次 `func`，返回该定时器 id 非 0
	- `clearInterval(id)`：停止一个定时器
- 延时函数，类似 interval，但只会执行一次
	- `id = setTimeout(fun, delay)`
	- `clearTimeout(id)`

## 异步

JS 于 HTML5 开始允许通过 Web Worker 利用多线程，提高了异步任务的能力
- 同步任务：执行栈
- 异步任务：任务队列 + 回调，包括普通事件，资源加载，定时器等，由 js 的宿主环境执行（浏览器，nodejs 等），并在执行完毕后将回调函数插入任务栈

事件循环：每次执行时，先执行同步任务，并将异步任务入队，循环查询任务栈内容

![[Pasted image 20230318154014.png]]

## location

- `href`：保存了当前网站的 URL
	- 跳转页面：修改 href 属性即可，**该方法跳转后不可后退**
- `search`：返回表单查询或提交部分（字符串，`?` 后的部分，包括 ?）
- `hash`：返回 `#` 后的部分，常用于路由，在不跳转页面的情况下显示不容页面，包括 `#`
- `reload([bool])`：刷新，传入 true 时强制刷新，重新下载所有资源，类似 `[CTRL+]F5`

## navigation

包含浏览器和系统平台的相关信息
- `userAgent`：UA，常用于判断平台
![[Pasted image 20230318160313.png]]

## history

历史记录管理
- forward()：前进
- back()：后退
- go(n)：前进几步，n 为负数时表示后退

## 本地存储

HTML 5 新增的，浏览器为每一个域名提供了大约 5MB 的 `sessionStorage` 和 `localStorage` 的存储空间，可保证在页面刷新等场景下仍保留数据，常用于 Web 应用

`localStorage` 可在多窗口页面中共享，且以键值对形式存储，数据会持久化保存在客户端中，除非手动清除
- `localStorage.setItem(key, value)`
- `localStorage.getItem(key)`
- `localStorage.remove(key)`
- `localStorage.clear()`

`sessionStorage` 类似 `localStorage`，但生命周期与浏览器窗口相同，即浏览器窗口关闭后数据将销毁

注意，存储时候**只能存储字符串或其他基本类型，无法存储对象**，可通过 `JSON.stringify(obj)` 转换成 json 字符串，读取时通过 `JSON.parse(str)` 转换回对象


# 性能优化

- 防抖 `debounce`：单位时间内，对于频繁触发的事件，只执行最后一次触发效果
- 节流 `throttle`：单位时间内，对于频繁触发的事件，两次触发的时间有一定间隔