package dev.servercore.listeners;

import dev.servercore.ServerCore;
import dev.servercore.data.PlayerData;
import dev.servercore.managers.RankManager;
import dev.servercore.managers.WarpManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitListener implements Listener {

    private final ServerCore plugin;

    public JoinQuitListener(ServerCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData data = plugin.getPlayerDataManager().load(player);
        RankManager rm = plugin.getRankManager();

        String prefix = rm.getPrefix(data.getRank());
        boolean firstTime = !player.hasPlayedBefore();

        String joinMsg = plugin.getConfig().getString("messages.join",
                "&8[&a+&8] " + prefix + " &f{player}");
        joinMsg = joinMsg
                .replace("{player}", player.getName())
                .replace("{prefix}", prefix)
                .replace("&", "§");

        event.setJoinMessage(joinMsg);

        // Teleport to spawn on first join
        if (firstTime) {
            Location spawn = plugin.getWarpManager().getSpawn();
            if (spawn != null) player.teleport(spawn);
        }

        // Send MOTD
        String motd = plugin.getConfig().getString("messages.motd", "");
        if (!motd.isEmpty()) {
            player.sendMessage(motd.replace("&", "§").replace("{player}", player.getName()));
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerData data = plugin.getPlayerDataManager().get(player);

        String prefix = data != null ? plugin.getRankManager().getPrefix(data.getRank()) : "";
        String quitMsg = plugin.getConfig().getString("messages.quit",
                "&8[&c-&8] " + prefix + " &f{player}");
        quitMsg = quitMsg
                .replace("{player}", player.getName())
                .replace("{prefix}", prefix)
                .replace("&", "§");

        event.setQuitMessage(quitMsg);
        plugin.getPlayerDataManager().unload(player.getUniqueId());
    }
}
