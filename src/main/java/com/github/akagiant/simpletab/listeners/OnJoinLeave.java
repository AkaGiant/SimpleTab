package com.github.akagiant.simpletab.listeners;

import com.github.akagiant.simpletab.luckperms.TabManager;
import com.github.akagiant.simpletab.luckperms.TeamManager;
import me.akagiant.giantapi.util.ColorManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnJoinLeave implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		TabManager.updatePlayerHeader(e.getPlayer());
		TabManager.updatePlayerFooter(e.getPlayer());
		TabManager.updatePlayerTabName(e.getPlayer());

		TeamManager.updatePlayerTagName(e.getPlayer());

		e.setJoinMessage(ColorManager.formatColours("&e" + e.getPlayer().getName() + " joined the game"));
	}
}
