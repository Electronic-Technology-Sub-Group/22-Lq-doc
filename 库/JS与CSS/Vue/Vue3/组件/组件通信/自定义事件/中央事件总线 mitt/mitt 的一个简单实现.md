# mitt 的一个简单实现

```js
// all 类型应为一个 Map，返回对象包含 on，off，emit 方法
function mitt(all) {
    all = all || new Map()
    return {
        all,
        on(type, handler) {
            const handlers = all[type]
            const added = handlers && handlers.push(handler)
            if (!added) {
                all[type] = [handler]
            }
        },
        off(type, handler) {
            const handlers = all[type]
            if (handlers) {
                handlers.splice(handlers.indexOf(handler) >>> 0, 1)
            }
        },
        emit(type, evt) {
            (all[type] || []).slice().map(handler => handler(evt))
            (all['*'] || []).slice().map(handler => handler(type, evt))
        },
    }
}
```

* `on(type, handler)`：注册事件处理器包含一个事件类型和事件处理器
* `off(type, handler)`：移除事件处理器
* `emit(type, evt)`：发布事件

  * 触发注册在 `type` 上的所有事件
  * 触发注册在 `*` 类型的事件处理器

‍
