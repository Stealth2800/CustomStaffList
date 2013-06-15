package com.stealthyone.bukkit.customstafflist.config;

import com.stealthyone.bukkit.customstafflist.CustomStaffList;

public enum ConfigBoolean {

	DEBUG("Debug"),
	CHECK_FOR_UPDATES("Check for updates"),
	LIMIT_TO_ONE_GROUP("Limit player appearance to one group"),
	HIDE_VANISHED("Hide vanished players"),
	USE_PLAYER_DISP_NAME("Format.Use player display name"),
	SHOW_EMPTY_GROUPS("Format.Show groups with no one on");
	
	private String path;
	
	private ConfigBoolean(String path) {
		this.path = path;
	}
	
	public final boolean get() {
		return CustomStaffList.getInstance().getConfig().getBoolean(path);
	}
	
	public final void set(boolean newValue) {
		CustomStaffList.getInstance().getConfig().set(path, newValue);
	}
	
}