package com.github.akagiant.simpletab;

import com.github.akagiant.simpletab.commands.CommandSimpleTab;
import com.github.akagiant.simpletab.listeners.OnJoinLeave;
import com.github.akagiant.simpletab.listeners.TeamMessageCommandSend;
import com.github.akagiant.simpletab.luckperms.LuckPermsManager;
import com.github.akagiant.simpletab.luckperms.TabManager;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIConfig;
import lombok.Getter;
import me.akagiant.giantapi.util.Config;
import me.akagiant.giantapi.util.ConfigUtil;
import me.akagiant.giantapi.util.Logger;
import me.akagiant.giantapi.util.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class SimpleTab extends JavaPlugin {

	// TODO: Allow for modification of player name in chat with formatting from LuckPerms.
	// TODO: Implement Update Checker for Spigot.

	public static Config config;
	public static Logger logger;

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
		logger = new Logger(this);

		logger.toConsole(SPACER);
		logger.toConsole("&fPlugin is loading...");

		// Register Configuration Files
		config = new Config(this, "config");

		// Register Events
		getServer().getPluginManager().registerEvents(new OnJoinLeave(), this);
		getServer().getPluginManager().registerEvents(new TeamMessageCommandSend(), this);

		// Register the LuckPermsManager
		LuckPermsManager luckPermsManager = new LuckPermsManager();
		luckPermsManager.register();

		// Register Commands
		CommandSimpleTab.register();

		// If Tab Updating is enabled, start the update.
		if (ConfigUtil.getBoolean(config, "tab.enabled")) {
			TabManager tabManager = new TabManager();
			tabManager.update();
		}


		// Find and Log all Dependencies.
		findDependencies();

		// Check for Updates.
		checkForUpdates();

		// Plugin Enabled :) Have fun!
		logger.toConsole(SPACER);
		logger.toConsole("&ahas been Enabled");
		logger.toConsole(SPACER);
		logger.toConsole("&fDeveloped by &aAkaGiant");
		logger.toConsole("&fVersion: &a" + plugin.getDescription().getVersion());
		logger.toConsole(SPACER);
	}

	private void checkForUpdates() {
		logger.toConsole("&m—————————&r &fUpdate Checker &m&8———————————");
		try {
			UpdateChecker updateChecker = new UpdateChecker(this, new URL("https://raw.githubusercontent" +
				".com/AkaGiant/SimpleTab/dev/pom.xml"));

			if (updateChecker.updateIsAvailable()) {
				logger.toConsole("There is a new Update Available");
				logger.toConsole("Current: " + updateChecker.getCurrentVersion());
				logger.toConsole("New: " + updateChecker.latestVersion);
			} else {
				logger.toConsole("&fNo new update found");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
		logger.toConsole(SPACER);
		logger.toConsole("&ahas been Disabled");
		logger.toConsole(SPACER);
	}

	private void findDependencies() {
		findFullDepends();
		findSoftDepends();
	}

	private void findFullDepends() {
		if (plugin.getDescription().getDepend().isEmpty()) { return; }

		logger.toConsole("&m——————————&r &fDependencies &m&8————————————");
		logger.toConsole("&fLooking for &a" + plugin.getDescription().getDepend().size() + " &fDependencies");

		List<String> found = new ArrayList<>();
		List<String> missing = new ArrayList<>();

		for (String dependency : plugin.getDescription().getDepend()) {
			if (Bukkit.getServer().getPluginManager().getPlugin(dependency) == null) missing.add(dependency);
			else found.add(dependency);
		}

		if (!found.isEmpty()) logger.toConsole("&fFound &8| &a" + String.join("&8, &a", found));
		if (!missing.isEmpty()) logger.toConsole("&fMissing &8| &c" + String.join("&8, &c", missing));

	}

	private void findSoftDepends() {
		if (plugin.getDescription().getSoftDepend().isEmpty()) { return; }
		logger.toConsole("&m—————————&r &fSoft Dependencies &m&8————————");
		logger.toConsole("&fLooking for &a" + plugin.getDescription().getSoftDepend().size() + " &fSoft Dependencies");

		List<String> found = new ArrayList<>();
		List<String> missing = new ArrayList<>();
		for (String dependency : plugin.getDescription().getSoftDepend()) {
			if (Bukkit.getServer().getPluginManager().getPlugin(dependency) == null) missing.add(dependency);
			else found.add(dependency);
		}

		if (!found.isEmpty()) logger.toConsole("&fFound &8| &a" + String.join("&8, &a", found));
		if (!missing.isEmpty()) logger.toConsole("&fMissing &8| &c" + String.join("&8, &c", missing));
	}

}
