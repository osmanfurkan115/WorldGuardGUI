package me.heymrau.worldguardguiplugin.model;

import me.heymrau.worldguardguiplugin.WorldGuardGUIPlugin;
import me.heymrau.worldguardguiplugin.chat.BlockedCommandPrompt;
import me.heymrau.worldguardguiplugin.chat.RegionNamePrompt;
import me.heymrau.worldguardhook.WorldGuardService;
import org.bukkit.conversations.Prompt;
import org.bukkit.plugin.java.JavaPlugin;

public class ConversationType {
    private static final WorldGuardService plugin = JavaPlugin.getPlugin(WorldGuardGUIPlugin.class).getWorldGuard();

    public static final Prompt BLOCK_COMMAND = new BlockedCommandPrompt(plugin);
    public static final Prompt REGION_NAME = new RegionNamePrompt(plugin);


}
