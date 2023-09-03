# 逻辑存储结构

![[Pasted image 20230722000746.png]]
- 表空间 Tablespace：`.ibd` 文件。一个 MySQL 实例可以对应多个表空间，存储记录、索引等数据
- 段：Segment
	- 数据段：Leaf node segment，B+ 树的叶节点
	- 索引段：Non-leaf node segment 
	- 回滚段：Rollback segment
- 区：表空间的单元结构，默认每个区大小为 1M，包含 64 个连续页。InnoDB 申请内存空间以区为单位，为保证数据连续性，每次申请 4-5 个区
- 页：InnoDB 磁盘管理的最小单位，默认大小为 16K
- 行：InnoDB 引擎中的数据，除每条数据得的每个字段外，还包含以下内容：
	- `Trx_id`：最后一次事务操作的事务 id
	- `Roll_pointer`：每次写入时，会把旧版本记录到 `undo` 日志，该字段用于查找以前版本的数据
# 架构

![[Pasted image 20230810170030.png]]
## 内存结构

缓冲池 Buffer Pool 是主内存的一个区域，存储磁盘上经常操作的数据。执行 CRUD 操作时，先操作缓冲池数据，再以一定频率或条件回写到内存，减少磁盘 IO

缓冲池内部以页为单位，使用链表管理页
- free page：未被使用的空闲页
- clean page：未被修改过的数据
- dirty page：脏页，页中的内容被修改过，与硬盘中的内容不一致

### 更改缓冲区

MySQL 8.0 后的缓冲池中包含一个特殊区域 更改缓冲区 Change Buffer，在非唯一二级索引页中，执行 DML 语句时，若数据不在 BufferPool 中，会将数据变更存储在 ChangeBuffer 中，在未来读取数据时再合并到 Buffer Pool 中

二级索引通常非唯一，且插入、删除、更改操作的数据也是随机的，这些操作很可能影响到索引树中不相邻的其他二级索引页。更改缓冲区的存在允许将这些修改在缓冲池中合并处理，减少磁盘 IO
### 自适应 Hash

Adaptive Hash Index，用于优化对 Buffer Pool 的查询。InnoDB 会监控表上的索引页查询，若感知到使用 Hash 会提升效率，则自动建立 Hash 索引。

默认情况下，数据库的 `adaptive_hash_index` 变量为 ON，表示自适应哈希的功能是开启的
### 日志缓冲区

日志缓冲区 Log Buffer 保存要写入磁盘的日志记录，主要包含 `redo log` 和 `undo log`。

与日志缓冲区相关的参数变量有：
- `innodb_log_buffer_size`：日志缓冲区大小，默认 16MB
- `innodb_flush_log_at_trx_commit`：日志写入磁盘的时机
	- 1：默认，每次事务提交时写入并保存到磁盘
	- 0：每秒写入一次日志并刷新到磁盘
	- 2：每次事务提交时写入日志，每秒写入一次磁盘
## 磁盘结构
### 系统表空间

系统表空间 System Tablespace 主要存放更改缓冲区的数据
- 若表是在系统表空间而不是每个表文件或通用表空间中创建，可能包含表和索引的数据
- MySQL 5.x 版本还存储了 InnoDB 数据字典、`undo log` 等
- 参数 `innodb_data_file_path` 存储了系统表空间文件

### 独立表空间

File-Per-Table Tablespace。独立表空间开启时，每张表都有自己的独立表空间，表数据和索引不会存放到系统表空间中。
- `innodb_file_per_table`：是否开启独立表空间，默认 ON（开启）
### 通用表空间

General Tablespace，需要通过 `create tablespace` 手动创建，在创建表时可以指定使用该表空间

```mysql
create tablespace 表空间名 add datafile '关联文件名' engine = 存储引擎名;
create table 表名(...) ... tablespace 表空间名;
```
### 撤销表空间

Undo Tablespace，存储 `undo log` 日志，MySQL 会自动创建两个相同大小的撤销表空间（默认 16M），默认文件名为 `undo_001`，`undo_002`
### 临时表空间

Temporary Tablespace，存放用户创建的临时表等数据，包括会话临时表空间和全局临时表空间
### 双写缓冲区

Doublewrite Buffer Files，BufferPool 数据页写入磁盘时会先写到双写缓冲区中，便于系统异常时恢复数据，保证数据安全性，为两个 `.dblwr` 文件
### 重做日志

Redo Log，用于维护事务持久性，包括内存中的重做日志缓冲 redo log buffer 和重做日志文件 redo log。

