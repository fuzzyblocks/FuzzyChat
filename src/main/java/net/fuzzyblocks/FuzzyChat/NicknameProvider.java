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
package net.fuzzyblocks.FuzzyChat;

import net.fuzzyblocks.FuzzyChat.utils.Names;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.util.Set;
import java.util.logging.Level;

/**
 *
 * @author LankyLord
 */
public final class NicknameProvider {

    private static BiMap<String, String> displayNames = HashBiMap.create();
    private final FuzzyChat plugin;
    private FileConfiguration nicks = null;
    private File nicksFile = null;

    NicknameProvider(FuzzyChat plugin) {
        this.plugin = plugin;
        displayNames = loadNicks();
    }

    public static BiMap<String, String> getDisplayNames() {
        return displayNames;
    }

    /**
     * Get the nickname from a player name
     * @param userName The name of the player
     * @return The nickname of the player
     */
    public static String getNick(String userName) {
        String value = displayNames.get(Names.expandName(userName).toLowerCase());
        if (value == null)
            return Bukkit.getPlayer(userName).getName();
        else
            return value;
    }

    /**
     * Get the player name from a nickname
     * @param displayName The nickname
     * @return The player name
     */
    public static String getUser(String displayName) {
        return displayNames.inverse().get(Names.expandDisplayName(displayName));
    }

    /**
     * Set the nickname of a player
     * @param userName The player
     * @param displayName The nickname
     */
    public boolean setNick(String userName, String displayName) {
        if (!displayNames.containsValue(displayName)) {
            displayNames.put(userName.toLowerCase(), displayName);
            return true;
        }
        return false;
    }

    /**
     * Remove the nickname of a player
     * @param userName The player
     */
    public boolean removeNick(String userName) {
        if (displayNames.containsKey(userName.toLowerCase())) {
            displayNames.remove(userName.toLowerCase());
            getNickConfig().set(userName.toLowerCase(), null);
            return true;
        }
        return false;
    }

    private void reloadNicks() {
        if (nicksFile == null)
            nicksFile = new File(plugin.getDataFolder(), "nicknames.yml");
        nicks = YamlConfiguration.loadConfiguration(nicksFile);
    }

    /**
     * Save nicknames asynchronously
     */
    public void saveNicksAsync() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new BukkitRunnable() {
            @Override
            public void run() {
                saveNicks();
            }
        });
    }

    /**
     * Save nicknames to file
     */
    private void saveNicks() {
        if (nicks == null || nicksFile == null)
            return;
        try {
            // Add nicknames to config
            for (String user : displayNames.keySet()) {
                String display = displayNames.get(user);
                this.getNickConfig().set(user, display);
            }
            getNickConfig().save(nicksFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save nicks to " + nicksFile, e);
        }
    }

    private FileConfiguration getNickConfig() {
        if (nicks == null)
            reloadNicks();
        return nicks;
    }

    BiMap<String, String> loadNicks() {
        Set<String> users = this.getNickConfig().getKeys(false);
        HashBiMap<String, String> hashBiMap = HashBiMap.create(users.size());
        for (String user : users) {
            String display = this.getNickConfig().getString(user);
            hashBiMap.put(user, display);
        }
        return hashBiMap;
    }
}