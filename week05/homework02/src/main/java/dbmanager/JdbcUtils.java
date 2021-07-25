package dbmanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * @author Created by diandian
 * @date 2021/7/20.
 */
public class JdbcUtils implements DBManger {

    private static final String driver;
    private static final String url;
    private static final String username;
    private static final String password;


    static {
        ResourceBundle bundle = ResourceBundle.getBundle("jdbc");
        driver = bundle.getString("driver");
        url = bundle.getString("url");
        username = bundle.getString("userName");
        password = bundle.getString("password");
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver class can not found!");
        }
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException("Can not access to url :" + url);
        }
    }
}
