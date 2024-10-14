package io.github.redpvpcore.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private final Connection connection;

    public DatabaseManager(String path) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS player_items (" +
                    "uuid TEXT, " +
                    "itemid TEXT, " +
                    "amount INTEGER NOT NULL DEFAULT 0, " +
                    "PRIMARY KEY (uuid, itemid))");
        }
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public void add(Player player, ItemStack item) throws SQLException {
        String id = item.getType().toString();
        int amount = item.getAmount();

        if (exist(player, id)) {
            update(player, id, getAmount(player, id) + amount);
        } else {
            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO player_items (uuid, itemid, amount) VALUES (?, ?, ?)")) {
                preparedStatement.setString(1, player.getUniqueId().toString());
                preparedStatement.setString(2, id);
                preparedStatement.setInt(3, amount);
                preparedStatement.executeUpdate();
            }
        }
    }

    public void add(Player player, List<ItemStack> items) throws SQLException {
        for (ItemStack item : items) {
            add(player, item);
        }
    }

    private boolean exist(Player player, String id) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM player_items WHERE uuid = ? AND itemid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.setString(2, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
    }

    public int getAmount(Player player, String id) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT amount FROM player_items WHERE uuid = ? AND itemid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.setString(2, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("amount");
            }
        }
        return 0;
    }

    public void update(Player player, String id, int amount) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE player_items SET amount = ? WHERE uuid = ? AND itemid = ?")) {
            preparedStatement.setInt(1, amount);
            preparedStatement.setString(2, player.getUniqueId().toString());
            preparedStatement.setString(3, id);
            preparedStatement.executeUpdate();
        }
    }

    public List<ItemStack> get(Player player) throws SQLException {
        List<ItemStack> items = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT itemid, amount FROM player_items WHERE uuid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String itemId = resultSet.getString("itemid");
                int amount = resultSet.getInt("amount");
                Material material = Material.getMaterial(itemId);
                if (material != null) {
                    ItemStack itemStack = new ItemStack(material, amount);
                    items.add(itemStack);
                }
            }
        }
        return items;
    }

    public void remove(Player player, String id) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM player_items WHERE uuid = ? AND itemid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.setString(2, id);
            preparedStatement.executeUpdate();
        }
    }
}
