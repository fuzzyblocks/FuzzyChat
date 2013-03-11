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

import be.darnell.mc.FuzzyLog.FuzzyLog;
import be.darnell.mc.FuzzyLog.LogFacility;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * @author cedeel
 */
@SuppressWarnings("unused")
public class FuzzyChatListener implements Listener {

    private static Pattern chatColorPattern = Pattern.compile("&([a-fA-F0-9])");
    private static Pattern chatMagicPattern = Pattern.compile("&([k])", Pattern.CASE_INSENSITIVE);
    private static Pattern chatStylePattern = Pattern.compile("&([lmnor])", Pattern.CASE_INSENSITIVE);
    public final static String MESSAGE_FORMAT = "<%prefix%player%suffix> %message";
    private boolean useLogger = false;
    private static LogFacility logger;
    protected String messageFormat = MESSAGE_FORMAT;
    protected String displayNameFormat = "%prefix%player%suffix";
    protected String optionDisplayname = "display-name-format";
    protected String optionMessageFormat = "message-format";
    private final MetaDataProvider meta;

    public FuzzyChatListener(FileConfiguration config, MetaDataProvider meta) {
        this.meta = meta;
        messageFormat = config.getString(optionMessageFormat, this.messageFormat);
        useLogger = config.getBoolean("useLogger", useLogger);
        if (useLogger) {
            if (Bukkit.getPluginManager().getPlugin("FuzzyLog") != null) {
                FuzzyLog.addFacility("CHAT");
                logger = FuzzyLog.getFacility("CHAT");
                Bukkit.getLogger().info("[FuzzyChat] Got logger: " + logger.toString());
            } else {
                useLogger = false;
                Bukkit.getLogger().warning("FuzzyChat: useLogger set to true, but FuzzyLog not found.");
            }

        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Player player = event.getPlayer();

        String message = messageFormat;
        String chatMessage = event.getMessage();

        if (player.hasPermission("fuzzychat.chat.color")) {
            chatMessage = this.colorize(chatMessage);
        }
        if (player.hasPermission("fuzzychat.chat.magic")) {
            chatMessage = this.magicify(chatMessage);
        }
        if (player.hasPermission("fuzzychat.chat.stylize")) {
            chatMessage = this.stylize(chatMessage);
        }

        message = message.replace("%message", "%2$s").replace("%displayname", "%1$s");
        message = this.replacePlayerPlaceholders(player, message);

        event.setFormat(message);
        event.setMessage(chatMessage);

        if (useLogger) {
            event.setCancelled(true);
            StringBuilder toLog = new StringBuilder(32);
            toLog.append(event.getPlayer().getName());
            toLog.append(": ");
            toLog.append(event.getMessage());

            logger.log(ChatColor.stripColor(toLog.toString()));

            for (Player p : event.getRecipients()) {
                p.sendMessage(String.format(event.getFormat(), player, event.getMessage()));
            }
        }

    }

    protected String replacePlayerPlaceholders(Player player, String format) {
        format = format.replace("%prefix", meta.getPrefix(player))
                .replace("%suffix", meta.getSuffix(player))
                .replace("%world", player.getWorld().getName())
                .replace("%player", player.getDisplayName());
        format = colorize(format);
        format = magicify(format);
        format = stylize(format);
        return format;
    }

    /**
     * @param string The uncolored string
     * @return The string with color codes.
     */
    protected String colorize(String string) {
        if (string == null) {
            return "";
        }

        return chatColorPattern.matcher(string).replaceAll("\u00A7$1");
    }

    /**
     * @param string The string to "magicify"
     * @return A string with &K replaced by §K
     */
    protected String magicify(String string) {
        if (string == null) {
            return "";
        }
        return chatMagicPattern.matcher(string).replaceAll("\u00A7$1");
    }

    /**
     * @param chatMessage The string to stylize
     * @return The same string, with replaced style codes.
     */
    private String stylize(String chatMessage) {
        if (chatMessage == null) {
            return "";
        }
        return chatStylePattern.matcher(chatMessage).replaceAll("\u00A7$1");
    }
}
