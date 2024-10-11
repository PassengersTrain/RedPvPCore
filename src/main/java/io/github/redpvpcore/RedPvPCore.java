package io.github.redpvpcore;

import io.github.redpvpcore.commands.EnchantItemCommand;
import io.github.redpvpcore.commands.ItemInsuranceCommand;
import io.github.redpvpcore.enchantment.Enchant;
import io.github.redpvpcore.enchantment.GoldrushEnchant;
import io.github.redpvpcore.enchantment.LifestealEnchant;
import io.github.redpvpcore.enchantment.PoisonEnchant;
import io.github.redpvpcore.listener.Events;
import org.bukkit.plugin.java.JavaPlugin;

public final class RedPvPCore extends JavaPlugin {
    @Override
    public void onEnable() {
        System.out.println("Creating default configs.");
        saveDefaultConfig();
        System.out.println("Registering commands.");
        registerCommands();
        System.out.println("Registering events.");
        registerEvents();
        System.out.println("Registering enchants.");
        Enchant.registerEnchants(this);
    }

    @Override
    public void onDisable() {
        System.out.println("Config is null, contact is an administrator.");
    }

    public void registerCommands() {
        getCommand("itse").setExecutor(new ItemInsuranceCommand());
        getCommand("itenc").setExecutor(new EnchantItemCommand());
    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new ItemInsuranceCommand(), this);
        getServer().getPluginManager().registerEvents(new Events(), this);
        getServer().getPluginManager().registerEvents(new PoisonEnchant(600500), this);
        getServer().getPluginManager().registerEvents(new LifestealEnchant(200500), this);
        getServer().getPluginManager().registerEvents(new GoldrushEnchant(100500), this);
    }
}
