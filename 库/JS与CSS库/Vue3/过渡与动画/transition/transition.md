# transition

使用 `<transition>` 过渡封装组件包裹要实现过渡效果的组件，包括 Vue 组件和原生 HTML 元素，可以方便设置插入、更新或移除 DOM 时的过渡效果。

```html
<transition [name="<过渡名称>"]>
    ...
</transition>
```

`<transition>` 支持 <span data-type="text" parent-style="color: var(--b3-card-success-color);background-color: var(--b3-card-success-background);">v-if</span>、<span data-type="text" parent-style="color: var(--b3-card-success-color);background-color: var(--b3-card-success-background);"><component></span> 切换元素

CSS 过渡动画通过 style 类控制动画的播放与停止。当需要播放动画时，Vue 为元素添加对应的类，播放完成后移除。

```html
<div id="app">
    <!-- app.data.show = true -->
    <button @click="show = !show">
        点我，我将渐渐地离开，渐渐地来
    </button>
    <transition name="fade">
        <!-- fontSize: 30px; color: red -->
        <p v-show="show" 
           :style="animObj">动画实例</p>
    </transition>
</div>CSS 过渡
```

```html
<style>
    .fade-enter-active,
    .fade-leave-active {
        transition: opacity 2s ease;
    }
    .fade-enter-from,
    .fade-leave-to {
        opacity: 0;
    }
</style>
```

> `<transition>` 可以添加 `appear` 属性，表示在 DOM 加载完成后立即播放动画
>
> ```html
> <transition appear> ... </transition>
> ```

# 过渡动画

`<transition>` 允许同时使用 CSS 过渡动画和 JS 动画，CSS 过渡动画又分为 CSS 过渡和 CSS 动画两种。

> `<transition>` 同时使用过渡和动画时，需要设置播放动画的时间：
>
> * 使用 `type` 指定使用 `animation` 还是 `transition` 时间作为动画时间
>
>   ```html
>   <transition type="transition"> ... </transition>
>   ```
> * 使用 `duration` 属性设置动画时间，单位 `ms`
>
>   ```html
>   <transition :duration="{enter: 5000, leave: 5000}"> ... </transition>
>   ```

# 多元素动画

当有多个元素同时播放动画时，`<transition>` 默认 `enter` 和 `leave` 动画同时播放。可以通过 `mode` 属性修改：

* `in-out`：先进入后退出
* `out-in`：先退出后进入

```html
<transition mode="in-out"> ... </transition>
```

多个元素切换时，如果没有涉及到 DOM 变化的话需要加 `:key` 属性绑定到一个属性，使 Vue 为相同元素生成多个实例，否则不会触发切换动画

```html
<div id="app">
    <!-- handleClick 负责切换控件，实际就是改变 docState 值 -->
    <button @click="handleClick('saved')">显示 Edit</button>&nbsp;&nbsp;
    <button @click="handleClick('edited')">显示 Save</button>&nbsp;&nbsp;
    <button @click="handleClick('editing')">显示 Cancel</button>
    <br><br>
    <transition name="fade" mode="out-in">
        <!-- 添加了 :key 绑定了 docState 属性，该属性变化时 Vue 识别控件变化触发动画 -->
        <!-- buttonMessage 是一个计算属性，根据 docState 生成文本 -->
        <button :key="docState">{{buttonMessage}}</button>
    </transition>
</div>
```

```css
.fade-enter-from,
.fade-leave-to {
    opacity: 0;
}
.fade-enter-active,
.fade-leave-active {
    transition: opacity 0.5s ease;
}
```

`<component>` 变更 `is` 属性不需要加 `:key`

```html
<div id="app">
    <button @click="show">多组件过渡</button>
    <transition name="check" mode="out-in">
        <component :is="view"></component>
    </transition>
</div>
```

```css
.check-enter-from,
.check-leave-to {
    opacity: 0;
}
.check-enter-active,
.check-leave-active {
    transition: opacity 0.5s ease;
}
```

```js
Vue.createApp({
    data() {
        return {
            view: 'login'
        }
    },
    methods: {
        show() {
            this.view = this.view === 'login' ? 'register' : 'login'
        }
    },
    components: {
        login: { template: '#login' },
        register: { template: '#register' },
    }
}).mount('#app')
```

‍
