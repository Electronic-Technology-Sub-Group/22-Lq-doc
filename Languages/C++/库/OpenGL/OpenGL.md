参考资料：[主页 - LearnOpenGL CN (learnopengl-cn.github.io)](https://learnopengl-cn.github.io/)

OpenGL 是一套用于屏幕渲染的 C 库。OpenGL 只定义 API 规范，具体实现由宿主系统决定，通常由显卡驱动或操作系统实现。OpenGL 使用 C/S 架构，负责渲染的部分（通常为显卡，以下以显卡简称）称为服务端，CPU 和代码中的程序部分称为客户端。

早期，OpenGL 使用立即渲染模式，即固定管线。GL 3.2 后，默认使用核心渲染模式，即使用可编程管线。

OpenGL 本体可以看做是一个巨大的状态机，整个 OpenGL 的状态称为 OpenGL 上下文。

通常来说，OpenGL 只作为渲染工具，配套还有其他相关库辅助使用
- [GLFW](https://www.glfw.org)：创建窗口并初始化 OpenGL 上下文
- [GLAD](https://glad.dav1d.de)：检查和使用扩展
- [GLM](https://github.com/g-truc/glm)：OpenGL Mathematics，OpenGL 的数学（尤其是矩阵）库
- [Assimp](https://learnopengl-cn.github.io/03%20Model%20Loading/01%20Assimp/)：模型加载库

Java 下可直接使用 [LWJGL](https://www.lwjgl.org/customize) 自身包含 GLAD 的功能，并提供 GLFW 扩展

目录

基本绘制
- [[OpenGL 第一个窗口]]：使用 GLFW 创建窗口，绑定上下文并初始化 GLAD
- [[OpenGL 基础渲染]]：使用 `glDrawArray`、`glDrawElements` 的基本绘制流程
- [[OpenGL GLSL]]
- [[OpenGL 纹理]]：纹理就是图片材质
- [[OpenGL 变换]]
- [[OpenGL 3D坐标]]
- [[OpenGL 基础光照]]
- [[OpenGL 投光物]]
- [[OpenGL Assimp]]
- [[OpenGL 深度测试]]
- [[OpenGL 模板测试]]
- [[OpenGL 混合]]
- [[OpenGL 面剔除]]
- [[OpenGL 帧缓冲]]
- [[OpenGL 立方体贴图]]
- [[OpenGL 缓冲区]]
- [[OpenGL 高级 GLSL]]
- [[OpenGL 几何着色器]]
- [[OpenGL 高级渲染]]
- [[OpenGL 抗锯齿与多重采样]]
- [[OpenGL 高级光照模型]]
