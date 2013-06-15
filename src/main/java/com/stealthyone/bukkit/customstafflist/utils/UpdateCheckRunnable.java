package com.stealthyone.bukkit.customstafflist.utils;

import com.stealthyone.bukkit.customstafflist.CustomStaffList;
import com.stealthyone.bukkit.customstafflist.CustomStaffList.Log;
import com.stealthyone.bukkit.customstafflist.config.ConfigHelper;
import com.stealthyone.bukkit.stcommonlib.utils.StringUtils;

public final class UpdateCheckRunnable implements Runnable {
		
	private CustomStaffList plugin;
	
	public UpdateCheckRunnable(CustomStaffList plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void run() {
		if ((boolean) ConfigHelper.CHECK_FOR_UPDATES.get()) {
			if (StringUtils.containsMultiple(plugin.getVersion(), "SNAPSHOT", "BETA", "ALPHA", "b")) {
				Log.info("Currently running a snapshot, beta, or alpha build. Update check cancelled.");
				return;
			}
		
			Log.info("Checking for updates...");
			
			UpdateChecker updateChecker = new UpdateChecker(plugin, "http://dev.bukkit.org/bukkit-mods/customstafflist/files.rss");
			
			if (updateChecker.updateNeeded()) {
				Log.info("Found a different version on BukkitDev! (New: " + updateChecker.getVersion() + " | Current: " + plugin.getVersion() + ")");
				Log.info("You can download it from: " + updateChecker.getLink());
			}
		} else {
			//Update checker disable, alert console
			Log.info("Update checker is disabled, enable in config for auto update checking");
			Log.info("You can also check for updates by typing /stafflist version");
		}
	}
	
}