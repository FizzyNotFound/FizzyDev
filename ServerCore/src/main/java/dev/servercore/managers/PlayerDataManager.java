package dev.servercore.managers;

import dev.servercore.ServerCore;
import dev.servercore.data.PlayerData;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {

    private final ServerCore plugin;
    private final Map<UUID, PlayerData> cache = new HashMap<>();
    private final File dataFolder;

    public PlayerDataManager(ServerCore plugin) {
        this.plugin = plugin;
        this.dataFolder = new File(plugin.getDataFolder(), "playerdata");
        if (!dataFolder.exists()) dataFolder.mkdirs();
    }

    public PlayerData load(Player player) {
        UUID uuid = player.getUniqueId();
        File file = new File(dataFolder, uuid + ".yml");
        PlayerData data;

        if (file.exists()) {
            YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
            data = new PlayerData(uuid, player.getName());
            data.setName(cfg.getString("name", player.getName()));
            data.setRank(cfg.getString("rank", "default"));
            data.setFirstJoin(cfg.getLong("firstJoin", System.currentTimeMillis()));
            data.setLastSeen(cfg.getLong("lastSeen", System.currentTimeMillis()));
            data.setKills(cfg.getInt("kills", 0));
            data.setDeaths(cfg.getInt("deaths", 0));
            data.setPlaytimeSeconds(cfg.getLong("playtime", 0));
        } else {
            data = new PlayerData(uuid, player.getName());
        }

        data.setSessionStart(System.currentTimeMillis());
        cache.put(uuid, data);
        return data;
    }

    public void save(PlayerData data) {
        File file = new File(dataFolder, data.getUuid() + ".yml");
        YamlConfiguration cfg = new YamlConfiguration();
        cfg.set("name", data.getName());
        cfg.set("rank", data.getRank());
        cfg.set("firstJoin", data.getFirstJoin());
        cfg.set("lastSeen", data.getLastSeen());
        cfg.set("kills", data.getKills());
        cfg.set("deaths", data.getDeaths());
        cfg.set("playtime", data.getPlaytimeSeconds());
        try {
            cfg.save(file);
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to save data for " + data.getName());
        }
    }

    public void saveAll() {
        cache.values().forEach(this::save);
    }

    public void unload(UUID uuid) {
        PlayerData data = cache.get(uuid);
        if (data != null) {
            long sessionSeconds = (System.currentTimeMillis() - data.getSessionStart()) / 1000;
            data.addPlaytime(sessionSeconds);
            data.setLastSeen(System.currentTimeMillis());
            save(data);
            cache.remove(uuid);
        }
    }

    public PlayerData get(UUID uuid) { return cache.get(uuid); }
    public PlayerData get(Player player) { return cache.get(player.getUniqueId()); }
    public boolean isLoaded(UUID uuid) { return cache.containsKey(uuid); }
}
