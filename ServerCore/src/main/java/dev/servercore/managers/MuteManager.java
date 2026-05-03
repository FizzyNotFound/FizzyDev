package dev.servercore.managers;

import dev.servercore.ServerCore;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MuteManager {

    private final ServerCore plugin;
    private final File muteFile;
    private YamlConfiguration muteConfig;

    // UUID -> expiry timestamp (-1 = permanent)
    private final Map<UUID, Long> mutes = new HashMap<>();
    private final Map<UUID, String> muteReasons = new HashMap<>();

    public MuteManager(ServerCore plugin) {
        this.plugin = plugin;
        this.muteFile = new File(plugin.getDataFolder(), "mutes.yml");
        load();
    }

    private void load() {
        if (!muteFile.exists()) {
            try { muteFile.createNewFile(); } catch (IOException ignored) {}
        }
        muteConfig = YamlConfiguration.loadConfiguration(muteFile);
        for (String key : muteConfig.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(key);
                long expiry = muteConfig.getLong(key + ".expiry", -1);
                String reason = muteConfig.getString(key + ".reason", "No reason provided");
                if (expiry == -1 || expiry > System.currentTimeMillis()) {
                    mutes.put(uuid, expiry);
                    muteReasons.put(uuid, reason);
                }
            } catch (IllegalArgumentException ignored) {}
        }
    }

    public void mute(UUID uuid, long durationMillis, String reason) {
        long expiry = durationMillis <= 0 ? -1 : System.currentTimeMillis() + durationMillis;
        mutes.put(uuid, expiry);
        muteReasons.put(uuid, reason);
        muteConfig.set(uuid + ".expiry", expiry);
        muteConfig.set(uuid + ".reason", reason);
        save();
    }

    public void unmute(UUID uuid) {
        mutes.remove(uuid);
        muteReasons.remove(uuid);
        muteConfig.set(uuid.toString(), null);
        save();
    }

    public boolean isMuted(UUID uuid) {
        if (!mutes.containsKey(uuid)) return false;
        long expiry = mutes.get(uuid);
        if (expiry != -1 && System.currentTimeMillis() > expiry) {
            unmute(uuid);
            return false;
        }
        return true;
    }

    public String getReason(UUID uuid) {
        return muteReasons.getOrDefault(uuid, "No reason provided");
    }

    public long getExpiry(UUID uuid) {
        return mutes.getOrDefault(uuid, -1L);
    }

    public String getExpiryFormatted(UUID uuid) {
        long expiry = getExpiry(uuid);
        if (expiry == -1) return "Permanent";
        long remaining = expiry - System.currentTimeMillis();
        long minutes = remaining / 60000;
        long hours = minutes / 60;
        long days = hours / 24;
        if (days > 0) return days + "d " + (hours % 24) + "h";
        if (hours > 0) return hours + "h " + (minutes % 60) + "m";
        return minutes + "m";
    }

    private void save() {
        try { muteConfig.save(muteFile); }
        catch (IOException e) { plugin.getLogger().warning("Could not save mutes.yml"); }
    }

    public void setMuted(@org.jetbrains.annotations.NotNull UUID uniqueId, boolean b) {
    }
}
