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
