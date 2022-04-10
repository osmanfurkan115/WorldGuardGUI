package me.heymrau.worldguardguiplugin.inventories;

import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.RequiredArgsConstructor;
import me.heymrau.worldguardguiplugin.WorldGuardGUIPlugin;
import me.heymrau.worldguardguiplugin.model.CustomItem;
import me.heymrau.worldguardguiplugin.model.Template;
import me.heymrau.worldguardguiplugin.utils.Utils;
import me.heymrau.worldguardguiplugin.utils.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
public class TemplateInventory {
    private final WorldGuardGUIPlugin plugin;

    private List<String> getLore(Template template) {
        List<String> lore = new ArrayList<>();
        lore.add("&7");
        lore.add("&7Allowed Flags:");
        template.getEnabledFlags().forEach(flag -> {
            if (flag != null) lore.add(" &8- &a" + flag.getName());
        });
        lore.add("&7");
        lore.add("&7Denied Flags:");
        template.getDeniedFlags().forEach(flag -> {
            if (flag != null) lore.add(" &8- &c" + flag.getName());
        });
        lore.add("&7");
        lore.add("&eClick to set as template");
        return lore;
    }

    public void open(Player player, ProtectedRegion region) {
        PaginatedGui gui = Gui.paginated().rows(5).pageSize(36).title(Utils.colored("&7Template Management")).create();
        for (Template template : plugin.getTemplateManager().getTemplatesList()) {
            List<String> lore = getLore(template);
            ItemStack itemStack = new CustomItem("&e" + template.getName(), lore, XMaterial.GRASS_BLOCK.parseMaterial(), false, (short) 0, 1).complete();
            gui.addItem(ItemBuilder.from(itemStack).asGuiItem(event -> {
                HashMap<Flag<?>, Object> flags = new HashMap<>();
                template.getEnabledFlags()
                        .forEach(flag -> flags.put(flag, StateFlag.State.ALLOW));
                template.getDeniedFlags()
                        .forEach(flag -> flags.put(flag, StateFlag.State.DENY));
                region.setFlags(flags);
                player.sendMessage(ChatColor.YELLOW + "Region template changed");
                gui.close(player);
            }));
        }
        plugin.getInventoryManager().setupPageButtons(gui);
        gui.open(player);
    }
}
