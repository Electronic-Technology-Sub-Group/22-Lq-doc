# SimpleJdbcInsert

`SimpleJdbcInsert` 是一个利用数据库元数据简化 `insert` 语句的辅助类，可以通过 `DataSource` 创建。

```java
private SimpleJdbcInsert simpleJdbcInsert;

@Autowired
private void setDataSource(DataSource dataSource) {
    simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("fixed_deposit_details")
            .usingGeneratedKeyColumns("FIXED_DEPOSIT_ID");
}
```

使用 `SimpleJdbcInsert` 可以直接通过参数构建 `insert` 语句，也可以直接获取通过 `usingGeneratedKeyColumns` 传入的主键，不需要使用 `KeyHolder`

```java
public int createFixedDetail(FixedDepositDetails fixedDepositDetails) {
    Map<String, Object> arguments = new HashMap<>();
    arguments.put("account", fixedDepositDetails.getBankAccountId());
    arguments.put("creation_date", new Date(fixedDepositDetails.getCreationDate().getTime()));
    arguments.put("amount", fixedDepositDetails.getDepositAmount());
    arguments.put("tenure", fixedDepositDetails.getTenure());
    arguments.put("active", fixedDepositDetails.isActive() ? "Y" : "N");
    int fixedDepositId = simpleJdbcInsert.executeAndReturnKey(arguments).intValue();
    fixedDepositDetails.setId(fixedDepositId);
    return fixedDepositId;
}
```

‍
