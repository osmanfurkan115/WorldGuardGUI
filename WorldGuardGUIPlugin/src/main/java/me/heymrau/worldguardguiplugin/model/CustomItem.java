package me.heymrau.worldguardguiplugin.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CustomItem {
    private final String name;

    private final List<String> lore;

    private Material material;

    private final boolean glow;

    private final short data;

    private final int amount;

    private ItemStack itemStack;

    public CustomItem(String name, List<String> lore, Material material, boolean glow, short data, int amount) {
        this.name = name;
        this.lore = lore;
        this.material = material;
        this.glow = glow;
        this.data = data;
        this.amount = amount;
    }

    public CustomItem(String name, List<String> lore, ItemStack itemStack, boolean glow, short data, int amount) {
        this.name = name;
        this.lore = lore;
        this.glow = glow;
        this.data = data;
        this.amount = amount;
        this.itemStack = itemStack;
    }

    public ItemStack complete() {
        ItemStack returnItem = itemStack == null ? new ItemStack(getMaterial(), getAmount(), getData()) : itemStack;

        ItemMeta itemMeta = returnItem.getItemMeta();
        if (itemMeta != null) {
            if(lore != null) {
                itemMeta.setLore(lore.stream().map(line -> line = ChatColor.translateAlternateColorCodes('&', line)).collect(Collectors.toList()));
            }

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

