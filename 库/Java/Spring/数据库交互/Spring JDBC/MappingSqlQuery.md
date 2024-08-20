# MappingSqlQuery

`MappingSqlQuery` 可以将一个查询的结果 `ResultSet` 直接转换为一个对象，使用 `setParameters` 设置参数

```java
public class FixedDepositDetailsMappingSqlQuery extends MappingSqlQuery<FixedDepositDetails> {
    private static final String SQL_QUERY_DEPOSIT_BY_ID = "select * from fixed_deposit_details where FIXED_DEPOSIT_ID = ?";
    public FixedDepositDetailsMappingSqlQuery(DataSource ds) {
        super(ds, SQL_QUERY_DEPOSIT_BY_ID);
        setParameters(new SqlParameter("id", java.sql.Types.INTEGER));
    }
    @Override
    protected FixedDepositDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
        return FixedDepositDetails.builder()
                .id(rs.getInt("FIXED_DEPOSIT_ID"))
                .bankAccountId(rs.getInt("ACCOUNT_ID"))
                .creationDate(rs.getDate("FD_CREATION_DATE"))
                .depositAmount(rs.getInt("AMOUNT"))
                .tenure(rs.getInt("TENURE"))
                .active(rs.getString("ACTIVE").equals("Y"))
                .build();
    }
}
```

```java
private MappingSqlQuery<FixedDepositDetails> mappingSqlQuery;

@Autowired
private void setDataSource(DataSource dataSource) {
    // ...
    mappingSqlQuery = new FixedDepositDetailsMappingSqlQuery(dataSource);
}

public FixedDepositDetails getFixedDeposit(int id) {
    return mappingSqlQuery.findObject(id);
}
```
