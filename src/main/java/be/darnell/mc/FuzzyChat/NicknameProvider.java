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

import java.io.*;
import java.util.HashMap;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 *
 * @author LankyLord
 */
public final class NicknameProvider {

    private FuzzyChat plugin;
    public static HashMap<String, String> displaynames = new HashMap<String, String>();
    public static HashMap<String, String> usernames = new HashMap<String, String>();

    NicknameProvider(FuzzyChat plugin) {
        this.plugin = plugin;
        displaynames = loadNicks();
        for (Entry<String, String> entry : displaynames.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            usernames.put(value.toLowerCase(), key);
        }
    }

    public String getNick(String playername) {
        String value = displaynames.get(playername);
        if (value == null)
            return playername;
        else
            return value;
    }

    public String getUser(String nickname) {
        String value = usernames.get(nickname);
        return value;
    }

    public void setNick(String playername, String nick) {
        displaynames.put(playername.toLowerCase(), nick);
        usernames.put(nick, playername.toLowerCase());
    }

    public void saveNicks() {
        File displaynamesFile = new File(plugin.getDataFolder(), "displaynames.txt");
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(displaynamesFile));
            out.writeObject(displaynames);
        } catch (Exception e) {
            plugin.getServer().getLogger().severe("Could not write displaynames to file");
        }
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, String> loadNicks() {
        File displaynamesFile = new File(plugin.getDataFolder(), "displaynames.txt");
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(displaynamesFile));
            return (HashMap<String, String>) in.readObject();
        } catch (Exception e) {
            plugin.getServer().getLogger().severe("Could not read displaynames from file");
            return new HashMap<String, String>();


        }
    }
}