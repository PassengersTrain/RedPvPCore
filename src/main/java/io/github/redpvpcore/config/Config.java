package io.github.redpvpcore.config;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

public class Config {
    private Plugin p;
    private FileConfiguration config;
    private File cfile;
    private FileConfiguration data;
    private File dfile;

    public Config(Plugin plugin) {
        this.p = plugin;
        setup();
    }

    public void setup() {
        cfile = new File(p.getDataFolder(), "config.yml");
        config = p.getConfig();

        if (!p.getDataFolder().exists()) {
            p.getDataFolder().mkdir();
        }

        dfile = new File(p.getDataFolder(), "insurance.yml");
        if (!dfile.exists()) {
            try {
                dfile.createNewFile();
            } catch (IOException e) {
                Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not create insurance.yml!");
            }
        }
        loadData();
    }

    public void loadData() {
        data = YamlConfiguration.loadConfiguration(dfile);
    }

    public FileConfiguration getData() {
        return data;
    }

    public void saveData() {
        try {
            data.save(dfile);
        } catch (IOException e) {
            Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save insurance.yml!");
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void saveConfig() {
        try {
            config.save(cfile);
        } catch (IOException e) {
            Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save config.yml!");
        }
    }

    public PluginDescriptionFile getDesc() {
        return p.getDescription();
    }

    public void savePlayerData(UUID id, String item, long duration) {
        String path = "players." + id.toString();
        data.set(path + ".item", item);
        data.set(path + ".duration", duration);
        saveData();
    }

    public void loadPlayerData(UUID id) {
        String path = "players." + id.toString();
        if (data == null) {
            System.out.println("Unable to load player data, check if the plugin is loaded in the right manner or player data doesn't exist.");
        }
        String item = data.getString(path + ".item");
        long duration = data.getLong(path + ".duration", 0);
        if (item != null) {
            System.out.println("============================================================");
            System.out.println("Config value has been changed.");
            System.out.println("Item: " + item);
            System.out.println("Duration: " + duration);
            System.out.println("=============================================================");
        } else {
            Bukkit.getLogger().log(Level.INFO, "No player data found for UUID: " + id);
        }
    }

    public String getPlayerItem(UUID id) {
        String path = "players." + id.toString();
        return data.getString(path + ".item");
    }

    public long getPlayerDuration(UUID id) {
        String path = "players." + id.toString();
        return data.getLong(path + ".duration", 0); 
    }

    public void removePlayerData(UUID id) {
        String path = "players." + id.toString();
        data.set(path, null);
        saveData();
    }
}
