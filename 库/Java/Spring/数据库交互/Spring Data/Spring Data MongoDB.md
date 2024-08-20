# Spring Data MongoDB

MongoDB 是一个 NoSQL 数据库，由 字段 - 值 的结构组成，类似 json 对象。

每个文件都有一个 `_id` 字段，该字段由 Mongodb 自动生成，作用类似主键。

依赖（Spring）：`org.springframework.boot:spring-boot-starter-data-mongodb`

依赖（MongoDB）：`org.mongodb:mongodb-driver-sync`，`org.mongodb:mongodb-driver-core`

依赖（Querydsl）：`com.querydsl:querydsl-mongodb`

1. 创建实体类，即前面 JPA 的 `@Entity` 类

    ```java
    import org.springframework.data.annotation.Id;
    import org.springframework.data.mongodb.core.mapping.Document;

    @Document(collection = "bank_accounts")
    public class BankAccountDetails {

        @Id
        private String accountId;
        private int balance;
        private Date lastTransactionTimestamp;
        private List<FixedDepositDetails> fixedDeposits;
    }
    ```
    ```java
    import org.bson.types.ObjectId;
    import org.springframework.data.annotation.Id;

    public class FixedDepositDetails {
        @Id
        private ObjectId fixedDepositId;
        private int fdAmount;

        public FixedDepositDetails() {
            fixedDepositId = ObjectId.get();
        }
    }
    ```
    * 使用 `@Document` 注解表示一个 MongoDB 文档建模域实体，`@Id` 表示主键字段
    * `FixedDepositDetails` 类作为 `BankAccountDetails` 的一个嵌入对象，不需要一个单独的文档，故没有 `@Document` 注解
    * 我们需要 `FixedDepositDetails` 具有唯一标识符识别定期存款，故手动使用 `ObjectId.get()` 方法要求分配一个 ID（否则 `@Id` 也无效）

    使用 `Querydsl`，需要将这两个类也使用 `@Entity`，用于生成 Q 类
2. 配置 MongoDB 连接

    ```java
    @Configuration
    @Profile("mongodb")
    @EnableAsync
    @EnableMongoRepositories(basePackages = "com.example.mybank.mongodb_repository")
    public class MongoDBProfileConfig {

        @Bean
        public Properties mongoProperties() throws IOException {
            Properties props = new Properties();
            ClassPathResource resource = new ClassPathResource("database/mongodb.properties");
            try (InputStream inputStream = resource.getInputStream()) {
                props.load(inputStream);
            }
            return props;
        }

        @Bean
        public MongoClient mongoClient(@Qualifier("mongoProperties") Properties properties) {
            return MongoClients.create(properties.getProperty("url"));
        }

        @Bean
        public MongoDatabaseFactory mongoDbFactory(MongoClient mongoClient, @Qualifier("mongoProperties") Properties properties) {
            return new SimpleMongoClientDatabaseFactory(mongoClient, properties.getProperty("database"));
        }
      
        @Bean
        public MongoTemplate mongoTemplate(MongoDatabaseFactory mongoDbFactory) {
            return new MongoTemplate(mongoDbFactory);
        }
    }
    ```
    `@EnableAsync` 表示允许异步。使用 `XML` 配置时使用 `mongo` 命名空间

    ```xml
    <mongo:repositories base-package="com.example.mybank.mongodb_repository" />
    <mongo:mongo-client host="..." port="..." />
    <mongo:db-factory dbname="..." mongo-ref="mongoClient">
    <mongo:template db-factory-ref="mongoDbFactory">
    ```
3. 创建 `Repository` 接口类型，继承 `MongoRepository`。若需要扩展方法，使用 `Repository` 的扩展方式。

    ```xml
    public interface BankAccountRepository 
           extends MongoRepository<BankAccountDetails, String>, QuerydslPredicateExecutor<BankAccountDetails> {
      
        List<BankAccountDetails> findByFixedDepositsTenureAndFixedDepositsFdAmount(int tenure, int fdAmount);
      
        @Async
        CompletableFuture<List<BankAccountDetails>> findAllByBalanceGreaterThan(int balance);

        @Query("{'balance': {$lt: ?0}}")
        List<BankAccountDetails> findByCustomQuery(int balance);
    }
    ```
    MongoDB 也支持 Querydsl。处理如 `List<FixedDepositDetails> fixedDeposits` 的数据时，使用 `.any()` 表示遍历，类似 `filter`

    ```java
    BankAccountDetails bankDetails = new BankAccountDetails();
    ExampleMatcher matcher = ExampleMatcher.withIngore("account", "balance", "lastTransactionTemplate").any 把

    ```
