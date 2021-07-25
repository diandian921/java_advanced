package week05.db;

import org.springframework.util.CollectionUtils;
import week05.db.dbmanager.DBManger;
import week05.db.dbmanager.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by diandian
 * @date 2021/7/20.
 */
public class JdbcTest2 {

    DBManger dbManger = new JdbcUtils();

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
        } catch (SQLException e) {
            dbManger.rollback(conn);
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
            dbManger.rollback(conn);
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
        Connection conn = dbManger.getConnection();
        int res = 0;
        String sql = "select id ,name from student where id = ?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery(sql);

            List<Student> students = mapResult(resultSet);
            return CollectionUtils.isEmpty(students) ? null : students.get(0);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            dbManger.close(conn, preparedStatement, resultSet);
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
