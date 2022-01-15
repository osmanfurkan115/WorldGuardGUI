package me.heymrau.worldguardguiplugin.managers;

import com.hakan.inventoryapi.inventory.ClickableItem;
import com.hakan.inventoryapi.inventory.HInventory;
import com.hakan.inventoryapi.inventory.Pagination;
import me.heymrau.worldguardguiplugin.model.CustomItem;
import org.bukkit.Material;

import java.util.Arrays;

public class InventoryManager {
    public void setupButtons(HInventory inventory, Pagination pagination) {
        inventory.setItem(38, ClickableItem.of(new CustomItem("&6Previous Page", null, Material.ARROW, false, (short) 0, 1).complete(), (event) -> pagination.previousPage()));

        inventory.setItem(40, ClickableItem.of(new CustomItem("&cClose", null, Material.BARRIER, false, (short) 0, 1).complete(), (event) -> event.getWhoClicked().closeInventory()));

        inventory.setItem(42, ClickableItem.of(new CustomItem("&6Next Page", null, Material.ARROW, false, (short) 0, 1).complete(), (event) -> pagination.nextPage()));
    }

    public void setupInventory(HInventory inventory, Pagination pagination) {
        inventory.guiAir();
        pagination.setItemSlots(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35));
    }
}
