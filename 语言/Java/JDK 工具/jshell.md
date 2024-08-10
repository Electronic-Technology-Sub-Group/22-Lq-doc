#java9 

![[Pasted image 20240806131511.png]]

JShell 是 Java 的一个 REPL 命令行环境，位于 `${jdk安装目录}$/bin/jshell.exe` ，并使用 `--start` 指定启动参数
* `--start DEFAULT`：仅导入默认的几个 import
* `--start PRINTING`：额外静态导入 `System.out.print`, `System.out.println`, `System.out.printf`

JShell 环境下，执行 Java 语句不必要输入 `;`。除直接执行 Java 语句外，常用的命令有：

|命令|说明|
| ---------| --------------------------------|
|help|查看 JShell 命令的帮助|
|exit|退出 jshell|
|vars|列出所有变量|
|list|查看已输入过的代码片段|
|drop|移除已执行的代码|
|edit|打开一个 GUI，可以批量执行代码|
|imports|查看所有的导入（`import` 语句）|
|save|保存代码|
|open|导入代码|

‍
