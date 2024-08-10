# v-on 等价方案

```js
app.component('my-component', {
    data() { return { value: '' } },
    render(h) {
        // <button @click="...">
        return Vue.h('button', {
            onClick: ev =>  { /* do something... */ }
        })
    }
})
```

‍
