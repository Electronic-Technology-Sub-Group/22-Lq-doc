# 事件绑定 v-on

通过 `v-on` 属性可以给一个元素添加 HTML 事件监听器，通常绑定的是一个方法

```html
<div id="event-handling">
    <p>{{ message }}</p>
    <button v-on:click="reverseMessage()">反转 message</button>
</div>
```

```js
Vue.createApp({
    data() {
        return {
            message: "Hello Vue!"
        }
    },
    methods: {
        reverseMessage() {
            this.message = this.message.split('').reverse().join('')
        }
    }
}).mount("#event-handling")
```

`v-on` 简写为 `@`，如果方法没有参数也可以省略 `()`

```html
<button @click="reverseMessage">反转 message</button>
```

# 原生 DOM 对象

使用 `$event` 可以访问原生 DOM 事件对象

```html
<div id="event-handling">
    <a href="https://www.baidu.com/" @click="warn('考试期间禁止百度！', $event)">百度</a>
</div>
```

```js
Vue.createApp({
    methods: {
        warn(message, event) {
            // 使用 DOM 原生事件对象禁用默认行为
            event.preventDefault()
            alert(message)
        }
    }
}).mount("#event-handling")
```

# 事件修饰符

针对 `event.preventDefault()`，`event.stopPropagation()` 等常用方法，Vue 为其设计了一系列事件修饰符

```html
<div id="event-handling">
    <a href="https://www.baidu.com/" 
       @click.prevent="alert('考试期间禁止百度！')">百度</a>
</div>
```

```js
Vue.createApp({
    methods: {
        warn(message) {
            alert(message)
        }
    }
}).mount("#event-handling")
```

所有事件修饰符包括：

|事件修饰符|说明|
| ------------| --------------------------|
|`stop`|阻止点击事件|
|`prevent`|阻止默认事件行为|
|`capture`|使用事件捕获模式<sup>（对应事件先由该元素处理，再由内部元素处理）</sup>|
|`self`|事件在该元素<sup>（而不是内部元素触发）</sup>触发时才触发|
|`once`|对应的事件回调只触发一次|
|`passive`|事件的默认行为立即触发|

```html
<div id="event-handling">
    <!-- 阻止点击事件 -->
    <a @click.stop="doThis"></a>
    <!-- 提交事件不再重载页面 -->
    <form @submit.prevent="onSubmit"></form>
    <!-- 串联修饰符 -->
    <a @click.stop.prevent="doThat"></a>
    <!-- 修饰符不带属性值 -->
    <form @submit.prevent></form>
    <!-- 事件监听器使用捕获模式 -->
    <div @click.capture="doThis"></div>
    <!-- 仅事件自身触发回调 -->
    <div @click.self="doThat"></div>
    <!-- 仅触发一次 -->
    <div @click.once="doThis"></div>
    <!-- 滚动事件默认行为（滚动）立即触发 -->
    <!-- 不等待 onScroll 完成 -->
    <div @scroll.passive="onScroll"></div>
</div>
```

在事件名后面连接一个或多个 `.事件修饰符` 即可，多个事件修饰符之间可以串联。

```html
<a @click.stop.prevent="doThat"></a>
```

带有事件修饰符的属性也可以没有值

```html
<form @submit.prevent></form>
```
