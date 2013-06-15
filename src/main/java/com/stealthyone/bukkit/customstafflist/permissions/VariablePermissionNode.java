package com.stealthyone.bukkit.customstafflist.permissions;

import org.bukkit.command.CommandSender;

public enum VariablePermissionNode {

	GROUP("group.%s", 1);
	
	private String permission;
	private int variableCount;
	
	private VariablePermissionNode(int variableCount) {
		this.permission = PermissionNode.PREFIX + this.toString().toLowerCase().replace("_", ".");
		this.variableCount = variableCount;
	}
	
	private VariablePermissionNode(String permission, int variableCount) {
		this.permission = PermissionNode.PREFIX + permission;
		this.variableCount = variableCount;
	}
	
	public final boolean isAllowed(CommandSender sender, String... variables) {
		if (variables.length > variableCount || variables.length < variableCount) {
			throw new IllegalArgumentException("Too many arguments for node: " + this.toString() + " - max is " + variableCount);
		}
		
		return sender.hasPermission(String.format(permission, (Object[]) variables));
	}
	
}