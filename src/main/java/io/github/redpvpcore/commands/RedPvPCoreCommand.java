package io.github.redpvpcore.commands;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.github.redpvpcore.RedPvPCore;
import io.github.redpvpcore.listener.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RedPvPCoreCommand implements CommandExecutor, Listener {
    private final RedPvPCore plugin;
    private final DatabaseManager manager;

    private final Cache<Player, List<ItemStack>> managerCache = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .build();

    public RedPvPCoreCommand(RedPvPCore plugin, DatabaseManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Player player = (Player) commandSender;
        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Syntax cannot be null.");
            return true;
        }
        String cmd = args[0];
        switch (cmd) {
            case "insureitem":
                ItemStack item = player.getInventory().getItemInHand();
                if (item == null || item.getType() == Material.AIR) {
                    player.sendMessage(ChatColor.RED + "You are not holding an item.");
                }
                List<ItemStack> insuredItem = managerCache.getIfPresent(player);
                if (insuredItem == null) {
                    insuredItem = new ArrayList<>();
                }
                insuredItem.add(item.clone());
                managerCache.put(player, insuredItem);
                System.out.println("==================================================");
                System.out.println("Cache updated...");
                System.out.println("Player: " + player.getName());
                System.out.println("Updated cache: " + insuredItem.toString());
                System.out.println("==================================================");
                try {
                    manager.add(player, item);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco take " + player.getName() + " 100 ");
                    player.sendMessage(ChatColor.GREEN + "Item secured successfully.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return true;
            default:
                player.sendMessage(ChatColor.RED + "Unknown command.");
                return true;
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
