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
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RedPvPCoreCommand implements CommandExecutor, Listener {
    public static RedPvPCore plugin;
    public static DatabaseManager manager;

    public static Cache<Player, List<ItemStack>> managerCache = CacheBuilder.newBuilder()
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
}
