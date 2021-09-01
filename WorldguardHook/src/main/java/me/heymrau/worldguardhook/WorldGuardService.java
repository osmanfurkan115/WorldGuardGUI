package me.heymrau.worldguardhook;

import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import java.util.List;
import java.util.Map;

public interface WorldGuardService {
    void remove(String regionName);
    void allowFlag(String regionName, StateFlag flag);
    void denyFlag(String regionName, StateFlag flag);
    void setParent(String childRegion, String parentRegion);
    List<StateFlag> getAllFlags();
    List<StateFlag> getEnabledFlags(String regionName);
    ProtectedRegion getRegionByName(String regionName);
    Map<String, ProtectedRegion> getRegionsByWorld(String worldName);
    StateFlag getFlagByName(String flagName);
}
