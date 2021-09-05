package me.heymrau.wg6;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.heymrau.worldguardhook.WorldGuardLocation;
import me.heymrau.worldguardhook.WorldGuardService;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WorldGuard6Hook implements WorldGuardService {

    @Override
    public void remove(String regionName) {
        for (World world: Bukkit.getWorlds()) {
            final RegionManager regionManager = WorldGuardPlugin.inst().getRegionContainer().get(world);
            final ProtectedRegion region = getRegionByName(regionName);

            if(regionManager != null && region != null) {
                removeWg(region, regionManager);
            }
        }

    }
    private void removeWg(ProtectedRegion region, RegionManager regionManager) {
        regionManager.removeRegion(region.getId());
    }

    @Override
    public void allowFlag(ProtectedRegion region, StateFlag flag) {
        region.setFlag(flag, StateFlag.State.ALLOW);
    }

    @Override
    public void denyFlag(ProtectedRegion region, StateFlag flag) {
        region.setFlag(flag, StateFlag.State.DENY);
    }

    @Override
    public void rename(String oldRegionName, String newRegionName) {
        ProtectedRegion region = getRegionByName(oldRegionName);
        RegionManager regionManager = null;
        for (World world: Bukkit.getWorlds()) {
            final RegionContainer container = WorldGuardPlugin.inst().getRegionContainer();
            regionManager = container.get(world);


            if(regionManager != null && region != null && regionManager.getRegions().containsKey(oldRegionName)) {
                break;
            }
        }
        ProtectedRegion newRegion = new ProtectedCuboidRegion(newRegionName, region.getMinimumPoint(), region.getMaximumPoint());
        newRegion.copyFrom(region);
        if (regionManager != null) {
            regionManager.addRegion(newRegion);
            regionManager.removeRegion(region.getId());
        }
    }

    @Override
    public WorldGuardLocation getMinimumPoint(ProtectedRegion region, String worldName) {
        final BlockVector minimumPoint = region.getMinimumPoint();
        return new WorldGuardLocation(worldName, minimumPoint.getBlockX(), minimumPoint.getBlockY(), minimumPoint.getBlockZ());
    }

    @Override
    public WorldGuardLocation getMaximumPoint(ProtectedRegion region, String worldName) {
        final BlockVector maximumPoint = region.getMaximumPoint();
        return new WorldGuardLocation(worldName, maximumPoint.getBlockX(), maximumPoint.getBlockY(), maximumPoint.getBlockZ());
    }

    @Override
    public List<StateFlag> getEnabledFlags(ProtectedRegion region) {

        final List<StateFlag> collect =  new ArrayList<>();
        for(Flag<?> flag: region.getFlags().keySet()) {
            if(flag instanceof StateFlag && region.getFlags().get(flag).equals(StateFlag.State.ALLOW)) {
                StateFlag toReturnFlag = (StateFlag) flag;
                collect.add(toReturnFlag);
            }
        }
        return collect;
    }
    @Override
    public List<StateFlag> getDeniedFlags(ProtectedRegion region) {
        final List<StateFlag> collect =  new ArrayList<>();
        for(Flag<?> flag: region.getFlags().keySet()) {
            if(flag instanceof StateFlag && region.getFlags().get(flag).equals(StateFlag.State.DENY)) {
                StateFlag toReturnFlag = (StateFlag) flag;
                collect.add(toReturnFlag);
            }
        }
        return collect;
    }

    @Override
    public List<StateFlag> getAllFlags() {
        List<Flag<?>> flags = new ArrayList<>();
        WorldGuardPlugin.inst().getFlagRegistry().forEach(flags::add);
        List<StateFlag> toReturnList = new ArrayList<>();
        flags.stream().filter(flag -> flag instanceof StateFlag).forEach(flag -> toReturnList.add((StateFlag) flag));
        return toReturnList;
    }

    @Override
    public ProtectedRegion getRegionByName(String regionName) {
        for (World world: Bukkit.getWorlds()) {
            final RegionContainer container = WorldGuardPlugin.inst().getRegionContainer();
            final RegionManager regions = container.get(world);
            if(regions != null &&  regions.getRegion(regionName) != null) return regions.getRegion(regionName);
        }
        return null;
    }

    @Override
    public Map<String, ProtectedRegion> getRegionsByWorld(String worldName) {
        final RegionContainer container = WorldGuardPlugin.inst().getRegionContainer();
        final RegionManager regions = container.get(Bukkit.getWorld(worldName));
        if(regions != null) return regions.getRegions();
        return null;
    }

    @Override
    public StateFlag getFlagByName(String flagName) {
        Flag<?> flag = WorldGuardPlugin.inst().getFlagRegistry().get(flagName);
        return flag instanceof StateFlag ? (StateFlag) flag : null;
    }

}
