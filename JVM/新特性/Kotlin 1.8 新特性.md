# 1.8.0

## Lombok @Builder 支持

支持在 Kotlin 与 Java 代码都有的模块中调用 Java 的 Lombok 所生成代码

## Optional 扩展

- `getOrNull()`，`getOrDefault()`，`getOrElse()`
- `toList()`，`toSet()`，`toCollection()`，`asSequence()`

## 可加减的 TimeMark 
#实验性功能 

## 目录递归操作
#实验性功能 

`Path` 支持对目录的递归操作：
- `copyToRecursively()`
- `deleteRecursively()`

## 其他

- `Math.cbrt()` 立方根
- `toDuratonUnit()`，`toTimeUnit()` 可用于与 `TimeUnit` 的转换
- `-Xno-new-java-annotation-targets` 参数用于避免生成 `TYPE_USE` 及 `TYPE_PARAMETER` 注解目标
- `-Xdebug` 参数用于禁用编译器优化
- 删除旧版后端
- 新版 Android 源代码集布局

## KotlinNative

- 支持 Xcode 14.1
- 改进了 Objective-C/Swift 互操作性
- CocoaPods Gradle 插件中默认动态 framework

## KotlinJS

- 稳定版 JS IR 编译器后端
- 报告 yarn.lock 已更新的新设置
- 通过 Gradle 属性添加浏览器测试目标
- 向项目添加 CSS 支持的新方式

## Gradle

- 将 Kotlin 编译器选项暴露为 Gradle 惰性属性
- 提高最低支持版本
- 禁用 Kotlin 守护程序回退策略的能力
- 在传递依赖项中使用最新的 kotlin-stdlib 版本
- 强制检查相关 Kotlin 与 Java 编译任务的 JVM 目标兼容等价性
- 解决 Kotlin Gradle 插件的传递依赖项
- 弃用与删除