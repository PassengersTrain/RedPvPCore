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

public class LifestealEnchant extends EnchantmentWrapper implements Listener {
    public LifestealEnchant(int id) {
        super(id);
    }

    @Override
    public String getName() {
        return "Life Steal";
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
        return true;
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return item.getType() == Material.DIAMOND_AXE;
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
                if (item.containsEnchantment(Enchant.LIFE_STEAL)) {
                    int lvl = item.getEnchantmentLevel(Enchant.LIFE_STEAL);
                    double ch = Math.min(0.1 + 0.02 * lvl, 1.0);
                    Random rand = new Random();
                    if (rand.nextDouble() < ch) {
                        double healAmount = Math.min(player.getMaxHealth() - player.getHealth(), 0.5 + 0.02 * lvl);
                        player.setHealth(Math.min(player.getHealth() + healAmount, player.getMaxHealth()));
                    }
                }
            }
        }
    }
}