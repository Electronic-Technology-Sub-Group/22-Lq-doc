
OpenGL 是一套用于屏幕渲染的 C 库。OpenGL 只定义 API 规范，具体实现由宿主系统决定，通常由显卡驱动或操作系统实现。OpenGL 使用 C/S 架构，负责渲染的部分（通常由显卡执行）称为服务端，CPU 和代码中的程序部分称为客户端。

早期 OpenGL 使用立即渲染模式，即固定管线。GL 3.2 后，默认使用核心渲染模式，即使用可编程管线。

至今 OpenGL 已经有点过时了，但由于兼容性强仍然有用

OpenGL 本体可以看做是一个巨大的状态机，整个 OpenGL 的状态称为 OpenGL 上下文。

> 通常来说，OpenGL 只作为渲染工具，配套还有其他相关库辅助使用
>
> * [GLFW](https://www.glfw.org)：创建窗口并初始化 OpenGL 上下文
> * [GLAD](https://glad.dav1d.de)：检查和使用扩展
> * [GLM](https://github.com/g-truc/glm)：OpenGL Mathematics，OpenGL 的数学（尤其是矩阵）库
> * [Assimp](https://learnopengl-cn.github.io/03%20Model%20Loading/01%20Assimp/)：模型加载库

Java 下可直接使用 [LWJGL](https://www.lwjgl.org/customize) 自身包含 GLAD 的功能，并提供 GLFW 扩展

- [ ] ⏬ 间接渲染
# 参考

```cardlink
url: https://learnopengl-cn.github.io/
title: "主页 - LearnOpenGL CN"
description: "http://learnopengl.com 系列教程的简体中文翻译"
host: learnopengl-cn.github.io
```

‍
