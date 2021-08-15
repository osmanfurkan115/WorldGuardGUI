package me.heymrau.worldguardgui.inventories;

import com.hakan.inventoryapi.inventory.HInventory;
import org.bukkit.entity.Player;

public abstract class Inventory {

    abstract void createInventory();

    abstract public HInventory getInventory(String regionName);

    abstract public void open(Player player);
}