每次事务提交后，都会将修改信息存入该日志中，用于在刷新脏页到磁盘发生错误时进行数据恢复
## 后台线程

将 InnoDB 缓冲区数据在合适的时机写入硬盘，通过相应的指令可以查看 InnoDB 相关线程情况

```mysql
show engine innodb status; 
```
### Master Thread

核心后台线程，负责内容包括：
- 调度其他线程
- 将缓冲池中的数据异步写入磁盘，保证数据一致性
- 脏页刷新、合并插入缓存、undo 页回收
### IO Thread

负责 AIO 处理 IO 请求时的回调，包括
- Read thread，负责读操作，默认 4 个
- Write thread，负责写操作，默认 4 个
- Log thread，负责将日志缓冲区数据写入磁盘，默认 1 个
- Insert buffer thread，付泽江写缓冲区数据写入磁盘，默认 1 个
### Purge Thread

回收事务已提交的 `undo log`
### Page Clear Thread

协助 `Master Thread` 刷新脏页数据，减轻 `Master Thread` 压力，减少阻塞
# 事务原理

![[Pasted image 20230810184943.png]]
## 重做日志

redo log，记录事务提交时数据页的物理修改，实现事务的持久性

![[Pasted image 20230810190029.png]]

当事务提交之后，所有修改信息会记录到重做日志中，用于刷新脏页到磁盘发生错误时进行数据恢复
- 将 redo log 写入磁盘是事务的一部分，写入失败会造成事务失败
- 不直接将数据写入磁盘而是写入日志，主要由于日志写入是追加，不涉及到随机 IO 操作，性能更高
## 回滚日志

undo log，记录数据被修改前的信息，提供回滚和 MVCC，保证数据库原子性

与 redo log 不同的是，redo log 记录了新数据的数据内容，属于物理日志；undo log 记录了与执行的操作相反的 SQL 语句，属于逻辑日志。当我们执行 `rollback` 时，可以方便的从 undo log 中找到相应内容并回滚。

undo log 在事务执行时产生，事务提交后不会立即删除，当不涉及 MVCC 后再删除。

undo log 以段方式记录和管理，存放于 rollback segment，内包含 1024 个 undo log segment。
# 多版本并发控制

MVCC，Multiple Version Concurrency Control，维护一个数据的多个版本，使读写无冲突
- 当前读：读取操作读的是记录的最新版本，且保证其他并发事务不能修改当前记录，需要对读取的事务加锁，包括：
	- 共享锁：select ... lock in share mode
	- 排他锁：select ... for update, update, insert, delete
- 快照读：读取的是当前数据的可见版本，不加锁，但不一定是最新版本，主要是简单的 select
	- Read Committed：每次 select 生成一个快照读
	- Repeatable Read：每次事务第一个 select 生成一个快照读
	- Serializable：快照读退化为当前读

MVCC 依赖于数据库的三个隐式字段、`undo log` 日志和 `readView`
## 原理

- 隐藏字段：InnoDB 每条数据都有三个隐藏字段：
	- `db_trx_id`：最近修改的事务 id，表示插入或最后一次修改该记录的事务 id
	- `db_roll_ptr`：回滚指针，指向记录的上个版本，通过 `undo log` 可快速回滚
	- `db_row_id`：隐藏主键，当表结构无主键时自动生成（仅无主键时产生）
- 回滚日志
	- 当执行 insert 语句时，undo log 只在回滚时需要，可以在事务提交后立即删除
	- 当执行 update，delete 语句时，undo log 需要在快照读中发挥作用，不能被立即删除
	- undo log 版本链：不同事务对相同记录进行修改，产生的 undo log 是一个链表结构，头部为最新的纪录，尾部是最早的记录
- readView：快照读 SQL 执行时 MVCC 提取数据的依据，记录并维护系统当前活跃（未提交）的事务 id，主要字段包括：

| 字段           | 含义                                |
| -------------- | ----------------------------------- |
| m_ids          | 当前活跃事务 id 集合                |
| min_trx_id     | 最小活跃事务 id                     |
| max_trx_id     | 预分配事务 id（当前最大事务 id +1） |
| creator_trx_id | ReadView 创建者事务 id              | 

访问规则：trx_id 表示待检查 db_trx_id 值
- trx_id == creator_trx_id：可访问该版本，数据是当前事务更改的
- trx_id < min_trx_id：可访问该版本，数据已经提交
- trx_id > max_trx_id：不可访问版本，当前事务在 ReadView 生成后才开启
- min_trx_id <= trx_id <= max_trx_id 且 trx_id 不在 m_ids 中：可访问该版本，数据已提交