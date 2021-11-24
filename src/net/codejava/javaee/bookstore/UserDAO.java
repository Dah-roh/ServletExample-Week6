package net.codejava.javaee.bookstore;

import lombok.Data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserDAO {
    private String jdbcURL;
    private String jdbcUsername;
    private  String jdbcPassword;
    private Connection jdbcConnection;

    public UserDAO(String jdbcURL, String jdbcUsername, String jdbcPassword) {
        this.jdbcURL = jdbcURL;
        this.jdbcUsername = jdbcUsername;
        this.jdbcPassword = jdbcPassword;
    }

    protected void connect() throws SQLException {
        if (jdbcConnection == null || jdbcConnection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                System.out.println(e.toString());
                throw new SQLException(e);
            }
            jdbcConnection = DriverManager.getConnection(
                    jdbcURL, jdbcUsername, jdbcPassword);
            System.out.println(jdbcConnection.isClosed());
        }
    }

    protected void disconnect() throws SQLException {
        if (jdbcConnection != null && !jdbcConnection.isClosed()) {
            jdbcConnection.close();
        }
    }

    public boolean insertUser(User user) throws SQLException {
        String sql = "INSERT INTO user (username, password) VALUES (?, ?)";
        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setString(1, user.getUsername());
        statement.setString(2, user.getPassword());

        boolean rowInserted = statement.executeUpdate() > 0;
        statement.close();
        disconnect();
        return rowInserted;
    }

    public User getUser (String username, String password) throws SQLException {
        User user = new User();
        String sql = "SELECT * FROM user WHERE username = ? AND password = ?";

        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setString(1, username);
        statement.setString(2, password);

        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Long id = resultSet.getLong("user_id");
            String name = resultSet.getString("username");
            String pass = resultSet.getString("password");
            user.setId(id);
            user.setUsername(name);
            user.setPassword(pass);

        }

        resultSet.close();
        statement.close();

        disconnect();

        return user;
    }
}
