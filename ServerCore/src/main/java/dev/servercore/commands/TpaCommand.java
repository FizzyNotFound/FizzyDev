package dev.servercore.commands;

import dev.servercore.ServerCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpaCommand implements CommandExecutor {
    private final ServerCore plugin;

    public TpaCommand(ServerCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /tpa <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "Player not found.");
            return true;
        }

        plugin.tpaRequests.put(target.getUniqueId(), player.getUniqueId());

        player.sendMessage(ChatColor.GREEN + "Teleport request sent to " + target.getName() + ".");
        target.sendMessage(ChatColor.YELLOW + player.getName() + " wants to teleport to you.");
        target.sendMessage(ChatColor.YELLOW + "Type " + ChatColor.GOLD + "/tpaccept" + ChatColor.YELLOW + " to confirm.");
        return true;
    }
}