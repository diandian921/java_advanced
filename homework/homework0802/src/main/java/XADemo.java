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
