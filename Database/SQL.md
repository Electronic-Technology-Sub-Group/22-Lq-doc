Structured Query Language，结构化查询语言，专用于处理和访问数据库数据

SQL 语句中，关键字大小写不敏感，如 `SELECT` 与 `select` 等效；但对于具体数据大小写敏感，如 `id` 和 `ID` 表示不同的数据

若只有一条语句，句末 `;` 可省略

在 SQL 中，字符串可使用 `''` 或 `""` 包围

# 注释

SQL 使用 `--` 开头表示注释

```sql
-- 这是一行注释
```

# 查询

查询使用 `SELECT`，将查询结果保存到一个结果表中

```sql
SELECT 列名称 FROM 表
```

获取多个列数据使用 `,` 分隔

```sql
SELECT 列1, 列2, ... FROM 表
```

若要获取所有表中数据，列名称可被替换成 `*`

```sql
SELECT * FROM 表
```

## 条件

使用 `WHERE` 进行条件过滤

```sql
select 展示列 from 表 where 列 = 值
```

表示列出所有 `列 = 值` 的数据条目中 `展示列` 的值

```sql
select * from users where status = 0
```

## 统计个数

使用 `count(*)` 代替 `*` 表示统计结果数目

```sql
-- 统计 users 表中 status=0 的条目个数
select count(*) from users where status = 0
```

## 别名

使用 `AS` 可为查询结果设置列名

```sql
select username as name, password as pwd, status from users 
```

# 增加

## 插入数据

使用 `INSERT` 向表中插入数据

```sql
INSERT INTO 表名 (列名...) VALUES (数据名...)
```

多个列名和数据名之间用 `,` 分隔

```sql
insert into 表名 (列1, 列2, ...) values (值1, 值2, ...)
```

# 修改

使用 `UPDATE` 修改指定数据

```sql
update 表 set 更新列 = 新值 where 查询列 = 值
```

表示通过查询列确定行，然后将改行（或几行）某更新列数据的某值修改成新值

```sql
-- 将 users 表中 id = 7 的数据的 username 列更新为 az
update users set username = "az" where id = 7
```

若有多个列需要更新，使用 `,` 分隔

```sql
update 表 set 列1 = 值1, 列2 = 值2, ... where 列1 = 值1
```

**注意，若不写 `where`，将更新整张表**

# 删除

使用 `DELETE` 从表中删除数据

```sql
delete from 表 where 列 = 值
```

**注意，若不写 `where`，将删除整张表**

# 子句

子句用于选择需要操作的行

## WHERE

`WHERE` 子句用于限定选择的行，即前面 `insert`，`update`，`delete` 等操作中 `where` 及之后的部分

### 运算符

`where` 子句支持的运算符除了普通的比较运算符外，还有 `<>` 与 `!=` 都表示不等于，`BETWEEN` 表示在某个范围内，`LIKE` 表示匹配某种模式

```sql
select * from users where status = 1
select * from users where id > 2
select * from users where username != 'admin'
```

多条语句之间使用 `AND` 与 `OR` 连接，相当于 `&&` 和 `||`

```sql
select * from users where status = 0 and id < 10
```

## ORDER

`ORDER` 用于对选择的行进行排序，默认为升序排序，`DESC` 表示降序，`ASC` 表示升序

```sql
order by 列 [asc | desc]
```

如

```sql
-- 查询所有 status=0 的数据，并对结果按 username 列升序排序
select * from users where status = 0 order by username
```

若要根据多个条件进行排序，使用 `,` 分隔

```sql
-- 查询所有 status=0 的数据，并按 name 列进行升序排序，之后按 id 列进行降序排序
select * from users where status = 0 order by name, id desc
```
