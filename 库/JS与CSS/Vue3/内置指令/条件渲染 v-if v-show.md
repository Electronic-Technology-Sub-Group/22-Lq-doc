# `v-if`

条件渲染使用一组连续的 `v-if`，`v-else-if`，`v-else` 标签元素

```js
Vue.createApp({
    data() {
        return {
            score: 0
        }
    }
}).mount("#v-if-handling")
```

```html
<div id="v-if-handling">
    <div v-if="score >= 90">优秀</div>
    <div v-else-if="score >= 80">良好</div>
    <div v-else-if="score >= 70">中等</div>
    <div v-else-if="score >= 60">及格</div>
    <div v-else>不及格</div>
    <label>成绩：
        <input type="number" v-model="score">
    </label>
</div>
```

> `v-if` 系列属性用于一个单独标签。如果要将一个 `if` 分支应用于多个块，可以使用 `<template>` 模板占位符元素：
>
> ```html
> <template v-if="ok">
>     <h1>Title</h1>
>     <p>Paragraph 1</p>
>     <p>Paragraph 2</p>
> </template>
> ```

# `v-show`

`v-show` 类似于 `v-if`，区别在于每次变更显示状态时不会从 DOM 中移除元素，而是使用 `display:none` 隐藏元素

> `v-show` 有更高的初始渲染消耗，`v-if` 有更高的切换消耗。
>
> `v-show` 适用于需要频繁切换的场景，`v-if` 适用于加载后几乎不需要切换的场景

```html
<div id="v-show-handling">
    <div v-if="flag">v-if</div>
    <div v-show="flag">v-show</div>
    <button @click="flag=!flag">切换</button>
</div>
```

```js
Vue.createApp({
    data() {
        return {
            flag: true
        }
    }
}).mount("#v-show-handling")
```

![[image-20240522145801-svvdgmz.png]]

![[image-20240522145942-n5rqwtz.png]]

‍
