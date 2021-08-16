package me.heymrau.wg7;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import me.heymrau.worldguardhook.WorldGuardService;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class WorldGuard7Hook implements WorldGuardService {

    @Override
    public void remove(String regionName) {
        for (World world: Bukkit.getWorlds()) {
            final RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            final RegionManager regionManager = container.get(BukkitAdapter.adapt(world));

            ProtectedRegion region = getRegionByName(regionName);
            if(regionManager != null && region != null) {
                removeWg(region, regionManager);
            }
        }

    }
    private void removeWg(ProtectedRegion region, RegionManager regionManager) {

        regionManager.removeRegion(region.getId());
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
    public List<StateFlag> getEnabledFlags(String regionName) {
        final ProtectedRegion regionByName = getRegionByName(regionName);

        final List<StateFlag> collect =  new ArrayList<>();
        for(Flag<?> flag: regionByName.getFlags().keySet()) {
            if(flag instanceof StateFlag && regionByName.getFlags().get(flag).equals(StateFlag.State.ALLOW)) {
                StateFlag toReturnFlag = (StateFlag) flag;
                collect.add(toReturnFlag);
            }
        }
        return collect;
    }

    @Override
    public List<StateFlag> getAllFlags() {
        List<Flag<?>> flags = new ArrayList<>();
        WorldGuard.getInstance().getFlagRegistry().forEach(flags::add);
        List<StateFlag> toReturnList = new ArrayList<>();
        flags.stream().filter(flag -> flag instanceof StateFlag).forEach(flag -> toReturnList.add((StateFlag) flag));
        return toReturnList;
    }

    @Override
    public ProtectedRegion getRegionByName(String regionName) {
        for (World world: Bukkit.getWorlds()) {
            final RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            final RegionManager regions = container.get(BukkitAdapter.adapt(world));
            if(regions != null &&  regions.getRegion(regionName) != null) return regions.getRegion(regionName);
        }
        return null;
    }

    @Override
    public StateFlag getFlagByName(String flagName) {
        Flag<?> flag = WorldGuard.getInstance().getFlagRegistry().get(flagName);
        return flag instanceof StateFlag ? (StateFlag) flag : null;
    }

}
