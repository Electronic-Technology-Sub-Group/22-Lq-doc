2D 绘制时，不需要考虑远近问题，z 轴可以只使用 0。而进入 3D，就要考虑 z 坐标问题了。
# 模型空间

OpenGL 的坐标系是一个典型的右手系，z 轴垂直于屏幕向外。
![[Pasted image 20230911013627.png]]
从绘制到 NDC 坐标之间通常会经历几个过渡坐标系变化，以方便 3D 物体建模和计算。
![[Pasted image 20230910121012.png]]
- 局部空间：建模时使用的空间坐标，一般尽可能方便模型建模
- 世界空间：通过模型矩阵 ModelMatrix 对模型进行缩放、平移、旋转后，将模型放在场景世界中，这个容纳了场景中所有模型的空间称为世界空间
- 观察空间：通过观察矩阵 ViewMatrix 将世界空间整体平移、旋转，变换成用户视野（或摄像机视野）的坐标空间
- 裁剪空间：通过投影矩阵 ProjectionMatrix 将观察空间的坐标统一映射成 NDC 坐标空间。这个过程称为投影
- 屏幕空间：将 NDC 坐标系转换到 `glViewport` 指定的绘制空间，该步骤由 OpenGL 通过视口变换自动完成。

最终用于坐标转换的矩阵自右向左运算，最右侧为局部空间的坐标，用于顶点着色器中计算。
$$
V_{clip} = M_{projection} \cdot M_{view} \cdot M_{model} \cdot V_{local}
$$
> [!warning]
> OpenGL 默认以调用 `draw` 方法的顺序绘制，最终输出时不会按 z 轴删除遮挡像素。可以通过深度测试开启剪裁：`glEnable(GL_DEPTH_TEST)`
> 
> 开启后，每次绘制前需要清空深度缓冲区：`glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)`
> 
> 更多深度测试相关详见 [[OpenGL 深度测试]]
# 投影

为了将顶点坐标从观察变换到裁剪空间，我们需要定义一个投影矩阵(Projection Matrix)，它指定了一个范围的坐标，比如在每个维度上的-1000到1000。投影矩阵接着会将在这个指定的范围内的坐标变换为标准化设备坐标的范围(-1.0, 1.0)。所有在范围外的坐标不会被映射到在-1.0到1.0的范围之间，所以会被裁剪掉。在上面这个投影矩阵所指定的范围内，坐标(1250, 500, 750)将是不可见的，这是由于它的x坐标超出了范围，它被转化为一个大于1.0的标准化设备坐标，所以被裁剪掉了。

+ 平截头体：由投影矩阵创建的观察箱，通常使用正射投影或透视投影
+ 透视除法：Perspective Division，每次顶点着色器执行后自动进行，将一个 vec4 类型的齐次坐标 xyz 分量同时除以 z 分量，转换为三维坐标系 

投影矩阵可以通过 GLM 库创建。
## 正射投影

![[Pasted image 20230911002743.png]]

正射投影 Orthographic Projection 的平截头体是立方体，立方体之外的顶点都会被裁剪掉，由宽、高、近平面、远平面四个值指定。

```c++
glm::ortho<float>(left, right, bottom, top, zNear, zFar);
```

正射投影可以直接将平截头体内的顶点坐标映射为标准化坐标，因为 w 分量没有改变。

正交投影直接将坐标投影到 2D 平面，实际上并不很真实，因为没有考虑透视，但可以反映真实的长度比例，常用于 2D、建筑、工程学绘图。
## 透视投影

透视投影 Perspective Projection 将给定的平截头体范围映射到裁剪空间，除此之外还修改了每个顶点坐标的w值，从而使得离观察者越远的顶点坐标w分量越大。最终顶点坐标的每个分量都会除以它的w分量，距离观察者越远顶点坐标就会越小。
![[Pasted image 20230911012705.png]]
```c++
glm::perspective<float>(fov, aspect, near, far);
```

