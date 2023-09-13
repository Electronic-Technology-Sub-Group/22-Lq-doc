几何着色器作用于顶点着色器和片元着色器之间，可在片元着色器之前对一个图元进行变换，在编译时对应着色器为 GL_GEOMETRY_SHADER

```c++
layout(points) in;
layout(points, max_vertices=2) out;

EmitVertex();
EndPrimitive();
```

- 除版本外，前两行 layout 包括了几何着色器的一些配置
	- `in`，`out` 分别表示输入和输出图元类型
	- points 位置表示修改的图元，与 draw 方法时传入的 mode 参数一一对应
	- max_vertices 参数表示产生图元的顶点数

| 图元类型            | 绘制模式                                            | 包含最小顶点数 |
| ------------------- | --------------------------------------------------- | -------------- |
| points              | GL_POINTS                                           | 1              |
| lines               | GL_LINES, GL_LINE_STRIP                             | 2              |
| lines_adjacency     | GL_LINES_ADJACENCY, GL_LINE_STRIP_ADJACENCY         | 4              |
| triangles           | GL_TRIANGLES, GL_TRIANGLE_FAN, GL_TRIANGLE_STRIP    | 3              |
| triangles_adjacency | GL_TRIANGLES_ADJACENCY, GL_TRIANGLE_STRIP_ADJACENCY | 6              | 

- EmitVertex() 表示提交顶点
- EndPrimitive() 表示提交图元
- 传参规则：允许从顶点着色器传入参数
	- 可通过 `in` 接受所有从顶点着色器输出的属性，但类型为对应类型的数组，表示一组顶点的所有点属性
	- 可将属性通过 `out` 输出到片元着色器中，但不需要以数组形式表示 - 在每一次 `EmitVertex`  传递一次属性值
- 内建变量 `gl_in` 是一个数组，只读，包含顶点着色器的属性

```c++
in gl_Vertex {
    vec4 gl_Position;
    float gl_PointSize;
    float gl_ClipDistance;
} gl_in[];
```

几何着色器通常用于
- 炸裂效果：片元沿法线向前移动一段距离
- 法线可视化，用于调试或毛发效果

```c++
// 获取一个三角形的法向量
vec3 a = gl_in[0].gl_Position.xyz - gl_in[1].gl_Position.xyz;
vec3 b = gl_in[2].gl_Position.xyz - gl_in[1].gl_Position.xyz;
vec norm = normalize(cross(a, b));
```

一个几何着色器的代码示例：

```c++
#include 460 core

layout(points) in;
layout(triangle_strip, max_vertices=5) out;

// 顶点着色器: out vec4 vColor;
in vec4 vColor[];
out vec4 gColor;

void main() {
    // 因为是点，gl_in，vColor 只有一个变量
    vec4 position = gl_in[0].gl_Position;
    gColor = vColor[0];

    // 左下
    gl_Position = position + vec4(-0.2, -0.2, 0, 0); EndVertex();
    // 右下
    gl_Position = position + vec4( 0.2, -0.2, 0, 0); EndVertex();
    // 左上
    gl_Position = position + vec4(-0.2,  0.2, 0, 0); EndVertex();
    // 右上
    gl_Position = position + vec4( 0.2,  0.2, 0, 0);
    // 顶部
    gl_Position = position + vec4( 0.0,  0.4, 0, 0);
    gColor = vec4(1, 1, 1, 1);
    EmitVertex();

    EndPrimitive();
}
```