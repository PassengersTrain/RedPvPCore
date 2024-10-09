package io.github.redpvpcore.enchantment;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class GoldrushEnchant extends EnchantmentWrapper implements Listener {
    public GoldrushEnchant(int id) {
        super(id);
    }

    @Override
    public String getName() {
        return "Gold Rush";
    }

    @Override
    public int getMaxLevel() {
        return 200;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.WEAPON;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return true;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK)
            return;
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            if (livingEntity instanceof Player) {
                Player player = (Player) livingEntity;
                ItemStack item = player.getInventory().getItemInHand();
                if (item.containsEnchantment(Enchant.GOLD_RUSH)) {
                    int lvl = item.getEnchantmentLevel(Enchant.GOLD_RUSH);
                    double ch = Math.min(0.01 + 0.005 * lvl, 1.0);
                    Random rand = new Random();
                    if (rand.nextDouble() < ch) {
                        int amt = rand.nextInt(2);
                        ItemStack gold = new ItemStack(Material.GOLD_INGOT, amt);
                        player.getInventory().addItem(gold);
                    }
                }
            }
        }
    }
}