---
参考资料: https://wiki.melvoridle.com/w/Mod_Creation/Mod_Context_API_Reference
---
# 上下文环境

Melvor Mod 上下文可以通过以下几种途径获取：

- 在 `manifest.json` 中定义了 `setup` 函数

```js
export function setup(ctx) {
    // do something
}
```

- 传统 JS 配置

```js
const ctx = mod.getContext(import.meta);
```

- 从 Mod 注册函数中获取

```js
mod.register(ctx => {
    // do something
})
```

- 从生命周期函数获取

```js
onCharacterLoaded(ctx => {
    // do something
})
```

- 获取开发环境上下文，不应在实际 Mod 中使用

```js
const devCtx = mod.getDevContext();
```
# 