# 内建变量

OpenGL 在着色器中有一些内建变量，完整版详见 [Built-in Variable (GLSL) - OpenGL Wiki (khronos.org)](https://www.khronos.org/opengl/wiki/Built-in_Variable_(GLSL))、[[Built-in Variable (GLSL)]]
- 顶点着色器
	- `gl_Position`：顶点着色器输出，记录顶点坐标
	- `gl_PointSize`：点作为图元时绘制的大小，当且仅当 `glEnable(GL_PROGRAM_POINT_SIZE)` 时可写
	- `gl_VertexID`：只读，渲染索引（；）
		- 使用 `glDrawElements` 系列方法进行渲染时表示该顶点对应的索引
		- 使用 `glDrawArrays` 系列方法进行渲染时表示已处理定点数量
- 片元着色器
	- `gl_FragCoord`：只读，顶点坐标
		- x、y 表示窗口坐标（`glViewport` 设置）
		- z 表示深度值
	- `gl_FrontFacing`：只读，bool 类型，该面是否为正面。
		- 当开启面剔除时无意义，因为背面已经剔除过
	- `gl_FragDepth`：深度值
		- OpenGL 4.2 之前的版本只要修改了此值，提前深度测试便会禁用，GL42 后可配置

```c++
layout(depth [condition]) out float gl_FragDepth;
```
- `[condition]` 为 `any` 时，一旦修改，关闭所有提前深度测试
- `[condition]` 为 `greater` 时，深度值比当前值大时不会关闭提前测试
- `[condition]` 为 `less` 时，深度值比当前值小时不会关闭提前测试
- `[condition]` 为 `unchanged` 时，深度值只读
# 接口块

用于两个着色器之间传递复杂数据，类似 struct
- 仍使用 in 或 out 声明
- 接口块仅要求块名相同即可互相匹配，实例名可互不相同
- 实例名可以不存在，可用于解决命名冲突
![[Pasted image 20230912204548.png]]

```c++
out VS_OUT {
    vec2 TexCoords;
} vs_out;
```

```c++
in VS_OUT {
    vec2 TexCoords;
} fs_in;
```
# uniform 缓冲对象

一种特殊缓冲区对象，绑定后为着色器提供 Uniform 块的值

- 块布局
	- `binding`：OpenGL4.2 后可用，显示指定一个绑定点
	- 布局规则：内存布局规则
		- 默认：共享布局
		- `packed`：紧凑布局，具体布局依赖于硬件实现
		- `std140`
			- 标量 int，bool，float 基准对齐量为 4 字节
			- 向量基准对齐量为 8 或 16 字节，矩阵以列向量形式存储
			- 数组内每个元素的基准对齐量为 16
			- 结构体根据规则计算，并将结果扩展到 16 的倍数

```c++
layout (std140, binding=2) uniform ExampleBlock {
                     //基本对齐量   对齐偏移量
    float value;     //        4            0
    vec3  vector;    //       16           16
    mat4  matrix;    //     16x4  32 48 64 80
    float values[3]; //     16x3   96 112 128
    bool  boolean;   //        4          144
    int   integer;   //        4          148
}
```

- Uniform 缓冲区对象：在代码中访问 Uniform 块

![[Pasted image 20230912211648.png]]

```c++
// 创建和初始化缓冲区
GLuint ubo;
glGenBuffers(1, &ubo);
glBindBuffer(GL_UNIFORM_BUFFER, ubo);
glBufferData(GL_UNIFORM_BUFFER, ...);
glBindBuffer(GL_UNIFORM_BUFFER, 0);
// 绑定 Uniform 缓冲
GLuint index = glGetUniformBlockIndex(program, "ExampleBlock");
glUniformBlockBinding(program, index, 2);
glBindBufferBase(GL_UNIFORM_BUFFER, 2, ubo);
```

之后，直接操作缓冲区即可
