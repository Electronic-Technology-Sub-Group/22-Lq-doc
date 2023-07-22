# 多表关系

## 一对多

在多的一方建立外键，指向一的一方

![[Pasted image 20230721005116.png]]

## 多对多

通过一张中间表实现，中间表至少存在两列，分别表示两张表的主键

![[Pasted image 20230721004739.png]]

## 一对一

直接使用主键，并将外键设为唯一约束（UNIQUE）

![[Pasted image 20230721005047.png]]

# 多表查询

从多张表中进行数据查询，只需要在 `from` 中将多张表全部填入，使用 `,` 分隔即可，查询结果为多张表的笛卡尔积。

若需要去除无效数据，仍在 `where` 字句中添加过滤条件。条件中 A 表的 a 列数据表示为 `A.a`

```sql
-- 查询 emp 与 dept 两张表
-- 去除无效数据条件：emp 中数据的 dept_id 值与 dept 表中的 id 值相等
select * from emp, dept where emp.dept_id = dept.id;
```

## 连接查询

![[Pasted image 20230721103915.png]]

### 内连接

查询表 A、B 的交集，即图中绿色部分

#### 隐式内连接

```sql
select 字段列表 from 表列表 where 连接条件;
```

#### 显式内连接

```sql
select 字段列表 from 表1 [inner] join 表2 on 连接条件;
```

### 外连接

- 左外连接：查询左表的所有数据及两张表的交集，即图中绿色+蓝色部分

```sql
select 字段列表 from 表1 left [outer] join 表2 on 连接条件;
```

- 右外连接：查询右表的所有数据及两张表的交集，即图中绿色+黄色部分

```sql
select 字段列表 from 表1 right [outer] join 表2 on 连接条件;
```

左、右外连接会查询所有左表数据，而右表数据无法找到关联数据时对应列为 `null`

左外、右外连接实际是等效的

### 自连接

自连接在 `from` 中为一张表赋予多个别名即可。以下只给出内连接语法，外连接同理。

```sql
select 字段列表 from 表A [as] 别名A join 表B [as] 别名B on 连接条件;
```

## 子查询

在 SQL 语句中嵌套 SELECT 查询语句，称为嵌套查询，又称子查询

```sql
任意可带有查询子句的语句 where/from/select 名 = (select ...)
```

子查询根据查询结果不同，可以分为：
- 标量子查询：子查询返回结果为单个值，可以用常用的比较运算进行比较

```sql
-- 查询销售部所有员工信息
--   1. 查询销售部的部门 id：select id from dept where name = '销售部'
--   2. 查询员工信息，条件为 dept_id = 1 中查查询出来的 id
select * from emp where dept_id = (select id from dept where name = '销售部');
```

- 列子查询：子查询返回多行，可用操作符为 `in`，`not in`，`any`，`some`，`all`
	- `in`：子查询中存在对应值
	- `not in`：子查询中不存在对应值
	- `any`，`some`：子查询中存在符合条件的值
	- `all`：子查询中所有值都符合条件

```sql
-- 查询销售部和市场部中所有员工信息
--   1. 查询销售部与市场部的 id：select id from dept where name in ('销售部', '市场部')
--   2. 查询员工信息，条件为 dept_id 在 1 中查询出的 id 列表中
select *
    from emp
    where dept_id in (select id from dept where name in ('销售部', '市场部'));

-- 查询比财务部任何人工资都高的员工信息
--   1. 查询财务部 id：select id from dept where name = '财务部'
--   2. 查询财务部所有人工资：select salary from emp where dept_id = (...)
--   3. 查询 salary>所有财务部工资 的员工：select * from emp where salary > all (...)
select *
from emp
where salary > all (select salary from emp where dept_id = (select id from dept where name = '财务部'));
```

- 行子查询：子查询返回一行，常用操作符有 `=`，`<>`，`in`，`not in` 等，多个关键字使用 `()` 组合

```sql
-- 查询薪资、上级都与 abc 相同的员工信息
--   1. 查询 abc 的薪资和上级 select salary, managerid from emp where name = 'abc'
--   2. 查询员工信息 (salary, managerid) = (...)
select *
from emp
where (salary, managerid) = (select salary, managerid from emp where name = 'abc');
```

- 表子查询：子查询返回多行多列，常用运算符为 `in`

```sql
-- 查询与 a 或 b 的薪资与部门相同的员工信息
--   1. 查询 a 或 b 的薪资与部门信息 select salary, dept_id from emp where name = 'a' or name = 'b'
--   2. 查询员工信息 where (salary, dept_id) in (...)
select *
from emp
where (salary, dept_id) in (select salary, dept_id from emp where name = 'a' or name = 'b');

-- 查询入职时间在 2006-01-01 之后的员工信息及部门信息
--   1. 查询 2006-01-01 之后员工 select * from emp where entrydate > '2006-01-01'
--   2. 将 1 查询的结果作为一张子表，与部门信息表进行联结查询 from (...) e left join ... 
select *
from (select * from emp where entrydate > '2006-01-01') e
         left join dept on dept.id = e.dept_id;
```

根据所在位置不同，可以分为 where 之后的子查询，from 之后的子查询，select 之后的子查询。
