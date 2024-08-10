Data Query Language，用来查询数据库中表的记录，使用 `select`。

```sql
select 字段列表 
from 表名列表
where 条件列表 
group by 分组字段列表 
having 分组后条件列表 
order by 排序字段列表;
```

执行顺序：

1. `from` 确定要查询表
2. `where` 确定查询条目
3. `group by` 完成分组
4. `having` 对分组后的值进一步筛选
5. `select` 决定查询的字段和聚合
6. `order by` 进行排序
# 基本查询

仅涉及到 `select` 与 `from` 的查询语句
* `select` 后的多个字段使用 `,` 分割；查询所有字段时使用 `*`
    * 尽量不使用 `*` ，效率不高，可读性差
* 字段和表可以设置别名，使用 `字段 as 别名`，`as` 可省略
    * 一旦为表设置别名，访问表只能使用别名而不能使用表名
* `distinct` 可用于去重，紧跟 `select` 之后

```sql
select [distinct] * from 表名 [[as] 表别名];
select [distinct] 字段1 [ [as] 别名1], 字段2 [ [as] 别名2], ... from 表名 [[as] 表别名];
```
# 条件查询

`WHERE` 子句用于限定选择的行，对列的值进行筛选

> [!warning]  
> 省略 `where` 条件通常表示修改/选择整张表所有数据

|运算符|功能|其他|
| -----------------| --------------------------| --------------------------------|
|>, <, >=, <=|大小比较||
| `==`, <>, !=|相等性比较| `<>` 与 `!=` 相同|
|between A and B| $\in [a,b]$ |包含边界|
|in(列表)|在列表中||
|like 占位符|可通过占位符匹配模糊匹配|_ 匹配单个字符，% 匹配任意字符|
| `is null`，`is not null` |数据是否为空||
|and &&|与关系||
|or \|\||或关系||
|not !|非关系||

```sql
select * from users where age = 88;
select * from users where age > 20 && age < 50;
select * from users where age between 20 and 50;
select * from users where id_card is null;
select * from users where age in (18, 20, 40);
-- 姓名为 2 个字
select * from users where name like '__';
select * from users where id_card like '%X';
```
# 分组查询

使用 `group by` 分组
## 聚合函数

将一列数据作为整体计算，作用于 `select` 之后字段名上

```sql
select 聚合函数(字段名) from 表名
```

聚合可以有别名，且聚合的别名可以直接参与后面的计算

|函数名|作用|
| ----------| -----------|
|count|条目数|
|max, min|最大/小值|
|sum, avg|和/平均数|
注意：所有 `null` 不参与聚合运算
## 分组查询

```sql
select 字段列表 from 表 [where 条件] group by 分组字段名 [having 分组后过滤条件];
```

* `where` 对表中的数据进行筛选，而 `having` 则针对分组后的数据进行筛选
* `where` 中不能使用聚合函数，`having` 则可以
* 查询返回的字段为分组和聚合的字段，查询其他字段无意义（通常只显示第一个）

```sql title:根据性别分组，查询男性员工与女性员工的平均年龄
-- 查询列：gender，age
-- 聚合：avg(age)
-- 分组：gender
select gender, avg(age) from employee group by gender;
```

```sql title:根据性别分组，查询男性员工与女性员工的性别
-- 查询列：gender，*
-- 聚合：count(*)
-- 分组：gender
select gender, count(*) from employee group by gender;
```

```sql title:'年龄 <45，根据工作地址分组，获取员工数量不少于 3 人的工作地址'
-- 查询列：address, *
-- 聚合：count(*) as address_count
-- 条件：age < 45
-- 分组：address
-- 分组后条件：address_count >= 3
select address, count(*) as address_count from employee where age < 45 group by address having address_count >= 3;
```
# 查询排序

使用 `order by` 对结果进行排序

```sql
select 字段 from 表 [where 条件] order by 字段1 排序方式1, 字段2 排序方式2, ...;
```

* 排序方式：升序 `asc` 或降序 `desc`，`asc` 可省略
* 多字段排序时，只有前面字段相同的才会按后面字段设置排序
## 联合查询

将多次查询的结果合并起来，形成查询结果集。

