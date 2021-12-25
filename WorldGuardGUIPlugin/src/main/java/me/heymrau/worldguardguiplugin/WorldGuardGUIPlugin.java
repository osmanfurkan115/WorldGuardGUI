package me.heymrau.worldguardguiplugin;

import com.hakan.inventoryapi.InventoryAPI;
import com.hakan.inventoryapi.inventory.ClickableItem;
import com.hakan.inventoryapi.inventory.HInventory;
import com.hakan.inventoryapi.inventory.Pagination;
import lombok.Getter;
import me.heymrau.wg6.WorldGuard6Hook;
import me.heymrau.wg7.WorldGuard7Hook;
import me.heymrau.worldguardguiplugin.commands.WGGuiCommand;
import me.heymrau.worldguardguiplugin.inventories.Inventory;
import me.heymrau.worldguardguiplugin.inventories.MainInventory;
import me.heymrau.worldguardguiplugin.listeners.ChatListener;
import me.heymrau.worldguardguiplugin.managers.TemplateManager;
import me.heymrau.worldguardguiplugin.model.CustomItem;
import me.heymrau.worldguardguiplugin.utils.Yaml;
import me.heymrau.worldguardhook.WorldGuardService;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

@Getter
public final class WorldGuardGUIPlugin extends JavaPlugin {

    /* Key: Player
    Value: Region name */
    private final Map<Player, String> chatInput = new HashMap<>();
    private InventoryAPI inventoryAPI;
    private Inventory mainInventory;
    private WorldGuardService worldGuard;
    private TemplateManager templateManager;
    private Yaml templates;

    @Override
    public void onEnable() {
        templates = new Yaml(getDataFolder() + "/templates.yml", "templates.yml");

        inventoryAPI = InventoryAPI.getInstance(this);


        mainInventory = setupVariable(mainInventory, new MainInventory(this));

        final String version = Bukkit.getPluginManager().getPlugin("WorldGuard").getDescription().getVersion();
        worldGuard = version.startsWith("6") ? setupVariable(worldGuard, new WorldGuard6Hook()) : setupVariable(worldGuard, new WorldGuard7Hook());
        templateManager = new TemplateManager(this);
        templateManager.initializeTemplates();

        getCommand("wggui").setExecutor(new WGGuiCommand(this));
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        new Metrics(this, 12471);

        getLogger().info("WorldGuardGUI has started successfully");

    }

    private <T> T setupVariable(T variable, T instance) {
        return variable == null ? instance : variable;
    }

    public void setupButtons(HInventory inventory, Pagination pagination) {
        inventory.setItem(38, ClickableItem.of(new CustomItem("&6Previous Page", null, Material.ARROW, false, (short) 0, 1).complete(), (event) -> pagination.previousPage()));

        inventory.setItem(40, ClickableItem.of(new CustomItem("&cClose", null, Material.BARRIER, false, (short) 0, 1).complete(), (event) -> event.getWhoClicked().closeInventory()));

        inventory.setItem(42, ClickableItem.of(new CustomItem("&6Next Page", null, Material.ARROW, false, (short) 0, 1).complete(), (event) -> pagination.nextPage()));
    }
}
