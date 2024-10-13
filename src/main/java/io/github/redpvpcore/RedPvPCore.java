package io.github.redpvpcore;

import io.github.redpvpcore.commands.RedPvPCoreCommand;
import io.github.redpvpcore.listener.Events;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class RedPvPCore extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig();
        System.out.println("Registering commands.");
        registerCommands();
        System.out.println("Registering events.");
        registerEvents();
    }

    @Override
    public void onDisable()
    {
        savePlayerData();
    }

    public void registerCommands() {
        getCommand("redcore").setExecutor(new RedPvPCoreCommand(this));
    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new RedPvPCoreCommand(this), this);
        getServer().getPluginManager().registerEvents(new Events(), this);
    }


    public FileConfiguration getPlayerData() {
        return getConfig();
    }

    public void savePlayerData() {
        saveConfig();
    }
}
