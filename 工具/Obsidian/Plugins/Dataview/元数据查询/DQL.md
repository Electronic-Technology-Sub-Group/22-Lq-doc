# DQL

DQL 查询通用结构包括三部分：
1. 一个查询类型，后包含 0 个或几个参数
2. 0 个或多个数据来源，以 `from` 开头
3. 0 个或多个数据命令继续处理

````
```dataview
<query-type> <fields>
from <source>
<data-command> <expression>
<data-command> <expression>
```
````

> [!danger]
> 若无 `from` 指定查询来源，默认查询所有文档，当数据过多时容易造成无响应

> [!success]
> 类似 SQL，DQL 关键字不区分大小写，但查询内容区分大小写

DQL 类似于 SQL，与 SQL 执行方式不同，只是关键字和语法相似。

DQL 自上而下一行行执行，而不是将整个查询作为一条语句执行。`FROM` 选定数据所在文档后，通过若干条数据指令进行筛选。因此 `sort`，`group by`，`limit` 等语句都可以多次生效
# 查询类型

DQL 查询类型有四种：<font color="#9bbb59">TABLE</font>，<font color="#9bbb59">LIST</font>，<font color="#9bbb59">TASK</font>，<font color="#9bbb59">CALENDAR</font>

| 类型         | 说明             |
| ---------- | -------------- |
| `table`    | 查询一张二维表，后接每一列名 |
| `list`     | 查询一个列表         |
| `task`     | 查询一个待办任务列表     |
| `calendar` | 查询一个日历视图       |
## list

列表默认展示给定源中的所有文件及子目录中的文件

``````col
`````col-md
flexGrow=1
===
````
```dataview
list
from #dataview
```
````
`````
`````col-md
flexGrow=1
===
```dataview
list
from #dataview
```
`````
``````

`list` 后可以接受一个字符串值，表示后面展示的内容

``````col
`````col-md
flexGrow=1
===
````
```dataview
list "创建于 " + file.cday
from #dataview
```
````
`````
`````col-md
flexGrow=1.5
===
```dataview
list "创建于 " + file.cday
from #dataview
```
`````
``````

使用 `list without id` 可以隐藏列表 ID（文件名）

``````col
`````col-md
flexGrow=1.3
===
````
```dataview
list without id "创建于 " + file.cday
from #dataview
```
````
`````
`````col-md
flexGrow=1
===
```dataview
list without id "创建于 " + file.cday
from #dataview
```
`````
``````

使用 `group by` 进行分组，默认只显示类型名

``````col
`````col-md
flexGrow=1
===
````
```dataview
list
from #dataview
group by type
```
````
`````
`````col-md
flexGrow=1
===
```dataview
list
from #dataview
group by type
```
`````
``````

`list` 后加一个描述字符可以将分组后的项展开，`rows.file` 表示每项文件，通常会将文件链接加入作为目录使用

``````col
`````col-md
flexGrow=1
===
````
```dataview
list rows.file.link
from #dataview
group by type
```
````
`````
`````col-md
flexGrow=1
===
```dataview
list rows.file.link
from #dataview
group by type
```
`````
``````

也可以通过 `length(rows)` 配合 `list without id` 统计每一组文件数量，`key` 可以表示类型名

``````col
`````col-md
flexGrow=2
===
````
```dataview
list without id key + ": " + length(rows)
from #dataview
group by type
```
````
`````
`````col-md
flexGrow=1
===
```dataview
list without id key + ": " + length(rows)
from #dataview
group by type
```
`````
``````
## table

表 table 生成一个二维表，默认第一列为数据 id（文件名）

``````col
`````col-md
flexGrow=1
===
````
```dataview
table
from #dataview
```
````
`````
`````col-md
flexGrow=1
===
```dataview
table
from #dataview
```
`````
``````

多列查询，每一列使用 `,` 分隔，使用 `as` 设置别名，列名同样支持表达式

``````col
`````col-md
flexGrow=1
===
````
```dataview
table type, file.cday as 创建日期, 7
from #dataview
```
````
`````
`````col-md
flexGrow=1
===
```dataview
table type, file.cday as 创建日期
from #dataview
```
`````
``````
\
使用 `table without id` 可以取消第一列 File 列
## task

