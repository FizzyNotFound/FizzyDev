package dev.servercore.commands;

import dev.servercore.ServerCore;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (!player.hasPermission("core.setspawn")) {
            player.sendMessage("§cNo permission.");
            return true;
        }

        Location loc = player.getLocation();
        ServerCore.getInstance().getConfig().set("spawn.world", loc.getWorld().getName());
        ServerCore.getInstance().getConfig().set("spawn.x", loc.getX());
        ServerCore.getInstance().getConfig().set("spawn.y", loc.getY());
        ServerCore.getInstance().getConfig().set("spawn.z", loc.getZ());
        ServerCore.getInstance().getConfig().set("spawn.yaw", loc.getYaw());
        ServerCore.getInstance().getConfig().set("spawn.pitch", loc.getPitch());
        ServerCore.getInstance().saveConfig();

        player.sendMessage("§a§lSUCCESS! §7Spawn point has been set at your location.");
        return true;
    }
}