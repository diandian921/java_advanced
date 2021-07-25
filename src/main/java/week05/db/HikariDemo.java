package week05.db;

import org.springframework.util.CollectionUtils;
import week05.db.dbmanager.DBManger;
import week05.db.dbmanager.HikariUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by diandian
 * @date 2021/7/25.
 */
public class HikariDemo {

    DBManger dbManger = new HikariUtils();

    public int insert(Student student) {
        Connection conn = null;
        int res = 0;
        String sql = "insert into student(id,name) values(?,?)";
        PreparedStatement preparedStatement = null;
        try {
            conn = dbManger.getConnection();
            conn.setAutoCommit(false);
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, student.getId());
            preparedStatement.setString(2, student.getName());
            res = preparedStatement.executeUpdate();
            dbManger.commit(conn);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbManger.close(conn, preparedStatement, null);
        }
        return res;
    }

    public int delete(int id) {
        Connection conn = dbManger.getConnection();
        int res = 0;
        String sql = "delete from student where id = ?";
        PreparedStatement preparedStatement = null;
        try {
            conn.setAutoCommit(false);
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            res = preparedStatement.executeUpdate();
            dbManger.commit(conn);
        } catch (SQLException e) {
        } finally {
            dbManger.close(conn, preparedStatement, null);
        }
        return res;
    }

    public int update(Student student) {
        Connection conn = dbManger.getConnection();
        int res = 0;
        String sql = "update student set name = ? where id = ?";
        PreparedStatement preparedStatement = null;
        try {
            conn.setAutoCommit(false);
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, student.getName());
            preparedStatement.setInt(2, student.getId());
            res = preparedStatement.executeUpdate();
            dbManger.commit(conn);
        } catch (SQLException e) {
            dbManger.rollback(conn);
        } finally {
            dbManger.close(conn, preparedStatement, null);
        }
        return res;
    }

    public Student queryById(int id) {
        Connection connection = null;
        PreparedStatement prepareStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dbManger.getConnection();
            // 创建PreparedStatement对象
            String sql = "select * from student where id = ?";
            prepareStatement = connection.prepareStatement(sql);
            // 入参映射
            prepareStatement.setInt(1, id);
            // 执行sql
            resultSet = prepareStatement.executeQuery();
            // 出参映射
            List<Student> list = mapResult(resultSet);
            return CollectionUtils.isEmpty(list) ? null : list.get(0);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            // 释放资源
            dbManger.close(connection, prepareStatement, null);
        }
    }

    private List<Student> mapResult(ResultSet resultSet) throws SQLException {
        List<Student> list = new ArrayList<>();
        while (resultSet.next()) {
            Student student = new Student();
            student.setId(resultSet.getInt(1));
            student.setName(resultSet.getString(2));
            list.add(student);
        }
        return list;
    }

}
