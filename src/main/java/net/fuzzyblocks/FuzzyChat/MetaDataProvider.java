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

import org.bukkit.OfflinePlayer;

public interface MetaDataProvider {

    /**
     * Get the prefix of a player
     * @param player The player to whom the prefix belongs
     * @return The prefix of the player, if none exists an empty string.
     */
    public String getPrefix(OfflinePlayer player);

    /**
     * Get the suffix of a player
     * @param player The player to whom the suffix belongs
     * @return The suffix of the player, if none exists an empty string.
     */
	public String getSuffix(OfflinePlayer player);

    /**
     * Set the prefix of a player
     * @param name The name of the player
     * @param prefix The prefix to be applied
     * @throws UnsupportedOperationException
     */
    public void setPlayerPrefix(String name, String prefix) throws UnsupportedOperationException;

    /**
     * Set the suffix of a player
     * @param name The name of the player
     * @param suffix The suffix to be applied
     * @throws UnsupportedOperationException
     */
	public void setPlayerSuffix(String name, String suffix) throws UnsupportedOperationException;

    /**
     * Set the prefix of a group
     * @param name The name of the group
     * @param prefix The prefix to be applied
     * @throws UnsupportedOperationException
     */
    public void setGroupPrefix(String name, String prefix) throws UnsupportedOperationException;

    /**
     * Set the suffix of a group
     * @param name The name of the group
     * @param suffix The suffix to be applied
     * @throws UnsupportedOperationException
     */
	public void setGroupSuffix(String name, String suffix) throws UnsupportedOperationException;
}
