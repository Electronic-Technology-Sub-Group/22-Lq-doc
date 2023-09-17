使用 Git 命令时提示 ssh: connect to host github.com port 22: Connection refused，因 22 端口被拒绝。
- 查看防火墙是否禁止 22 端口
- 检查 host 是否对 `github.com` 有重定向。某些代理工具，如 Steam++ 允许修改 host 代理 github

![[Pasted image 20230915172746.png]]

- 检查 DNS 服务器，使用 `ipconfig /flushdns` 清空 DNS 缓存
- 使用 443 端口替代 22 端口：修改 `/.ssh/config` 文件

```
Host github.com
  Hostname ssh.github.com
  Port 443
```
