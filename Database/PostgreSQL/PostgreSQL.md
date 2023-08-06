[PostgreSQL: The world's most advanced open source database](https://www.postgresql.org/)
PostgreSQL 是一个开源对象关系型数据库，与 MySQL 的区别在于：
- MySQL 只有配合 InnoDB+NDB 才符合 ACID 合规性（原子性、一致性、隔离性、持久性），PostgreSQL  则在所有配置都支持
- PostgreSQL 支持并发控制 MVCC，没有读写锁
- PostgreSQL 支持更多的索引类型和微调
- PostgreSQL 支持将数据存储为具有属性的对象，支持继承关系等
- PostgreSQL 支持更多数据类型，如几何、枚举、地址、数组、XML、组合等
- PostgreSQL 支持实例化视图，可以预先存储一定的值
- PostgreSQL 支持除 SQL 外的语言编写的存储过程
- PostgreSQL 支持 instead of 触发器
- PostgreSQL 支持多种扩展插件

选择：
- PostgreSQL 适合更频繁写入和复杂查询的应用，MySQL 适合读取数据更频繁的应用
	- PostgreSQL 内置 MVCC，没有写锁，写入效率更高
	- PostgreSQL 每次读取都会拉起一个新的系统进程，提供大约 10MB 的内存，需要内存密集资源扩展多个用户查询；而 MySQL 则每个用户为单一进程，查询效率高于 PostgreSQL
- PostgreSQL 更加复杂，需要更复杂的基础设置和问题排查
# 检查

通过以下命令创建一个 book 数据库并查看扩展包是否正确安装

```bash
createdb book
psql book -c "SELECT '1'::cube;"
```

PostgreSQL 默认监听端口为 5432，可通过 psql 工具连接。由于安装过程中出现问题，没有实际使用。

PostgreSQL 提供 psql 工具进入控制台直接执行 SQL命令，`\h` 查看 SQL 命令信息，`\?` 查看 psql 帮助信息
# 查询语言

PostgreSQL 支持的查询的查询语言包括 PL/pgSQL（SQL 的 PostgreSQL 方言），Tel，Perl，Python，同时社区还有 Ruby，Java，PHP，Scheme 等各种语言的扩展模块
# 关系与 CRUD

PostgreSQL CRUD 使用的也是一般的 SQL 语句

- `raise` 可以直接向命令行输出内容，类似 `select 内容` 或者 printf
```postgresql
-- #% 表示参数占位
raise notice '... #% ...' 参数
```

- check约束：建表时可以校验数据
```postgresql
create table xxx {
    postal_code varchar(9) check (postal_code <> '')
}
```

- 插入并返回：插入数据后可以返回插入的这条数据的某几列
```postgresql
insert ... values ... returning 列名;
```

- 建表时，自动创建主键索引

- 插入数据允许使用子表查询
```postgresql
insert ... values (..., (select ... from ... where ...));
```

## 窗口函数

类似 `group by`，允许对多行执行聚合函数，但允许使用内置聚合函数，不要求对每个字段单独分组成行

## 事务

```postgresql
-- 发起事务并提交
begin transaction
 ...
end;

-- 发起事务但回滚
begin transaction
 ...
rollback;
```
