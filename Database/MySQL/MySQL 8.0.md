开源免费数据库程序，属于关系型数据库（SQL数据库）

# 关系型数据库

RDBMS，建立在关系模型的基础上，由多张相互连接的二维数据表组成的数据库

优点
- 使用表存储，格式统一，易于维护
- 使用 SQL 操作，标准统一，使用方便

常见关系型数据库
- Oracle：大型数据库，收费
- MySQL：中小型数据库，包括社区版和收费版
- Microsoft SQL Server：中型数据库
- PostgreSQL：开源免费的中小型数据库
- SQLite：嵌入的微型数据库

# 配置

MySQL 8.0.33 在安装完成后会自动启动配置过程。

## 安装位置

默认下，MySQL 安装到 C 盘中。在安装时选择 Custom，添加组件时选中右侧组件，点选下方 `Advanced Options` 可设置每个组件的安装目录

## 端口

MySQL 默认端口为 3306

## 服务

MySQL 安装完成后会注册为系统服务，默认名称为 MySQL80，若需要退出 MySQL 可直接停止该服务

## root 管理员密码

需要设定 root 管理员密码，并在设置的最后一步要求验证

# 启动与停止

MySQL 的启动与停止可以有两种方法：
1. 在系统的服务中启动或停止 MySQL 安装后注册的服务，默认名称为 MySQL80
2. 使用命令行：（也是服务）

```bash
# 启动
net start mysql80

# 停止
net stop mysql80

# 重启（MySQL 中）
systemctl restart mysqld
```

# 连接

1. 可以通过 MySQL 自带的命令行（MySQL Command Line）或工作台（MySQL Workbench）连接
2. 其他连接工具或库通过 `localhost:3306` 访问，其中 `localhost` 为数据库地址地址，`3306` 为之前设置的端口 `mysql -h 地址 -P 端口 -u 用户名 -p`

# MySQL

- [[MySQL 数据类型]]
- [[MySQL 分页查询及DCL]]
- [[MySQL 函数]]
- [[MySQL 约束]]
- [[MySQL 存储引擎]]
- [[MySQL 索引]]
- [[MySQL 其他SQL语句优化]]
- [[MySQL 存储对象]]
- [[MySQL 锁]]
- [[MySQL InnoDB引擎]]
- [[MySQL 管理]]
- [[MySQL 日志]]
- [[MySQL 主从复制]]
- [[MySQL 分库分表]]
- [[MySQL 读写分离]]