Linux Namespace 是 Linux 内核提供的工具，可以隔离一系列系统资源，包括进程树、网络接口、挂载点等。

Linux Namespace 可以做到 UID 级别隔离。

| Namespace 类型    | 系统调用参数  | 隔离资源                                                                                                                        | 实现内核版本 |
| ----------------- | ------------- | ------------------------------------------------------------------------------------------------------------------------------- | ------------ |
| Mount Namespace   | CLONE_NEWNS   | 挂载点视图                                                                                                                      | 2.4.19       |
| UTS Namespace     | CLONE_NEWUTS  | nodename<sup>(网络上的主机名，标识特定的虚拟机和服务器)</sup> 和 domainname<sup>(系统网络信息服务（NIS）域名)</sup> 系统标识 | 2.6.19<br /> |
| IPC Namespace     | CLONE_NEWIPC  | `System V IPC` 和 `POSIX message queues`                                                                                     |              |
| PID Namespace     | CLONE_NEWPID  | 进程 ID                                                                                                                         | 2.6.24       |
| Network Namespace | CLONE_NEWNET  | 网络设备、IP地址端口等网络栈                                                                                                    | 2.6.29       |
| User Namespace    | CLONE_NEWUSER | `UserID` 和 `GroupID`                                                                                                        | 3.8          |

Namespace API 主要有：
* `clone()`：创建新进程，根据参数设置 Namespace
* `setns()`：将进程加入某个 Namespace 中
* `unshare()`：将进程移出某个 Namespace

‍
