# modules

将应用所有状态都集中到 `store` 将会导致其过于臃肿。Vuex 允许将 `store` 中的状态分割为多个模块 `module`。

* 每个模块都可以包含各自的 `state`，`getters`，`mutations`，`actions`，`modules`
* 子模块通过 `getters`、`mutations`、`actions` 中的函数通过第三个参数 `rootState` 访问根模块的状态
* 通过根模块可以使用 `state.<module 1>...` 访问子模块的状态

```js
const moduleA = {
    state: () => ({ ... }),
    getters: { ... },
    mutations: { ... },
    actions: { ... },
}

const moduleB = {
    state: () => ({ ... }),
    getters: { ... },
    mutations: { ... },
    actions: { ... },
}
```

```js
const store = createStore({
    modules: {
        a: moduleA,
        b: moduleB
    }
})

// 使用
store.state.a
```

‍
