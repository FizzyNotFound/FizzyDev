package dev.servercore.commands;

import dev.servercore.ServerCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DelWarpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("core.delwarp")) return true;

        if (args.length < 1) {
            sender.sendMessage("§cUsage: /delwarp <name>");
            return true;
        }

        String warpName = args[0].toLowerCase();
        // Logic to remove from your WarpManager or Config
        ServerCore.getInstance().getConfig().set("warps." + warpName, null);
        ServerCore.getInstance().saveConfig();

        sender.sendMessage("§eWarp §f" + warpName + " §ehas been deleted.");
        return true;
    }
}