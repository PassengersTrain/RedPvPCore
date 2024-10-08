package io.github.redpvpcore.enchantment;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

public class Enchant {

    public static final PoisonEnchant POISON = new PoisonEnchant(600500);

    public static void registerEnchants(JavaPlugin plugin) {
        try {
            java.lang.reflect.Field field = Enchantment.class.getDeclaredField("acceptingNew");
            field.setAccessible(true);
            field.set(null, true);

            Enchantment.registerEnchantment(POISON);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void enchantLore() {

    }
}
