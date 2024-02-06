# 目录结构

Gradle 目录分为两部分 - Gradle 用户目录和项目目录。前者存放 Gradle 本体需要的公共配置文件和缓存文件，后者为被 Gradle 管理的项目。

![[Pasted image 20240202164330.png]]
## Gradle 用户目录

Gradle 用户目录由 `GRADLE_USER_HOME` 环境变量配置，默认为 `~/.gradle` 或 `C:\Users\用户名\.gradle`。其结构如下：

| 名称                   | 类型 | 说明                          |
| ---------------------- | ---- | ----------------------------- |
| caches                 | 目录 | 版本无关的缓存                |
| caches/版本号          | 目录 | 用于特定 Gradle 版本的缓存    |
| caches/jars-n          | 目录 | 共享缓存（依赖项等），n 为数字          |
| caches/modules-n       | 目录 | 共享缓存（依赖项等），n 为数字          |
| daemon                 | 目录 | Gradle 守护进程的注册表和日志 |
| init.d | 目录 | 保存全局初始化脚本                |
| jdks                   | 目录 | 由工具链下载的 JDK            |
| wrapper/dists          | 目录 | 下载的 Gradle Wrapper 发行版  |
| gradle.property                       | 文件     | 全局 Gradle 配置                              |
### 自动缓存清理

默认每天由 Gradle 守护进程运行一次清理程序，包括：
- `cache/版本号` 目录中，正式版 30 天不使用则清除，快照版 7 天不使用则清除
- `cache/jars-n` 等共享缓存，远程 30 天未使用删除，本地生成的缓存 7 天删除
- `wrapper/dists` 发行版未被使用时删除

8.0 之后，通过 `init.d/cache-settings.gradle` 可以设置缓存清理策略：

```groovy
beforeSettings { settings ->
    settings.caches {
        // 未使用正式版 Wrapper 缓存删除日期
        releasedWrappers.removeUnusedEntriesAfterDays = 45
        // 未使用快照版 Wrapper 缓存删除日期
        snapshotWrappers.removeUnusedEntriesAfterDays = 10
        // 未使用共享缓存（如依赖项等）删除日期，如依赖库等
        downloadedResources.removeUnusedEntriesAfterDays = 45
        // 未使用共享缓存（构建时生成）删除日期，如工件转换等
        createdResources.removeUnusedEntriesAfterDays = 10
        // 缓存清理程序运行配置
        //   DEFAULT: 默认定期在后台执行（24h 每次）
        //   DISABLED: 不自动执行
        //   ALWAYS: 每次回话后执行
        cleanup = Cleanup.DISABLED
    }
}
```

>[!note]
>由于缓存清理策略仅 8.0 之后存在，可以考虑使用 `GradleVersion.current() >= GradleVersion.version(8.0)` 进行判断，然后导入该策略。其他对版本有要求的设置也可以这样进行。
>
>1. 将缓存清理策略设置文件放在非 `init.d` 根目录中，如 `init.d/8.0/cache-settings.gradle`
>2. 在 `init.d/cache-settings` 中将其导入：
>
>```groovy
>if (GradleVersion.current() >= GradleVersion.version(8.0)) {
>    apply from: '8.0/cache-settings.gradle'
>}
>```
### 缓存标记

Gradle 8.1 开始支持使用 `CACHEDIR.TAG` 文件设置缓存标记，详见[[Cache Directory Tagging Specification|缓存标记规范]]。

若需要移除标记，使用 `markingStrategy` 设置

```groovy
// /init.d/cache-settings.gradle
beforeSettings { settings ->
    settings.caches {
        markingStrategy = MarkingStrategy.NONE
    }
}
```
## 项目目录

一个 Gradle 管理的项目目录的典型结构如下：

![[msedge_20240203_36.png]]

1. Gradle 用于特定项目的缓存目录
2. 用于特性项目特定 Gradle 版本的缓存目录
3. 该项目的构建目录，`build` 过程中产生的文件都在这里
4. Gradle Wrapper 及其配置
5. 特定项目的 Gradle 配置属性
6. 特定项目的 Gradle 执行脚本（可以认为是 Gradle 主程序）
7. 项目配置文件，可以在里面定义子项目
8. 子项目目录
9. 用于子项目的构建脚本
# 生命周期

