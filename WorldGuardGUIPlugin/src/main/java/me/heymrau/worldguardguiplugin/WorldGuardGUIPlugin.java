package me.heymrau.worldguardguiplugin;

import com.hakan.inventoryapi.InventoryAPI;
import lombok.Getter;
import me.heymrau.wg6.WorldGuard6Hook;
import me.heymrau.wg7.WorldGuard7Hook;
import me.heymrau.worldguardguiplugin.commands.WGGuiCommand;
import me.heymrau.worldguardguiplugin.inventories.Inventory;
import me.heymrau.worldguardguiplugin.inventories.MainInventory;
import me.heymrau.worldguardguiplugin.managers.InventoryManager;
import me.heymrau.worldguardguiplugin.managers.ParticleManager;
import me.heymrau.worldguardguiplugin.managers.TemplateManager;
import me.heymrau.worldguardguiplugin.utils.Metrics;
import me.heymrau.worldguardguiplugin.utils.Yaml;
import me.heymrau.worldguardhook.WorldGuardService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class WorldGuardGUIPlugin extends JavaPlugin {

    private InventoryAPI inventoryAPI;
    private Inventory mainInventory;
    private WorldGuardService worldGuard;
    private TemplateManager templateManager;
    private InventoryManager inventoryManager;
    private ParticleManager particleManager;
    private Yaml templates;

    @Override
    public void onEnable() {
        templates = new Yaml(getDataFolder() + "/templates.yml", "templates.yml");

        inventoryAPI = InventoryAPI.getInstance(this);
        inventoryManager = new InventoryManager();
        mainInventory = new MainInventory(this);
        final String version = Bukkit.getPluginManager().getPlugin("WorldGuard").getDescription().getVersion();
        worldGuard = version.startsWith("6") ? new WorldGuard6Hook() : new WorldGuard7Hook();

        templateManager = new TemplateManager(this);
        templateManager.initializeTemplates();

        particleManager = new ParticleManager(this);

        getCommand("wggui").setExecutor(new WGGuiCommand(this));

        new Metrics(this, 12471);

        getLogger().info("WorldGuardGUI has started successfully");
    }
}
