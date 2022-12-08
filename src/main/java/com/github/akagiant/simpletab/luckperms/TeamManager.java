package com.github.akagiant.simpletab.luckperms;

import com.github.akagiant.simpletab.SimpleTab;
import com.github.akagiant.simpletab.util.ConfigUtil;
import com.github.akagiant.simpletab.util.InternalPlaceholderManager;
import com.github.akagiant.simpletab.util.Logger;
import jdk.jpackage.internal.Log;
import me.akagiant.giantapi.util.ColorManager;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

public class TeamManager {

	private static final LuckPerms luckPerms = LuckPermsProvider.get();

	/**
	 * Get the team a player is currently in.
	 *
	 * @param player the player to get the team of
	 * @return a {@link Team} object, if one matching the name exists, or null if not
	 * @throws NullPointerException if the name is null
	 */
	public static @Nullable Team getTeamFromPlayer(Player player) throws NullPointerException {
		Group group = LuckPermsManager.getUserPrimaryGroup(player);
		if (group == null) return null;

		Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

		String teamName = getGroupTeamName(group);
		if (teamName == null) return null;
		Team team = scoreboard.getTeam(teamName);
		if (team == null) return null;
		return team;
	}

	/**
	 * Updates a players tag name
	 * @param player the player to update the tag of.
	 */
	public static void updatePlayerTagName(Player player) {
		Team team = TeamManager.getTeamFromPlayer(player);
		team.addPlayer(player);
	}

	/**
	 * Update a teams prefix based on the corresponding LuckPerms prefix.
	 * @param teamName the name of the team we wish to update
	 */
	public static void updateTeamPrefix(String teamName) {
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
		Team team = scoreboard.getTeam(teamName);
		Group group = LuckPermsManager.getGroup(team.getName().split("_")[1]);
		String prefix = LuckPermsManager.getGroupPrefix(group);

		team.setPrefix(ColorManager.formatColours(prefix + " &8| "));
	}

	/**
	 * Creates Teams for every LuckPerms group with weightings for identification and prioritisation in TAB
	 */
	public void createTeamsFromServerRoles() {
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
		Map<String, Integer> sortedGroups = sortByWeights();

		for (int i = 0; i < sortedGroups.size(); i++) {
			String key = (String) sortedGroups.keySet().toArray()[i];
			if (teamExists(key)) continue;

			Logger.toConsole("&fCreating Team &a" + key);

			Team team = scoreboard.registerNewTeam(i + "_" + key);
			TeamManager.updateTeamPrefix(i + "_" + key);
			team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
		}
	}

	/**
	 * Gets a users primary group
	 * @return a {@link Map} object, of String (group name) and Integer (group weight) from LuckPerms
	 */
	private static Map<String, Integer> sortByWeights() {
		Map<String, Integer> values = new HashMap<>();

		if (luckPerms.getGroupManager().getLoadedGroups().isEmpty()) return Collections.emptyMap();

		for (Group loadedGroup : luckPerms.getGroupManager().getLoadedGroups()) {
			String name = loadedGroup.getName();

			if (!loadedGroup.getWeight().isPresent()) continue;

			int weight = loadedGroup.getWeight().getAsInt();
			values.put(name, weight);
		}

		return values
			.entrySet()
			.stream()
			.sorted(Collections.reverseOrder(comparingByValue()))
			.collect(
				toMap(Map.Entry::getKey, Map.Entry::getValue, (g1, g2) -> g2,
					LinkedHashMap::new));

	}

	/**
	 * get a groups team name
	 *
	 * @param group the group to get the team name of.
	 * @return a {@link String}, if one matching the name exists, or null if not
	 * @throws NullPointerException if the name is null
	 */
	public static @Nullable String getGroupTeamName(@NotNull Group group) throws NullPointerException {
		String groupName = group.getName();
		Map<String, Integer> teams = sortByWeights();

		for (int i = 0; i < teams.size(); i++) {
			String teamsGroup = (String) teams.keySet().toArray()[i];
			if (groupName.equalsIgnoreCase(teamsGroup)) {
				return i + "_" + groupName;
			}
		}
		return null;
	}

	/**
	 * Check weather or not a team exists by a given team name.
	 *
	 * @param teamName the team name to check existence of.
	 * @return a {@link Boolean}, if one matching the name exists, or null if not
	 */
	private static boolean teamExists(String teamName) {
		for (Team team : Bukkit.getScoreboardManager().getMainScoreboard().getTeams()) {
			if (team.getName().split("_")[1].equals(teamName)) return true;
		}
		return false;
	}
}
