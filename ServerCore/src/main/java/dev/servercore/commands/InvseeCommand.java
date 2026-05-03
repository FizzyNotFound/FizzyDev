package dev.servercore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class InvseeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // 1. Check if the sender is a player
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        // 2. Check permissions
        if (!player.hasPermission("core.invsee")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to use this.");
            return true;
        }

        // 3. Check if a target name was provided
        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /invsee <player>");
            return true;
        }

        // 4. Find the target player
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "Player not found.");
            return true;
        }

        // 5. Open the inventory
        player.openInventory(target.getInventory());
        player.sendMessage(ChatColor.GREEN + "Opening inventory of " + ChatColor.YELLOW + target.getName());
        return true;
    }
}