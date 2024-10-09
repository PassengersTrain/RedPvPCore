package io.github.redpvpcore.enchantment;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

public class Enchant {

    public static final PoisonEnchant POISON = new PoisonEnchant(600500);
    public static final LifestealEnchant LIFE_STEAL = new LifestealEnchant(200500);
    public static final GoldrushEnchant GOLD_RUSH = new GoldrushEnchant(100500);

    public static void registerEnchants(JavaPlugin plugin) {
        try {
            java.lang.reflect.Field field = Enchantment.class.getDeclaredField("acceptingNew");
            field.setAccessible(true);
            field.set(null, true);
            Enchantment.registerEnchantment(POISON);
            Enchantment.registerEnchantment(LIFE_STEAL);
            Enchantment.registerEnchantment(GOLD_RUSH);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
