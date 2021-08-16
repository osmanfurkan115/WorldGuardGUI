package me.heymrau.worldguardguiplugin.inventories;

import com.hakan.inventoryapi.inventory.HInventory;
import org.bukkit.entity.Player;

public abstract class Inventory {

    abstract void createInventory();

    abstract public HInventory getInventory(String regionName, Player player);

    abstract public void open(Player player, String regionName);
}
