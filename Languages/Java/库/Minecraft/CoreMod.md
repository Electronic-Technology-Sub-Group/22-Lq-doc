# AT&AW
# Mixin

使用 Mixin 可以在不接触字节码（ASM）的情况下修改 MC 源代码。

参考资料：
- [5 Mixin - CoreModTutor (gitbook.io)](https://xfl03.gitbook.io/coremodtutor/5)
- [Mixin 介绍 [Fabric Wiki] (fabricmc.net)](https://fabricmc.net/wiki/zh_cn:tutorial:mixin_introduction)
## 编译配置
### LexForge 1.20

[SpongePowered/MixinGradle: Gradle plugin that adds the Mixin technology capability to your project. (github.com)](https://github.com/SpongePowered/MixinGradle)

```groovy
// build.gradle

// 引入 MixinGradle 插件
buildscript {
        repositories {
            maven {
                url = 'https://repo.spongepowered.org/repository/maven-public/'
            }
       }
       
       dependencies {
           // ... forge gradle
           classpath 'org.spongepowered:mixingradle:0.7-SNAPSHOT'
       }
}

apply plugin: 'org.spongepowered.mixin'

// Mixin 仓库与依赖

repositories {
    maven {
        url = 'https://repo.spongepowered.org/repository/maven-public/'
    }
}

dependencies {
    annotationProcessor 'org.spongepowered:mixin:0.8.5:processor'
}

// 配置 Mixin

mixin {
    add sourceSets.main, "[modid].refmap.json"
    config "[modid].mixins.json"
}

sourceSets.main {
    ext.refMap = "[modid].refmap.json"
}
```
### Fabric

[Mixin 介绍 [Fabric Wiki] (fabricmc.net)](https://fabricmc.net/wiki/zh_cn:tutorial:mixin_introduction)

不需要引入 `MixinGradle` 插件。

```json
// resources/fabric.mod.json
{
  "mixins" {
    "[moodid].mixins.json"
  }
}
```
### NexForge

不需要引入 `MixinGradle` 插件。

```toml
# mods.toml
[[mixins]]  
config = "[modid].mixins.json"
```
### Architecture

[loom:mixins | Architectury Documentation](https://docs.architectury.dev/loom/mixins)

不需要引入 `MixinGradle` 插件。在 Architecture 项目的具体某平台项目中，只需要声明一下使用 Mixin 即可。

- LexForge

```groovy
// build.gradle
loom {
    forge {
        mixinConfig '[moodid].mixins.json'
    }
}
```

- Fabric

```json
// resources/fabric.mod.json
{
  "mixins" {
    "[moodid].mixins.json"
  }
}
```

- NeoForge

```toml
# mods.toml
[[mixins]]  
config = "[modid].mixins.json"
```
## Mixin 配置

Mixin 配置文件位于 `resources/[modid].mixins.json` 中

```json
// resources/[modid].mixins.json
{
  "required": true,
  // 最低 mixin 版本
  "minVersion": "0.8",
  // mixin 类所在包
  // 该包内所有类不应被 mod 直接访问
  "package": "mod.example.mixin",
  // 适用于的 Java 版本
  "compatibilityLevel": "JAVA_17",
  // 同时应用于客户端与服务端的 Mixin 类
  "mixins": [
    // 指 mod.example.mixin.BlockEntityTypeMixin 类
    "BlockEntityTypeMixin"
  ],
  // 仅应用于客户端的 Mixin 类
  "client": [
    // 指 mod.example.mixin.MinecraftMixin 类
    "MinecraftMixin"
  ],
  // 仅应用于服务端的 Mixin 类
  "server": [
    // 指 mod.example.mixin.MinecraftServerMixin 类
    "MinecraftServerMixin"
  ],
  "injectors": {
    "defaultRequire": 1
  }
}

```
## 使用

创建 Mixin 类，并使用 `@Mixin` 指定修改的类。所有对待修改类的修改都将在该类中实现。

由于该类永远不会被实例化，可以将其设置为 `abstract`。

```java
@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
}
```

为方便表达，下面将被注入类（例中的 `Minecraft`）称为原始类，将 Mixin 注入类（例中的 `MinecraftMixin`）称为 Mixin 类。

> [!danger]
> 使用 Mixin 务必添加详细的注释，以便后期调试或阅读
### 添加成员

在 Mixin 类中的所有成员将直接合并到原始类中，因此尽量起一个不会引起冲突的成员名，通常在成员前添加 `modid$` 前缀。

不能直接访问添加的成员，主要原因为：
- 内容是在 build 生成字节码时添加，存在在字节码中而不在源码中，因此在代码中直接访问会报错。
- 由于 Mixin 类都会被添加到类加载器的 `invalidClasses` 中，也不能直接将原始类实例强转为 Mixin 类。

有两种方式可以访问：

- 反射。反射访问对应内容是可行的，但存在性能问题。
- 接口。使 Mixin 类实现某一个接口，然后将被修改类型强转为接口类型

```java
// 接口类
public interface IBlockLootSubProvider {
    Iterable<Block> sino$getKnownBlocks();
}
```

```java
// Mixin 类
@Mixin(BlockLootSubProvider.class)
public abstract class BlockLootSubProviderMixin implements IBlockLootSubProvider {

    @Override
    public Iterable<Block> sino$getKnownBlocks() {
        // do something
    }
}
```

```java
// 具体使用场景
BlockLootSubProvider provider;
IBlockLootSubProvider mixined = (IBlockLootSubProvider) provider;
Iterable<Block> blocks = mixined.sino$getKnownBlocks();
```

### 访问成员

Mixin 提供 `@Accessor` 和 `@Invoker` 注解，用于暴露原始类的非公开成员。其中 `@Accessor` 注解用于变量，`@Invoker` 用于方法。

对于变量：
- 方法名以 `get`，`is`，`set` 开头，分别用于获取字段，获取 boolean 字段，修改字段
- `get`，`is` 开头的方法不能有参数，且返回值类型必须与字段类型相同
- `set` 开头的方法只能有一个参数，参数类型与字段类型相同，返回类型必须是 `void`
- 若省略 `value` 属性，方法名剩余部分与变量名必须相同，但首字母大写

对于方法：
- 方法名以 `call` 或 `invoke` 开头
- 方法返回值、形参列表与对应函数必须相同
- 若省略 `value` 属性，方法名剩余部分与函数名必须相同，但首字母大写

这两个注解只能用于抽象方法（或接口方法），且不需要手动实现。若需要在类外使用（接口形式），需要满足一定条件：
- 注解位于接口中
- 接口只能存在这两种注解
- 接口使用 `@Mixin` 标记并在 Mixin 配置上添加该接口

```java
@Mixin(GuiMainMenu)
public interface IGuiMainMenuMixin {
    @Accessor
    void setSplashText(String text);
}
```
### 注入

使用 `@Inject` 注解标记的方法会在特定注入点注入代码，以达成局部修改代码的效果。该注解提供多个参数用于确定注入点：
- `method`：注入函数。若不存在同名函数，使用函数名即可；否则，需要完整函数签名
	- 构造函数函数名为 `<init>`
	- 类初始化函数名为 `<cinit>`
- `slice`：确定注入位置的存在范围，指定一个或多个 `@Slice` 注解
- `at`：确定注入点位置，指定一个或多个 `@At` 注解
- `cancellable`：默认 false，是否支持注入 `return`
- `locals`：捕获方式

注入方法需要满足：
- 返回类型为 `void`，推荐访问等级为 `private`（注入方法访问等级与原方法无关）
- 若原函数为 `static` 的，则注入方法也必须是 `static` 的
- 函数名可以是任何合法标识符，Mixin 会自动重命名
- 允许包含原函数的全部或部分参数，必须在所有参数开头，且顺序与类型必须与原函数参数一致
- 若不存在捕获变量，最后一个参数必须是 `CallbackInfo` 对象，若原函数返回值非 `void` 则是 `CallbackInfoReturnable<T>`
- 允许捕获局部变量，被捕获变量位于 `CallbackInfo` 之后

`CallbackInfo` 或 `CallbackInfoReturnable<T>` 是必要的。当 `cancellable=true` 时，调用以下方法相当于注入了一个 `return`：
- `CallbackInfo#cancel()`
- `CallbackInfoReturnable#setReturnValue()`
#### @At

`@At` 注解非常重要，决定了注入位置
- `value`，`args`，`target`，`ordinal`，`opcode`：定位方式
- `slice`：选择一个或多个在外部 `slice` 属性定义的 `@Slice` 的 id，确定注入范围
- `shift`，`by`：相对于前面确定的注入点的偏移量调整

注入定位（`value`，`args`，`target`，`ordinal`，`opcode`）：

| value         | args                                                                                                                        | target                                           | ordinal                             | opcode                                                                      | 说明                                                           |
| ------------- | --------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------ | ----------------------------------- | --------------------------------------------------------------------------- | -------------------------------------------------------------- |
| HEAD          |                                                                                                                             |                                                  |                                     |                                                                             | 注入到方法开头                                                 |
| RETURN        |                                                                                                                             |                                                  | 第 n 个 return，省略表示所有 return |                                                                             | 注入到 return 之前                                             |
| TAIL          |                                                                                                                             |                                                  |                                     |                                                                             | 注入到最后一个 return 之前                                     |
| INVOKE        | log:bool，是否输出日志                                                                                                      | 带完整限定名的方法签名，若省略则表示所有方法调用 | 第 n 个 调用，省略表示所有方法调用  |                                                                             | 注入到方法调用之前                                             |
| INVOKE_STRING | ldc:string，匹配的字面量<p></p>log:bool，是否输出日志                                                                       | 带完整限定名的方法签名，若省略则表示所有方法调用 | 第 n 个 调用，省略表示所有方法调用  |                                                                             | 注入到只接收一个 String 类型参数，且返回值为 void 的方法调用前 |
| INVOKE_ASSIGN | log:bool，是否输出日志                                                                                                      | 带完整限定名的方法签名，若省略则表示所有方法调用 | 第 n 个 调用，省略表示所有方法调用  |                                                                             | 注入到返回值类型非 void 的函数调用之后（STORE操作符之后）      |
| FIELD         | array:get/set/length，匹配数组元素被引用/赋值/读length属性时<p></p>log:bool，是否输出日志                                   | 带完整限定名的字段签名，若省略则表示所有字段签名 | 第 n 个 调用，省略表示所有字段引用  | 178：GETSTATIC<p></p>179：PUTSTATIC<p></p>180：GETFIELD<p></p>181：PUTFIELD | 注入到字段引用之前                                             |
| NEW           | class: 当 target 未指定时表示匹配实例化类型                                                                                 | 匹配实例化类名或类名或构造函数签名               | 第 n 个 调用，省略表示所有对象创建  |                                                                             | 注入到 NEW 操作符之前                                          |
| CONSTANT      | nullValue:bool，是否匹配 null<p></p>intValue, floatValue, longValue, doubleValue, stringValue, class：各种匹配常量值<p></p>log:bool，是否输出日志<p></p>expandZeroConditions：与 0 比较时的策略 |                                                  | 第 n 个 引用，省略表示所有字面量引用                                    |                                                                             | 注入到常量引用之前                                             |
| JUMP          |                                                                                                                             |                                                  | 第 n 个 跳转，省略表示所有字面量引用                                    | 153-168<p></p>198<p></p>199                                                                            | 注入到跳转操作符之前                                           |
| LOAD          |                                                                                                                             |                                                  | 第 n 个 引用                                    |                                                                             | 注入到 LOAD 操作符之前，与 `@ModifyVariable` 配合使用          |
| STORE         |                                                                                                                             |                                                  | 第 n 个 引用                                    |                                                                             | 注入到 STORE 操作符之前，与 `@ModifyVariable` 配合使用         |
#### @Slice

- id：指定 `Slice` id
- from：范围开始
- to：范围结束
#### 捕获局部变量

在设置好 `@At` 注入位置后，若是要捕获函数内局部变量，可以先调整 `locals` 参数值设置为 `PRINT` 输出可捕获变量
- PRINT：捕获但不注入，并将局部变量表打印到控制台
- CAPTURE_FAILSOFT：局部变量捕获失败则不注入，在控制台输出异常
- CAPTURE_FAILHEAD：局部变量捕获失败则抛出异常
- CAPTURE_FAILEXCEPTION：局部变量捕获失败则注入且包含一个异常声明
### 重写

使用 `@Overwrite` 注解标记的方法会替换原本方法
- 方法名、方法签名都必须一致
- 访问优先级不能低于原始访问优先级（private-protected-public）

```java
@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    // 重写 Minecraft#createTitle() 方法
    @Overwrite
    private String createTitle() {
        // do something
        return "Minecraft";
    }
}
```
### 局部修改

`@Redirect` 注解允许取消原先的某些方法调用，替换为被修饰方法
- 被修饰方法的返回值和形参列表与被调用的方法相同；若重定向的是父类构造，则需要该方法为 `static` 的
- `method`：修改方法名
- `at`：注入点，详见[[#@At]]，只允许 `INVOKE` 与 `FIELD` 类型

`@ModifyArg` 注解允许修改某个传入参数
- 被修饰方法返回值必须与形参类型相同；被修饰方法的形参为以下三种情况之一：
	- 被修改参数的原本值
	- target 指向目标的所有参数
	- target 指向目标的所有参数和目标方法的所有参数
- `method`：修改方法名
- `at`：注入点，详见[[#@At]]，只允许 `INVOKE` 类型
- `index`：修改的第 i 个对应类型的参数

`@ModifyArgs` 注解允许批量修改传入参数。被修饰函数返回值为 `void`，且第一个参数为 `Args` 类型参数，其余与 `@ModifyArg` 类似。

`@ModifyConstant` 用于修改 `LDC`，`*CONST_*`，`*IPUSH` 等操作码引用的常量
- `method`：修改方法名
- `constant`：匹配常量值

`@ModifyVariable` 用于修改局部变量
- 被修饰方法的返回值为被修改变量类型，只有一个形参也是被修改变量类型，其值为原始值
- `method`：修改方法名
- `at`：注入点
- `print`：输出所有局部变量
- `ordinal`：被修改局部变量在相同类型的变量的索引
- `index`：被修改局部变量在局部变量表中的索引
- `name`：局部变量名称
- `argsOnly`：若为 true，只匹配方法参数
### 隐式接口实现

理论上，在编译后的字节码中，同一个类中只要两个函数的签名不同就可以共存（但Java编译器和语法不支持），比如：

```java
// ALLATORIxDEMO(Ljava/lang/String;)V
public void ALLATORIxDEMO(String iIiIIIIiIi)
// ALLATORIxDEMO(Ljava/lang/String;)Ljava/lang/String;
public String ALLATORIxDEMO(String iIiIIIIiIi)
```

Mixin 通过在编译时添加一个前缀完成编译，并在合并时去除前缀的方法实现该种“重载”。
- `@Implements`：存储 `@Interface` 注解
- `@Interface`：要实现的接口
	- `iface`：指定要实现的接口
	- `prefix`：编译时的特征前缀，要求以 `$` 结尾

```java
// 带有重名但签名不同的函数
public interface IMinecraftMixin {
    public float getLimitFramerate();
}

// Mixin 类
@Mixin(Minecraft.class)
@Implements(@Interface(iface = IMinecraftMixin.class, prefix="example$"))
public abstract class MinecraftMixin {
    // 原本存在的方法
    @Shadow
    public abstract int getLimitFramerate();
    // 合并入的方法
    public float example$getLimitFramerate() {
        // do something
    }
}
```
### Mixin 类访问原始类成员
#### this

可以在 Mixin 类的方法中通过 this 拿到原始类对象，只需要强转就行，先转换成 `Object` 再转换成对应类型：

```java
@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    public void aFunction() {
        Minecraft mc = (Minecraft) (Object) this;
    }
}
```
#### super

为使用 super 关键字，可以将 Mixin 继承自原始类的父类

```java
@Mixin(GuiMainMenu.class)
public abstract class GuiMainMenuMixin extends GuiScreen {
}
```
#### 其他成员

在 Mixin 中访问原始类中的非公开成员可以通过 `@Shadow` 注解修饰被访问成员，要求被修饰成员与原始成员的变量签名或函数签名相同
- 函数：使用 `abstract` 修饰函数
- 变量：不需要初始化变量值

`@Shadow` 注解提供 `prefix` 属性，表示该名称有一个前缀，对应原始类中的成员需要去除该前缀。该功能的作用可以以另一种角度实现[[#使用#隐式接口实现]]的功能

```java
@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    // 引用 Minecraft$getLimitFramerate()
    @Shadow(prefix="example$")
    public abstract int example$getLimitFramerate();

    // 没问题。虽然原始类中存在同名函数，但实际签名不同（返回值不同）
    public float getLimitFramerate() {
        // do something
    }
}
```

对于 `final` 修饰的成员，需要同时使用 `@Final` 注解标注。如果要修改其值，应当同时使用 `@Mutable` 注解标注。
#### 非公有类型

若目标方法参数包含非公有类型，可以使用 `@Coerce` 注解，此时目标参数类型为实际类型的基类（不能是接口）。若实际类型为某接口，则应为 `Object`
#### 不存在的成员

有时我们会修改一些编译时不存在的成员，这些成员在运行时可能存在（比如其他 mod 注入的方法），我们可以使用 `@Dynamic` 注解
## 调试

- `mixin.debug`: 启用所有调试选项
- `mixin.debug.export`: 导出所有合并后的类文件至`.mixin.out`文件夹内，如果在类路径中存在`Fernflower`反编译器，还会输出反编译后的文件
- `mixin.debug.export.filter`: 一个用于指定要导出哪些类的过滤器，如果需要反编译，这个属性非常有用，因为反编译所有类需要消耗较长时间，支持以下模糊匹配规则：
    - `*` : 匹配一个或多个字符，除了点(`.`)
    - `**` : 匹配任意数量的字符
    - `?` : 匹配单个任意字符
- `mixin.debug.export.decompile`: 启用反编译，仅当类路径中存在`Fernflower`反编译器时有效
- `mixin.debug.export.decompile.async`: 启用独立的线程反编译，通常会大大缩短反编译导致的启动游戏的时间。如果启动中游戏崩溃，则可能会输出未反编译的类
- `mixin.debug.export.decompile.mergeGenericSignatures`: 如果启用反编译器，那么会合并注入类中的泛型签名，有时候会让某些进行泛型验证的子系统出现问题，启用此属性禁用泛型合并
- `mixin.debug.verify`: 检查Mixin是否正确注入，仅当调试Mixin库本身的时候才建议启用，用于调试使用Mixin的模组则不建议启用
- `mixin.debug.verbose`: 将`DEBUG`级别的日志消息降低为`INFO`级别，这样就能在运行时在控制台中显示更多信息
- `mixin.debug.countInjections`: 参见`@Inject.expect`属性文档
- `mixin.debug.strict`: 启用严格的检查
- `mixin.debug.strict.unique`: 如果启用，则`@Unique`方法在遇到相同方法签名的方法时会直接抛出异常终止游戏，而不是默认的仅在控制台打印警告
- `mixin.debug.strict.targets`: 启用对注入目标严格的检查
- `mixin.debug.profiler`: 启用性能分析
- `mixin.dumpTargetOnFailure`: 通常在Mixin使用时出现异常后会打印一些消息，有时候这些消息并不准确，通常是其他CoreMod对相关的目标进行了更改，启用此属性允许输出应用Mixin前的类文件到`.mixin.out`文件夹内，以确定出现异常的原因
- `mixin.checks`: 启用所有检查选项
- `mixin.checks.interfaces`: 检查使用`@Inplements`实现的接口，启用此属性，会在控制台输出一个报告，指示了哪些方法未实现，调用这些方法时，会抛出`AbstractMethodError`错误。报告会生成在`.mixin.out`文件夹下
- `mixin.checks.interfaces.strict`: 当启用了`mixin.checks.interfaces`，但未启用此属性，Mixin会跳过对目标类是抽象类的检查，如果启用了此属性，抽象类也将被检查
- `mixin.ignoreConstraints`: 启用此属性，会对注入注解中的`constraints`属性的检查出现异常后的抛出错误降级为仅在控制台输出一个警告
- `mixin.hotSwap`: 启用热加载(?)，如果用`-javaagent`参数加载Mixin的jar包，则会自动启用此属性
- `mixin.env`: 意义不明，Mixin并未使用过此属性，可能是仅为了设计模式而存在
- `mixin.env.obf`: 启用强制使用mcp name
- `mixin.env.disableRefMap`: 启用此属性会禁用映射表
- `mixin.env.remapRefMap`: 启用此属性需要手动指定映射表
- `mixin.env.refMapRemappingFile`: 手动指定一个映射到searge name的映射表，仅当`mixin.env.remapRefMap`启用时有效
- `mixin.env.refMapRemappingEnv`: 如果`mixin.env.refMapRemappingFile`指定的映射表不是映射到searge name的，需要设置此属性指定映射表指向notch name还是mcp name
- `mixin.env.allowPermissiveMatch`: 此属性默认启用，效果是当启用了`mixin.env.remapRefMap`属性时，只匹配目标的名称，不匹配目标的类型/参数类型，将此属性设置为`false`则禁用此行为
- `mixin.env.ignoreRequired`: 启用此属性则忽略所有注解的`require`属性设置
- `mixin.env.compatLevel`: 设置兼容的Java最低版本
- `mixin.env.shiftByViolation`: 当`@At.shift`属性设置得超出最大允许的值时，目前会抛一个警告，高版本Mixin可能会抛出错误，允许以下取值：
    - `ignore`: Mixin 0.7以前版本的默认值，忽略警告
    - `warn`: 当前的默认值，打印一个警告
    - `error`: 直接抛出一个错误
- `mixin.initialiserInjectionMode`: 设置初始化注入时的行为，默认为`default`，可以改为`safe`
# 反射
# 接口注入

Fabric 的 Injected Interfaces 允许将 Mixin 创建的对象接口直接应用到对应类上，无需将实例对象强转为接口再使用。

**注入的接口方法必须有默认实现，随便怎么实现都行**

> Forge 不支持，Architectury 支持 

1. 使用 Mixin 完成对原始类的修改，需要一个接口，详见[[#Mixin#使用#添加成员]]。称被修改类型为原始类型，实现的接口为 Mixin 接口。
	- 设 Mixin 接口类名为 mod.example.IMixinMinecraft
	- 设原始类为 net.minecraft.client.Minecraft
2. 在配置文件中配置（详见下方两个代码块，分别表示 Fabric 和 Architectury 的处理方式）
	- 包之间通过 `/` 分隔
	- 内部类与外部类之间通过 `$` 分隔
1. 每次完成注入，运行一次 `genSources` Gradle任务

```json
// Fabric: fabric.mod.json
{
    "custom": {
        "loom:injected_interfaces": {
            // net.minecraft.client.Minecraft
            "net/minecraft/class_310": ["mod/example/IMixinMinecraft"]
        }
    }
}
```

```json
// Architectury: architectury.common.json
{
    "injected_interfaces": {
        "net/minecraft/class_310": ["mod/example/IMixinMinecraft"]
    }
}
```
# ASM