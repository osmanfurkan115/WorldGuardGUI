package me.heymrau.worldguardguiplugin.commands;

import me.heymrau.worldguardguiplugin.WorldGuardGUIPlugin;
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
        if (!(sender instanceof Player) || !sender.hasPermission("worldguardgui.admin")) return true;
        Player player = (Player) sender;
        if (args.length == 1) {
            if (plugin.getWorldGuard().getRegionByName(args[0]) != null) {
                plugin.getMainInventory().open(player, args[0]);
            } else {
                player.sendMessage(colored("&cCouldn't find a region named " + args[0]));
            }
        } else {
            player.sendMessage(colored("&cUsage: &a/wggui <region>"));
        }
        return true;
    }

    private String colored(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
