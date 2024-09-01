记录使用 TypeScript 时踩得一些坑

# 使用 npm + webstorm 运行 ts

## Run Configuration for Typescript

起初，使用 `ts-node` 和插件 `Run Configuration for Typescript` 运行

![[Pasted image 20230508153819.png]]

```bash
npm i -g ts-node
```

## 手动配置

使用插件运行虽然方便，但命令行输出的编译异常中换行有问题。故手动配置项目：

手动配置也需要安装 `ts-node`

在 运行/调试配置 中，添加 `Node.js` 项目
- 节点形参：`--require ts-node/register`
- JavaScript 文件：`$FilePathRelativeToProjectRoot$`

在 `tsconfig.json` 中添加：
```json
{
	"compilerOptions": {
		"sourceMap": true
	}
}
```

## Cannot find module 'ts-node/register'

*这是 ts-node 的一个 Bug*

`ts-node` 不能安装到全局，需要安装到项目中

```bash
npm install ts-node
```

# TypeScript 使用 Module

- `package.json`

```json
{
	"type": "module"
}
```

- `tsconfig.json`

```json
{
	"compilerOptions": {
		"module": "ES2022", /* 选一个 ES2015 或之后的版本即可 */
		"esModuleInterop": true
	}
}
```

项目配置中，在 节点形参 中增加 `--loader ts-node/esm`

## Cannot find module 'express'

在项目中安装 `@types/express`

```bash
npm i @types/express --save-dev
```

有很多库都只需要安装 `@types/xxx` 即可

## Cannot find module 'axios'

详见 [axios/axios: Promise based HTTP client for the browser and node.js (github.com)](https://github.com/axios/axios#typescript)

在 `tsconfig.json` 中设置：
```json
{
	"compilerOptions": {
		"moduleResolution": "node16"
	}
}
```