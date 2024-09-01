为普通命令进行授权，临时以 root 权限执行

```bash
sudo 其他命令
```

注意：要使用该命令，需要给对应用户授权。在 root 权限下进行：

1. 运行 `visudo` 打开 `/etc/sudoers` 文件
2. 在文件末尾追加 `用户名 ALL=(ALL) [NOPASSWD:ALL]`，其中 `NOPASSWD: ALL` 表示使用 `sudo` 时无需输入密码
3. `wq` 保存退出