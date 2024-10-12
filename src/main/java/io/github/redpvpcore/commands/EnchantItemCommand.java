package io.github.redpvpcore.commands;

import io.github.redpvpcore.enchantment.Enchant;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class EnchantItemCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This is not a console-level command.");
        }

        Player player = (Player) sender;
        ItemStack item = player.getInventory().getItemInHand();
        if (item.getType() == Material.AIR) {
            player.sendMessage("Null item cannot be enchanted.");
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            player.sendMessage("Item with null meta cannot be enchanted.");
        }

        List<String> lore = new ArrayList<>();
        if (meta.hasLore()) {
            lore = meta.getLore();
        }
        for (int i = 0; i < args.length; i += 2) {
            try {
                int eid = Integer.parseInt(args[i]);
                int elvl = Integer.parseInt(args[i + 1]);
                switch (eid) {
                    case 600500:
                        if (!meta.hasEnchant(Enchant.POISON)) {
                            meta.addEnchant(Enchant.POISON, elvl, true);
                            lore.add(ChatColor.GRAY + "Poison " + elvl);
                            lore.add(ChatColor.GRAY + "With every successful hit, you have a 100% chance ");
                            lore.add(ChatColor.GRAY + " to poison your enemies");
                        }
                        break;

                    case 200500:
                        if (!meta.hasEnchant(Enchant.LIFE_STEAL)) {
                            meta.addEnchant(Enchant.LIFE_STEAL, elvl, true);
                            lore.add(ChatColor.GRAY + "Lifesteal " + elvl);
                            lore.add(ChatColor.GRAY + "With every successful hit, you have a " + (0.02 * elvl) + " % chance");
                            lore.add(ChatColor.GRAY + " to regain one heart.");
                        }
                        break;

                    case 100500:
                        if (!meta.hasEnchant(Enchant.GOLD_RUSH)) {
                            meta.addEnchant(Enchant.GOLD_RUSH, elvl, true);
                            lore.add(ChatColor.GRAY + "Gold Rush " + elvl);
                            lore.add(ChatColor.GRAY + "With every successful hit, you have a " + (0.005 * elvl) + " % chance");
                            lore.add(ChatColor.GRAY + "to drop a gold ingot." );
                        }
                        break;

                    case 300500:
                        if (!meta.hasEnchant(Enchantment.PROTECTION_ENVIRONMENTAL)) {
                            meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, elvl, true);
                            break;
                        }

                    case 400500:
                        if (!meta.hasEnchant(Enchantment.PROTECTION_PROJECTILE)) {
                            meta.addEnchant(Enchantment.PROTECTION_PROJECTILE, elvl, true);
                            break;
                        }
                    case 120500:
                        if (!meta.hasEnchant(Enchantment.THORNS)) {
                            meta.addEnchant(Enchantment.THORNS, elvl, true);
                            break;
                        }
                    case 156789:
                        if (!meta.hasEnchant(Enchantment.ARROW_DAMAGE)) {
                            meta.addEnchant(Enchantment.ARROW_DAMAGE, elvl, true);
                            break;
                        }
                    case 200789:
                        if (!meta.hasEnchant(Enchantment.ARROW_KNOCKBACK)) {
                            meta.addEnchant(Enchantment.ARROW_KNOCKBACK, elvl, true);
                            break;
                        }
                    case 196578:
                        if (!meta.hasEnchant(Enchantment.ARROW_FIRE)) {
                            meta.addEnchant(Enchantment.ARROW_FIRE, elvl, true);
                        }
                    case 204768:
                        if (!meta.hasEnchant(Enchantment.DURABILITY)) {
                            meta.addEnchant(Enchantment.DURABILITY, elvl, true);
                            break;
                        }
                    case 214768:
                        if (!meta.hasEnchant(Enchantment.KNOCKBACK)) {
                            meta.addEnchant(Enchantment.KNOCKBACK, elvl, true);
                            break;
                        }
                    case 25897:
                        if (!meta.hasEnchant(Enchantment.DAMAGE_ALL)) {
                            meta.addEnchant(Enchantment.DAMAGE_ALL, elvl, true);
                            break;
                        }
                    default:
                        if (lore.size() > 1) {
                            lore.add(String.valueOf(eid + elvl));
                            lore.clear();
                        }
                }
            } catch (NumberFormatException e) {
                throw new RuntimeException(e);
            }
            meta.setLore(lore);
            item.setItemMeta(meta);
            player.sendMessage(ChatColor.GREEN + "Enchantments applied successfully!");
        }
        return true;
    }
}
