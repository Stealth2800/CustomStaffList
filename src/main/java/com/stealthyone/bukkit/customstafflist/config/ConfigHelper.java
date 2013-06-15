package com.stealthyone.bukkit.customstafflist.config;

import com.stealthyone.bukkit.customstafflist.CustomStaffList;

public enum ConfigHelper {

	NOTHING("NOTHING");
	
	private String path;
	
	private ConfigHelper(String path) {
		this.path = path;
	}
	
	public final Object get() {
		return CustomStaffList.getInstance().getConfig().get(path);
	}
	
	public final void set(Object newValue) {
		CustomStaffList.getInstance().getConfig().set(path, newValue);
	}
	
	public final static ConfigBoolean DEBUG = ConfigBoolean.DEBUG;
	public final static ConfigBoolean CHECK_FOR_UPDATES = ConfigBoolean.CHECK_FOR_UPDATES;
	public final static ConfigBoolean LIMIT_TO_ONE_GROUP = ConfigBoolean.LIMIT_TO_ONE_GROUP;
	public final static ConfigBoolean HIDE_VANISHED = ConfigBoolean.HIDE_VANISHED;
	public final static ConfigBoolean SHOW_EMPTY_GROUPS = ConfigBoolean.SHOW_EMPTY_GROUPS;
	public final static ConfigMessageFormat MESSAGE_HEADER = ConfigMessageFormat.HEADER;
	public final static ConfigMessageFormat MESSAGE_NAME = ConfigMessageFormat.NAME;
	public final static ConfigMessageFormat MESSAGE_GROUPNAME = ConfigMessageFormat.GROUPNAME;
	public final static ConfigMessageFormat MESSAGE_FOOTER = ConfigMessageFormat.FOOTER;
	public final static ConfigMessageFormat MESSAGE_NO_ONLINE_STAFF = ConfigMessageFormat.NO_ONLINE_STAFF;
	public final static ConfigMessageFormat MESSAGE_NO_ONLINE_GROUP_MEMBERS = ConfigMessageFormat.NO_ONLINE_GROUP_MEMBERS;
	public final static ConfigBoolean USE_PLAYER_DISP_NAME = ConfigBoolean.USE_PLAYER_DISP_NAME;
	
}