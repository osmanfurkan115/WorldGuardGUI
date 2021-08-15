package me.heymrau.worldguardgui.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

@AllArgsConstructor
@Data
public class CustomItem {
    private String name;

    private List<String> lore;

    private Material material;

    private boolean glow;

    private short data;

    private int amount;


    public ItemStack complete() {
        ItemStack returnItem = new ItemStack(getMaterial(), getAmount(), getData());

        ItemMeta itemMeta = returnItem.getItemMeta();
        if (itemMeta != null) {
            if(lore != null) itemMeta.setLore(getLore());

            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.name));

            if (isGlow()) {
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                itemMeta.addEnchant(Enchantment.LUCK, 1, false);
            }
        }
        returnItem.setItemMeta(itemMeta);
        return returnItem;
    }

}

