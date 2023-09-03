# 分页查询

使用 `limit` 分页

```mysql
select 字段 from 表 [where 条件] limit 起始索引, 每页记录数;
```

- 起始索引：从 0 开始的索引数，通常计算方法为 `(页码-1)*每页记录数`
- 若起始索引为 0 则可省略
- `limit` 执行顺序在 `order by` 之后

# DCL

## 用户控制

### 查询

MySQL 中，所有用户信息及权限保存在 `mysql` 数据库的 `user` 表中

```mysql
use mysql;
select * from user;
```

查询该表可以获得所有用户及用户的各种权限
- Host：主机地址，表示当前用户仅能在所在主机访问数据库
- User：用户名

### 创建

```mysql
create user '用户名'@'主机名' identified by '密码';
```

### 修改密码

```mysql
alter user '用户名'@'主机名' identified with 加密方式 by '新密码';
```

- MySQL 加密方式默认为 `mysql_native_password`

### 删除

```mysql
drop user '用户名'@'主机名';
```

*三种操作的主机名都可以使用 `_` 及 `%` 通配符进行匹配*

## 权限控制

	MySQL 定义了多种用户权限，常用命令都有与之对应的权限
- `ALL` 或 `ALL PRIVILEGES`：所有权限
- `SELECT`：查询数据
- `INSERT`：插入数据
- `UPDATE`：修改数据
- `DELETE`：删除数据
- `ALTER`：修改表
- `DROP`：删除数据库、表或视图
- `CREATE`：创建数据库或表
- `USAGE`：登陆权限

### 查询

```mysql
show grants for '用户名'@'主机名';
```

### 授予

```mysql
grant 权限列表 on 数据库名.表名 to '用户名'@'主机名';
```

### 撤销

```mysql
revoke 权限列表 on 数据库名.表名 from '用户名'@'主机名';
```

*所有的数据库名和表名都可以使用 `*` 通配符*

