package com.github.akagiant.simpletab.util;

import com.github.akagiant.simpletab.util.Logger;
import me.akagiant.giantapi.util.ColorManager;
import me.akagiant.giantapi.util.Config;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class ConfigUtil {
	private ConfigUtil() {
		//no instance
	}

	public static String getString(Config config, String path) {
		if (!isSet(config, path)) return null;
		return ColorManager.formatColours(config.getConfig().getString(path));
	}

	public static List<String> getStringList(Config config, String path) {
		if (!isSet(config, path)) return new ArrayList<>();
		return ColorManager.formatColours(config.getConfig().getStringList(path));
	}

	public static boolean getBoolean(Config config, String path) {
		if (!isSet(config, path)) return false;
		return config.getConfig().getBoolean(path);
	}

	public static boolean isSet(Config config, String path) {
		return config.getConfig().isSet(path);
	}

	public static double getDouble(Config config, String path) {
		if (!isSet(config, path)) {
			Logger.severe("&f" + path + " is &cNULL");
			return 0.0;
		}
		return config.getConfig().getDouble(path);
	}

	public static int getInt(Config config, String path) {
		if (!isSet(config, path)) {
			Logger.severe("&f" + path + " is &cNULL");
			return 0;
		}
		return config.getConfig().getInt(path);
	}

	public static boolean isSetAndStringIsValid(Config config, String path) {
		return config.getConfig().isSet(path) && config.getConfig().getString(path) != null;
	}

	public static boolean isSetAndIsBoolean(Config config, String path) {
		return config.getConfig().isSet(path) && config.getConfig().getBoolean(path);
	}

	public static ConfigurationSection getConfigurationSection(Config config, String path) {
		if (config.getConfig().getConfigurationSection(path) == null) return null;
		return config.getConfig().getConfigurationSection(path);
	}

}
