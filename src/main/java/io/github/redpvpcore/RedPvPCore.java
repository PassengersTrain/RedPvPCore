package io.github.redpvpcore;

import io.github.redpvpcore.commands.RedPvPCoreCommand;
import io.github.redpvpcore.listener.Events;
import io.github.redpvpcore.listener.YamlManager;
import org.bukkit.plugin.java.JavaPlugin;

import static io.github.redpvpcore.listener.YamlManager.save;

public final class RedPvPCore extends JavaPlugin {
    @Override
    public void onEnable() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        YamlManager.init(this);
        YamlManager.get().options().copyDefaults(true);
        System.out.println("Registering commands.");
        registerCommands();
        System.out.println("Registering events.");
        registerEvents();
    }

    @Override
    public void onDisable() {
        save();
    }

    public void registerCommands() {
        getCommand("redcore").setExecutor(new RedPvPCoreCommand(this));
    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new RedPvPCoreCommand(this), this);
        getServer().getPluginManager().registerEvents(new Events(), this);
    }
}
