package dev.servercore.data;

import java.util.UUID;

public class PlayerData {

    private final UUID uuid;
    private String name;
    private String rank;
    private long firstJoin;
    private long lastSeen;
    private int kills;
    private int deaths;
    private long playtimeSeconds;
    private long sessionStart;

    public PlayerData(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.rank = "default";
        this.firstJoin = System.currentTimeMillis();
        this.lastSeen = System.currentTimeMillis();
        this.kills = 0;
        this.deaths = 0;
        this.playtimeSeconds = 0;
        this.sessionStart = System.currentTimeMillis();
    }

    // Getters & setters
    public UUID getUuid() { return uuid; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRank() { return rank; }
    public void setRank(String rank) { this.rank = rank; }
    public long getFirstJoin() { return firstJoin; }
    public void setFirstJoin(long firstJoin) { this.firstJoin = firstJoin; }
    public long getLastSeen() { return lastSeen; }
    public void setLastSeen(long lastSeen) { this.lastSeen = lastSeen; }
    public int getKills() { return kills; }
    public void setKills(int kills) { this.kills = kills; }
    public int getDeaths() { return deaths; }
    public void setDeaths(int deaths) { this.deaths = deaths; }
    public long getPlaytimeSeconds() { return playtimeSeconds; }
    public void setPlaytimeSeconds(long playtimeSeconds) { this.playtimeSeconds = playtimeSeconds; }
    public long getSessionStart() { return sessionStart; }
    public void setSessionStart(long sessionStart) { this.sessionStart = sessionStart; }

    public void addPlaytime(long seconds) { this.playtimeSeconds += seconds; }
    public void incrementKills() { this.kills++; }
    public void incrementDeaths() { this.deaths++; }

    public String getFormattedPlaytime() {
        long s = playtimeSeconds;
        long hours = s / 3600;
        long minutes = (s % 3600) / 60;
        long seconds2 = s % 60;
        return String.format("%dh %dm %ds", hours, minutes, seconds2);
    }
}
