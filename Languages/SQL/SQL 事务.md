事务：向数据库同时提交或撤销的一组操作的集合。事务请求所包含的所有操作要么全部成功，要么全部失败

一般来说，一条 SQL 就是一个事务，事务自动提交。

# 操作

## 提交方式

设置提交方式仅对当前会话有效

```sql
-- 查看事务提交方式
--   1 自动提交
--   0 手动提交
select @@autocommit;

-- 设置手动提交
set @@autocommit = 0;
```

## 提交/回滚

```sql
-- 提交事务
commit;
-- 回滚事务
rollback;
```

## 开启事务

在不修改会话事务提交方式的情况下，可以通过 `start transaction` 或 `begin` 手动开启一个事务

```sql
-- 开启事务
-- 或 begin;
start transaction;

-- 执行 SQL 语句

-- 提交
commit;
-- 回滚
rollback;
```

# 特性

ACID
- 原子性：**A**tomicity，在一次任务中，事务不可再次分割，要么全部成功，要么全部失败
- 一致性：**C**onsistency，事务完成时，所有数据必须保持一致状态
- 隔离性：**I**solation，数据库系统提供隔离机制，保证事务在不受外部并发操作影响的独立环境下运行
- 持久性：**D**urability，事务一旦提交或回滚，其对数据库数据的改变是永久的

# 并发

## 并发问题

- 脏读：一个事务读到另一个事务尚未提交的数据
- 不可重复读：一个事务先后读取同一条记录，但两次数据不同
- 幻读：一个事务按条件查询数据时，没有对应的数据行，但插入数据时，该行数据却已经存在

## 隔离级别

不同事务操作隔离级别不同，以解决不同问题

- Read uncommitted：读未提交
	- 可能出现脏读，不可重复读，幻读
- Read committed：Oracle 等默认，读已提交
	- 可能出现不可重复读，幻读
- Repeatable Read：MySQL 默认
	- 可能出现幻读
- Serializable：串行化，解决所有问题

```sql
-- 查看当前事务隔离级别
select @@transaction_isolation;

-- 设置事务隔离级别
--   session: 仅对当前会话有效
--   global：对全局有效
set [session | global] tansaction isolation level 隔离级别;
```

事务隔离级别越高，数据越安全，但性能越低（串行化已经无法并行提交了）
