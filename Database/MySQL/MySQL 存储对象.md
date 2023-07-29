# 视图

视图：View，一种虚拟存在的**表**，不在数据库中真实存在。视图的行和列来源于自定义视图中使用的表，并在使用视图时动态生成（只保存查询逻辑，不保存查询结果）

基表：基础表，视图中使用的表

一般来说，视图的作用有以下几个：
- 简化：简化数据量和查询条件，突出重点数据
- 安全：通过视图，可以使指定用户只能查询和修改其可见的数据
- 隔离：通过视图，屏蔽真实表结构变化（主要是字段名变化）带来的影响

## 创建

```mysql
create [or replace] view 视图名称[(列1, 列2, ...)] as select语句 from 表名 [with [cascaded | local] check option]
```
- `or replace`：当视图存在时替换视图，否则执行错误
- 视图可以看作一张虚拟表，因此 from 的表名可以是视图名
- `with check option`：通过视图进行数据变更时进行检查，修改后需要符合 select 语句

创建完成后，可以通过 `show create view` 查看创建语句

```mysql
show create view 视图名;
```

## 查询

查询视图数据类似查表

```mysql
select ... from 视图名称 ...;
```

## 修改与删除

```mysql
-- 创建或替换，这里主要是通过替换原视图达到修改的目的
create or replace view ...;
-- 修改
alter view 视图[(字段名...)] as select语句 ...;
-- 删除
drop view [if exists] 视图名[, 视图名...];
```

## 更新

可以在视图上执行 `insert`，`update` 等操作数据的指令，其实际修改的是基表数据

视图的行与基础表必须存在一比一的对应关系，满足以下任意一项则视图无法被修改（更新）：
- 聚合函数，窗口函数（sum，min，max，count 等）
- distinct
- group by
- having
- union

同时，更新数据还需要满足 `check option` 的要求

- `cascaded`：默认，强制要求满足该视图及之前依赖链的所有视图的条件
 ![[Pasted image 20230729145819.png]]

```mysql
create view v1 as id, name from a_table where id <= 20;
create view v2 as id, name from v1 where id >= 10 with cascaded check option;
create view v3 as id, name from v2 where id <= 15;

-- 通过，v1 没有加 check option
insert into v1 values (50, 'A');

-- 通过，15 在 10-20 之间
insert into v2 values (15, 'B');
-- 不通过，5 < 10
insert into v2 values (5,  'C');
-- 不通过，30 > 20，v2 加入 with check option 后前面的依赖必须满足
insert into v2 values (30, 'D');

-- 通过，18 为加验证且在 10-20 之间
insert into v3 values (18, 'E');
-- 不通过，5 < 15 但不在 10-20 范围之间
insert into v3 values (5,  'F');
```

- `local`：要求满足该视图的条件，之前依赖的视图若有 `check option` 则需要满足其条件，否则不需要满足

![[Pasted image 20230729145916.png]]

# 存储过程

存储过程：事先编译并存储在数据库中的一段 SQL 语句的集合（类似一个函数），是数据库 SQL 语言层面上的代码封装与重用

特点：
- 封装，复用
- 支持参数和返回值
- 减少数据库与服务器之间的网络传输，提升效率

创建：

```mysql
create procedure 存储过程名称(参数)
begin
  -- 需要执行的 SQL 语句，注意不能省略 ;
end;
```

- 注意：使用 SHELL 命令行创建时，需要先使用 `delimiter 新符号或字符串` 更改下结束语句的符号，并将 `end` 后的分号更换为新符号。`delimiter` 不影响 `begin` 与 `end` 之间的语句

删除：

```mysql
drop procedure [if exists] 存储过程名;
```

使用：

```mysql
call 存储过程名称(参数);
```

创建后，可通过内置表查找所有的存储过程（包含系统和自定义的），并通过 `show` 查看存储过程创建语句：

```mysql
-- 显示所有存储过程
select * from INFORMATION_SCHEMA.ROUTINES where ROUTINE_SCHEMA = '存储过程名';
-- 查看存储过程定义
show create procedure 存储过程名;
```

## 变量

### 系统变量

MySQL 服务器提供的变量，`session|global` 省略时默认为 `session`
	- 使用 `show [session|global] variables [like ...]` 查看所有（名称满足条件的）系统变量
	- 使用 `select @@[session.|global.]系统变量名` 查看指定系统变量
	- 使用 `set [@@][session|global] 系统变量名 = 值` 设置系统变量

**注意：即便修改了全局变量，下次重启 MySQL 也会恢复到默认情况。永久更改需要更改 `my.conf` 配置文件**

### 用户变量

用户自己定义的变量，作用域为当前会话。用户变量通过 `@变量名` 使用，不需要声明。当不存在时返回 null

