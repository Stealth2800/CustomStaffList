package com.stealthyone.bukkit.customstafflist;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

import com.stealthyone.bukkit.customstafflist.commands.CmdStaffList;
import com.stealthyone.bukkit.customstafflist.config.ConfigHelper;
import com.stealthyone.bukkit.customstafflist.hooks.HookVanishNoPacket;
import com.stealthyone.bukkit.customstafflist.utils.UpdateCheckRunnable;
import com.stealthyone.bukkit.customstafflist.utils.UpdateChecker;

public final class CustomStaffList extends JavaPlugin {
	
	public final static class Log {
		
		public final static void debug(String msg) {
			if (ConfigHelper.DEBUG.get())
				instance.logger.log(Level.INFO, String.format("[%s DEBUG] %s", CustomStaffList.getInstance().getName(), msg));
		}

		public final static void info(String msg) {
			instance.logger.log(Level.INFO, String.format("[%s] %s", CustomStaffList.getInstance().getName(), msg));
		}
		
		public final static void warning(String msg) {
			instance.logger.log(Level.WARNING, String.format("[%s] %s", CustomStaffList.getInstance().getName(), msg));
		}
		
		public final static void severe(String msg) {
			instance.logger.log(Level.SEVERE, String.format("[%s] %s", CustomStaffList.getInstance().getName(), msg));
		}
	}
	
	private static CustomStaffList instance;
	{
		instance = this;
	}
	
	public final static CustomStaffList getInstance() {
		return instance;
	}
	
	private boolean isHookVanish;
	
	private HookVanishNoPacket hookVanish;
	
	private Logger logger;
	private PluginMethods methods;
	
	@Override
	public final void onLoad() {
		// Setup logger //
		logger = getServer().getLogger();
		
		// Create directory if it doesn't already exist //
		if (!getDataFolder().exists())
			getDataFolder().mkdir();
	}
	
	@Override
	public void onEnable() {
		// Setup config //
		saveDefaultConfig();
		getConfig().options().copyDefaults(false);
		saveConfig();
		
		// Setup hooks //
		isHookVanish = getServer().getPluginManager().getPlugin("VanishNoPacket") != null;
		if (isHookVanish) {
			hookVanish = new HookVanishNoPacket(this);
			String vanishVersion = getServer().getPluginManager().getPlugin("VanishNoPacket").getDescription().getVersion();
			Log.info(String.format("Successfully hooked with VanishNoPacket v%s", vanishVersion));
		} else {
			Log.info("Didn't find VanishNoPacket.");
		}
		
		// Check config value for hiding vanished players //
		if (ConfigHelper.HIDE_VANISHED.get()) {
			if (!isHookVanish)
				Log.info("Config has 'Hide vanished players' set to TRUE but VanishNoPacket is not installed!");
			else
				Log.info("Hiding vanished players ENABLED.");
		} else {
			Log.info("Hiding vanished players DISABLED.");
		}
		
		// Setup important plugin pieces //
		methods = new PluginMethods(this);
		
		// Register commands //
		getCommand("stafflist").setExecutor(new CmdStaffList(this));
		
		getServer().getScheduler().runTaskTimerAsynchronously(this, new UpdateCheckRunnable(this), 40, 432000);
		Log.info(String.format("%s v%s by Stealth2800 enabled!", getName(), getVersion()));
	}
	
	@Override
	public void onDisable() {
		Log.info(String.format("%s v%s by Stealth2800 disabled!", getName(), getVersion()));
	}
	
	public final void reloadAll() {
		reloadConfig();
	}
	
	/**
	 * Returns the plugin version
	 * @return
	 */
	public final String getVersion() {
		return this.getDescription().getVersion();
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
	
	/**
	 * Returns the hook manager for VanishNoPacket
	 * @return
	 */
	public final HookVanishNoPacket getVanishNoPacketHook() {
		return hookVanish;
	}
	
	/**
	 * Returns if there's a new update available
	 * @return
	 */
	public final boolean isUpdate() {
		return new UpdateChecker(this, "http://dev.bukkit.org/server-mods/customstafflist/files.rss").updateNeeded();
	}
	
}