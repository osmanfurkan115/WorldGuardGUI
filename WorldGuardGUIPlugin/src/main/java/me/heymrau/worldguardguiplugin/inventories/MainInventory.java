package me.heymrau.worldguardguiplugin.inventories;

import com.cryptomorin.xseries.XMaterial;
import com.hakan.inventoryapi.inventory.ClickableItem;
import com.hakan.inventoryapi.inventory.HInventory;
import me.heymrau.worldguardguiplugin.model.CustomItem;
import me.heymrau.worldguardguiplugin.WorldGuardGUIPlugin;
import org.bukkit.ChatColor;
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
        final ItemStack info = new CustomItem("&eAbout plugin", Arrays.asList("&7", "&7You can manage your", "&7flags and can set", "&7their value to &aALLOW", "&7or &cDENY &7easily"), Material.BOOK, false, (short) 0,1).complete();
        inventory.setItem(2, ClickableItem.empty(info));

        this.inventory = inventory;

    }

    @Override
    public HInventory getInventory(String regionName, Player player) {
        final ItemStack regionFlag = new CustomItem("&aManage region flags", null, XMaterial.GRASS_BLOCK.parseMaterial(), false, (short) 0,1).complete();
        final ItemStack deleteRegion = new CustomItem("&cDelete region " + regionName, null, Material.BARRIER, false, (short) 0,1).complete();
        inventory.setItem(0, ClickableItem.of(regionFlag, item -> new FlagInventory(plugin).open(player,regionName)));
        inventory.setItem(1, ClickableItem.of(deleteRegion, item -> {
            plugin.getWorldGuard().remove(regionName);
            player.closeInventory();
            player.sendMessage(ChatColor.GREEN + "Region deleted succesfully");
        }));
        return inventory;
    }

    @Override
    public void open(Player player, String regionName) {
        getInventory(regionName, player).open(player);
    }
}
