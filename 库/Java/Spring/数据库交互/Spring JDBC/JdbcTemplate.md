# JdbcTemplate

`JdbcTemplate` 负责管理 `Connection`，`Statement` 和 `ResultSet`，捕获 JDBC 异常转化为易于理解的异常，批处理操作等。

```java
@Bean 
public JdbcTemplate jdbcTemplate(DataSource dataSource) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate();
    jdbcTemplate.setDataSource(dataSource);
    return jdbcTemplate;
}
```

之后，在 `Dao` 类中使用 `JdbcTemplate` 的 `update` 方法执行 SQL 语句即可。SQL 语句中使用 `?` 替代待传入参数，在 `PreparedStatement` 中根据位置传入参数，从 1 开始。

* `PreparedStatementCreator`：提供 `Connection`，执行 SQL 语句
* `KeyHolder`：在插入 SQL 语句时自动生成的键，默认实现是 `GeneratedKeyHolder`

  可以通过 KeyHolder 获取 GENERATED_KEY 获取主键自增的主键，返回值是一个 BigInteger

```java
private static final String SQL_CREATE_FIXED_DETAIL =
        "insert into fixed_deposit_details(ACCOUNT_ID, FD_CREATION_DATE, AMOUNT, TENURE, ACTIVE) values (?, ?, ?, ?, ?)";

@Autowired
private JdbcTemplate jdbcTemplate;

public int createFixedDetail(FixedDepositDetails fdd) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    // 返回值是本次调用影响数据库的行数
    return jdbcTemplate.update(conn -> {
        PreparedStatement ps = conn.prepareStatement(SQL_CREATE_FIXED_DETAIL);
        ps.setInt(1, fdd.getBankAccountId());
        ps.setDate(2, new Date(fdd.getCreationDate().getTime()));
        ps.setInt(3, fdd.getDepositAmount());
        ps.setInt(4, fdd.getTenure());
        ps.setCharacterStream(5, new StringReader(fdd.isActive() ? "Y" : "N"));
        return ps;
    }, keyHolder);
}
```

利用 `batchUpdate` 方法可以在同一个 `PreparedStatement` 上批量执行 `update` 语句
