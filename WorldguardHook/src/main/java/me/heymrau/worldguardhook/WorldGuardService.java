package me.heymrau.worldguardhook;

import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import java.util.List;
import java.util.Map;

public interface WorldGuardService {
    void remove(String regionName);
    void allowFlag(ProtectedRegion region, StateFlag flag);
    void denyFlag(ProtectedRegion region, StateFlag flag);
    void rename(String oldRegionName, String newRegionName);
    WorldGuardLocation getMinimumPoint(ProtectedRegion region, String worldName);
    WorldGuardLocation getMaximumPoint(ProtectedRegion region, String worldName);
    List<StateFlag> getAllFlags();
    List<StateFlag> getEnabledFlags(ProtectedRegion regionName);
    List<StateFlag> getDeniedFlags(ProtectedRegion region);
    ProtectedRegion getRegionByName(String regionName);
    Map<String, ProtectedRegion> getRegionsByWorld(String worldName);
    StateFlag getFlagByName(String flagName);
}
