package jdbc;


import lombok.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleJDBCRepository {

    private Connection connection = CustomDataSource.getInstance().getConnection();
//    private PreparedStatement ps = null;
    // private Statement st = createStatement();

    private static final String createUserSQL = "insert into myusers(firstname,lastname, age) values ( ? , ? , ? ) RETURNING id";
    private static final String updateUserSQL = "update myusers set firstname=?, lastname=?, age=? where id=? returning id, firstname, lastname, age";
    private static final String deleteUser = "delete from myusers where id=?";
    private static final String findUserByIdSQL = "select * from myusers where id=?";
    private static final String findUserByNameSQL = "select * from myusers where firstname=?";
    private static final String findAllUserSQL = "select * from myusers";

    @SneakyThrows
    public Long createUser(User user) {
        PreparedStatement preparedStatement = connection.prepareStatement(createUserSQL);
        preparedStatement.setString(1, user.getFirstName());
        preparedStatement.setString(2, user.getLastName());
        preparedStatement.setInt(3, user.getAge());
        ResultSet rs = preparedStatement.executeQuery();
        rs.next();
        return rs.getLong(1);
    }

    @SneakyThrows
    public User findUserById(Long userId) {
        PreparedStatement preparedStatement = connection.prepareStatement(findUserByIdSQL);
        preparedStatement.setLong(1, userId);
        ResultSet rs = preparedStatement.executeQuery();
        rs.next();
        return new User(rs.getLong("id"), rs.getString("firstname"), rs.getString("lastname"),
                rs.getInt("age"));
    }

    @SneakyThrows
    public User findUserByName(String userName) {
        PreparedStatement preparedStatement = connection.prepareStatement(findUserByNameSQL);
        preparedStatement.setString(1, userName);
        ResultSet rs = preparedStatement.executeQuery();
        rs.next();
        return new User(rs.getLong("id"), rs.getString("firstname"), rs.getString("lastname"),
                rs.getInt("age"));
    }

    @SneakyThrows
    public List<User> findAllUser() {
        ResultSet rs = connection.createStatement().executeQuery(findAllUserSQL);
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            users.add(new User(rs.getLong("id"), rs.getString("firstname"), rs.getString("lastname"),
                    rs.getInt("age")));
        }
        return users;
    }

    @SneakyThrows
    public User updateUser(User user) {
        PreparedStatement preparedStatement = connection.prepareStatement(updateUserSQL);
        preparedStatement.setLong(4, user.getId());
        preparedStatement.setString(1, user.getFirstName());
        preparedStatement.setString(2, user.getLastName());
        preparedStatement.setInt(3, user.getAge());
        ResultSet rs = preparedStatement.executeQuery();
        rs.next();
        return new User(rs.getLong("id"), rs.getString("firstname"), rs.getString("lastname"),
                rs.getInt("age"));
    }

    @SneakyThrows
    public void deleteUser(Long userId) {
        PreparedStatement preparedStatement = connection.prepareStatement(deleteUser);
        preparedStatement.setLong(1, userId);
        preparedStatement.executeUpdate();
    }

//    public static void main(String[] args) {
//        SimpleJDBCRepository repository = new SimpleJDBCRepository();
//        User user = new User(null, "dgjdfil", "gkljmfgl", 4);
//        user.setId(repository.createUser(user));
//        repository.findAllUser();
//        repository.findUserById(user.getId());
//        repository.findUserByName(user.getFirstName());
//        repository.updateUser(user);
//        repository.deleteUser(user.getId());
//    }
}
