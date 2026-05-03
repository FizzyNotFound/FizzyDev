package dev.servercore.listeners;

import dev.servercore.ServerCore;
import dev.servercore.data.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerDataListener implements Listener {

    private final ServerCore plugin;
    public static final Set<UUID> frozenPlayers = new HashSet<>();

    public PlayerDataListener(ServerCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        PlayerData victimData = plugin.getPlayerDataManager().get(victim);
        if (victimData != null) victimData.incrementDeaths();

        if (victim.getKiller() != null) {
            PlayerData killerData = plugin.getPlayerDataManager().get(victim.getKiller());
            if (killerData != null) killerData.incrementKills();
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!frozenPlayers.contains(player.getUniqueId())) return;

        // Allow head rotation but block movement
        if (event.getFrom().getBlockX() != event.getTo().getBlockX()
                || event.getFrom().getBlockY() != event.getTo().getBlockY()
                || event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
            event.setTo(event.getFrom().clone().add(0, 0, 0));
            player.sendMessage("§cYou are frozen and cannot move.");
        }
    }
}
