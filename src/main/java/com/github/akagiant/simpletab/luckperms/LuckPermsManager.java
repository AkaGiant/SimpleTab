package com.github.akagiant.simpletab.luckperms;

import com.github.akagiant.simpletab.SimpleTab;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.node.NodeMutateEvent;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;

public class LuckPermsManager {

	private static final LuckPerms luckPerms = LuckPermsProvider.get();

	public void register() {
		EventBus eventBus = luckPerms.getEventBus();

		eventBus.subscribe(SimpleTab.getPlugin(), NodeMutateEvent.class, this::onUserModify);
		eventBus.subscribe(SimpleTab.getPlugin(), NodeMutateEvent.class, this::onGroupPrefixChange);

		TeamManager teamManager = new TeamManager();
		teamManager.createTeamsFromServerRoles();
	}

	private void onUserModify(NodeMutateEvent e) {
		if (!e.isUser() || !(e.getTarget() instanceof Player)) return;

		User user = getUser((Player) e.getTarget());
		if (user == null) return;

		Player player = Bukkit.getPlayer(user.getUniqueId());
		TeamManager.updatePlayerTagName(player);
		TabManager.updatePlayerTabName(player);
	}

	private void onGroupPrefixChange(NodeMutateEvent e) {
		if (!e.isGroup()) return;

		Set<Node> nodes = e.getDataAfter();
		for (Node node : nodes) {
			Group group = null;

			String[] key = getKeyFromImmutableNode(node);
			if (!key[0].equalsIgnoreCase("prefix")) return;

			group = LuckPermsManager.getGroupFromPrefix(Integer.parseInt(key[1]), key[2]);
			if (group == null) return;

			String name = TeamManager.getGroupTeamName(group);
			TeamManager.updateTeamPrefix(name);

			for (Player player : Bukkit.getOnlinePlayers()) {
				User user = getUser(player);
				if (user == null) return;
				Group group1 = getUserPrimaryGroup(user);
				if (group1 == null) return;
				TabManager.updatePlayerTabName(player);
			}
		}
	}

	private String[] getKeyFromImmutableNode(Node node) {
		return node.getKey().split("\\.");
	}

	/**
	 * Gets a LuckPerms User
	 *
	 * @param player the player object to get a User Object from
	 * @return a {@link User} object, if one matching the player exists, or null if not
	 * @throws NullPointerException if the player is null
	 */
	public static User getUser(Player player) throws NullPointerException {
		return luckPerms.getUserManager().getUser(player.getUniqueId());
	}

	/**
	 * Gets a users primary group
	 *
	 * @param name the name of the intended player
	 * @return a {@link User} object, if one matching the player exists, or null if not
	 * @throws NullPointerException if the player is null
	 */
	public static User getUser(String name) throws NullPointerException {
		Player player = Bukkit.getPlayer(name);
		if (player == null) return null;
		return getUser(player);
	}

	/**
	 * Gets a group object
	 *
	 * @param groupName the name of the group to get the group of
	 * @return a {@link Group} object, if one matching the group exists, or null if not
	 * @throws NullPointerException if the group is null
	 */
	public static Group getGroup(String groupName) throws NullPointerException {
		return luckPerms.getGroupManager().getGroup(groupName);
	}

	/**
	 * Gets a users primary group
	 *
	 * @param user the user to get the primary group of
	 * @return a {@link Group} object, if one matching the name exists, or null if not
	 * @throws NullPointerException if the name is null
	 */
	public static Group getUserPrimaryGroup(User user) throws NullPointerException {
		return luckPerms.getGroupManager().getGroup(user.getPrimaryGroup());
	}

	/**
	 * Gets a users primary group
	 *
	 * @param player the player to get the primary group of
	 * @return a {@link Group} object, if one matching the name exists, or null if not
	 * @throws NullPointerException if the name is null
	 */
	public static Group getUserPrimaryGroup(Player player) throws NullPointerException {
		User user = getUser(player);
		return getUserPrimaryGroup(user);
	}

	/**
	 * gets a users prefix from their primary group
	 *
	 * @param player the player to get the primary group prefix of
	 * @return a {@link String}
	 * @throws NullPointerException if the prefix is null
	 */
	public static @Nullable String getPrefixFromPlayer(@NotNull Player player) throws NullPointerException {
		User user = getUser(player);
		if (user == null) return null;
		return getPrefixFromUser(user);
	}

	/**
	 * gets a users suffix from their primary group
	 *
	 * @param player the player to get the primary group suffix of
	 * @return a {@link String}
	 * @throws NullPointerException if the suffix is null
	 */
	public static @Nullable String getSuffixFromPlayer(@NotNull Player player) throws NullPointerException {
		User user = getUser(player);
		if (user == null) return null;
		return getSuffixFromUser(user);
	}

	/**
	 * gets a users prefix from their primary group
	 *
	 * @param user the user to get the primary group prefix of
	 * @return a {@link String}
	 * @throws NullPointerException if the prefix is null
	 */
	public static @Nullable String getPrefixFromUser(@NotNull User user) throws NullPointerException {
		Group group = luckPerms.getGroupManager().getGroup(user.getPrimaryGroup());
		CachedMetaData metaData = group.getCachedData().getMetaData();
		if (metaData.getPrefix() == null) return null;
		return metaData.getPrefix();
	}

	/**
	 * gets a users suffix from their primary group
	 *
	 * @param user the user to get the primary group suffix of
	 * @return a {@link String}
	 * @throws NullPointerException if the suffix is null
	 */
	public static @Nullable String getSuffixFromUser(@NotNull User user) throws NullPointerException {
		Group group = luckPerms.getGroupManager().getGroup(user.getPrimaryGroup());
		CachedMetaData metaData = group.getCachedData().getMetaData();
		if (metaData.getSuffix() == null) return null;
		return metaData.getSuffix();
	}

	/**
	 * gets a group from a provided prefix
	 *
	 * @param prefix the prefix to search for in a group.
	 * @return a {@link String}
	 * @throws NullPointerException if the group is null
	 */
	public static @Nullable Group getGroupFromPrefix(int prefixWeight, String prefix) throws NullPointerException {
		for (Group loadedGroup : luckPerms.getGroupManager().getLoadedGroups()) {
			for (Map.Entry<Integer, String> entry : loadedGroup.getCachedData().getMetaData().getPrefixes().entrySet()) {
				if (entry.getValue().equals(prefix) && entry.getKey() == prefixWeight) {
					return loadedGroup;
				}
			}
		}
		return null;
	}

	/**
	 * Get a groups prefix.
	 *
	 * @param group the group to get the prefix of
	 * @return a {@link String} object, if one matching the prefix exists, or null if not
	 * @throws NullPointerException if the prefix is null
	 */
	public static String getGroupPrefix(Group group) {
		CachedMetaData metaData = group.getCachedData().getMetaData();
		if (metaData.getPrefix() == null) return null;
		return metaData.getPrefix();
	}
}
