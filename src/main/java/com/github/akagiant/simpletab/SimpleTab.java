package com.github.akagiant.simpletab;

import com.github.akagiant.simpletab.commands.CommandSimpleTab;
import com.github.akagiant.simpletab.listeners.OnJoinLeave;
import com.github.akagiant.simpletab.listeners.TeamMessageCommandSend;
import com.github.akagiant.simpletab.luckperms.LuckPermsManager;
import com.github.akagiant.simpletab.luckperms.TabManager;
import com.github.akagiant.simpletab.util.ConfigUtil;
import com.github.akagiant.simpletab.util.Logger;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIConfig;
import lombok.Getter;
import me.akagiant.giantapi.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class SimpleTab extends JavaPlugin {

	// TODO: Allow for modification of player name in chat with formatting from LuckPerms.
	// TODO: Implement Update Checker for Spigot.

	public static Config config;

	@Getter
	private static Plugin plugin;

	private static final String SPACER = "&m————————————————————————————————————";

	@Override
	public void onLoad() { CommandAPI.onLoad(new CommandAPIConfig().silentLogs(true)); }

	@Override
	public void onEnable() {
		// Plugin startup logic
		plugin = this;
		CommandAPI.onEnable(this);

		Logger.toConsole(SPACER);
		Logger.toConsole("&fPlugin is loading...");
		Logger.toConsole(SPACER);

		config = new Config(this, "config");

		getServer().getPluginManager().registerEvents(new OnJoinLeave(), this);
		getServer().getPluginManager().registerEvents(new TeamMessageCommandSend(), this);

		LuckPermsManager luckPermsManager = new LuckPermsManager();
		luckPermsManager.register();

		CommandSimpleTab.register();

		boolean tabEnabled = ConfigUtil.getBoolean(config, "tab.enabled");

		if (tabEnabled) {
			TabManager tabManager = new TabManager();
			tabManager.update();
		}


		// Find and Log all Dependencies.
		findDependencies();

		// Plugin Enabled :) Have fun!
		Logger.toConsole(SPACER);
		Logger.toConsole("&ahas been Enabled");
		Logger.toConsole(SPACER);
		Logger.toConsole("&fDeveloped by &aAkaGiant");
		Logger.toConsole("&fVersion: &a" + plugin.getDescription().getVersion());
		Logger.toConsole(SPACER);


	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
		Logger.toConsole(SPACER);
		Logger.toConsole("&ahas been Disabled");
		Logger.toConsole(SPACER);
	}

	private void findDependencies() {
		findFullDepends();
		findSoftDepends();
	}

	private void findFullDepends() {
		if (plugin.getDescription().getDepend().isEmpty()) { return; }

		Logger.toConsole("&m———————————&r &fDependencies &m&8————————————");
		Logger.toConsole("&fLooking for &a" + plugin.getDescription().getDepend().size() + " &fDependencies");

		List<String> found = new ArrayList<>();
		List<String> missing = new ArrayList<>();

		for (String dependency : plugin.getDescription().getDepend()) {
			if (Bukkit.getServer().getPluginManager().getPlugin(dependency) == null) missing.add(dependency);
			else found.add(dependency);
		}

		if (!found.isEmpty()) Logger.toConsole("&fFound &8| &a" + String.join("&8, &a", found));
		if (!missing.isEmpty()) Logger.toConsole("&fMissing &8| &c" + String.join("&8, &c", missing));

	}

	private void findSoftDepends() {
		if (plugin.getDescription().getSoftDepend().isEmpty()) { return; }
		Logger.toConsole("&m——————————&r &fSoft Dependencies &m&8————————");
		Logger.toConsole("&fLooking for &a" + plugin.getDescription().getSoftDepend().size() + " &fSoft Dependencies");

		List<String> found = new ArrayList<>();
		List<String> missing = new ArrayList<>();
		for (String dependency : plugin.getDescription().getSoftDepend()) {
			if (Bukkit.getServer().getPluginManager().getPlugin(dependency) == null) missing.add(dependency);
			else found.add(dependency);
		}

		if (!found.isEmpty()) Logger.toConsole("&fFound &8| &a" + String.join("&8, &a", found));
		if (!missing.isEmpty()) Logger.toConsole("&fMissing &8| &c" + String.join("&8, &c", missing));
	}

}
