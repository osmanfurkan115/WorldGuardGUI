package me.heymrau.worldguardguiplugin.managers;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.PaginatedGui;
import me.heymrau.worldguardguiplugin.utils.Utils;
import org.bukkit.Material;

public class InventoryManager {
    public void setupPageButtons(PaginatedGui paginatedGui) {
        int finalRow = paginatedGui.getRows();
        paginatedGui.setItem(finalRow, 3, ItemBuilder.from(Material.ARROW)
                .name(Utils.colored("&6Previous Page"))
                .asGuiItem(event -> paginatedGui.previous()));
        paginatedGui.setItem(finalRow, 5, ItemBuilder.from(Material.BARRIER)
                .name(Utils.colored("&cClose"))
                .asGuiItem(event1 -> paginatedGui.close(event1.getWhoClicked())));
        paginatedGui.setItem(finalRow, 7, ItemBuilder.from(Material.ARROW)
                .name(Utils.colored("&6Next Page"))
                .asGuiItem(event -> paginatedGui.next()));
    }
}
