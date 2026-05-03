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
import java.util.stream.Collectors;

public class WarpCommand implements CommandExecutor, TabCompleter {

    private final ServerCore plugin;

    public WarpCommand(ServerCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        WarpManager wm = plugin.getWarpManager();

        if (command.getName().equalsIgnoreCase("setwarp")) {
            if (!(sender instanceof Player)) { sender.sendMessage("§cPlayers only."); return true; }
            if (!sender.hasPermission("core.setwarp")) { sender.sendMessage("§cNo permission."); return true; }
            if (args.length < 1) { sender.sendMessage("§cUsage: /setwarp <name>"); return true; }
            Player player = (Player) sender;
            wm.setWarp(args[0], player.getLocation());
            sender.sendMessage("§aWarp §e" + args[0] + " §ahas been set.");
            return true;
        }

        if (command.getName().equalsIgnoreCase("delwarp")) {
            if (!sender.hasPermission("core.delwarp")) { sender.sendMessage("§cNo permission."); return true; }
            if (args.length < 1) { sender.sendMessage("§cUsage: /delwarp <name>"); return true; }
            if (wm.deleteWarp(args[0])) sender.sendMessage("§aWarp §e" + args[0] + " §adeleted.");
            else sender.sendMessage("§cWarp not found.");
            return true;
        }

        // /warp [name]
        if (!(sender instanceof Player)) { sender.sendMessage("§cPlayers only."); return true; }
        Player player = (Player) sender;

        if (args.length < 1) {
            // List warps
            if (wm.getWarpNames().isEmpty()) {
                player.sendMessage("§cNo warps have been set.");
            } else {
                player.sendMessage("§eAvailable warps: §f" + String.join("§7, §f", wm.getWarpNames()));
            }
            return true;
        }

        Location loc = wm.getWarp(args[0]);
        if (loc == null) { player.sendMessage("§cWarp §e" + args[0] + " §cdoes not exist."); return true; }
        player.teleport(loc);
        player.sendMessage("§aTeleported to warp §e" + args[0] + "§a.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            String partial = args[0].toLowerCase();
            return plugin.getWarpManager().getWarpNames().stream()
                    .filter(w -> w.startsWith(partial))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
