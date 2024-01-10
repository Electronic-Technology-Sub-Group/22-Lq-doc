> [!error]
> 那么，有什么用呢？
> 我也不知道啊，我的极3Pro也不能刷机啊啊啊啊

访问 [极路由ROOT local-ssh利用工具 (hiwifi.wtf)](http://www.hiwifi.wtf/) 它会提供两个网址，同时打开

![[Pasted image 20240108205414.png]]

第一个进入将 token 填入 local token 输入框

![[Pasted image 20240108205445.png]]

第二个打开后是一个 json，将其中 uuid 后的值（不带双引号）填入 uuid 输入框

![[Pasted image 20240108205520.png]]

然后点击提交。网站会提供 cloud token，填入第一个网页的 cloud token 输入框

![[Pasted image 20240108205632.png]]

![[Pasted image 20240108205647.png]]

点击提交，提示 Success: ssh port is 22 表示临时 root 权限获取成功。

![[Pasted image 20240108205702.png]]

ssh 连接地址就是管理地址，可以在极路由后台查询

![[Pasted image 20240108205855.png]]

打开命令行，进行连接，密码即后台管理密码

```bash
ssh 192.168.199.1 -p 22 -l root
```

![[Pasted image 20240108210013.png]]

出现 该界面表示登陆成功。运行以下命令开启永久 ssh 登录：

```bash
/etc/init.d/dropbear enable && /etc/init.d/dropbear start
```

至此永久 root 权限获取成功