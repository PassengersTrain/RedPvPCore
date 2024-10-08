package io.github.redpvpcore.commands;

import io.github.redpvpcore.config.Config;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ItemInsuranceCommand implements CommandExecutor, Listener {

    private final Map<UUID, ItemStack> item2 = new HashMap<>();
    private final Map<UUID, Long> time = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Failed to execute as a console level command.");
        }

        Player player = (Player) sender;
        if (!player.hasPermission("insurance.use.command")) {
            player.sendMessage(ChatColor.RED + "Permission Denied.");
        }

        ItemStack item = player.getItemInHand();

        if(item == null) {
            player.sendMessage(ChatColor.RED + "Null cannot be secured.");
        }

        insureItem(player, item);

        return true;
    }

    @EventHandler
    public void onItemInsurance(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (item2.containsKey(player.getUniqueId())) {
            double t = time.get(player.getUniqueId());
            if (System.currentTimeMillis() < t) {
                ItemStack insuredItem = item2.get(player.getUniqueId());
                event.getDrops().removeIf(item -> item.isSimilar(insuredItem));
            }
        }
    }

    public void insureItem(Player player, ItemStack clone) {
        ItemStack item = player.getItemInHand();
        item2.put(player.getUniqueId(), item.clone());
        long t = System.currentTimeMillis() + 3600 * 1000;
        time.put(player.getUniqueId(), t);

        Config config = new Config(JavaPlugin.getProvidingPlugin(getClass()));
        config.savePlayerData(player.getUniqueId(), item.getType().toString(), t);

        new BukkitRunnable() {
            @Override
            public void run() {
                item2.remove(player.getUniqueId());
                time.remove(player.getUniqueId());
                player.sendMessage(ChatColor.RED + "Your insurance has expired and you will drop items upon death.");
            }
        }.runTaskLater(JavaPlugin.getProvidingPlugin(getClass()), 3600 * 20);
        player.sendMessage(ChatColor.GREEN + "Your item insurance lasts for " + (t - System.currentTimeMillis()) / 1000 + " seconds.");

    }
}
