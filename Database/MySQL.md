开源免费数据库程序，属于关系型数据库（SQL数据库）

# 数据组织结构

## 数据库 Database

## 表 Table

## 行 Row

## 字段

### 类型

MySQL 常用数据类型如下：

- INT：整型
	- TINYINT(n)：最最大为 n 位的整型，通常使用 `TINYINT(1)` 表示布尔
- VARCHAR(n)：长度最大为 n 的字符串

每个字段还有一个特殊标志，常见如下：

- PK：Primary Key，主键
- NN：Not Null，值非空
- UQ：Unique，值唯一，不可重复
- AI：Auto Increment，自动递增