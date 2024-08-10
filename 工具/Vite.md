
```cardlink
url: https://cn.vitejs.dev/
title: "Vite"
description: "Next Generation Frontend Tooling"
host: cn.vitejs.dev
favicon: https://cn.vitejs.dev/logo.svg
image: https://vitejs.dev/og-image.png
```

一种新型前端构建工具，提供开箱即用的配置
* 开发服务器：支持热更新的开发服务器
* 构建指令：使用 Rollup 打包

Vite 将应用模块分为依赖和源代码两类，对其采用不同策略
* 依赖：大多为在开发时不会变动的 JavaScript 代码，Vite 使用 Esbuild 预构建依赖
* 源代码：非直接 JavaScript 代码，需要转换（CSS、vue 组件等），Vue 以原生 ESM 提供源代码，且在浏览器请求时按需提供源代码

使用：

1. 安装，可以安装到全局，以后再创建项目不需要重新安装

    ```bash
    npm i vite -g
    ```
2. 初始化项目

    ```bash
    npm init vite <project-name>
    ```
3. 安装依赖

    ```bash
    npm i
    ```
4. 启动开发服务器

    ```bash
    npm run dev
    ```
