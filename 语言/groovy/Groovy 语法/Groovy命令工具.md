# Groovy Complier

groovyc：Groovy 编译器，作用与 javac 相同，将源代码编译为 class 文件

| 参数             | 完整参数                | 描述                                                         |
| :--------------- | :---------------------- | :----------------------------------------------------------- |
| @argfile         |                         | 指定源文件                                                   |
| -cp              | -classpath, --classpath | 第一个参数，指定源文件                                       |
|                  | --sourcepath            | 指定源文件目录                                               |
|                  | --temp                  | 指定临时目录                                                 |
|                  | --encoding              | 指定编码                                                     |
|                  | --help                  | 显示帮助                                                     |
| -d               |                         | 指定 class 文件输出位置                                      |
| -v               | --version               | 查看编译器版本                                               |
| -e               | --exception             | 发生异常时，查看异常栈                                       |
| -j               | --jointCompilation*     | 启用联合编译                                                 |
| -b               | --basescript            | 用于脚本，设置脚本名                                         |
| -indy            | --indy                  | 启用 invokedynamic 支持，要求 Java 7+                        |
|                  | --configscript          | 脚本高级编译                                                 |
| -Jproperty=value |                         | 若启用联合编译，传给 javac 的参数                            |
| -Fflag           |                         | 若启用联合编译，传递给 javac 的 flag                         |
| -pa              | --parameters            | 为方法参数的反射生成 metadata，需要 java8+（？Generates metadata for reflection on method parameter names.） |
| -pr              | --enable-preview        | 启用 Java 预览功能 (JEP 12) (jdk12+ only).                   |

## Ant Task

https://docs.groovy-lang.org/latest/html/documentation/groovyc-ant-task.html#ThegroovycAntTask-groovyc

## Gant

https://github.com/Gant/Gant

## Gradle

http://www.gradle.org/

## Maven

- GMavenPlus
- GMaven/Gradle
- Groovy-Eclipse 插件
- Maven 的 Ant 插件

## GMaven & GMavenPlus

GMaven 是 Groovy 的原始 Maven 插件，支持 Groovy 类和脚本编译，已被 GMavenPlus 替代

GMavenPlus 和 GMaven2 都在开发中

## 联合编译

Java 与 Groovy 混用，共同编译，使用 -j 命令

## Android 支持

```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:2.1.2'
        classpath 'org.codehaus.groovy:groovy-android-gradle-plugin:1.0.0'
    }
}

apply plugin: 'groovyx.android'

dependencies {
    compile 'org.codehaus.groovy:groovy:2.4.7:grooid'       // requires the grooid classifier
    compile ('org.codehaus.groovy:groovy-json:2.4.7') {     // no grooid version available
        transitive = false                                  // so do not depend on non-grooid version
    }
}
```



# Groovy Shell

groovysh，解释运行 Groovy 代码的命令行工具

- 不需要 go 命令执行缓冲区
- 跨平台，历史记录等，依赖于 JLine2
- ANSI 颜色
- 命令系统，包括联机帮助
- 个人资料支持

## 命令行选项

```bash
./bin/groovysh --help

Usage: groovysh [options] [...]
The Groovy Shell, aka groovysh, is a command-line application which allows easy
access to evaluate Groovy expressions, define classes and run simple
experiments.
  -C, --color[=<FLAG>]    Enable or disable use of ANSI colors
      -cp, -classpath, --classpath
                          Specify where to find the class files - must be first
                            argument
  -d, --debug             Enable debug output
  -D, --define=<name=value>
                          Define a system property
  -e, --evaluate=<CODE>   Evaluate the code first when starting interactive session
  -h, --help              Display this help message
  -pa, --parameters       Generate metadata for reflection on method parameter names
                            (jdk8+ only)
  -pr, --enable-preview          Enable preview Java features (JEP 12) (jdk12+ only)
  -q, --quiet             Suppress superfluous output
  -T, --terminal=<TYPE>   Specify the terminal TYPE to use
  -v, --verbose           Enable verbose output
  -V, --version           Display the version
```



## 表达式计算

支持简单表达式和多行表达式，类等所有 Groovy 代码

## 命令

```bash
groovy:000> :help

For information about Groovy, visit:
    http://groovy-lang.org

Available commands:
  :help      (:h ) Display this help message
  ?          (:? ) Alias to: :help
  :exit      (:x ) Exit the shell
  :quit      (:q ) Alias to: :exit
  import     (:i ) Import a class into the namespace
  :display   (:d ) Display the current buffer
  :clear     (:c ) Clear the buffer and reset the prompt counter
  :show      (:S ) Show variables, classes or imports
  :inspect   (:n ) Inspect a variable or the last result with the GUI object browser
  :purge     (:p ) Purge variables, classes, imports or preferences
  :edit      (:e ) Edit the current buffer
  :load      (:l ) Load a file or URL into the buffer
  .          (:. ) Alias to: :load
  :save      (:s ) Save the current buffer to a file
  :record    (:r ) Record the current session to a file
  :history   (:H ) Display, manage and recall edit-line history
  :alias     (:a ) Create an alias
  :set       (:= ) Set (or list) preferences
  :grab      (:g ) Add a dependency to the shell environment
  :register  (:rc) Register a new command with the shell
  :doc       (:D ) Open a browser window displaying the doc for the argument

For help on a specific command type:
    :help <command>
```

