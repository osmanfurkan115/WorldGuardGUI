package me.heymrau.worldguardguiplugin.listeners;

import me.heymrau.worldguardguiplugin.WorldGuardGUIPlugin;
import me.heymrau.worldguardguiplugin.model.ChatInput;
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
        if (plugin.getChatInput().containsKey(player)) {
            e.setCancelled(true);
            final ChatInput chatInput = plugin.getChatInput().get(player);
            switch (chatInput.getInputType()) {
                case NAME:
                    plugin.getWorldGuard().rename(chatInput.getRegionName(), e.getMessage());
                    player.sendMessage(ChatColor.GREEN + "Region name changed to " + e.getMessage());
                    break;
                case COMMAND:
                    plugin.getWorldGuard().addBlockedCommand(plugin.getWorldGuard().getRegionByName(chatInput.getRegionName()), e.getMessage());
                    player.sendMessage(ChatColor.GREEN + "Command " + e.getMessage() + " added to the blocked commands");
            }
            plugin.getChatInput().remove(player);
        }
    }
}
