/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.darnell.mc.FuzzyChat.commands;

import be.darnell.mc.FuzzyChat.NicknameProvider;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author LankyLord
 */
public class Whois implements CommandExecutor {

    private NicknameProvider provider;

    public Whois(NicknameProvider nicks) {
        provider = nicks;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmnd, String string, String[] args) {
        if (args.length != 1)
            return false;
        else {
            String user = provider.getUser(args[0].toLowerCase());
            if(user != null) {
                Player player = Bukkit.getPlayer(user);
                String actual = player != null ? player.getName() : user;
                String nick = provider.getNick(user);
                sender.sendMessage(ChatColor.AQUA + nick + "'s actual name is " + actual);
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "User doesn't have a nickname");
                return false;
            }

        }
    }
}