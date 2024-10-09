package io.github.redpvpcore.commands;

import io.github.redpvpcore.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ItemInsuranceCommand implements CommandExecutor, Listener {

    private ItemStack ins;
    private long time;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return false;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("insurance.use.command")) {
            player.sendMessage(ChatColor.RED + "Permission Denied.");
            return false;
        }
        if (strings.length == 0) {
            player.sendMessage(ChatColor.RED + "Item name or ID cannot be null.");
            return false;
        }
        String name = strings[0];
        ItemStack item = getName(name);
        if (item == null) {
            player.sendMessage(ChatColor.RED + "Item name is null.");
            return false;
        }
        insureItem(player, item);
        return true;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (ins != null && System.currentTimeMillis() < time) {
            Bukkit.getScheduler().runTask(JavaPlugin.getProvidingPlugin(getClass()), () -> {
                player.getInventory().addItem(ins);
            });
        } else {
            player.sendMessage(ChatColor.RED + "Your item insurance has expired, and you will drop items upon death.");
            ins = null;
        }
    }

    public void insureItem(Player player, ItemStack item) {
        ins = item.clone();
        time = System.currentTimeMillis() + 3600 * 1000;
        Config config = new Config(JavaPlugin.getProvidingPlugin(getClass()));
        config.savePlayerData(player.getUniqueId(), item.getType().toString(), time);

        new BukkitRunnable() {
            @Override
            public void run() {
                ins = null;
                player.sendMessage(ChatColor.RED + "Your insurance has expired, and you will drop items upon death.");
            }
        }.runTaskLater(JavaPlugin.getProvidingPlugin(getClass()), 3600 * 20);

        long remain = time - System.currentTimeMillis();
        player.sendMessage(ChatColor.GREEN + "Your item insurance lasts for " + remain / 1000 + " seconds.");
    }

    private ItemStack getName(String name) {
        Material mat = Material.matchMaterial(name);
        if (mat != null) {
            return new ItemStack(mat);
        }
        return null;
    }
}
