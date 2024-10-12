package io.github.redpvpcore.util;

import java.sql.Connection;

import io.github.redpvpcore.util.InsuranceManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.*;

public class SQLManager {

    private final Connection connection;

    public SQLManager(String path) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS players (" +
                    "uuid TEXT PRIMARY KEY, " +
                    "duration LONG NOT NULL, " +
                    "time LONG NOT NULL, " +
                    "item TEXT NOT NULL)");
        }
    }

    public SQLManager(Connection connection) {
        this.connection = connection;
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}