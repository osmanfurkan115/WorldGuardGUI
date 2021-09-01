package me.heymrau.worldguardguiplugin.listeners;

import me.heymrau.worldguardguiplugin.WorldGuardGUIPlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    private final WorldGuardGUIPlugin plugin;

    public ChatListener(WorldGuardGUIPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        if(plugin.getChatInput().containsKey(player)) {
            plugin.getWorldGuard().rename(plugin.getChatInput().get(player), e.getMessage());
            player.sendMessage(ChatColor.GREEN + "Region name changed to " + e.getMessage());
            plugin.getChatInput().remove(player);
            e.setCancelled(true);
        }
    }
}
