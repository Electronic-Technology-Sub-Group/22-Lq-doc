Data Manipulation Language，对数据库中的数据进行增加、删除、修改等
# insert

向表中添加数据

```sql
-- 完整语法
insert into 表名 [(字段1, 字段2, 字段3, ...)] values (值1, 值2, 值3, ...)[, (值1, 值2, 值3, ...), ...];
-- 为表中所有字段依次赋值
insert into 表名 values (值1, 值2, 值3, ...);
-- 为表中所有字段依次赋 2 组值
insert into 表名 values (值1, 值2, 值3, ...), (值1, 值2, 值3, ...);
```

* 省略列名表示插入全部列并按顺序对应
* `values` 之后可以接多组数据
* 字符串和日期类型字面量使用引号包围
# update

修改表中数据，`where` 字句详见条件查询

```sql
update 表名 set 字段1=值1, 字段2=值2, ... [where 条件];
```

# delete

删除一行数据

```sql
delete from 表名 [where 条件]; 
```

> [!note] 无法删除某个字段的值，仅删除某字段值使用 `update` 将值修改为 `null`
