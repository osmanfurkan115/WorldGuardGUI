package me.heymrau.worldguardguiplugin;

import com.hakan.inventoryapi.InventoryAPI;
import lombok.Getter;
import me.heymrau.worldguardguiplugin.inventories.Inventory;
import me.heymrau.worldguardguiplugin.inventories.MainInventory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class WorldGuardGUIPlugin extends JavaPlugin implements Listener {

    private InventoryAPI inventoryAPI;
    private Inventory mainInventory;

    @Override
    public void onEnable() {
        inventoryAPI = InventoryAPI.getInstance(this);
        mainInventory = setupVariable(mainInventory, new MainInventory(this));

        getServer().getPluginManager().registerEvents(this,this);
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

    //Debug
    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        mainInventory.open(e.getPlayer());
    }
}