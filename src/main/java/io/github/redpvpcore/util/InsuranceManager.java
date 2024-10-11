package io.github.redpvpcore.util;

import org.bukkit.inventory.ItemStack;

public class InsuranceManager {
    private ItemStack item;
    private long duration;
    private long time;

    public InsuranceManager() {
        this.item = null;
        this.duration = 0;
        this.time = 0;
    }
    public void setInsuredItem(ItemStack item, long duration) {
        this.item = item.clone();
        this.duration = duration;
        this.time = System.currentTimeMillis();
    }

    public String getInsuredItemName() {
        if(item != null) {
            return item.getType().toString();
        } else {
            return "Invalid insurance item or duration.";
        }
    }

    public boolean isItemInsured() {
        return item != null && (System.currentTimeMillis() - time) < duration;
    }

    public ItemStack getInsuredItem() {
        if (isItemInsured()) {
            return item.clone();
        } else {
            return null;
        }
    }
    public void remove() {
        this.item = null;
        this.duration = 0;
        this.time = 0;
    }
}
