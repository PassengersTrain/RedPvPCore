package io.github.redpvpcore.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Arrays;
import java.util.List;

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
}