```mysql
-- 赋值
set @变量名 = 值;
set @变量名 := 值;
select @变量名 := 值;
select 字段名 into @变量名 from 表名 where ...;
-- 查看
select @变量名;
```

### 局部变量

局部变量使用 `declare` 声明，变量类型详见[[MySQL 数据类型]]，其有效范围在其所在的 `begin...end` 块中：

```mysql
-- 声明变量
declare 变量名 变量类型 [default 默认值];
-- 变量赋值
set 变量名 = 值;
set 变量名 := 值;
select 字段名 into 变量名 from 表名 where ...;
```

### 参数传递

```mysql
create procedure 过程名([in|out|inout] 变量名 类型， ...)
begin
  -- ...
end;
```

- in：仅输入，默认，参数仅作传入参数
- out：仅输出，改变后会改变原变量，但不能读取值
- inout：既输入又输出

## 流程控制

### if

```mysql
if 条件1 then
  -- SQL 语句
elseif 条件2 then
  -- SQL 语句
else
  -- SQL 语句
end if;
```

### case

```mysql
case 值
    when 值1 then SQL语句1;
    when 值2 then SQL语句2;
    ...
    else SQL语句;
end case;
```

```mysql
case
    when 条件1 then SQL语句1;
    when 条件2 then SQL语句2;
    ...
    else SQL语句;
end case;
```

### while

```mysql
while 条件 do
  -- SQL 语句
end while;
```

### repeat

当 until 的条件为 True 时跳出循环

```mysql
repeat
  -- SQL 语句
until 条件 end repeat;
```

### loop

通过控制语句控制循环
- `leave`：跳出循环，`break`
- `iterate`：结束本次循环，`continue`

```mysql
[标签:] loop
  -- SQL 语句
  -- 需要有 leave 或 iterate 控制退出循环
  leave 标签;
  iterate 标签;
end loop [标签];
```

## 条件处理程序

条件处理程序 Handle 可以用来自定义流程控制语句中遇到问题时对应的处理步骤（异常处理？）

```mysql
declare 动作 handler for 条件1, 条件2, ... 其他语句;
```
- 动作：当触发条件时进行的动作
	- continue：继续执行程序（`iterate`）
	- exit：终止执行当前程序（`leave`）
- 条件：触发条件
	- sqlstat 状态码：检查 SQL 执行的状态码
	- sqlwarning：被所有以 01 开头的 SQL 状态码触发
	- not found：被所有以 02 开头的 SQL 状态码触发
	- sqlexception：被所有不以 01 和 02 开头的 SQL 状态码触发
- 其他语句：触发动作之前的语句，（`finally`）

## 游标

Curosr 游标是一种特殊的类型，用于查询结果集。游标内的数据可以被过程和函数循环处理.

游标类型为 cursor，**且需要在其他类型变量之后声明**，从声明到使用结束的一般流程如下：

```mysql
-- 0. 声明其他变量：变量1, 变量2, ...
declare ...;
-- 1. 声明游标
declare 游标名称 cursor for 查询语句;
-- 2. 声明 02000 开头的条件处理(或 not found)结束时关闭游标
declare exit handler for not found close 游标名称;
-- 3. 打开游标
open 游标名称;
-- 4. 查询记录
while true do
  fetch 游标名称 into 变量1, 变量2, ...;
  -- 4. 使用变量执行 SQL 语句
-- 5. 关闭游标
close 游标名称;
```

## 存储函数

存储函数：有返回值的存储过程，但要求参数只能是 `in` 类型，使用 `create function` 创建

```mysql
create function 函数名称(参数列表) returns 返回值类型 特征
begin
  -- SQL 语句
  return 结果;
end;
```
- 参数类型不带 `in` 修饰
- 特征：必须包含至少一个，空格分割
	- `deterministic`：相同的输入参数必定返回相同结果（纯函数）
	- `no sql`：不包含 SQL 语句
	- `reads sql data`：包含查询语句，但不修改数据

# 触发器

触发器：与表有关的对象，在 insert，update，delete 之前或之后触发并执行触发器定义的 SQL 语句集合（钩子函数 or 事件监听？），并提供 `OLD` 和 `NEW` 用于引用发生变化的数据内容
- insert：NEW 表示新插入数据，OLD 无用
- update：NEW，OLD 分别表示新旧数据
- delete：OLD 表示要删除数据，NEW 无用

触发器仅支持行级触发（即影响了几行就出发几次），不支持语句级触发

```mysql
-- 创建触发器
create trigger 触发器名 before|after insert|update|delete on 表名 for each row
begin
  -- 待执行 SQL 语句
end;
```

创建的所有触发器可通过 `show triggers;` 查看，并使用 `drop` 删除

```mysql
-- 查看触发器
show triggers;
-- 删除触发器
drop trigger [数据库名.]触发器名;
```
- 数据库名默认使用当前使用的数据库