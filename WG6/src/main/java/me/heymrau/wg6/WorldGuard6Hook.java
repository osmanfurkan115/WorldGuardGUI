package me.heymrau.wg6;

import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.heymrau.worldguardhook.WorldGuardService;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class WorldGuard6Hook implements WorldGuardService {

    @Override
    public void remove(String regionName) {
        for (World world: Bukkit.getWorlds()) {
            final RegionContainer container = WorldGuardPlugin.inst().getRegionContainer();
            final RegionManager regions = container.get(world);
            if(regions == null) return;
            regions.removeRegion(getRegionByName(regionName).getId());
        }
    }

    @Override
    public void allowFlag(String regionName, StateFlag flag) {
        getRegionByName(regionName).setFlag(flag, StateFlag.State.ALLOW);
    }

    @Override
    public void denyFlag(String regionName, StateFlag flag) {
        getRegionByName(regionName).setFlag(flag, StateFlag.State.DENY);
    }

    @Override
    public List<Flag> getFlags(String regionName) {
        return new ArrayList<>(getRegionByName(regionName).getFlags().keySet());
    }

    @Override
    public ProtectedRegion getRegionByName(String regionName) {
        for (World world: Bukkit.getWorlds()) {
            final RegionContainer container = WorldGuardPlugin.inst().getRegionContainer();
            final RegionManager regions = container.get(world);
            return regions == null ? null : regions.getRegion(regionName);
        }
        return null;
    }
}
