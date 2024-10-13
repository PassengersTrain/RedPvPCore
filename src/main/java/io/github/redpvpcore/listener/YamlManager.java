/*
Credits to https://www.youtube.com/watch?v=3en6w7PNL08
 */

package io.github.redpvpcore.listener;

import io.github.redpvpcore.RedPvPCore;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class YamlManager {
    private static YamlManager instance;
    private File file;
    private FileConfiguration yaml;

    public YamlManager(RedPvPCore plugin) {
        setup(plugin);
    }

    public static void init(RedPvPCore plugin) {
        if (instance == null) {
            instance = new YamlManager(plugin);
        }
    }

    private void setup(RedPvPCore plugin) {
        file = new File(plugin.getDataFolder(), "data.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        yaml = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get() {
        if (instance == null) {
            return null;
        }
        return instance.yaml;
    }

    public static void save() {
        if (instance != null) {
            try {
                instance.yaml.save(instance.file);
            } catch (IOException e) {
                System.out.println("Couldn't save file");
            }
        }
    }

    public void reload() {
        yaml = YamlConfiguration.loadConfiguration(file);
    }

    public void saveData(Player player, List<ItemStack> insuredItem) {
        String path = "player." + player.getUniqueId() + ".item";
        List<Integer> id = new ArrayList<>();
        List<Integer> amount = new ArrayList<>();
        for (ItemStack item : insuredItem) {
            if (item != null && item.getType() != Material.AIR) {
                id.add(item.getType().getId());
                amount.add(item.getAmount());
            }
        }
        yaml.set(path + ".id", id);
        yaml.set(path + ".amount", amount);
        save();
    }
    public List<ItemStack> loadData(Player player) {
        String path = "player." + player.getUniqueId() + ".item";
        List<Integer> id = yaml.getIntegerList(path + ".id");
        List<Integer> amount = yaml.getIntegerList(path + ".amount");
        List<ItemStack> item = new ArrayList<>();
        for (int i = 0; i < id.size(); i++) {
            Material material = Material.getMaterial(id.get(i));
            if (material != null) {
                item.add(new ItemStack(material, amount.get(i)));
            }
        }
        return item;
    }
}
