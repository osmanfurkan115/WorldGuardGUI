package me.heymrau.worldguardguiplugin.inventories;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import lombok.RequiredArgsConstructor;
import me.heymrau.worldguardguiplugin.utils.Utils;
import me.heymrau.worldguardguiplugin.utils.XMaterial;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class ConfirmationInventory {

    public void open(Player player, Consumer<Player> onConfirm) {
        Gui gui = Gui.gui().rows(3).title(Utils.colored("&cConfirmation")).create();

        gui.setItem(11, ItemBuilder.from(XMaterial.GREEN_WOOL.parseItem())
                .name(Utils.colored("&aConfirm"))
                .asGuiItem((e) -> {
                    player.closeInventory();
                    onConfirm.accept(player);
                }));
        gui.setItem(15, ItemBuilder.from(XMaterial.RED_WOOL.parseItem())
                .name(Utils.colored("&cCancel"))
                .asGuiItem((e) -> player.closeInventory()));

        gui.open(player);
    }
}
