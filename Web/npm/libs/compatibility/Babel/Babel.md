Babel 是一个[[JavaScript]]转义器，提供将使用新版本标准书写的 `JavaScript` 代码转译成较低版本的 JS 规则，以保证浏览器的兼容性。

Babel 需要[[npm]]环境，安装到项目目录下即可，无需安装到全局。

Babel 通过将当前代码解析成 AST，替换其中 ES6 的内容，最后重新生成 JS 代码，实现转义降级。

- 核心模块
	- `babel-core`：提供转义 API，用于转义代码
	- `babylon`：JS 解析器，默认使用 ES2017
	- `babel-traverse`：用于查找 AST 树
	- `babel-generator`：转义最后一步，用于根据 AST 生成代码
- 功能包
	- `babel-types`：用于对 AST 的校验、构建和修改
	- `babel-template`：为 `babel-types` 构建 AST 树提供辅助
	- `babel-code-frames`：生成错误信息，指出错误位置
	- `babel-helpers`：包含一系列 `babel-template` 函数，提供给 `plugin` 使用
	- `babel-plugin-xxx`：转义中使用的插件
	- `babel-preset-xxx`：转义中使用的插件（针对 ES 标准）
	- `babel-polyfill`：用于构建一个完整的 ES6+ 环境
	- `babel-runtime`：类似 `babel-polyfill`，但不会污染全局作用域
- 工具包
	- `babel-cli`：命令行工具
	- `babel-register`：用于转义 `require` 引用的 JS 文件

在使用 Babel 前，我们需要先[[配置 Babel]]，之后再[[调用 Babel]]。