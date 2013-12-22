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
package com.stealthyone.mcb.customuserlist.messages;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public enum UsageMessage {

    STAFFLIST_LIST("/%s list <list alias>");
	
	private String message;
	
	private UsageMessage(String message) {
		this.message = message;
	}
	
	public final void sendTo(CommandSender sender, String label) {
		sender.sendMessage(ChatColor.DARK_RED + "USAGE: " + ChatColor.RED + String.format(message, label));
	}
	
}