package com.github.akagiant.simpletab.luckperms;

import com.github.akagiant.simpletab.SimpleTab;
import com.github.akagiant.simpletab.util.ConfigUtil;
import com.github.akagiant.simpletab.util.InternalPlaceholderManager;
import me.akagiant.giantapi.util.ColorManager;
import me.clip.placeholderapi.PlaceholderAPI;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TabManager {

	/**
	 * Gets a users primary group
	 *
	 * @param player the player to update the tab name of.
	 */
	public static void updatePlayerTabName(Player player) {
		User user = LuckPermsManager.getUser(player);
		String prefix = LuckPermsManager.getPrefixFromUser(user);
		if (prefix == null) return;

		String stringFormatting = ConfigUtil.getString(SimpleTab.config, "tab-formatting");
		if (stringFormatting == null) return;

		InternalPlaceholderManager.format(player, stringFormatting);

		player.setPlayerListName(ColorManager.formatColours(stringFormatting));
	}

	public static void updatePlayerHeader(@NotNull Player player) {
		player.setPlayerListHeader(PlaceholderAPI.setPlaceholders(player, getHeader()));
	}

	public static void updatePlayerFooter(@NotNull Player player) {
		player.setPlayerListFooter(PlaceholderAPI.setPlaceholders(player, getFooter()));
	}

	public static @NotNull String getHeader() {
		List<String> header = ConfigUtil.getStringList(SimpleTab.config, "tab.header");
		StringBuilder stringBuilder = new StringBuilder();
		for (String string : header) {
			stringBuilder.append(string).append("\n");
		}
		return stringBuilder.toString();
	}

	public static @NotNull String getFooter() {
		List<String> header = ConfigUtil.getStringList(SimpleTab.config, "tab.footer");
		StringBuilder stringBuilder = new StringBuilder();
		for (String string : header) {
			stringBuilder.append(string).append("\n");
		}
		return stringBuilder.toString();
	}


	public void update() {
		final int interval = ConfigUtil.getInt(SimpleTab.config, "tab.interval") * 20;

		new BukkitRunnable() {
			@Override
			public void run() {
				if (Bukkit.getOnlinePlayers().size() > 0) {
					for (Player player : Bukkit.getOnlinePlayers()) {
						player.setPlayerListHeader(PlaceholderAPI.setPlaceholders(player, getHeader()));
						player.setPlayerListFooter(PlaceholderAPI.setPlaceholders(player, getFooter()));
					}
				}
			}
		}.runTaskTimer(SimpleTab.getPlugin(), 0, interval);

	}

}
