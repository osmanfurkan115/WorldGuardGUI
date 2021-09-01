package me.heymrau.worldguardhook;

public class WorldGuardLocation {
    private final String worldName;
    private final int x;
    private final int y;
    private final int z;

    public WorldGuardLocation(String worldName, int x, int y, int z) {
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String getWorldName() {
        return worldName;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
}