查询交互式任务列表，任务列表的状态变化会同步到源文件。

``````col
`````col-md
flexGrow=1
===
````
```dataview
task
from #dataview
```
````
`````
`````col-md
flexGrow=1
===
```dataview
task
from #dataview
```
`````
``````

通过数据指令可以进一步筛选，如仅显示未完成、根据 tag 筛选、分组等，`file` 可以访问文件本身。

> [!note]
> 子任务属于父任务，因此只要父任务符合条件，子任务也会被查询出来

``````col
`````col-md
flexGrow=2
===
````
```dataview
task
from #dataview
where !completed and contains(tags, "#tag1")
group by file.link
```
````
`````
`````col-md
flexGrow=1
===
```dataview
task
from #dataview
where !completed and contains(tags, "#tag1")
group by file.link
```
`````
``````

> [!note]
> 如果只有子任务符合条件，也会查询出父任务，但只会查询出符合条件的子任务


``````col
`````col-md
flexGrow=2
===
````
```dataview
task
from #dataview
where urgent
```
````
`````
`````col-md
flexGrow=1
===
```dataview
task
from #dataview
where urgent
```
`````
``````
## calendar

查询出一个日历，需要附带一个参数作为需要标记的时间
- 被标记的时间点会在日历对应格子下方加一个点
- 若给定参数不一定是一个日期，可以使用 `where` 数据指令进行过滤
- `sort`，`group by` 数据指令无效果

``````col
`````col-md
flexGrow=1
===
````
```dataview
calendar file.mday
where typeof(file.mday)="date"
```
````
`````
`````col-md
flexGrow=1
===
```dataview
calendar file.mday
where typeof(file.mday)="date"
```
`````
``````

# 数据指令

数据指令包括 <font color="#9bbb59">FROM</font>，<font color="#9bbb59">WHERE</font>，<font color="#9bbb59">SORT</font>，<font color="#9bbb59">GROUP BY</font>，<font color="#9bbb59">FLATTEN</font> 和 <font color="#9bbb59">LIMIT</font>
## from

确定数据来源
- 标签，包括子标签：`from #tag`
- 目录或文件，包括子目录：`from "path"`
- 链接：`from [[ link ]]`
	- 出链（链接到给定链接的文档链接）：`from outgoing([[ link ]])`
	- 当前文档：`[[]]` 或 `[[#]]`
- 组合：支持 `and` 和 `or`，否定使用 `!`
	- 支持括号，如 `#tag and ("folder" or !#other-tag)`
- 排除某种来源在前面使用 `-` ，如 `-#tag`

## where

筛选数据，使用 `where <condition>`，支持 `and` 和 `or`

- 筛选来自于 24 小时内修改过的文件

````
```dataview
list
where file.mtime >= date(today) - dur(1 day)
```
````

- 筛选（所在文件创建于）一个月前的未完成任务

````
```dataview
task
where !completed and file.ctime <= date(today) - dur(1 mo)
```
````
## sort

对查询出的数据进行排序，多个条件排序使用 `,` 分隔

````
```dataview
...
sort <field1> [ascending/asc/descending/desc], ...
```
````
## group by

分组

````
```dataview
group by <field>
group by (计算条件) by <name>
```
````

分组后，可以通过 `rows` 访问所有数据，通过 `rows.field` 和 `rows.name`  访问筛选出的数据（数组）
## flatten

将一个数组扁平化为多个条目（笛卡尔积）

``````col
`````col-md
flexGrow=1.3
===
````
```dataview
table flatten_value as value
from "_resources/codes/Dataview"
where length(flatten_value) > 0
flatten flatten_value
```
````
`````
`````col-md
flexGrow=1
===
```dataview
table flatten_value as value
from "_resources/codes/Dataview"
where length(flatten_value) > 0
flatten flatten_value
```
`````
``````

## limit

限制查询出的值数量

````
```dataview
...
limit 5
```
````
