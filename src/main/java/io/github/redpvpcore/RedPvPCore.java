package io.github.redpvpcore;

import io.github.redpvpcore.commands.RedPvPCoreCommand;
import io.github.redpvpcore.listener.DatabaseManager;
import io.github.redpvpcore.listener.Events;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class RedPvPCore extends JavaPlugin {

    private DatabaseManager db;

    @Override
    public void onEnable() {
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            System.out.println("Registering database.");
            db = new DatabaseManager(getDataFolder().getAbsolutePath() + "/item.db");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to connect to database! " + e.getMessage());
            return;
        }
        System.out.println("Registering commands.");
        registerCommands();
        System.out.println("Registering events.");
        registerEvents();
    }

    @Override
    public void onDisable() {
        try {
            db.closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void registerCommands() {
        getCommand("redcore").setExecutor(new RedPvPCoreCommand(this, db));
    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new Events(), this);
    }
}
