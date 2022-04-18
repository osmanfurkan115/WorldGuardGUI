package me.heymrau.worldguardguiplugin.commands;

import me.heymrau.worldguardguiplugin.WorldGuardGUIPlugin;
import me.heymrau.worldguardguiplugin.inventories.MainInventory;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WGGuiCommand implements CommandExecutor {
    private final WorldGuardGUIPlugin plugin;

    public WGGuiCommand(WorldGuardGUIPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player) || !sender.hasPermission("worldguardgui.gui")) return true;
        Player player = (Player) sender;
        if (args.length != 1) {
            player.sendMessage(colored("&cUsage: /wggui <region>"));
            return true;
        }
        String region = args[0];
        if (plugin.getWorldGuard().getRegionByName(region) == null) {
            player.sendMessage(colored("&cRegion not found"));
            return true;
        }
        new MainInventory(plugin).open(player, region);
        return true;
    }

    private String colored(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
