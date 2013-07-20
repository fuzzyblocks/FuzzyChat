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
package be.darnell.mc.FuzzyChat;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Level;

/**
 *
 * @author LankyLord
 */
public final class NicknameProvider {

    private final FuzzyChat plugin;
    private static HashMap<String, String> userToDisplayName = new HashMap<String, String>();
    private static final HashMap<String, String> displayToUserName = new HashMap<String, String>();
    private FileConfiguration nicks = null;
    private File nicksFile = null;

    NicknameProvider(FuzzyChat plugin) {
        this.plugin = plugin;
        userToDisplayName = loadNicks();
        for (Entry<String, String> entry : userToDisplayName.entrySet())
            displayToUserName.put(entry.getValue().toLowerCase(), entry.getKey());
    }

    public String getNick(String userName) {
        String value = userToDisplayName.get(userName.toLowerCase());
        if (value == null)
            return Bukkit.getPlayer(userName).getName();
        else
            return value;
    }

    public String getUser(String displayName) {
        return displayToUserName.get(displayName);
    }

    public void setNick(String userName, String displayName) {
        if (userToDisplayName.containsKey(userName.toLowerCase()))
            displayToUserName.remove(getNick(userName.toLowerCase()));
        userToDisplayName.put(userName.toLowerCase(), displayName);
        displayToUserName.put(displayName.toLowerCase(), userName.toLowerCase());
    }

    private void reloadNicks() {
        if (nicksFile == null)
            nicksFile = new File(plugin.getDataFolder(), "nicknames.yml");
        nicks = YamlConfiguration.loadConfiguration(nicksFile);
    }

    public void saveNicks() {
        if (nicks == null || nicksFile == null)
            return;
        try {
            this.addNicksToFile();
            getNickConfig().save(nicksFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save nicks to " + nicksFile, e);
        }
    }

    private void addNicksToFile() {
        for (String user : userToDisplayName.keySet()) {
            String display = userToDisplayName.get(user);
            this.getNickConfig().set(user, display);
        }
    }

    private FileConfiguration getNickConfig() {
        if (nicks == null)
            reloadNicks();
        return nicks;
    }

    HashMap<String, String> loadNicks() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        for (String user : this.getNickConfig().getKeys(false)) {
            String display = this.getNickConfig().getString(user);
            hashMap.put(user, display);
        }
        return hashMap;
    }
}