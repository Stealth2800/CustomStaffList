package com.stealthyone.bukkit.customstafflist;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.stealthyone.bukkit.customstafflist.CustomStaffList.Log;
import com.stealthyone.bukkit.customstafflist.config.ConfigHelper;

public final class PluginMethods {

	private CustomStaffList plugin;
	
	public PluginMethods(CustomStaffList plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * Returns if a player is vanished or not
	 * @param name
	 * @return
	 */
	public final boolean isVanished(String name) {
		if (!plugin.isHookVanish()) {
			Log.debug("(isVanished) no vanish hook");
			return false;
		} else {
			if (!plugin.getConfig().getBoolean("Hide vanished players")) {
				Log.debug("(isVanished) hiding disabled in config");
				return false;
			}
			
			return plugin.getVanishNoPacketHook().isVanished(name);
		}
	}
	
	public final boolean isVanished(Player player) {
		return isVanished(player.getName());
	}
	
	/**
	 * Constructs the staff list based on online players
	 * @return
	 */
	public final List<String> createStaffList() {
		FileConfiguration config = plugin.getConfig();
		
		List<String> returnList = new ArrayList<String>();
		
		String header = ConfigHelper.MESSAGE_HEADER.get();
		if (header.length() > 0) {
			Log.debug("Added header: " + header);
			returnList.add(ChatColor.translateAlternateColorCodes('&', header));
		}
		
		String name = ConfigHelper.MESSAGE_NAME.get();
		returnList.add(ChatColor.translateAlternateColorCodes('&', name));
		Log.debug("Added name: " + name);
		
		List<String> addedPlayers = new ArrayList<String>();
		String groupNameFormat = ConfigHelper.MESSAGE_GROUPNAME.get();
		for (String groupName : config.getConfigurationSection("Groups").getKeys(false)) {
			// Construct group name //
			StringBuilder line = new StringBuilder();
			String groupDispName = config.getString("Groups." + groupName + ".display name");
			String groupColor = config.getString("Groups." + groupName + ".color");
			if (groupDispName != null && !groupDispName.equalsIgnoreCase(""))
				line.append(groupNameFormat.replace("%groupname%", groupColor + groupDispName));
			else
				line.append(groupNameFormat.replace("%groupname%", groupColor + groupName));
			
			// Get players //
			String groupPerm = config.getString("Groups." + groupName + ".permission");
			Log.debug("GroupPerm: " + groupPerm);
			List<String> playerList = new ArrayList<String>();
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (player.hasPermission(groupPerm)) {
					// If addedPlayers list contains player name, don't add it again //
					if (addedPlayers.contains(player.getName()))
						continue;
					// If hiding vanished players is enabled, don't add the player
					if (ConfigHelper.HIDE_VANISHED.get())
						if (isVanished(player.getName()))
							continue;
					if (ConfigHelper.USE_PLAYER_DISP_NAME.get()) {
						playerList.add(player.getDisplayName());
					} else {
						playerList.add(player.getName());
					}
					if (ConfigHelper.LIMIT_TO_ONE_GROUP.get())
						addedPlayers.add(player.getName());
				}
			}
			
			if (playerList.size() == 0) {
				line.append(ChatColor.translateAlternateColorCodes('&', " " + ConfigHelper.MESSAGE_NO_ONLINE_GROUP_MEMBERS.get()));
				Log.debug("No players in group");
				Log.debug("SHOW_EMPTY_GROUPS: " + ConfigHelper.SHOW_EMPTY_GROUPS.get());
				if (!ConfigHelper.SHOW_EMPTY_GROUPS.get())
					continue;
			} else {
				line.append(" " + playerList.toString().replace("[", "").replace("]", ""));
			}
			Log.debug("Test");
			returnList.add(ChatColor.translateAlternateColorCodes('&', line.toString()));
		}
		
		String footer = ConfigHelper.MESSAGE_FOOTER.get();
		
		if (!ConfigHelper.SHOW_EMPTY_GROUPS.get()) {
			if (2 + ((header.length() > 0) ? 1 : 0) + ((footer.length() > 0) ? 1 : 0) > returnList.size()) {
				Log.debug("No one online, adding no one online message");
				returnList.add(ChatColor.translateAlternateColorCodes('&', ConfigHelper.MESSAGE_NO_ONLINE_STAFF.get()));
			}
		}
		
		if (footer.length() > 0) {
			Log.debug("Added footer: " + footer);
			returnList.add(ChatColor.translateAlternateColorCodes('&', footer));
		}
		
		return returnList;
	}
	
}