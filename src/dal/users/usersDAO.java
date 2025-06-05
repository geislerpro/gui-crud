package dal.users;

import db.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class usersDAO {
    public static boolean createUser(String username, String firstName, String lastName, String password) throws SQLException {
        try (Connection conn = Database.getConnection()) {
            String createTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT UNIQUE NOT NULL," +
                    "first_name TEXT NOT NULL," +
                    "last_name TEXT NOT NULL," +
                    "password TEXT NOT NULL)";
            conn.createStatement().execute(createTable);

            String query = "INSERT INTO users (username, first_name, last_name, password) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, firstName);
            stmt.setString(3, lastName);
            stmt.setString(4, password);
            stmt.executeUpdate();
            return true;
        }
    }
    public static boolean loginUser(String username, String password) throws SQLException {
        try (Connection conn = Database.getConnection()) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            return stmt.executeQuery().next(); // true if user exists
        }
    }
}
