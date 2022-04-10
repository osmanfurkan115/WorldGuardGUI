package me.heymrau.worldguardguiplugin.inventories;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.heymrau.worldguardguiplugin.WorldGuardGUIPlugin;
import me.heymrau.worldguardguiplugin.utils.Utils;
import me.heymrau.worldguardguiplugin.utils.XMaterial;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class CommandInventory {
    private final WorldGuardGUIPlugin plugin;

    public CommandInventory(WorldGuardGUIPlugin plugin) {
        this.plugin = plugin;
    }

    public void open(Player player, ProtectedRegion region) {
        Gui gui = Gui.gui().rows(5).title(Utils.colored("&7Blocked Commands")).create();

        int i = 0;
        for (String command : plugin.getWorldGuard().getBlockedCommands(region)) {
            if (i <= 35) {
                GuiItem guiItem = ItemBuilder.from(XMaterial.CAULDRON.parseMaterial())
                        .name(Utils.colored("&e" + command))
                        .lore(Component.empty(), Utils.colored("&7Click to remove"))
                        .asGuiItem(event -> {
                            player.sendMessage(ChatColor.YELLOW + "Removed the blocked command " + ChatColor.RED + command);
                            plugin.getWorldGuard().removeBlockedCommand(region, command);
                            gui.close(player);
                        });
                gui.setItem(i, guiItem);
            }
            i++;
        }

        gui.setItem(40, ItemBuilder.from(Material.BARRIER)
                .name(Utils.colored("&cClose"))
                .asGuiItem((e) -> e.getWhoClicked().closeInventory()));
        gui.setItem(43, ItemBuilder.from(XMaterial.GREEN_WOOL.parseItem())
                .name(Utils.colored("&aAdd Command"))
                .asGuiItem((e) -> {
                    player.closeInventory();
                    plugin.getConversationManager().beginBlockCommandConversation(player, region.getId());
                }));

        gui.open(player);

    }

}
