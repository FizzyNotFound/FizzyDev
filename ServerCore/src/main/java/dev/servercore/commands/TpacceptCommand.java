package dev.servercore.commands;

import dev.servercore.ServerCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.UUID;

public class TpacceptCommand implements CommandExecutor {
    private final ServerCore plugin;

    public TpacceptCommand(ServerCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        // Check if there is a pending request for this player
        UUID requesterUUID = plugin.tpaRequests.get(player.getUniqueId());

        if (requesterUUID == null) {
            player.sendMessage(ChatColor.RED + "You do not have any pending teleport requests.");
            return true;
        }

        Player requester = Bukkit.getPlayer(requesterUUID);

        if (requester != null && requester.isOnline()) {
            requester.teleport(player.getLocation());
            requester.sendMessage(ChatColor.GREEN + "Teleporting to " + player.getName() + "...");
            player.sendMessage(ChatColor.GREEN + "You accepted the teleport request.");
        } else {
            player.sendMessage(ChatColor.RED + "The player who sent the request is no longer online.");
        }

        // Clear the request after it is processed
        plugin.tpaRequests.remove(player.getUniqueId());
        return true;
    }
}