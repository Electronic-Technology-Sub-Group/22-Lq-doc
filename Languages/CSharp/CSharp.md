C# 始于 J#，起初模仿 Java，因此与 Java 有很多相似的语法。但相较于 Java，C# 也有很多很好的地方。C# 的文件扩展名为 `.cs`

平台与工具
- .Net 平台在 Windows 下有很强的性能优势，以及 WPF 等 GUI 控件，在 Windows 下可以说是首选语言，Linux 下也有 mono 做跨平台实现
- 直接编译成二进制文件，摆脱了 JVM 早期解释运行的低效率，可用于 Unity，在游戏上也被广泛使用
- 服务器上有 Windows Service 下使用 ASP.Net，移动设备上有 MAUI
- 微软技术支持，MSDN 有完善且详细的文档（中文！），宇宙最强 IDE Visual Studio 加持

语法，主要是相对于 Java 的。
- 支持结构体、真实泛型
- 值类型与引用类型基本统一
- 支持指针直接访问内存，更加灵活
- 支持对象属性（带有 get set 方法）
- 支持部分类（一个类可以放入多个文件中，但感觉是很差的设计）、命名空间（与目录名可不同）
- 与其他代码调用（如调用 C 库）更方便，没有 JNI 的复杂操作

C# 会先编译成 .Net 平台的中间代码（IL），类似于 Java 的字节码，但字节码是 Java 编译的最终产物，运行时不考虑 JIT 优化的情况下是解释运行的，而 C# 会继续编译成可执行文件，效率更高

程序集：类似于库，.Net 程序的库和可执行文件，包含编译好的面向 .Net Framework 的代码的逻辑单元
- 私有程序集：附带在某软件上, 且只能用于该软件
- 共享程序集：其他程序可以使用的公共库，当名称冲突时可使用全局程序缓存 GAC 处理

- [[CSharp 基本语法]]
- [[CSharp 流程控制]]
- [[CSharp 类型]]
- [[CSharp 命名空间]]
- [[CSharp 结构体]]
- [[CSharp 类与继承]]
	- [[CSharp 属性]]
	- [[CSharp 函数]]
	- [[CSharp 运算符]]
	- [[CSharp 枚举]]
	- [[CSharp 密封类]]
	- [[CSharp 接口]]
- [[CSharp 异常处理]]
- [[CSharp 泛型]]
- [[CSharp 数组与元组]]
- [[CSharp 字符串]]
- [[CSharp 迭代器与集合]]
- [[CSharp 委托与事件]]
- [[CSharp 文件与流]]
- [[CSharp 网络]]
- [[CSharp 并行任务]]
- [[CSharp 锁与同步]]
- [[CSharp 内存与资源]]
- [[CSharp 预处理指令]]
- [[CSharp 反射]]
- [[CSharp 动态类型]]
- [[CSharp 指针]]
- [[CSharp 平台调用]]
# 未完成

- 安全验证：有关 Windows 身份相关 API，详见 `Principal` 类、`WindowsIdentity` 类
- Composition
- XML 和 JSON
- 本地化