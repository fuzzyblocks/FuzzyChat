/*
 * Copyright (c) 2013 LankyLord.
 * All rights reserved.
 * 
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * The name of the author may not be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package be.darnell.mc.FuzzyChat.commands;

import be.darnell.mc.FuzzyChat.FuzzyChat;
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
public class SetNick implements CommandExecutor {

    private FuzzyChat instance;

    public SetNick(FuzzyChat instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
//        Player player = (Player) sender;
//        if (args.length < 1)
//            return false;
//        if (args.length == 1)
//            player.setDisplayName(args[0]);
//        setDisplayName(player.getName(), args[0]);
//        player.sendMessage(ChatColor.AQUA + "Nickname changed to " + args[0]);
//        return true;
        if(args.length < 1 || args.length > 2) return false;
        String nick;
        if(args.length == 1 && sender instanceof Player) {
            nick = args[0];
            Player player = (Player)sender;
            instance.nickprovider.setNick(player.getName(), nick);
            player.setDisplayName(args[0]);
            player.sendMessage(ChatColor.AQUA + "Nickname changed to " + nick);
        } else if(args.length == 2) {
            nick = args[1];
            String target = args[0];
            instance.nickprovider.setNick(target, nick);
            Player player = Bukkit.getPlayer(target);
            if(player != null) {
                player.setDisplayName(nick);
                sender.sendMessage(ChatColor.AQUA + player.getName() + "'s nickname changed to " + nick);
            }
        }
        return true;
    }
}
