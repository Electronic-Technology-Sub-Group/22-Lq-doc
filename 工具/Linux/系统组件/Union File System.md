---
icon: SiLinux
---
Union File System，简称 `UnionFS` ，是为 Linux、FreeBSD、NetBSD 设计的文件系统服务，可以把其他文件系统联合到一个联合挂载点。
* `branch`：使用 `branch` 将不同文件系统的文件和目录透明覆盖，形成单一一致的文件系统
* 写时复制：CoW，copy-on-write，也叫隐式共享，对可修改资源实现高效复制。如果一个资源不被修改，不需要创建一个新资源，可以被旧实例共享；在第一次对该资源修改时，创建一个新资源

  该特性可以让一个只读挂载点的文件被修改，并仅将修改的数据保存在可读写的挂载点上。比如 `Knoppix` 可以将一个 CD 和一个可读写设备上的 `knoppix.img` 的文件系统联合起来，任何对 CD 的修改将应用到 U 盘上，不改变 CD 本来内容
# AUFS

新版本 Linux 中不再存在 `AUFS`，而是使用 `OverlayFS` 替代

Advanced Multi-Layerd Unification Filesystem，重写了 UnionFS 1.x，提高了可靠性和性能，引入可写分支负载等新功能。

> ```bash
> sudo mount -t aufs -o \
>      dirs=./container-layer:./image-layer4:./image-layer3:./image-layer2:./image-layer1 none ./mnt
> ```
>
> 将以下目录以 AUFS 的方式挂载到 `./mnt`，第一个可读写，剩下的只读
>
> * `./container-layer` 权限 rw
> * `./image-layer4` 权限 ro
> * `./image-layer3` 权限 ro
> * `./image-layer2` 权限 ro
> * `./image-layer1` 权限 ro
>
> 可以通过 `cat /sys/fs/aufs/<???>/*` 查看
>
> 当尝试修改后四个目录的文件时，原本文件不变，复制一份到第一个目录并修改
# OverlyFS

OverlayFS 是一种堆叠文件系统，它依赖并建立在其它的文件系统之上，不直接参与磁盘空间结构的划分，仅将原来文件系统中不同目录和文件进行合并。

```bash
sudo mount -t overlay -o\
     lowerdir=image-layer4:image-layer3:image-layer2:image-layer1,\
     upperdir=container-layer,\
     workdir=template-layer overlay mnt
```

挂载一个 `overlay` 文件系统，可以通过 `mount -t overlay -o <options> overlay <mount point>` 来实现。

`options` 以 `,` 分隔

其中 `overlay` 的 `options` 如下：

* `lowerdir=<dir>`：指定用户需要挂载的 lower 层目录，用 `:` 间隔，优先级依次降低。最多支持 500 层
* `upperdir=<dir>`：指定用户需要挂载的 upper 层目录
* `workdir=<dir>`：指定文件系统挂载后用于存放临时和间接文件的工作基础目录。
* `redirect_dir=on/off`：开启或关闭 redirect directory<sup>(开启后可支持 merged 目录和纯 lower 层目录的 rename/renameat 系统调用。)</sup> 特性
* `index=on/off`：开启或关闭 index<sup>(开启后可避免 hardlink copyup broken 问题。)</sup> 特性

说明：

* upper 层优先级高于所有的 lower 层目录
* lowerdir 只读，upperdir 可写，upperdir 存在时应同时指定 workdir
* 修改（包括删除） lowerdir 数据时遵循 CoW 规则

‍
