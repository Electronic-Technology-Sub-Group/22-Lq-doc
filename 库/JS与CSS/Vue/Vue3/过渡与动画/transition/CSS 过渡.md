# CSS 过渡

`app` 程序中设置一个 `show` 变量，用于控制元素显示

```js
Vue.createApp({
    data() {
        return {
            show: true
        }
    }
}).mount('#app')
```

`app` 元素引入 `v-if="show"` 控制 `<img>` 显示与隐藏，并使用 `<transition>` 控制动画

```html
<div id="app">
    <button @click="show = !show">切换显示图片</button>
    <transition name="slide-img">
        <p v-if="show"><img src="img/99.jpg" /></p>
    </transition>
</div>
```

使用 `css` 设置过渡

```css
.slide-img-enter-active {
    transition: all 0.8s ease-out;
}
.slide-img-leave-active {
    transition: all 0.8s cubic-bezier(1, 0.5, 0.8, 1);
}
.slide-img-enter-from,
.slide-img-leave-to {
    transform: rotateX(45deg);
    transform: rotateY(45deg);
    transform: rotateZ(45deg);
    transform: rotate3d(1, 1, 1, 45deg);
    opacity: 0;
}
```
