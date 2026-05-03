package dev.servercore.listeners;

import dev.servercore.ServerCore;
import dev.servercore.data.PlayerData;
import dev.servercore.managers.MuteManager;
import dev.servercore.managers.RankManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private final ServerCore plugin;

    public ChatListener(ServerCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        MuteManager mm = plugin.getMuteManager();

        if (mm.isMuted(player.getUniqueId())) {
            event.setCancelled(true);
            String reason = mm.getReason(player.getUniqueId());
            String expiry = mm.getExpiryFormatted(player.getUniqueId());
            player.sendMessage("§cYou are muted. Reason: §f" + reason + " §c| Expires: §f" + expiry);
            return;
        }

        PlayerData data = plugin.getPlayerDataManager().get(player);
        RankManager rm = plugin.getRankManager();

        String rank = data != null ? data.getRank() : "default";
        String prefix = rm.getPrefix(rank).replace("&", "§");
        String color = rm.getColor(rank).replace("&", "§");

        String format = plugin.getConfig().getString("messages.chat-format",
                "{prefix} &f{player}&7: {color}{message}");
        format = format
                .replace("{prefix}", prefix)
                .replace("{player}", player.getName())
                .replace("{color}", color)
                .replace("{message}", "%2$s")
                .replace("&", "§");

        event.setFormat(format.replace("%2$s", "%2$s"));
    }
}
