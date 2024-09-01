微软提供的 Windows 包管理器，Windows 11 默认安装，其他系统参考：

```cardlink
url: https://learn.microsoft.com/zh-cn/windows/package-manager/winget/#install-winget
title: "使用 WinGet 工具安装和管理应用程序"
description: "开发人员可以在 Windows 计算机上使用 WinGet 命令行工具来发现、安装、升级、删除和配置应用程序。"
host: learn.microsoft.com
image: https://learn.microsoft.com/en-us/media/open-graph-image.png
```

# 查找

`winget search/find <应用名>`

一些常用的选项有：
- `--id` 、`--name`、`--tag`、`--cmd/--command`：查找方式
- `--source` / `-s`：指定源
- `--versions`：显示所有可用版本
- `--proxy`、`--no-proxy`：代理设置

# 安装

`winget install/add <应用名>`

用于搜索的选项也适用于安装时搜索包，其他常用选项有：
- `--version/-v`：指定安装版本
	- `--no-upgrade`：如果已安装旧版本，不升级
	- `--uninstall-previous`：如果已安装旧版本，卸载旧版本
- `--silent/-h`：静默安装
- `--interactive/-i`：交互式安装
- `--location/-l`：安装位置

# 下载

仅下载安装包，`winget download <应用名> <options>`

用于搜索的选项也适用于安装时搜索包，其他常用选项有：
- `--download-directory/-d`：下载位置
- `--version/-v`：指定下载版本
- `--platform`：选择目标平台

# 批量安装

使用 `export` 导出包信息 `json` 文件

`winget export [-o] <json 文件路径>`
- `--include-versions`：包含版本信息，否则为 `latest`
- `--source/-s`：指定导出的源

然后使用 `import` 导入

`winget import [-i] <json 文件路径>`
- `--ignore-unavailable`：忽略不可用包
- `--ignore-versions`：忽略版本信息
- `--no-upgrade`：跳过升级

# 管理

使用 `list` 显示所有应用，也可以查看是否可升级

`winget list [-q 查询]`
- `--upgrade-available`：仅查看可升级应用
	- `--include-unknown`：显示未知版本
- 支持筛选应用，也支持 `--id`、`--name` 等查询方式

# 更新

使用 `upgrade` 更新应用；不接任何应用时，显示系统中所有可更新应用

`winget upgrade/update <应用>`
- `--version/-v`：指定版本，默认 `latest`
- `--recurse/-r/-all`：升级所有可升级应用
- `--unknown/-u/--include-unknown`：包括升级无法确定版本的应用
- `--pinned/--include-pinned`：升级使用 `pin` 固定版本的应用
- `--uninstall-previous`：删除早期版本

使用 `pin` 将指定应用版本，可以指定升级到的版本，也可以阻止升级包

固定有三种模式：
- `pinning`：默认，从 `upgrade --all` 中排除，但允许使用 `upgrade <应用>`
- `blocking`：禁止 `upgrade --all` 和 `upgrade <应用>`
- `gating`：固定到某个确定版本或某个版本范围

`winget pin add/remove/list/reset`
- `add <应用>`：添加一个新 `pin`
	- `--installed`：固定已安装版本
	- `--blocking`：固定模式切换为 `blocking`
	- `--version/-v`：固定到某个版本，固定模式切换为 `gating`，表示某个版本范围时使用通配符 `*`
- `remove [-q <应用>]`：移除一个 `pin`
- `list`：显示所有 `pin`
- `reset`：重置一个 `pin`

# 卸载

`winget uninstall `

# 设置

# 脚本

以下选项多用于创建安装脚本：
- `--wait`：提示使用任意键退出
- `--nowarn/--ignore-warning`：隐藏警告
- `--disable-interactivity`：禁用交互提示
- `--verbose/--verbose-logs`：显示详细日志
- `--accept-source-agreements`：接受源许可协议，并不显示提示
- `--accept-package-agreements`：接受许可协议，并不显示提示
- `--installer-type`：安装类型，`EXE`、`ZIP`、`MSI` 等
- `--silent/-s`：静默安装
- `--scope`：安装到当前用户还是所有用户
- `--custom`：除默认值外传递给安装程序的参数
- `--override`：直接传递给安装程序的字符串