- help：帮助
- exit：退出
- import：导入
- grab：依赖
- display：显示缓冲区
- clear：清空缓冲区
- show：显示定义的变量，类，参数，导入等
  - variables
  - classes
  - imports
  - preferences
  - all
- inspect：显示 GUI 对象浏览器
- purge：清除，可选参数与 show 相同
- edit：在其他编辑器编辑当前缓冲区
- load：将文件读入缓冲区
- save：保存缓冲区到文件
- record：保存会话到文件
  - start
  - stop
  - status
- history：历史记录
  - shou
  - recall
  - flush
  - clear
- alias：别名
- doc：从浏览器打开类的文档
- set：首选项

## 首选项

使用 set 或 := 设置

- interpreterMode：是否允许使用 def 等声明动态类型
- verbosity：shell 输出等级，默认 INFO
  - DEBUG
  - VERBOSE
  - INFO
  - QUIET
- color：shell 颜色，默认 true
- show-last-result：显示执行后的结果，默认 true
- sanitize-stack-trace：清理堆栈跟踪，默认 true
- editor：edit 使用的编辑器，默认系统变量 EDITOR 值

命令

- set：设置首选项
- show preferences：显示所有首选项
- purge preferences：恢复默认值

## 用户配置

- $HOME/.groovy/groovysh.profile

  shell 启动时调用

- $HOME/.groovy/groovysh.rc

  shell 进入交互状态时调用

- $HOME/.groovy/groovysh.history

  历史记录

## 自定义命令

使用 register 注册自定义命令，自定义命令对应类应继承 CommandSupport 类

```groovy
import org.codehaus.groovy.tools.shell.CommandSupport
import org.codehaus.groovy.tools.shell.Groovysh

class Stats extends CommandSupport {
    protected Stats(final Groovysh shell) {
        super(shell, 'stats', 'T')
    }

    public Object execute(List args) {
        println "Free memory: ${Runtime.runtime.freeMemory()}"
    }

}
```

```bash
register stats
stats
# Free memory: xxx
```

## 故障排除

### JLine DLL

当 Windows 下 JLine2 出现问题时，可使用 --terminal 禁用

```bash
groovysh --termianl=none
```

- none
- false
- off
- jline.UnsupportedTerminal

### Cygwin

使用 Cygwin 运行 groovy 时，可能会出问题，使用以下方法可能解决

```bash
stty -icanon min 1 -echo
groovysh --terminal=unix
stty icanon echo
```



### GMavenPlus Maven

使 Groovy Shell 或 Groovy Console 支持自动绑定 Maven 项目

### Gradle Groovysh

Gradle 插件，使 gradle 任务绑定到 Groovy Shell

# Groovy Console

使用 Swing 制作的 Groovy Shell

## 基础

- 通过 groovyConsole 启动
- 有一个输入和一个输出区域
- 使用 Actions-Run 运行脚本

## 特点

### 参数

```bash
./bin/groovyConsole --help
Usage: groovyConsole [options] [filename]
The Groovy Swing Console allows a user to enter and run Groovy scripts.
      --configscript=PARAM   A script for tweaking the compiler configuration options
      -cp, -classpath, --classpath
                             Specify where to find the class files - must be first
                               argument
  -D, --define=<name=value>  Define a system property
  -h, --help                 Display this help message
  -i, --indy                 Enable InvokeDynamic (Indy) compilation for scripts
  -pa, --parameters          Generate metadata for reflection on method parameter
                               names (jdk8+ only)
  -pr, --enable-preview             Enable preview Java features (JEP 12) (jdk12+ only)
  -V, --version              Display the version
```
### 运行

- Ctrl+Enter 或 Ctrl+R 运行，相当于 Run Script
- 使用 View-Capture Xxx 打开或关闭输入输出流
### 编辑

File 菜单的 Open、Save、New File 等
### 历史

- Action-Inspect Last 查看
- 控制台会记录最近的 10 个脚本，从 Edit-Next/Previous 查看，快捷键 Ctrl+N/P
- 最后一个语句绑定到变量 _
- 每次运行结果绑定到变量 __，可以通过 [] 访问
### 中断

使用 interrupt 中断，若线程没有处理中断，需要 Script-Allow interruption

### 其他

- 字体大小：Action Menu-Smaller/Lager Font
- 可作为 Applet 运行，位于 groovy.ui.ConsoleApplet
- 回车时，代码自动缩进
- 可拖拽打开文件
- 可从 Script 菜单添加 jar  或目录改变脚本类路径
- 允许异常
- 可通过 Script-Enable Indy Compilation 启用 Invoke Dynamic（Idny） 编译模式
## 嵌入式控制台

```java
Console console = new Console();
console.setVariable("var1", getValueOfVar1());
console.setVariable("var2", getValueOfVar2());
console.run();
```
