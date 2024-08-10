# Vue Router

Vue Router 是 Vue 官方路由管理器，与 Vue.js 核心深度集成，有利于单页面应用管理。

Vue Router 是一个库，可以通过导入一般 JS 库的形式（单独文件，`CDN`）等导入，也可以使用 `Vue CLI` 工具导入。

使用 Vue Router 动态加载组件时需要将组件映射到路由中，并在特定的地方显示即可。

# 跳转

* 使用 `<router-link>` 标签跳转，功能上类似 `<a>` 标签

  * `to`：目标地址
  * `tag`：指定渲染成的标签，默认为 `<a>`
  * `replace`：添加该属性后，不会留下历史记录，无法后退

  ```html
  <router-link to="/" tag="li">Home</router-link>
  <router-link to="/about">About</router-link>
  ```
* 使用 `$router.push('路径')` 等编程式导航 API 跳转

# 查询参数

Vue Router 支持两种查询参数传递方法：

* `query` 传参：如 `to=/?id=888&pwd=999`

  使用 `$route.query.<param-name>` 获取参数值
* `params` 传参：如 `/888/999`，将符合某种匹配模式的所有路由路径映射到同一个组件，使用动态路径参数 `:参数名` 实现。

  注册用于动态匹配的路由规则

  ```js
  {
    path: '/:id/:pwd',
    name: '...',
    component: ...
  }
  ```

  ```js
  const routes = [{
    path: 'user/:uname/:pwd',
    name: 'user',              // 路由名称
    component: UserView        // 路由对应组件
  }]
  ```

  使用 `$route.params.<param-name>` 获取参数值，如 `$route.params.uname`

> `$route` 对象：记录路由相关信息
>
> |属性|类型|说明|
> | ----| ----| ------------------------------------|
> |`path`|`string`|当前路由路径|
> |`params`|`object\|{}`|一个 `key:value` 对象，包含所有((20240529140108-lghljgd "动态参数"))|
> |`query`|`object\|{}`|一个 `key:value` 对象，包含所有 ((20240529135814-wt9eub5 "URL 查询参数"))|
> |`hash`|`string\|""`|当前路由哈希值（不带 `#`）|
> |`fullPath`|`string`|解析后的完整 URL，包含查询参数和哈希|
> |`matched`|`object[]`|当前匹配路径中包含的所有片段对应配置|
> |`name`|`string`|当前路径名称|
> |`meta`|`object`|路由元信息|

# 路由

路由配置通常在 `src/router` 目录下的 `js` 文件中进行，并在 `main.js`  导入。

路由记录：`routes` 中记录的路由信息

1. 创建用于显示的组件与页面

    * `src/components/CommonView.vue` 用于显示信息

      ```html
      <template>
          <div>
              <img src="../assets/logo.png" alt="Vue logo">
              <h1>{{ msg }}</h1>
          </div>
      </template>
      <script>
      export default {
          name: 'CommonView',
          props: {
              msg: String
          }
      }
      </script>
      ```
    * `src/views/FirstView.vue` 测试使用 `query` 形式传参

      ```html
      <template>
          <div>
              <CommonView msg="First View" />
              <br>
              <h4>uname: {{ $route.query.uname }}</h4>
              <h4>pwd: {{ $route.query.pwd }}</h4>
          </div>
      </template>
      <script>
      import CommonView from '@/components/CommonView.vue'
      export default {
          name: 'FirstView',
          components: { CommonView }
      }
      </script>
      ```
    * `src/views/SecondView.vue` 测试使用 `params` 形式传参

      ```html
      <template>
          <div>
              <CommonView msg="Second View" />
              <br>
              <h4>uname: {{ $route.params.uname }}</h4>
              <h4>pwd: {{ $route.params.pwd }}</h4>
          </div>
      </template>
      <script>
      import CommonView from '@/components/CommonView.vue'
      export default {
          name: 'SecondView',
          components: { CommonView }
      }
      </script>
      ```
