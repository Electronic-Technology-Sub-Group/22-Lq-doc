# 基本概念

OpenGL 中代码绘制在一个 3D 空间中完成。从 3D 坐标转换为 2D 坐标（若需要）则由**图形渲染管线**完成。

- 图形渲染管线：简称管线，指一组原始图型**数据**通过一系列各种变化后，最终出现在屏幕上的过程。
- 着色器：管线中每个阶段都是接收上一个阶段的输出作为输入，并将结果发送给下一个阶段。这些阶段在 GPU 中进行。负责某一阶段的具体运算，运行在 GPU 上的程序称为着色器 shader
- 图元：OpenGL 可绘制的基本类型，分为点、线、三角形三种。

从 CPU 向 GPU 传递的用于绘制的初始数据称为顶点数据，这些数据经过多个管线最终输出到屏幕。具体过程如下（加粗表示必有的着色器，斜体表示由 OpenGL 完成无法通过着色器程序控制）

![[Pasted image 20230909050840.png]]

1. **顶点着色器** 将传入的顶点数据转换为正确的 NDC 顶点坐标，通常利用视图变换矩阵完成
2. *图元装配* 将顶点坐标组成指定图元（点、线、三角形等）
3. 几何着色器 根据图元顶点数据，尝试生成新图元
4. *光栅化*
	1. 将图元数据映射到最终屏幕的像素，生成片元
	2. 剪裁 Clipping：将屏幕之外无法显示的像素丢弃
5. **片元着色器** 计算所有像素颜色，通常包括光源、阴影、光色等参数
6. *测试、混合*
	1. 根据深度值判断各物体前后顺序，丢弃部分像素
	2. 根据模板丢弃部分遮挡像素
	3. 根据 Alpha 通道透明度进行混合
# 顶点输入

- 标准化设备坐标 NDC：Normalized Device Coordinates，标准化坐标，屏幕可显示范围的 XYZ 坐标范围为 $[-1, 1]$，超出该范围表示窗口范围之外

![[Pasted image 20230909051140.png]]
- 视口变换：通过 glViewport 指定刷新的屏幕范围，绘制时仅绘制指定区域，默认为整个屏幕
	- 在调整窗口大小时应当更新为新的窗口大小
	- 也可用于局部更新
- 缓冲区：一系列 GPU 上的内存（即显存）空间。OpenGL 允许一次性将大量数据提交到 GPU，再由着色器处理。OpenGL 有多种着色器类型，对应不同用途。

顶点输入实际就是将顶点坐标传递到 `GL_ARRAY_BUFFER` 缓冲区中。
1. 准备顶点数据
2. 申请缓冲区对象 BufferObject，表现在代码中为一个 int 值，称为顶点缓冲区对象 VBO，VertexBufferObject
3. 绑定缓冲区对象，OpenGL 在此时根据缓冲区类型初始化缓冲区
4. 初始化、提交数据

```c++
// 准备顶点数据
// 这里有 3 个顶点共 9 个 float 类型数字
float vertices[] {
    -0.5f, -0.5f, 0f,
     0.5f, -0.5f, 0f,
       0f,  0.5f, 0f
};

// 申请缓冲区
GLuint vbo;
glGenBuffers(1, &vbo);
// 绑定缓冲区
glBindBuffer(GL_ARRAY_BUFFER, vbo);
// 初始化数据
glBufferData(GL_ARRAY_BUFFER, sizeof vertices, vertices, GL_STATIC_DRAW);
```

`glGenBuffers` 用于得到缓冲区对象的可用名称，可以一次返回多个缓冲区（`count` 个，存储至 `buffers` 数组中）。每个缓冲区对象名都是一个非 0 无符号整数，若申请失败则对应缓冲区值为 0。

```c++
glGenBuffer(GLsizei count, GLuint* buffers);
```

`glGenBuffers` 仅返回未使用缓冲区对象名称，不会创建新缓冲区。因此如果你确认一个 GLuint 值未曾作为缓冲区对象使用，可以不调用该方法直接绑定。

```c++
glBindBuffer(GLenum target, GLuint buffer);
```

`glBindBuffer` 用于绑定当前缓冲区（`buffer`）到一个类型槽（`target`）并激活缓冲区。
- 若第一次绑定缓冲区，创建对应缓冲区对象，并根据 target 初始化对象
- 将对应 target 绑定到该缓冲区对象名称
- 若给定缓冲区对象名称为 0，表示将与该 target 绑定的任何对象解绑

`glBufferData` 方法用于初始化数据。该方法会先初始化缓冲区大小（`size`，单位字节），然后将 `data` 中的值发送给缓冲区。若仅初始化大小不需要初值，`data` 使用 null。

