package dev.servercore.commands;

import dev.servercore.ServerCore;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnmuteCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("core.mute")) {
            sender.sendMessage("§cNo permission.");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage("§cUsage: /unmute <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found.");
            return true;
        }

        // Sets muted status to false in your manager
        ServerCore.getInstance().getMuteManager().setMuted(target.getUniqueId(), false);
        sender.sendMessage("§aYou have successfully unmuted §f" + target.getName());
        target.sendMessage("§aYou are no longer muted and can chat again.");
        return true;
    }
}