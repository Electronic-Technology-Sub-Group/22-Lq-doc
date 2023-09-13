OpenGL 的缓冲区实际代表一块内存，具体使用非常灵活，当且仅当绑定到特定缓冲区目标时才具有特殊意义。
# 缓冲区方法

-  `glBufferData`：分配一块内存，当 data 非 NULL 时将其中的数据作为缓冲区的初始内容
-  `glBufferSubData`：填充部分缓冲区区域，需要一个 offset 偏移量和数据数目
-  `glMapBuffer`：返回一个指向缓冲区的指针，通过程序可以直接访问缓冲区
-  `glUnmapBuffer`：解除一个缓冲区指针映射，对缓冲区的修改会在此时回写到缓冲区
-  `glCopyBufferSubData`：不通过 CPU 直接进行缓冲区数据复制
# 缓冲区类型

各种缓冲区 target 的作用如下：
- `GL_ARRAY_BUFFER`：顶点缓冲区，用于提交给顶点着色器
- `GL_ELEMENT_ARRAY_BUFFER`：顶点索引缓冲区，用于 `glDrawElements` 索引形式绘制
- `GL_DRAW_INDIRECT_BUFFER`：间接绘制缓冲区，用于间接绘制数据
- `GL_UNIFORM_BUFFER`：绑定到缓冲区 uniform 对象，对大量数据快速赋值
- `GL_TEXTURE_BUFFER`：纹理缓存，保存纹理数据（图片）
- `GL_TRANSFORM_FEEDBACK_BUFFER`：可获取着色器顶点处理阶段完成后的数据
- `GL_PIXEL_PACK_BUFFER`：读取 GL 中的纹理数据，如 `glReadPixels` 等
- `GL_PIXEL_UNPACK_BUFFER`：使 GL 读取纹理数据，作为 `glTexImage2D` 等数据源使用
- `GL_COPY_[READ/WRITE]_BUFFER`：在缓冲区之间拷贝数据，不会产生额外状态变化和其他调用

usage 可选值为 `GL_[STREAM/STATIC/DYNAMIC]_[READ/COPY/DRAW]` 共 9 种组合方法。
- STREAM：数据写入一次后几乎不再改变，也只使用几次
- STATIC：数据写入一次后几乎不再改变，但要多次使用
- DYNAMIC：数据会重复修改，多次访问

- DRAW：数据在程序端修改，并在 GL 端使用
- READ：数据在 GL 端修改，并返回给程序
- COPY：数据在 GL 端修改，并在 GL 端使用
# 具名缓冲区

OpenGL 4.5 后 OpenGL 引入使用对象名而不是绑定位点对缓冲区的操作方法：

| 新方法               | 旧方法          | 备注                                                       |
| -------------------- | --------------- | ---------------------------------------------------------- |
| glCreateBuffers      | glGenBuffers    | 旧方法仅返回未使用缓冲区名，新方法还会包含部分初始化操作 ‘ |
| glNamedBufferData    | glBufferData    | 仅用于初始化数据                                           |
| glNamedBufferSubData | glBufferSubData |                                                            |
| glMapNamedBuffer     | glMapBuffer     |                                                            |
| glUnmapNamedBuffer   | glUnmapBuffer   |                                                            |
| glNamedBufferStorage | glBufferStorage |                                                            |
