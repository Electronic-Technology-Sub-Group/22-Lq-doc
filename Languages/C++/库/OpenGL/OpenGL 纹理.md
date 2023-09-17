纹理可以表示一张纹理图片，也可以表示任何大量数据。常见用处有：
- 纹理贴图，使用较少的顶点显示更精细的世界
- 在着色器上存储大量数据用于查询

一些概念：
- 纹理坐标：每个顶点对应在纹理上的坐标，是一个 vec2 类型数据
- 采样：使用纹理坐标获取纹理颜色
# 环绕方式

一般来说，纹理坐标各个分量应在 $[0, 1]$ 范围之内，$(0,0)$ 表示左下角，$(1,1)$ 表示右上角。

但我们实际上的纹理范围是可以超过这个范围的，超出范围的纹理坐标的采样方式称为环绕方式，通过 `glTexParameterf` 修改

```c++
void glTexParameterf(GLenum target, GLenum pname, GLfloat param);
```

- target：修改的纹理类型，这里使用 `GL_TEXTURE_2D`
- pname：纹理参数，涉及环绕的参数为 `GL_TEXTURE_WRAP_[T]`，`T` 取 S、T、R 分别代表 x, y, z 轴环绕方式
- `param`：参数值，环绕方式默认为 `GL_REPEAT`

| 环绕方式             | 说明         |
| -------------------- | ------------ |
| `GL_REPEAT`          | 重复纹理图像 |
| `GL_MIRRORED_REPEAT` | 镜像重复     |
| `GL_CLAMP_TO_EDGE`   | 边缘拉伸     |
| `GL_CLAMP_TO_BORDER` | 使用纯色     |

![[Pasted image 20230909175154.png]]

`GL_CLAMP_TO_BORDER` 使用的纯色可通过 `GL_TEXTURE_BORDER_COLOR` 属性设定

```c++
float borderColor[] = { 1.0f, 1.0f, 0.0f, 1.0f };
glTexParameterfv(GL_TEXTURE_2D, GL_TEXTURE_BORDER_COLOR, borderColor);
```
# 纹理过滤

纹理坐标是精确的 float 值，而用于采样的像素点却是一个色块，当图片放大或缩小时难免有失真。

纹理过滤允许设置如何选择纹理坐标对应的颜色，即如何采样，通过 `glTexParameteri` 设定
- `GL_TEXTURE_MIN_FILTER`：缩小时的取样方法
- `GL_TEXTURE_MAG_FILTER`：放大时的取样方法

![[Pasted image 20230909180035.png]]

可用的值有
- `GL_NEAREST`：临近过滤，默认，选择中心点最接近纹理坐标的那个像素
![[Pasted image 20230909175930.png]]

- `GL_LINEAR`：线性过滤，基于纹理坐标附近的纹理像素，计算出一个插值，近似出这些纹理像素之间的颜色，距离纹理中心坐标越近的颜色对最终的样本颜色的贡献越大
![[Pasted image 20230909175938.png]]
# 多级渐远纹理

当纹理在很远处或被缩小展示时，尤其是 3D 物体的远处，会在跨度很大的片段中进行采样，很难采样到正确值。此时使用多级渐远纹理 `Mipmap` 辅助采样

`Mipmap` 纹理分为很多级，0 级为原图，第 n+1 级的长和宽是第 n 级的 1/2。OpenGL 支持手动设置每级纹理，也可以通过 `glGenerateMipmaps` 自动生成。

![[Pasted image 20230909180619.png]]

