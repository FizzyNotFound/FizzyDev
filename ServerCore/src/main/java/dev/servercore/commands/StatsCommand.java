package dev.servercore.commands;

import dev.servercore.ServerCore;
import dev.servercore.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StatsCommand implements CommandExecutor {

    private final ServerCore plugin;

    public StatsCommand(ServerCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player target;

        if (args.length >= 1) {
            target = Bukkit.getPlayer(args[0]);
            if (target == null) { sender.sendMessage("§cPlayer not found or offline."); return true; }
        } else {
            if (!(sender instanceof Player)) { sender.sendMessage("§cSpecify a player name."); return true; }
            target = (Player) sender;
        }

        PlayerData data = plugin.getPlayerDataManager().get(target);
        if (data == null) { sender.sendMessage("§cNo data found for that player."); return true; }

        String rank = plugin.getRankManager().getPrefix(data.getRank()).replace("&", "§");
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");

        sender.sendMessage("§8§m---§r §e" + target.getName() + "'s Stats §8§m---");
        sender.sendMessage("§7Rank: " + rank);
        sender.sendMessage("§7Kills: §f" + data.getKills());
        sender.sendMessage("§7Deaths: §f" + data.getDeaths());
        double kdr = data.getDeaths() == 0 ? data.getKills() : (double) data.getKills() / data.getDeaths();
        sender.sendMessage(String.format("§7KDR: §f%.2f", kdr));
        sender.sendMessage("§7Playtime: §f" + data.getFormattedPlaytime());
        sender.sendMessage("§7First joined: §f" + sdf.format(new Date(data.getFirstJoin())));
        sender.sendMessage("§8§m---------");
        return true;
    }
}
