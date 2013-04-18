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
    private String nick;
    private String user;
    private String actual;

    public Whois(NicknameProvider nicks) {
        provider = nicks;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmnd, String string, String[] args) {
        if (args.length != 1)
            return false;
        else {
            nick = args[0].toLowerCase();
            user = provider.getUser(nick);
            Player player = Bukkit.getPlayer(user);
            if (player == null) {
                sender.sendMessage(ChatColor.RED + "Nickname not found.");
                return false;
            }
            if (player != null) {
                String playername = player.getName();
                actual = provider.getNick(user);
                sender.sendMessage(ChatColor.AQUA + actual + "'s actual name is " + playername);
            }
            return false;


        }
    }
}