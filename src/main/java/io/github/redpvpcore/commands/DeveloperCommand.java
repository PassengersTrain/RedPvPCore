package io.github.redpvpcore.commands;

import io.github.redpvpcore.util.InsuranceManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeveloperCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        String cmd = args[0];
        Player player = (Player) commandSender;
        switch (cmd) {
            case "getdata":
                Player p = Bukkit.getPlayer(args[1]);
                InsuranceManager ins = new InsuranceManager();
                if(p.getName() == null) {
                    player.sendMessage(ChatColor.RED + "Username cannot be null.");
                }
                player.sendMessage(ChatColor.DARK_GRAY + "Username: " + ChatColor.GRAY + p.getName());
                player.sendMessage(ChatColor.DARK_GRAY + "Insured Items: " + ChatColor.GRAY + ins.getName());
                player.sendMessage(ChatColor.DARK_GRAY + "Insurance Duration: " + ChatColor.GRAY + ins.getTime());
        }
        return false;
    }
}
