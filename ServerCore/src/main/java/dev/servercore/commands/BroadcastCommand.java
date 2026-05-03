package dev.servercore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BroadcastCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("core.broadcast")) return true;
        if (args.length == 0) return false;

        String message = String.join(" ", args);
        // Cleaned up the broadcast logic to remove invalid tokens
        Bukkit.broadcastMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + "ALERT" + ChatColor.DARK_RED + "] "
                + ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', message));
        return true;
    }
}