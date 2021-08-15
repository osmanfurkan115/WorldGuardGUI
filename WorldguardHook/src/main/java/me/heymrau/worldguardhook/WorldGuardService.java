package me.heymrau.worldguardhook;

import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import java.util.List;

public interface WorldGuardService {
    void remove(String regionName);
    void allowFlag(String regionName, StateFlag flag);
    void denyFlag(String regionName, StateFlag flag);
    List<Flag> getFlags(String regionName);
    ProtectedRegion getRegionByName(String regionName);
}