2. 在 `src/router/index.js` 中配置路由

    * `path`：路由匹配的路径
    * `name`：路由配置名，可以用于命名路由
    * `component`：路由对应组件，可以是一个组件对象或其工厂函数

    ```js
    import { createRouter, createWebHistory } from 'vue-router'
    import FirstView from '@/views/FirstView.vue'

    // 路由列表
    const routes = [
        {
            path: '/first',
            name: 'first',
            component: FirstView
        },
        {
            path: '/second',
            name: 'second',
            component: () => import('@/views/SecondView.vue')
        }
    ]

    // 路由实例
    const router = createRouter({
        // 创建 HTML5 的历史记录
        history: createWebHistory(process.env.BASE_URL),
        routes
    })
    export default router
    ```
3. 在 `main.js` 中使用路由

    ```js
    // main.js
    import { createApp } from 'vue'
    import App from './App.vue'
    // 从 src/router 加载 router 模块
    import router from './router'
    // 注册 router
    createApp(App).use(router).mount('#app')
    ```
4. 创建根组件 `src/App.vue`，测试

    ```html
    <template>
      <nav>
        <router-link to="/first?uname=chenheng&pwd=123456">第一个页面</router-link> |
        <router-link to="/second/shenheng1/654321">第二个页面</router-link>
      </nav>
      <!-- <router-view> 是路由出口 -->
      <!-- 路由返回的组件将输出到这里 -->
      <router-view />
    </template>

    <style>
    #app {
      font-family: Avenir, Helvetica, Arial, sans-serif;
      -webkit-font-smoothing: antialiased;
      -moz-osx-font-smoothing: grayscale;
      text-align: center;
      color: #2c3e50;
    }

    nav {
      padding: 30px;
    }

    nav a {
      font-weight: bold;
      color: #2c3e50;
    }

    nav a.router-link-exact-active {
      color: #42b983;
    }
    </style>

    ```

<div style="
    border: 1px solid rgb(222, 222, 222);
    box-shadow: rgba(0, 0, 0, 0.06) 0px 1px 3px;
  ">
