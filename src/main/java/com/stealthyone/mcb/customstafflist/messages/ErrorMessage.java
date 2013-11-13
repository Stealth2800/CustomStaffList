/*
 *               The Building Game - Bukkit Plugin
 * Copyright (C) 2013 Stealth2800 <stealth2800@stealthyone.com>
 *               Website: <http://stealthyone.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.stealthyone.mcb.customstafflist.messages;

import com.stealthyone.mcb.customstafflist.CustomStaffList;
import com.stealthyone.mcb.stbukkitlib.lib.messages.IMessagePath;
import com.stealthyone.mcb.stbukkitlib.lib.messages.MessageRetriever;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public enum ErrorMessage implements IMessagePath {

    INVALID_USERLIST,
	NO_PERMISSION;
	
	private final String PREFIX = "messages.errors.";
	
	private String path;
	private boolean isList;
	
	private ErrorMessage() {
		this(false);
	}
	
	private ErrorMessage(boolean isList) {
		this.path = this.toString().toLowerCase();
		this.isList = isList;
	}
	
	@Override
	public final String getPrefix() {
		return PREFIX;
	}

	@Override
	public final String getMessagePath() {
		return this.path;
	}
	
	@Override
	public final boolean isList() {
		return this.isList;
	}
	
	public final String getFirstLine() {
		return CustomStaffList.getInstance().getMessageManager().getMessage(this)[0];
	}
	
	public final void sendTo(CommandSender sender) {
		MessageRetriever messageRetriever = CustomStaffList.getInstance().getMessageManager();
		String[] messages = messageRetriever.getMessage(this);
		
		for (String message : messages) {
			message = ChatColor.RED + message;
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message.replace("{TAG}", messageRetriever.getTag())));
		}
	}
	
	public final void sendTo(CommandSender sender, String... replacements) {
		MessageRetriever messageRetriever = CustomStaffList.getInstance().getMessageManager();
		String[] messages = messageRetriever.getMessage(this);
		
		for (String message : messages) {
			message = ChatColor.RED + message;
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.RED + String.format(message.replace("{TAG}", messageRetriever.getTag()), (Object[]) replacements)));
		}
	}
	
}