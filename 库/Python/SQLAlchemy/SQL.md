# SQL

* 若一次查询以 `filter()` 结尾，通过 `__str__` 可获取其 SQL 语句
* 通过 `session.execute()` 可直接执行 SQL 语句，返回一个 cursor 对象
