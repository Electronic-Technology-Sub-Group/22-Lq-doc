# Error: listen EACCES: permission denied ::1:8000

```
error when starting dev server:
Error: listen EACCES: permission denied ::1:8000
    at Server.setupListenHandle [as _listen2] (node:net:1855:21)
    at listenInCluster (node:net:1920:12)
    at GetAddrInfoReqWrap.doListen [as callback] (node:net:2069:7)
    at GetAddrInfoReqWrap.onlookup [as oncomplete] (node:dns:109:8)

```

端口被占用。使用 `netstat -ano` 查看发现被 `PID=5152` 的进程占用

![[image-20240603000706-m5m69hk.png]]

经检查发现该进程为 `httpd.exe`，是 `Incredibuild` 自带的一个组件，将其结束并禁止自启即可。

```
  VITE v5.2.12  ready in 311 ms

  ➜  Local:   http://localhost:8000/
  ➜  Network: use --host to expose
  ➜  press h + enter to show help

```
