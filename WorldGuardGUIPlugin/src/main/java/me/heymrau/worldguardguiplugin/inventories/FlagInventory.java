package me.heymrau.worldguardguiplugin.inventories;

import com.cryptomorin.xseries.XMaterial;
import com.hakan.inventoryapi.inventory.ClickableItem;
import com.hakan.inventoryapi.inventory.HInventory;
import com.hakan.inventoryapi.inventory.Pagination;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import me.heymrau.worldguardguiplugin.WorldGuardGUIPlugin;
import me.heymrau.worldguardguiplugin.model.CustomItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class FlagInventory extends Inventory {

    private final WorldGuardGUIPlugin plugin;
    private final List<ClickableItem> clickableItemList = new ArrayList<>();
    private final Set<StateFlag> allFlags;
    private int page;
    private String regionName;
    private HInventory inventory;

    public FlagInventory(WorldGuardGUIPlugin plugin) {
        this.plugin = plugin;
        allFlags = new HashSet<>(plugin.getWorldGuard().getAllFlags());
    }

    public FlagInventory(WorldGuardGUIPlugin plugin, int page) {
        this.plugin = plugin;
        allFlags = new HashSet<>(plugin.getWorldGuard().getAllFlags());
        this.page = page;
    }

    @Override
    void createInventory() {
        final HInventory inventory = plugin.getInventoryAPI().getInventoryCreator().setSize(5).setId("flaginv").setTitle(ChatColor.GRAY + "Flag Management").create();
        inventory.guiAir();
        Pagination pagination = inventory.getPagination();
        pagination.setItemSlots(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35));


        for (StateFlag key : allFlags) {

            final boolean equals = plugin.getWorldGuard().getEnabledFlags(regionName).contains(key);
            final ItemStack item = equals ? getEnabledItem(key) : getDisabledItem(key);

            final ClickableItem clickableItem = ClickableItem.of(item, flag -> {
                Player player = (Player) flag.getWhoClicked();
                final StateFlag flagByName = plugin.getWorldGuard().getFlagByName(ChatColor.stripColor(item.getItemMeta().getDisplayName()));
                if (equals) {
                    plugin.getWorldGuard().denyFlag(regionName, flagByName);
                    inventory.getInventory().setItem(flag.getSlot(), getDisabledItem(key));
                } else {
                    plugin.getWorldGuard().allowFlag(regionName, flagByName);
                    inventory.getInventory().setItem(flag.getSlot(), getEnabledItem(key));
                }
                new FlagInventory(plugin, inventory.getPagination().getPage()).open(player,regionName);
            });


            clickableItemList.add(clickableItem);
        }


        pagination.setItems(clickableItemList);
        inventory.setItem(38, ClickableItem.of(new CustomItem("&6Previous Page", null, Material.ARROW, false, (short) 0, 1).complete(), (event) -> pagination.previousPage()));

        inventory.setItem(40, ClickableItem.of(new CustomItem("&cClose", null, Material.BARRIER, false, (short) 0, 1).complete(), (event) -> event.getWhoClicked().closeInventory()));

        inventory.setItem(42, ClickableItem.of(new CustomItem("&6Next Page", null, Material.ARROW, false, (short) 0, 1).complete(), (event) -> pagination.nextPage()));
        if(page != 0)  pagination.setPage(page);
        this.inventory = inventory;

    }

    private ItemStack getEnabledItem(Flag<?> key) {
        return new CustomItem("&a" + key.getName(), Arrays.asList("&7", "&7Value: &aenabled", "&7", "&7Click to &cdeny &7flag"), XMaterial.GREEN_WOOL.parseItem(), false, (short) 0, 1).complete();
    }

    private ItemStack getDisabledItem(Flag<?> key) {
        return new CustomItem("&c" + key.getName(), Arrays.asList("&7", "&7Value: &cdisabled", "&7", "&7Click to &aallow &7flag"), XMaterial.RED_WOOL.parseItem(), false, (short) 0, 1).complete();
    }

    @Override
    public HInventory getInventory(String regionName, Player player) {
        return inventory;
    }

    @Override
    public void open(Player player, String regionName) {
        this.regionName = regionName;
        createInventory();
        getInventory(regionName, player).open(player);

    }
}


