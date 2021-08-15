package me.heymrau.worldguardgui;

import com.hakan.inventoryapi.InventoryAPI;
import lombok.Getter;
import me.heymrau.worldguardgui.inventories.Inventory;
import me.heymrau.worldguardgui.inventories.MainInventory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class WorldGuardGUI extends JavaPlugin implements Listener {

    private InventoryAPI inventoryAPI;
    private Inventory mainInventory;

    @Override
    public void onEnable() {
        inventoryAPI = InventoryAPI.getInstance(this);
        mainInventory = setupInventory(mainInventory, new MainInventory(this));
        getServer().getPluginManager().registerEvents(this,this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private <T> T setupInventory(T variable, T instance) {
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
