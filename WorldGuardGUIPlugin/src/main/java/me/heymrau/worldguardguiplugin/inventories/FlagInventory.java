package me.heymrau.worldguardguiplugin.inventories;

import com.hakan.inventoryapi.inventory.ClickableItem;
import com.hakan.inventoryapi.inventory.HInventory;
import com.hakan.inventoryapi.inventory.Pagination;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.heymrau.worldguardguiplugin.WorldGuardGUIPlugin;
import me.heymrau.worldguardguiplugin.model.CustomItem;
import me.heymrau.worldguardguiplugin.utils.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
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
        this(plugin);
        this.page = page;
    }

    @Override
    void createInventory() {
        final HInventory inventory = plugin.getInventoryAPI().getInventoryCreator().setSize(5).setId("flaginv").setTitle(ChatColor.GRAY + "Flag Management").create();
        Pagination pagination = inventory.getPagination();
        plugin.getInventoryManager().setupInventory(inventory, pagination);
        for (StateFlag key : allFlags) {
            ProtectedRegion region = plugin.getWorldGuard().getRegionByName(regionName);
            final boolean equals = plugin.getWorldGuard().getEnabledFlags(region).contains(key);
            final ItemStack item = equals ? getEnabledItem(key) : getDisabledItem(key);

            final ClickableItem clickableItem = ClickableItem.of(item, flag -> {
                Player player = (Player) flag.getWhoClicked();
                final StateFlag flagByName = plugin.getWorldGuard().getFlagByName(ChatColor.stripColor(item.getItemMeta().getDisplayName()));
                if (equals) denyFlag(region, flagByName, inventory, key, flag);
                else allowFlag(inventory, key, region, flag, flagByName);
                new FlagInventory(plugin, inventory.getPagination().getPage()).open(player, regionName);
            });


            clickableItemList.add(clickableItem);
        }


        pagination.setItems(clickableItemList);
        plugin.getInventoryManager().setupPageButtons(inventory, pagination);
        if (page != 0) pagination.setPage(page);
        this.inventory = inventory;

    }




    private void allowFlag(HInventory inventory, StateFlag key, ProtectedRegion region, InventoryClickEvent flag, StateFlag flagByName) {
        plugin.getWorldGuard().allowFlag(region, flagByName);
        inventory.getInventory().setItem(flag.getSlot(), getEnabledItem(key));
    }

    private void denyFlag(ProtectedRegion region, StateFlag flagByName, HInventory inventory, StateFlag key, InventoryClickEvent flag) {
        plugin.getWorldGuard().denyFlag(region, flagByName);
        inventory.getInventory().setItem(flag.getSlot(), getDisabledItem(key));
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


