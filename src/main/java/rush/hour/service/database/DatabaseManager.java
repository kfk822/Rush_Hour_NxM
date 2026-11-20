package rush.hour.service.database;

import rush.hour.service.exceptions.DatabaseManagerException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DEFAULT_URL = "jdbc:h2:./leaderboards/leaderboard";
    private final Connection connection;

    public DatabaseManager() throws DatabaseManagerException {
        this(DEFAULT_URL);
    }

    public DatabaseManager(String url) throws DatabaseManagerException {
        String user = "sa";
        String password = "";

        String createTableSql = "CREATE TABLE IF NOT EXISTS leaderboard (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(255) UNIQUE NOT NULL, " +
                "score INT)";
        try {
            this.connection = DriverManager.getConnection(url, user, password);
            try (Statement statement = connection.createStatement()) {
                statement.execute(createTableSql);
            }
        } catch (SQLException e) {
            throw new DatabaseManagerException(e.getMessage());
        }
    }

    public boolean playerExists(String name) throws DatabaseManagerException {
        String playerExists = "SELECT 1 FROM leaderboard WHERE name = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(playerExists)) {
            preparedStatement.setString(1, name);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new DatabaseManagerException(e.getMessage());
        }
    }

    public void addPlayer(String name) throws DatabaseManagerException {
        String addPlayer = "INSERT INTO leaderboard (name, score) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(addPlayer)) {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, 0);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseManagerException(e.getMessage());
        }
    }

    public void increaseScore(String name) throws DatabaseManagerException {
        String increaseScore = "UPDATE leaderboard SET score = score + 1 WHERE name = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(increaseScore)) {
            preparedStatement.setString(1, name);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseManagerException(e.getMessage());
        }
    }

    public String getLeaderboard() throws DatabaseManagerException {
        String getLeaderboard = "SELECT name, score FROM leaderboard ORDER BY score DESC";

        StringBuilder leaderboard = new StringBuilder();
        try (PreparedStatement preparedStatement = connection.prepareStatement(getLeaderboard);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                leaderboard.append(resultSet.getString("name"))
                        .append(" - ")
                        .append(resultSet.getInt("score"))
                        .append("\n");
            }
        } catch (SQLException e) {
            throw new DatabaseManagerException(e.getMessage());
        }
        return leaderboard.toString();
    }

    public void close() throws DatabaseManagerException {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new DatabaseManagerException(e.getMessage());
        }
    }
}