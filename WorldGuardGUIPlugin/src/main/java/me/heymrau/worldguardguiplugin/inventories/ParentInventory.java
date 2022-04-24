package me.heymrau.worldguardguiplugin.inventories;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import me.heymrau.worldguardguiplugin.WorldGuardGUIPlugin;
import me.heymrau.worldguardguiplugin.inventories.permission.InventoryPermission;
import me.heymrau.worldguardguiplugin.model.CustomItem;
import me.heymrau.worldguardguiplugin.utils.Utils;
import me.heymrau.worldguardguiplugin.utils.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ParentInventory extends Inventory {
    private final WorldGuardGUIPlugin plugin;


    public ParentInventory(WorldGuardGUIPlugin plugin) {
        super(InventoryPermission.PARENT);
        this.plugin = plugin;
    }

    public void open(Player player, ProtectedRegion region) {
        PaginatedGui gui = Gui.paginated()
                .rows(5)
                .pageSize(36)
                .title(Utils.colored("&7Parent Management"))
                .disableAllInteractions()
                .create();
        Map<String, ProtectedRegion> regionsByWorld = plugin.getWorldGuard()
                .getRegionsByWorld(player.getWorld().getName());
        regionsByWorld.forEach((key, value) -> {
            if (!value.equals(region)) {
                ProtectedRegion parent = region.getParent();
                List<String> lore = Arrays.asList("&7", "&eClick to set as parent");
                ItemStack enabledItem = new CustomItem("&a" + key + " &8(&eActive&8)", lore, XMaterial.GRASS_BLOCK.parseMaterial(), true, (short) 0, 1).complete();
                ItemStack disabledItem = new CustomItem("&a" + key, lore, XMaterial.GRASS_BLOCK.parseMaterial(), false, (short) 0, 1).complete();
                boolean active = parent != null && parent.equals(value);

                GuiItem guiItem = ItemBuilder.from(active ? enabledItem : disabledItem).asGuiItem(event -> {
                    try {
                        region.setParent(value);
                    } catch (ProtectedRegion.CircularInheritanceException e) {
                        e.printStackTrace();
                    }
                    if (active) gui.setItem(event.getSlot(), ItemBuilder.from(disabledItem).asGuiItem());
                    else gui.setItem(event.getSlot(), ItemBuilder.from(enabledItem).asGuiItem());

                    new ParentInventory(plugin).open(player, region);
                });

                gui.addItem(guiItem);
            }
        });
        plugin.getInventoryManager().setupPageButtons(gui);
        gui.open(player);
    }
}
