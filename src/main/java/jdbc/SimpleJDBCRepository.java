package jdbc;


import lombok.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleJDBCRepository {

    private Connection connection =CustomDataSource.getInstance().getConnection();
//    private PreparedStatement ps = null;
   // private Statement st = createStatement();

    private static final String createUserSQL = "insert into myusers(id, firstname,lastname, age) values (1,'kolia', 'olia', 33)";
    private static final String updateUserSQL = "update myusers set lastname=sveta where id=1";
    private static final String deleteUser = "delete from myusers where id=?";
    private static final String findUserByIdSQL = "select * from myusers where id=?";
    private static final String findUserByNameSQL = "select * from myusers where name=?";
    private static final String findAllUserSQL = "select * from myusers";

    public Long createUser() throws SQLException {
        ResultSet rs = connection.createStatement().executeQuery(createUserSQL);
        return rs.getLong("id");
    }

    public User findUserById(Long userId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(findUserByIdSQL);
        preparedStatement.setLong(1,userId);
        ResultSet rs = preparedStatement.executeQuery();
        return new User(rs.getLong("id"), rs.getString("firstname"), rs.getString("lastname"),
                rs.getInt("age"));
    }

    public User findUserByName(String userName) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(findUserByNameSQL);
        preparedStatement.setString(1,userName);
        ResultSet rs = preparedStatement.executeQuery();
        return new User(rs.getLong("id"), rs.getString("firstname"), rs.getString("lastname"),
                rs.getInt("age"));
    }

    public List<User> findAllUser() throws SQLException {
        ResultSet rs = connection.createStatement().executeQuery(findAllUserSQL);
        List<User> users = new ArrayList<>();
        while(rs.next()){
            users.add(new User(rs.getLong("id"), rs.getString("firstname"), rs.getString("lastname"),
                    rs.getInt("age")));
        }
        return users;
    }

    public User updateUser() throws SQLException {
        ResultSet rs = connection.createStatement().executeQuery(updateUserSQL);
        return new User(rs.getLong("id"), rs.getString("firstname"), rs.getString("lastname"),
                rs.getInt("age"));
    }

    private void deleteUser(Long userId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(deleteUser);
        preparedStatement.setLong(1,userId);
        preparedStatement.executeQuery();
    }
}
