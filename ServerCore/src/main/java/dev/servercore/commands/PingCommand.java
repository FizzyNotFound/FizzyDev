package dev.servercore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PingCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Determine the target: either the player who typed the command or someone else in args
        Player target = (sender instanceof Player) ? (Player) sender : null;

        if (args.length > 0) {
            target = Bukkit.getPlayer(args[0]);
        }

        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found.");
            return true;
        }

        // Get the latency (ping) and send it to the sender
        int ping = target.getPing();
        sender.sendMessage(ChatColor.YELLOW + target.getName() + "'s Ping: " + ChatColor.GREEN + ping + "ms");
        return true;
    }
}