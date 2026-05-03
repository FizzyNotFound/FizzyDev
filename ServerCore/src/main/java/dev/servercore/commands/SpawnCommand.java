package dev.servercore.commands;

import dev.servercore.ServerCore;
import dev.servercore.managers.WarpManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SpawnCommand implements CommandExecutor {

    private final ServerCore plugin;

    public SpawnCommand(ServerCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("setspawn")) {
            if (!(sender instanceof Player)) { sender.sendMessage("§cPlayers only."); return true; }
            if (!sender.hasPermission("core.setspawn")) { sender.sendMessage("§cNo permission."); return true; }
            Player player = (Player) sender;
            plugin.getWarpManager().setSpawn(player.getLocation());
            sender.sendMessage("§aSpawn location set.");
            return true;
        }

        // /spawn
        if (!(sender instanceof Player)) { sender.sendMessage("§cPlayers only."); return true; }
        Player player = (Player) sender;
        Location spawn = plugin.getWarpManager().getSpawn();
        if (spawn == null) { player.sendMessage("§cSpawn has not been set yet."); return true; }
        player.teleport(spawn);
        player.sendMessage("§aTeleported to spawn.");
        return true;
    }
}
