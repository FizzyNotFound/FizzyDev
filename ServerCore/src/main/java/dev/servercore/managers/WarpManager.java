package dev.servercore.managers;

import dev.servercore.ServerCore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WarpManager {

    private final ServerCore plugin;
    private final File warpFile;
    private YamlConfiguration warpConfig;
    private final Map<String, Location> warps = new HashMap<>();

    public WarpManager(ServerCore plugin) {
        this.plugin = plugin;
        this.warpFile = new File(plugin.getDataFolder(), "warps.yml");
        loadWarps();
    }

    private void loadWarps() {
        if (!warpFile.exists()) {
            try { warpFile.createNewFile(); } catch (IOException ignored) {}
        }
        warpConfig = YamlConfiguration.loadConfiguration(warpFile);
        warps.clear();

        ConfigurationSection section = warpConfig.getConfigurationSection("warps");
        if (section == null) return;

        for (String name : section.getKeys(false)) {
            String worldName = section.getString(name + ".world");
            World world = Bukkit.getWorld(worldName);
            if (world == null) continue;
            double x = section.getDouble(name + ".x");
            double y = section.getDouble(name + ".y");
            double z = section.getDouble(name + ".z");
            float yaw = (float) section.getDouble(name + ".yaw");
            float pitch = (float) section.getDouble(name + ".pitch");
            warps.put(name.toLowerCase(), new Location(world, x, y, z, yaw, pitch));
        }
    }

    public void setWarp(String name, Location loc) {
        String key = name.toLowerCase();
        warps.put(key, loc);
        String path = "warps." + key;
        warpConfig.set(path + ".world", loc.getWorld().getName());
        warpConfig.set(path + ".x", loc.getX());
        warpConfig.set(path + ".y", loc.getY());
        warpConfig.set(path + ".z", loc.getZ());
        warpConfig.set(path + ".yaw", loc.getYaw());
        warpConfig.set(path + ".pitch", loc.getPitch());
        save();
    }

    public boolean deleteWarp(String name) {
        String key = name.toLowerCase();
        if (!warps.containsKey(key)) return false;
        warps.remove(key);
        warpConfig.set("warps." + key, null);
        save();
        return true;
    }

    public Location getWarp(String name) {
        return warps.get(name.toLowerCase());
    }

    public boolean warpExists(String name) {
        return warps.containsKey(name.toLowerCase());
    }

    public Set<String> getWarpNames() {
        return warps.keySet();
    }

    public Location getSpawn() {
        return getWarp("spawn");
    }

    public void setSpawn(Location loc) {
        setWarp("spawn", loc);
    }

    private void save() {
        try { warpConfig.save(warpFile); }
        catch (IOException e) { plugin.getLogger().warning("Failed to save warps.yml"); }
    }
}
