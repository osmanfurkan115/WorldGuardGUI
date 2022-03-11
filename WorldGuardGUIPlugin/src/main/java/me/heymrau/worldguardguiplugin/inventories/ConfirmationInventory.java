package me.heymrau.worldguardguiplugin.inventories;

import com.hakan.inventoryapi.inventory.ClickableItem;
import com.hakan.inventoryapi.inventory.HInventory;
import lombok.RequiredArgsConstructor;
import me.heymrau.worldguardguiplugin.WorldGuardGUIPlugin;
import me.heymrau.worldguardguiplugin.model.CustomItem;
import me.heymrau.worldguardguiplugin.utils.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class ConfirmationInventory {
    private final WorldGuardGUIPlugin plugin;

    public void open(Player player, Consumer<Player> onConfirm) {
        final HInventory inventory = plugin.getInventoryAPI().getInventoryCreator().setSize(3).setTitle(ChatColor.RED + "Confirmation").create();
        inventory.setItem(11, ClickableItem.of(new CustomItem("&aConfirm", null, XMaterial.GREEN_WOOL.parseItem(), false, (short) 0, 1).complete(),
                (event) -> {
                    player.closeInventory();
                    onConfirm.accept(player);
                }));

        inventory.setItem(15, ClickableItem.of(new CustomItem("&cClose", null, XMaterial.RED_WOOL.parseItem(), false, (short) 0, 1).complete(),
                (event) -> player.closeInventory()));

        inventory.open(player);
    }
}
