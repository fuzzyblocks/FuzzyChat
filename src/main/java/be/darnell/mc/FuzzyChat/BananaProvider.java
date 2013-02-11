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

import de.bananaco.bpermissions.api.ApiLayer;
import de.bananaco.bpermissions.api.util.CalculableType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public final class BananaProvider implements MetaDataProvider {

	@Override
	public String getPrefix(OfflinePlayer player) {
		try {
      Player p = Bukkit.getPlayer(player.getName());
      return ApiLayer.getValue(p.getWorld().getName(), CalculableType.USER, p.getName(), "prefix");
    } catch (NullPointerException e) {}
    return ApiLayer.getValue(Bukkit.getWorlds().get(0).getName(), CalculableType.USER, player.getName(), "prefix");
	}

	@Override
	public String getSuffix(OfflinePlayer player) {
		try {
      Player p = Bukkit.getPlayer(player.getName());
      return ApiLayer.getValue(p.getWorld().getName(), CalculableType.USER, p.getName(), "suffix");
    } catch (NullPointerException e) {}
    return ApiLayer.getValue(Bukkit.getWorlds().get(0).getName(), CalculableType.USER, player.getName(), "suffix");
	}

	@Override
	public void setPlayerPrefix(String name, String prefix) {
		throw new UnsupportedOperationException("Not currently supported."); 
		
	}

	@Override
	public void setPlayerSuffix(String name, String suffix) {
		throw new UnsupportedOperationException("Not currently supported");
		
	}

	@Override
	public void setGroupPrefix(String name, String prefix) {
		throw new UnsupportedOperationException("Not currently supported");
		
	}

	@Override
	public void setGroupSuffix(String name, String suffix) {
		throw new UnsupportedOperationException("Not currently supported");
		
	}

}