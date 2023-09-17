 OpenGL 着色器语言 GLSL（OpenGL Shader Language），类似 C 语言，额外提供针对向量和矩阵的操作。
# 程序结构

```c++
#version 460 core

layout (location = 0) in vec3 pos;

void main() {
    gl_Position = vec4(pos, 1);
}
```

- 版本声明：使用 `#version` 声明的版本，应与程序使用的 OpenGL 版本相匹配
- 配置变量：所有变量可以使用 `in`、`out`、`uniform` 进行修饰
	- `in`：输入变量，顶点着色器的 `in` 变量来自代码通过 VAO 传入，其他着色器来自前一个着色器的同名同类型 `out` 类型变量
		- `in` 未使用 `location` 指定位置，可通过两种方式获取：
			- `glGetAttribLocation(变量名)`
			- 着色器编译时，使用 `glBindAttribLocation` 绑定，相当用指定 location
		- 设备支持的最大 `in` 类型变量个数由 `glGetInteger(GL_MAX_VERTEX_ATTRIBS)` 获取，但至少支持 16 个包含 4 个分量的属性
	- `out`：输出变量，输出到下一阶段，由下一阶段的 `in` 变量接收，片元着色器输出到颜色空间，支持多个输出
	- `uniform`：共享数据，由程序传入，但对着色器只读。不同着色器同名同类型的 `uniform` 变量值相同
		1. 激活对应着色器程序（`glUseProgram`）
		2. 获取 Uniform 变量位置（location 属性，或 `glGetUniformLocation`）
		3. 使用 `glUniformXxx` 赋值，其中 Xxx 为类型
# 数据类型

GLSL 支持各种基本类型、结构体，并额外原生支持向量和矩阵
- 基本类型：`int`、`uint`、`float`、`double`、`bool`
- 结构体，详见 [[OpenGL 高级 GLSL]]
- 向量 `[T]vec[N]`，矩阵 `[T]mat[N]x[M]`
	- T：内部元素类型，省略表示 float，b 为 bool，i 为 int，u 为 uint，d 为 double
	- N、M：元素个数，取值范围 $[1, 4]$
- 采样器：`sampler2D`、`sampler3D`，详见 [[OpenGL 纹理]]

向量各分量可以通过 `rgba`、`xyzw` 或 `stpq` 访问，但不能混合使用

```c++
vec4 v4 = vec4(1, 2, 3, 4);
vec3 v3rgb = v4.rgb;
vec3 v3xyz = v4.xyz;
bool b1 = v3rgb == v3xyz; // true
vec3 v3rba = v4.rba; // 正确
vec3 v3 = rgx; // 编译错误
```

通过向量的构造，可以将各向量互相组合

```c++
vec3 color = vec3(1, 0.5f, 1);
vec4 colorRGBA = vec4(color, 1); // 1 0.5 1 1
```

矩阵也可以拆分为向量，或者由向量通过构造函数创建。
