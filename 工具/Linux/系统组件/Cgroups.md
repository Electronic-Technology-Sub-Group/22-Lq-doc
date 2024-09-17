Linux Cgroups 提供对一组进程及子进程的资源限制、限制和统计，包括 CPU、内存、存储、网络等，主要由 `cgroups` 、`subsystem`  和 `hierarchy`  组成

* `cgroups`：一种进程分组管理机制。一个 `cgroups` 包含一组进程，并在这个进程组里增加 Linux subsystem 参数配置
* `subsystem`：一组资源控制模块，关联到一个 `cgroups` 上，对该进程组的进程做限制和控制

  |subsystem 模块|功能|
  | ----------------| -------------------------------------------------------------------------------------------|
  |`blkio`|控制对块设备（硬盘等）输入输出的访问|
  |`cpu`|控制 `cgroups` 内进程的 CPU 调度策略|
  |`cpuacct`|统计 `cgroups` 中进程的 CPU 占用|
  |`cpuset`|控制 `cgroups` 中进程的 CPU 和内存<sup>（仅限 NUMA 架构）</sup>占用|
  |`devices`|控制 `cgroups` 中进程对设备的访问|
  |`freezer`|控制 `cgroups` 中进程的挂起和恢复|
  |`memory`|控制 `cgroups` 中进程的内存占用|
  |`net_cls`|分析 `cgroups` 中进程产生的网络包，分类以便 tc<sup>(traffic controller)</sup> 限流或监控|
  |`net_prio`|控制 `cgroups` 中进程产生网络包的优先级|
  |`ns`|使 `cgroups` 的进程在新 Namespace 中 fork 新进程时，创建出一个新 `cgroups`，该 `cgroups` 包含新 Namespace 进程|

  这里只是一部分，还有 `perf_event`，`pids`，`rdma`，`misc` 等

  通过 `lssubsys -a` 可以查看系统支持的模块

  `lssubsys` 包含在 `cgroup-tools` 软件包中
* `hierarchy`：把一组 `cgroups` 串成树状结构，实现 `cgroups` 的继承，该树状结构即 `hierarchy`。

Cgroups 通过三个组件的互相协作实现的

1. 系统创建 `hierarchy`（同时自动创建一个根 `cgroups` 节点），系统中所有进程加入该 `hierarchy` 的 `cgroups` 根节点
2. 一个 `subsystem` 只能附加到一个 `hierarchy` 上，一个 `hierarchy` 可以附加多个 `subsystem`
3. 一个进程可以属于多个 `cgroups`，但每个 `cgroups` 必须在不同 `hierarchy` 中
4. 一个进程 fork 出新进程时，子进程属于父进程的 `cgroups`，也可以移动到其他 `cgroups` 中

# Kernal 接口使用

1. 创建并挂载一个 `hierarchy`

    创建一个目录（`cgroup-test`）作为挂载点，挂载到 `cgroup` 作为 `hierarchy`

    ```bash
    mkdir cgroup-test
    sudo mount -t cgroup -o none,name=cgroup-test cgroup-test ./cgroup-test
    ```

    挂载后，该目录下自动创建的文件即 `cgroup` 的配置文件

![[../../../_resources/images/image-20240611205546-ebg30oe.png]]

    * `cgroup.clone_children`：默认为 0。若该文件值为 1，子 `cgroup` 继承父 `cgroup` 的 `cpuset` 配置
    * `cgroup.procs`：树中当前节点 `cgroup` 的进程组 ID，根节点则为系统中进程组的 ID
    * `notify_on_release`：标记该 `cgroup` 最后一个进程退出时是否执行了 `release_agent`
    * `release_agent`：通常用于进程退出后自动清理不再需要的 `cgroups`
    * `tasks`：该 `cgroups` 下的进程 ID，可用于将进程加入该 `cgroups`
2. 在 `hierarchy` 的根节点扩展出两个子 `cgroups` 节点

    直接创建目录即可，系统自动创建对应配置文件

    ```bash
    cd cgroup-test/
    sudo mkdir cgroup-1
    sudo mkdir cgroup-2
    ```

    可以通过 `tree` 查看

![[../../../_resources/images/image-20240611210545-cogusvy.png]]

3. 在 `cgroups` 中移动进程。系统默认进程在根 `cgroups` 上，只需要将进程 ID 写入对应 `cgroups` 的 `tasks` 上即可。

    ```bash
    cd cgroup-1
    # 将当前终端进程写入 cgroup-1 的 tasks 中
    sudo sh -c "echo $$ >> tasks"
    ```

    可以通过查看 `/proc/<pid>/cgroup` 确认

![[../../../_resources/images/image-20240611211146-epyb2r0.png]]

4. 通过 `subsystem` 限制 `cgroups` 资源

    ```bash
    sudo sh -c "echo "100m" > memory.limit_in_bytes"
    ```

‍
