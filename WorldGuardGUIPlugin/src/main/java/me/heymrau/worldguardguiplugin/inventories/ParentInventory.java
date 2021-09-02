package me.heymrau.worldguardguiplugin.inventories;

import com.cryptomorin.xseries.XMaterial;
import com.hakan.inventoryapi.inventory.ClickableItem;
import com.hakan.inventoryapi.inventory.HInventory;
import com.hakan.inventoryapi.inventory.Pagination;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.heymrau.worldguardguiplugin.WorldGuardGUIPlugin;
import me.heymrau.worldguardguiplugin.model.CustomItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ParentInventory extends Inventory {
    private final WorldGuardGUIPlugin plugin;
    private final String regionName;
    private HInventory inventory;
    private final List<ClickableItem> clickableItemList = new ArrayList<>();
    private int page;
    private final Player player;


    public ParentInventory(WorldGuardGUIPlugin plugin, String regionName, Player player) {
        this.plugin = plugin;
        this.regionName = regionName;
        this.player = player;
    }

    public ParentInventory(WorldGuardGUIPlugin plugin, String regionName, int page, Player player) {
        this(plugin, regionName, player);
        this.page = page;
    }

    @Override
    void createInventory() {
        final HInventory inventory = plugin.getInventoryAPI().getInventoryCreator().setSize(5).setTitle(ChatColor.GRAY + "Parent Management").create();
        inventory.guiAir();
        Pagination pagination = inventory.getPagination();
        pagination.setItemSlots(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35));

        final Map<String, ProtectedRegion> regionsByWorld = plugin.getWorldGuard().getRegionsByWorld(player.getWorld().getName());

        for(Map.Entry<String, ProtectedRegion> regions: regionsByWorld.entrySet()) {
            if(!regions.getValue().getId().equals(regionName)) {
                final ProtectedRegion activeParent = plugin.getWorldGuard().getRegionByName(regionName).getParent();
                final List<String> lore = Arrays.asList("&7", "&eClick to set as parent");
                final ItemStack enabledItem = new CustomItem("&a" + regions.getKey() + " &8(&eActive&8)", lore, XMaterial.GRASS_BLOCK.parseMaterial(), true, (short) 0, 1).complete();
                final ItemStack disabledItem = new CustomItem("&a" + regions.getKey(), lore, XMaterial.GRASS_BLOCK.parseMaterial(), false, (short) 0, 1).complete();
                final boolean active = activeParent != null && activeParent.getId().equals(regions.getKey());

                final ClickableItem clickableItem =  ClickableItem.of(active ? enabledItem : disabledItem, event -> {
                    try {
                        plugin.getWorldGuard().getRegionByName(regionName).setParent(regions.getValue());
                    } catch (ProtectedRegion.CircularInheritanceException e) {
                        e.printStackTrace();
                    }
                    if(active) inventory.getInventory().setItem(event.getSlot(), disabledItem);
                    else inventory.getInventory().setItem(event.getSlot(), enabledItem);

                    new ParentInventory(plugin, regionName, inventory.getPagination().getPage(), player).open(player,regionName);

                });
                clickableItemList.add(clickableItem);
            }

        }

        pagination.setItems(clickableItemList);
        inventory.setItem(38, ClickableItem.of(new CustomItem("&6Previous Page", null, Material.ARROW, false, (short) 0, 1).complete(), (event) -> pagination.previousPage()));

        inventory.setItem(40, ClickableItem.of(new CustomItem("&cClose", null, Material.BARRIER, false, (short) 0, 1).complete(), (event) -> event.getWhoClicked().closeInventory()));

        inventory.setItem(42, ClickableItem.of(new CustomItem("&6Next Page", null, Material.ARROW, false, (short) 0, 1).complete(), (event) -> pagination.nextPage()));
        if(page != 0)  pagination.setPage(page);
        this.inventory = inventory;
    }

    @Override
    public HInventory getInventory(String regionName, Player player) {
        return inventory;
    }

    @Override
    public void open(Player player, String regionName) {
        createInventory();
        getInventory(regionName, player).open(player);
    }
}
