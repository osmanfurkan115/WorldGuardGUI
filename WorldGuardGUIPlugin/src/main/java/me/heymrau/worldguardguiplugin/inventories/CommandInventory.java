package me.heymrau.worldguardguiplugin.inventories;

import com.cryptomorin.xseries.XMaterial;
import com.hakan.inventoryapi.inventory.ClickableItem;
import com.hakan.inventoryapi.inventory.HInventory;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.RequiredArgsConstructor;
import me.heymrau.worldguardguiplugin.WorldGuardGUIPlugin;
import me.heymrau.worldguardguiplugin.model.CustomItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@RequiredArgsConstructor
public class CommandInventory extends Inventory {
    private final WorldGuardGUIPlugin plugin;
    private final Player player;
    private final String regionName;

    private HInventory inventory;

    @Override
    void createInventory() {
        final ProtectedRegion region = plugin.getWorldGuard().getRegionByName(regionName);
        final HInventory inventory = plugin.getInventoryAPI().getInventoryCreator().setSize(5).setTitle(ChatColor.GRAY + "Blocked Commands").create();
        inventory.guiAir();
        int i = 0;
        for (String command : plugin.getWorldGuard().getBlockedCommands(region)) {
            if (i <= 35) {
                ItemStack itemStack = new CustomItem("&e" + command, Arrays.asList("", "&cClick to remove"),
                        XMaterial.CAULDRON.parseMaterial(), false, (short) 0, 1).complete();
                inventory.setItem(i, ClickableItem.of(itemStack, event -> {
                    player.sendMessage(ChatColor.YELLOW + "Removed the blocked command &c" + command);
                    plugin.getWorldGuard().removeBlockedCommand(region, command);
                    inventory.close(player);

                }));
            }
            i++;
        }
        inventory.setItem(40, ClickableItem.of(new CustomItem("&cClose", null, Material.BARRIER, false, (short) 0, 1).complete(), (event) -> event.getWhoClicked().closeInventory()));
        this.inventory = inventory;
    }

    @Override
    public HInventory getInventory(String regionName, Player player) {
        return this.inventory;
    }

    @Override
    public void open(Player player, String regionName) {
        createInventory();
        getInventory(regionName, player).open(player);
    }
}
