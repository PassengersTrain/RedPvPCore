package io.github.redpvpcore;

import io.github.redpvpcore.commands.DeveloperCommand;
import io.github.redpvpcore.commands.EnchantItemCommand;
import io.github.redpvpcore.commands.ItemInsuranceCommand;
import io.github.redpvpcore.enchantment.Enchant;
import io.github.redpvpcore.enchantment.GoldrushEnchant;
import io.github.redpvpcore.enchantment.LifestealEnchant;
import io.github.redpvpcore.enchantment.PoisonEnchant;
import io.github.redpvpcore.listener.Events;
import io.github.redpvpcore.util.SQLManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class RedPvPCore extends JavaPlugin {
    private SQLManager sql;
    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
        try {
            SQLManager sql = new SQLManager(getDataFolder().getAbsolutePath() + "/sql.db");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Registering commands.");
        registerCommands();
        System.out.println("Registering events.");
        registerEvents();
        System.out.println("Registering enchants.");
        Enchant.registerEnchants(this);
    }

    @Override
    public void onDisable()
    {
        try {
            sql.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void registerCommands() {
        getCommand("itse").setExecutor(new ItemInsuranceCommand());
        getCommand("itenc").setExecutor(new EnchantItemCommand());
        getCommand("developer").setExecutor(new DeveloperCommand());
    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new ItemInsuranceCommand(), this);
        getServer().getPluginManager().registerEvents(new Events(), this);
        getServer().getPluginManager().registerEvents(new PoisonEnchant(600500), this);
        getServer().getPluginManager().registerEvents(new LifestealEnchant(200500), this);
        getServer().getPluginManager().registerEvents(new GoldrushEnchant(100500), this);
    }
}
