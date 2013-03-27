package com.stealthyone.bukkit.customstafflist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public final class PluginMethods {

	private BasePlugin plugin;
	
	public PluginMethods(BasePlugin plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * Returns if a player is vanished or not
	 * @param name
	 * @return
	 */
	public final boolean isVanished(String name) {
		if (!plugin.isHookVanish()) {
			plugin.getLog().debug("(isVanished) no vanish hook");
			return false;
		} else {
			if (!plugin.getConfig().getBoolean("Hide vanished players")) {
				plugin.getLog().debug("(isVanished) hiding disabled in config");
				return false;
			}
			
			return plugin.isVanished(name);
		}
	}
	
	public final List<String> createStaffList() {
		FileConfiguration config = plugin.getConfig();
		
		Player[] onlinePlayers = plugin.getServer().getOnlinePlayers();
		Map<String, Object> values = config.getConfigurationSection("Groups").getValues(true);
		
		if (values.size() == 0 || onlinePlayers.length == 0) {
			return null;
		}
		Object[] keys = values.keySet().toArray();
		plugin.getLog().debug("keys size: " + keys.length);
		plugin.getLog().debug("keys: " + Arrays.toString(keys));
		
		List<String> returnList = new ArrayList<String>();
		
		for (int i = 0; i < values.size(); i++) {
			String groupName = keys[i].toString();
			String permissionToCheck = config.getConfigurationSection("Groups").getString(groupName);
			List<String> applicablePlayers = new ArrayList<String>();
			for (int j = 0; j < onlinePlayers.length; j++) {
				if (onlinePlayers[j].hasPermission(permissionToCheck)) {
					if (!plugin.getMethods().isVanished(onlinePlayers[j].getName())) {
						applicablePlayers.add(onlinePlayers[j].getName());
					}
				}
			}
			if (applicablePlayers.size() > 0) {
				returnList.add(ChatColor.GOLD + "[" + groupName + "]: " + ChatColor.GREEN + applicablePlayers.toString().replace(Pattern.quote("["), "").replace(Pattern.quote("]"), ""));
			}
		}
		
		return returnList;
	}
}