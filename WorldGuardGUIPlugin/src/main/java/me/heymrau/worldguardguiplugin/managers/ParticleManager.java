package me.heymrau.worldguardguiplugin.managers;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.RequiredArgsConstructor;
import me.heymrau.worldguardguiplugin.WorldGuardGUIPlugin;
import me.heymrau.worldguardhook.WorldGuardLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;
import xyz.xenondevs.particle.task.TaskManager;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ParticleManager {
    private final WorldGuardGUIPlugin plugin;

    public void showBorder(Player player, ProtectedRegion region) {
        final WorldGuardLocation minimumPoint1 = plugin.getWorldGuard().getMinimumPoint(region, player.getWorld().getName());
        final Location minimumPoint = getLocationFromWorldGuardLocation(minimumPoint1);
        final WorldGuardLocation maximumPoint1 = plugin.getWorldGuard().getMaximumPoint(region, player.getWorld().getName());
        final Location maximumPoint = getLocationFromWorldGuardLocation(maximumPoint1);
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
    }

    private Location getLocationFromWorldGuardLocation(WorldGuardLocation location) {
        return new Location(Bukkit.getWorld(location.getWorldName()), location.getX(), location.getY(), location.getZ());
    }

    private void showParticles(Player player, List<Block> blocks) {
        final List<Object> packets = new ArrayList<>();
        final ParticleBuilder particle = new ParticleBuilder(ParticleEffect.FLAME).setAmount(1).setOffsetY(1f).setSpeed(0f);

        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () ->
                blocks.forEach(block -> packets.add(particle.setLocation(block.getLocation()).toPacket())), 20L);

        final int id = TaskManager.startSingularTask(packets, 20, player);

        Bukkit.getScheduler().runTaskLater(plugin, () ->
                TaskManager.getTaskManager().stopTask(id), 15 * 20L);
    }
}
