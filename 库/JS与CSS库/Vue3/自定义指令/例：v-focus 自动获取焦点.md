# 例：v-focus 自动获取焦点

```html
<div id="app">
    <input><br>
    <input v-focus><br>
    <input>
</div>
```

```js
Vue.createApp({})
    .directive('focus', {
        mounted(el) {
            el.focus()
        }
    })
    .mount('#app')
```

‍
