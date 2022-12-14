package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CustomConnector {
    public Connection getConnection(String url) throws SQLException {
        Connection con= DriverManager.getConnection(
                url);
        return con;
    }

    public Connection getConnection(String url, String user, String password) throws SQLException {
        Connection con=DriverManager.getConnection(
                url,user,password);
        return con;
    }
}
