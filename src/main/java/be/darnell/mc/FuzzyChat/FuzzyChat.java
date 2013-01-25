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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 
 * @author cedeel
 */
public class FuzzyChat extends JavaPlugin {
  protected static final Logger log = Logger.getLogger("Minecraft");
  protected FuzzyChatListener listener;
  protected MetaDataProvider provider;

  @Override
  public void onEnable() {
    PluginDescriptionFile pdf = this.getDescription();
    
    FileConfiguration config = this.getConfig();

    config.addDefaults(getConfig());

    // Metadata provider resolution
    String pr = config.getString("provider", "internal");
    if(pr.equalsIgnoreCase("auto")) provider = resolveProvider();
    else provider = new InternalProvider(this);

    this.listener = new FuzzyChatListener(config, provider);
    getServer().getPluginManager().registerEvents(listener, this);
    log.info(pdf.getName() + " v" + pdf.getVersion() + " started.");

    this.saveConfig();
  }

  @Override
  public void onDisable() {
    PluginDescriptionFile pdf = this.getDescription();
    log.info(pdf.getName() + " v" + pdf.getVersion() + " stopped.");
  }
  
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if(!(args.length > 2))
      return false;
    
    String cmd = args[0];
    
    StringBuilder ms = new StringBuilder();
    for(int i = 2; i < args.length; i++) {
      ms.append(args[i]).append(" ");
    }
    String metadata = ms.toString().trim();
    
    String UnsupportedBackend = "Operation not supported with current backend.";
    if(cmd.equalsIgnoreCase("pp")) {
      try {
        String player = getPlayerNameString(args[1]);
        provider.setPlayerPrefix(player, metadata);
        sender.sendMessage(player + "'s prefix set to: " + ChatColor.translateAlternateColorCodes("&".charAt(0),
                provider.getPrefix(getServer().getOfflinePlayer(player))));
      } catch (UnsupportedOperationException e) {
        sender.sendMessage(UnsupportedBackend);
        return false;
      }
      return true;
    }
    
    else if(cmd.equalsIgnoreCase("ps")) {
      try {
        String player = getPlayerNameString(args[1]);
        provider.setPlayerSuffix(player, metadata);
        sender.sendMessage(player + "'s suffix set to: " + ChatColor.translateAlternateColorCodes("&".charAt(0),
                provider.getSuffix(getServer().getOfflinePlayer(player))));
      } catch (UnsupportedOperationException e) {
        sender.sendMessage(UnsupportedBackend);
        return false;
      }
      return true;
    }
    
    else if(cmd.equalsIgnoreCase("gp")) {
      try {
        provider.setGroupPrefix(args[1], metadata);
      } catch (UnsupportedOperationException e) {
        sender.sendMessage(UnsupportedBackend);
        return false;
      }
      return true;
    }
    
    else if(cmd.equalsIgnoreCase("gs")) {
      try {
        provider.setGroupSuffix(args[1], metadata);
      } catch (UnsupportedOperationException e) {
        sender.sendMessage(UnsupportedBackend);
        return false;
      }
      return true;
    }
    return false;
  }
  
  private String getPlayerNameString(String s) {
    try {
      return getServer().getPlayer(s).getName();
    } catch (NullPointerException e) {
      // Player not online.
    }
    return s;
  }
  
  private MetaDataProvider resolveProvider() {
    Plugin bp = getServer().getPluginManager().getPlugin("bPermissions");
    if(bp != null && bp.isEnabled()) return new BananaProvider();
    log.log(Level.WARNING, "[FuzzyChat] No supported external metadata provider found. Falling back to internal.");
    return new InternalProvider(this);
  }
}
