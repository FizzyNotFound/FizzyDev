package dev.servercore.commands;

import dev.servercore.ServerCore;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class VanishCommand implements CommandExecutor {

    private final ServerCore plugin;
    public static final Set<UUID> vanishedPlayers = new HashSet<>();

    public VanishCommand(ServerCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("core.vanish")) { sender.sendMessage("§cNo permission."); return true; }
        if (!(sender instanceof Player)) { sender.sendMessage("§cPlayers only."); return true; }

        Player player = (Player) sender;

        if (vanishedPlayers.contains(player.getUniqueId())) {
            // Reappear
            vanishedPlayers.remove(player.getUniqueId());
            for (Player online : Bukkit.getOnlinePlayers()) {
                online.showPlayer(plugin, player);
            }
            player.sendMessage("§aYou are now §2visible§a.");
        } else {
            // Vanish
            vanishedPlayers.add(player.getUniqueId());
            for (Player online : Bukkit.getOnlinePlayers()) {
                if (!online.hasPermission("core.vanish.see")) {
                    online.hidePlayer(plugin, player);
                }
            }
            player.sendMessage("§aYou are now §7vanished§a.");
        }
        return true;
    }
}
