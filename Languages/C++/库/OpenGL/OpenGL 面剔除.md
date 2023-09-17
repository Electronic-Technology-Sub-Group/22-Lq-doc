面剔除用于处理不可视的面，默认关闭

```c++
glEnable(GL_CULL_FACE);
```

开启面剔除后，保留正向三角形，剔除逆向三角形。默认正向三角形为逆时针方向

```c++
glFrontFace(GL_CCW);
```
- `GL_CCW`：逆时针
- `GL_CW`：顺时针

开启面剔除后，可配置保留正向还是逆向图形

```c++
glCullFace(GL_BACK);
```
- `GL_BACK`：背面剔除，背面图形不会渲染
- `GL_FRONT`：正面剔除
- `GL_FRONT_AND_BACK`：全剔除
