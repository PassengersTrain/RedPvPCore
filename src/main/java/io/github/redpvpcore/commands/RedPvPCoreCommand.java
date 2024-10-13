package io.github.redpvpcore.commands;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.github.redpvpcore.RedPvPCore;
import io.github.redpvpcore.listener.YamlManager;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RedPvPCoreCommand implements CommandExecutor, Listener {
    private final RedPvPCore plugin;
    private final YamlManager manager;

    private final Cache<Player, List<ItemStack>> managerCache = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .build();

    public RedPvPCoreCommand(RedPvPCore plugin) {
        this.plugin = plugin;
        this.manager = new YamlManager(plugin);
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
                    return true;
                }

                List<ItemStack> insuredItem = managerCache.getIfPresent(player);
                if (insuredItem == null) {
                    insuredItem = new ArrayList<>();
                }
                insuredItem.add(item.clone());
                managerCache.put(player, insuredItem);
                player.sendMessage(ChatColor.GREEN + "Item insured successfully.");
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
            for (ItemStack item : insuredItem) {
                if (item != null) {
                    player.getInventory().addItem(item);
                }
            }
            System.out.println("==================================================");
            System.out.println("Player: " + player.getName());
            System.out.println("Added Items: " + insuredItem);
            System.out.println("==================================================");
        } else {
            System.out.println("==================================================");
            System.out.println("Player: " + player.getName());
            System.out.println("Added Items: " + "Null, player did not have any insured items? or is this a bug?");
            System.out.println("==================================================");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        List<ItemStack> insuredItem = managerCache.getIfPresent(player);
        if (insuredItem != null && !insuredItem.isEmpty()) {
            manager.saveData(player, insuredItem);
            System.out.println("==================================================");
            System.out.println("Saving player data, this is a debug.");
            System.out.println("Player: " + player.getName());
            System.out.println("Items: " + insuredItem.toString());
            System.out.println("==================================================");

        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        List<ItemStack> insuredItem = manager.loadData(player);
        managerCache.put(player, insuredItem);
        System.out.println("==================================================");
        System.out.println("Loading player data, this is a debug.");
        System.out.println("Player: " + player.getName());
        System.out.println("Items: " + insuredItem);
        System.out.println("==================================================");    }
}
