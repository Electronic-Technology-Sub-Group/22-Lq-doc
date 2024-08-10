不使用 ASM 的情况下修改 MC 源代码。

配置好 Mixin 后，创建 Mixin 类，并使用 `@Mixin` 指定修改的类。所有对待修改类的修改都将在该类中实现。

由于 Mixin 类永远不会被实例化，可以将其设置为 `abstract`。

```java
@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
}
```

为方便表达，下面将被注入类（例中的 `Minecraft`）称为原始类，将 Mixin 注入类（例中的 `MinecraftMixin`）称为 Mixin 类。

使用 Mixin 务必添加详细的注释，以便后期调试或阅读

# 参考

```cardlink
url: https://xfl03.gitbook.io/coremodtutor/5
title: "5 Mixin | CoreModTutor"
host: xfl03.gitbook.io
favicon: https://xfl03.gitbook.io/coremodtutor/~gitbook/icon?size=small&theme=light
image: https://xfl03.gitbook.io/coremodtutor/~gitbook/ogimage/-Lmtu6u2mnQVo_iywdSc
```

```cardlink
url: https://fabricmc.net/wiki/zh_cn:tutorial:mixin_introduction
title: "Mixin 介绍 [Fabric Wiki]"
host: fabricmc.net
```