```c++
glBufferData(GLenum target, GLsizeptr size, const GLvoid *data, GLenum usage);
```

`usage` 变量用于在是定数据分配后的读写方式，使用合适的读写方式会提高读写效率，常用 `GL_STATIC_DRAW` 或 `GL_DYNAMIC_DRAW`，表示数据几乎不会变化或会多次变化，且在 GL 端读取。

缓冲区使用十分灵活，详见 [[OpenGL 缓冲区]]。
# 顶点着色器

顶点着色器接收一组顶点数据，并根据该组数据生成一个基于 NDC 的顶点坐标。顶点着色器会被多次调用，无法访问到完整的顶点数据（详见 [[#链接顶点属性]]）

```c++
#version 460 core

layout (location = 0) in vec3 pos;

void main() {
    gl_Position = vec4(pos, 1);
}
```

OpenGL 着色器是运行于 GPU 的程序，其语法类似于 C 语言，但也有一些区别
- `main` 函数允许返回 `void` 类型
- `#version` 表示着色器使用的 OpenGL 版本。`core` 表示工作于核心模式
- `layout` 表示布局，`location` 表示变量位置，在[[#链接顶点属性]]中会用到
- `in` 表示数据是从输入数据，`out` 表示输出数据
- `vec3` 表示输入的顶点数据类型。内置类型除了基本数据类型还有 `vec3`，`vec4`，`mat3x3`，`mat4x4` 等表示向量和矩阵
- `gl_Position`：一个内置变量，存储最终的一个顶点坐标，接收一个 `vec4` 类型变量

具体详见 [[OpenGL GLSL]]
# 片元着色器

```c++
#version 460 core

out vec4 FragColor;

void main() {
    // vec 表示颜色时，每个值范围为 [0, 1] 表示 RGBA 的 0-255
    FragColor = vec4(1.0f, 0.5f, 0.5f, 1.0f);
}
```

与顶点着色器不同的是，片元着色器要求一个输出而不是内置变量表示该顶点的颜色。变量名可以是任意名称，但类型必须是 `vec4`
# 着色器程序

一个着色器程序表示一组着色器，着色器是一段运行于 GPU 的程序，完成渲染操作。
## 编译着色器

编译着色器之前需要着色器源代码（字符串）。设着色器源码为 `const char* shaderSource`
1. 提交着色器源码
2. 编译着色器
3. 检查状态，异常处理

```c++
// shader 即着色器对象
const GLuint shader = glCreateShader(着色器类型);
// 提交源码并编译
glShaderSource(shader, 1, &shaderSource, nullptr);
glCompileShader(shader);
// 检查编译是否成功
GLint status, info_len;
glGetShaderiv(shader, GL_COMPILE_STATUS, &status);
if (status != GL_TRUE)
{
	glGetShaderiv(shader, GL_INFO_LOG_LENGTH, &info_len);
	const auto info = new GLchar[info_len + 1];
	glGetShaderInfoLog(shader, info_len, &info_len, info);
	// 编译错误，进行异常处理，异常信息为 info
	cerr << info << endl;
	// 清理环境
	delete[] info;
	glDeleteShader(shader);
}
```

编译着色器之前需要使用 `glCreateShader` 创建一个着色器程序，各有各自的类型
- `GL_VERTEX_SHADER` 为顶点着色器
- `GL_FRAGMENT_SHADER` 为片元着色器

使用 `glShaderSource` 提交着色器源码，允许同时提交多个着色器。

```c++
void glShaderSource(GLuint shader, GLsizei count, const GLchar **string, const GLint *length);
```

- shader 表示绑定到的着色器对象
- count 表示提交的着色器源码个数
- string 表示所有提交的着色器源码数组
- length 表示所有提交的着色器源码长度数组。当使用 `\0` 结尾的字符串时可为 null

提交后通过 `glCompileShader` 编译，使用 `glGetShaderiv` 查询编译结果。
## 着色器程序

着色器程序对象 Shader Program Object 表示一个着色器程序。着色器程序是一组不同类型着色器组合后的对象，在一次绘制时同时应用这些着色器。

```c++
const GLuint program = glCreateProgram();
// 绑定着色器对象并链接
glAttachShader(program, shaderVertex);
glAttachShader(program, shaderFragment);
glLinkProgram(program);
// 检查链接结果
glGetProgramiv(program, GL_LINK_STATUS, &status);
if (status != GL_TRUE)
{
	glGetProgramiv(program, GL_INFO_LOG_LENGTH, &info_len);
	const auto info = new GLchar[info_len + 1];
	glGetProgramInfoLog(program, info_len, &info_len, info);
	// 异常处理，异常信息为 info
	cerr << info << endl;
	// 清理环境
	delete[] info;
	glDetachShader(program, shaderVertex);
	glDetachShader(program, shaderFragment);
	glDeleteProgram(program);
}
// 链接过后，若不需要绑定其他着色器程序，着色器对象可以删掉了
glDetachShader(program, shaderVertex);
glDetachShader(program, shaderFragment);
glDeleteShader(shaderVertex);
glDeleteShader(shaderFragment);
```
# 顶点数组对象

现在我们已经有了顶点数据、顶点缓冲区和着色器程序，下一步就是需要将他们组合起来。

顶点数据已经在顶点缓冲区初始化时候作为默认数据传入了，现在顶点缓冲区中包含了 9 个 float 类型值。

已知一个顶点着色器处理一组顶点数据，我们下一步需要的是将所有顶点数据划分为多组（本例为三组），每次传递一组数据（3 个 float 值）。

OpenGL 管理顶点着色器程序接收数据的接口称为顶点数组对象 VAO（VertexArrayObject）。当一次渲染大量图形时（因为可能涉及反复切换多个着色器程序或多种映射类型）反复修改顶点属性很麻烦，可以将这些接口配置绑定到一个对象上，包括：
- 顶点通道开关 `glEnableVertexAttribArray`、`glEnableVertexAttribArray`
- 关联顶点缓冲区 `glBindBuffer`
- 顶点缓冲区与顶点着色器关联方法 `glVertexAttribPointer`

```c++
// 创建 VAO 对象
glGenVertexArrays(1, &vao);
// 绑定 VAO 对象以配置
glBindVertexArray(vao);
// 配置 VAO
glBindBuffer(GL_ARRAY_BUFFER, vbo);
glEnableVertexAttribArray(0);
glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, nullptr);
```

`glEnableVertexAttribArray` 表示开启一个顶点通道，`glEnableVertexAttribArray` 关闭顶点通道。一个顶点通道即顶点着色器中 `in` 类型变量 `layout` 中 `location` 属性的值，如果没有手动指定可以通过 `glGetAttribLocation` 根据变量名查找。

> [!seealso]
> GL 4.5 之后，允许不进行绑定后进行开关配置，需要传入对应 vao 名
> 
> | 新方法                     | 旧方法                     |
> | -------------------------- | -------------------------- |
> | glGenVertexArrays          | glCreateVertexArrays       | 
> | glEnableVertexArrayAttrib  | glEnableVertexAttribArray  |
> | glDisableVertexArrayAttrib | glDisableVertexAttribArray |
> 

`glVertexAttribPointer` 定义了顶点缓冲区中的数据如何传入顶点着色器，一个 `in` 类型属性对应一次 `glVertexAttribPointer` 方法调用

```c++

void glVertexAttribPointer(GLuint index,
	                       GLint size,
	                       GLenum type,
	                       GLboolean normalized,
	                       GLsizei stride,
	                       const GLvoid * pointer);
```
- index：顶点着色器 `in` 属性 `layout` 中 `location` 属性值，或通过 `glGetAttribLocation` 获取
- size：一个属性包含的值个数，如 `vec3` 中包含了 3 个 float 值
- type：每个值的类型，实例中 float 类型对应 `GL_FLOAT`
- normalized：是否归一化（标准化），即将数据转换为 $[0, 1]$ 或 $[-1, 1]$ 范围内
- stride：步长，两组数据之间的间隔，单位字节
- pointer：在缓冲区中的偏移量，单位字节

stride 和 pointer 在不同组合的情况下有不同作用

> [!example]
> 顶点着色器：
> layout (location = 0) in vec3 pos;
> 
> 顶点数组配置：
> glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, nullptr)
> 
> ![[Pasted image 20230909145745.png]]
> 

> [!example]
> 顶点着色器：
> `layout (location = 0) in vec3 pos;`
> `layout (location = 1) in vec3 color;`
> 
> 顶点数组配置：
> `glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * sizeof(float), nullptr);`
> `glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * sizeof(float), (void*) (3 * sizeof(float)));`
> 
> ![[Pasted image 20230909153441.png]]

> [!example]
> 顶点着色器：
> `layout (location = 0) in vec3 pos;`
> `layout (location = 1) in vec3 color;`
> 
> 顶点数组配置：
> `glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, nullptr);`
> `glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, (void*) (9 * sizeof(float)));`
> 
> ![[Pasted image 20230909154900.png]]
# 绘制三角形

![[Pasted image 20230909161607.png]]

目前为止，我们已经完成了所有准备
- 顶点缓冲区中存储了用于绘制的顶点信息
- 链接了顶点着色器用于解析顶点、片元着色器用于设置颜色的着色器程序
- 连接顶点缓冲区和顶点着色器的接口 - 顶点数组对象
我们只需要开启各个对象后，调用 `glDrawArray` 即可绘制

![[Pasted image 20230909160343.png]]

```c++
// 使用着色器程序
glUseProgram(program);
// 使用顶点数组对象 - 顶点缓冲区也在此配置中
glBindVertexArray(vao);
// 绘制指令：三角形
glDrawArrays(GL_TRIANGLES, 0, 3);
```

其中，`glDrawArrays` 表示直接绘制顶点数据

```c++
void glDrawArrays(GLenum mode, GLint first, GLsizei count);
```

- mode 表示绘制的图形类型，这里我们绘制的是三角形
- first 表示第一个绘制的顶点
- count 表示绘制的顶点总数
# 线框模式

使用 `glPoloygonMode` 可以设置线框模式。其第一个参数必须是 `GL_FRONT_AND_BACK`
- GL_FILL：填充，默认
- GL_LINE：仅线框，线宽可使用 `glLineWidth` 设置
![[Pasted image 20230909161914.png]]
- GL_POINT：仅顶点，大小可以通过 `glPointSize` 设置
![[Pasted image 20230909162052.png]]
# 绘制矩形

![[Pasted image 20230909165346.png]]

OpenGL 没有矩形图元，当我们绘制一个矩形时，我们只需要绘制两个三角形。OpenGL  默认以逆时针作为封闭图形正方向，因此我们可以使用以下的定点数组：
![[Pasted image 20230909163321.png]]

```c++
float vertices[] {
    -0.5f,  0.5f, 0,  // p0
    -0.5f, -0.5f, 0,  // p1
     0.5f,  0.5f, 0,  // p2
     0.5f,  0.5f, 0,  // p2
    -0.5f, -0.5f, 0,  // p1
     0.5f, -0.5f, 0,  // p3
};
```

如果我们以 `glDrawArray` 的绘制方法，需要绘制 6 个顶点组成两个三角形。但其中有很多重复的点 - p1，p2 点都需要重复传入两次。

为避免重复录入造成资源浪费，可以使用 `glDrawElements` 方式绘制。该方式可以将绘制的顶点顺序与顶点录入顺序分离，利用一组额外的缓冲区数据表示渲染的顶点索引

```c++
// 顶点数据
float vertices[] {
    -0.5f,  0.5f, 0,  // p0
    -0.5f, -0.5f, 0,  // p1
     0.5f,  0.5f, 0,  // p2
     0.5f, -0.5f, 0,  // p3
};
// 顶点索引数据
// 绘制顺序为第 0 1 2 2 1 3 共 6 个顶点
GLuint indices[] { 0, 1, 2, 2, 1, 3 };
```

新的索引数据需要绑定到 target 为 `GL_ELEMENT_ARRAY_BUFFER` 的缓冲区上，称为元素缓冲区对象 EBO（Element Buffer Object）或索引缓冲区对象 IBO（Index Buffer Object）

```c++
GLuint ebo;
glGenBuffers(1, &ebo);
glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
glBufferData(GL_ELEMENT_ARRAY_BUFFER, sizeof indices, indices, GL_STATIC_DRAW);

// 绘制
glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, nullptr);
```

之后使用 `glDrawElements` 绘制即可。

```c++
void glDrawElements(GLenum mode, GLsizei count, GLenum type, const GLvoid * indices);
```
- mode：渲染模式
- count：渲染顶点总数
- type：元素缓冲区中数据类型
- indices：当使用 `GL_ELEMENT_ARRAY_BUFFER` 缓冲区时为 `nullptr`，否则为顶点数组
# 资源释放

为防止内存泄漏，创建的缓冲区对象、顶点数组对象、着色器对象和着色器程序对象等都应该在不用的时候及时释放。

- 缓冲区：设缓冲区类型为 target，缓冲区名为 buffer
```c++
// 1. 解除绑定
glBindBuffer(target, 0);
// 2. 删除缓冲区
glDeleteBuffer(buffer);
```

- 顶点数组对象：设顶点数组对象名为 vao
```c++
// 1. 解除所有顶点数组对象绑定
glBindVertexArray(0);
// 2. 删除 VAO
glDeleteVertexArrays(1, &vao);
```

- 着色器程序：包括着色器对象和着色器程序对象
```c++
// 删除着色器
glDetachShader(program, shader);
glDeleteShader(shader);

// 删除着色器程序
glUseProgram(0);
glDeleteProgram(program);
```
