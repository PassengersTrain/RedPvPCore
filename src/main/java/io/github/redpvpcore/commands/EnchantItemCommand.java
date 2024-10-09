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

import java.util.ArrayList;
import java.util.List;

public class EnchantItemCommand implements CommandExecutor {

    private int eid = 0;
    public int elvl = 0;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This is not a console level command.");
        }

        Player player = (Player) sender;
        try {
            eid = Integer.parseInt(args[0]);
            elvl = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage("Invalid enchantment ID or level.");
        }

        ItemStack item = player.getInventory().getItemInHand();
        if (item.getType() == Material.AIR) {
            player.sendMessage("Null cannot be enchanted.");
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            player.sendMessage("Null cannot be enchanted.");
        }

        switch (eid) {
            case 600500:
                meta.addEnchant(Enchant.POISON, elvl, true);
                List<String> plore = new ArrayList<>();
                plore.add("");
                plore.add(ChatColor.GRAY + "Poison " + elvl);
                plore.add(ChatColor.GRAY + "With every successful hit,");
                plore.add(ChatColor.GRAY + "you have a 100% chance to poison your enemies.");
                meta.setLore(plore);
                player.getInventory().getItemInHand().setItemMeta(meta);
                break;

            case 200500:
                meta.addEnchant(Enchant.LIFE_STEAL, elvl, true);
                List<String> llore = new ArrayList<>();
                llore.add(ChatColor.GRAY + "Lifesteal " + elvl);
                llore.add(ChatColor.GRAY + "With every successful hit, ");
                llore.add(ChatColor.GRAY + "you have a " + (0.02 * elvl) + "% chance to regain one heart.");
                meta.setLore(llore);
                player.getInventory().getItemInHand().setItemMeta(meta);
                break;

            case 100500:
                meta.addEnchant(Enchant.GOLD_RUSH, elvl, true);
                List<String> glore = new ArrayList<>();
                glore.add(ChatColor.GRAY + "Gold Rush " + elvl);
                glore.add(ChatColor.GRAY + "With every successful hit, ");
                glore.add(ChatColor.GRAY + "you have a " + (0.005 * elvl) + "% chance to drop a gold ingot.");
                meta.setLore(glore);
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
