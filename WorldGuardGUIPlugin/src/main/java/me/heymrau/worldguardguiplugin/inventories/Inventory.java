package me.heymrau.worldguardguiplugin.inventories;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.heymrau.worldguardguiplugin.inventories.permission.InventoryPermission;
import org.bukkit.entity.Player;

public abstract class Inventory {
    private final String permission;

    public Inventory() {
        this.permission = "";
    }

    public Inventory(InventoryPermission inventoryPermission) {
        this.permission = inventoryPermission.getPermission();
    }

    public boolean checkPermission(Player player) {
        return player.hasPermission(permission) || player.hasPermission("worldguardgui.admin") || permission.isEmpty();
    }

    abstract void open(Player player, ProtectedRegion region);
}
