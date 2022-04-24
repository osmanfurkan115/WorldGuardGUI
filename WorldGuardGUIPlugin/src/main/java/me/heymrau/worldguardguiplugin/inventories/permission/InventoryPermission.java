package me.heymrau.worldguardguiplugin.inventories.permission;

public enum InventoryPermission {
    COMMAND("command"),
    FLAG("flag"),
    PARENT("parent"),
    TEMPLATE("template"),
    OTHER("other");

    private static final String PREFIX = "worldguardgui.";
    private final String permission;

    InventoryPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return PREFIX + permission;
    }
}
