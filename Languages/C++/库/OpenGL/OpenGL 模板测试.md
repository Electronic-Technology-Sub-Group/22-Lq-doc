模板测试位于片元着色器执行之后，深度测试之前。

> [!note]- 模板测试
> 将片段与模板缓冲中的数据进行比较，测试未通过则片段被丢弃

> [!note]- 模板缓冲
> 存储模板值，通常为8位，由 GLFW 创建窗口时对当前窗口创建

类似深度测试，默认情况下模板缓冲也是关闭的，需要通过 `glEnable(GL_STENCIL_TEST)` 开启测试，并在每次渲染时通过 `glClear(GL_STENCIL_BUFFER_BIT)` 清空模板缓冲区数据。
# 模板 API

- 启用或关闭测试，默认关闭

```c++
// 启用深度测试
glEnable(GL_STENCIL_TEST);
// 关闭深度测试
glDisable(GL_STENCIL_TEST);
```

- 清空深度缓冲区，每次绘制前调用，否则绘制结果会受上一次渲染影响

```c++
glClear(GL_STENCIL_BUFFER_BIT);
```

- 设置位掩码。之后写入模板缓冲的值将与该值进行 `&` 位运算，默认为 `0xFF`
	- `glClear` 也受位掩码影响

```c++
glStencilMask(0xFF);
```

- 设置模板测试方法
	- func：模板值与 ref 值进行测试的方法，与[[OpenGL 深度测试]]中 `glDepthFunc` 的值相同
	- ref：模板参考值，参与模板测试方法进行比较
	- mask：掩码，模板值在与 ref 值进行比较之前，先与该值进行 `&` 运算

```c++
glStencilFunc(func, ref, mask);
```

- 设置模板测试行为
	- sfail：模板测试时失败的行为
	- dpfail：模板测试通过，深度测试失败时的行为
	- dppass：模板测试通过，深度测试也通过时的行为

```c++
glStencilOp(sfail, dpfail, dppass);
```

| 测试行为                  | 说明                                                    |
| ------------------------- | ------------------------------------------------------- |
| GL_KEEP/GL_INVERT         | 保留原值或按位取反                                      |
| GL_ZERO/GL_REPLACE        | 将模板值替换为 0 或 ref                                 |
| GL_INCR/GL_DECR           | 将模板值 +1 或 -1，不会超过最大或最小值                 |
| GL_INCR_WRAP/GL_DECR_WRAP | 将模板值 +1 或 -1，超过则归零或置为最大值（无符号溢出） |
# 使用模板

使用模板的过程通常为：
1. 将模板的测试方法设置为 `GL_ALWAYS`：`glStencilFunc(GL_ALWAYS, 1, 0xFF)`
2. 渲染物体，更新模板缓冲区（绘制模板同时也会绘制到屏幕）
3. 设置测试方法和行为同时更新屏幕

绘制时，当且仅当模板缓冲区测试通过时才会被绘制到屏幕。

初始化过程：
```c++
glEnable(GL_DEPTH_TEST);
glEnable(GL_STENCIL_TEST);
```

渲染过程，以渲染物品边框为例：
```c++
// 模板缓冲区置 0
glStencilFunc(GL_NOTEQUAL, 1, 0xFF);
glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);
glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);

glStencilMask(0x00);
// 渲染与模板测试无关的图形，也不会写入模板缓冲区
// do draw...

glStencilFunc(GL_ALWAYS, 0x01, 0xFF);
glStencilMask(0xFF);
// 渲染，此处图形被渲染内容同时被写入模板中（写入为 0x01）
// do draw...

glStencilFunc(GL_NOT_EQUIAL, 0x01, 0xFF);
glStencilMask(0x00);
glDisable(GL_DEPTH_TEST); // 关闭深度测试
// 渲染，此处渲染边框
//     渲染区域比前面的物品略大（大出边框宽度）
//     渲染时上面渲染过的纹理位置将会被丢弃
// do draw...

// 恢复环境
glStencilFunc(GL_ALWAYS, 0x00, 0xFF);
glStencilMask(0xFF);
glEnable(GL_DEPTH_TEST);
```
