package me.heymrau.worldguardguiplugin.inventories;

import com.hakan.inventoryapi.inventory.ClickableItem;
import com.hakan.inventoryapi.inventory.HInventory;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.heymrau.worldguardguiplugin.WorldGuardGUIPlugin;
import me.heymrau.worldguardguiplugin.chat.BlockedCommandPrompt;
import me.heymrau.worldguardguiplugin.model.CustomItem;
import me.heymrau.worldguardguiplugin.utils.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;

public class CommandInventory extends Inventory {
    private final WorldGuardGUIPlugin plugin;
    private final Player player;
    private final String regionName;
    private final ConversationFactory commandConversationFactory;

    private HInventory inventory;

    public CommandInventory(WorldGuardGUIPlugin plugin, Player player, String regionName) {
        this.plugin = plugin;
        this.player = player;
        this.regionName = regionName;
        commandConversationFactory = new ConversationFactory(plugin)
                .withLocalEcho(false)
                .withFirstPrompt(new BlockedCommandPrompt(plugin.getWorldGuard()))
                .withTimeout(30)
                .withEscapeSequence("cancel");
    }

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
                    player.sendMessage(ChatColor.YELLOW + "Removed the blocked command " + ChatColor.RED + command);
                    plugin.getWorldGuard().removeBlockedCommand(region, command);
                    inventory.close(player);

                }));
            }
            i++;
        }

        inventory.setItem(40, ClickableItem.of(new CustomItem("&cClose", null, Material.BARRIER, false, (short) 0, 1).complete(), (event) -> event.getWhoClicked().closeInventory()));
        inventory.setItem(43, ClickableItem.of(new CustomItem("&aAdd Blocked Command", null, XMaterial.GREEN_WOOL.parseItem(), false, (short) 0, 1).complete(), (event) -> {
            player.closeInventory();
            beginCommandChangeConversation(regionName, player);
        }));
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

    private void beginCommandChangeConversation(String regionName, Player player) {
        final HashMap<Object, Object> dataMap = new HashMap<>();
        dataMap.put("regionName", regionName);

        commandConversationFactory.withInitialSessionData(dataMap).buildConversation(player).begin();
    }
}
