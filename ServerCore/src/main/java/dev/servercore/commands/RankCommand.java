package dev.servercore.commands;

import dev.servercore.ServerCore;
import dev.servercore.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankCommand implements CommandExecutor {

    private final ServerCore plugin;

    public RankCommand(ServerCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // /rank set <player> <rank>
        if (args.length >= 3 && args[0].equalsIgnoreCase("set")) {
            if (!sender.hasPermission("core.rank.set")) { sender.sendMessage("§cNo permission."); return true; }

            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) { sender.sendMessage("§cPlayer not found or offline."); return true; }

            String rankName = args[2].toLowerCase();
            if (!plugin.getRankManager().rankExists(rankName)) {
                sender.sendMessage("§cRank §e" + rankName + " §cdoes not exist. Available: §f"
                        + String.join("§7, §f", plugin.getRankManager().getRankNames()));
                return true;
            }

            PlayerData data = plugin.getPlayerDataManager().get(target);
            if (data == null) { sender.sendMessage("§cCould not find data for that player."); return true; }

            data.setRank(rankName);
            plugin.getPlayerDataManager().save(data);

            String prefix = plugin.getRankManager().getPrefix(rankName).replace("&", "§");
            sender.sendMessage("§aSet §e" + target.getName() + "§a's rank to " + prefix + "§a.");
            target.sendMessage("§aYour rank has been updated to " + prefix + "§a.");
            return true;
        }

        // /rank list
        if (args.length >= 1 && args[0].equalsIgnoreCase("list")) {
            sender.sendMessage("§eAvailable ranks: §f" + String.join("§7, §f", plugin.getRankManager().getRankNames()));
            return true;
        }

        sender.sendMessage("§cUsage: /rank set <player> <rank> | /rank list");
        return true;
    }
}
