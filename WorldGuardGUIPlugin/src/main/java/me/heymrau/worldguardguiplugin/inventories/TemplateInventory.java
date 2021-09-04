package me.heymrau.worldguardguiplugin.inventories;

import com.cryptomorin.xseries.XMaterial;
import com.hakan.inventoryapi.inventory.ClickableItem;
import com.hakan.inventoryapi.inventory.HInventory;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.RequiredArgsConstructor;
import me.heymrau.worldguardguiplugin.WorldGuardGUIPlugin;
import me.heymrau.worldguardguiplugin.model.CustomItem;
import me.heymrau.worldguardguiplugin.model.Template;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
public class TemplateInventory extends Inventory {
    private final WorldGuardGUIPlugin plugin;
    private final String regionName;
    private final Player player;

    private HInventory inventory;

    @Override
    void createInventory() {
        final HInventory inventory = plugin.getInventoryAPI().getInventoryCreator().setSize(5).setTitle(ChatColor.GRAY + "Template Management").create();
        inventory.guiAir();
        int i = 0;
        for(Template template: plugin.getTemplateManager().getTemplatesList()) {
            if(i<=35) {
                List<String> lore = getLore(template);
                ItemStack itemStack = new CustomItem("&e" + template.getName(), lore, XMaterial.GRASS_BLOCK.parseMaterial(), false, (short) 0,1).complete();
                inventory.setItem(i, ClickableItem.of(itemStack, event -> {
                    ProtectedRegion region = plugin.getWorldGuard().getRegionByName(regionName);
                    final HashMap<Flag<?>, Object> flags = new HashMap<>();
                    template.getEnabledFlags().forEach(flag -> {
                        if(flag != null) flags.put(flag, StateFlag.State.ALLOW);
                    });
                    template.getDeniedFlags().forEach(flag -> {
                        if(flag != null) flags.put(flag, StateFlag.State.DENY);
                    });
                    region.setFlags(flags);
                    player.sendMessage(ChatColor.YELLOW + "Region template changed");
                    inventory.close(player);

                }));
            }
            i++;
        }
        inventory.setItem(40, ClickableItem.of(new CustomItem("&cClose", null, Material.BARRIER, false, (short) 0, 1).complete(), (event) -> event.getWhoClicked().closeInventory()));
        this.inventory = inventory;
    }

    private List<String> getLore(Template template) {
        List<String> lore = new ArrayList<>();
        lore.add("&7");
        lore.add("&7Allowed Flags:");
        template.getEnabledFlags().forEach(flag -> {
            if(flag != null) lore.add(" &8- &a" + flag.getName());
        });
        lore.add("&7");
        lore.add("&7Denied Flags:");
        template.getDeniedFlags().forEach(flag -> {
            if(flag != null) lore.add(" &8- &c" + flag.getName());
        });
        lore.add("&7");
        lore.add("&eClick to set as template");
        return lore;
    }

    @Override
    public HInventory getInventory(String regionName, Player player) {
        return this.inventory;
    }

    @Override
    public void open(Player player, String regionName) {
        createInventory();
        getInventory(regionName,player).open(player);
    }
}
