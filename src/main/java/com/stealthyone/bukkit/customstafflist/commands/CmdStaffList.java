package com.stealthyone.bukkit.customstafflist.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.stealthyone.bukkit.customstafflist.BasePlugin;

public final class CmdStaffList implements CommandExecutor {

	private BasePlugin plugin;
	
	public CmdStaffList(BasePlugin plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("version") || args[0].equalsIgnoreCase("v")) {
				sender.sendMessage(ChatColor.GOLD + "CustomStaffList v" + plugin.getVersion() + " by Stealth2800");
				sender.sendMessage(ChatColor.GREEN + "BukkitDev: N/A");
				return true;
			} else if (args[0].equalsIgnoreCase("reload")) {
				if (!sender.hasPermission("customstafflist.reload")) {
					sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
					return true;
				}
				plugin.reloadConfig();
				sender.sendMessage(ChatColor.GOLD + "[CustomStaffList] v" + ChatColor.RED + plugin.getVersion() + " reloaded");
				return true;
			}
		}
		
		if (!sender.hasPermission("customstafflist.check")) {
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
			return true;
		}
		
		List<String> messages = plugin.getMethods().createStaffList();
		
		sender.sendMessage(ChatColor.AQUA + "--Online Staff Members--");
		if (messages == null || messages.size() == 0) {
			sender.sendMessage(ChatColor.RED + "No online staff!");
			return true;
		}
		for (int i = 0; i < messages.size(); i++) {
			sender.sendMessage(messages.get(i));
		}
		return true;
	}

}
