package dev.servercore.commands;

import dev.servercore.ServerCore;
import dev.servercore.listeners.PlayerDataListener;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MuteCommand implements CommandExecutor {

    private final ServerCore plugin;

    public MuteCommand(ServerCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean isUnmute = command.getName().equalsIgnoreCase("unmute");

        if (!sender.hasPermission("core.mute")) { sender.sendMessage("§cNo permission."); return true; }

        if (isUnmute) {
            if (args.length < 1) { sender.sendMessage("§cUsage: /unmute <player>"); return true; }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) { sender.sendMessage("§cPlayer not found or offline."); return true; }
            plugin.getMuteManager().unmute(target.getUniqueId());
            sender.sendMessage("§aUnmuted §e" + target.getName() + "§a.");
            target.sendMessage("§aYou have been unmuted.");
            return true;
        }

        // /mute <player> [duration] [reason]
        if (args.length < 1) { sender.sendMessage("§cUsage: /mute <player> [duration] [reason]"); return true; }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) { sender.sendMessage("§cPlayer not found or offline."); return true; }

        long durationMillis = -1;
        String reason = "No reason provided";

        if (args.length >= 2) {
            try {
                durationMillis = parseDuration(args[1]);
            } catch (IllegalArgumentException e) {
                reason = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));
            }
        }
        if (args.length >= 3 && durationMillis != -1) {
            reason = String.join(" ", java.util.Arrays.copyOfRange(args, 2, args.length));
        }

        plugin.getMuteManager().mute(target.getUniqueId(), durationMillis, reason);
        String expiry = plugin.getMuteManager().getExpiryFormatted(target.getUniqueId());
        sender.sendMessage("§aMuted §e" + target.getName() + "§a. Duration: §f" + expiry + "§a. Reason: §f" + reason);
        target.sendMessage("§cYou have been muted. Reason: §f" + reason + " §c| Duration: §f" + expiry);
        return true;
    }

    private long parseDuration(String input) {
        long millis = 0;
        String num = "";
        for (char c : input.toCharArray()) {
            if (Character.isDigit(c)) {
                num += c;
            } else {
                long val = num.isEmpty() ? 0 : Long.parseLong(num);
                switch (c) {
                    case 's': millis += val * 1000; break;
                    case 'm': millis += val * 60000; break;
                    case 'h': millis += val * 3600000; break;
                    case 'd': millis += val * 86400000L; break;
                    default: throw new IllegalArgumentException("Unknown unit: " + c);
                }
                num = "";
            }
        }
        if (millis == 0) throw new IllegalArgumentException("No valid duration");
        return millis;
    }
}
