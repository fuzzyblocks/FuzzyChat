/*
 * Copyright (c) 2012 cedeel.
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
package net.fuzzyblocks.FuzzyChat;

import net.fuzzyblocks.FuzzyChat.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author cedeel
 */
public class FuzzyChat extends JavaPlugin {

    private NicknameProvider nickprovider;
    private MetaDataProvider metaProvider;

    @Override
    public void onEnable() {
        FileConfiguration config = this.getConfig();

        config.addDefaults(getConfig());
        this.saveDefaultConfig();

        // Metadata provider
        metaProvider = new InternalProvider(this);

        nickprovider = new NicknameProvider(this);

        getServer().getPluginManager().registerEvents(new LoginListener(nickprovider), this);
        getServer().getPluginManager().registerEvents(new FuzzyChatListener(config, metaProvider), this);
        getLogger().info(this + " started.");

        this.saveConfig();

        registerCommands();
    }

    @Override
    public void onDisable() {
        getLogger().info(this + " stopped.");
    }

    /**
     * Get the name of a player, expand if online
     * @param playerName The player to get the name of
     * @return The player's full name, or the original string if none exists.
     */
    public static String getPlayerNameString(String playerName) {
        try {
            //noinspection deprecation
            return Bukkit.getServer().getPlayer(playerName).getName();
        } catch (NullPointerException ignored) {
            // Player not online.
        }
        return playerName;
    }

    private void registerCommands() {
        this.getCommand("setprefix").setExecutor(new SetPrefix(metaProvider));
        this.getCommand("setsuffix").setExecutor(new SetSuffix(metaProvider));
        this.getCommand("setnick").setExecutor(new SetNick(nickprovider));
        this.getCommand("whois").setExecutor(new Whois());
    }
}
