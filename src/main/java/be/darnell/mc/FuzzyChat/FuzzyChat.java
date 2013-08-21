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
package be.darnell.mc.FuzzyChat;

import be.darnell.mc.FuzzyChat.commands.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author cedeel
 */
public class FuzzyChat extends JavaPlugin {

    static final Logger log = Logger.getLogger("Minecraft");
    private NicknameProvider nickprovider;

    @Override
    public void onEnable() {
        FileConfiguration config = this.getConfig();

        config.addDefaults(getConfig());
        this.saveDefaultConfig();

        // Metadata provider resolution
        MetaDataProvider provider;

        String pr = config.getString("provider", "internal");
        if (pr.equalsIgnoreCase("auto"))
            provider = resolveProvider();
        else
            provider = new InternalProvider(this);

        nickprovider = new NicknameProvider(this);
        getServer().getPluginManager().registerEvents(new LoginListener(nickprovider), this);
        getServer().getPluginManager().registerEvents(new FuzzyChatListener(config, provider), this);
        log.info(this + " started.");


        this.saveConfig();

        this.getCommand("setprefix").setExecutor(new SetPrefix(provider));
        this.getCommand("setsuffix").setExecutor(new SetSuffix(provider));
        this.getCommand("setnick").setExecutor(new SetNick(nickprovider));
        this.getCommand("whois").setExecutor(new Whois(nickprovider));
    }

    @Override
    public void onDisable() {
        log.info(this + " stopped.");
    }

    /**
     * Get the name of a player, expand if online
     * @param s The player to get the name of
     * @return The player's full name, or the original string if none exists.
     */
    public static String getPlayerNameString(String s) {
        try {
            return Bukkit.getServer().getPlayer(s).getName();
        } catch (NullPointerException e) {
            // Player not online.
        }
        return s;
    }

    private MetaDataProvider resolveProvider() {
        Plugin bp = getServer().getPluginManager().getPlugin("bPermissions");
        if (bp != null && bp.isEnabled())
            return new BananaProvider();
        log.log(Level.WARNING, "[FuzzyChat] No supported external metadata provider found. Falling back to internal.");
        return new InternalProvider(this);
    }
}
