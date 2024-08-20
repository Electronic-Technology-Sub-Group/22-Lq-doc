# NamedParameterJdbcTemplate

`NamedParameterJdbcTemplate` 是 `JdbcTemplate` 的一个包装类。该类允许将 SQL 中的 `?` 替换成以 `:` 开头的名称作为占位符

```java
@Bean
public NamedParameterJdbcTemplate jdbcTemplate(DataSource dataSource) {
    return new NamedParameterJdbcTemplate(dataSource);
}
```

之后，可以通过 Map 传入对应的参数；如果只有一个参数，则可以直接创建 `SqlParameterSource`

包含多种 `query` 方法，`queryForObject` 可以通过返回值创建一个对象，`queryForList` 可以返回 `List` 等

```java
@Autowired
private NamedParameterJdbcTemplate jdbcTemplate;

private static final String SQL_CREATE_FIXED_DETAIL =
        "insert into fixed_deposit_details(ACCOUNT_ID, FD_CREATION_DATE, AMOUNT, TENURE, ACTIVE) " +
        "value (:account, :creation_date, :amount, :tenure, :active)";
private static final String SQL_QUERY_DEPOSIT_BY_ID =
        "select * from fixed_deposit_details where FIXED_DEPOSIT_ID = :id";

// 使用 Map 传入多个参数
public int createFixedDetail(FixedDepositDetails fixedDepositDetails) {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("account", fixedDepositDetails.getBankAccountId());
    parameters.put("creation_date", new Date(fixedDepositDetails.getCreationDate().getTime()));
    parameters.put("amount", fixedDepositDetails.getDepositAmount());
    parameters.put("tenure", fixedDepositDetails.getTenure());
    parameters.put("active", fixedDepositDetails.isActive() ? "Y" : "N");
    MapSqlParameterSource parameterSource = new MapSqlParameterSource(parameters);
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(SQL_CREATE_FIXED_DETAIL, parameterSource, keyHolder);
    // 通过 KeyHolder 获取返回的 GENERATED_KEY 获取主键自增的主键，返回值是一个 BigInteger
    int fixedDepositId = ((Number) keyHolder.getKeys().get("GENERATED_KEY")).intValue();
    fixedDepositDetails.setId(fixedDepositId);
    return fixedDepositId;
}

// 只有一个参数，直接创建 SqlParameterSource
public FixedDepositDetails getFixedDeposit(int id) {
    SqlParameterSource parameters = new MapSqlParameterSource("id", id);
    return jdbcTemplate.queryForObject(SQL_QUERY_DEPOSIT_BY_ID, parameters, (rowSet, rowNum) -> new FixedDepositDetails(
            rowSet.getInt("FIXED_DEPOSIT_ID"),
            rowSet.getInt("ACCOUNT_ID"),
            rowSet.getDate("FD_CREATION_DATE"),
            rowSet.getInt("AMOUNT"),
            rowSet.getInt("TENURE"),
            rowSet.getString("ACTIVE").equals("Y")
    ));
}
```

‍
