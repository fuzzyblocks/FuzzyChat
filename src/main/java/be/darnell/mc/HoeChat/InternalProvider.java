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
package be.darnell.mc.HoeChat;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import net.krinsoft.privileges.Privileges;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import de.bananaco.bpermissions.api.ApiLayer;
import de.bananaco.bpermissions.api.util.CalculableType;

public final class InternalProvider implements MetaDataProvider {

  private HashMap<String, Meta> groups;
  private HashMap<String, Meta> users;
  private HoeChat plugin;
  private FileConfiguration usersConfig, groupsConfig;

  public InternalProvider(HoeChat plugin) {
    this.plugin = plugin;
    loadGroups();
    loadUsers();
  }

  @Override
  public String getPrefix(Player player) {
    if(users.containsKey(player.getName().toLowerCase()))
      return users.get(player.getName().toLowerCase()).prefix;
    
    else if(groups.containsKey(getUserGroup(player).toLowerCase()))
      return groups.get(getUserGroup(player).toLowerCase()).prefix;
    
    return "";
  }

  @Override
  public String getSuffix(Player player) {
    if(users.containsKey(player.getName().toLowerCase()))
      return users.get(player.getName().toLowerCase()).suffix;
    
    else if(groups.containsKey(getUserGroup(player).toLowerCase()))
      return groups.get(getUserGroup(player).toLowerCase()).suffix;
    
    return "";

  }

  class Meta {
    String prefix;
    String suffix;

    Meta(String prefix, String suffix) {
      this.prefix = prefix;
      this.suffix = suffix;
    }
  }

  @Override
  public void setPlayerPrefix(String name, String prefix) {
    Meta meta = users.get(name.toLowerCase());
    if(meta == null) meta = new Meta(prefix, "");
    else meta.prefix = prefix;

    users.put(name.toLowerCase(), meta);
    saveUsers();
  }

  @Override
  public void setPlayerSuffix(String name, String suffix) {
    Meta meta = users.get(name.toLowerCase());
    if(meta == null) meta = new Meta("", suffix);
    else meta.suffix = suffix;

    users.put(name.toLowerCase(), meta);
    saveUsers();
  }

  @Override
  public void setGroupPrefix(String name, String prefix) {
    Meta meta = groups.get(name.toLowerCase());
    if(meta == null) meta = new Meta(prefix, "");
    else meta.prefix = prefix;

    groups.put(name.toLowerCase(), meta);
    saveGroups();

  }

  @Override
  public void setGroupSuffix(String name, String suffix) {
    Meta meta = groups.get(name.toLowerCase());
    if(meta == null) meta = new Meta("", suffix);
    else meta.suffix = suffix;

    groups.put(name.toLowerCase(), meta);
    saveGroups();
  }

  private String getUserGroup(Player p) {
    Plugin bp = plugin.getServer().getPluginManager().getPlugin("bPermissions");
    Plugin priv = plugin.getServer().getPluginManager().getPlugin("Privileges");
    if(bp != null && bp.isEnabled()) {
      return ApiLayer.getGroups(p.getWorld().getName(), CalculableType.USER, p.getName())[0];
    }
    if(priv != null && priv.isEnabled()) {
      return ((Privileges) priv).getGroupManager().getGroup((OfflinePlayer) p).getName();
    }
    return "default";
  }
  
  private void loadUsers() {
    File usersFile = new File(plugin.getDataFolder(), "users.yml");
    if(!usersFile.exists()) {
      usersFile.getParentFile().mkdirs();
      try {
        usersFile.createNewFile();
      } catch (IOException e) {
        HoeChat.log.log(Level.SEVERE, "[HoeChat] Could not create users.yml file.");
        return;
      }
      
    }
    users = new HashMap<String, Meta>();
    usersConfig = YamlConfiguration.loadConfiguration(usersFile);
    ConfigurationSection userConfig = usersConfig.getConfigurationSection("users");
    for(String user : userConfig.getKeys(false)) {
      ConfigurationSection meta = userConfig.getConfigurationSection(user);
      users.put(user.toLowerCase(), new Meta(meta.getString("prefix", ""), meta.getString("suffix", "")));
    }
  }
  
  private void loadGroups() {
    File groupsFile = new File(plugin.getDataFolder(), "groups.yml");
    if(!groupsFile.exists()) {
      groupsFile.getParentFile().mkdirs();
      try {
        groupsFile.createNewFile();
      } catch (IOException e) {
        HoeChat.log.log(Level.SEVERE, "[HoeChat] Could not create users.yml file.");
        return;
      }
      
    }
    groups = new HashMap<String, Meta>();
    groupsConfig = YamlConfiguration.loadConfiguration(groupsFile);
    ConfigurationSection groupConfig = groupsConfig.getConfigurationSection("groups");
    for(String group : groupConfig.getKeys(false)) {
      ConfigurationSection meta = groupConfig.getConfigurationSection(group);
      groups.put(group.toLowerCase(), new Meta(meta.getString("prefix", ""), meta.getString("suffix", "")));
    }
  }

  private void saveUsers() {
    File usersFile = new File(plugin.getDataFolder(), "users.yml");
    FileConfiguration fc = new YamlConfiguration();
    ConfigurationSection userSection = fc.createSection("users");
    for(Map.Entry<String, Meta> entry : users.entrySet()) {
      ConfigurationSection cs = userSection.createSection(entry.getKey());
      cs.set("prefix", entry.getValue().prefix);
      cs.set("suffix", entry.getValue().suffix);
    }
    try {
      fc.save(usersFile);
    } catch(IOException e) {
      HoeChat.log.log(Level.SEVERE, "[HoeChat] Failed to write to users.yml. Changes not saved!");
    }
  }
  
  private void saveGroups() {
    File groupsFile = new File(plugin.getDataFolder(), "groups.yml");
    FileConfiguration fc = new YamlConfiguration();
    ConfigurationSection groupSection = fc.createSection("groups");
    for(Map.Entry<String, Meta> entry : groups.entrySet()) {
      ConfigurationSection cs = groupSection.createSection(entry.getKey());
      cs.set("prefix", entry.getValue().prefix);
      cs.set("suffix", entry.getValue().suffix);
    }
    try {
      fc.save(groupsFile);
    } catch(IOException e) {
      HoeChat.log.log(Level.SEVERE, "[HoeChat] Failed to write to users.yml. Changes not saved!");
    }
  }

}
