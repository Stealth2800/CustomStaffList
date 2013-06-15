package com.stealthyone.bukkit.customstafflist.permissions;

import org.bukkit.command.CommandSender;

public enum PermissionNode {

	RELOAD;
	
	public final static String PREFIX = "customstafflist.";
	
	private String permission;
	
	private PermissionNode() {
		permission = PREFIX + this.toString().toLowerCase().replace("_", ".");
	}
	
	public final String getPermission() {
		return this.permission;
	}
	
	public final boolean isAllowed(CommandSender sender) {
		return sender.hasPermission(this.permission);
	}
	
	public final static VariablePermissionNode GROUP = VariablePermissionNode.GROUP;
	
}