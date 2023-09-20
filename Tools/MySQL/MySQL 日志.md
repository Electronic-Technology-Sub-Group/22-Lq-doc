# 错误日志

记录 MySQL 启动、停止和运行时发生的所有严重错误的信息，Linux 下默认位于 `/var/log/mysqld.log`，可通过 `log_error` 变量查询

![[Pasted image 20230919172551.png]]
# 二进制日志

二进制日志 BINLOG 记录数据库变更，即所有 DDL、DML 语句，但不记录 SELECT，SHOW 等查询语句

二进制日志用于
- 灾难时数据恢复
- 主从复制

通过 `%log_bin%` 可查询二进制日志文件相关参数
- `log_bin`：是否开启
- `log_bin_basename`：二进制日志文件名前缀
- `log_bin_index`：当前数据库关联的二进制日志文件，可通过 `show master status` 查看

![[Pasted image 20230919173134.png]]

通过 `%binlog_format%` 可查询二进制日志格式，更换后需要重启 MySQL 服务：
- `STATEMENT`：基于 SQL 语句日志。记录的是每一条操作的 SQL 语句
- `ROW`：基于行日志（默认）。记录的是每一行数据变更
- `MIXED`：混合模式，以 `STATEMENT` 为主，特殊情况下切换至 `ROW`

二进制日志通过 `mysqlbinlog` 工具查看，详见 [[MySQL 管理工具#mysqlbinlog]]

二进制日志删除相关命令有以下几种，都在 mysql 命令行中执行：
- `reset master`：清空所有日志，且日志编号将从 1 重新开始
- `purge master logs to binlog.[n]`：清空 n 之前的所有日志
- `purge master logs before 'yyyy-mm-dd HH:mm:ss'`：清空指定日期前的所有日志

`binlog_expire_log_seconds` 变量记录了二进制日志的过期时间（单位秒），MySQL 默认过期时间为 30 天
# 查询日志

查询日志记录所有 DQL 与 DDL 日志，相关系统参数为 `general_log`

![[Pasted image 20230919174611.png]]

默认查询日志关闭，通过 `general_log=1` 开启。
# 慢查询日志

详见 [[MySQL 索引#慢查询日志]]

