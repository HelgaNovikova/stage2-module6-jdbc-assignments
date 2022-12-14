package jdbc;

import javax.sql.DataSource;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

@Getter
@Setter
public class CustomDataSource implements DataSource {
    private static volatile CustomDataSource instance;
    private final String driver;
    private final String url;
    private final String name;
    private final String password;
    private final Connection connection;

    private CustomDataSource(String driver, String url, String password, String name){
        try {
            connection = new CustomConnector().getConnection(url, name, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.driver = driver;
        this.name = name;
        this.password = password;
        this.url = url;
    }

    @SneakyThrows
    public static CustomDataSource getInstance() {
        if (instance == null) {
            synchronized (CustomDataSource.class) {
                if (instance == null) {
                    Properties props = new Properties();
                    FileInputStream fis =  new FileInputStream("app.properties");
                    props.load(fis);
                    String url = props.getProperty("postgres.url");
                    String password = props.getProperty("postgres.password");
                    String name = props.getProperty("postgres.name");
                    String driver = props.getProperty("postgres.driver");
                    instance = new CustomDataSource(driver, url, password, name);
                }
            }
        }
        return instance;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return connection;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
