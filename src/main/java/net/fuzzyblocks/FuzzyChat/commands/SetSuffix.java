/*
 * Copyright (c) 2013 cedeel.
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
package net.fuzzyblocks.FuzzyChat.commands;

import net.fuzzyblocks.FuzzyChat.FuzzyChat;
import net.fuzzyblocks.FuzzyChat.MetaDataProvider;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * @author cedeel
 */
public class SetSuffix implements CommandExecutor {

    private final MetaDataProvider provider;

    public SetSuffix(MetaDataProvider provider) {
        this.provider = provider;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1)
            return false;

        String name, metadata;

        // Setting a group suffix
        if (args.length > 1 && args[0].equalsIgnoreCase("-g")) {
            name = args[1];

            StringBuilder ms = new StringBuilder();
            for (int i = 2; i < args.length; i++) {
                ms.append(args[i]).append(" ");
            }
            metadata = ms.toString().trim();

            provider.setGroupSuffix(name, metadata);

            return true;
        } // Setting a user suffix
        else {
            name = FuzzyChat.getPlayerNameString(args[0]);

            try {
                if (args.length == 1) {
                    provider.setPlayerSuffix(name, "");
                    sender.sendMessage(name + "'s suffix removed successfully.");
                    return true;
                }

                StringBuilder ms = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    ms.append(args[i]).append(" ");
                }
                metadata = ms.toString().trim();
                provider.setPlayerSuffix(name, metadata);
                sender.sendMessage(name + "'s suffix set to: " + ChatColor.translateAlternateColorCodes('&',
                        provider.getSuffix(Bukkit.getServer().getOfflinePlayer(name))));
            } catch (UnsupportedOperationException e) {
                sender.sendMessage("Operation not supported with current backend.");
                return false;
            }

            return true;
        }
    }
}
