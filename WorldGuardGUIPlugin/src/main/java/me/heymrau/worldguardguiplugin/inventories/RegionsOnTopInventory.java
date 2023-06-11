package me.heymrau.worldguardguiplugin.inventories;

import com.google.common.collect.Sets;
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
import java.util.stream.Collectors;

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
        Location location = player.getLocation();
        WorldGuardLocation worldGuardLocation = new WorldGuardLocation(player.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
        Set<ProtectedRegion> applicableRegions = plugin.getWorldGuard().getApplicableRegions(worldGuardLocation);

        if (!player.hasPermission("worldguardgui.admin")) {
            applicableRegions = Sets.newLinkedHashSet(applicableRegions.stream().filter((i) -> i.getOwners().contains(player.getUniqueId())).collect(Collectors.toList()));
        }

        if(applicableRegions.isEmpty()) {
            player.sendMessage(ChatColor.RED + "You are not in a Region that you own!");
            return;
        }

        if(applicableRegions.size() == 1) {
            new MainInventory(plugin).open(player, applicableRegions.stream().findFirst().get());
            return;
        }

        PaginatedGui gui = Gui.paginated()
                .rows(2)
                .pageSize(9)
                .title(Utils.colored("&7Regions on Top"))
                .disableAllInteractions()
                .create();

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