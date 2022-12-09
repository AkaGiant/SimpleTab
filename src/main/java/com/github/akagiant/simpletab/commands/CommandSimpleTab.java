package com.github.akagiant.simpletab.commands;

import com.github.akagiant.simpletab.SimpleTab;
import com.github.akagiant.simpletab.luckperms.TabManager;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.LiteralArgument;
import me.akagiant.giantapi.util.ConfigUtil;

public class CommandSimpleTab {

	private CommandSimpleTab() {
		//no instance
	}
	
	public static void register() {
		new CommandAPICommand("simpletab")
			.withAliases("st")
			.withPermission("simpletab.reload")
			.withArguments(new LiteralArgument("reload"))
			.executes((commandSender, objects) -> {
				SimpleTab.config.reloadConfig();
				boolean tabEnabled = ConfigUtil.getBoolean(SimpleTab.config, "tab.enabled");

				if (tabEnabled) {
					TabManager tabManager = new TabManager();
					tabManager.update();
				}

				commandSender.sendMessage("Configuration Files Reloaded");
			})
			.register();

		new CommandAPICommand("simpletab")
			.withAliases("st")
			.withPermission("simpletab.info")
			.withArguments(new LiteralArgument("info"))
			.executes((commandSender, objects) -> {
				commandSender.sendMessage("Hi There! My name is AkaGiant, and i'm the creator of SimpleTab!");
				commandSender.sendMessage("My GitHub: https://github.com/AkaGiant");
				commandSender.sendMessage("Installed Version: " + SimpleTab.getPlugin().getDescription().getVersion());
				commandSender.sendMessage("Latest Version: ...");
			})
			.register();
	}

}
