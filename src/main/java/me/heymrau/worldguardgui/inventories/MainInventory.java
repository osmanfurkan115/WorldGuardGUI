package me.heymrau.worldguardgui.inventories;

import com.hakan.inventoryapi.inventory.ClickableItem;
import com.hakan.inventoryapi.inventory.HInventory;
import me.heymrau.worldguardgui.WorldGuardGUI;
import me.heymrau.worldguardgui.model.CustomItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class MainInventory extends Inventory {

    private final WorldGuardGUI plugin;
    private HInventory inventory;

    public MainInventory(WorldGuardGUI plugin) {
        this.plugin = plugin;
        createInventory();
    }

    @Override
    void createInventory() {
        final HInventory inventory = plugin.getInventoryAPI().getInventoryCreator().setInventoryType(InventoryType.FURNACE).setTitle("Test").create();
        final ItemStack info = new CustomItem("&eAbout worldguard gui", Arrays.asList("xxx", "yyy", "zzz"), Material.BOOK, false, (short) 0,1).complete();
        inventory.setItem(2, ClickableItem.empty(info));

        this.inventory = inventory;

    }

    @Override
    public HInventory getInventory(String regionName) {
        final ItemStack regionFlag = new CustomItem("&aManage region flags", null, Material.GRASS, true, (short) 0,1).complete();
        final ItemStack deleteRegion = new CustomItem("&cDelete region " + regionName, null, Material.BARRIER, false, (short) 0,1).complete();
        inventory.setItem(0, ClickableItem.empty(regionFlag));
        inventory.setItem(1, ClickableItem.empty(deleteRegion));
        return inventory;
    }

    @Override
    public void open(Player player) {
        getInventory("x").open(player);
    }
}
