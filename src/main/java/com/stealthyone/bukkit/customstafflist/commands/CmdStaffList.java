package com.stealthyone.bukkit.customstafflist.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.stealthyone.bukkit.customstafflist.CustomStaffList;
import com.stealthyone.bukkit.customstafflist.permissions.PermissionNode;
import com.stealthyone.bukkit.customstafflist.utils.UpdateChecker;

public final class CmdStaffList implements CommandExecutor {

	private CustomStaffList plugin;
	
	public CmdStaffList(CustomStaffList plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length > 0) {
			switch (args[0]) {
				case "version": case "v":
					sender.sendMessage(String.format(ChatColor.GREEN + "CustomStaffList" + ChatColor.GRAY + "-" + ChatColor.GOLD + "v%s", plugin.getVersion()));
					sender.sendMessage(ChatColor.BLUE + "By Stealth2800" + ChatColor.GRAY + "-" + ChatColor.AQUA + "http://stealthyone.com/bukkit");
					sender.sendMessage(ChatColor.DARK_AQUA + "BukkitDev: " + ChatColor.AQUA + "http://bit.ly/10Do1Of");
					if (plugin.isUpdate()) {
						//Update available, notify player
						String newVersion = new UpdateChecker(plugin, "http://dev.bukkit.org/server-mods/customstafflist/files.rss").getVersion();
						sender.sendMessage(ChatColor.RED + "Found different version on BukkitDev! (New: " + newVersion + " | Current: " + plugin.getVersion() + ")");
					}
					return true;
					
				case "reload":
					if (!PermissionNode.RELOAD.isAllowed(sender)) {
						sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
						return true;
					}
					plugin.reloadAll();
					sender.sendMessage(ChatColor.RED + "CustomStaffList v" + plugin.getVersion() + " reloaded.");
					return true;
			}
		}
		
		if (!sender.hasPermission("customstafflist.check")) {
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
			return true;
		}
		
		for (String message : plugin.getMethods().createStaffList()) {
			sender.sendMessage(message);
		}
		return true;
	}

}
