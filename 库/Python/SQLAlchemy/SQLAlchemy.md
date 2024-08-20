Python 的 ORM 框架，主要有Engine 框架引擎、Connection Pool连接池、Dialect （DB API 类型）、Schema/Type 数据库架构类型、SQL 表达式语句五部分组成

* 开发效率高：SQLAlchemy 几乎不需要调用原生 SQL 语句
* 安全性强：ORM 最底层会自动修复一些安全问题，如 SQL 注入等
* 灵活性强：一套代码，可适配 SQLite，MySQL，Oracle，PostgreSQL 等多种关系型数据库，不用关心底层 SQL 语言细节

ORM：Object Relation Mapping，对象关系映射，解决面向对象语言与关系型数据库之间相互不匹配的技术，在提供持久化类与表的映射关系的情况下，将对象持久化到数据库中

![[Pasted image 20230730130431-20240513185636-9v81drv.png]]

```bash
pip3 install sqlalchemy
```

# 连接与绑定

根据使用的连接驱动不同，使用的连接字符串也不同

* MYSQL-Python：`mysql+mysqldb://user:pwd@host:port/dbname`
* pymysql：`mysql+pymysql://user:pwd@host:port/dbname?options`
* MYSQL-Connector：`mysql+mysqlconnector://user:pwd@host:port/dbname`
* `cx_Oracle`：`oracle+cx_oracle://user:pwd@host:port/dbname?k=v&k=v...`

根据连接字符串就可以创建引擎并绑定会话了

```python
from sqlalchemy import create_engine
# 创建 Engine
engine = create_engine(
    url='mysql+pymysql://root:...@localhost:3306/db1?charset=utf8mb4',
    # 连接池大小
    pool_size=10,
    # 超过连接池后，最大还允许创建几个连接
    max_overflow=0,
    # 连接超时，单位 秒
    pool_timeout=10,
    # 多久回收一次连接池的连接
    pool_recycle=1,
    # 是否列出执行的 SQL 语句
    echo=True
)

from sqlalchemy.orm import sessionmaker
from sqlalchemy.orm import scoped_session
# 绑定会话
Session = sessionmaker(bind=engine)
# 开启一个会话
session = scoped_session(Session)
```
