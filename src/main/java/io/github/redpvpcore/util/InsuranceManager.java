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
        if (item != null) {
            this.item = item.clone();
            this.duration = duration;
            this.time = System.currentTimeMillis();
        }
    }

    public long getTime() {
        return System.currentTimeMillis() - time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
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

    public String getName() {
        if (item == null) {
            return "No such data exists? Please contact the administrator.";
        } else {
            return item.toString();
        }
    }

    public void remove() {
        this.item = null;
        this.duration = 0;
        this.time = 0;
    }
}
