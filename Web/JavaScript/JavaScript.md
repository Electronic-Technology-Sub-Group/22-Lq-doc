# JavaScript

负责网页的行为，即网页与服务器，网页与用户的交互

JS 分为两部分，ECMAScript 定义了 JS 的基础核心语法，WebAPIs 则包含了 DOM 和 BOM

- [[JS 语法]]
- [[JS 内置类与方法]]
- [[JS WebAPI]]
- [[JS ES6 标准]]，[[JS ES8 标准]]

# 引入 JS

自上而下优先级递减；可以放在网页中任意位置，一般放在 `<body>` 底部，以保证网页元素加载完成
- 内联 JS：在每个标签的事件属性引号中直接写的 JS 代码
- 内部 JS：以 `<script>` 标签包围，标签内直接写 JS 代码
- 外部 JS：通过 `<script>` 标签引入：`<script src="JS 文件地址"></script>`
	- 注意：`<script></script>` 标签中间不要有任何代码，否则 `src` 属性会被忽略

# 专题

## 对象拷贝

- 浅拷贝
	- 数组：`Array.concat([], arr)`，`[...arr]`
	- 对象：`Object.assign({}, obj)`，`{...obj}`
- 深拷贝：递归/三方库lodash或cloneDeep/JSON序列化后反序列化

## this

1. 对于一般函数，this 指向调用函数的对象，省略一般是 window（浏览器中）
	- 事件回调中，this 指向触发事件的元素
2. 对于箭头函数，其本身没有 this，this 为作用域链中的 this，为其最近作用域中的 this
3. 原型函数和构造中指向调用的对象名
4. 使用 `call`，`apply`，`bind` 可以修改 this 指向

![[JS 内置类与方法#Function]]

## 严格模式

添加一句 `'user strict'`

## EventLoop

JavaScript 是一门单线程语言，但不代表 JavaScript 不能实现异步执行。

JavaScript 的异步任务是委托给宿主环境执行的，其环境通常是浏览器的 V8 引擎或 NodeJS，并在执行结束后通知 JavaScript 调用回调函数。这种轮询调度循环称为事件循环 `EventLoop`
1. JavaScript 取任务于任务栈
2. JavaScript 按顺序调用栈中的同步任务，将异步任务委托给宿主环境，并将回调放入任务队列
3. 当栈中同步任务清空后，取出回调函数，判断任务是否完成，完成则执行

![[Pasted image 20230430104652.png]]

### 宏任务与微任务

JavaScript 将异步任务进一步划分为宏任务和微任务，**宏任务和微任务都是异步任务**

宏任务 macrotask
- Ajax 请求
- setTimeout，setInterval
- 文件读写
- 其他

微任务 microtask
- Promise.then, catch, finally
- process.nextTick
- 其他

每当一个宏任务执行完成时，JavaScript 都会检查是否有微任务，如果存在微任务则先执行所有微任务再执行下一个宏任务

![[Pasted image 20230430105309.png]]

