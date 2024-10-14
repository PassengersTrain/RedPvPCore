package io.github.redpvpcore.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static io.github.redpvpcore.commands.RedPvPCoreCommand.*;

public class Events implements Listener {

    private final List<Material> banned = Arrays.asList(
            Material.BEDROCK,
            Material.SKULL,
            Material.COMMAND,
            Material.COMMAND_MINECART,
            Material.LAVA_BUCKET,
            Material.TNT,
            Material.BARRIER
    );

    @EventHandler(priority = EventPriority.MONITOR)
    public void itemChecker(PlayerJoinEvent e) {
        for (Player player : Bukkit.getOnlinePlayers())
            for (Material item : banned) {
                while (player.getInventory().contains(item)) {
                    player.sendMessage(ChatColor.RED + "Clearing illegal items in your inventory.");
                    player.getInventory().remove(item);
                }
            }
    }

    @EventHandler
    public void onDeath(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        List<ItemStack> insuredItem = managerCache.getIfPresent(player);
        if (insuredItem != null && !insuredItem.isEmpty()) {
            try {
                List<ItemStack> dbitems = manager.get(player);
                for (ItemStack item : dbitems) {
                    player.getInventory().addItem(item.clone());
                }
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    try {
                        if (player.isOnline()) {
                            manager.remove(player, dbitems.toString());
                            System.out.println("==================================================");
                            System.out.println("Clearing database...");
                            System.out.println("Player: " + player.getName());
                            System.out.println("Item insurance has expired.");
                            System.out.println("==================================================");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }, 20L * 60L * 60L);
                managerCache.invalidate(player);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        List<ItemStack> insuredItem = managerCache.getIfPresent(player);
        if (insuredItem != null && !insuredItem.isEmpty()) {
            try {
                manager.add(player, insuredItem);
                System.out.println("==================================================");
                System.out.println("Saving player data...");
                System.out.println("Player: " + player.getName());
                System.out.println("Items: " + insuredItem.toString());
                System.out.println("==================================================");
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Error saving insured items for player: " + player.getName());
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        try {
            List<ItemStack> insuredItem = manager.get(player);
            if (insuredItem != null && !insuredItem.isEmpty()) {
                managerCache.put(player, insuredItem);
                System.out.println("==================================================");
                System.out.println("Loading player data...");
                System.out.println("Player: " + player.getName());
                System.out.println("Items: " + insuredItem.toString());
                System.out.println("==================================================");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


