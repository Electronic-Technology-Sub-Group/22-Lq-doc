`ToolProvider` 取代了曾经的 JSR 199 编译器插件，允许在程序运行时对源代码进行编译，或生成其文档。
# JavaCompiler

`DocumentationTool` 通过 `ToolProvider.getSystemJavaCompiler()` 获取，用于获取系统级的 Java 编译器。使用该编译器编译 Java 源码步骤如下：
1. 准备待编译 Java 源代码，可以是文件也可以是字符串或流等
2. 创建 Java 源文件集合
3. 创建编译任务并执行编译
4. 根据编译结果进行下一步操作
## 源文件集合

一个源文件集合是一个 `JavaFileObject` 类型的可迭代对象，通常是列表。

`JavaFileObject` 表示一个 Java 源文件
- 重写 `getCharContent` 方法用于获取源代码文本
- 若使用字符串作为源码，URI 使用 `string:///` 协议
- 文件路径是包名（`.` 替换成 `/`）+类名+扩展名，扩展名可以使用 `JavaFileObject.Kind.SOURCE`

```java
// 准备源码
String classPath = "org/example/test/Test";
String source = createClassSource();
// 创建源文件路径，这里使用 string 协议表示使用字符串源码
JavaFileObject sourceFile = new SimpleJavaFileObject(
        URI.create("string:///" + classPath + JavaFileObject.Kind.SOURCE.extension),
        JavaFileObject.Kind.SOURCE) {
    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return source;
    }
};
// 创建源代码集合
List<? extends JavaFileObject> compilationUnits = List.of(sourceFile);
```
## 编译源代码

编译源代码的任务使用 `JavaCompiler#getTask` 创建，并使用 `task.call()` 方法执行。该方法返回一个布尔值，表示是否编译成功。

`JavaCompiler#getTask` 接收方法的参数如下：
- out：警告、错误信息的输出位置，`null` 表示使用默认 `System.err`
- fileManager：文件管理器，可以通过 `compile.getStandardFileManager` 方法获取
- diagnostics：接收警告、异常等信息的具体内容，可以使用 `DiagnosticCollector` 对象
- options：编译器参数，`null` 表示无参数
- classes：待编译类名，`null` 表示全部源码类
- compilationUnits：前面准备的源文件集合，也是编译单元集合

`StandardJavaFileManager` 对象是 Java 编译器的一部分，它提供了文件管理器的标准实现，可以通过他获取编译结果。通常使用 `compile.getStandardFileManager` 方法获取，各个参数都可以为 `null`，表示使用默认值。
- diagnosticListener：异常信息（诊断信息）侦听器
 
```java
JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits);
Boolean compileSucceed = task.call();
```
## 编译结果

`task.call()` 返回的值表示编译是否成功。若编译成功，编译后的字节数组可以通过 `fileManager` 获取；若编译失败，异常信息可以通过 `diagnostics` 获取。

```java
Boolean compileSucceed = task.call();
if (compileSucceed) {
    // 编译成功，获取编译结果
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    FileObject output = fileManager.getFileForOutput(null, "org/example/test", "Test.class", null);
    output.openInputStream().transferTo(bos);
    byte[] bytes = bos.toByteArray();
}

// 编译错误，检查异常
// 也包括其他 WARN，INFO 等信息
for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
    // 诊断错误
}
```
# DocumentationTool

`DocumentationTool` 提供了一种生成文档的方法，该文档可以从源代码和注释中提取。生成的文件仍需要通过 `StandardJavaFileManager` 获取

```java
// 创建源代码集合
List<? extends JavaFileObject> compilationUnits = List.of(sourceFile);
// 生成文档
DocumentationTool documentationTool = ToolProvider.getSystemDocumentationTool();
StandardJavaFileManager fileManager = documentationTool.getStandardFileManager(null, null, null);
DocumentationTool.DocumentationTask task = documentationTool.getTask(null, fileManager, null, null, null, compilationUnits);
Boolean generateSucceed = task.call();
if (generateSucceed) {
    FileObject output = fileManager.getFileForOutput(null, "org/example/test", "Test.html", null);
    // do something
}
```