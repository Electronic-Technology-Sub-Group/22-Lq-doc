
# 生命周期

![[Pasted image 20230515011048.png]]

1. 网页通过 `Vue.createApp` 创建 App 实例
	- 生命周期：`beforeCreate`，此时还无法访问任何数据（`undefined`）
2. 初始化数据和方法
	- 生命周期：`created`，可以访问数据，但无法访问任何模板和 DOM相关内容（`this.$el=null`）
3. 通过 `mount()` 方法与 DOM 绑定，编译模板
	- 生命周期：`beforeMount`
5. 将 DOM 中的模板和属性替换成 Vue 方法，`mount` 完成
	- 生命周期：`mounted`
6. 发生数据变动或事件触发时，更新数据
	- 生命周期：`beforeUpdate`，数据已更新但未同步到 DOM 中
7. 更新网页模板
	- 生命周期：`updated`，已更新，多用于调试
8. 销毁 App 实例：调用 `unmount()` 方法
	- 生命周期：`beforeUnmount`，此时应用程序仍在运行，对应元素清空（`this.$el=""`）
9. 完成 App 实例的销毁
	- 生命周期：`unmounted`，此时实例已经销毁，所有数据无法访问

## Hook

生命周期 Hook 通过将对应生命周期事件添加到初始化对象中即可触发

```javascript
Vue.createApp({
	data() {},
	// ...
	beforeCreate() { /* ... */ },
	created() { /* ... */ },
	beforeMount() { /* ... */ },
	beforeUpdate() { /* ... */ },
	updated() { /* ... */ },
	beforeUnmount() { /* ... */ },
	unmounted() { /* ... */ }
})
```

# 虚拟 DOM

> To take in something and convert it into something else

虚拟 DOM 是一个轻量级 Javascript 对象，存储控制的 DOM 的轻量级副本。

浏览器在更新网页时有如下特点：
- 只能通过 `node.innerHTML` 修改某元素内部数据
- DOM 在解析和操作 `innerHTML` 属性时效率很高
- 在决定哪些数据发生变化，遍历和查找浏览器 DOM 树，产生更多开销

在编译中，Vue 会维护一个虚拟的 DOM 树模型，以便在更新时快速查找变动分支和生成新的代码

![[Pasted image 20230515131731.png]]

Vue 的 DOM 操作并不是直接操作浏览器 DOM 树。Vue 先将数据的变化通知虚拟 DOM，由虚拟 DOM 生成用于更新 DOM 的”补丁“，根据这些补丁可以实现只更新属性、内容或变化的元素，以最小的代价快速更新浏览器 DOM。

# 响应式与代理

## 代理

Javascript 本身存在代理类，可用于取代直接的对对象进行操作，而是将其转移到另一个对象中

```javascript
// 原对象
const data = {
    name: "Luis"
}

// 代理对象
const observed = new Proxy(data, {
    // 代理 set 方法
    set(target, key, value) {
        // target: 被修改对象，即实际的 data。此时数据还未变化
        // key：被修改变量
        // value：新值

        // 代理后，赋值完全由代理实现，因此要手动设置值
        target[key] = value
        // 代理方法中就可以对属性变更进行一个操作了，比如更新 HTML
        document.querySelector("#name").innerHTML = value
    }
})

// 使用代理对象对原对象进行修改，即可触发 set 方法
observed.name = 'Join'
```

Vue 通过代理对象实现对数据的监听和绑定，可实现对修改，读取，删除等各种操作的代理

# 模板与编译


## 模板属性

可以在初始化对象中使用 `template` 属性而不是直接在 HTML 中声明模板。这与在 HTML 中写模板效果是一样的

```javascript
let vm = Vue.createApp({
    data() {
        return {
            message: 'hello world'
        }
    },
    // 这里写模板
    template: `<p>{{ message }}</p>`
}).mount('#app')
```

```html
<div id='app'></div>
```

但由于可读性等原因，不建议这么写。某些时候，如模板字符串需要从其他地方获取（其他地址/配置文件或动态拼接等），可以使用这种方式

## 编译

编译：将模板字符串转化成 JavaScript 渲染函数：`Vue.compile(template[, options])`

在创建 Vue App 时，可以自定义编译程序，只需在初始化对象中声明 `render()` 函数即可。该函数中可以通过 `Vue.h()` 调用 Vue 默认的编译程序。

```javascript
let vm2 = Vue.createApp({
    data() {
        return {
            message: 'hello world'
        }
    },
    render() {
	    // 输出一个 h1 标签，内容为 message 变量
        return Vue.h('h1', this.message)
    },
}).mount('#app2')
```

## 不带编译器的 Vue