在多级纹理采样时，不同 level 之间的采样也涉及一个采样问题。设置该采样方式的方式与[[#纹理过滤]]相同，只是取 `GL_[A]_MIPMAP_[B]` 类型的值。

| 过滤方式                  | 描述                                                                     |
| ------------------------- | ------------------------------------------------------------------------ |
| GL_NEAREST_MIPMAP_NEAREST | 使用最邻近的多级渐远纹理来匹配像素大小，并使用邻近插值进行纹理采样       | 
| GL_LINEAR_MIPMAP_NEAREST  | 使用最邻近的多级渐远纹理级别，并使用线性插值进行采样                     |
| GL_NEAREST_MIPMAP_LINEAR  | 在两个最匹配像素大小的多级渐远纹理之间进行线性插值，使用邻近插值进行采样 |
| GL_LINEAR_MIPMAP_LINEAR   | 在两个邻近的多级渐远纹理之间使用线性插值，并使用线性插值进行采样         |
# 加载纹理

从硬盘加载纹理，C++ 通常使用 STBImage 库，Java 可以直接使用 ImageIO.read 方法。STBImage 支持 jpg，png，tga，psd，gif 等常见格式
1. `STBImage.stbi_load`：加载纹理
2. `STBImage.stbi_image_free`：释放纹理

```c++
// stbImage 必须先声明 STB_IMAGE_IMPLEMENTATION
#define STB_IMAGE_IMPLEMENTATION

#include <filesystem>
#include <fstream>
#include <stb_image.h>

using std::filesystem::path;

/// p 图片路径
void load_image(const path& p)
{
	using namespace std::filesystem;
	
	// 检查路径是否合法
	if (!is_regular_file(p))
	{
		std::cerr << "File " << p.string() << " is not a regular file." << endl;
	}

    // 设置翻转，默认加载的图片在 OpenGL 坐标系下是倒过来的
	stbi_set_flip_vertically_on_load(true);
	// 加载图片
	int width, height, channels;
    unsigned char* data;
	data = stbi_load(p.string().c_str(), &width, &height, &channels, 4);
	// 使用图片 ...
	// 释放图片
	stbi_image_free(data);
	return img;
}
```

将纹理加载到内存中后，还需要将其提交给 OpenGL 对应的缓冲区中才可以被着色器程序访问
1. 创建纹理对象
2. 设置纹理属性
3. 加载纹理并将纹理数据传递给 OpenGL

```c++
// 创建并绑定纹理对象
glGenTextures(1, &texture);
glBindTexture(GL_TEXTURE_2D, texture);
// 设置纹理属性
glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
// 加载纹理
const char* filename = "纹理文件地址";
int width, height, channels;
stbi_set_flip_vertically_on_load(true);
const auto image_buffer = stbi_load(filename, &width, &height, &channels, 4);
// 提交纹理数据
glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image_buffer);
// 释放纹理文件
stbi_image_free(image_buffer);
```

其中，`glTexImage2D` 用于提交二维纹理。

```c++
void glTexImage2D(GLenum target,
 	              GLint level,
 	              GLint internalformat,
 	              GLsizei width,
 	              GLsizei height,
 	              GLint border,
 	              GLenum format,
 	              GLenum type,
 	              const GLvoid * data);
```
- `target`：2D 纹理类型，这里直接使用 `GL_TEXTURE_2D`
- `level`：`Mipmap` 等级，0 为原图
- `internalformat`：图片在 OpenGL 内的存储形式
- `format`：传入纹理的组织形式
- `type`：传入纹理的数据类型
- `border`：无用，应总是 0

OpenGL 支持同时使用多个纹理，每个纹理处于各自独立的纹理单元 Texture Unit，使用 `glActiveTexture(GL_TEXTUREn)` 启用第 n 个纹理单元。默认启用的纹理为 `GL_TEXTURE0` 单元，且 `GL_TEXTUREn` 的值为 `GL_TEXTURE0 + n`。OpenGL 保证至少有 16 个纹理单元可用。
# 应用纹理

纹理在片元着色器中通过纹理坐标进行采样，采样结果为对应位置的纹理颜色。

进行纹理采样需要两个变量 - 纹理采样器和纹理坐标。采样器决定从哪一个纹理中进行采样，纹理坐标决定取哪个位置的颜色。通常来说，采样器作为 uniform 变量传入着色器，而纹理坐标作为顶点数据传入。

采样器类型为 `samplerXxx`，其中 Xxx 为采样器类型，这里使用 `sampler2D` 表示 `GL_TEXTURE_2D` 类型纹理，从代码中传入的类型为 int，值为第几个纹理单元。

在片元着色器中使用 `texture(采样器, 纹理坐标)` 对纹理进行采样，采样结果为对应位置的颜色。

一个最简单的使用纹理的片元着色器如下，其中 `texCoord` 为从顶点着色器传递来的纹理坐标，`tex` 在程序中使用 `glUniform1i` 传入。

```c++
#version 460 core

uniform samper2D tex;
in vec2 texCoord;

out vec4 FragColor;

void main() {
    FragColor = texture(tex, texCoord);
}
```
# 纹理混合

若一个位置需要叠加多张图片，可以使用 GLSL 自带的 `mix` 函数；若多个颜色直接叠加，使用 `*` 运算符。

```c++
uniform samper2D tex1, tex2;
in vec2 texCoord1, texCoord2;

out vec4 FragColor;

void main() {
    FragColor = mix(
        texture(tex1, texCoord1),
        texture(tex2, texCoord2),
        0.2
    );
}
```
# 纹理销毁

```c++
glActiveTexture(待销毁纹理单元);
// 取消绑定
glBindTexture(GL_TEXTURE_2D, 0);
// 销毁纹理
glDeleteTextures(1, &texture);
```
