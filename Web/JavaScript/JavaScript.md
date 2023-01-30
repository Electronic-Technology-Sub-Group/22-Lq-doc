# JavaScript

负责网页的行为，即网页与服务器，网页与用户的交互

JS 分为两部分，ECMAScript 定义了 JS 的基础核心语法，WebAPIs 则包含了 DOM 和 BOM

- [[JS 基本语法]]
- [[JS WebAPI]]
- [[JS 类与对象]]
- [[JS ES6]]

# 引入 JS

自上而下优先级递减；可以放在网页中任意位置，一般放在 `<body>` 底部，以保证网页元素加载完成
- 内联 JS：在每个标签的事件属性引号中直接写的 JS 代码
- 内部 JS：以 `<script>` 标签包围，标签内直接写 JS 代码
- 外部 JS：通过 `<script>` 标签引入：`<script src="JS 文件地址"></script>`
	- 注意：`<script></script>` 标签中间不要有任何代码，否则 `src` 属性会被忽略