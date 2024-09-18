# JS 动画

通过注册 `JavaScript` 事件，使用代码控制过渡和动画

* 动画事件，事件处理函数第一个参数为 `el`，第二个参数为 `done`：

  * `before-enter`，`enter`，`after-enter`，`enter-cancelled`
  * `before-leave`，`leave`，`after-leave`，`leave-cancelled`
* `done` 参数是一个函数，当动画完成后必须调用 `done()` 通知 Vue 完成

`JS` 控制的动画和 CSS 过渡动画可以同时存在

* 同时使用 CSS 过渡动画和 JS 动画时，JS 事件中 `done` 可以不调用，CSS 动画完成后自动完成
* `:css="false"` 属性表示禁用 CSS 动画，可以防止 CSS 动画参数对 JS 动画的影响

```js
Vue.createApp({
    data() {
        return {
            show: false
        }
    },
    methods: {
        beforeEnter(el) {
            // 初始状态，只触发一次
            el.style.transform = 'translate(0, 0)'
        },
        enter(el, done) {
            // 获取 offsetWidth 强制刷新动画
            el.offsetWidth
            // 动画设置
            el.style.transform = 'translate(200px, -200px)'
            el.style.transition = 'all 3s cubic-bezier(0, 0.54, 0.55, 1.18)'
            // done 调用后不再触发 enter，并触发一次 after-enter
            done()
        },
        alterEnter(el) {
            this.show = !this.show
        }
    }
}).mount('#app')
```

```html
<div id="app">
    <button @click="show = !show" class="btn">添加到购物车</button>
    <transition 
            @before-enter="beforeEnter" 
            @enter="enter"
            @after-enter="afterEnter">
        <div v-if="show" class="ball"></div>
    </transition>
</div>
```

```css
.ball {
    width: 30px;
    height: 30px;
    border-radius: 50%;
    background: green;
    position: absolute;
    z-index: 99;
    top: 200px;
    left: 100px;
}
.btn {
    position: absolute;
    top: 200px;
}
```
