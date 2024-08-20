Groovy 脚本的依赖管理器 *Groovy Adaptable Packaging Engine*，可以声明对应位置需要哪些依赖
- group：所有匹配 `/groovy\[x\]\[\\\.\.\*\]^/` 的包被保留
- module：maven id 或 ivy artifact
- version：版本或版本区间
- classifier：可选 classifier，如 jdk13

```groovy
@Grab(group='org.springframework', module='spring-orm', version='3.2.5.RELEASE')
import org.springframework.jdbc.core.JdbcTemplate

@Grab('org.springframework:spring-orm:3.2.5.RELEASE')
import org.springframework.jdbc.core.JdbcTemplate
```

可使用 classifier 属性

```groovy
@Grab(group='net.sf.json-lib', module='json-lib', version='2.2.3', classifier='jdk15')
```

在 shell 中，使用 grab 方法

```groovy
groovy.grape.Grape.grab(group:'org.springframework', module:'spring', version:'2.5.6')
```
# 仓库与代理

使用 GrabResolver 设置仓库

```groovy
@GrabResolver(name='restlet', root='http://maven.restlet.org/')
@Grab(group='org.restlet', module='org.restlet', version='1.1.6')
```

或通过修改参数设置

```bash
groovy 
 # 代理设置
 -Dhttp.proxyHost=yourproxy -Dhttp.proxyPort=8080 
 # 仓库地址
 -Dgrape.root=/repo/grapes
 # 脚本文件
 yourscript.groovy
```

或 Java 参数

```bash
JAVA_OPTS = -Dhttp.proxyHost=yourproxy -Dhttp.proxyPort=8080
```
# 依赖传递排除

使用 GrabExclude 排除依赖传递

```groovy
@Grab('net.sourceforge.htmlunit:htmlunit:2.8')
@GrabExclude('xml-apis:xml-apis')
```
# JDBC

JDBC 需要添加到系统类加载器

```groovy
@GrabConfig(systemClassLoader=true)
@Grab(group='mysql', module='mysql-connector-java', version='5.1.6')
```
# Logging

将 `groovy.grape.report.downloads` 设为 `true`（如 -Dgroovy.grape.report.downloads=true）可以看到依赖下载的过程

提高 ivy 的 log 等级，可以看到任何细节：-Divy.message.logger.level=4
# 命令行

- grape install
- grape list
- grape uninstall
