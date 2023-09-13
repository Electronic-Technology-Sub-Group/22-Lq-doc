---
状态: 未完成
参考资料: https://learnopengl-cn.github.io/05%20Advanced%20Lighting/03%20Shadows/01%20Shadow%20Mapping/
未完成部分: 阴影
---
# Blinn-Phong 光照模型

Phong 光照模型的镜面反射会在一些情况下出现问题，特别是物体反光度很低时，会导致大片（粗糙的）高光区域，如下图镜面高光区域的边缘出现了一道很明显的断层。
![[Pasted image 20230914015649.png]]

出现这个问题的原因是观察向量和反射向量间的夹角不能大于 90°。如果点积的结果为负数，镜面光分量会变为0.0。

当考虑漫反射光的时候，如果法线和光源夹角大于90度，光源会处于被照表面的下方，这个时候光照的漫反射分量的确是为0.0。但是，在考虑镜面高光时，我们测量的角度并不是光源与法线的夹角，而是视线与反射光线向量的夹角。
![[Pasted image 20230914015826.png]]

Blinn-Phong模型采用半程向量(Halfway Vector)，即光线与视线夹角一半方向上的一个单位向量替换反射向量。当半程向量与法线向量越接近时，镜面光分量就越大。
![[Pasted image 20230914015956.png]]

```c++
lightDir = normalize(lightPos - vFrogPos);
viewDir = normalize(viewPos - vFrogPos);
// Blinn-Phong 模型
vec3 halfwayDir = normalize(lightDir + viewDir);
float spec = pow(max(dot(normalize, halfwayDir), 0), 32);
```

![[Pasted image 20230914020228.png]]
![[Pasted image 20230914020252.png]]

Blinn-Phong 的镜面光分量会比冯氏模型更锐利一些。Blinn-Phong 的反光度通常是 Phong 模型的 2-4 倍
# Gamma 校正

```ad-note
Gamma：灰度系数，$设备输出亮度=电压^{Gamma}$

Gamma 校正：对每个像素取 $\frac{1}{2.2}$ 约为 0.45 次幂

sRGB 颜色空间：基于 Gamma 2.2 的颜色空间
```

对于CRT，Gamma通常为2.2。人类所感知的亮度恰好和CRT所显示出来相似的指数关系非常匹配，而与物理亮度的梯度不同

![[Pasted image 20230914020740.png]]

- 自动校正：`glEnable(GL_FRAMEBUFFER_SRGB)`
- 手动校正：片元着色器中 `fColor.rgb = pow(fColor.rgb, vec3(1/gamma))`
- Gamma 纹理：internal format 参数为 GL_SRGB
# 阴影
