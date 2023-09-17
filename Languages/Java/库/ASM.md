---
类型: 代码生成
包名: org.ow2.asm:asm
版本: "9.5"
---
ASM 是一种通用 Java 字节码操作和分析框架，目标是生成、转换和分析编译后的 java 类。它可以用于修改现有的 class 文件或动态生成 class 文件
- AOP 切片
- 替代反射，提高效率
- 程序分析及逆向工程
- 混淆或反混淆

[ASM (ow2.io)](https://asm.ow2.io/)
[[ASM4手册中文版.pdf]]

ASM 提供了两种生成和转换已编译类的 API - CoreAPI 与 TreeAPI。
# CoreAPI

基于事件的 API，类似 SAX 解析，速度快，内存占用少，但不保存上下文。相关包有：
- `asm.jar`：`org.objectweb.asm`，`org.objectweb.asm.signature`，基于事件的 API 的定义和类解析器、编写器组件
- `asm-util.jar`：`org.objectweb.asm.util`，核心 API 各种工具，用于 ASM 应用程序的开发和调试
- `asm-commons.jar`：`org.objectweb.asm.commons`，提供几个有用的预定义转换器

CoreAPI 基于访问者模式，将对类的修改放置到一个 Visitor 对象中。
## ClassVisitor

用于生成和转换编译类的 ASM API，每个方法对应类结构。

为创建转换完成后的字节码，可使用以下类：
- 对于注解：`AnnotationVisitor`，实现类为 `AnnotationWriter`
- 对于字段：`FieldVisitor`，实现类为 `FieldWriter`
- 对于方法：`MethodVisitor`，实现类为 `MethodWriter`

所有 `ClassVisitor` 由事件产生器 `ClassReader` 调用。
## ClassReader

读取字节码文件，并将数据通过事件转发到 `ClassVisitor`。`ClassReader` 的 `accept` 方法接收一个 Visitor 对象，并需要一个 `parsingOptions` 参数
- `SKIP_CODE`：跳过代码部分，仅访问类结构
- `SKIP_DEBUG`：跳过调试信息，且不为其创建人工标签
- `SKIP_FRAMES`：跳过堆栈映射帧
- `EXPAND_FRAMES`：压缩帧
## ClassWriter

`ClassVisitor` 有一个实现类 `ClassWriter`，通常使用该类。当传入一个 ClassReader 时表示修改类，直接使用表示创建新类。

直接调用 `visitXxx` 方法可以向类中添加元素
- 以 `visit()` 方法表示类声明的开头，并以 `visitEnd()` 方法结束
- 要添加元素使用对应的 `visitXxx` 方法，且最后需要调用 `visitEnd()`

```java
ClassWriter cw = new ClassWriter(0);  
cw.visit(V17, ACC_PUBLIC | ACC_ABSTRACT, "Hello", null, "java/lang/Object", null);  
cw.visitMethod(ACC_PUBLIC | ACC_ABSTRACT, "print", "()V", null, null).visitEnd();  
cw.visitEnd();  
  
byte[] b = cw.toByteArray();  
Files.write(Path.of("Hello.class"), b, StandardOpenOption.CREATE_NEW);
```

`ClassWriter` 带有一个方法 `toByteArray()` 可直接获取字节码
## Adapter

若要修改一个类，应在 `ClassReader` 与 `ClassWriter` 之间插入一个或多个 `ClassVisitor` 用于修改类，这些修改器称为适配器 Adapter

![[Pasted image 20230826085327.png]]

```java
ClassReader cr = new ClassReader("untitled/lq2007/asm/ASMCode");
ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);

ClassVisitor adapter = new ClassAdapter(cw);
cr.accept(adapter, 0);

byte[] bytes = cw.toByteArray();
Files.write(Path.of("ASMCode.class"), bytes, StandardOpenOption.CREATE_NEW);
```

`ClassAdapter` 实现：

```java
static class ClassAdapter extends ClassVisitor {
    
    boolean hasSameName = false;
    
    protected ClassAdapter(ClassVisitor cv) {
        super(ASM9, cv);
    }
    
    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        // 检查是否存在同名字段
        hasSameName = hasSameName | ("hello".equals(name));
        return super.visitField(access, name, descriptor, signature, value);
    }
    
    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        // 修改一个方法签名 - 将方法名 ABC 转化为 ABCD
        if ("ABC".equals(name)) {
            return super.visitMethod(access, "ABCD", descriptor, signature, exceptions);
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }
    
    @Override
    public void visitEnd() {
        // 添加一个字段
        if (!hasSameName)
            getDelegate().visitField(ACC_PUBLIC | ACC_FINAL, "hello", "I", null, 10).visitEnd();
        super.visitEnd();
    }
}
```
## 工具类

- `Type`：表示一种 Java 类型，可通过 Class 创建，可构建各种普通类型、泛型、基本类型等
- `TraceClassVisitor`：继承自 ClassVisitor，获取创建类的可跟踪文本内容，生成的记录如下
	- `ASMifier`：`TraceClassVisitor` 的一个可选后端

```java
// class version 49.0 (49)
// access flags 1537
public abstract interface pkg/Comparable implements pkg/Mesurable {
// access flags 25
public final static I LESS = -1
// access flags 25
public final static I EQUAL = 0
// access flags 25
public final static I GREATER = 1
// access flags 1025
public abstract compareTo(Ljava/lang/Object;)I
}
```

- `CheckClassAdapter`：附带核对方法调用顺序检查和参数有效性检查
- `AnalyzerAdapter`：基于 visitFrame 访问过的帧，来对比每个指令的栈映射帧
- `LocalVariableSorter`：讲一个方法中的局部变量按顺序编号，同时可使用 `newLocal` 创建新局部变量

```java
public void visitCode() {
​    super.visitCode();
    mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J");
    time = newLocal(Type.LONG_TYPE);
    mv.visitVarInsn(LSTORE, time);
}
```

- `AdviceAdapter`：抽象类，可在开头、结尾前插入代码；同时适用于构造，允许在父类构造前
	- `onMethodEnter()`：方法插入前
	- `onMethodExit()`：方法结束前，包括 RETURN 等
# TreeAPI

基于对象的 API，类似 DOM 解析，会将整个对象加载到内存中，因此占用内存更大，速度也相对慢一点，但可以访问到整个对象信息，使用也更符合直觉
- `asm-tree.jar`：`org.objectweb.asm.tree`，定义基于对象的 API，并提供基于对象和基于事件表示之间的转换工具
- `asm-analysis.jar`：`org.objectweb.asm.tree.analysis`：基于 TreeAPI 的类分析框架和几个预定义类分析器

`ClassNode` 表示一个类，其他各类元素为 `XxxNode`，并使用列表的方法操作其中元素

```java
ClassReader cr = new ClassReader("untitled/lq2007/asm/ASMCode");
ClassNode cn = new ClassNode();
cr.accept(cn, 0);

MethodNode mn = new MethodNode(ACC_PUBLIC | ACC_ABSTRACT, "print", "()V", null, null);
cn.methods.add(mn);

ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
cn.accept(cw);
byte[] bytes = cw.toByteArray();
Files.write(Path.of("ASMCode.class"), bytes, StandardOpenOption.CREATE_NEW);
```
# InsnNode

- FieldInsnNode：用于 `GETFIELD` 和 `PUTFIELD` 之类的字段操作的字节码
	- owner 字段所在的类
	- name 字段的名称
	- desc 字段的类型
- VarInsnNode：局部变量操作的字节码：`ALOAD`，`ASTORE` 等
	- var 局部变量的编号
- InsnNode：无参数值操作的字节码，如 `ALOAD_0`，`DUP` 等
- IntInsnNode：操作数是一个整数的字节码指令，包括 `BIPUSH`，`SIPUSH`，`NEWARRAY`
- IincInsnNode：变量自加（`+=`）
	- var 目标局部变量的位置
	- incr 要增加的数
- LdcInsnNode：用于 LDC 等插入引用值的字节码
	- cst 引用值
- TypeInsnNode：类型相关的字节码，包括 `NEW`，`ANEWARRAY`，`CHECKCAST` 等
	- desc 类型
- MultiANewArrayInsnNode：创建多维数组 `MULTIANEWARRAY`
	- desc 类型
	- dims 维数
- MethodInsnNode：传统的方法调用，如 `INVOKEVIRTUAL` 等，不适用于 `INVOKEDYNAMIC`
	- owner 方法所在的类
	- name 方法的名称
	- desc 方法的描述
- InvokeDynamicInsnNode：`INVOKEDYNAMIC` 操作字节码
	- name 方法的名称
	- desc 方法的描述
	- bsm 句柄
	- bsmArgs 参数常量
- LabelNode：插入用于表示转跳点的 Label 节点
- JumpInsnNode：用于 `IFEQ` 或 `GOTO` 等转跳操作字节码
	- lable 目标lable
- TableSwitchInsnNode：实现 `TABLESWITCH` 操作的字节码
	- min 键的最小值
	- max 键的最大值
	-  dflt `default` 块对应的 Lable
	- labels 其他对应的 Label
- LookupSwitchInsnNode：实现 `LOOKUPSWITCH` 操作的字节码
	- dflt `default`块对应的Lable
	- keys 值
	- labels 对应的Label
- LineNumberNode：表示行号的节点
	- line 行号
	- start 对应的 第一个Label
- FrameNode：栈映射帧的字节码
