package me.heymrau.worldguardguiplugin;

import com.hakan.inventoryapi.InventoryAPI;
import lombok.Getter;
import me.heymrau.wg6.WorldGuard6Hook;
import me.heymrau.wg7.WorldGuard7Hook;
import me.heymrau.worldguardguiplugin.commands.WGGuiCommand;
import me.heymrau.worldguardguiplugin.inventories.Inventory;
import me.heymrau.worldguardguiplugin.inventories.MainInventory;
import me.heymrau.worldguardhook.WorldGuardService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class WorldGuardGUIPlugin extends JavaPlugin {

    private InventoryAPI inventoryAPI;
    private Inventory mainInventory;
    private WorldGuardService worldGuard;


    @Override
    public void onEnable() {
        inventoryAPI = InventoryAPI.getInstance(this);
        mainInventory = setupVariable(mainInventory, new MainInventory(this));
        final String version = Bukkit.getPluginManager().getPlugin("WorldGuard").getDescription().getVersion();
        worldGuard = version.startsWith("6") ? setupVariable(worldGuard, new WorldGuard6Hook()) : setupVariable(worldGuard, new WorldGuard7Hook());
        getCommand("wggui").setExecutor(new WGGuiCommand(this));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private <T> T setupVariable(T variable, T instance) {
        if(variable == null) {
            return instance;
        }
        return variable;
    }

}