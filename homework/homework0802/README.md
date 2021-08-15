### 作业：基于hmily TCC或ShardingSphere的Atomikos XA实现一个简单的分布式事务应用demo（二选一）

说明：作业采用ShardingSphere，环境基于上个作业创建的库表。2个库，每个库16张表。

程序配置：

maven依赖：
```xml
     <dependencies>
            <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
                <version>5.1.45</version>
            </dependency>
    
            <dependency>
                <groupId>org.apache.shardingsphere</groupId>
                <artifactId>shardingsphere-jdbc-core</artifactId>
                <version>5.0.0-beta</version>
            </dependency>
    
            <dependency>
                <groupId>org.apache.shardingsphere</groupId>
                <artifactId>shardingsphere-transaction-xa-core</artifactId>
                <version>5.0.0-beta</version>
            </dependency>
    
            <dependency>
                <groupId>com.zaxxer</groupId>
                <artifactId>HikariCP</artifactId>
                <version>2.2.5</version>
            </dependency>
        </dependencies>
```

sharding-config.xml
```yaml
# 配置真实数据源
dataSources:
  # 配置第 1 个数据源
  ds_0: com.zaxxer.hikari.HikariDataSource
    jdbcUrl: jdbc:mysql://localhost:3306/shopping_0?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true
    driverClassName: com.mysql.jdbc.Driver
    username: root
    password: root
  # 配置第 2 个数据源
  ds_1: com.zaxxer.hikari.HikariDataSource
    driverClassName: com.mysql.jdbc.Driver
    jdbcUrl: jdbc:mysql://localhost:3306/shopping_1?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true
    username: root
    password: root

rules:
  # 配置分片规则
  - !SHARDING
    tables:
      # 配置 order_info 表规则
      t_order:
        actualDataNodes: ds_${0..1}.order_info_${0..15}
        # 配置分库策略
        databaseStrategy:
          standard:
            shardingColumn: user_info_id
            shardingAlgorithmName: database_inline
        # 配置分表策略
        tableStrategy:
          standard:
            shardingColumn: id
            shardingAlgorithmName: table_inline
    # 配置分片算法
    shardingAlgorithms:
      database_inline:
        type: INLINE
        props:
          algorithm-expression: ds_${user_info_id % 2}
      table_inline:
        type: INLINE
        props:
          algorithm-expression: order_info_${user_info_id % 16}
```

测试demo:
```java
import org.apache.shardingsphere.driver.api.yaml.YamlShardingSphereDataSourceFactory;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.apache.shardingsphere.transaction.core.TransactionTypeHolder;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * @author Created by diandian
 * @date 2021/8/15.
 */
public class XADemo {


    public static void main(String[] args) throws Exception {
        String path = System.getProperty("user.dir") + "/src/main/resources/sharding-config.yaml";
        DataSource dataSource = YamlShardingSphereDataSourceFactory.createDataSource(new File(path));

        TransactionTypeHolder.set(TransactionType.XA);

        Connection conn = dataSource.getConnection();
        String sql = "insert into order_info (id, user_info_id) VALUES (?, ?);";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            for (int i = 1; i < 16; i++) {
                statement.setLong(1, i);
                statement.setLong(2, i);
                statement.executeUpdate();
            }
            conn.commit();
        }


        // 如果设置XA事务生效，则所有的数据都不会插入
        // 如果设置XA事务不生效，则所有数据就会插入到数据库
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            for (int i = 1; i < 16; i++) {
                statement.setLong(1, i + 15);
                statement.setLong(2, i + 15);

                //模拟异常
                if (i == 10) {
                    throw new RuntimeException();
                }
                statement.executeUpdate();
            }
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
        } finally {
            conn.close();
        }


    }
}

```