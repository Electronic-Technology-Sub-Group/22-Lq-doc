# 环境构建

一个 Mod 至少含有以下两个文件：

- `manifest.json`：Mod 加载入口，至少包含一个 `setup` 或 `load` 属性

```json
{
"setup": "setup.mjs"
}
```

| 属性      | 值类型  | 说明                                                        |
| --------- | ------- | ----------------------------------------------------------- |
| namespace | string? | Mod 命名空间，一些工具需要使用，不能以 `melvor` 开头        |
| icon      | string? | Mod 图标，位于 Mod 根目录，最大 38px 的 png 或 svg 图片     |
| setup     | string? | （`setup`、`load`二选一）JS Mod 模块入口，优先级高于 `load` |
| load          | string? 或 string\[]        | （`setup`、`load`二选一）按顺序加载资源。不推荐，推荐在 `setup` 中定义的入口里加载。可用资源包括：js（JS文件）, mjs（JS模块）, css, json, html                                                             |
- `setup.mjs`：前面 `manifest.json` 中 `setup` 属性定义的入口文件，至少导出一个 `setup` 方法

```js
export function setup(ctx) {
    // do something
    // Mod 入口
}
```
# Mod 样例

一个简单的双倍经验 Mod，修改 Skill 类达到获取所有双倍

```js
export function setup(ctx) {
    ctx.patch(Skill, 'addXP').before(function(amount, masteryAction) {
        return [amount * 2, masteryAction];
    });
}
```
# 设置选项

通过 `ctx.settings` 创建设置菜单

```js
export function setup(ctx) {
    // 设置菜单
    ctx.settings.section('General').add({
        type: 'number',
        name: 'xp-multiplier',
        label: 'XP Multiplier',
        hint: 'Multiply all XP gains by this amount',
        default: 2
    });

    // Mod 内容
    ctx.patch(Skill, 'addXP').before(function(amount, masteryAction) {
        const xpMultiplier = ctx.settings
                                .section('General')
                                .get('xp-multiplier');
        return [amount * xpMultiplier, masteryAction];
    });
}
```
# 发布

将 `manifest.json` 和所有 JavaScript 代码打包到一个 `zip` 包根目录，上传到 [Mod](mod.io) 即可。
# 使用资源
## 使用模块

通过 `ctx.loadModule` 导入 JS 模块文件

```js
// setup.mjs
export async function setup(ctx) {
    const configService = await ctx.loadModule('configService.mjs');
}
```

```js
// configService.mjs
const ctx = mod.getContext(import.meta);

// ...
```
## 使用脚本

通过 `ctx.loadScript` 导入 JS 脚本文件

```js
// setup.mjs
export async function setup(ctx) {
    await ctx.loadScript('hello-melvor-script.js');
}
```

```js
// hello-melvor-script.js
mod.register(ctx => {
    // ...
})
```

使用 `manifest.json` 的 `load` 属性也能引入 `js` 文件，与 `ctx.loadScript` 作用相同
## 其他资源

`context` 有各种方法可以导入不同资源：
- `loadTemplates`：导入 `html` 文件中的所有 `<template>` 标签
- `loadStylesheet`：导入一个 `css` 文件到当前页面
- `loadData`：导入一个 `json` 对象（返回该对象）
- `getResourceUrl`：导入图片、声音和其他类型资源，返回一个可以引用该资源的 url
# 修改游戏
## 生命周期

游戏允许在不同情况下注入不同回调代码

| 生命周期函数                 | 说明                                               |
| ---------------------------- | -------------------------------------------------- |
| `onModsLoaded`               | 所有 Mod 加载完成                                  |
| `onCharacterSelectionLoaded` | 角色选择界面加载完成                               |
| `onCharacterLoaded`          | 角色选择完成，所有游戏对象创建，但离线收益还未计算 |
| `onInterfaceReady`                             | 离线收益计算完成，游戏内 UI 可以被安全修改                                                   |
```js
export function setup(ctx) {
    ctx.onModsLoaded(ctx => {
        // do something
    });
}
```
## 游戏对象
### 通过 json 添加游戏对象

普通游戏对象可以通过 json 定义

优点
- 方便添加
- 易于分离游戏逻辑与数据
- 通过 `$schema` 允许某些编辑器支持代码提示
缺点
- 有些游戏对象无法通过该方法创建