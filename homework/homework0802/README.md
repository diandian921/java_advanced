### 作业：基于hmily TCC或ShardingSphere的Atomikos XA实现一个简单的分布式事务应用demo（二选一）

说明：作业采用ShardingSphere，2个库，每个库2张表。

程序配置：

maven依赖：
```xml
     <dependencies>
           <dependency>
                   <groupId>mysql</groupId>
                   <artifactId>mysql-connector-java</artifactId>
                   <version>8.0.11</version>
               </dependency>
               <dependency>
                   <groupId>com.zaxxer</groupId>
                   <artifactId>HikariCP</artifactId>
                   <version>4.0.3</version>
               </dependency>
               <dependency>
                   <groupId>org.apache.shardingsphere</groupId>
                   <artifactId>shardingsphere-jdbc-core</artifactId>
                   <version>5.0.0-alpha</version>
               </dependency>
       
               <!-- 使用 XA 事务时，需要引入此模块 -->
               <dependency>
                   <groupId>org.apache.shardingsphere</groupId>
                   <artifactId>shardingsphere-transaction-xa-core</artifactId>
                   <version>5.0.0-alpha</version>
               </dependency>
        </dependencies>
```

sharding-config.xml
```yaml
dataSources:
  ds_0: !!com.zaxxer.hikari.HikariDataSource
    driverClassName: com.mysql.cj.jdbc.Driver
    jdbcUrl: jdbc:mysql://localhost:3306/ds_0?serverTimezone=UTC&useSSL=false&useUnicode=true&characterEncoding=UTF-8
    username: root
    password: 1qaz2wsx
  ds_1: !!com.zaxxer.hikari.HikariDataSource
    driverClassName: com.mysql.cj.jdbc.Driver
    jdbcUrl: jdbc:mysql://localhost:3306/ds_1?serverTimezone=UTC&useSSL=false&useUnicode=true&characterEncoding=UTF-8
    username: root
    password: 1qaz2wsx

rules:
  # 配置分片规则
  - !SHARDING
    tables:
      # 配置 t_order 表规则
      t_order:
        actualDataNodes: ds_${0..1}.t_order_${0..1}
        # 配置分库策略
        databaseStrategy:
          standard:
            shardingColumn: user_id
            shardingAlgorithmName: database_inline
        # 配置分表策略
        tableStrategy:
          standard:
            shardingColumn: order_id
            shardingAlgorithmName: table_inline

    # 配置分片算法
    shardingAlgorithms:
      database_inline:
        type: INLINE
        props:
          algorithm-expression: ds_${user_id % 2}
      table_inline:
        type: INLINE
        props:
          algorithm-expression: t_order_${order_id % 2}

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
import java.sql.SQLException;

/**
 * @author Created by diandian
 * @date 2021/8/15.
 */
public class XADemo {


    public static void main(String[] args) throws Exception {
        // 使用 ShardingSphereDataSource
        String path = "/sharding-config.yaml";
        File yamlFile = getFile(path);
        DataSource dataSource = YamlShardingSphereDataSourceFactory.createDataSource(yamlFile);

        // 支持 TransactionType.LOCAL, TransactionType.XA, TransactionType.BASE
        TransactionTypeHolder.set(TransactionType.XA);
        Connection conn = dataSource.getConnection();
        conn.setAutoCommit(false);
        PreparedStatement ps = conn.prepareStatement("INSERT INTO t_order (order_id, user_id) VALUES (?, ?)");
        try {
            for (int i = 1; i < 5; i++) {
                ps.setLong(1, i);
                ps.setLong(2, i);
                ps.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            System.out.println("first error :" + e.getMessage());
            conn.rollback();
        } finally {
            ps.close();
            conn.close();
        }

        System.out.println("----------------------------------------");


        conn.setAutoCommit(false);
        try {
            for (int i = 5; i < 9; i++) {
                ps.setLong(1, i);
                ps.setLong(2, i);
                if (i == 8) {
                    throw new RuntimeException("出错了");
                }
                ps.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            System.out.println("second error :" + e.getMessage());
            conn.rollback();
        } finally {
            ps.close();
            conn.close();
        }

    }

    private static File getFile(final String fileName) {
        return new File(Thread.currentThread().getClass().getResource(fileName).getFile());
    }
}


```