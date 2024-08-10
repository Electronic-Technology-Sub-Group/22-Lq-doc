# CSS 动画

CSS 动画与 CSS 过渡类似，区别在于 `v-enter` 类可能不会立即删除，而是在动画完成后删除。其他与 CSS 过渡相同，只是 `css` 部分使用 `animation` 和 `@keyframes` 设置动画即可

`<transition name="bounce-img">`

```css
.bounce-img-enter-active {
    animation: bounce-in 0.8s;
}
.bounce-img-leave-active {
    animation: bounce-in 0.8s reverse;
}
@keyframes bounce-in {
    0% {
        transform: scale(0);
    }
    50% {
        transform: scale(1.25);
    }
    100% {
        transform: scale(1);
    }
}
```

‍