`fov` 表示视野（Field of View），即观察空间大小，参数使用弧度，可以使用 `glm::radians` 转换。45° 通常能表示比较真实的效果，而末日风格会更大一点。一般不会超过 60°。

`aspect` 为宽高比，即实际窗口大小 `width / heigth` 值

`near`，`far` 表示近平面、远平面，通常为 0.1 和 100
# 摄像机

摄像机并非 OpenGL 概念，而是一个虚拟的观察视角，实现时也是将场景中的所有物体向反方向移动以模拟摄像机的移动。

![[Pasted image 20230911020252.png]]
## 属性

摄像机通常有以下几个参数：
- 摄像机位置 `cameraPos`
- 摄像机方向
	- `cameraTarget`：摄像机所注视的点坐标
	- `cameraDirection`：摄像机视线，实际是摄像机方向的反方向，作为 z 轴使用

```c++
vec3 cameraPos;
vec3 cameraTarget;

vec3 cameraDirection = normalize(cameraPos - cameraTarget);
```

- 摄像机其他几个坐标轴。
	1. `cameraRight`：X 轴。找一个 YOZ 平面中的其他向量与 `cameraDirection` 做叉乘，归一化
	2. `cameraUp`：Y 轴。将 `cameraRight` 与 `cameraDirection` 做叉乘

```c++
vec3 cameraDirection;

vec3 up; // 找一个 YOZ 平面中的其他向量
vec3 cameraRight = normalize(cross(up, cameraDirection));
vec3 cameraUp = cross(cameraDirection, cameraRight);
}
```
## LookAt 矩阵

摄像机对应的是 3D 渲染中的观察空间，生成观察矩阵 View Matrix，即 LookAt 矩阵。

设 R 为 `cameraRight`，U 为 `cameraUp`，D 为 `cameraDirection`，P 为 `cameraPos`，则矩阵为：
$$
LookAt = \begin{bmatrix} \color{red}{R_x} & \color{red}{R_y} & \color{red}{R_z} & 0 \\ \color{green}{U_x} & \color{green}{U_y} & \color{green}{U_z} & 0 \\ \color{blue}{D_x} & \color{blue}{D_y} & \color{blue}{D_z} & 0 \\ 0 & 0 & 0  & 1 \end{bmatrix} * \begin{bmatrix} 1 & 0 & 0 & -\color{purple}{P_x} \\ 0 & 1 & 0 & -\color{purple}{P_y} \\ 0 & 0 & 1 & -\color{purple}{P_z} \\ 0 & 0 & 0  & 1 \end{bmatrix}
$$
注意 OpenGL 矩阵为按列存储，提交 uniform 值时按列提交
## 自由移动

- 位置移动：利用 `cameraFront` 替换 `cameraDirection` 以便计算 `cameraTarget`
	- `cameraDirection` = `cameraPos` + `cameraFront`
	- 前后移动时，直接修改 `cameraPos` 即可
	- 左右移动时，移动方向为 `cameraFront` 与 `cameraUp` 的叉乘方向

```c++
vec3 cameraPos;
vec3 cameraFront;
vec3 cameraUp;

// 移动速度
float cameraSpeed = 0.05f;
// 向上移动
cameraPos += cameraSpeed * cameraFront;
// 向下移动
cameraPos -= cameraSpeed * cameraFront;
// 向左移动
cameraPos -= normalize(cross(cameraFront, cameraUp)) * cameraSpeed;
// 向右移动
cameraPos += normalize(cross(cameraFront, cameraUp)) * cameraSpeed;

// 新的 lookAt 矩阵
view = glm::lookAt(cameraPos, cameraPos + cameraFront, cameraUp);
```

- 视角变化：通过修改 `cameraFront` 实现，同时更新 `cameraRight` 与 `cameraUp`

```c++
x = cos(glm::radians(pitch)) * cos(glm::radians(yaw));
y = sin(glm::radians(pitch));
z = cos(glm::radians(pitch)) * sin(glm::radians(yaw));
```

- 缩放：即视野，通过修改 fov 实现
