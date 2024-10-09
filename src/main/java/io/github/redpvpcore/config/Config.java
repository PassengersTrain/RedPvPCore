package io.github.redpvpcore.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Config {
    private Plugin p;
    private FileConfiguration config;
    private File cfile;
    private FileConfiguration insdata;
    private File insf;

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

        insf = new File(p.getDataFolder(), "insurance.yml");
        if (!insf.exists()) {
            try {
                insf.createNewFile();
                System.out.println("Successfully created configuration file insurance.yml.");
            } catch (IOException e) {
                System.err.println("Could not create insurance.yml, please contact the administrator immediately.");
            }
        }
        loadInsuranceData();
    }

    public void loadInsuranceData() {
        insdata = YamlConfiguration.loadConfiguration(insf);
    }

    public FileConfiguration getInsuranceData() {
        return insdata;
    }

    public void saveInsurance() {
        try {
            insdata.save(insf);
        } catch (IOException e) {
            System.err.println("Unable to save insurance.yml, please contact the administrator immediately.");
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void saveConfig() {
        try {
            config.save(cfile);
        } catch (IOException e) {
            System.err.println("Unable to save config file, is the plugin broken?");
        }
    }

    public PluginDescriptionFile getDesc() {
        return p.getDescription();
    }

    public void savePlayerData(UUID id, String item, long duration) {
        String path = "players." + id.toString();
        insdata.set(path + ".item", item);
        insdata.set(path + ".duration", duration);
        saveInsurance();
    }

    public void loadPlayerData(UUID id) {
        String path = "players." + id.toString();
        String item = insdata.getString(path + ".item");
        long duration = insdata.getLong(path + ".duration", 0);

        if (item != null) {
            System.out.println("============================================================");
            System.out.println("Config value has been changed.");
            System.out.println("Item: " + item);
            System.out.println("Duration: " + duration);
            System.out.println("=============================================================");
        } else {
            System.out.println("No player data found for UUID: " + id);
        }
    }

    public String getPlayerItem(UUID id) {
        String path = "players." + id.toString();
        return insdata.getString(path + ".item");
    }

    public long getPlayerDuration(UUID id) {
        String path = "players." + id.toString();
        return insdata.getLong(path + ".duration", 0);
    }

    public void removePlayerData(UUID id) {
        String path = "players." + id.toString();
        insdata.set(path, null);
        saveInsurance();
    }
}
