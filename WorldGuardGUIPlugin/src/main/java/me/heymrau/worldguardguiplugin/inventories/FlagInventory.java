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

public class FlagInventory extends Inventory{

    private final WorldGuardGUIPlugin plugin;
    private HInventory inventory;
    private final String regionName;
    List<ClickableItem> clickableItemList = new ArrayList<>();

    public FlagInventory(WorldGuardGUIPlugin plugin, String regionName) {
        this.plugin = plugin;
        this.regionName = regionName;
        createInventory();
    }


    //Bad practice, I'll change it ASAP
    @Override
    void createInventory() {
        final HInventory inventory = plugin.getInventoryAPI().getInventoryCreator().setSize(5).setId("flaginv").setTitle(ChatColor.GRAY + "Flag Management").create();
        Pagination pagination = inventory.getPagination();
        pagination.setItemSlots(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35));



        final ItemStack backItem = new CustomItem("&cClose", null, Material.BARRIER, false, (short) 0,1).complete();

        final List<StateFlag> allFlags = plugin.getWorldGuard().getAllFlags();
        final List<StateFlag> enabledFlags = plugin.getWorldGuard().getEnabledFlags(regionName);

        for (StateFlag key : allFlags) {

            final boolean equals = enabledFlags.contains(key);
            final ItemStack item = equals ? getEnabledItem(key) : getDisabledItem(key);

            clickableItemList.add(ClickableItem.of(item, flag -> {

                final StateFlag flagByName = plugin.getWorldGuard().getFlagByName(ChatColor.stripColor(item.getItemMeta().getDisplayName()));
                if (equals) {
                    plugin.getWorldGuard().denyFlag(regionName, flagByName);
                    inventory.setItem(flag.getSlot(), ClickableItem.empty(getDisabledItem(key)));
//                    flag.setCurrentItem(getDisabledItem(key));
                } else {
                    plugin.getWorldGuard().allowFlag(regionName, flagByName);
                    inventory.setItem(flag.getSlot(), ClickableItem.empty(getEnabledItem(key)));

//                    flag.setCurrentItem(getEnabledItem(key));
                }
                Player player = (Player) flag.getWhoClicked();
                inventory.clone().open(player);
//
//                inventory.close(player);



            }));
        }


        pagination.setItems(clickableItemList);
        inventory.setItem(38, ClickableItem.of(new CustomItem("&6Previous Page", null, Material.ARROW, false, (short)0,1).complete(), (event) -> pagination.previousPage()));

        inventory.setItem(40, ClickableItem.of(backItem, (event) -> event.getWhoClicked().closeInventory()));

        inventory.setItem(42, ClickableItem.of(new CustomItem("&6Next Page", null, Material.ARROW, false, (short)0,1).complete(), (event) -> pagination.nextPage()));
        this.inventory = inventory;

    }

    private ItemStack getEnabledItem(Flag<?> key) {
        return new CustomItem("&a" + key.getName(), Arrays.asList("&7", "&7Value: &aenabled", "&7", "&7Click to &cdeny &7flag"), XMaterial.GREEN_WOOL.parseItem(), false, (short) 0,1).complete();
    }

    private ItemStack getDisabledItem(Flag<?> key) {
        return new CustomItem("&c" + key.getName(), Arrays.asList("&7", "&7Value: &cdisabled", "&7", "&7Click to &aallow &7flag"), XMaterial.RED_WOOL.parseItem(), false, (short) 0,1).complete();
    }

    @Override
    public HInventory getInventory(String regionName, Player player) {
        return inventory;
    }

    @Override
    public void open(Player player, String regionName) {
        getInventory(regionName, player).open(player);
    }
}
