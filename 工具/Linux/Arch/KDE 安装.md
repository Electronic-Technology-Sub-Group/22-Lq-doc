# 安装

`````col
````col-md
flexGrow=1
===
# 完整安装

```bash
# 安装 Plasm 桌面
pacman -S plasma
# 安装 KDE 应用
pacman -S kde-applications
```
````
````col-md
flexGrow=1
===
# 最小安装

```bash
# 安装 Plasm 桌面
pacman -S plasma-desktop
# 安装 KDE 应用
pacman -S kde-applications-meta
```
````
`````

> [!note] 中文字体包
> -  `noto-fonts`：基础字体包
> - `noto-fonts-cjk`：中日韩字体包
> - `noto-fonts-emoji`：表情字体包

# 启动

```bash
/usr/lib/plasma-dbus-run-session-if-needed /usr/bin/startplasma-wayland
```

使用 `xorg-xinit`

```bash title:.xinitrc
export DESKTOP_SESSION=plasma
exec startplasma-x11
```

或直接使用 `startx` 启动

```bash
startx /usr/bin/startplasma-x11
```

# 开机加载

```bash
systemctl enable sddm.service
```

自动登录可以编辑 `/etc/sddm.conf.d/autologin.conf`

> [!attention] 更换其他桌面管理器时，记得改 `Session` 属性

``` title:autologin.conf
[Autologin]
User=<用户名>
Session=plasma
```

# 中文字体

- 用户配置：
- 全局配置：

刷新字体缓存

```bash
fc-cache -fv
```

然后注销重新登录即可