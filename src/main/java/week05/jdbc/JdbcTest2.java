package week05.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Created by diandian
 * @date 2021/7/20.
 */
public class JdbcTest2 {

    public int insert(Student student) {
        Connection conn = JdbcUtils.getConnection();
        int res = 0;
        String sql = "insert into student(id,name) values(?,?)";
        PreparedStatement preparedStatement = null;
        try {
            conn.setAutoCommit(false);
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, student.getId());
            preparedStatement.setString(1, student.getName());
            res = preparedStatement.executeUpdate();
            JdbcUtils.commit(conn);
        } catch (SQLException e) {
            JdbcUtils.rollback(conn);
        } finally {
            JdbcUtils.close(conn, preparedStatement, null);
        }
        return res;
    }

    public int delete(int id) {
        Connection conn = JdbcUtils.getConnection();
        int res = 0;
        String sql = "delete from student where id = ?";
        PreparedStatement preparedStatement = null;
        try {
            conn.setAutoCommit(false);
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            res = preparedStatement.executeUpdate();
            JdbcUtils.commit(conn);
        } catch (SQLException e) {
            JdbcUtils.rollback(conn);
        } finally {
            JdbcUtils.close(conn, preparedStatement, null);
        }
        return res;
    }

    public int update(Student student) {
        Connection conn = JdbcUtils.getConnection();
        int res = 0;
        String sql = "update student set name = ? where id = ?";
        PreparedStatement preparedStatement = null;
        try {
            conn.setAutoCommit(false);
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, student.getName());
            preparedStatement.setInt(2, student.getId());
            res = preparedStatement.executeUpdate();
            JdbcUtils.commit(conn);
        } catch (SQLException e) {
            JdbcUtils.rollback(conn);
        } finally {
            JdbcUtils.close(conn, preparedStatement, null);
        }
        return res;
    }

    public Student queryById(int id) {
        Connection conn = JdbcUtils.getConnection();
        int res = 0;
        String sql = "select id ,name from student where id = ?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Student student = null;
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery(sql);
            while (resultSet.next()) {
                student = new Student();
                student.setId(resultSet.getInt("id"));
                student.setName(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(conn, preparedStatement, resultSet);
        }
        return student;
    }
}
