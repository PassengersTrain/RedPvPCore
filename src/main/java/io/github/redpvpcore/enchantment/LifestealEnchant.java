package io.github.redpvpcore.enchantment;

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
        return true;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            return;
        }
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            if (livingEntity instanceof Player) {
                Player player = (Player) livingEntity;
                ItemStack item = player.getInventory().getItemInHand();
                if (item.getEnchantments().containsKey(Enchant.LIFE_STEAL)) {
                    System.out.println("Debug for ENCHANT_LIFE_STEAL");
                    int lvl = item.getEnchantmentLevel(Enchant.LIFE_STEAL);
                    double ch = Math.min(0.1 + 0.02 * lvl, 1.0);
                    Random rand = new Random();
                    if (rand.nextDouble() < ch) {
                        double amt = Math.min(0.5 + 0.02 * lvl, player.getMaxHealth() - player.getHealth());
                        if (amt > 0) {
                            player.setHealth(Math.min(player.getHealth() + amt, player.getMaxHealth()));
                            player.sendMessage("You have healed yourself for " + amt + " health!");
                        }
                    }
                }
            }
        }
    }
}
