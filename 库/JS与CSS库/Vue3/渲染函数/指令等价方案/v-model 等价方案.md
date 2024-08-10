# v-model 等价方案

```js
app.component('my-component', {
    data() { return { value: '' } },
    render(h) {
        const that = this
        // <input v-model="value">
        return Vue.h('input', {
            value: that.value,
            oninput: ev => that.value = ev.target.value
        })
    }
})
```

‍
