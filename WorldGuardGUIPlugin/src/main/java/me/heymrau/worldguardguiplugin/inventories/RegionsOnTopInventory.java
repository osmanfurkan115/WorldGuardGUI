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
import me.heymrau.worldguardhook.WorldGuardLocation;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Menu with all the regions that the player is currently on top.
 */
public class RegionsOnTopInventory extends Inventory {
    private final WorldGuardGUIPlugin plugin;


    public RegionsOnTopInventory(WorldGuardGUIPlugin plugin) {
        super(InventoryPermission.PARENT);
        this.plugin = plugin;
    }

    public void open(Player player, ProtectedRegion _ignore) {
        // TODO: support filtering if the player is Owner
        // TODO: if there is only one region, open directly the MainInventoryMenu
        if (!player.hasPermission("worldguardgui.admin")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this GUI!");
            return;
        }

        PaginatedGui gui = Gui.paginated()
                .rows(1)
                .pageSize(9)
                .title(Utils.colored("&7Regions on Top"))
                .disableAllInteractions()
                .create();
        Location location = player.getLocation();
        WorldGuardLocation worldGuardLocation = new WorldGuardLocation(player.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
        Set<ProtectedRegion> applicableRegions = plugin.getWorldGuard().getApplicableRegions(worldGuardLocation);

        applicableRegions.forEach((region) -> {
            XMaterial iconMaterial = region.getId().equals(ProtectedRegion.GLOBAL_REGION) ? XMaterial.BEDROCK : XMaterial.GRASS_BLOCK;

            List<String> lore = Arrays.asList("&7", "&eClick to open Region configuration");
            ItemStack regionItem = new CustomItem("&a" + region.getId(), lore, iconMaterial.parseMaterial(), false, (short) 0, 1).complete();

            GuiItem guiItem = ItemBuilder.from(regionItem).asGuiItem(event -> {
                new MainInventory(plugin).open(player, region);
            });

            gui.addItem(guiItem);
        });
        plugin.getInventoryManager().setupPageButtons(gui);
        gui.open(player);
    }
}