利用 cmake 的代码，将其编译成 makefile 等其他编译工具的脚本。

CMake 脚本位于 `CMakeLists.txt` 中，基本结构为：

```cmake
# 设置 CMake 最低版本
cmake_minimum_required(VERSION <minimum-cmake-version>)
# 设置项目名
project(<project-name>)

# 添加可执行文件
add_executable(<target> <dependencies>)
```

CMake 依赖只需要指定 `cpp` 源文件，不需要指定头文件，CMake 会自动查找依赖的头文件。

```reference
file: "@/_resources/codes/cmake/hello/CMakeLists.txt"
lang: "cmake"
```

CMake 构建分为两步：
1. 生成构建脚本：`cmake -B build`
	- `-B`：指定构建目录
	- `-G`：指定构建工具
2. 构建：`cmake --build build`

# 参考

```cardlink
url: https://www.bilibili.com/video/BV14h41187FZ/
title: "IPADS新人培训第二讲：CMake_哔哩哔哩_bilibili"
description: "本次tutorial分享的是 GNU Make 和 CMake 的基本用法，主要内容是 CMake。以一个不断演进的样例程序为例，讲解在项目编写过程中针对什么需求可以用到 CMake 的什么特性，帮助刚加入实验室的新同学快速了解 CMake。主讲人：钱宇超（IPADS 二年级硕士）样例程序代码仓库：https://github.com/richardchien/modern-cmake-by-ex, 视频播放量 49818、弹幕量 114、点赞数 1052、投硬币枚数 961、收藏人数 2776、转发人数 258, 视频作者 上海交通大学IPADS, 作者简介 上海交通大学并行与分布式系统研究所官方账号。联系方式：邮箱：ipads@sjtu.edu.cn ，相关视频：现代操作系统：原理与实现（上海交通大学），基于VSCode和CMake实现C/C++开发 | Linux篇，IPADS新人培训第一讲：Shell，软件构建: CMake 快速入门，【公开课】现代CMake高级教程（持续更新中），【录播】现代C++中的高性能并行编程与优化（持续更新中），【CMake第一讲】：编写CMake的最底层逻辑，IPADS OSDI23论文介绍之HEDB，【文档向】CMake基础知识，【技术】手把手教你写CMake一条龙教程——421施公队Clang出品"
host: www.bilibili.com
image: https://i1.hdslb.com/bfs/archive/41ac5f8cca57b21a28b67233865a10b4db1fe9d0.jpg@100w_100h_1c.png
```
