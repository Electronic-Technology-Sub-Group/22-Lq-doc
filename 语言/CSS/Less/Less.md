> 给 CSS 加点料

Leaner Style Sheets 的简称，是一种向后兼容的 CSS 扩展语言。

[Less 快速入门 | Less.js 中文文档 - Less 中文网 (bootcss.com)](https://less.bootcss.com/)

# 使用

## Node.js

Less 有一套配合的编译工具 `Less.js`，通过 `npm` 安装：

```bash
npm install less -g
```

这类编译工具常安装于全局，也可以直接安装在项目中，也能用

```bash
npm install less --save-dev
```

`less.js` 提供 `lessc` 工具，依赖于 `Node.js` 环境

```bash
lessc [option option=parameter ...] <source> [description]
```
- option：参数
	- --silent：控制台中不显示任何警告信息
	- --version，-v：显示当前 `less.js` 版本
	- --help，-h：显示所有参数帮助信息
	- --depends，-M：显示依赖列表
- source：源文件或 `-`
	- `-`：从控制台读入文件
- description：输出文件，默认为当前目录同名 .css 文件

若不需要 less 导出成 css，只需要在文件开头添加 `// out:false` 即可，常用于需要 `@import` 到其他文件中的公共样式：

```Less
// out:false
```

## 浏览器

浏览器环境中可以直接使用 Less，包括 IE 在内的几乎所有浏览器和移动端浏览器都支持

```HTML
<html>
<head>
    <link rel="stylesheet/less" type="text/css" href="css/styles.less"/>  
    <script src="https://cdn.jsdelivr.net/npm/less@4"></script>
</head>
</html>
```

# 作用域

类似 CSS 作用域，先查找本地变量和混合，否则向上查找父作用域，逐级向上

# 注释

除了 CSS 中的 `/* 注释内容 */` 样式的多行注释外，还可使用单行注释：`// 注释内容`

# 用法

1. [[Less 运算]]
2. [[Less 变量]]
3. [[Less 选择器]]
4. [[Less 合并]]
5. [[Less Mixin]]
6. [[Less 解绑规则]]
7. [[Less @import]]
8. [[Less @plugin]]
9. [[Less 附录]]

