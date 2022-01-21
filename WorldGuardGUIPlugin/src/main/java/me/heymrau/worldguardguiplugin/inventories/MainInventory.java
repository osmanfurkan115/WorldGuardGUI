package me.heymrau.worldguardguiplugin.inventories;

import com.hakan.inventoryapi.inventory.ClickableItem;
import com.hakan.inventoryapi.inventory.HInventory;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.heymrau.worldguardguiplugin.WorldGuardGUIPlugin;
import me.heymrau.worldguardguiplugin.model.ChatInput;
import me.heymrau.worldguardguiplugin.model.CustomItem;
import me.heymrau.worldguardguiplugin.model.InputType;
import me.heymrau.worldguardguiplugin.model.Template;
import me.heymrau.worldguardguiplugin.utils.XMaterial;
import me.heymrau.worldguardhook.WorldGuardLocation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;
import xyz.xenondevs.particle.task.TaskManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainInventory extends Inventory {

    private final WorldGuardGUIPlugin plugin;
    private HInventory inventory;

    public MainInventory(WorldGuardGUIPlugin plugin) {
        this.plugin = plugin;
        createInventory();
    }

    @Override
    void createInventory() {
        final HInventory inventory = plugin.getInventoryAPI().getInventoryCreator().setSize(5).setTitle("WorldGuard GUI").create();
        final ItemStack info = new CustomItem("&eAbout plugin", Arrays.asList("&7", "&7You can manage your", "&7flags and can set", "&7their value to &aALLOW", "&7or &cDENY &7easily"), Material.BOOK, false, (short) 0, 1).complete();
        inventory.setItem(44, ClickableItem.empty(info));

        this.inventory = inventory;

    }

    @Override
    public HInventory getInventory(String regionName, Player player) {
        final ItemStack rename = new CustomItem("&aRename region", Arrays.asList("&7", "&7Active name: &a" + regionName), Material.NAME_TAG, false, (short) 0, 1).complete();
        final ItemStack border = new CustomItem("&aShow border", null, XMaterial.BLUE_DYE.parseItem(), false, (short) 0, 1).complete();
        final ItemStack saveAsTemplate = new CustomItem("&aSave as template", null, XMaterial.GOLD_INGOT.parseItem(), false, (short) 0, 1).complete();
        final ItemStack blockedCommands = new CustomItem("&aBlocked Commands", null, XMaterial.CAULDRON.parseItem(), false, (short) 0, 1).complete();
        inventory.setItem(11, ClickableItem.of(new CustomItem("&aManage region flags", null, XMaterial.GRASS_BLOCK.parseMaterial(), false, (short) 0, 1).complete(), item -> new FlagInventory(plugin).open(player, regionName)));
        inventory.setItem(12, ClickableItem.of(new CustomItem("&aSet region parent", null, Material.ANVIL, false, (short) 0, 1).complete(), item -> new ParentInventory(plugin, regionName, player).open(player, regionName)));
        inventory.setItem(13, ClickableItem.of(new CustomItem("&aTemplates", null, XMaterial.CLOCK.parseItem(), false, (short) 0, 1).complete(), item -> new TemplateInventory(plugin, regionName, player).open(player, regionName)));
        final ProtectedRegion region = plugin.getWorldGuard().getRegionByName(regionName);
        inventory.setItem(14, ClickableItem.of(border, item -> {
            player.sendMessage(ChatColor.YELLOW + "Border displayed for 15 seconds");
            final WorldGuardLocation minimumPoint1 = plugin.getWorldGuard().getMinimumPoint(region, player.getWorld().getName());
            final Location minimumPoint = new Location(Bukkit.getWorld(minimumPoint1.getWorldName()), minimumPoint1.getX(), minimumPoint1.getY(), minimumPoint1.getZ());
            final WorldGuardLocation maximumPoint1 = plugin.getWorldGuard().getMaximumPoint(region, player.getWorld().getName());
            final Location maximumPoint = new Location(Bukkit.getWorld(maximumPoint1.getWorldName()), maximumPoint1.getX(), maximumPoint1.getY(), maximumPoint1.getZ());
            final List<Block> blocks = new ArrayList<>();

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                for (int y = minimumPoint.getBlockY(); y <= maximumPoint.getBlockY(); ++y) {
                    for (int x = minimumPoint.getBlockX(); x <= maximumPoint.getBlockX(); ++x) {
                        blocks.add(player.getWorld().getBlockAt(x, y, minimumPoint.getBlockZ()));
                        blocks.add(player.getWorld().getBlockAt(x, y, maximumPoint.getBlockZ()));
                    }
                    for (int z = minimumPoint.getBlockZ(); z <= maximumPoint.getBlockZ(); ++z) {
                        blocks.add(player.getWorld().getBlockAt(minimumPoint.getBlockX(), y, z));
                        blocks.add(player.getWorld().getBlockAt(maximumPoint.getBlockX(), y, z));
                    }
                }
            });
            showParticles(player, blocks);
        }));

        inventory.setItem(15, ClickableItem.of(rename, item -> {
            player.closeInventory();
            player.sendMessage(ChatColor.YELLOW + "Type a new name for the region named " + regionName);
            player.sendMessage(ChatColor.YELLOW + "You have 30 seconds");
            plugin.getChatInput().put(player, new ChatInput(regionName, InputType.NAME));
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (plugin.getChatInput().containsKey(player)) {
                        plugin.getChatInput().remove(player);
                        player.sendMessage(ChatColor.RED + "You didn't type any name in 30 seconds!");
                    }

                }
            }.runTaskLater(plugin, 20 * 30L);
        }));

        inventory.setItem(21, ClickableItem.of(saveAsTemplate, item -> plugin.getTemplateManager().addTemplate(new Template(regionName, plugin.getWorldGuard().getEnabledFlags(region), plugin.getWorldGuard().getDeniedFlags(region)))));

        inventory.setItem(23, ClickableItem.of(blockedCommands, event -> new CommandInventory(plugin, player, regionName).open(player, regionName)));


        inventory.setItem(40, ClickableItem.of(new CustomItem("&cDelete region " + regionName, null, Material.BARRIER, false, (short) 0, 1).complete(), item -> {
            plugin.getWorldGuard().remove(regionName);
            player.closeInventory();
            player.sendMessage(ChatColor.GREEN + "Region deleted successfully");
        }));
        return inventory;
    }

    private void showParticles(Player player, List<Block> blocks) {
        List<Object> packets = new ArrayList<>();
        ParticleBuilder particle = new ParticleBuilder(ParticleEffect.FLAME).setAmount(1).setOffsetY(1f).setSpeed(0f);
        new BukkitRunnable() {
            @Override
            public void run() {
                blocks.forEach(block -> packets.add(particle.setLocation(block.getLocation()).toPacket()));
            }
        }.runTaskLaterAsynchronously(plugin, 20L);
        int id = TaskManager.startSingularTask(packets, 20, player);

        new BukkitRunnable() {
            @Override
            public void run() {
                TaskManager.getTaskManager().stopTask(id);
            }
        }.runTaskLater(plugin, 15 * 20L);
    }

    @Override
    public void open(Player player, String regionName) {
        getInventory(regionName, player).open(player);
    }
}
