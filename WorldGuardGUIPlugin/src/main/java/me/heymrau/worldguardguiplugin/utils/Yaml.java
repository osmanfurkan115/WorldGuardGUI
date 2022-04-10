package me.heymrau.worldguardguiplugin.utils;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Yaml extends YamlConfiguration {
    private final JavaPlugin plugin;
    private final File file;

    public Yaml(JavaPlugin plugin, File file) {
        this.plugin = plugin;
        this.file = file;

        createFile();
    }

    private void createFile() {
        if (!exists()) {
            if (!file.getParentFile().mkdirs()) {
                throw new IllegalStateException("Could not create directories for " + file);
            }
            plugin.saveResource(file.getName(), false);
        }
    }

    public void reload() {
        if(!exists()) createFile();
        try {
            load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean exists() {
        return file.exists();
    }
}