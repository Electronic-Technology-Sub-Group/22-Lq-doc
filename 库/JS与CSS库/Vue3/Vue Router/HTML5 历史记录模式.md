# HTML5 历史记录模式

Vue 默认历史记录模式为 Hash 模式，通过 `createWebHashHistory()` 创建

推荐使用 HTML5 历史记录模式，通过 `createWebHistory()` 创建

```js
import { createRouter, createWebHistory } from 'vue-router'
const router = createRouter({
    history: createWebHistory()
})
```

* 优点：URL 看上去像一个正常的 URL
* 缺点：直接访问 URL 返回 404 错误，需要添加回退路由（若不匹配任何静态资源则返回 `index.html`）
