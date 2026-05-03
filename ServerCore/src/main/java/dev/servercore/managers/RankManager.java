package dev.servercore.managers;

import dev.servercore.ServerCore;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class RankManager {

    private final ServerCore plugin;
    private final Map<String, Map<String, Object>> ranks = new LinkedHashMap<>();

    public RankManager(ServerCore plugin) {
        this.plugin = plugin;
        loadRanks();
    }

    private void loadRanks() {
        ranks.clear();
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("ranks");
        if (section == null) {
            // Default ranks if config section missing
            addDefaultRanks();
            return;
        }
        for (String rankId : section.getKeys(false)) {
            Map<String, Object> data = new HashMap<>();
            data.put("prefix", section.getString(rankId + ".prefix", "&7[" + rankId + "]&r"));
            data.put("color", section.getString(rankId + ".color", "&f"));
            data.put("weight", section.getInt(rankId + ".weight", 0));
            ranks.put(rankId.toLowerCase(), data);
        }
    }

    private void addDefaultRanks() {
        Map<String, Object> defaultRank = new HashMap<>();
        defaultRank.put("prefix", "&7[Member]&r");
        defaultRank.put("color", "&f");
        defaultRank.put("weight", 0);
        ranks.put("default", defaultRank);

        Map<String, Object> modRank = new HashMap<>();
        modRank.put("prefix", "&a[Mod]&r");
        modRank.put("color", "&a");
        modRank.put("weight", 50);
        ranks.put("mod", modRank);

        Map<String, Object> adminRank = new HashMap<>();
        adminRank.put("prefix", "&c[Admin]&r");
        adminRank.put("color", "&c");
        adminRank.put("weight", 100);
        ranks.put("admin", adminRank);

        Map<String, Object> ownerRank = new HashMap<>();
        ownerRank.put("prefix", "&4&l[Owner]&r");
        ownerRank.put("color", "&4");
        ownerRank.put("weight", 200);
        ranks.put("owner", ownerRank);
    }

    public String getPrefix(String rank) {
        if (!ranks.containsKey(rank.toLowerCase())) return "&7[Member]&r";
        return (String) ranks.get(rank.toLowerCase()).getOrDefault("prefix", "&7[Member]&r");
    }

    public String getColor(String rank) {
        if (!ranks.containsKey(rank.toLowerCase())) return "&f";
        return (String) ranks.get(rank.toLowerCase()).getOrDefault("color", "&f");
    }

    public boolean rankExists(String rank) {
        return ranks.containsKey(rank.toLowerCase());
    }

    public Set<String> getRankNames() {
        return ranks.keySet();
    }

    public void reload() {
        plugin.reloadConfig();
        loadRanks();
    }
}
