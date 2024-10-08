package io.github.redpvpcore.gui;
/*
import io.github.redpvpcore.commands.ItemInsuranceCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ItemInsuranceGUI implements Listener {

    private final ItemInsuranceCommand ims;
    private Inventory inv;

    public ItemInsuranceGUI(ItemInsuranceCommand ims) {
        this.ims = ims;
        createInventory();
    }

    public void createInventory() {
        inv = Bukkit.createInventory(null, 27, "Item Insurance");
        initializeItems();
    }

    public void initializeItems() {
        inv.setItem(12, guiItem(Material.DIAMOND, ChatColor.GRAY + "Item Insurance",
                ChatColor.GRAY + "Cost: " + ChatColor.GOLD + "100 Coins" + " \n " +
                        ChatColor.GRAY + "Insurance lasts for 1 hour!"));
        inv.setItem(14, guiItem(Material.BARRIER, ChatColor.GRAY + "Drop the item here!",
                ChatColor.GRAY + "Drag and drop an item to secure it."));
    }

    protected ItemStack guiItem(Material mat, String name, String lore) {
        final ItemStack item = new ItemStack(mat, 1);
        final ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            throw new IllegalArgumentException("ItemMeta is null for material: " + mat);
        }
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }

    public void openInventory(final HumanEntity player) {
        player.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().equals(inv)) {
            event.setCancelled(true);
            Player p = (Player) event.getWhoClicked();
            ItemStack cli = event.getCurrentItem();
            ItemStack ci = event.getCursor();
            if (cli != null && cli.getType() == Material.DIAMOND) {
                if (ci != null && ci.getType() != Material.AIR) {
                    ims.insureItem(p, ci.clone());
                    p.setItemOnCursor(new ItemStack(Material.AIR));
                    inv.setItem(14, guiItem(Material.GLASS, ChatColor.GRAY + "Item Secured",
                            ChatColor.GRAY + "Your item has been secured."));
                    p.closeInventory();
                } else {
                    p.sendMessage(ChatColor.RED + "Cannot secure a null item. Drag and drop an item to secure it.");
                }
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getInventory().equals(inv)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDrop(InventoryClickEvent event) {
        if (event.getInventory().equals(inv) && event.getSlot() == 14) {
            Player p = (Player) event.getWhoClicked();
            ItemStack cursorItem = event.getCursor();
            if (cursorItem != null && cursorItem.getType() != Material.AIR) {
                ims.insureItem(p, cursorItem.clone());
                p.setItemOnCursor(new ItemStack(Material.AIR));
                inv.setItem(14, guiItem(Material.GLASS, ChatColor.GRAY + "Item Secured",
                        ChatColor.GRAY + "Your item has been secured."));
            } else {
                p.sendMessage(ChatColor.RED + "Cannot secure a null item.");
            }
        }
    }
}

 */
