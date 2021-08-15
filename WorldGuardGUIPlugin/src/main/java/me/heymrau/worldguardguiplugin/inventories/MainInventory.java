package me.heymrau.worldguardguiplugin.inventories;

import com.hakan.inventoryapi.inventory.ClickableItem;
import com.hakan.inventoryapi.inventory.HInventory;
import me.heymrau.wg6.WorldGuard6Hook;
import me.heymrau.worldguardguiplugin.model.CustomItem;
import me.heymrau.worldguardguiplugin.WorldGuardGUIPlugin;
import me.heymrau.worldguardhook.WorldGuardService;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class MainInventory extends Inventory {

    private final WorldGuardGUIPlugin plugin;
    private HInventory inventory;

    public MainInventory(WorldGuardGUIPlugin plugin) {
        this.plugin = plugin;
        createInventory();
    }

    @Override
    void createInventory() {
        final HInventory inventory = plugin.getInventoryAPI().getInventoryCreator().setInventoryType(InventoryType.FURNACE).setTitle("WorldGuard GUI").create();
        final ItemStack info = new CustomItem("&eAbout worldguard gui", Arrays.asList("xxx", "yyy", "zzz"), Material.BOOK, false, (short) 0,1).complete();
        inventory.setItem(2, ClickableItem.empty(info));

        this.inventory = inventory;

    }

    @Override
    public HInventory getInventory(String regionName) {
        final ItemStack regionFlag = new CustomItem("&aManage region flags", null, Material.GRASS, true, (short) 0,1).complete();
        final ItemStack deleteRegion = new CustomItem("&cDelete region " + regionName, null, Material.BARRIER, false, (short) 0,1).complete();
        inventory.setItem(0, ClickableItem.empty(regionFlag));
        inventory.setItem(1, ClickableItem.of(deleteRegion, item -> {
            WorldGuardService worldGuard = new WorldGuard6Hook();
            worldGuard.remove(regionName);
        }));
        return inventory;
    }

    @Override
    public void open(Player player) {
        getInventory("test").open(player);
    }
}
