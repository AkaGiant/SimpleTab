package com.github.akagiant.simpletab.util;

import com.github.akagiant.simpletab.SimpleTab;
import me.akagiant.giantapi.util.ColorManager;
import org.bukkit.Bukkit;

public class Logger {

	private Logger() {
		//no instance
	}

	public static void severe(String msg) {
		Bukkit.getConsoleSender().sendMessage(
			ColorManager.formatColours("&8[&c" + SimpleTab.getPlugin().getName() + " &c&lSEVERE&8] &f" + msg)
		);
	}

	public static void toConsole(String msg) {
		Bukkit.getConsoleSender().sendMessage(
			ColorManager.formatColours("&8[&b" + SimpleTab.getPlugin().getName() + "&8] " + msg)
		);
	}

}
