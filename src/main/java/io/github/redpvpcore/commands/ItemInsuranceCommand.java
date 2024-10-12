package io.github.redpvpcore.commands;

import io.github.redpvpcore.util.InsuranceManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ItemInsuranceCommand implements CommandExecutor, Listener {
    private final Map<Player, InsuranceManager> manager = new HashMap<>();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This is not a console-level command.");
        }

        Player player = (Player) sender;
         ItemStack item = player.getInventory().getItemInHand();
        if (item == null || item.getType() == Material.AIR) {
            player.sendMessage(ChatColor.RED + "You are not holding an item.");
        }

        manager.putIfAbsent(player, new InsuranceManager());
        manager.get(player).setInsuredItem(item, 3600000);
        player.sendMessage(ChatColor.GREEN + "SUCCESSFUL");
        return true;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        InsuranceManager insman = manager.get(player);
        if (manager != null && insman.isItemInsured()) {
            ItemStack item = insman.getInsuredItem();
            if (item != null) {
                player.getInventory().addItem(item);
                player.sendMessage(ChatColor.GREEN + "DEBUG 2");
            } else {
                player.sendMessage(ChatColor.RED + "FAILED.");
            }
        }
    }
}
