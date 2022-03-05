package me.heymrau.worldguardguiplugin.inventories;

import com.hakan.inventoryapi.inventory.ClickableItem;
import com.hakan.inventoryapi.inventory.HInventory;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.heymrau.worldguardguiplugin.WorldGuardGUIPlugin;
import me.heymrau.worldguardguiplugin.chat.RegionNamePrompt;
import me.heymrau.worldguardguiplugin.model.CustomItem;
import me.heymrau.worldguardguiplugin.model.Template;
import me.heymrau.worldguardguiplugin.utils.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;

public class MainInventory extends Inventory {

    private final WorldGuardGUIPlugin plugin;
    private final ConversationFactory nameConversationFactory;
    private HInventory inventory;

    public MainInventory(WorldGuardGUIPlugin plugin) {
        this.plugin = plugin;
        createInventory();
        nameConversationFactory = new ConversationFactory(plugin)
                .withLocalEcho(false)
                .withFirstPrompt(new RegionNamePrompt(plugin.getWorldGuard()))
                .withTimeout(30)
                .withEscapeSequence("cancel");
    }

    @Override
    void createInventory() {
        final HInventory inventory = plugin.getInventoryAPI().getInventoryCreator().setSize(5).setTitle("WorldGuard GUI").create();
        final ItemStack info = new CustomItem("&eAbout plugin", Arrays.asList("&7", "&7You can manage your", "&7flags and can set", "&7their value to &aALLOW", "&7or &cDENY &7easily"), Material.BOOK, false, (short) 0, 1).complete();
        inventory.setItem(44, ClickableItem.empty(info));

        this.inventory = inventory;

    }

    @Override
    public HInventory getInventory(String regionName, Player player) {
        final ItemStack rename = new CustomItem("&aRename region", Arrays.asList("&7", "&7Active name: &a" + regionName), Material.NAME_TAG, false, (short) 0, 1).complete();
        final ItemStack border = new CustomItem("&aShow border", null, XMaterial.BLUE_DYE.parseItem(), false, (short) 0, 1).complete();
        final ItemStack saveAsTemplate = new CustomItem("&aSave as template", null, XMaterial.GOLD_INGOT.parseItem(), false, (short) 0, 1).complete();
        final ItemStack blockedCommands = new CustomItem("&aBlocked Commands", null, XMaterial.CAULDRON.parseItem(), false, (short) 0, 1).complete();
        inventory.setItem(11, ClickableItem.of(new CustomItem("&aManage region flags", null, XMaterial.GRASS_BLOCK.parseMaterial(), false, (short) 0, 1).complete(), item -> new FlagInventory(plugin).open(player, regionName)));
        inventory.setItem(12, ClickableItem.of(new CustomItem("&aSet region parent", null, Material.ANVIL, false, (short) 0, 1).complete(), item -> new ParentInventory(plugin, regionName, player).open(player, regionName)));
        inventory.setItem(13, ClickableItem.of(new CustomItem("&aTemplates", null, XMaterial.CLOCK.parseItem(), false, (short) 0, 1).complete(), item -> new TemplateInventory(plugin, regionName, player).open(player, regionName)));
        final ProtectedRegion region = plugin.getWorldGuard().getRegionByName(regionName);

        inventory.setItem(14, ClickableItem.of(border, item -> {
            player.sendMessage(ChatColor.YELLOW + "Border displayed for 15 seconds");
            plugin.getParticleManager().showBorder(player, region);
        }));

        inventory.setItem(15, ClickableItem.of(rename, item -> {
            player.closeInventory();
            beginNameChangeConversation(regionName, player);
        }));

        inventory.setItem(21, ClickableItem.of(saveAsTemplate, item -> plugin.getTemplateManager().addTemplate(new Template(regionName, plugin.getWorldGuard().getEnabledFlags(region), plugin.getWorldGuard().getDeniedFlags(region)))));

        inventory.setItem(23, ClickableItem.of(blockedCommands, event -> new CommandInventory(plugin, player, regionName).open(player, regionName)));

        inventory.setItem(40, ClickableItem.of(new CustomItem("&cDelete region " + regionName, null, Material.BARRIER, false, (short) 0, 1).complete(), item -> {
            plugin.getWorldGuard().remove(regionName);
            player.closeInventory();
            player.sendMessage(ChatColor.GREEN + "Region is deleted successfully");
        }));
        return inventory;
    }

    private void beginNameChangeConversation(String regionName, Player player) {
        final HashMap<Object, Object> dataMap = new HashMap<>();
        dataMap.put("regionName", regionName);

        nameConversationFactory.withInitialSessionData(dataMap).buildConversation(player).begin();
    }

    @Override
    public void open(Player player, String regionName) {
        getInventory(regionName, player).open(player);
    }
}