```sql
select 字段列表1 from 表1 ... -- 第一次查询
union [all]
select 字段列表2 from 表2 ... -- 第二次查询
```

* 所有字段列表列数必须一致，每次查询相同列的数据类型必须一致
* `union all` 表示直接合并，`union` 表示合并并去重
# 多表查询

```tabs
tab: 一对多
在多的一方建立外键，指向一的一方
<br/>

![[Pasted image 20230721005116-20240516032707-ebvusdm.png]]
tab: 多对多
通过一张中间表实现，中间表至少存在两列，分别表示两张表的主键
<br/>

![[Pasted image 20230721004739-20240516032718-ru2phr8.png]]
tab: 一对一
直接使用主键，并将外键设为唯一约束（UNIQUE）
<br/>

![[Pasted image 20230721005047-20240516032723-ko6p10b.png]]
```

从多张表中进行数据查询，只需要在 `from` 中将多张表全部填入，使用 `,` 分隔即可，查询结果为多张表的笛卡尔积。

若需要去除无效数据，仍在 `where` 字句中添加过滤条件。条件中 A 表的 a 列数据表示为 `A.a`

```sql
-- 查询 emp 与 dept 两张表
-- 去除无效数据条件：emp 中数据的 dept_id 值与 dept 表中的 id 值相等
select * from emp, dept where emp.dept_id = dept.id;
```
## 连接查询

![[Pasted image 20230721103915-20240516032733-38351wb.png]]
### 内连接

查询表 A、B 的交集，即图中绿色部分

````tabs
tab: 隐式内连接
```sql
select 字段列表 from 表列表 where 连接条件;
```

tab: 显式内连接
```sql
select 字段列表 from 表1 [inner] join 表2 on 连接条件;
```
````
### 外连接

````tabs
tab: 左外连接
查询左表的所有数据及两张表的交集，即图中绿色+蓝色部分
<br/>

```sql
select 字段列表 from 表1 left [outer] join 表2 on 连接条件;
```

tab: 右外连接
查询右表的所有数据及两张表的交集，即图中绿色+黄色部分
<br/>

```sql
select 字段列表 from 表1 right [outer] join 表2 on 连接条件;
```
````

左、右外连接会查询所有左表数据，而右表数据无法找到关联数据时对应列为 `null`

> [!note] 左外、右外连接实际是等效的
### 自连接

自连接在 `from` 中为一张表赋予多个别名即可。以下只给出内连接语法，外连接同理。

```sql
select 字段列表 from 表A [as] 别名A join 表B [as] 别名B on 连接条件;
```
## 子查询

在 SQL 语句中嵌套 `SELECT` 查询语句，称为嵌套查询，又称子查询

```sql
任意可带有查询子句的语句 where/from/select 名 = (select ...)
```

````tabs
tab: 标量子查询
子查询返回结果为单个值，可以用常用的比较运算进行比较
<br/>

```sql
-- 查询销售部所有员工信息
--   1. 查询销售部的部门 id：select id from dept where name = '销售部'
--   2. 查询员工信息，条件为 dept_id = 1 中查查询出来的 id
select * from emp where dept_id = (select id from dept where name = '销售部');
```

tab: 列子查询
子查询返回多行，可用操作符为 `in`，`not in`，`any`，`some`，`all`
  * `in`：子查询中存在对应值
  * `not in`：子查询中不存在对应值
  * `any`，`some`：子查询中存在符合条件的值
  * `all`：子查询中所有值都符合条件
<br/>

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

tab: 行子查询
子查询返回一行，常用操作符有 `=`，`<>`，`in`，`not in` 等，多个关键字使用 `()` 组合
<br/>

```sql
-- 查询薪资、上级都与 abc 相同的员工信息
--   1. 查询 abc 的薪资和上级 select salary, managerid from emp where name = 'abc'
--   2. 查询员工信息 (salary, managerid) = (...)
select *
from emp
where (salary, managerid) = (select salary, managerid from emp where name = 'abc');
```

tab: 表子查询
子查询返回多行多列，常用运算符为 `in`
<br/>

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

````
子查询可以在 `where`，`from` 或 `select` 之后。