<style>
.w {
	overflow: hidden;
	margin: 0;
	padding: 0;
	background: none transparent;
	text-align: left;
}
.em > a,
.tc > a,
.th > a {
	background-color: transparent;
	-webkit-text-decoration-skip: objects;
}
.em a:not([href]):not([tabindex]),
.tc a:not([href]):not([tabindex]),
.th a:not([href]):not([tabindex]) {
	color: inherit;
	text-decoration: none;
}
.em a:not([href]):not([tabindex]):focus,
.tc a:not([href]):not([tabindex]):focus,
.th a:not([href]):not([tabindex]):focus {
	outline: 0;
}
.em > a,
.tc > a,
.th > a {
	text-decoration: none;
	color: inherit;
	-ms-touch-action: manipulation;
	touch-action: manipulation;
}
.w {
	line-height: 1.4;
	font-family: -apple-system, system-ui, BlinkMacSystemFont, 'Segoe UI',
		Roboto, 'Helvetica Neue', Arial, sans-serif;
	font-weight: 400;
	font-size: 15px;
	color: inherit;
	-webkit-hyphens: auto;
	-moz-hyphens: auto;
	-ms-hyphens: auto;
	hyphens: auto;
	word-wrap: break-word;
	overflow-wrap: break-word;
}
._rtl {
	direction: rtl;
	text-align: right;
}
.t,
.w,
.wf {
	display: -ms-flexbox;
	display: flex;
	-ms-flex-direction: column;
	flex-direction: column;
	max-width: 100%;
	width: 100%;
}
@supports (-webkit-overflow-scrolling: touch) {
	.w {
		max-width: 100vw;
	}
}
.wc,
.wt {
	overflow: hidden;
}
._sc,
._sm {
	background: inherit;
}
._or .tf {
	-ms-flex-order: 0;
	order: 0;
}
._or .th {
	-ms-flex-order: 1;
	order: 1;
}
._or .td {
	-ms-flex-order: 2;
	order: 2;
}
._alsr._ls .wf {
	-ms-flex-direction: column-reverse;
	flex-direction: column-reverse;
}
._alcr._lc .wf {
	-ms-flex-direction: row-reverse;
	flex-direction: row-reverse;
}
._sc._ls .wt,
._ss._ls .wt {
	padding-left: 0;
	padding-right: 0;
}
._sc._ls._alsd .wt,
._ss._ls._alsd .wt {
	padding-bottom: 0;
}
._sc._ls._alsr .wt,
._ss._ls._alsr .wt {
	padding-top: 0;
}
._sc._lc .wt,
._ss._lc .wt {
	padding-top: 0;
	padding-bottom: 0;
}
._ss._lc._alcd .wt {
	padding-right: 0;
}
._ss._lc._alcr .wt {
	padding-left: 0;
}
._lc .wf {
	-ms-flex-direction: row;
	flex-direction: row;
}
._lc .wt {
	display: -ms-flexbox;
	display: flex;
	-ms-flex: 1;
	flex: 1;
	-ms-flex-align: center;
	align-items: center;
}
._sc._lc._alcd .wf {
	padding-right: 0 !important;
}
._sc._lc._alcr .wf {
	padding-left: 0 !important;
}
.wt {
	padding: 8px 10px;
}
@media (min-width: 360px) {
	.wt {
		padding: 12px 15px;
	}
}
@media (min-width: 600px) {
	.wt {
		padding: 16px 20px;
	}
}
._lc._sm:not(.xd) .wc {
	min-width: 100px;
	width: 100px;
	min-height: 100px;
}
@media (min-width: 360px) {
	._lc._sm:not(.xd) .wc {
		min-width: 110px;
		width: 110px;
		min-height: 110px;
	}
}
@media (min-width: 460px) {
	._lc._sm:not(.xd) .wc {
		min-width: 140px;
		width: 140px;
		min-height: 140px;
	}
}
@media (min-width: 600px) {
	._lc._sm:not(.xd) .wc {
		min-width: 160px;
		width: 160px;
		min-height: 160px;
	}
}
._lc._sm._xd:not(._xf) .wc,
._lc._sm._xf:not(._xd) .wc {
	min-width: 100px;
	width: 100px;
	min-height: 100px;
}
@media (min-width: 360px) {
	._lc._sm._xd:not(._xf) .wc,
	._lc._sm._xf:not(._xd) .wc {
		min-width: 110px;
		width: 110px;
		min-height: 110px;
	}
}
@media (min-width: 600px) {
	._lc._sm._xd:not(._xf) .wc,
	._lc._sm._xf:not(._xd) .wc {
		min-width: 120px;
		width: 120px;
		min-height: 120px;
	}
}
._lc._sm._xd._xf .wc {
	min-width: 100px;
	width: 100px;
	min-height: 100px;
}
._lc._sc:not(.xd) .wc {
	min-width: 92px;
	width: 92px;
	min-height: 92px;
}
@media (min-width: 360px) {
	._lc._sc:not(.xd) .wc {
		min-width: 100px;
		width: 100px;
		min-height: 100px;
	}
}
@media (min-width: 460px) {
	._lc._sc:not(.xd) .wc {
		min-width: 130px;
		width: 130px;
		min-height: 130px;
	}
}
@media (min-width: 600px) {
	._lc._sc:not(.xd) .wc {
		min-width: 145px;
		width: 145px;
		min-height: 145px;
	}
}
._lc._sc._xd:not(._xf) .wc,
._lc._sc._xf:not(._xd) .wc {
	min-width: 92px;
	width: 92px;
	min-height: 92px;
}
@media (min-width: 360px) {
	._lc._sc._xd:not(._xf) .wc,
	._lc._sc._xf:not(._xd) .wc {
		min-width: 100px;
		width: 100px;
		min-height: 100px;
	}
}
@media (min-width: 600px) {
	._lc._sc._xd:not(._xf) .wc,
	._lc._sc._xf:not(._xd) .wc {
		min-width: 110px;
		width: 110px;
		min-height: 110px;
	}
}
._lc._sc._xd._xf .wc {
	min-width: 92px;
	width: 92px;
	min-height: 92px;
}
@supports (-moz-appearance: meterbar) and (all: initial) {
	._lc .wc {
		display: -ms-flexbox;
		display: flex;
		-ms-flex-direction: column;
		flex-direction: column;
		-ms-flex-align: stretch;
		align-items: stretch;
		-ms-flex-line-pack: stretch;
		align-content: stretch;
	}
}
._lc._ss:not(.xd) .wc {
	min-width: 100px;
	width: 100px;
	min-height: 100px;
}
@media (min-width: 360px) {
	._lc._ss:not(.xd) .wc {
		min-width: 110px;
		width: 110px;
		min-height: 110px;
	}
}
@media (min-width: 460px) {
	._lc._ss:not(.xd) .wc {
		min-width: 140px;
		width: 140px;
		min-height: 140px;
	}
}
@media (min-width: 600px) {
	._lc._ss:not(.xd) .wc {
		min-width: 160px;
		width: 160px;
		min-height: 160px;
	}
}
._lc._ss._xd:not(._xf) .wc,
._lc._ss._xf:not(._xd) .wc {
	min-width: 100px;
	width: 100px;
	min-height: 100px;
}
@media (min-width: 360px) {
	._lc._ss._xd:not(._xf) .wc,
	._lc._ss._xf:not(._xd) .wc {
		min-width: 110px;
		width: 110px;
		min-height: 110px;
	}
}
@media (min-width: 600px) {
	._lc._ss._xd:not(._xf) .wc,
	._lc._ss._xf:not(._xd) .wc {
		min-width: 120px;
		width: 120px;
		min-height: 120px;
	}
}
._lc._ss._xd._xf .wc {
	min-width: 100px;
	width: 100px;
	min-height: 100px;
}
._sc .wf {
	padding: 8px;
}
@media (min-width: 360px) {
	._sc:not(._xd):not(._xf) .wf {
		padding: 10px;
	}
}
@media (min-width: 460px) {
	._sc:not(._xd):not(._xf) .wf {
		padding: 12px;
	}
}
@media (min-width: 600px) {
	._sc:not(._xd):not(._xf) .wf {
		padding: 16px;
	}
}
._ls .th {
	-webkit-line-clamp: 2;
}
._ls._lh10 .th {
	max-height: 2em;
}
._ls._lh11 .th {
	max-height: 2.2em;
}
._ls._lh12 .th {
	max-height: 2.4em;
}
._ls._lh13 .th {
	max-height: 2.6em;
}
._ls._lh14 .th {
	max-height: 2.8em;
}
._ls._lh15 .th {
	max-height: 3em;
}
._ls .td {
	-webkit-line-clamp: 3;
}
._ls._lh10 .td {
	max-height: 3em;
}
._ls._lh11 .td {
	max-height: 3.3em;
}
._ls._lh12 .td {
	max-height: 3.6em;
}
._ls._lh13 .td {
	max-height: 3.9em;
}
._ls._lh14 .td {
	max-height: 4.2em;
}
._ls._lh15 .td {
	max-height: 4.5em;
}
._ls .twd {
	display: none;
}
@media (max-width: 459px) {
	._lc .ti,
	._lc .tm,
	._lc .tw + .tx,
	._lc .twt {
		display: none;
	}
}
@media (min-width: 460px) {
	._lc .twd {
		display: none;
	}
}
._lc:not(._ap):not(._ts) .th {
	-webkit-line-clamp: 3;
}
._lc:not(._ap):not(._ts)._lh10 .th {
	max-height: 3em;
}
._lc:not(._ap):not(._ts)._lh11 .th {
	max-height: 3.3em;
}
._lc:not(._ap):not(._ts)._lh12 .th {
	max-height: 3.6em;
}
._lc:not(._ap):not(._ts)._lh13 .th {
	max-height: 3.9em;
}
._lc:not(._ap):not(._ts)._lh14 .th {
	max-height: 4.2em;
}
._lc:not(._ap):not(._ts)._lh15 .th {
	max-height: 4.5em;
}
@media (max-width: 359px) {
	._lc:not(._ap):not(._ts) .td {
		display: none;
	}
}
@media (min-width: 360px) {
	._lc:not(._ap):not(._ts) .th {
		-webkit-line-clamp: 2;
	}
	._lc:not(._ap):not(._ts)._lh10 .th {
		max-height: 2em;
	}
	._lc:not(._ap):not(._ts)._lh11 .th {
		max-height: 2.2em;
	}
	._lc:not(._ap):not(._ts)._lh12 .th {
		max-height: 2.4em;
	}
	._lc:not(._ap):not(._ts)._lh13 .th {
		max-height: 2.6em;
	}
	._lc:not(._ap):not(._ts)._lh14 .th {
		max-height: 2.8em;
	}
	._lc:not(._ap):not(._ts)._lh15 .th {
		max-height: 3em;
	}
	._lc:not(._ap):not(._ts) .td {
		-webkit-line-clamp: 1;
	}
	._lc:not(._ap):not(._ts)._lh10 .td {
		max-height: 1em;
	}
	._lc:not(._ap):not(._ts)._lh11 .td {
		max-height: 1.1em;
	}
	._lc:not(._ap):not(._ts)._lh12 .td {
		max-height: 1.2em;
	}
	._lc:not(._ap):not(._ts)._lh13 .td {
		max-height: 1.3em;
	}
	._lc:not(._ap):not(._ts)._lh14 .td {
		max-height: 1.4em;
	}
	._lc:not(._ap):not(._ts)._lh15 .td {
		max-height: 1.5em;
	}
}
@media (min-width: 460px) {
	._lc:not(._ap):not(._ts) .td {
		-webkit-line-clamp: 2;
	}
	._lc:not(._ap):not(._ts)._lh10 .td {
		max-height: 2em;
	}
	._lc:not(._ap):not(._ts)._lh11 .td {
		max-height: 2.2em;
	}
	._lc:not(._ap):not(._ts)._lh12 .td {
		max-height: 2.4em;
	}
	._lc:not(._ap):not(._ts)._lh13 .td {
		max-height: 2.6em;
	}
	._lc:not(._ap):not(._ts)._lh14 .td {
		max-height: 2.8em;
	}
	._lc:not(._ap):not(._ts)._lh15 .td {
		max-height: 3em;
	}
}
._lc._ap:not(._ts) .th {
	-webkit-line-clamp: 3;
}
._lc._ap:not(._ts)._lh10 .th {
	max-height: 3em;
}
._lc._ap:not(._ts)._lh11 .th {
	max-height: 3.3em;
}
._lc._ap:not(._ts)._lh12 .th {
	max-height: 3.6em;
}
._lc._ap:not(._ts)._lh13 .th {
	max-height: 3.9em;
}
._lc._ap:not(._ts)._lh14 .th {
	max-height: 4.2em;
}
._lc._ap:not(._ts)._lh15 .th {
	max-height: 4.5em;
}
._lc._ap:not(._ts) .td {
	-webkit-line-clamp: 2;
}
._lc._ap:not(._ts)._lh10 .td {
	max-height: 2em;
}
._lc._ap:not(._ts)._lh11 .td {
	max-height: 2.2em;
}
._lc._ap:not(._ts)._lh12 .td {
	max-height: 2.4em;
}
._lc._ap:not(._ts)._lh13 .td {
	max-height: 2.6em;
}
._lc._ap:not(._ts)._lh14 .td {
	max-height: 2.8em;
}
._lc._ap:not(._ts)._lh15 .td {
	max-height: 3em;
}
@media (min-width: 360px) {
	._lc._ap:not(._ts) .th {
		-webkit-line-clamp: 2;
	}
	._lc._ap:not(._ts)._lh10 .th {
		max-height: 2em;
	}
	._lc._ap:not(._ts)._lh11 .th {
		max-height: 2.2em;
	}
	._lc._ap:not(._ts)._lh12 .th {
		max-height: 2.4em;
	}
	._lc._ap:not(._ts)._lh13 .th {
		max-height: 2.6em;
	}
	._lc._ap:not(._ts)._lh14 .th {
		max-height: 2.8em;
	}
	._lc._ap:not(._ts)._lh15 .th {
		max-height: 3em;
	}
	._lc._ap:not(._ts) .td {
		-webkit-line-clamp: 3;
	}
	._lc._ap:not(._ts)._lh10 .td {
		max-height: 3em;
	}
	._lc._ap:not(._ts)._lh11 .td {
		max-height: 3.3em;
	}
	._lc._ap:not(._ts)._lh12 .td {
		max-height: 3.6em;
	}
	._lc._ap:not(._ts)._lh13 .td {
		max-height: 3.9em;
	}
	._lc._ap:not(._ts)._lh14 .td {
		max-height: 4.2em;
	}
	._lc._ap:not(._ts)._lh15 .td {
		max-height: 4.5em;
	}
}
@media (min-width: 460px) {
	._lc._ap:not(._ts) .td {
		-webkit-line-clamp: 4;
	}
	._lc._ap:not(._ts)._lh10 .td {
		max-height: 4em;
	}
	._lc._ap:not(._ts)._lh11 .td {
		max-height: 4.4em;
	}
	._lc._ap:not(._ts)._lh12 .td {
		max-height: 4.8em;
	}
	._lc._ap:not(._ts)._lh13 .td {
		max-height: 5.2em;
	}
	._lc._ap:not(._ts)._lh14 .td {
		max-height: 5.6em;
	}
	._lc._ap:not(._ts)._lh15 .td {
		max-height: 6em;
	}
}
._lc._ts .th {
	-webkit-line-clamp: 1;
}
._lc._ts._lh10 .th {
	max-height: 1em;
}
._lc._ts._lh11 .th {
	max-height: 1.1em;
}
._lc._ts._lh12 .th {
	max-height: 1.2em;
}
._lc._ts._lh13 .th {
	max-height: 1.3em;
}
._lc._ts._lh14 .th {
	max-height: 1.4em;
}
._lc._ts._lh15 .th {
	max-height: 1.5em;
}
._lc._ts .td {
	-webkit-line-clamp: 2;
}
._lc._ts._lh10 .td {
	max-height: 2em;
}
._lc._ts._lh11 .td {
	max-height: 2.2em;
}
._lc._ts._lh12 .td {
	max-height: 2.4em;
}
._lc._ts._lh13 .td {
	max-height: 2.6em;
}
._lc._ts._lh14 .td {
	max-height: 2.8em;
}
._lc._ts._lh15 .td {
	max-height: 3em;
}
@media (min-width: 460px) {
	._lc._ts .th {
		-webkit-line-clamp: 1;
	}
	._lc._ts._lh10 .th {
		max-height: 1em;
	}
	._lc._ts._lh11 .th {
		max-height: 1.1em;
	}
	._lc._ts._lh12 .th {
		max-height: 1.2em;
	}
	._lc._ts._lh13 .th {
		max-height: 1.3em;
	}
	._lc._ts._lh14 .th {
		max-height: 1.4em;
	}
	._lc._ts._lh15 .th {
		max-height: 1.5em;
	}
	._lc._ts .td {
		-webkit-line-clamp: 3;
	}
	._lc._ts._lh10 .td {
		max-height: 3em;
	}
	._lc._ts._lh11 .td {
		max-height: 3.3em;
	}
	._lc._ts._lh12 .td {
		max-height: 3.6em;
	}
	._lc._ts._lh13 .td {
		max-height: 3.9em;
	}
	._lc._ts._lh14 .td {
		max-height: 4.2em;
	}
	._lc._ts._lh15 .td {
		max-height: 4.5em;
	}
}
@media (min-width: 460px) {
	._lc._xf:not(._xd)._ts .td {
		-webkit-line-clamp: 2;
	}
	._lc._xf:not(._xd)._ts._lh10 .td {
		max-height: 2em;
	}
	._lc._xf:not(._xd)._ts._lh11 .td {
		max-height: 2.2em;
	}
	._lc._xf:not(._xd)._ts._lh12 .td {
		max-height: 2.4em;
	}
	._lc._xf:not(._xd)._ts._lh13 .td {
		max-height: 2.6em;
	}
	._lc._xf:not(._xd)._ts._lh14 .td {
		max-height: 2.8em;
	}
	._lc._xf:not(._xd)._ts._lh15 .td {
		max-height: 3em;
	}
}
@media (min-width: 460px) {
	._lc._xf:not(._xd)._tl .td,
	._lc._xf:not(._xd)._tm .td {
		-webkit-line-clamp: 1;
	}
	._lc._xf:not(._xd)._tl._lh10 .td,
	._lc._xf:not(._xd)._tm .td {
		max-height: 1em;
	}
	._lc._xf:not(._xd)._tl._lh11 .td,
	._lc._xf:not(._xd)._tm .td {
		max-height: 1.1em;
	}
	._lc._xf:not(._xd)._tl._lh12 .td,
	._lc._xf:not(._xd)._tm .td {
		max-height: 1.2em;
	}
	._lc._xf:not(._xd)._tl._lh13 .td,
	._lc._xf:not(._xd)._tm .td {
		max-height: 1.3em;
	}
	._lc._xf:not(._xd)._tl._lh14 .td,
	._lc._xf:not(._xd)._tm .td {
		max-height: 1.4em;
	}
	._lc._xf:not(._xd)._tl._lh15 .td,
	._lc._xf:not(._xd)._tm .td {
		max-height: 1.5em;
	}
}
.t {
	-webkit-hyphens: auto;
	-moz-hyphens: auto;
	-ms-hyphens: auto;
	hyphens: auto;
}
.td,
.th {
	overflow: hidden;
	text-overflow: ellipsis;
	display: block;
}
@supports (display: -webkit-box) {
	.td,
	.th {
		display: -webkit-box;
		-webkit-box-orient: vertical;
	}
}
.td {
	vertical-align: inherit;
}
.tf,
.th {
	margin-bottom: 0.5em;
}
.td {
	margin-bottom: 0.6em;
}
._od .td:last-child,
._od .tf:last-child,
._od .th:last-child {
	margin-bottom: 0 !important;
}
._or .td {
	margin-bottom: 0 !important;
}
.tf {
	display: -ms-flexbox;
	display: flex;
	-ms-flex-align: center;
	align-items: center;
}
.tc {
	-ms-flex: 1;
	flex: 1;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}
