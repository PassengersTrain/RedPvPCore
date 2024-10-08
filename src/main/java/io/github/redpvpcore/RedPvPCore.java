package io.github.redpvpcore;

import io.github.redpvpcore.commands.EnchantItemCommand;
import io.github.redpvpcore.commands.ItemInsuranceCommand;
import io.github.redpvpcore.config.Config;
import io.github.redpvpcore.enchantment.Enchant;
import io.github.redpvpcore.enchantment.PoisonEnchant;
import io.github.redpvpcore.listener.Events;
import org.bukkit.plugin.java.JavaPlugin;

public final class RedPvPCore extends JavaPlugin {
    private Config cf;
    @Override
    public void onEnable() {
        System.out.println("Creating default configs.");
        saveDefaultConfig();
        Config cf = new Config(this);
        cf.setup();
        System.out.println("Registering commands.");
        registerCommands();
        System.out.println("Registering events.");
        registerEvents();
        System.out.println("Registering enchants.");
        Enchant.registerEnchants(this);
    }

    @Override
    public void onDisable() {
        if (cf != null) {
            System.out.println("Saving default config.");
            cf.saveData();
        } else {
            System.out.println("Config is null, contact is an administrator.");
        }
    }

    public void registerCommands() {
        getCommand("itse").setExecutor(new ItemInsuranceCommand());
        getCommand("itenc").setExecutor(new EnchantItemCommand());
    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new ItemInsuranceCommand(), this);
        getServer().getPluginManager().registerEvents(new Events(), this);
        getServer().getPluginManager().registerEvents(new PoisonEnchant(600500), this);
    }
}
