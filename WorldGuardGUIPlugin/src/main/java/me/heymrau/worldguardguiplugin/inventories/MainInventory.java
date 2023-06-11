package me.heymrau.worldguardguiplugin.inventories;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.heymrau.worldguardguiplugin.WorldGuardGUIPlugin;
import me.heymrau.worldguardguiplugin.inventories.permission.InventoryPermission;
import me.heymrau.worldguardguiplugin.model.Template;
import me.heymrau.worldguardguiplugin.utils.Utils;
import me.heymrau.worldguardguiplugin.utils.XMaterial;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MainInventory extends Inventory {

    private final WorldGuardGUIPlugin plugin;

    public MainInventory(WorldGuardGUIPlugin plugin) {
        this.plugin = plugin;
    }

    public void open(Player player, ProtectedRegion region) {
        // TODO: if is Global region, we should disable a bunch of things
        if (!region.getOwners().contains(player.getUniqueId()) && !player.hasPermission("worldguardgui.admin")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this GUI!");
            return;
        }

        Gui gui = Gui.gui().rows(5).title(Utils.colored("WorldGuard GUI")).disableAllInteractions().create();


        GuiItem flagItem = ItemBuilder.from(XMaterial.GRASS_BLOCK.parseMaterial())
                .name(Utils.colored("&aManage region flags"))
                .asGuiItem((event) -> new FlagInventory(plugin).open(player, region));
        GuiItem parentItem = ItemBuilder.from(Material.ANVIL)
                .name(Utils.colored("&aSet parent region"))
                .asGuiItem((event) -> new ParentInventory(plugin).open(player, region));
        GuiItem templateItem = ItemBuilder.from(XMaterial.CLOCK.parseMaterial())
                .name(Utils.colored("&aTemplates"))
                .asGuiItem((event) -> new TemplateInventory(plugin).open(player, region));
        GuiItem borderItem = ItemBuilder.from(XMaterial.BLUE_DYE.parseItem())
                .name(Utils.colored("&aShow region border"))
                .asGuiItem((event) -> {
                    validatePermission(player, InventoryPermission.OTHER);
                    player.sendMessage(ChatColor.YELLOW + "Border displayed for 15 seconds");
                    player.closeInventory();
                    plugin.getParticleManager().showBorder(player, region);
                });
        GuiItem renameItem = ItemBuilder.from(Material.NAME_TAG)
                .name(Utils.colored("&aRename region"))
                .lore(Component.empty(), Utils.colored("&7Active name: &a" + region.getId()))
                .asGuiItem((event) -> {
                    validatePermission(player, InventoryPermission.OTHER);
                    player.closeInventory();
                    plugin.getConversationManager().beginRegionNameConversation(player, region.getId());
                });
        GuiItem saveTemplateItem = ItemBuilder.from(Material.GOLD_INGOT)
                .name(Utils.colored("&aSave region as template"))
                .asGuiItem((event) -> {
                    validatePermission(player, InventoryPermission.OTHER);
                    player.closeInventory();
                    plugin.getTemplateManager()
                            .addTemplate(new Template(region.getId(), plugin.getWorldGuard()
                                    .getEnabledFlags(region), plugin.getWorldGuard().getDeniedFlags(region)));
                });
        GuiItem commandItem = ItemBuilder.from(XMaterial.COMMAND_BLOCK.parseMaterial())
                .name(Utils.colored("&aBlock command"))
                .asGuiItem((event) -> new CommandInventory(plugin).open(player, region));
        GuiItem deleteItem = ItemBuilder.from(Material.BARRIER)
                .name(Utils.colored("&cDelete region " + region.getId()))
                .asGuiItem((event) -> {
                    validatePermission(player, InventoryPermission.OTHER);
                    new ConfirmationInventory().open(player, (confirmationPlayer) -> {
                        plugin.getWorldGuard().remove(region.getId());
                        confirmationPlayer.sendMessage(ChatColor.GREEN + "Region is deleted successfully");
                    });
                });

        gui.setItem(11, flagItem);
        gui.setItem(12, parentItem);
        gui.setItem(13, templateItem);
        gui.setItem(14, borderItem);
        gui.setItem(15, renameItem);
        gui.setItem(21, saveTemplateItem);
        gui.setItem(23, commandItem);
        gui.setItem(40, deleteItem);

        gui.open(player);
    }

    private void validatePermission(Player player, InventoryPermission inventoryPermission) {
        if (!player.hasPermission(inventoryPermission.getPermission())) {
            player.sendMessage(ChatColor.RED + "&cYou don't have permission to use this inventory");
            player.closeInventory();
        }
    }
}
