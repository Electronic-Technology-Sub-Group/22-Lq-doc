# Flask-SQLAlchemy

Flask-SQLAlchemy 包是 Flask 对 SQLAlchemy 的支持与封装
数据库连接地址详见 [[SQLAlchemy#Engine]]

```python
# 配置数据库信息
app.config['SQLALCHEMY_DATABASE_URI'] = 数据库连接地址
db = SQLAlchemy(app)
# 测试连接
with app.app_context():
    with db.engine.connect() as conn:
        rs = conn.execute('select 1')
        print(rs.fetchone())  # (1,)
```

SQLAlchemy 的具体方法见[[SQLAlchemy]]，注意很多操作都需要应用上下文（`with app.app_context()`）
- `declarative_base()` 等效于 `db.Model`
- 所有类型可以通过 `db.类型` 获取，约束也可以使用 `db.ForeignKey` 等
- `Base.metadata.create_all()` 相当于 `db.create_all()`
- `session.query(表类)` 可以使用 `表类.query`

# Flask-migrate

Flask-migrate 用于将 Flask-SQLAlchemy 创建的映射与数据库同步

```python
from flask_migrate import Migrate
migrate = Migrate(app, db)
```

1. 初始化 flask db 迁移脚本

```bash
flask db init
```

2. 修改表后，生成迁移脚本

```bash
flask db migrate
```

3. 运行迁移脚本，同步

```bash
flask db upgrade
```