package com.stealthyone.bukkit.customstafflist;

import org.bukkit.plugin.java.JavaPlugin;
import org.kitteh.vanish.staticaccess.VanishNoPacket;
import org.kitteh.vanish.staticaccess.VanishNotLoadedException;

import com.stealthyone.bukkit.customstafflist.commands.CmdStaffList;

public final class BasePlugin extends JavaPlugin {

	private class HookVanish {
		
		private BasePlugin plugin;
		
		public HookVanish(BasePlugin plugin) {
			this.plugin = plugin;
		}
		
		public boolean isVanished(String name) {
			try {
				return VanishNoPacket.isVanished(name);
			} catch (VanishNotLoadedException e) {
				return false;
			}
		}
	}
	
	public static final String prefix = "[CustomStaffList] ";
	
	private boolean isHookVanish;
	
	private HookVanish hookVanish;
	
	private PluginConfig config;
	private PluginLogger log;
	private PluginMethods methods;
	
	@Override
	public void onEnable() {
		/* Setup config */
		config = new PluginConfig(this);
		config.load();
		
		/* Setup log */
		log = new PluginLogger(this);
		
		/* Setup hooks */
		isHookVanish = getServer().getPluginManager().getPlugin("VanishNoPacket") != null;
		if (isHookVanish) {
			log.info("VanishNoPacket hooked successfully");
			hookVanish = new HookVanish(this);
		}
		
		/* Setup misc useful stuff */
		methods = new PluginMethods(this);
		
		/* Register commands */
		getCommand("stafflist").setExecutor(new CmdStaffList(this));
		
		log.info("SimpleStaffList v" + this.getVersion() + " by Stealth2800 enabled!");
	}
	
	@Override
	public void onDisable() {
		log.info("SimpleStaffList v" + this.getVersion() + " by Stealth2800 disabled!");
	}
	
	/**
	 * Returns if the plugin is in debug mode or not
	 * @return
	 */
	public final boolean isDebug() {
		return this.getConfig().getBoolean("Debug");
	}
	
	/**
	 * Returns the plugin version
	 * @return
	 */
	public final String getVersion() {
		return this.getDescription().getVersion();
	}
	
	/**
	 * Returns the log handler for logging purposes
	 * @return
	 */
	public final PluginLogger getLog() {
		return this.log;
	}
	
	/**
	 * Returns the plugin methods handler
	 * @return
	 */
	public final PluginMethods getMethods() {
		return this.methods;
	}
	
	/**
	 * Returns if the plugin is hooked with vanish or not
	 * @return
	 */
	public final boolean isHookVanish() {
		return this.isHookVanish;
	}
	
	public final boolean isVanished(String name) {
		if (hookVanish == null) {
			return false;
		} else {
			return hookVanish.isVanished(name);
		}
	}
}