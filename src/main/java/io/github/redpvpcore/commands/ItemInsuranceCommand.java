package io.github.redpvpcore.commands;

import io.github.redpvpcore.util.InsuranceManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class ItemInsuranceCommand implements CommandExecutor, Listener {
    InsuranceManager manager = new InsuranceManager();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;
        ItemStack item = player.getInventory().getItemInHand();
        if(item == null) {
            player.sendMessage(ChatColor.RED + "This item has no meta value.");
        } else {
            manager.setInsuredItem(item, 3600000);
            player.sendMessage(ChatColor.GREEN + "DEBUG 1, SUCCESSFUL");
        }
        return true;
    }
    @EventHandler
    private void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if(manager.isItemInsured()) {
            ItemStack item = manager.getInsuredItem();
            if(item != null) {
                player.getInventory().addItem(item);
                player.sendMessage(ChatColor.GREEN + "DEBUG 1, SUCCESSFUL");
            } else {
                player.sendMessage(ChatColor.RED + "DEBUG 2, FAIL");
            }
        }
    }
}