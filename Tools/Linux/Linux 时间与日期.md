# date

查看当前的系统时间

```bash
date [-d 时间] [+格式化字符串]
```
- `-d`：基于当前日期进行运算
	- `+n -n`：向后推进、向前提前
	- `year`，`month`，`day`，`hour`，`minute`，`second`：可选单位
![[Pasted image 20230811155618.png]]

- 格式化字符串：用于特定字符串标记，控制显示格式。若存在特殊字符（空格等）加双引号

| 占位符 | 说明             |
| ------ | ---------------- |
| `%Y`   | 年               |
| `%y`   | 年（后两位数字） |
| `%M`   | 月               |
| `%d`   | 日               |
| `%H`   | 时               |
| `%M`   | 分               |
| `%S`   | 秒               |
| `%s`   | 秒（时间戳）     | 

![[Pasted image 20230811155801.png]]
# 时区

**更改时区操作需要 root 权限**

Linux 下系统时区存在 `/etc/localtime` 文件中，所有支持的时区在 `/use/share/zoneinfo` 中。因此我们只要将原有时区文件删除，将新时区文件连接过去即可

```bash
rm -f /etc/localtime
sudo ln -s /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
```
# ntp

包 ntp 可用于自动校准系统时间，该软件包安装后会自动添加 `ntpd` 服务。

CentOS 8+ 使用 chrony 软件包，服务为 chronyd

```bash
# 设置开机启动
systemctl start ntpd
systemctl enable ntpd
```

也可以手动校准，需要 root 权限

```bash
ntpdate -u 校准地址 
```

校准地址可用阿里云提供的服务 `ntp.aliyun.com`
