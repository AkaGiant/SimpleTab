package com.github.akagiant.simpletab.util;

import com.github.akagiant.simpletab.luckperms.LuckPermsManager;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.Map;

public class InternalPlaceholderManager {

	private InternalPlaceholderManager() {
		//no instance
	}
	
	public static String format(OfflinePlayer player, String str) {
		String formattedString = str;
		

		Map<String, Object> replaceableValues = new HashMap<>();
		replaceableValues.put("[luckperms primary group prefix]", LuckPermsManager.getPrefixFromPlayer(player.getPlayer()));
		replaceableValues.put("[luckperms primary group suffix]",
			LuckPermsManager.getSuffixFromPlayer(player.getPlayer()));
		replaceableValues.put("[player name]", player.getName());
		replaceableValues.put("[player displayname]", player.getPlayer().getDisplayName());

		for (Map.Entry<String, Object> entry : replaceableValues.entrySet()) {
			formattedString = formattedString.replace(entry.getKey(), String.valueOf(entry.getValue()));
		}

		return formattedString;
	}

}
