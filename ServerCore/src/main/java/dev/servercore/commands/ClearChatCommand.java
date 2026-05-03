package dev.servercore.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ClearChatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("servercore.clearchat")) return true;

        for (int i = 0; i < 100; i++) {
            Bukkit.broadcastMessage("");
        }

        Bukkit.broadcastMessage("§eChat was cleared by §6" + sender.getName());
        return true;
    }
}