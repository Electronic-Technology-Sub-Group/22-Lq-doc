MyCat 是一个基于 Java 的 MySQL 数据库中间件，通过伪装服务实现以操作 MySQL 的方式操作 MyCat
# MyCat 1.6

MyCat 1.6 解压后，其目录结构为：
- bin：MyCat 可执行文件
- conf：配置文件
- lib：依赖包。MySQL 驱动也在这个目录中
- logs：日志

MyCat 1.6 需要一台单独的服务器运行以链接多台 MySQL 服务器。

![[Pasted image 20230920013618.png]]

MyCat 本身不存储数据，而是根据分片规则，将逻辑库到逻辑表关联到对应的节点主机上，每个节点主机是一台具体的 MySQL 服务器。MyCat 服务器默认端口号为 8066

分片规则对应 `conf/schema.xml`，其中逻辑表的 `rule` 属性为分片规则，对应 `rule.xml` 下的相关配置。

```xml
<mycat:schema xmlns:mycat="http://io.mycat/">
    <!-- 逻辑库 -->
    <schema name="逻辑库名" checkSQLschema="true" sqlMaxLimit="100">
        <!-- 逻辑表 -->
        <table name="逻辑表名" dataNode="数据节点名，逗号分隔" rule="auto-sharding-long">
    </schema>
    <!-- 数据节点 -->
    <dataNode name="数据节点名" dataHost="节点主机名" database="节点数据库名" />
    <!-- 节点主机 -->
    <dataHost name="节点主机名" maxCon="100" minCon="10" balance="0" writeType="0" dbType="mysql" dbDriver="jdbc">
        <heartbeat>select user()</heartbeat>
        <writeHost href="master 与主从复制有关" url="jdbc:mysql://...." user="用户名" password="密码"/>
    </dataHost>
</mycat:schema>
```

用户权限相关配置在 `conf/server.xml` 中：

```xml
<user name="用户名" defaultAccount="true">
    <property name="password">密码</property>
    <property name="schema">可访问逻辑库名</property>
    <!-- 只读，默认 false -->
    <property name="readOnly">true</property>
</user>
```
## 配置
### schema.xml

配置 MyCat 中逻辑库、逻辑表、分片规则、分节点及数据源等信息

- schema 标签：定义逻辑库，对应 MySQL 中 database

| 属性           | 类型   | 说明                                          |
| -------------- | ------ | --------------------------------------------- |
| name           | 字符串 | 逻辑库库名                                    |
| checkSQLschema | 布尔   | 当 SQL 操作指定了数据库名，执行时是否自动去除 |
| sqlMaxLimit    | 数字   | 未指定 limit 查询时，列表查询模式查询的记录数 | 

- schema/table 子标签：定义逻辑表，对应 MySQL 中 table

| 属性       | 类型        | 说明                                                                                            |
| ---------- | ----------- | ----------------------------------------------------------------------------------------------- |
| name       | 字符串      | 表名                                                                                            |
| dataNode   | 字符串/引用 | 所属 dataNode 名，多个使用 `,` 分割                                                             |
| rule       | 字符串/引用 | 分片规则，`rule.xml` 中对应规则的引用。仅分表时省略                                             |
| primaryKey | 字符串      | 对应真实表的主键                                                                                |
| type       | 字符串/枚举 | 逻辑表类型，默认普通表，`global` 表示全局表，用于数据字典表，每个节点中的数据都相同，且同步更新 | 

- dataNode 标签：定义数据节点，实现数据分片

| 属性     | 类型        | 说明           |
| -------- | ----------- | -------------- |
| name     | 字符串      | 数据节点名     |
| dataHost | 字符串/引用 | 关联节点主机名 | 
| database | 字符串      | 使用的数据库   |

- dataHost 标签：直接定义具体数据库实例

