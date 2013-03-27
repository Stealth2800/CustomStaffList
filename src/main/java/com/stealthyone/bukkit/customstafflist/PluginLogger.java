package com.stealthyone.bukkit.customstafflist;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class PluginLogger {

	private BasePlugin plugin;
	private Logger log;
	
	public PluginLogger(BasePlugin plugin) {
		this.plugin = plugin;
		log = plugin.getServer().getLogger();
	}
	
	public final void debug(String s) {
		if (plugin.isDebug()) {
			log.log(Level.INFO, BasePlugin.prefix + s);
		}
	}
	
	public final void info(String s) {
		log.log(Level.INFO, BasePlugin.prefix + s);
	}
	
	public final void warning(String s) {
		log.log(Level.WARNING, BasePlugin.prefix + s);
	}
	
	public final void severe(String s) {
		log.log(Level.SEVERE, BasePlugin.prefix + s);
	}
}