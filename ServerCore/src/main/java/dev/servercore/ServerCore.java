package dev.servercore;

import dev.servercore.commands.*;
import dev.servercore.listeners.*;
import dev.servercore.managers.*;
import org.bukkit.plugin.java.JavaPlugin;

import javax.naming.directory.Attributes;

public class ServerCore extends JavaPlugin {

    private static ServerCore instance;
    public Attributes tpaRequests;
    private PlayerDataManager playerDataManager;
    private RankManager rankManager;
    private WarpManager warpManager;
    private MuteManager muteManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        // Init managers
        this.playerDataManager = new PlayerDataManager(this);
        this.rankManager = new RankManager(this);
        this.warpManager = new WarpManager(this);
        this.muteManager = new MuteManager(this);

        // Register listeners
        getServer().getPluginManager().registerEvents(new JoinQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDataListener(this), this);

        // Register commands
        getCommand("spawn").setExecutor(new SpawnCommand(this));
        getCommand("setspawn").setExecutor(new SpawnCommand(this));
        getCommand("warp").setExecutor(new WarpCommand(this));
        getCommand("setwarp").setExecutor(new WarpCommand(this));
        getCommand("delwarp").setExecutor(new WarpCommand(this));
        getCommand("mute").setExecutor(new MuteCommand(this));
        getCommand("unmute").setExecutor(new MuteCommand(this));
        getCommand("freeze").setExecutor(new FreezeCommand(this));
        getCommand("vanish").setExecutor(new VanishCommand(this));
        getCommand("stats").setExecutor(new StatsCommand(this));
        getCommand("rank").setExecutor(new RankCommand(this));

        getLogger().info("ServerCore enabled successfully.");
    }

    @Override
    public void onDisable() {
        if (playerDataManager != null) playerDataManager.saveAll();
        getLogger().info("ServerCore disabled. All data saved.");
    }

    public static ServerCore getInstance() { return instance; }
    public PlayerDataManager getPlayerDataManager() { return playerDataManager; }
    public RankManager getRankManager() { return rankManager; }
    public WarpManager getWarpManager() { return warpManager; }
    public MuteManager getMuteManager() { return muteManager; }
}
