package me.heymrau.worldguardguiplugin.inventories;

import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import me.heymrau.worldguardguiplugin.WorldGuardGUIPlugin;
import me.heymrau.worldguardguiplugin.inventories.permission.InventoryPermission;
import me.heymrau.worldguardguiplugin.model.CustomItem;
import me.heymrau.worldguardguiplugin.utils.Utils;
import me.heymrau.worldguardguiplugin.utils.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FlagInventory extends Inventory {

    private final WorldGuardGUIPlugin plugin;
    private final Set<StateFlag> allFlags;

    public FlagInventory(WorldGuardGUIPlugin plugin) {
        super(InventoryPermission.FLAG);
        this.plugin = plugin;
        allFlags = new HashSet<>(plugin.getWorldGuard().getAllFlags());
    }

    @Override
    public void open(Player player, ProtectedRegion region) {
        if (!checkPermission(player)) return;
        PaginatedGui gui = Gui.paginated()
                .rows(5)
                .pageSize(36)
                .title(Utils.colored("&7Flag Management"))
                .disableAllInteractions()
                .create();
        for (StateFlag key : allFlags) {
            boolean equals = plugin.getWorldGuard().getEnabledFlags(region).contains(key);
            ItemStack item = equals ? getEnabledItem(key) : getDisabledItem(key);

            gui.addItem(ItemBuilder.from(item).asGuiItem(event -> {
                if (equals) denyFlag(gui, key, region, event);
                else allowFlag(gui, key, region, event);
                new FlagInventory(plugin).open(player, region);
            }));
        }
        plugin.getInventoryManager().setupPageButtons(gui);
        gui.open(player);
    }

    private void allowFlag(PaginatedGui gui, StateFlag flag, ProtectedRegion region, InventoryClickEvent event) {
        plugin.getWorldGuard().allowFlag(region, flag);
        gui.setItem(event.getSlot(), ItemBuilder.from(getEnabledItem(flag)).asGuiItem());
    }

    private void denyFlag(PaginatedGui gui, StateFlag flag, ProtectedRegion region, InventoryClickEvent event) {
        plugin.getWorldGuard().denyFlag(region, flag);
        gui.setItem(event.getSlot(), ItemBuilder.from(getEnabledItem(flag)).asGuiItem());
    }

    private ItemStack getEnabledItem(Flag<?> key) {
        return new CustomItem("&a" + key.getName(), Arrays.asList("&7", "&7Value: &aenabled", "&7", "&7Click to &cdeny &7flag"), XMaterial.GREEN_WOOL.parseItem(), false, (short) 0, 1).complete();
    }

    private ItemStack getDisabledItem(Flag<?> key) {
        return new CustomItem("&c" + key.getName(), Arrays.asList("&7", "&7Value: &cdisabled", "&7", "&7Click to &aallow &7flag"), XMaterial.RED_WOOL.parseItem(), false, (short) 0, 1).complete();
    }
}


