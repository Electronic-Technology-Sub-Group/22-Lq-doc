# 基本结构

从 [architectury/architectury-templates: Downloads to template mods to set-up architectury mods. (github.com)](https://github.com/architectury/architectury-templates) 下载模板，对应平台代码放置在不同模块中：
- `common`：平台无关代码
- `forge`：Forge 平台代码
- `neoforge`：NeoForge 平台代码
- `fabric`：Fabric 平台代码

`common` 模块通常包含一个以 Mod 主类命名的类用于存放平台无关的公共代码，`Mod主类Platform` 命名的类用于存放平台相关的公共代码。

`common` 模块中某些类若涉及到对应平台代码，满足某些条件时 Architectury 会自动去对应平台查找具体实现。
- 方法必须是 static 的
- 使用 `@ExpectPlatform` 注解标记

对应平台目录中，至少需要包含以下内容：
- 各自平台的 Mod 主类，按各自平台的要求写（forge：`@Mod` 注解，fabric：`ModInitializer` 接口）
- `common` 模块中的具体实现。使用 `@ExpectPlatform` 注解标记的方法的具体实现在 `包名.平台名.类名Impl` 类中的同名函数，必须使用 `public static` 修饰

```java
// common 包
// net.examplemod.ExampleExpectPlatform
public class ExampleExpectPlatform {
    @ExpectPlatform
    public static Path getConfigDirectory() {
        // Just throw an error, the content should get replaced at runtime.
        throw new AssertionError();
    }
}
```

```java
// forge 包
// net.examplemod.forge.ExampleExpectPlatformImpl
public class ExampleExpectPlatformImpl {
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}
```

```java
// fabric 包
// net.examplemod.fabric.ExampleExpectPlatformImpl
public class ExampleExpectPlatformImpl {
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
```
