package io.github.redpvpcore.commands;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.github.redpvpcore.RedPvPCore;
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

    int index = 0;
    String ppath = "player.";
    String ipath = ".item";

    public RedPvPCoreCommand(RedPvPCore plugin) {
        this.plugin = plugin;
    }

    private final Cache<Player, List<ItemStack>> managerCache = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .build();
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        String cmd = args[0];
        Player player = (Player) commandSender;

        if(cmd == null) {
            player.sendMessage(ChatColor.RED + "Syntax cannot be null.");
        }
        switch(cmd) {
            case "insureitem":
                ItemStack item = player.getInventory().getItemInHand();
                if (item == null || item.getType() == Material.AIR) {
                    player.sendMessage(ChatColor.RED + "You are not holding an item.");
                }
                List<ItemStack> insuredItem = managerCache.getIfPresent(player);
                if (insuredItem == null) {
                    insuredItem = new ArrayList<>();
                }
                insuredItem.add(item);
                managerCache.put(player, insuredItem);
                player.sendMessage(ChatColor.GREEN + "SUCCESS #1.");
        }
        return true;
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
            player.sendMessage(ChatColor.GREEN + "SUCCESS #2.");
        } else {
            player.sendMessage(ChatColor.RED + "Something went wrong? Again?");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        List<ItemStack> insuredItem = managerCache.getIfPresent(player);
        if (insuredItem != null && !insuredItem.isEmpty()) {
            for (index = 0; index < insuredItem.size(); index++) {
                ItemStack item = insuredItem.get(index);
                plugin.getPlayerData().set(ppath + player.getUniqueId() + ipath + index, item.serialize());
            }
            plugin.savePlayerData();
        }
    }
        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent event) {
            Player player = event.getPlayer();
            List<ItemStack> insuredItem = new ArrayList<>();
            while (plugin.getPlayerData().contains(ppath + ipath + index)) {
                ItemStack item = ItemStack.deserialize(plugin.getPlayerData().getConfigurationSection(ppath + ipath + index).getValues(false));
                insuredItem.add(item);
                index++;
            }
            managerCache.put(player, insuredItem);
        }
}


