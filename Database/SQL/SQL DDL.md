Data Definition Language，数据定义语言，定义数据库对象（数据库、表、字段）等

# 数据库操作

## 查询

- 显示所有数据库

```sql
show databases;
```

![[Pasted image 20230718121715.png]]

- 查询当前数据库，未选中时返回 NULL

```sql
select database();
```

![[Pasted image 20230718121859.png]]

## 创建

```sql
create database [if not exists] 数据库名 [default charset 字符集] [collate 排序规则];
```

- `if not exists`：仅当数据库不存在时创建；如果不加，当存在同名数据库时产生异常
- `default charset 字符集`：设置默认字符集。默认使用 utf8mb4
- `database` 还可以使用 `schema` 替代

## 删除

```sql
drop database [if exists] 数据库名;
```

## 使用

选定之后操作的数据库

```sql
use 数据库名;
```

# 表操作

## 查询

使用 `show` 可查询使用的数据库的所有表及某张表的建表语句

```sql
show tables;
```

![[Pasted image 20230718132159.png]]

```sql
show create table 表明;
```

![[Pasted image 20230718132232.png]]

使用 `desc` 则可以查询表结构

```sql
desc 表名;
```

![[Pasted image 20230718132302.png]]

## 创建

```sql
create table 表名(
    字段1 类型 [约束] [comment "字段1注释"],
    字段2 类型 [约束] [comment "字段2注释"],
    字段3 类型 [约束] [comment "字段3注释"],
    ...
) [comment "表注释"];
```

## 修改

修改表主要使用 `alter` 命令

向表中添加字符使用 `add`

```sql
alter table 表名 add 字段名 类型 [comment 注释] [约束]; 
```

修改字段类型使用 `modify`

```sql
alter table 表名 modify 字段名 新类型;
```

若要修改整个条目信息，使用 `change`

```sql
alter table 表名 change 旧字段名 新字段名 字段类型 [comment 注释] [约束];
```

删除字段使用 `drop`

```sql
alter table 表名 drop 字段名;
```

重命名表名使用 `rename`

```sql
alter table 表名 rename to 新表名;
```

# 删除

删除表有 `drop` 和 `truncate`

```sql
drop table [if exists] 表名;
```

使用 `truncate` 可以删除表并创建一个新表（可用于清空表数据）

```sql
truncate table 表名;
```
