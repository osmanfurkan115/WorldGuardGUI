package me.heymrau.worldguardguiplugin.managers;

import lombok.RequiredArgsConstructor;
import me.heymrau.worldguardguiplugin.WorldGuardGUIPlugin;
import org.bukkit.Bukkit;
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

    public void showParticles(Player player, List<Block> blocks) {
        List<Object> packets = new ArrayList<>();
        ParticleBuilder particle = new ParticleBuilder(ParticleEffect.FLAME).setAmount(1).setOffsetY(1f).setSpeed(0f);

        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () ->
                blocks.forEach(block -> packets.add(particle.setLocation(block.getLocation()).toPacket())), 20L);

        int id = TaskManager.startSingularTask(packets, 20, player);

        Bukkit.getScheduler().runTaskLater(plugin, () ->
                TaskManager.getTaskManager().stopTask(id), 15 * 20L);
    }
}
