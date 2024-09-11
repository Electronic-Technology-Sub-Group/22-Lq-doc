
创建 `obsidian://advanced-uri` 开头的 URI，通过查询参数与 Obsidian 交互，可在 Obsidian 内使用，也可以通过浏览器、命令行打开。

> [!caution] 字符转义
> 可以使用 https://www.urlencoder.io/ 在线转义工具
> - ` `：`%20`
> - `/`：`%2F`
> - `%`：`%25`
> - 使用 `xdg-open` 打开 URI 需要二次转义（如 `/` 为 `%252F`）

# 框架

使用 `vault` 指定操作的笔记本，默认使用最后一次打开的笔记本

```
obsidian://advanced-uri?vault=<vault-name>
```

# 文件

指定操作的文件

| 键          | 值                   | 说明                    |
| ---------- | ------------------- | --------------------- |
| `filepath` | 文件路径                | 基于日记本的相对路径，可以省略 `.md` |
| `filename` | 文件名                 | 仅文件名，可忽略 `.md`        |
| `daily`    | bool，日记             | 使用当天日记，若不存在则自动创建      |
| `uid`      | UUID，frontmatter 区键 | 通过 UUID 查找文件          |


# 参考

```cardlink
url: https://vinzent03.github.io/obsidian-advanced-uri/zh-CN/
title: "Obsidian Advanced URI | Obsidian Advanced URI"
description: "概述"
host: vinzent03.github.io
favicon: https://vinzent03.github.io/obsidian-advanced-uri/zh-CN/img/favicon.ico
```