| 属性          | 类型        | 说明                                             |
| ------------- | ----------- | ------------------------------------------------ |
| name          | 字符串      | 数据库名                                         |
| maxCon/minCon | 数字        | 最大/小连接数                                    |
| balance       | 数字/枚举   | 负载均衡策略，可取值为 0 1 2 3                   |
| writeType     | 数字/枚举   | 操作分发方式，可选为 0（顺序分发） 1（随即分发） | 
| dbDriver      | 字符串/枚举 | 数据库驱动，可选 `native`, `jdbc`                |
### rule.xml

定义分片时拆分表的规则，主要包含 tableRule 和 function 标签

- tableRule 标签：定义分片规则
	- name 属性：规则名
	- rule 子标签：使用规则
		- column 子标签：分片字段，根据该字段值进行分片
		- algorithm 子标签：使用的规则，与 Function 标签 name 属性相对应


- function 标签：定义分片规则对应 Java 类和配置文件
	- name 属性：规则函数名
	- class 属性：规则对应 Java 类名
	- property 子标签：属性
		- name 属性：属性名
		- 值：参数所在文件（相对于 conf 目录）
### server.xml

MyCat 运行时的信息

- system 标签：系统规则
- user 标签：用户及用户权限
	- name 属性：用户名
	- defaultAccount 属性：布尔，是否是默认用户
	- property 子标签：具体参数，有一个 name 属性

| property name | 说明                         |
| ------------- | ---------------------------- |
| password      | 密码                         |
| schemas       | 可访问逻辑库，多个以逗号分隔 |
| readOnly      | 布尔，是否只读（默认 false） |

- user/privileges 子标签：表级 DML 权限
	- check 属性：布尔，是否开启检查，默认 false
	- schema 子标签：配置指定逻辑库权限
		- name 属性：数据库名
		- dml 属性：四个二进制数组成的字符串，分别对应增改查删的权限
		- table 子标签：配置指定逻辑表的权限
			- name 属性：数据表名
			- dml 属性：单独的权限，若此处未配置使用 schema 的 dml 属性值（就近原则）
## 分片规则

- 范围：根据指定字段及其配置范围与数据节点的映射决定属于哪个分片
	- 规则名：`auto-sharding-long`
	- 配置：`autopartition-long.txt`，默认 500W 一个表，最多 3 个表
- 取模：针对节点值与某个值（节点数量）进行取模运算
	- 规则名：`mod-long`
	- 配置：`count` 值表示节点数量
- 一致性 Hash：对键的值取 hash 值，根据 hash 值进行分表
	- 规则名：`sharding-by-murmur`
	- 配置：`count` 节点数量
- 枚举分片：根据特定字段值为一个枚举进行分片
	- 规则名：`sharding-by-intfile-enumstatus`
	- 配置：
		- `defaultNode`：默认值
		- `partition-hash-int.txt`，内每行为 `枚举值=分片索引` 结构，索引从 0 开始
- 应用指定：根据某个字段字符串的子串作为分片号
	- 规则名：`sharding-by-substring`
	- 属性：
		- startIndex：开始索引，从 0 开始
		- size：长度
		- partitionCount：分片数量
		- defaultPartition：默认分片（无效字符串使用该值）
- 固定分片 hash：对分片字段转换成二进制后取低10位（<1024）后按范围选择
	- 规则：`sharding-by-long-hash`
	- 参数：
		- `partitionCount`：分段数
		- `partitionLength`：每段长度
![[Pasted image 20230920115127.png]] 

- 字符串 hash 解析：针对字符串取子字符串，取子字符串 hash 后按固定分片
	- 规则：`sharding-by-stringhash`
	- 参数：其他参数与 固定分片 hash 相同
		- `hashSlice`：`m:n` 表示截取 m 到 n 的，0 出现在 n 位表示总长度，-n 表示倒数第 n 个
- 按日分片：对于时间类型，按日进行分片
	- 规则：`sharding-by-data`
	- 参数：`dataFormat`：时间格式（如 yyyy-MM-dd）
		- `sBeginData`，`sEndData`，`mPartionDay`：从 `sBeginData` 到 `sEndData`，每隔 `mPartionDay` 天一个分片