.tim {
	display: block;
	min-width: 16px;
	min-height: 16px;
	width: 1em;
	height: 1em;
	margin-right: 6px;
}
._rtl .tim {
	margin-left: 6px;
	margin-right: 0;
}
.tx {
	opacity: 0.3;
	margin: 0 0.25em;
}
.tx:last-child {
	display: none !important;
}
._hd .td,
._hf .tf {
	display: none;
}
._hw .ti,
._hw .tw,
._hw .tw + .tx {
	display: none;
}
._hm .tm,
._hm .tw + .tx {
	display: none;
}
._hwi .ti {
	display: none;
}
._hwt .tw,
._hwt .tw + .tx {
	display: none;
}
._hmt .tmt,
._hmt .tmt + .tx {
	display: none;
}
._hmd .tm .tx,
._hmd .tmd {
	display: none;
}
._od._hf .td {
	margin-bottom: 0 !important;
}
._od._hd._hf .th,
._or._hd .th {
	margin-bottom: 0 !important;
}
@media (min-width: 460px) {
	.td {
		margin-bottom: 0.7em;
	}
}
._ffsa {
	font-family: -apple-system, system-ui, BlinkMacSystemFont, 'Segoe UI',
		Roboto, 'Helvetica Neue', Arial, sans-serif;
}
._ffse {
	font-family: Georgia, 'Times New Roman', Times, serif;
}
._ffmo {
	font-family: Menlo, Monaco, Consolas, 'Liberation Mono', 'Courier New',
		monospace;
}
._ffco {
	font-family: 'Comic Sans MS', 'Comic Sans', cursive;
}
._fwn {
	font-weight: 400;
}
._fwb {
	font-weight: 700;
}
._fsi {
	font-style: italic;
}
._fsn {
	font-style: normal;
}
._ttn {
	text-transform: none;
}
._ttu {
	text-transform: uppercase;
	letter-spacing: 0.025em;
}
._lh10 {
	line-height: 1;
}
._lh11 {
	line-height: 1.1;
}
._lh12 {
	line-height: 1.2;
}
._lh13 {
	line-height: 1.3;
}
._lh14 {
	line-height: 1.4;
}
._lh15 {
	line-height: 1.5;
}
._f3m {
	font-size: 11px;
}
._f0,
._f1m,
._f2m,
._f3m {
	font-size: 12px;
}
._f1p,
._f2p {
	font-size: 13px;
}
._f3p {
	font-size: 14px;
}
._f4p {
	font-size: 16px;
}
@media (min-width: 360px) {
	._f0 {
		font-size: 13px;
	}
	._f1p {
		font-size: 14px;
	}
	._f2p {
		font-size: 15px;
	}
	._f3p {
		font-size: 16px;
	}
	._f4p {
		font-size: 18px;
	}
}
@media (min-width: 460px) {
	._f1m {
		font-size: 13px;
	}
	._f0 {
		font-size: 14px;
	}
	._f1p {
		font-size: 15px;
	}
	._f2p {
		font-size: 16px;
	}
	._f3p {
		font-size: 18px;
	}
	._f4p {
		font-size: 21px;
	}
}
@media (min-width: 600px) {
	._f3m {
		font-size: 12px;
	}
	._f2m {
		font-size: 13px;
	}
	._f1m {
		font-size: 14px;
	}
	._f0 {
		font-size: 15px;
	}
	._f1p {
		font-size: 17px;
	}
	._f2p {
		font-size: 18px;
	}
	._f3p {
		font-size: 21px;
	}
	._f4p {
		font-size: 24px;
	}
}
.e {
	overflow: hidden;
	position: relative;
	width: 100%;
}
.e ._ls {
	height: 0;
	padding-bottom: 56.25%;
}
@supports (-moz-appearance: meterbar) and (all: initial) {
	._lc .e {
		-ms-flex: 1;
		flex: 1;
	}
}
._lc:not(._ap) .e {
	height: 100%;
	padding-bottom: 0;
}
.em {
	position: absolute;
	width: 100%;
	height: 100%;
}
.c,
.co {
	position: absolute;
	width: 100%;
	height: 100%;
}
.c {
	display: block;
	width: 100%;
	height: 100%;
	background: no-repeat center;
	background-size: cover;
}
.c {
	z-index: 20;
}
.co {
	z-index: 30;
}
.pr {
	position: absolute;
	width: 100%;
	height: 100%;
	z-index: 10;
}
.pr > video {
	width: 100%;
	height: 100%;
}
.pr .plyr {
	height: 100%;
}
.pv {
	display: block;
	width: 100%;
	height: 100%;
}
.w {
	background-color: inherit;
}
.t {
	line-height: 1.4;
	color: inherit;
}
.th {
	color: inherit;
}
.tf {
	color: #999;
}
.tw {
	color: #999;
}
</style>
  <div class="w __if _lc _sm _od _alsd _alcd _lh14 _xm _xi _ts _dm">
    <div class="wf">
      <div class="wc">
        <div class="e">
          <div class="em">
            <a href="https://router.vuejs.org/" target="_blank" rel="noopener" data-do-not-bind-click="" class="c" style="
                background-image: url('https://router.vuejs.org/logo.svg');
              "></a>
          </div>
        </div>
      </div>
      <div class="wt">
        <div class="t _f0 _ffsa _fsn _fwn">
          <div class="th _f1p _fsn _fwb">
            <a href="https://router.vuejs.org/" target="_blank" rel="noopener" class="thl">Vue Router</a>
          </div>
          <div class="td">The official Router for Vue.js</div>
          <div class="tf _f1m">
            <div class="tc">
              <a href="https://router.vuejs.org/" target="_blank" rel="noopener" class="tw _f1m"><span class="twt">https://router.vuejs.org/</span><span class="twd">https://router.vuejs.org/</span></a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
