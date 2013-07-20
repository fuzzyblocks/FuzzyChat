/*
* Copyright (c) 2013 cedeel.
* All rights reserved.
*
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
* * Redistributions of source code must retain the above copyright
* notice, this list of conditions and the following disclaimer.
* * Redistributions in binary form must reproduce the above copyright
* notice, this list of conditions and the following disclaimer in the
* documentation and/or other materials provided with the distribution.
* * The name of the author may not be used to endorse or promote products
* derived from this software without specific prior written permission.
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
package be.darnell.mc.FuzzyChat.utils;

import be.darnell.mc.FuzzyChat.NicknameProvider;
import org.bukkit.Bukkit;

import java.util.HashSet;

public class Names {

    public static String expandName(String p) {
        HashSet<String> set = new HashSet<String>();
        for (int n = 0; n < Bukkit.getOnlinePlayers().length; n++) {
            String name = Bukkit.getOnlinePlayers()[n].getName();
            if (name.equalsIgnoreCase(p))
                return name;
            if(containsIgnoreCase(name, p)){
                set.add(name);
            }
        }
        if(set.size()==1){
            return set.iterator().next();
        }
        return p;
    }

    public static String expandDisplayName(String p) {
        HashSet<String> set = new HashSet<String>();
        for (String name : NicknameProvider.getDisplayNames().keySet()) {
            String displayName = NicknameProvider.getDisplayNames().get(name);
            if (displayName.equalsIgnoreCase(p))
                return displayName;
            if(containsIgnoreCase(displayName, p)){
                set.add(displayName);
            }
        }
        if(set.size()==1){
            return set.iterator().next();
        }
        return p;
    }

    private static boolean containsIgnoreCase(String search, String entry){
        return search.toLowerCase().contains(entry.toLowerCase());
    }
}
