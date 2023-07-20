Data Query Language，数据查询语言，用来查询数据库中表的记录，使用 `select`。

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

- `select` 后的多个字段使用 `,` 分割；查询所有字段时使用 `*`
	- 尽量不使用 `*` ，效率不高，可读性差
- 字段可以设置别名，使用 `字段 as 别名`，`as` 可省略
- `distinct` 可用于去重，紧跟 `select` 之后

```sql
select [distinct] * from 表名;
select [distinct] 字段1 [ [as] 别名1], 字段2 [ [as] 别名2], ... from 表名;
```

# 条件查询

`WHERE` 子句用于限定选择的行，对列的值进行筛选

```ad-warning
省略 `where` 条件通常表示修改/选择整张表所有数据
```

| 运算符                   | 功能                     | 其他                           |
| ------------------------ | ------------------------ | ------------------------------ |
| >, <, >=, <=             | 大小比较                 |                                |
| `==`, <>, !=             | 相等性比较               | `<>` 与 `!=` 相同              |
| between A and B          | $\in [a,b]$              | 包含边界                       |
| in(列表)                 | 在列表中                 |                                |
| like 占位符              | 可通过占位符匹配模糊匹配 | _ 匹配单个字符，% 匹配任意字符 |
| `is null`，`is not null` | 数据是否为空             |                                |
| and &&                   | 与关系                   |                                |
| or \|\|                  | 或关系                   |                                |
| not !                    | 非关系                   |                                |

```sql
select * from users where age = 88;
select * from users where age > 20 && age < 50;
select * from users where between 20 and 50;
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

| 函数名   | 作用      |
| -------- | --------- |
| count    | 条目数    |
| max, min | 最大/小值 |
| sum, avg | 和/平均数 | 

注意：所有 `null` 不参与聚合运算

## 分组查询

```sql
select 字段列表 from 表 [where 条件] group by 分组字段名 [having 分组后过滤条件];
```

- `where` 对表中的数据进行筛选，而 `having` 则针对分组后的数据进行筛选
- `where` 中不能使用聚合函数，`having` 则可以
- 查询返回的字段为分组和聚合的字段，查询其他字段无意义（通常只显示第一个）

实例1 根据性别分组，查询男性员工与女性员工的平均年龄
- 查询列：`gender`，`age`
- 聚合：`avg(age)`
- 分组：`gender`

```sql
select gender, avg(age) from employee group by gender;
```

实例2 根据性别分组，查询男性员工与女性员工的性别
- 查询列：`gender`，`*`
- 聚合：`count(*)`
- 分组：`gender`

```sql
select gender, count(*) from employee group by gender;
```

实例3 查询年龄小于 45 岁的员工，根据工作地址分组，获取员工数量不少于 3 人的工作地址
- 查询列：`address`, `*`
- 聚合：`count(*) as address_count`
- 条件：`age < 45`
- 分组：`address`
- 分组后条件：`address_count >= 3`

```sql
select address, count(*) as address_count from employee where age < 45 group by address having address_count >= 3;
```

# 查询排序

使用 `order by` 对结果进行排序

```sql
select 字段 from 表 [where 条件] order by 字段1 排序方式1, 字段2 排序方式2, ...;
```

- 排序方式：升序 `asc` 或降序 `desc`，`asc` 可省略
- 多字段排序时，只有前面字段相同的才会按后面字段设置排序
