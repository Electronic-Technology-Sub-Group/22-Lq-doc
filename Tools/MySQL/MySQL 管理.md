# 系统数据库

## mysql

存储了 MySQL 服务器正常运行所需的各种信息，包括时区、主从数据库、用户及权限等
## information_schema

提供访问数据库元数据的各种表和视图，包括数据库、表、字段类型及访问权限等
## performance_schema

为 MySQL 服务器运行时状态提供底层监控功能，收集数据库性能参数
## sys

包含一系列方便 DBA 及开发人员利用 performance_schema 进行性能调优和诊断的视图
# 常用工具
## mysql

指 mysql 客户端工具

```bash
mysql [options] [database]
```
- `-u`，`--user=用户名`：用户名
- `-p`，`--password[=密码]`：密码
- `-h`，`--host=IP或域名`：服务器
- `-P`，`--port=端口号`：端口
- `-e`，`--execute="SQL语句"`：只执行该 SQL 语句后退出
## mysqladmin

执行管理操作的客户端程序，用于检查服务器配置和当前状态、执行创建和删除数据库等操作

```bash
mysqladmin --help
```
## mysqlbinlog

查看 MySQL 运行时服务器生成的二进制日志

```bash
mysqlbinlog [options] file1 file2 ...
```
- `-d`，`--database=数据库名`：只列出指定数据库相关的操作
- `-o`，`--offset=行数`：忽略前 n 行日志
- `-r`，`--result-file=文件名`：将日志以纯文本形式保存到指定文件
- `-s`，`--short-form`：显示简单格式，会省略某些信息
- `--start-datatime=起始日期 --stop-datetime=终止日期`：指定查询日期
- `--start-position=起始行 --stop-position=终止行`：指定查询位置
## mysqlshow

客户端对象查找工具，用于查找存在的数据库、表、列、索引等及其统计信息

```bash
mysqlshow [options] [库名[表名[字段名]]]
```
- `--count`：显示数据库及表的统计信息
- `-i`：显示数据库或表的状态信息
## mysqldump

用于数据库备份、迁移等

```bash
mysqldump [options] 库名 [表名]
mysqldump [options] --database|-B 库1 库2 ...
mysqldump [options] --all-database|-A
```
- `-u`，`-p`，`-h`，`-P` 用于连接数据库
- `-add-drop-database`：在每个数据库创建语句前加上 `drop database` 语句
- `-add-drop-table`：在每个表创建语句前加上 `drop table`，默认开启
	- `--skip-add-drop-table`：禁用
- `-n`，`--no-create-db`：不包含数据库创建语句
- `-t`，`--no-create-info`：不包含表创建语句
- `-d`，`--no-data`：不包含数据
- `-T`，`--table=目录`：自动生成两个文件：sql文件存储表结构语句，txt文件存储表数据

除 `-T` 外，也可直接使用 `>` 重定向到待备份文件
## mysqlimport

该方法专门用于导入 `mysqldump -T` 生成的数据文件

```bash
mysqlimport [options] 库名 文件1 文件2 ...
```
## source

该方法专门用于执行 sql 文件，需要在 mysql 中调用

```mysql
source sql文件地址
```