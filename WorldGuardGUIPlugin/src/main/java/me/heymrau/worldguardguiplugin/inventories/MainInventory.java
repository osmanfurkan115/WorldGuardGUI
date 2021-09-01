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
import org.bukkit.scheduler.BukkitRunnable;

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
        final HInventory inventory = plugin.getInventoryAPI().getInventoryCreator().setSize(4).setTitle("WorldGuard GUI").create();
        final ItemStack info = new CustomItem("&eAbout plugin", Arrays.asList("&7", "&7You can manage your", "&7flags and can set", "&7their value to &aALLOW", "&7or &cDENY &7easily"), Material.BOOK, false, (short) 0,1).complete();
        inventory.setItem(35, ClickableItem.empty(info));

        this.inventory = inventory;

    }

    @Override
    public HInventory getInventory(String regionName, Player player) {
        final ItemStack regionFlag = new CustomItem("&aManage region flags", null, XMaterial.GRASS_BLOCK.parseMaterial(), false, (short) 0,1).complete();
        final ItemStack deleteRegion = new CustomItem("&cDelete region " + regionName, null, Material.BARRIER, false, (short) 0,1).complete();
        final ItemStack parent = new CustomItem("&aSet region parent", null, Material.ANVIL, false, (short) 0,1).complete();
        final ItemStack rename = new CustomItem("&aRename region", Arrays.asList("&7", "&7Active name: &a" + regionName), Material.NAME_TAG, false, (short) 0,1).complete();
        inventory.setItem(11, ClickableItem.of(regionFlag, item -> new FlagInventory(plugin).open(player,regionName)));
        inventory.setItem(31, ClickableItem.of(deleteRegion, item -> {
            plugin.getWorldGuard().remove(regionName);
            player.closeInventory();
            player.sendMessage(ChatColor.GREEN + "Region deleted succesfully");
        }));
        inventory.setItem(15, ClickableItem.of(parent, item -> new ParentInventory(plugin, regionName, player).open(player,regionName)));
        inventory.setItem(13, ClickableItem.of(rename, item -> {
            player.closeInventory();
            player.sendMessage(ChatColor.YELLOW + "Type a new name for the region named " + regionName);
            player.sendMessage(ChatColor.YELLOW + "You have 30 seconds");
            plugin.getChatInput().put(player, regionName);
            new BukkitRunnable() {
                @Override
                public void run() {
                    plugin.getChatInput().remove(player);
                    player.sendMessage(ChatColor.RED + "You didn't type any name in 30 seconds!");
                }
            }.runTaskLater(plugin, 20*30L);
        }));
        return inventory;
    }

    @Override
    public void open(Player player, String regionName) {
        getInventory(regionName, player).open(player);
    }
}
