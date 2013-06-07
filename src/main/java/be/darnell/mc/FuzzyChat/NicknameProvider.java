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

import java.io.*;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 *
 * @author LankyLord
 */
public final class NicknameProvider {

    private FuzzyChat plugin;
    public static HashMap<String, String> userToDisplayName = new HashMap<String, String>();
    public static HashMap<String, String> displayToUserName = new HashMap<String, String>();

    NicknameProvider(FuzzyChat plugin) {
        this.plugin = plugin;
        userToDisplayName = loadNicks();
        for (Entry<String, String> entry : userToDisplayName.entrySet()) {
            displayToUserName.put(entry.getValue().toLowerCase(), entry.getKey());
        }
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
        if(userToDisplayName.containsKey(userName.toLowerCase()))
            displayToUserName.remove(getNick(userName.toLowerCase()));
        userToDisplayName.put(userName.toLowerCase(), displayName);
        displayToUserName.put(displayName.toLowerCase(), userName.toLowerCase());
    }

    public void saveNicks() {
        File displaynamesFile = new File(plugin.getDataFolder(), "userToDisplayName.txt");
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(displaynamesFile));
            out.writeObject(userToDisplayName);
        } catch (Exception e) {
            plugin.getServer().getLogger().severe("Could not write userToDisplayName to file");
        }
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, String> loadNicks() {
        File displaynamesFile = new File(plugin.getDataFolder(), "userToDisplayName.txt");
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(displaynamesFile));
            return (HashMap<String, String>) in.readObject();
        } catch (Exception e) {
            plugin.getServer().getLogger().severe("Could not read userToDisplayName from file");
            return new HashMap<String, String>();


        }
    }
}