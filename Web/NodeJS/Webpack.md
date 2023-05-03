[指南 | webpack 中文文档 (docschina.org)](https://webpack.docschina.org/guides/)

Webpack 是个模块打包器，用于打包前端项目，优化项目文件结构，提高加载效率。

![[Pasted image 20230430110443.png]]

Webpack 主要有以下几个特点：

- 代码拆分：以异步依赖为分割点，优化依赖树后每个异步都会被打包，条理清晰
- Loader：Loader 可以识别和解析 CSS，SASS，LESS 等，将其转化为 JS 并打包
	- LESS, SESS -> CSS
	- ES6, ES7 -> ES5
- 插件：Webpack 支持丰富的插件系统
- 效率：使用异步 I/O 和多级缓存提高运行效率

# 导入

- 使用 yarn 或 npm 安装到项目
- 修改 `package.json`，添加：

```json
{
  // ...
  "scripts": {
	  "build": "webpack"
  }
}
```

# 打包

1. 确保存在入口，入口文件默认为项目根目录下的 `index.js` 文件
2. 将依赖引入 js 文件中，webpack 默认只支持 JS 文件，其他需要添加对应的 Loader
	- 图片例外：使用 asset modules 技术打包 - 在 rules 里对对应文件设置 type: asset
3. 执行 `build` 任务，`webpack` 自动打包生成 `dist/main.js`

# 配置

Webpack 配置文件为项目根目录的 `webpack.config.js` 文件，没有新建即可，配置文档可在以下网站查询：
[概念 | webpack 中文文档 (docschina.org)](https://webpack.docschina.org/concepts/)

# 插件

[插件 | webpack 中文文档 (docschina.org)](https://webpack.docschina.org/plugins/)

- [HtmlWebpackPlugin](https://webpack.docschina.org/plugins/html-webpack-plugin/)：用于打包时自动生成 HTML5 文件

# Loader

[css-loader | webpack 中文文档 (docschina.org)](https://webpack.docschina.org/loaders/css-loader/)

- [css-loader](https://webpack.docschina.org/loaders/css-loader/)：允许打包 CSS 文件
- [style-loader](https://webpack.docschina.org/loaders/style-loader/)：将 CSS 添加到 DOM 中
- [babel-loader](https://webpack.docschina.org/loaders/babel-loader/)：：使用 [[Babel]] 插件降级 JS

# 开发服务器

[开发环境 | webpack 中文文档 (docschina.org)](https://webpack.docschina.org/guides/development/#using-webpack-dev-server)

`webpack-dev-server` 模块是 Webpack 提供的一个简易服务器，具有实时监视文件改变，修改后自动编译和重载功能，可快速查看修改结果