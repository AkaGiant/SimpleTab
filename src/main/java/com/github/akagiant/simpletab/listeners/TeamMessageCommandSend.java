package com.github.akagiant.simpletab.listeners;

import com.github.akagiant.simpletab.SimpleTab;
import me.akagiant.giantapi.util.ConfigUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class TeamMessageCommandSend implements Listener {

	@EventHandler
	public void onCommandSend(PlayerCommandPreprocessEvent e) {

 		if (e.getMessage().contains("teammsg") && ConfigUtil.getBoolean(SimpleTab.config, "override-team-message-command")) {
			e.setCancelled(true);
		}
	}


}
