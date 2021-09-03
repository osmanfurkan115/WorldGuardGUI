package me.heymrau.worldguardguiplugin.utils;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Yaml {

    private final String fileURL;
    private final File file;
    private FileConfiguration fileConfiguration;

    public Yaml(String fileURL, BufferedReader bufferedReader) {
        this.fileURL = fileURL;
        this.file = new File(fileURL);
        boolean fileExist = this.file.exists();
        createFile(fileURL);
        if (!fileExist && bufferedReader != null) {
            try {
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.file)));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    bw.write(line);
                    bw.newLine();
                }

                bufferedReader.close();
                bw.close();
            } catch (IOException e) {
                System.out.println("An internal error");
            }
        }
        this.fileConfiguration = YamlConfiguration.loadConfiguration(this.file);
    }

    public Yaml(String fileURL, InputStream inputStream) {
        this(fileURL, new BufferedReader(new InputStreamReader(inputStream)));
    }

    public Yaml(String fileURL, String resourceName) {
        this(fileURL, Yaml.class.getResourceAsStream("/" + resourceName));
    }

    public Yaml(String fileURL) {
        this(fileURL, (BufferedReader) null);
    }

    public void createFile(String url, String fileName) {
        File file = new File(url);
        file.mkdirs();
        File file2 = new File(url + "/" + fileName);
        try {
            file2.createNewFile();
        } catch (IOException e) {
            System.out.println("An internal error");
        }
    }

    public void createFile(String url) {
        StringBuilder stringBuilder = new StringBuilder();
        String[] sp = url.split("/");
        int m = 0;
        for (String value : sp) {
            m++;
            stringBuilder.append(value);
            if (sp.length == m + 1) {
                break;
            } else {
                stringBuilder.append("/");
            }
        }
        createFile(stringBuilder.toString(), sp[sp.length - 1]);
    }

    public void reload() {
        this.fileConfiguration = YamlConfiguration.loadConfiguration(this.file);
    }

    public void unload() {
        this.fileConfiguration = null;
        this.file.delete();
    }

    public void save() {
        try {
            this.fileConfiguration.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getFile() {
        return this.file;
    }

    public String getFileURL() {
        return this.fileURL;
    }

    public FileConfiguration getFileConfiguration() {
        return this.fileConfiguration;
    }

    public void set(String path, Object value) {
        this.fileConfiguration.set(path, value);
    }

    public boolean isSet(String path) {
        return this.fileConfiguration.isSet(path);
    }

    public String getString(String path) {
        return this.fileConfiguration.getString(path);
    }

    public String getString(String path, String def) {
        return this.fileConfiguration.getString(path, def);
    }

    public boolean isString(String path) {
        return this.fileConfiguration.isString(path);
    }

    public int getInt(String path) {
        return this.fileConfiguration.getInt(path);
    }

    public int getInt(String path, int def) {
        return this.fileConfiguration.getInt(path, def);
    }

    public boolean isInt(String path) {
        return this.fileConfiguration.isInt(path);
    }

    public boolean getBoolean(String path) {
        return this.fileConfiguration.getBoolean(path);
    }

    public boolean getBoolean(String path, boolean def) {
        return this.fileConfiguration.getBoolean(path, def);
    }

    public boolean isBoolean(String path) {
        return this.fileConfiguration.isBoolean(path);
    }

    public double getDouble(String path) {
        return this.fileConfiguration.getDouble(path);
    }

    public double getDouble(String path, double def) {
        return this.fileConfiguration.getDouble(path, def);
    }

    public boolean isDouble(String path) {
        return this.fileConfiguration.isDouble(path);
    }

    public long getLong(String path) {
        return this.fileConfiguration.getLong(path);
    }

    public long getLong(String path, long def) {
        return this.fileConfiguration.getLong(path, def);
    }

    public boolean isLong(String path) {
        return this.fileConfiguration.isLong(path);
    }

    public List<?> getList(String path) {
        return this.fileConfiguration.getList(path);
    }

    public List<?> getList(String path, List<?> def) {
        return this.fileConfiguration.getList(path, def);
    }

    public boolean isList(String path) {
        return this.fileConfiguration.isList(path);
    }

    public List<String> getStringList(String path) {
        return this.fileConfiguration.getStringList(path);
    }

    public List<Integer> getIntegerList(String path) {
        return this.fileConfiguration.getIntegerList(path);
    }

    public List<Boolean> getBooleanList(String path) {
        return this.fileConfiguration.getBooleanList(path);
    }

    public List<Double> getDoubleList(String path) {
        return this.fileConfiguration.getDoubleList(path);
    }

    public List<Float> getFloatList(String path) {
        return this.fileConfiguration.getFloatList(path);
    }

    public List<Long> getLongList(String path) {
        return this.fileConfiguration.getLongList(path);
    }

    public List<Byte> getByteList(String path) {
        return this.fileConfiguration.getByteList(path);
    }

    public List<Character> getCharacterList(String path) {
        return this.fileConfiguration.getCharacterList(path);
    }

    public List<Short> getShortList(String path) {
        return this.fileConfiguration.getShortList(path);
    }

    public List<Map<?, ?>> getMapList(String path) {
        return this.fileConfiguration.getMapList(path);
    }

    public OfflinePlayer getOfflinePlayer(String path) {
        return this.fileConfiguration.getOfflinePlayer(path);
    }

    public OfflinePlayer getOfflinePlayer(String path, OfflinePlayer def) {
        return this.fileConfiguration.getOfflinePlayer(path, def);
    }

    public boolean isOfflinePlayer(String path) {
        return this.fileConfiguration.isOfflinePlayer(path);
    }

    public Location getLocation(String path) {
        return (Location) this.fileConfiguration.get(path);
    }

    public Location getLocation(String path, Location def) {
        Location location = getLocation(path);
        return location != null ? location : def;
    }

    public boolean isLocation(String path) {
        return getLocation(path) != null;
    }

    public ItemStack getItemStack(String path) {
        return (ItemStack) this.fileConfiguration.get(path);
    }

    public ItemStack getItemStack(String path, ItemStack def) {
        ItemStack itemStack = getItemStack(path);
        return itemStack != null ? itemStack : def;
    }

    public boolean isItemStack(String path) {
        return getItemStack(path) != null;
    }

    public List<ItemStack> getItemStackList(String path) {
        List<ItemStack> itemStackList = new ArrayList<>();
        for (Object items : getList(path)) {
            itemStackList.add((ItemStack) items);
        }
        return itemStackList;
    }

    public List<ItemStack> getItemStackList(String path, List<ItemStack> def) {
        List<ItemStack> itemStackList = getItemStackList(path);
        return itemStackList.size() != 0 ? itemStackList : def;
    }

    public ConfigurationSection getConfigurationSection(String path) {
        return this.fileConfiguration.getConfigurationSection(path);
    }

    public boolean isConfigurationSection(String path) {
        return this.fileConfiguration.isConfigurationSection(path);
    }
}