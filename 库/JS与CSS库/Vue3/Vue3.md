Vue 是一个用于构建用户界面的渐进式 js 框架，采用自底而上的增量开发设计。

> MVVM：Model-View-ViewModel 模型。核心是提供 Model 和 ViewModel 的双向数据绑定，Model 层的变化自动映射到 View 层，无须手动操作 DOM，无须关注数据状态同步
>
> * Model：数据模型，也可以在其中定义数据修改和操作的业务逻辑
> * View：UI 组件，将数据转化为 UI 展示
> * ViewModel：同步 View 与 Model，同时与双方绑定

Vue 本身只是一个 js 库，通过尽可能简单的 API 实现数据绑定，可轻松实现 SPA 单页面应用。

* 引入本地独立 js 库或 CDN：`https://unpkg.com/vue`
* 使用 Vue CLI：`npm install vue@next`

单页面应用有助于减少访问请求，减轻服务器压力，但不利于 SEO 优化

# Hello Vue

![[image-20240521153840-npwpols.png]]

* 通过 `Vue.createApp( app配置 )` 创建一个 Vue app
* 通过 `app.mount(selector)`绑定 HTML 元素

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <script src="js/vue.global.js"></script>
</head>
<body>
<div id="hello-vue">
    {{ message }}
</div>
</body>
<script>
    const helloApp = {
        data() {
            return {
                message: "Hello Vue!"
            }
        }
    }
    const app = Vue.createApp(helloApp)
    const vm = app.mount("#hello-vue")
</script>
</html>
```

Vue 采取声明式渲染，利用模板将数据渲染到 DOM 中。任何数据变化都会响应式应用到页面上

页面中每隔 1s 数字加一。
* 任何一个 app 配置中的函数 `this` 都指向该 app
* `mounted` 是一个 Vue 生命周期钩子函数，在其中注册了一个定时器，每秒修改 `counter` 的值
* `{{ ... }}` 为模板的插值表达式

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <script src="js/vue.global.js"></script>
</head>
<body>
<div id="hello-vue">
    {{ counter }}
</div>
</body>
<script>
    const helloApp = {
        data() {
            return {
                counter: 0
            }
        },
        mounted() {
            setInterval(() => this.counter++, 1000)
        }
    }
    Vue.createApp(helloApp).mount("#hello-vue")
</script>
</html>
```