Vue 支持使用无编译器版本，其无法完成编译操作，但有如下优点：
- 库体积减少 30%
- 更轻量，更快
但模板需要提前编译，编译出的模板可读性会变差

要使用不带编译器的版本，引入 `runtime` 版的 js 文件即可。

# 组件

组件是网页的组成部分，是单独封装好的 Vue 模板

**组件必须在主程序 `mount` 之前声明创建**

使用 `app.component(name, config)` 可创建一个组件
- name：组件名，通常使用小写连字符或大写驼峰式命名
- config：配置对象，是一个对象，和 App 初始化对象类似
之后，在主 App 中的模板中创建与 name 相同的标签即可。每个组件之间数据相互独立。

```javascript
let vm  = Vue.createApp({

})

// 组件 hello
vm.component('hello', {
    data() {
        return {
            message: 'hello world'
        }
    },
    template: '<h1>{{ message }}</h1>'
})

vm.mount('#app')
```

```html
<div id="app">
  // 使用 hello 组件：插入对应标签
  <hello></hello>
  <hello></hello>
  <hello></hello>
</div>
```

# 底层工具

## Vite

Vue 使用的应用程序打包工具
- 尽可能减少文件个数和体积
- 效率高，第三方库支持广泛，对各种格式开箱即用，配置方便

在一个空目录中使用 `vite` 即可创建一个新包

```bash
npm create vite@latest
```

之后经过项目名，框架选择，语言选择（JS/TS），即可在当前目录下创建一个以项目名命名的项目文件夹。进入后运行 `npm install` 下载 npm 依赖即可使用

- 默认入口 `index.html`
	- 只有直接或间接导入到入口的资源才会被打包
- CSS 等资源可通过 JS 文件 `import [name] file` 语句导入，Vite 会进行拆分
- `npm run dev` 命令用于打开调试服务器，会自动监听本地文件变化并重新编译
- `npm run preview` 命令用于打预览服务器，不会自动编译和刷新
- `npm run build` 命令用于编译出可以发布的网页，生成的文件位于 `dist` 目录下

## SASS

扩展了 CSS
- `darken()`，`lighten()` 等调整颜色的函数
- 嵌套

需要通过 `npm i sass` 将 SASS 添加到项目依赖中才可以使用。

## PostCSS

将 CSS 编译成一个 JavaScript 对象，并在运行时允许通过 JS 与 CSS 交互

![[Pasted image 20230515174108.png]]

Vite 已内置该工具，只需要在项目目录中（`package.json` 同级） 创建 `postcss.config.cjs` 即可使用。

相关插件配置可在 [PostCSS.parts | A searchable catalog of PostCSS plugins](https://www.postcss.parts/) 中查询

- 可使用 `npm i autoprefixer --save-dev` 安装插件用于自动扫描 CSS 文件，并查找缺少前缀

```javascript
module.exports = {
    plugins: [require('autoprefixer')]
}
```

![[Pasted image 20230515175206.png]]

![[Pasted image 20230515175154.png]]

## ESLint

用于定义一组更严格的代码标准，并在编写和编译时进行校验和提示，以提高代码质量

需要通过 `npm i eslint --save-dev` 安装，并通过 `npm i vite-plugin-eslint --save-dev --force` 集成到 Vite

在项目目录下创建 `vite.config.js` 并增加以下配置：

```javascript
// 加载 vite 配置环境
import { defineConfig } from "vite";

import eslint from 'vite-plugin-eslint'

export default defineConfig({
    plugins: [eslint()]
})
```

在项目目录下创建 `.eslintrc` 文件作为 ESLint 的配置文件，当然也可以在 `package.json` 中添加，但推荐单独文件

```json
{
  "rules": {
    // 字符串强制双引号
    "quotes": "error"
  },
  "env": {
    // 设定运行环境为在浏览器中运行
    "browser": true
  },
  "parserOptions": {
    // ES 版本为 2022，支持 import 语句
    "ecmaVersion": 2022,
    // 声明使用 ES6 module 模式
    "sourceType": "module"
  }
}
```

我们甚至可以让 ESLint 通过命令主动修复这些不规范，只需要在 `package.json` 中配置下，即可使用 `npm run lint` 修复

```json
{
  "scripts": {
    "lint": "eslint main.js --fix"
  }
}
```

*ESLint 已被各种 IDE 兼容*

## Webpack

webpack 是另一个打包工具（与 Vite 为同类），也是使用广泛的打包工具，更灵活，更麻烦
- 配置文件：`webpack.config.js`
- 默认只支持 JavaScript，其他各种类型需要插件支持
详见 [[Webpack]]



