
import dbmanager.DBManger;
import dbmanager.JdbcUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Created by diandian
 * @date 2021/7/20.
 */
public class JdbcTest1 {

    DBManger dbManger = new JdbcUtils();

    public int insert(Student student) {
        Connection conn = dbManger.getConnection();
        int res = 0;
        String sql = "insert into student(id,name) values(" + student.getId() + ",'" + student.getName() + "')";
        Statement statement = null;
        try {
            statement = conn.createStatement();
            res = statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManger.close(conn, statement, null);
        }
        return res;
    }

    public int delete(int id) {
        Connection conn = dbManger.getConnection();
        int res = 0;
        String sql = "delete from student where id = " + id;
        Statement statement = null;
        try {
            statement = conn.createStatement();
            res = statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManger.close(conn, statement, null);
        }
        return res;
    }

    public int update(Student student) {
        Connection conn = dbManger.getConnection();
        int res = 0;
        String sql = "update student set name = '" + student.getName() + "' where id = " + student.getId();
        Statement statement = null;
        try {
            statement = conn.createStatement();
            res = statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManger.close(conn, statement, null);
        }
        return res;
    }

    public Student queryById(int id) {
        Connection conn = dbManger.getConnection();
        int res = 0;
        String sql = "select id,name from student where id = " + id;
        Statement statement = null;
        ResultSet resultSet = null;
        Student student = null;
        try {
            statement = conn.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                student = new Student();
                student.setId(resultSet.getInt("id"));
                student.setName(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbManger.close(conn, statement, resultSet);
        }
        return student;
    }
}
