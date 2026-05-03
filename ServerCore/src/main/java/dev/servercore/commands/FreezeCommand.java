package dev.servercore.commands;

import dev.servercore.ServerCore;
import dev.servercore.listeners.PlayerDataListener;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FreezeCommand implements CommandExecutor {

    private final ServerCore plugin;

    public FreezeCommand(ServerCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("core.freeze")) { sender.sendMessage("§cNo permission."); return true; }
        if (args.length < 1) { sender.sendMessage("§cUsage: /freeze <player>"); return true; }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) { sender.sendMessage("§cPlayer not found or offline."); return true; }

        if (PlayerDataListener.frozenPlayers.contains(target.getUniqueId())) {
            PlayerDataListener.frozenPlayers.remove(target.getUniqueId());
            sender.sendMessage("§aUnfroze §e" + target.getName() + "§a.");
            target.sendMessage("§aYou have been unfrozen.");
        } else {
            PlayerDataListener.frozenPlayers.add(target.getUniqueId());
            sender.sendMessage("§aFroze §e" + target.getName() + "§a.");
            target.sendMessage("§cYou have been frozen by a staff member. Do not log out.");
        }
        return true;
    }
}
