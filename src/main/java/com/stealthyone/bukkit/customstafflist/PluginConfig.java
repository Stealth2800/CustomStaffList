package com.stealthyone.bukkit.customstafflist;

import org.bukkit.configuration.file.FileConfiguration;

public final class PluginConfig {

	private BasePlugin plugin;
	
	public PluginConfig(BasePlugin plugin) {
		this.plugin = plugin;
	}
	
	public final void load() {
		plugin.reloadConfig();
		FileConfiguration config = plugin.getConfig();
		
		config.addDefault("Debug", false);
		config.addDefault("Hide vanished players", true);
		config.addDefault("Groups.Admin", "customstafflist.admin");
		
		config.options().copyDefaults(true);
		plugin.saveConfig();
	}
}