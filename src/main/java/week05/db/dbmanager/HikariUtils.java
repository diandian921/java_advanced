package week05.db.dbmanager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;

/**
 * @author Created by diandian
 * @date 2021/7/25.
 */
public class HikariUtils implements DBManger {

    private static final HikariDataSource dataSource;

    static {
        //初始化连接池
        HikariConfig config = new HikariConfig("/hikari.properties");
        dataSource = new HikariDataSource(config);
    }

    @Override
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
