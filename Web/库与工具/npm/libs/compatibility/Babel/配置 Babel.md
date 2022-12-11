配置内容可以包含在 `package.json` 中，但更常见的是在项目中创建一个名为 `.babelrc` 的文件，之后再向内添加配置内容，配置文件的格式是 `json` 格式

```json
{
"presets": [
  ["env",
  {
    "modules": false,
    "targets": {
      "browsers": ["> 1%", "last 2 versions", "not ie <= 8"]
    }
  }],
  "stage-2"
],

"plugins": ["transform-vue-jsx", "transform-runtime"]
}
```

`presets` 配置当前 ES 版本信息。
- 第一个 `env` 表示使用最新的 ES 标准并向下兼容
- 第二个对象为配置
	- `modules` 表示是否对项目中的依赖应用 Babel
	- `targets` 表示当前项目支持的目标，`browsers` 表示应用于浏览器，其参数由 `browserslist` 插件提供，示例中的属性表示：
		- 用户数量 >1%
		- 兼容最新的两个版本
		- 不兼容 IE 8 及以下的版本
- 第三个是兼容 JS 规范的制作阶段，`stage-2` 代表 `babel-preset-stage-2` 插件
	- stage0：TC 39 的讨论项目
	- stage1：正式化提案
	- stage2：草案已有规范，并以补丁形式向浏览器推送
	- stage3：候选推荐
	- stage4：测试实现，通过后将在下一个 ES 版本中正式发布

`plugin` 则用于向 Babel 中添加插件，其中 `transform-vue-jsx` 用于 Vue.js 支持 JSX 的语法