- 按月分片：对于时间类型，按月进行分片
	- 规则：`sharding-by-month`
	- 参数：类似按日分片，没有 `mPartionData`
## 读写分离

读写分离主要使用 `dataHost` 标签的相关配置实现。首先配置好主从复制，然后在 MyCat 中配置：

```xml
<dataHost balance="0" ...>
    <writeHost host="master" url="..." ...>
        <readHost host="slave" url="..." ... />
    </writeHost>
</dataHost>
```

读写分离在 `dataHost` 标签主要设置以下几个属性：
- `balance`：负载均衡策略，可取 0-3
	- 0：不开启读写分离，所有读写操作都应用到可用的 `writeHost` 节点
	- 1：全部 `readHost` 节点与备用 `writeHost` 节点参与 select 语句的负载均衡，第一台主 `writeHost` 节点用于写操作
	- 2：所有读写操作随机在 `readHost`，`writeHost` 上分发
	- 3：所有读操作随机分发到 `writeHost` 标签内的 `readHost` 标签中，`writeHost` 不承担读操作
- `writeType`：写操作配置，可取 0 1
	- 0：写操作全部转发到第一台 writeHost，writeHost 失效切换到第二台 writeHost
	- 1：所有写操作随机发送到 writeHost
- `switchType`：（写操作）自动切换设置，1 表示自动切换，-1 表示不自动切换
## 双主双从模式

![[Pasted image 20230921012422.png]]

双主双从模式中，两个主服务器、主从服务器之间都可以互相复制。两台主服务器分别为 Master1、Master2，两个从服务器分别为 Slave1、Slave2。

![[Pasted image 20230921012732.png]]

通常来说，Master1 与 Master2 互为备机，二者中有一台作为主机处理所有写操作，剩下一台主机和两台从机负责读操作。

- 主从复制设置（MySQL）：
	- Master1 复制到 Slave1
	- Master2 复制到 Slave2
	- Master1 与 Master2 之间互相复制
- 读写分离设置（MyCat）：

```xml
<schema>
    <dataHost balance="1" writeType="0" switchType="1">
        <writeHost host="master1">
            <readHost host="slave1"/>
        </writeHost>
        <writeHost host="master2">
            <readHost host="slave2"/>
        </writeHost>
    </dataHost>
</schema>
```

测试：
1. 查询及更新操作是否可进行分离、分离策略是否正确
2. 主库挂掉后是否可自动切换
## 管理与监控

![[Pasted image 20230920121453.png]]

MyCat 执行一条 SQL 语句的过程如下，1-5 步与 6-8 步均在 MyCat 处理
1. SQL 解析
2. 分片分析
3. 路由分析
4. 读写分离分析
5. 发送到具体 MySQL 服务器执行 SQL 语句
6. 结果合并
7. 聚合处理
8. 排序、分页等

MyCat 默认开通的两个端口 8066 和 9066 中，9066 为数据库管理端口，具有服务管理控制功能，用于 MyCat 整个集群状态。
## mycat

9066 端口连接后，可通过以下命令查看统计信息

| 命令              | 作用                                            |
| ----------------- | ----------------------------------------------- |
| show @@help       | 查看 MyCat 管理工具帮助文档（所有 show 命令等） |
| show @@version    | 查看 MyCat 版本                                 |
| show @@config     | 查看 MyCat 配置                                 |
| show @@datasource | 查看 MyCat 数据源信息                           |
| show @@datanode   | 查看 MyCat 分片节点信息                         |
| show @@threadpool | 查看 MyCat 线程池信息                           |
| show @@sql        | 查看执行的 SQL                                  |
| show @@sql.sum    | 查看执行的 SQL 统计                             |
## mycat-web

MyCat-web 即 Mycat-eye 是对 MyCat 服务器提供监控服务，通过 JDBC 对远程 MyCat 和 MySQL 服务器（仅限于 Linux）监控，包括数据库使用和系统的 CPU、内存、网络、磁盘等。

MyCat-web 需要 Zookeeper 前置。安装后可以在端口（默认 8082）下 `localhost:8082/mycat` 进行管理

