```ad-note
投光物：将光**投射**(Cast)到物体的光源叫做投光物(Light Caster)。
```

添加投光物主要用于模拟不同种类的光源。
# 平行光
![[Pasted image 20230912012811.png]]
当一个光源处于很远的地方时，来自光源的每条光线就会近似于互相平行。当我们使用一个假设光源处于**无限**远处的模型时，它就被称为定向光，因为它的所有光线都有着相同的方向，它与光源的位置是没有关系的。

当在着色器中计算平行光光源时，使用平行光方向 `-direction` 代替 `lightPos - vPosition`，其余保持一致即可。
# 点光源
![[Pasted image 20230912013222.png]]
点光源是处于世界中某一个位置的光源，它会朝着所有方向发光，但光线会随着距离逐渐衰减。

```ad-note
衰减：随着光线传播距离的增长逐渐削减光的强度通常叫做衰减(Attenuation)
```

衰减值计算公式如下，其中 d 表示距离，其余参数可详见[-Point Light Attenuation | Ogre Wiki (ogre3d.org)](https://wiki.ogre3d.org/tiki-index.php?page=-Point+Light+Attenuation)
$$
\begin{equation} F_{att} = \frac{1.0}{K_c + K_l * d + K_q * d^2} \end{equation}
$$
- $K_c$：常数项，通常为 1，用于保证计算结果不大于 1 
- $K_l$：一次项，以线性方式衰减
- $K_q$：二次项，以二次递减减少强度，当距离较远时影响远大于 $K_l$ ，而较近时 $K_c$ 的影响更明显。

| 距离 | 常数项 | 一次项 | 二次项   |
| ---- | ------ | ------ | -------- |
| 7    | 1.0    | 0.7    | 1.8      |
| 13   | 1.0    | 0.35   | 0.44     |
| 20   | 1.0    | 0.22   | 0.20     |
| 32   | 1.0    | 0.14   | 0.07     |
| 50   | 1.0    | 0.09   | 0.032    |
| 65   | 1.0    | 0.07   | 0.017    |
| 100  | 1.0    | 0.045  | 0.0075   |
| 160  | 1.0    | 0.027  | 0.0028   |
| 200  | 1.0    | 0.022  | 0.0019   |
| 325  | 1.0    | 0.014  | 0.0007   |
| 600  | 1.0    | 0.007  | 0.0002   |
| 3250 | 1.0    | 0.0014 | 0.000007 |
将各个光照分量最终与衰减值相乘即可。

```c++
float distance = length(position - FragPos);
float attenuation = 1.0 / (...);

ambient *= attenuation;
Lisght *= attenuation;
ambient *= attenuation;
```
# 聚光

聚光是位于环境中某个位置的光源，它只朝一个特定方向而不是所有方向照射光线。这样的结果就是只有在聚光方向的特定半径内的物体才会被照亮，其它的物体都会保持黑暗。聚光很好的例子就是路灯或手电筒。
![[Pasted image 20230912020411.png]]
- `LightDir`：从片段指向光源的向量。
- `SpotDir`：聚光所指向的方向。
- `Phi`ϕ：指定了聚光半径的切光角。落在这个角度之外的物体都不会被这个聚光所照亮。
	- outerCutOff = cosϕ 
- Theta`θ：LightDir向量和SpotDir向量之间的夹角。在聚光内部的 θ 值应该比 ϕ 值小。
	- cutOff = cosθ 

```c++
// direction 为聚光灯位置
float theta = dot(lightDir, normalize(-light, direction));
if (theta > cutOff) {
    float epsilon = cutOff - outerCutOff;
    float intensity = clamp((theta - outerCutOff) / epsilon, 0, 1);
    diffuse *= intensity;
    specular *= intensity;
}
```
# 多光源

多光源只需要多组 ambient, diffuse, specular 分别计算后叠加即可。