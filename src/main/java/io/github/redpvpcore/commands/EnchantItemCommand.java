package io.github.redpvpcore.commands;

import io.github.redpvpcore.enchantment.Enchant;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class EnchantItemCommand implements CommandExecutor {

    private int eid = 0;
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This command can only be used by players.");
        }

        Player player = (Player) commandSender;

        try {
            eid = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage("Invalid enchantment ID.");
        }

        ItemStack item = player.getItemInHand();
        if (item.getType() == Material.AIR) {
            player.sendMessage("You are not holding any item.");
            return true;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            player.sendMessage("Null cannot be enchanted.");
            return true;
        }

        switch (eid) {
            case 600500:
                meta.addEnchant(Enchant.POISON, 1, true);
                player.sendMessage(ChatColor.GREEN + "Poison enchantment added!");
                meta.setLore(Arrays.asList(ChatColor.GRAY + " With every successful hit ", ChatColor.GRAY + " have a 100% chance to " , ChatColor.GRAY + " poison your enemies. "));
                player.getInventory().getItemInHand().setItemMeta(meta);
                break;

            default:
                player.sendMessage("Enchant doesn't exist.");
                return true;
        }
        item.setItemMeta(meta);
        return true;
    }
}
