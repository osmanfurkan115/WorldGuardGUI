package me.heymrau.worldguardguiplugin;

import lombok.Getter;
import me.heymrau.wg6.WorldGuard6Hook;
import me.heymrau.wg7.WorldGuard7Hook;
import me.heymrau.worldguardguiplugin.commands.WGGuiCommand;
import me.heymrau.worldguardguiplugin.inventories.MainInventory;
import me.heymrau.worldguardguiplugin.managers.ConversationManager;
import me.heymrau.worldguardguiplugin.managers.InventoryManager;
import me.heymrau.worldguardguiplugin.managers.ParticleManager;
import me.heymrau.worldguardguiplugin.managers.TemplateManager;
import me.heymrau.worldguardguiplugin.utils.Metrics;
import me.heymrau.worldguardguiplugin.utils.Yaml;
import me.heymrau.worldguardhook.WorldGuardService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@Getter
public final class WorldGuardGUIPlugin extends JavaPlugin {

    private WorldGuardService worldGuard;
    private ConversationManager conversationManager;
    private TemplateManager templateManager;
    private InventoryManager inventoryManager;
    private ParticleManager particleManager;
    @Getter(lazy = true) private final Yaml templates = new Yaml(this, new File(getDataFolder(), "templates.yml"));

    @Override
    public void onEnable() {
        inventoryManager = new InventoryManager();

        String version = Bukkit.getPluginManager().getPlugin("WorldGuard").getDescription().getVersion();
        worldGuard = version.startsWith("6") ? new WorldGuard6Hook() : new WorldGuard7Hook();

        templateManager = new TemplateManager(this);
        templateManager.initializeTemplates();

        particleManager = new ParticleManager(this);
        conversationManager = new ConversationManager(this);

        getCommand("wggui").setExecutor(new WGGuiCommand(this));

        new Metrics(this, 12471);

        getLogger().info("WorldGuardGUI has started successfully");
    }
}
