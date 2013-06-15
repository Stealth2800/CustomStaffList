package com.stealthyone.bukkit.customstafflist.config;

import com.stealthyone.bukkit.customstafflist.CustomStaffList;

public enum ConfigMessageFormat {

	HEADER("Header"),
	NAME("Name"),
	GROUPNAME("GroupName"),
	FOOTER("Footer"),
	NO_ONLINE_STAFF("No online staff message"),
	NO_ONLINE_GROUP_MEMBERS("No online group members message");
	
	private final static String PREFIX = "Format.";
	
	private String path;
	
	private ConfigMessageFormat(String path) {
		this.path = path;
	}
	
	public final String get() {
		return CustomStaffList.getInstance().getConfig().getString(PREFIX + path);
	}
	
}