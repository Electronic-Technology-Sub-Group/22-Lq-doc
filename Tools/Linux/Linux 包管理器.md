Linux 可以使用包管理器安装、更新、卸载应用，并可以自动处理依赖问题。通常包管理器需要 root 权限才能运行。
# RPM

使用 RPM 作为包管理器的系统，包安装文件以 `.rpm` 为后缀名
## yum

`yum` 是 RPM 的包管理器的默认管理工具，主要用于 CentOS 等

```bash
yum [-y] install|remove|search 包名
```
- `-y`：安装或卸载无需用户确认
- `install`：安装
- `remove`：卸载
- `search`：搜索
## 换源

yum 的源存在于 `/etc/yum.repos.d` 目录中，可以直接以 root 权限打开修改。

清华的 [CentOS 镜像站](https://mirrors.tuna.tsinghua.edu.cn/help/centos/)提供 CentOS 8 的更换代码如下：

```bash
```shell
sudo sed -e 's|^mirrorlist=|#mirrorlist=|g' \
         -e 's|^#baseurl=http://mirror.centos.org/$contentdir|baseurl=https://mirrors.tuna.tsinghua.edu.cn/centos|g' \
         -i.bak \
         /etc/yum.repos.d/CentOS-*.repo

sudo yum makecache
```
# APT

使用 APT 作为包管理器的系统，包安装文件以 `.deb` 为后缀名，主要用于 Debian，Ubuntu 等系统

```bash
apt [-y] install|remove|search 包名
```
- `-y`：安装或卸载无需用户确认
- `install`：安装
- `remove`：卸载
- `search`：搜索