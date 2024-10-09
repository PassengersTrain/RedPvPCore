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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PoisonEnchant extends EnchantmentWrapper implements Listener {
    public PoisonEnchant(int id) {
        super(id);
    }

    @Override
    public String getName() {
        return "Poison";
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
                if (item.containsEnchantment(Enchant.POISON)) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 30, 3)); // 30 ticks, Amplifier 3 (adjust as needed)
                }
            }
        }
    }
}