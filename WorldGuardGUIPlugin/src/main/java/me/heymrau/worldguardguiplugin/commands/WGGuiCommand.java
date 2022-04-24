package me.heymrau.worldguardguiplugin.commands;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
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
        String regionName = args[0];
        ProtectedRegion region = plugin.getWorldGuard().getRegionByName(regionName);
        if (region == null) {
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
