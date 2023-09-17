由 6 张正方形纹理组成，分别表示立方体的 6 个面，在着色器中可使用一个 vec3 而不是两个 vec2 进行采样。
![[Pasted image 20230912195955.png]]
立方体贴图可以从 [Custom Map Makers - Index page](http://www.custommapmakers.org/) 下载，类似这样
![[Pasted image 20230912200120.png]]
立方体贴图使用 `GL_TEXTURE_CUBE_MAP` 作为纹理的目标，实际材质提交到 `GL_TEXTURE_CUBE_MAP_[POSITIVE/NEGATIVE]_[X/Y/Z]`，GLSL 中采样器使用 `samplerCube`

```c++
GLuint cube;
glGenTextures(1, &cube);

glBindTexture(GL_TEXTURE_CUBE_MAP, cube);
// GL_TEXTURE_CUBE_MAP_POSITIVE_X 到 GL_TEXTURE_CUBE_MAP_NEGATIVE_Z 实际值依次+1
glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X, ...); // 右
glTexImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_X, ...); // 左
glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Y, ...); // 上
glTexImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, ...); // 下
glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Z, ...); // 后
glTexImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, ...); // 前
```
# 天空盒

一个占据全屏的盒子，玩家位于盒中，盒子作为场景背景
- 天空盒不随摄像机移动，可以实现广阔的视觉效果
	- 将 ViewMatrix 从 4x4 降到 3x3 后再扩展到 4x4，移除位移部分的变换
- 最后渲染天空盒，充分利用提前深度测试，丢弃不可见部分
- 天空盒 z 与 w 坐标相同，因此其深度值永远为 1，深度测试总是失败

顶点着色器

```c++
#version 460 core

layout(location = 0) in vec3 position;
// 天空盒采样坐标
out vec3 vTexCoords;

uniform mat4 projection, view;

void main() {
    vTexCoords = position;
    gl_Position = projection * view * vec4(position, 1);
    gl_Position = gl_Position.xyww; // z=w=1
}
```

片元着色器

```c++
#version 460 core

in vec3 vTexCoords;
out vec4 fColor;

uniform samplerCube tex;

void main() {
    fColor = texture(tex, vTexCoords);
}
```

代码初始化阶段

```c++
glEnable(GL_DEPTH_TEST);
```

代码渲染阶段

```c++
// 正常绘制其他部分
// draw ...

glDepthFunc(GL_LEQUAL);

// 绘制天空盒
// mat4 view = mat4(mat3(viewMatrix))
// mat4 projection = projectionMatrix
// draw ...
```
# 环境映射

使用环境立方体贴图作为反射和折射属性
![[Pasted image 20230912200348.png]]
根据观察方向向量 $\color{gray}{\bar{I}}$ 和物体的法向量 $\color{red}{\bar{N}}$，来计算反射向量 $\color{green}{\bar{R}}$。我们可以使用 GLSL 内建的 `reflect` 函数来计算这个反射向量。最终的 $\color{green}{\bar{R}}$ 向量将会作为索引/采样立方体贴图的方向向量，返回环境的颜色值。最终的结果是物体看起来反射了天空盒。
![[Pasted image 20230912201242.png]]
光线通过不同介质会产生折射，使观察到后方的图像变形的现象。

顶点着色器：

```c++
in vec3 position, normal;
out vec3 vNormal, vPosition;

uniform mat4 model, view, projection;

void main() {
    vNormal = mat3(transpose(inverse(model))) * normal;
    vPosition = vec3(model * vec4(position, 1));
    // ...
}
```

片元着色器：

```c++
in vec3 vNormal, vPosition;
out vec4 fColor;

uniform vec3 cameraPos;
uniform samplerCube skybox;

void main() {
    float ratio = 入射材质折射率ri / 出射材质折射率ro;
    vec3 I = normalize(vPosition - cameraPos);
    // 直接计算反射向量
    // 不考虑折射的情况下，直接使用 reflect(I, normalize(vNormal));
    vec3 R = reflect(I, normalize(vNormal), ratio);
    fColor = vec4(texture(skybox, R).rgb, 1);
}
```
